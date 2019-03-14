package com.dreamless.treewarp;

import java.io.File;
import java.io.IOException;

import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class DataHandler {

	public static File fileReader = new File(TreeWarp.treeWarp.getDataFolder(), "data.yml");
	private static FileConfiguration configuration = new YamlConfiguration();
	
	public static void saveSpawnArea(Location first, Location second) {
		configuration.set("spawn1", (first != null? first.serialize() : ""));
		configuration.set("spawn2", (second != null? second.serialize() : ""));
		try {
			configuration.save(fileReader);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void loadSpawnArea() {
		//configuration.getString("spawn1");
	}
	
	
	public static boolean isOkay() {
		return fileReader.exists();
	}
	
}
