package com.dreamless.treewarp;

import java.io.File;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class LanguageReader {
	private static Map<String, String> entries = new TreeMap<String, String>();

	public static void loadEntries(File file) {
		FileConfiguration configFile = YamlConfiguration.loadConfiguration(file);
		
		Set<String> keySet = configFile.getKeys(false);
		for (String key : keySet) {
			entries.put(key, configFile.getString(key));
		}
	}

	public static String getText(String key) {
		String entry = entries.get(key);

		if (entry == null) {
			entry = "%No text for: '" + key + "'%";
		}

		return entry;
	}
}
