package com.dreamless.treewarp.listeners;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.world.StructureGrowEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.checkerframework.checker.units.qual.s;

import com.dreamless.treewarp.CacheHandler;
import com.dreamless.treewarp.DatabaseHandler;
import com.dreamless.treewarp.EffectHandler;
import com.dreamless.treewarp.LanguageReader;
import com.dreamless.treewarp.PlayerMessager;
import com.dreamless.treewarp.TeleportHandler;
import com.dreamless.treewarp.TreeHandler;
import com.dreamless.treewarp.TreeWarp;

import de.tr7zw.itemnbtapi.NBTCompound;
import de.tr7zw.itemnbtapi.NBTItem;

public class BlockListener implements Listener {

	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void onTreeGrowth(StructureGrowEvent event) {

		if (!event.isFromBonemeal())
			return;// Ignore if not bonemeal

		Player player = event.getPlayer();
		if (player == null)
			return; // Ignore if not player made

		// Get Tree type
		if (!TreeHandler.isPotentialTreeType(event.getSpecies()))
			return; // Ignore if not valid tree type

		// Get item in hand
		ItemStack inHand = player.getInventory().getItemInMainHand();
		if (inHand.getType() != Material.BONE_MEAL)
			return; // No bone meal in hand.

		NBTItem nbti = new NBTItem(inHand);

		if (nbti.hasKey("TreeWarp")) {

			/*** Actual processing here ***/

			PlayerMessager.debugLog("Processing Tree");

			// Cache
			CacheHandler.updateCaches(event.getBlocks(), event.getLocation(), player);

			// Database
			DatabaseHandler.addTreeBlocks(event.getBlocks(), event.getLocation(), player);
		}
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
			} // else PlayerMessager.debugLog("Nope?");
		} // else PlayerMessager.debugLog("YA Nope?");

		/*** Actual processing here ***/

		Location location = clickedBlock.getLocation();
		Location warpLocation = CacheHandler.getWarpLocation(location);

		if (warpLocation == null) {// This means this is not associated with any tree
			// PlayerMessager.debugLog("No tree here");
			return; // TODO: Figure out what to do here
		}

		event.setCancelled(true);

		if (harvestingLeaves) {
			// PlayerMessager.debugLog("Drop?");
			location.getWorld().dropItem(player.getEyeLocation(),
					TreeHandler.getWarpLeaf(clickedBlock.getType(), player, warpLocation));
		}
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onLeafDrop(PlayerDropItemEvent event) {
		Item item = event.getItemDrop();
		ItemStack itemStack = event.getItemDrop().getItemStack();

		if (!TreeHandler.isPotentialLeaf(itemStack.getType())) {
			PlayerMessager.debugLog("Not leaf");
			return;
		}
		
		NBTItem nbti = new NBTItem(itemStack);
		if (!nbti.hasKey("TreeWarp")) {
			PlayerMessager.debugLog("Not warp leaf");
			return;
		} // else PlayerMessager.debugLog("Nope?");

		/*** Actual processing here ***/

		// Variables
		Player player = event.getPlayer();
		
		// Get location
		NBTCompound treeWarp = nbti.getCompound("TreeWarp");
		Location destination = new Location(Bukkit.getWorld(treeWarp.getString("world")), treeWarp.getDouble("x"), treeWarp.getDouble("y"), treeWarp.getDouble("z"));
		
		// Remove leaf item
		item.setPickupDelay(1000);
		new BukkitRunnable() {
			
			@Override
			public void run() {
				item.remove();
			}
		}.runTaskLater(TreeWarp.treeWarp, 20);
		
		
		// Inform player
		PlayerMessager.msg(player, LanguageReader.getText("Teleport_Prepare"));
		new EffectHandler.EffectRunnable(player.getLocation(), 60, 3, "ready", itemStack.getType().createBlockData()).runTaskTimer(TreeWarp.treeWarp, 0, 3);
		new TeleportHandler(event.getPlayer(), destination, player.getLocation(), itemStack.getType()).runTaskLater(TreeWarp.treeWarp, 60);
	}
}
