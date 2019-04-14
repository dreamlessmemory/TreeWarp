package com.dreamless.treewarp.listeners;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;

import com.dreamless.laithorn.RecipeHandler;
import com.dreamless.treewarp.CustomRecipes;

import de.tr7zw.itemnbtapi.NBTItem;

public class CraftingBenchListener implements Listener {

	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void onPrepareItemCraftEvent(PrepareItemCraftEvent event) {
		
		Recipe recipe = event.getRecipe();
		if(recipe == null)
			return; // Ignore if no recipe
		
		if(!recipe.getResult().isSimilar(CustomRecipes.shearsItem()) && !recipe.getResult().isSimilar(CustomRecipes.spawnLeafItem())) {
			return; // Ignore if it's not the shears or leaf recipe
		}
		CraftingInventory inventory = event.getInventory();
		
		if(!surroundedByEssence(inventory)) {
			inventory.setResult(new ItemStack(Material.AIR)); // Effectively cancel event if not the right bonemeal
		}
		
		if(!RecipeHandler.canDoAction((Player) event.getView().getPlayer(), "create_shears")) {
			inventory.setResult(new ItemStack(Material.AIR)); // Effectively cancel event if not the right level
		}
	}

	private static boolean surroundedByEssence(CraftingInventory inventory) {
		ItemStack[] matrix = inventory.getMatrix();
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
