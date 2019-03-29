package com.dreamless.treewarp.listeners;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

import com.dreamless.treewarp.CustomRecipes;
import com.dreamless.treewarp.PlayerMessager;

import de.tr7zw.itemnbtapi.NBTItem;

public class AnvilListener implements Listener {

	public static int REPAIR_RATE = 50;

	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void onAnvilPrepare(PrepareAnvilEvent event) {
		AnvilInventory inventory = event.getInventory();

		ItemStack leftSide = inventory.getItem(0);
		if (leftSide == null || leftSide.getType() != Material.SHEARS) // Ignore if not Shears
			return;

		ItemStack rightSide = inventory.getItem(1);
		if (rightSide == null || rightSide.getType() != Material.BONE_MEAL) // Ignore if not bonemeal
			return;

		NBTItem bonemealNBT = new NBTItem(rightSide);

		if (!bonemealNBT.hasKey("Laithorn")) // Ignore if not essence
			return;

		/*** Processing ***/

		ItemStack result;

		NBTItem shearsNBT = new NBTItem(leftSide);
		if (shearsNBT.hasKey("TreeWarp")) { // Repairing
			result = repairShears(leftSide, rightSide);
		} else {
			return;
		}
		
		// Rename handling
		if (!inventory.getRenameText().isEmpty()) {
			ItemMeta meta = result.getItemMeta();
			meta.setDisplayName(inventory.getRenameText());
			result.setItemMeta(meta);
		}
		
		event.setResult(result);
	}
	
	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void onShearsPickup(InventoryClickEvent event) {
		if(!(event.getInventory() instanceof AnvilInventory)) { //ignore if not an anvil
			return;
		}
		
		if(event.getSlot() != 2) { // Did not click on result slot
			return;
		}
		
		AnvilInventory inventory = (AnvilInventory)event.getInventory();
		ItemStack item = inventory.getItem(2);
		
		if(item == null || !item.isSimilar(CustomRecipes.shearsItem())) { //Ignore if not magick shears
			PlayerMessager.debugLog("Not shears");
			return;
		}
		
		/*** Processing ***/
		
		event.setCancelled(true);
		
		// Clear items
		inventory.setItem(0, null);
		inventory.setItem(1, null);
		inventory.setItem(2, null);
		
		//Give item to cursor
		
		Player player = (Player)event.getWhoClicked();
		player.getInventory().addItem(item);
		player.updateInventory();
		//player.getInventory().setItemInMainHand(item);
		//event.setCurrentItem(item);
		
	}

	private ItemStack repairShears(ItemStack shears, ItemStack essence) {

		ItemStack repairedShears = shears.clone();
		Damageable damageable = (Damageable) shears.getItemMeta();

		int repairValue = essence.getAmount() * REPAIR_RATE;
		PlayerMessager.debugLog("Repairing by " + repairValue);
		damageable.setDamage(Math.max(damageable.getDamage() - repairValue, 0)); // 238 is the max durability of shears

		repairedShears.setItemMeta((ItemMeta) damageable);

		return repairedShears;
	}
	
}
