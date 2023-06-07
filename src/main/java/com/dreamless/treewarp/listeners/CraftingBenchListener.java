package com.dreamless.treewarp.listeners;

import org.bukkit.event.Listener;

public class CraftingBenchListener implements Listener {
/*
	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void onPrepareItemCraftEvent(PrepareItemCraftEvent event) {

		Recipe recipe = event.getRecipe();
		if (recipe == null)
			return; // Ignore if no recipe

		ItemStack result = recipe.getResult();
		CraftingInventory inventory = event.getInventory();

		if (result.isSimilar(CustomRecipes.shearsItem()) || result.isSimilar(CustomRecipes.spawnLeafItem())) {
			if (!Fragment.surroundedByEssence(inventory)) {
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
	*/
}
