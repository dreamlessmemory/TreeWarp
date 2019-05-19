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

import com.dreamless.laithorn.api.FragmentRarity;
import com.dreamless.laithorn.api.ItemCrafting;
import com.dreamless.laithorn.api.RequirementsHandler;
import com.dreamless.treewarp.CustomRecipes;

public class CraftingBenchListener implements Listener {

	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void onPrepareItemCraftEvent(PrepareItemCraftEvent event) {

		Recipe recipe = event.getRecipe();
		if (recipe == null)
			return; // Ignore if no recipe

		ItemStack result = recipe.getResult();
		CraftingInventory inventory = event.getInventory();

		if (result.isSimilar(CustomRecipes.shearsItem()) || result.isSimilar(CustomRecipes.spawnLeafItem())) {
			if (!ItemCrafting.surroundedByEssence(inventory)) {
				inventory.setResult(new ItemStack(Material.AIR)); // Effectively cancel event if not the right bonemeal
			}

			if (!RequirementsHandler.canDoAction((Player) event.getView().getPlayer(),
					CustomRecipes.SHEARS_CREATE_STRING, null)) {
				inventory.setResult(new ItemStack(Material.AIR)); // Effectively cancel event if not the right level
			}
		} else if (result.isSimilar(CustomRecipes.magicBonemealItem())) {
			if (!ItemCrafting.checkForCorrectFragment(inventory, FragmentRarity.DULL))
				inventory.setResult(new ItemStack(Material.AIR));
			if (!RequirementsHandler.canDoAction((Player) event.getView().getPlayer(),
					CustomRecipes.BONEMEAL_CREATE_STRING, null)) {
				inventory.setResult(new ItemStack(Material.AIR)); // Effectively cancel event if not the right level
			}
		} else {
			return;
		}
	}
}
