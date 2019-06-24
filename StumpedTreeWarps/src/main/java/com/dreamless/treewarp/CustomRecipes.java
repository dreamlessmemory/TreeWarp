package com.dreamless.treewarp;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.dreamless.laithorn.LaithornUtils;
import com.dreamless.laithorn.api.Fragment;
import com.dreamless.nbtapi.NBTCompound;
import com.dreamless.nbtapi.NBTItem;

//import com.dreamless.treewarp.TreeWarp;

public class CustomRecipes {
	
	public static final String SHEARS_REPAIR_STRING = "shears_repair";
	public static final String SHEARS_CREATE_STRING = "shears_create";
	public static final String BONEMEAL_CREATE_STRING = "bonemeal_create";
	public static List<String> SHEARS_REPAIR_TAGS; 

	public static ItemStack magicBonemealItem() {
		ItemStack item = new ItemStack(Material.BONE_MEAL);

		/*** Item Meta ***/
		ItemMeta itemMeta = item.getItemMeta();

		// Set Name
		itemMeta.setDisplayName(LanguageReader.getText("Growth_Item_Name"));

		// Set flavor text
		itemMeta.setLore(LaithornUtils.wrapText(LanguageReader.getText("Growth_Item_Text")));

		// Set cosmetic enchantment
		itemMeta.addEnchant(Enchantment.MENDING, 1, true);

		// Apply meta
		item.setItemMeta(itemMeta);

		/*** NBT ***/
		NBTItem nbti = new NBTItem(item);

		NBTCompound laithorn = nbti.addCompound(Fragment.getTopLevelTag());
		laithorn.setString("module", "TreeWarp");

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
		itemMeta.setLore(LaithornUtils.wrapText(LanguageReader.getText("Shears_Item_Text")));

		// Set cosmetic enchantment
		itemMeta.addEnchant(Enchantment.DURABILITY, 1, true);

		// Apply meta
		item.setItemMeta(itemMeta);

		/*** NBT ***/
		NBTItem nbti = new NBTItem(item);

		NBTCompound laithorn = nbti.addCompound(Fragment.getTopLevelTag());
		laithorn.setString("module", "TreeWarp");
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
		itemMeta.setLore(LaithornUtils.wrapText(LanguageReader.getText("Spawn_Leaf_Item_Text")));

		// Set cosmetic enchantment
		itemMeta.addEnchant(Enchantment.DURABILITY, 1, true);

		// Apply meta
		item.setItemMeta(itemMeta);

		/*** NBT ***/
		NBTItem nbti = new NBTItem(item);

		NBTCompound laithorn = nbti.addCompound(Fragment.getTopLevelTag());
		laithorn.setString("module", "TreeWarp");
		
		NBTCompound treeWarp = nbti.addCompound("TreeWarp");
		treeWarp.setString("spawn", "spawn");

		item = nbti.getItem();

		return item;
	}
}
