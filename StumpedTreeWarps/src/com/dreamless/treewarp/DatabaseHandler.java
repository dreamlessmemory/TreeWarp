package com.dreamless.treewarp;

import java.util.HashMap;
import java.util.List;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.bukkit.Location;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;

import com.google.gson.reflect.TypeToken;

public class DatabaseHandler {

	public static HashMap<Location, Location> getLeafToRootCache() {
		HashMap<Location, Location> leafToRoot = new HashMap<Location, Location>();

		String query = "SELECT * FROM " + TreeWarp.getDatabase() + "trees";

		try (PreparedStatement stmt = TreeWarp.connection.prepareStatement(query)) {
			ResultSet result = stmt.executeQuery();
			while (result.next()) {
				leafToRoot.put(TreeWarpUtils.deserializeLocation(result.getString("location")),
						TreeWarpUtils.deserializeLocation(result.getString("center")));
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
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
				playerToRoot.put(result.getString("player"),
						TreeWarpUtils.deserializeLocation(result.getString("center")));
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
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
			stmt.setString(1, TreeWarpUtils.serializeLocation(location));
			stmt.executeUpdate();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
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
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			query = "DELETE FROM " + TreeWarp.getDatabase() + "players WHERE center=?";
			
			try (PreparedStatement preparedStatement = TreeWarp.connection.prepareStatement(query)) {

				preparedStatement.setString(1, center);
				preparedStatement.executeUpdate();

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	private static void updatePlayer(Player player, Location location) {
		String query = "INSERT INTO " + TreeWarp.getDatabase()
				+ "players (player, center) VALUES (?, ?) ON DUPLICATE KEY UPDATE center=?";

		try (PreparedStatement preparedStatement = TreeWarp.connection.prepareStatement(query)) {

			preparedStatement.setString(1, player.getUniqueId().toString());
			preparedStatement.setString(2, TreeWarpUtils.serializeLocation(location));
			preparedStatement.setString(3, TreeWarpUtils.serializeLocation(location));
			preparedStatement.executeUpdate();

			PlayerMessager.debugLog(preparedStatement.toString());

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static void updateTrees(List<BlockState> list, Location location) {
		String insert = "INSERT INTO " + TreeWarp.getDatabase()
				+ "trees (location, center) VALUES (?, ?) ON DUPLICATE KEY UPDATE center=?";

		try (PreparedStatement preparedStatement = TreeWarp.connection.prepareStatement(insert)) {

			TreeWarp.connection.setAutoCommit(false);

			for (BlockState blockState : list) {
				preparedStatement.setString(1, TreeWarpUtils.serializeLocation(blockState.getLocation()));
				preparedStatement.setString(2, TreeWarpUtils.serializeLocation(location));
				preparedStatement.setString(3, TreeWarpUtils.serializeLocation(location));
				preparedStatement.addBatch();
			}

			preparedStatement.executeBatch();
			TreeWarp.connection.commit();

			TreeWarp.connection.setAutoCommit(true);

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static void removeOldTreeByPlayer(Player player) {
		String query = "SELECT center FROM  " + TreeWarp.getDatabase() + "players";

		String center = null;

		try (PreparedStatement preparedStatement = TreeWarp.connection.prepareStatement(query)) {

			ResultSet result = preparedStatement.executeQuery();
			if (result.next()) {
				center = result.getString("center");
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		removeTree(center);
	}
}
