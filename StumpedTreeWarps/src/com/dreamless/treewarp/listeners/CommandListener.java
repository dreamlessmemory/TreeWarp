package com.dreamless.treewarp.listeners;

import java.text.ParseException;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.dreamless.treewarp.CacheHandler;
import com.dreamless.treewarp.CustomRecipes;
import com.dreamless.treewarp.DatabaseHandler;
import com.dreamless.treewarp.LanguageReader;
import com.dreamless.treewarp.PlayerMessager;
import com.dreamless.treewarp.TeleportHandler;
import com.dreamless.treewarp.TreeWarp;
import com.dreamless.treewarp.TreeWarpUtils;

public class CommandListener implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

		String cmd = "help";
		if (args.length > 0) {
			cmd = args[0];
		}

		if (!sender.hasPermission("treewarp.admin")) {
			PlayerMessager.msg(sender, LanguageReader.getText("Error_NoPermissions"));
			return false;
		}

		if (cmd.equalsIgnoreCase("help")) {

		} else if (cmd.equalsIgnoreCase("reload")) {
			TreeWarp.treeWarp.reload();
			PlayerMessager.msg(sender, LanguageReader.getText("CMD_Reload"));

		} else if (cmd.equalsIgnoreCase("bonemeal")) {
			cmdBonemeal(sender);
		} else if (cmd.equalsIgnoreCase("shears")) {
			cmdShears(sender);
		} else if (cmd.equalsIgnoreCase("spawn")) {
			cmdSpawn(sender, args);
		} else if (cmd.equalsIgnoreCase("clear")) {
			cmdClearPlayer(sender, args);
		}
		return true;
	}

	private void cmdBonemeal(CommandSender sender) {
		if (sender instanceof Player) {
			((Player) sender).getInventory().addItem(CustomRecipes.bonemealItem());
		}
	}

	private void cmdShears(CommandSender sender) {
		if (sender instanceof Player) {
			((Player) sender).getInventory().addItem(CustomRecipes.shearsItem());
		}
	}

	private void cmdClearPlayer(CommandSender sender, String[] args) {
		try {
			Player player = Bukkit.getPlayer(TreeWarpUtils.getUUID(args[1]));
			
			if(player != null) {
			
			Location centerLocation = CacheHandler.removePlayerFromCache(player);
			DatabaseHandler.removeTree(TreeWarpUtils.serializeLocation(centerLocation));
			PlayerMessager.msg(sender, LanguageReader.getText("CMD_Player_Cleared", args[1]));
			} else {
				PlayerMessager.msg(sender, LanguageReader.getText("CMD_Player_Not_Cleared", args[1]));
			}
		} catch (ParseException | org.json.simple.parser.ParseException e) {
			e.printStackTrace();
		}
	}
	
	private void cmdSpawn(CommandSender sender, String[] args) {
		if (!(sender instanceof Player)) { // No console commands please
			return;
		}

		Player player = (Player) sender;

		if (args[1].equalsIgnoreCase("set")) {

			Location currentLocation = player.getLocation();
			Location targetLocationetLocation = new Location(currentLocation.getWorld(), currentLocation.getBlockX(),
					currentLocation.getBlockY(), currentLocation.getBlockZ());

			if (TeleportHandler.addCorner(targetLocationetLocation)) {
				PlayerMessager.msg(sender, LanguageReader.getText("CMD_Spawn_Success"));
			} else {
				PlayerMessager.msg(sender, LanguageReader.getText("CMD_Spawn_Failure"));
			}
		} else if (args[1].equalsIgnoreCase("clear")) {
			TeleportHandler.clearSpawn();
			PlayerMessager.msg(sender, LanguageReader.getText("CMD_Spawn_Cleared"));
		} else if (args[1].equalsIgnoreCase("leaf")) {
			player.getInventory().addItem(CustomRecipes.spawnLeafItem());
		}
	}
}
