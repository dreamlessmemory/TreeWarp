package com.dreamless.treewarp;

import java.util.HashMap;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;

public class CacheHandler {

	private static HashMap<Location, Location> leafToRoot = new HashMap<Location, Location>();
	private static HashMap<String, Location> playerToRoot = new HashMap<String, Location>();

	/*** Updating ***/

	// Both Caches
	
	public static void loadCaches() {
		leafToRoot = DatabaseHandler.getLeafToRootCache();
		playerToRoot = DatabaseHandler.getPlayerToRootCache();
	}
	
	public static void updateCaches(List<BlockState> list, Location location, Player player) {
		// Update player-root cache
		playerToRoot.put(player.getUniqueId().toString(), location);
		
		// Update tree-root cache
		removeUserTree(player);
		addToTreeCache(list, location);
	}

	// LeafToRoot
	public static void removeUserTree(Player player) {
		Location location = playerToRoot.get(player.getUniqueId().toString());
		if (location != null)
			if(leafToRoot.values().remove(location))
				PlayerMessager.debugLog("Removed old tree");
	}

	public static void addToTreeCache(List<BlockState> list, Location location) {
		for (BlockState blockState : list) {
			leafToRoot.put(blockState.getLocation(), location);
		}
	}

	/*** Lookup ***/

	public static Location getWarpLocation(Location location) {
		PlayerMessager.debugLog("Lookup: " +location.toString());
		return leafToRoot.get(location);
	}

	public static boolean containsTree(Location location) {
		return leafToRoot.containsKey(location);
	}
}
