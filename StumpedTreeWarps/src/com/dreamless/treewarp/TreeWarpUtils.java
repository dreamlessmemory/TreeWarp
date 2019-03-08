package com.dreamless.treewarp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import org.bukkit.Location;

import org.bukkit.TreeType;
import org.bukkit.util.ChatPaginator;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class TreeWarpUtils {

	public static int WRAP_SIZE = 35;
	// Parsing
	public static Gson gson = new Gson();

	public static ArrayList<String> wrapText(String text) {
		ArrayList<String> wrappedText = new ArrayList<String>();
		wrappedText.addAll(Arrays.asList(ChatPaginator.wordWrap(text, WRAP_SIZE)));
		return wrappedText;
	}
	
	public static String serializeLocation(Location location) {
		return gson.toJson(location.serialize());
	}
	
	public static Location deserializeLocation(String json) {
		return Location.deserialize(gson.fromJson(json, new TypeToken<HashMap<String, Object>>(){}.getType()));
	}

	public static boolean isValidTree(TreeType type) {
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
}
