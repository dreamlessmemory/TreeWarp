package com.dreamless.treewarp.listeners;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.ItemStack;

import com.dreamless.treewarp.CustomRecipes;

import de.tr7zw.itemnbtapi.NBTItem;

public class CraftingBenchListener implements Listener{

	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void onPrepareItemCraftEvent(PrepareItemCraftEvent event) {
		CraftingInventory inventory = event.getInventory();
		ItemStack[] matrix = inventory.getMatrix();

		if (matrix.length < 9)
			return; // Not crafting bench, ignore

		ItemStack center = matrix[4];
		if (center == null || center.getType() != Material.SHEARS)
			return; // no shears in center
		
		if(!surroundedByEssence(matrix))
			return; // not surrounded by Essence
		
		inventory.setResult(CustomRecipes.shearsItem());
	}

	private static boolean surroundedByEssence(ItemStack[] matrix) {
		for (int i = 0; i < matrix.length; i++) {
			if (i == 4)
				continue; // ignore the center
			if (!isEssence(matrix[i]))
				return false;
		}

		return true;
	}

	private static boolean isEssence(ItemStack item) {
		if (item == null || item.getType() == Material.AIR)
			return false;
		NBTItem nbti = new NBTItem(item);
		return nbti.hasKey("Laithorn");
	}

}
