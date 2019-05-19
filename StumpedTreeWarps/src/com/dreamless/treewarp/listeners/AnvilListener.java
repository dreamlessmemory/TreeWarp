package com.dreamless.treewarp.listeners;

import java.util.Arrays;
import java.util.List;

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

import com.dreamless.laithorn.api.ItemCrafting;
import com.dreamless.laithorn.api.ItemRepair;
import com.dreamless.laithorn.api.RequirementsHandler;
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
		if (rightSide == null || rightSide.getType() != ItemCrafting.getFragmentMaterial()) // Ignore if not bonemeal
			return;

		NBTItem bonemealNBT = new NBTItem(rightSide);

		if (!bonemealNBT.hasKey("Laithorn")) // Ignore if not essence
			return;
		
		if(!RequirementsHandler.canDoAction((Player) event.getView().getPlayer(), CustomRecipes.SHEARS_REPAIR_STRING, null)) {
			return;
		}

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
		
		//Exp Event
		//int expGain = 10;
		//Bukkit.getPluginManager().callEvent(new PlayerExperienceGainEvent(player, expGain, GainType.SMITHING, true));
		
	}

	private ItemStack repairShears(ItemStack shears, ItemStack essence) {

		ItemStack repairedShears = shears.clone();
		Damageable damageable = (Damageable) shears.getItemMeta();

		List<String> hardCodedTags = Arrays.asList("FERROUS", "ARBOREAL", "FLORAL");
		int repairValue = ItemRepair.getRepairValue(repairedShears, REPAIR_RATE, hardCodedTags);
		PlayerMessager.debugLog("Repairing by " + repairValue);
		damageable.setDamage(Math.max(damageable.getDamage() - repairValue, 0)); // 238 is the max durability of shears

		repairedShears.setItemMeta((ItemMeta) damageable);

		return repairedShears;
	}
	
}
