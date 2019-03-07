package com.dreamless.treewarp;

import java.util.ArrayList;
import java.util.Arrays;

import org.bukkit.util.ChatPaginator;

public class TreeWarpUtils {
	
	public static int WRAP_SIZE = 35;

	public static ArrayList<String> wrapText(String text){
		ArrayList<String> wrappedText =  new ArrayList<String>();
		wrappedText.addAll(Arrays.asList(ChatPaginator.wordWrap(text, WRAP_SIZE)));
		return wrappedText;
	}
	
}
