package com.dreamless.treewarp;

import java.util.ArrayList;
import java.util.Arrays;

import org.bukkit.TreeType;
import org.bukkit.util.ChatPaginator;

public class TreeWarpUtils {

	public static int WRAP_SIZE = 35;

	public static ArrayList<String> wrapText(String text) {
		ArrayList<String> wrappedText = new ArrayList<String>();
		wrappedText.addAll(Arrays.asList(ChatPaginator.wordWrap(text, WRAP_SIZE)));
		return wrappedText;
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
