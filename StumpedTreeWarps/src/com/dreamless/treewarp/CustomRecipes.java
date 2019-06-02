package com.dreamless.treewarp;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice.MaterialChoice;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.inventory.meta.ItemMeta;

import com.dreamless.laithorn.LaithornUtils;
import com.dreamless.laithorn.api.ItemCrafting;

import de.tr7zw.itemnbtapi.NBTCompound;
import de.tr7zw.itemnbtapi.NBTItem;

//import com.dreamless.treewarp.TreeWarp;

public class CustomRecipes {
	
	public static final String SHEARS_REPAIR_STRING = "shears_repair";
	public static final String SHEARS_CREATE_STRING = "shears_create";
	public static final String BONEMEAL_CREATE_STRING = "bonemeal_create";
	public static List<String> SHEARS_REPAIR_TAGS; 

	public static void registerRecipes() {
		Bukkit.addRecipe(shearsRecipe());
		Bukkit.addRecipe(spawnLeafRecipe());
		Bukkit.addRecipe(bonemealRecipe());
	}

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

		NBTCompound laithorn = nbti.addCompound(ItemCrafting.getTopLevelTag());
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

		NBTCompound laithorn = nbti.addCompound(ItemCrafting.getTopLevelTag());
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

		NBTCompound laithorn = nbti.addCompound(ItemCrafting.getTopLevelTag());
		laithorn.setString("module", "TreeWarp");
		
		NBTCompound treeWarp = nbti.addCompound(ItemCrafting.getTopLevelTag());
		treeWarp.setString("spawn", "spawn");

		item = nbti.getItem();

		return item;
	}

	private static ShapedRecipe shearsRecipe() {
		ItemStack shears = shearsItem();

		NamespacedKey key = new NamespacedKey(TreeWarp.treeWarp, "magicked_shears");

		ShapedRecipe recipe = new ShapedRecipe(key, shears);

		recipe.shape("EEE", "ESE", "EEE");
		recipe.setIngredient('E', Material.FLINT);
		recipe.setIngredient('S', Material.SHEARS);

		return recipe;
	}

	private static ShapedRecipe spawnLeafRecipe() {

		ItemStack leaf = spawnLeafItem();

		NamespacedKey key = new NamespacedKey(TreeWarp.treeWarp, "laithorn_leaf");

		ShapedRecipe recipe = new ShapedRecipe(key, leaf);

		recipe.shape("EEE", "ELE", "EEE");
		recipe.setIngredient('E', Material.FLINT);
		
		ArrayList<Material> leaves = new ArrayList<Material>();
		leaves.add(Material.BIRCH_LEAVES);
		leaves.add(Material.OAK_LEAVES);
		leaves.add(Material.DARK_OAK_LEAVES);
		leaves.add(Material.JUNGLE_LEAVES);
		leaves.add(Material.ACACIA_LEAVES);
		leaves.add(Material.SPRUCE_LEAVES);

		MaterialChoice choices = new MaterialChoice(leaves);
		recipe.setIngredient('L', choices);

		return recipe;

	}
	
	private static ShapelessRecipe bonemealRecipe() {
		
		ItemStack bonemeal = magicBonemealItem();

		NamespacedKey key = new NamespacedKey(TreeWarp.treeWarp, "magicked_bonemeal");

		ShapelessRecipe recipe = new ShapelessRecipe(key, bonemeal);

		recipe.addIngredient(Material.FLINT);
		recipe.addIngredient(Material.BONE_MEAL);

		return recipe;
	}
}
