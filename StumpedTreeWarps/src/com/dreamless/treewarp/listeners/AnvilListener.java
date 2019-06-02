package com.dreamless.treewarp.listeners;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.PrepareAnvilEvent;

import com.dreamless.laithorn.api.ItemRepair;
import com.dreamless.treewarp.CustomRecipes;

public class AnvilListener implements Listener {

	private static int REPAIR_RATE = 50;
	private static int REPAIR_EXP_GAIN = 3;

	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
	public void onAnvilPrepare(PrepareAnvilEvent event) {
		if(ItemRepair.anvilPrepareCheck(event, Material.SHEARS, CustomRecipes.SHEARS_REPAIR_STRING, null)) {
			event.setResult(ItemRepair.generateRepairedItem(event.getInventory(), REPAIR_RATE));
		}
	}

	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
	public void onShearsPickup(InventoryClickEvent event) {
		ItemRepair.anvilPickupCheck(Material.SHEARS, event, REPAIR_RATE, REPAIR_EXP_GAIN);
	}

	public static final int getRepairRate() {
		return REPAIR_RATE;
	}

	public static final int getRepairExpGain() {
		return REPAIR_EXP_GAIN;
	}

	public static final void setRepairRate(int repairRate) {
		REPAIR_RATE = repairRate;
	}

	public static final void setRepairExpGain(int repairExpGain) {
		REPAIR_EXP_GAIN = repairExpGain;
	}
}
