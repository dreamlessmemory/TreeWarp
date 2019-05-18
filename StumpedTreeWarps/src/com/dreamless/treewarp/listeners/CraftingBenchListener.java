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

import com.dreamless.laithorn.LaithornsGrace;
import com.dreamless.laithorn.RequirementsHandler;
import com.dreamless.treewarp.CustomRecipes;

import de.tr7zw.itemnbtapi.NBTCompound;
import de.tr7zw.itemnbtapi.NBTItem;

public class CraftingBenchListener implements Listener {

	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void onPrepareItemCraftEvent(PrepareItemCraftEvent event) {

		Recipe recipe = event.getRecipe();
		if (recipe == null)
			return; // Ignore if no recipe

		ItemStack result = recipe.getResult();
		CraftingInventory inventory = event.getInventory();

		if (result.isSimilar(CustomRecipes.shearsItem()) || result.isSimilar(CustomRecipes.spawnLeafItem())) {
			if (!surroundedByEssence(inventory)) {
				inventory.setResult(new ItemStack(Material.AIR)); // Effectively cancel event if not the right bonemeal
			}

			if (!RequirementsHandler.canDoAction((Player) event.getView().getPlayer(),
					CustomRecipes.SHEARS_CREATE_STRING, null)) {
				inventory.setResult(new ItemStack(Material.AIR)); // Effectively cancel event if not the right level
			}
		} else if (result.isSimilar(CustomRecipes.magicBonemealItem())) {
			if (!checkForCorrectFragment(inventory))
				inventory.setResult(new ItemStack(Material.AIR));
			if (!RequirementsHandler.canDoAction((Player) event.getView().getPlayer(),
					CustomRecipes.BONEMEAL_CREATE_STRING, null)) {
				inventory.setResult(new ItemStack(Material.AIR)); // Effectively cancel event if not the right level
			}
		} else {
			return;
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

	private static boolean checkForCorrectFragment(CraftingInventory inventory) {
		ItemStack[] matrix = inventory.getMatrix();
		for (int i = 0; i < matrix.length; i++) {
			ItemStack itemStack = matrix[i];
			if (itemStack == null)
				continue;
			if (itemStack.getType() == LaithornsGrace.getFragmentMaterial()) {
				NBTItem nbti = new NBTItem(itemStack);
				NBTCompound laithorn = nbti.getCompound("Laithorn");
				if (laithorn == null)
					return false;
				for (String key : laithorn.getKeys()) {
					if (laithorn.getString(key).equalsIgnoreCase("DULL"))
						return true;
				}
			}
		}
		return false;
	}

	private static boolean isEssence(ItemStack item) {
		if (item == null || item.getType() == Material.AIR)
			return false;
		NBTItem nbti = new NBTItem(item);
		return nbti.hasKey("Laithorn");
	}

}
