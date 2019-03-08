package com.dreamless.treewarp;

import java.util.List;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.bukkit.Location;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;

public class DatabaseHandler {

	public static void addTreeBlocks(List<BlockState> list, Location location, Player player) {
		updatePlayer(player, location);
		updateTrees(list, location);
	}

	private static void updatePlayer(Player player, Location location) {
		String query = "INSERT INTO " + TreeWarp.getDatabase()
				+ "players (player, center) VALUES (?, ?) ON DUPLICATE KEY UPDATE center=?";

		try (PreparedStatement preparedStatement = TreeWarp.connection.prepareStatement(query)) {

			preparedStatement.setString(1, player.getUniqueId().toString());
			preparedStatement.setString(2, TreeWarpUtils.serializeLocation(location));
			preparedStatement.setString(3, TreeWarpUtils.serializeLocation(location));
			preparedStatement.executeUpdate();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static void updateTrees(List<BlockState> list, Location location) {
		String insert = "INSERT INTO " + TreeWarp.getDatabase() + " (location, center) VALUES (?, ?, ?)";

		try (PreparedStatement preparedStatement = TreeWarp.connection.prepareStatement(insert)) {

			TreeWarp.connection.setAutoCommit(false);

			for (BlockState blockState : list) {
				preparedStatement.setString(1, TreeWarpUtils.serializeLocation(blockState.getLocation()));
				preparedStatement.setString(2, TreeWarpUtils.serializeLocation(location));
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
}
