package com.dreamless.treewarp.listeners;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.world.StructureGrowEvent;
import org.bukkit.inventory.ItemStack;

import com.dreamless.treewarp.DatabaseHandler;
import com.dreamless.treewarp.PlayerMessager;
import com.dreamless.treewarp.TreeWarpUtils;

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
		if (!TreeWarpUtils.isValidTree(event.getSpecies()))
			return; // Ignore if not valid tree type

		// Get item in hand
		ItemStack inHand = player.getInventory().getItemInMainHand();
		if (inHand.getType() != Material.BONE_MEAL)
			return; // No bone meal in hand.

		NBTItem nbti = new NBTItem(inHand);

		if (nbti.hasKey("TreeWarp")) {
			DatabaseHandler.addTreeBlocks(event.getBlocks(), event.getLocation(), player);
		}
	}

}
