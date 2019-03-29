package com.dreamless.treewarp;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;

import de.tr7zw.itemnbtapi.NBTCompound;
import de.tr7zw.itemnbtapi.NBTItem;

//import com.dreamless.treewarp.TreeWarp;

public class CustomRecipes {
	
	public static void registerRecipes() {
		
		ItemStack shears = shearsItem();
		
		NamespacedKey key = new NamespacedKey(TreeWarp.treeWarp, "magicked_shears");
		
		ShapedRecipe recipe = new ShapedRecipe(key, shears);
		
		recipe.shape("EEE", "ESE", "EEE");
		recipe.setIngredient('E', Material.BONE_MEAL);
		recipe.setIngredient('S', Material.SHEARS);
		
		Bukkit.addRecipe(recipe);
		
	}
	
	public static ItemStack essenceItem() {
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
		
		nbti.addCompound("Laithorn");
		//NBTCompound laithorn = nbti.addCompound("Laithorn");
		//laithorn.setString("laithorn", "laithorn");
		
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
	
	public static ItemStack spawnLeafItem() {
		ItemStack item = new ItemStack(Material.BIRCH_LEAVES);
		
		/*** Item Meta ***/
		ItemMeta itemMeta = item.getItemMeta();
		
		// Set Name
		itemMeta.setDisplayName(LanguageReader.getText("Spawn_Leaf_Item_Name"));
		
		// Set flavor text
		itemMeta.setLore(TreeWarpUtils.wrapText(LanguageReader.getText("Spawn_Leaf_Item_Text")));
		
		// Set cosmetic enchantment
		itemMeta.addEnchant(Enchantment.DURABILITY, 1, true);
		
		// Apply meta
		item.setItemMeta(itemMeta);
		
		/*** NBT ***/
		NBTItem nbti = new NBTItem(item);
		
		NBTCompound treeWarpCompound = nbti.addCompound("TreeWarp");
		
		treeWarpCompound.setString("spawn", "spawn");	
		
		item = nbti.getItem();
		
		return item;
	}
}
