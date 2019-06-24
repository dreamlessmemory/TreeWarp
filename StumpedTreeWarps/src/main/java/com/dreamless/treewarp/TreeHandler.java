package com.dreamless.treewarp;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.TreeType;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import com.dreamless.laithorn.LaithornUtils;
import com.dreamless.laithorn.api.Fragment;
import com.dreamless.nbtapi.NBTCompound;
import com.dreamless.nbtapi.NBTItem;

public class TreeHandler {

	// Checks if the structure that is grown is a valid type for a warp tree
	public static boolean isPotentialTreeType(TreeType type) {
		switch (type) {
		case ACACIA:
		case TALL_BIRCH:
		case BIRCH:
		case DARK_OAK:
		case SMALL_JUNGLE:
		case JUNGLE:
		case REDWOOD:
		case TALL_REDWOOD:
		case SWAMP:
		case TREE:
		case BIG_TREE:
			return true;
		default:
			return false;
		}
	}

	// Checks if the item is part of a potential warp tree
	public static boolean isPotentialTreeBlock(Material material) {
		switch (material) {
		case BIRCH_LEAVES:
		case OAK_LEAVES:
		case JUNGLE_LEAVES:
		case DARK_OAK_LEAVES:
		case ACACIA_LEAVES:
		case SPRUCE_LEAVES:
		case BIRCH_LOG:
		case OAK_LOG:
		case JUNGLE_LOG:
		case DARK_OAK_LOG:
		case ACACIA_LOG:
		case SPRUCE_LOG:
			return true;
		default:
			return false;
		}
	}
	
	public static boolean isPotentialLeaf(Material material) {
		switch (material) {
		case BIRCH_LEAVES:
		case OAK_LEAVES:
		case JUNGLE_LEAVES:
		case DARK_OAK_LEAVES:
		case ACACIA_LEAVES:
		case SPRUCE_LEAVES:
			return true;
		default:
			return false;
		}
	}
	
	public static Material getRelatedLeaf(Material material) {
		switch (material) {
		case BIRCH_LEAVES:
		case BIRCH_LOG:
			return Material.BIRCH_LEAVES;
		case OAK_LEAVES:
		case OAK_LOG:
			return Material.OAK_LEAVES;
		case JUNGLE_LEAVES:
		case JUNGLE_LOG:
			return Material.JUNGLE_LEAVES;
		case DARK_OAK_LEAVES:
		case DARK_OAK_LOG:
			return Material.DARK_OAK_LEAVES;
		case ACACIA_LEAVES:
		case ACACIA_LOG:
			return Material.ACACIA_LEAVES;
		case SPRUCE_LEAVES:
		case SPRUCE_LOG:
			return Material.SPRUCE_LEAVES;
		default:
			return Material.OAK_LEAVES;
		}
	}
	
	public static ItemStack getWarpLeaf(Material material, Player player, Location location) {
		ItemStack leaf = new ItemStack(getRelatedLeaf(material));
		
		/*** Item Meta ***/
		ItemMeta itemMeta = leaf.getItemMeta();
		
		// Set Name
		itemMeta.setDisplayName(LanguageReader.getText("Leaf_Item_Name", player.getName()));
		
		// Set flavor text
		itemMeta.setLore(LaithornUtils.wrapText(LanguageReader.getText("Leaf_Item_Text", player.getName())));
		
		// Set cosmetic enchantment
		itemMeta.addEnchant(Enchantment.DURABILITY, 1, true);
		
		// Apply meta
		leaf.setItemMeta(itemMeta);
		
		/*** NBT ***/
		NBTItem nbti = new NBTItem(leaf);

		NBTCompound laithorn = nbti.addCompound(Fragment.getTopLevelTag());
		laithorn.setString("module", "TreeWarp");
		
		NBTCompound treeWarp = nbti.addCompound("TreeWarp");
		
		treeWarp.setString("world", location.getWorld().getName());
		treeWarp.setString("player", player.getName());
		treeWarp.setString("playerUUID", player.getUniqueId().toString());
		
		leaf = nbti.getItem();
		
		//PlayerMessager.debugLog("Leaf: " + leaf.toString());
		
		return leaf;
	}
	
	public static class LeafRegrow extends BukkitRunnable{
		
		Location location;
		Material material;
		
		public LeafRegrow(Location location, Material material) {
			this.location = location;
			this.material = material;
		}

		@Override
		public void run() {
			location.getWorld().spawnParticle(Particle.VILLAGER_HAPPY, location.clone().add(0.5, 0.5, 0.5), 8, 0.5, 0.5, 0.5);
			location.getWorld().getBlockAt(location).setType(material);
		}
		
	}
}
