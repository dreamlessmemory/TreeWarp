package com.dreamless.treewarp.listeners;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.world.StructureGrowEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import com.dreamless.treewarp.CacheHandler;
import com.dreamless.treewarp.DatabaseHandler;
import com.dreamless.treewarp.EffectHandler;
import com.dreamless.treewarp.LanguageReader;
import com.dreamless.treewarp.PlayerMessager;
import com.dreamless.treewarp.TeleportHandler;
import com.dreamless.treewarp.TreeHandler;
import com.dreamless.treewarp.TreeWarp;
import com.dreamless.treewarp.TreeWarpUtils;

import de.tr7zw.itemnbtapi.NBTCompound;
import de.tr7zw.itemnbtapi.NBTItem;

public class BlockListener implements Listener {

	public static int durabilityLoss = 0;

	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void onTreeGrowth(StructureGrowEvent event) {

		if (!event.isFromBonemeal())
			return;// Ignore if not bonemeal

		Player player = event.getPlayer();
		if (player == null)
			return; // Ignore if not player made

		if (!player.hasPermission("treewarp.user")) {
			PlayerMessager.msg(player, LanguageReader.getText("Error_NoPermissions_Action"));
		}

		// Get Tree type
		if (!TreeHandler.isPotentialTreeType(event.getSpecies()))
			return; // Ignore if not valid tree type

		// Get item in hand
		ItemStack inHand = player.getInventory().getItemInMainHand();
		if (inHand.getType() != Material.BONE_MEAL)
			return; // No bone meal in hand.

		NBTItem nbti = new NBTItem(inHand);

		if (!nbti.hasKey("TreeWarp"))
			return;

		if (!player.hasPermission("treewarp.user")) {
			PlayerMessager.msg(player, LanguageReader.getText("Error_NoPermissions_Action"));
			return;
		}

		/*** Actual processing here ***/

		PlayerMessager.debugLog("Processing Tree");

		// Cache
		CacheHandler.updateCaches(event.getBlocks(), event.getLocation(), player);

		// Database
		DatabaseHandler.addTreeBlocks(event.getBlocks(), event.getLocation(), player);

		// Messaging
		PlayerMessager.msg(player, LanguageReader.getText("Tree_Grown"));
		
		// Sound Effects
		player.getWorld().playSound(event.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onBlockBreak(BlockBreakEvent event) {
		Block clickedBlock = event.getBlock();
		if (!TreeHandler.isPotentialTreeBlock(clickedBlock.getType())) {
			// PlayerMessager.debugLog("Not potentially a tree");
			return; // Ignore if not tree
		}

		Player player = event.getPlayer();

		// Check if using shears

		boolean harvestingLeaves = false;

		ItemStack item = player.getInventory().getItemInMainHand();
		if (item.getType() == Material.SHEARS) {
			NBTItem nbti = new NBTItem(item);
			if (nbti.hasKey("TreeWarp")) {
				// PlayerMessager.debugLog("Leaf harvesting");
				harvestingLeaves = true;

				// Handle degradation

				Damageable itemMeta = (Damageable) item.getItemMeta();
				itemMeta.setDamage(itemMeta.getDamage() + durabilityLoss);
				item.setItemMeta((ItemMeta) itemMeta);
			} // else PlayerMessager.debugLog("Nope?");
		} // else PlayerMessager.debugLog("YA Nope?");

		/*** Actual processing here ***/

		Location location = clickedBlock.getLocation();
		Location warpLocation = CacheHandler.getWarpLocation(location);

		if (warpLocation == null) {// This means this is not associated with any tree
			return;
		}

		if (!player.hasPermission("treewarp.user")) {
			PlayerMessager.msg(player, LanguageReader.getText("Error_NoPermissions_Deny"));
			event.setCancelled(true);
			return;
		}

		if (harvestingLeaves) { // Harvest a leaf

			event.setCancelled(true);
			new TreeHandler.LeafRegrow(location, clickedBlock.getType()).runTaskLater(TreeWarp.treeWarp, 20);
			
			location.getWorld().dropItem(location,
					TreeHandler.getWarpLeaf(clickedBlock.getType(), player, warpLocation));
			location.getWorld().getBlockAt(location).setType(Material.AIR);

		} else {
			
			location.getWorld().playSound(clickedBlock.getLocation(), Sound.BLOCK_FIRE_EXTINGUISH, 1, 1);
			location.getWorld().spawnParticle(Particle.END_ROD, location.clone().add(0.5, 0.5, 0.5), 8, 0.5, 0.5, 0.5);
			
			if (TreeHandler.isPotentialLeaf(clickedBlock.getType())) {// Remove just the leaf
				CacheHandler.removeLeafFromCache(location);
				DatabaseHandler.removeLeafBlock(location);
				PlayerMessager.msg(player, LanguageReader.getText("Tree_Lost_Leaf"));
			} else {
				CacheHandler.removeTreeFromCache(warpLocation);
				DatabaseHandler.removeTree(TreeWarpUtils.serializeLocation(warpLocation));
				PlayerMessager.msg(player, LanguageReader.getText("Tree_Lost_Tree"));
			}
		}
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onLeafDrop(PlayerDropItemEvent event) {
		Item item = event.getItemDrop();
		ItemStack itemStack = event.getItemDrop().getItemStack();

		if (!TreeHandler.isPotentialLeaf(itemStack.getType())) {
			// PlayerMessager.debugLog("Not leaf");
			return;
		}

		NBTItem nbti = new NBTItem(itemStack);
		if (!nbti.hasKey("TreeWarp")) {
			// PlayerMessager.debugLog("Not warp leaf");
			return;
		} // else PlayerMessager.debugLog("Nope?");

		// Variables
		Player player = event.getPlayer();

		if (!player.hasPermission("treewarp.user")) {
			PlayerMessager.msg(player, LanguageReader.getText("Error_NoPermissions_Action"));
			return;
		}

		/*** Actual processing here ***/

		// Get location
		NBTCompound treeWarp = nbti.getCompound("TreeWarp");

		Location destination;
		boolean spawn = treeWarp.hasKey("spawn");
		if (spawn) {
			destination = TeleportHandler.getSpawnWarpDestination();
			if (destination == null) {
				PlayerMessager.msg(player, LanguageReader.getText("Teleport_No_Spawn"));
				return;
			}
		} else {
			destination = CacheHandler.getWarpLocation(treeWarp.getString("playerUUID"));
			
			if(destination == null) {
				PlayerMessager.msg(player, LanguageReader.getText("Teleport_No_Tree"));
				return;
			}
			
			//= new Location(Bukkit.getWorld(treeWarp.getString("world")), treeWarp.getDouble("x"),
			//		treeWarp.getDouble("y"), treeWarp.getDouble("z"));
		}
		// Remove leaf item
		item.setPickupDelay(1000);
		new BukkitRunnable() {

			@Override
			public void run() {
				item.remove();
			}
		}.runTaskLater(TreeWarp.treeWarp, 20);

		// Inform player
		PlayerMessager.msg(player, LanguageReader.getText(spawn? "Teleport_Prepare_Spawn" : "Teleport_Prepare", treeWarp.getString("player")));
		
		player.getWorld().playSound(player.getLocation(), Sound.BLOCK_PORTAL_TRAVEL, 0.5f, 1.25f);
		
		//Delayed handlers
		new EffectHandler.EffectRunnable(player.getLocation(), 60, 1, "ready").runTaskTimer(TreeWarp.treeWarp, 0, 1);
		new TeleportHandler(event.getPlayer(), destination, player.getLocation(), treeWarp.hasKey("spawn"), treeWarp.getString("player"))
				.runTaskLater(TreeWarp.treeWarp, 60);
	}
}
