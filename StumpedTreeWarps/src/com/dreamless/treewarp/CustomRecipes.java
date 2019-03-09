package com.dreamless.treewarp;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;

import de.tr7zw.itemnbtapi.NBTItem;

//import com.dreamless.treewarp.TreeWarp;

public class CustomRecipes {
	
	public static void registerRecipes() {
		Bukkit.addRecipe(bonemealRecipe());
	}

	private static ShapedRecipe bonemealRecipe() {
		ItemStack item = new ItemStack(Material.BONE_MEAL);

		NamespacedKey key = new NamespacedKey(TreeWarp.treeWarp, "laithorn_essence");
		return new ShapedRecipe(key, item);
	}
	
	public static ItemStack bonemealItem() {
		ItemStack item = new ItemStack(Material.BONE_MEAL);
		
		/*** Item Meta ***/
		ItemMeta itemMeta = item.getItemMeta();
		
		// Set Name
		itemMeta.setDisplayName(LanguageReader.getText("Growth_Item_Name"));
		
		// Set flavor text
		itemMeta.setLore(TreeWarpUtils.wrapText(LanguageReader.getText("Growth_Item_Text")));
		
		// Set cosmetic enchantment
		itemMeta.addEnchant(Enchantment.MENDING, 1, true);
		
		// Apply meta
		item.setItemMeta(itemMeta);
		
		/*** NBT ***/
		NBTItem nbti = new NBTItem(item);
		
		nbti.addCompound("TreeWarp");
		
		item = nbti.getItem();
		
		return item;
	}

	public static ItemStack shearsItem() {
		ItemStack item = new ItemStack(Material.SHEARS);
		
		/*** Item Meta ***/
		ItemMeta itemMeta = item.getItemMeta();
		
		// Set Name
		itemMeta.setDisplayName(LanguageReader.getText("Shears_Item_Name"));
		
		// Set flavor text
		itemMeta.setLore(TreeWarpUtils.wrapText(LanguageReader.getText("Shears_Item_Text")));
		
		// Set cosmetic enchantment
		itemMeta.addEnchant(Enchantment.DURABILITY, 1, true);
		
		// Apply meta
		item.setItemMeta(itemMeta);
		
		/*** NBT ***/
		NBTItem nbti = new NBTItem(item);
		
		nbti.addCompound("TreeWarp");
		
		item = nbti.getItem();
		
		return item;
	}
}
