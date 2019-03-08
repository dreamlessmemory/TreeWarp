package com.dreamless.treewarp;

import org.bukkit.Material;
import org.bukkit.TreeType;

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
	
}
