package com.dreamless.treewarp.listeners;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.world.StructureGrowEvent;
import org.bukkit.inventory.ItemStack;

import com.dreamless.treewarp.CacheHandler;
import com.dreamless.treewarp.DatabaseHandler;
import com.dreamless.treewarp.TreeHandler;

import de.tr7zw.itemnbtapi.NBTItem;

public class BlockListener implements Listener {

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
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

			// Cache
			CacheHandler.updateCaches(event.getBlocks(), event.getLocation(), player);

			// Database
			DatabaseHandler.addTreeBlocks(event.getBlocks(), event.getLocation(), player);
		}
	}

	public void onShearsUse(PlayerInteractEvent event) {
		// Check if right click
		if (event.getAction() != Action.RIGHT_CLICK_BLOCK)
			return; // Ignore if not right click

		// Check if using shears
		ItemStack item = event.getItem();
		if (item.getType() != Material.SHEARS)
			return; // Ignore if not shears
		
		Block clickedBlock = event.getClickedBlock();
		if(!TreeHandler.isPotentialTreeBlock(clickedBlock.getType()))
			return; // Ignore if not tree
		
		NBTItem nbti = new NBTItem(item);
		if (!nbti.hasKey("TreeWarp"))
			return; // Ignore if not magic shears
		
		/*** Actual processing here ***/
		
		
	}

}
