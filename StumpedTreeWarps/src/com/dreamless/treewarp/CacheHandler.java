package com.dreamless.treewarp;

import java.util.Collection;
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

		// Update tree-root cache
		removeTreeFromCache(playerToRoot.get(player.getUniqueId().toString()));
		addToTreeCache(list, location);

		// Update player-root cache
		playerToRoot.put(player.getUniqueId().toString(), location);

	}

	// Remove Leaf from leafToRootCache
	public static void removeLeafFromCache(Location location) {
		leafToRoot.remove(location);
	}

	// LeafToRoot
	public static void removeTreeFromCache(Location location) {
		if (location != null) {
			// Remove from LeafToRoot
			while (leafToRoot.values().remove(location))
				//PlayerMessager.debugLog("Removed old tree from leaf cache");
			
			// Remove from playerToRoot
			playerToRoot.values().remove(location);
			//PlayerMessager.debugLog("Removed old tree from player cache");
		}
	}
	
	public static Location removePlayerFromCache(Player player) {
		Location centerLocation = playerToRoot.remove(player.getUniqueId().toString());
		removeTreeFromCache(centerLocation);
		
		return centerLocation;
	}

	public static void addToTreeCache(List<BlockState> list, Location location) {
		for (BlockState blockState : list) {
			leafToRoot.put(blockState.getLocation(), location);
		}
	}
	
	

	/*** Lookup ***/

	public static Location getWarpLocation(Location location) {
		//PlayerMessager.debugLog("Lookup: " + location.toString());
		return leafToRoot.get(location);
	}

	public static boolean containsTree(Location location) {
		return leafToRoot.containsKey(location);
	}

	public static Collection<Location> getWarpLocations() {
		return playerToRoot.values();
	}
}
