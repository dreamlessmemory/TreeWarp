package com.dreamless.treewarp;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;

import com.dreamless.laithorn.LaithornUtils;

public class DatabaseHandler {

	public static HashMap<Location, Location> getLeafToRootCache() {
		HashMap<Location, Location> leafToRoot = new HashMap<Location, Location>();

		String query = "SELECT * FROM " + TreeWarp.getDatabase() + "trees";

		try (PreparedStatement stmt = TreeWarp.connection.prepareStatement(query)) {
			ResultSet result = stmt.executeQuery();
			while (result.next()) {
				try
				{
					Location rootLocation = LaithornUtils.deserializeLocation(result.getString("center"));
					Location leafLocation = LaithornUtils.deserializeLocation(result.getString("location"));
					leafToRoot.put(leafLocation, rootLocation);
					PlayerMessager.debugLog("Root: " + rootLocation + " Leaf: " + leafLocation);
				}
				catch (IllegalArgumentException e)
				{
					PlayerMessager.log("Unable to add tree at: " + result.getString("center"));
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return leafToRoot;
	}

	public static HashMap<String, Location> getPlayerToRootCache() {
		HashMap<String, Location> playerToRoot = new HashMap<String, Location>();

		String query = "SELECT * FROM " + TreeWarp.getDatabase() + "players";

		try (PreparedStatement stmt = TreeWarp.connection.prepareStatement(query)) {
			ResultSet result = stmt.executeQuery();
			while (result.next()) {
				String player = result.getString("player");
				Location rootLocation = LaithornUtils.deserializeLocation(result.getString("center"));
				playerToRoot.put(player, rootLocation);
				PlayerMessager.debugLog("Player: " + player + " Leaf: " + rootLocation);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return playerToRoot;
	}

	public static void addTreeBlocks(List<BlockState> list, Location location, Player player) {
		removeOldTreeByPlayer(player);
		updatePlayer(player, location);
		updateTrees(list, location);
	}

	public static void removeLeafBlock(Location location) {
		String query = "DELETE FROM " + TreeWarp.getDatabase() + "trees WHERE location=?";
		try (PreparedStatement stmt = TreeWarp.connection.prepareStatement(query)) {
			stmt.setString(1, LaithornUtils.serializeLocation(location));
			stmt.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void removeTree(String center) {
		if (center != null) {
			String query = "DELETE FROM " + TreeWarp.getDatabase() + "trees WHERE center=?";

			try (PreparedStatement preparedStatement = TreeWarp.connection.prepareStatement(query)) {

				preparedStatement.setString(1, center);
				preparedStatement.executeUpdate();

			} catch (SQLException e) {
				e.printStackTrace();
			}

			query = "DELETE FROM " + TreeWarp.getDatabase() + "players WHERE center=?";

			try (PreparedStatement preparedStatement = TreeWarp.connection.prepareStatement(query)) {

				preparedStatement.setString(1, center);
				
				PlayerMessager.debugLog(preparedStatement.toString());
				preparedStatement.executeUpdate();

			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	private static void updatePlayer(Player player, Location location) {
		String query = "INSERT INTO " + TreeWarp.getDatabase()
				+ "players (player, center) VALUES (?, ?) ON DUPLICATE KEY UPDATE center=?";

		try (PreparedStatement preparedStatement = TreeWarp.connection.prepareStatement(query)) {

			preparedStatement.setString(1, player.getUniqueId().toString());
			preparedStatement.setString(2, LaithornUtils.serializeLocation(location));
			preparedStatement.setString(3, LaithornUtils.serializeLocation(location));
			preparedStatement.executeUpdate();

			PlayerMessager.debugLog(preparedStatement.toString());

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private static void updateTrees(List<BlockState> list, Location location) {
		String insert = "INSERT INTO " + TreeWarp.getDatabase()
				+ "trees (location, center) VALUES (?, ?) ON DUPLICATE KEY UPDATE center=?";

		try (PreparedStatement preparedStatement = TreeWarp.connection.prepareStatement(insert)) {
			

			TreeWarp.connection.setAutoCommit(false);

			for (BlockState blockState : list) {
				preparedStatement.setString(1, LaithornUtils.serializeLocation(blockState.getLocation()));
				preparedStatement.setString(2, LaithornUtils.serializeLocation(location));
				preparedStatement.setString(3, LaithornUtils.serializeLocation(location));
				PlayerMessager.debugLog(preparedStatement.toString());
				preparedStatement.addBatch();
			}

			preparedStatement.executeBatch();
			TreeWarp.connection.commit();

			TreeWarp.connection.setAutoCommit(true);

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private static void removeOldTreeByPlayer(Player player) {
		String query = "SELECT center FROM  " + TreeWarp.getDatabase() + "players WHERE player=?";

		String center = null;

		try (PreparedStatement preparedStatement = TreeWarp.connection.prepareStatement(query)) {
			
			preparedStatement.setString(1, player.getUniqueId().toString());
			
			PlayerMessager.debugLog(preparedStatement.toString());
			
			ResultSet result = preparedStatement.executeQuery();
			if (result.next()) {
				center = result.getString("center");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		removeTree(center);
	}
}
