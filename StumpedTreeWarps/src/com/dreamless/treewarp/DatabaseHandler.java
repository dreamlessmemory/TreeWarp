package com.dreamless.treewarp;

import java.util.List;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.bukkit.Location;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;



public class DatabaseHandler {

	public static void addTreeBlocks(List<BlockState> list, Location location, Player player) {
		
		String insert = "INSERT INTO " + TreeWarp.getDatabase() + " (location, center, player) VALUES (?, ?, ?)";
		
		try (PreparedStatement preparedStatement = TreeWarp.connection.prepareStatement(insert)){
			
			TreeWarp.connection.setAutoCommit(false);
			
			for(BlockState blockState : list) {
				preparedStatement.setString(1, TreeWarpUtils.serializeLocation(blockState.getLocation()));
				preparedStatement.setString(2, TreeWarpUtils.serializeLocation(location));
				preparedStatement.setString(3, player.getUniqueId().toString());
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
