package com.dreamless.treewarp.listeners;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.dreamless.treewarp.CustomRecipes;
import com.dreamless.treewarp.LanguageReader;
import com.dreamless.treewarp.PlayerMessager;
import com.dreamless.treewarp.TeleportHandler;
import com.dreamless.treewarp.TreeWarp;

public class CommandListener implements CommandExecutor {

	@SuppressWarnings("unused")
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

		String cmd = "help";
		if (args.length > 0) {
			cmd = args[0];
		}

		if (cmd.equalsIgnoreCase("help")) {

		} else if (cmd.equalsIgnoreCase("reload")) {

			// if (sender.hasPermission("treewarp.cmd.reload")) {
			if (true) {
				TreeWarp.treeWarp.reload();
				PlayerMessager.msg(sender, LanguageReader.getText("CMD_Reload"));
			} else {
				PlayerMessager.msg(sender, LanguageReader.getText("Error_NoPermissions"));
			}

		} else if (cmd.equalsIgnoreCase("bonemeal")) {

			// if (sender.hasPermission("treewarp.cmd.bonemeal")) {
			if (true) {
				cmdBonemeal(sender);
			} else {
				PlayerMessager.msg(sender, LanguageReader.getText("Error_NoPermissions"));
			}

		} else if (cmd.equalsIgnoreCase("shears")) {

			// if (sender.hasPermission("treewarp.cmd.bonemeal")) {
			if (true) {
				cmdShears(sender);
			} else {
				PlayerMessager.msg(sender, LanguageReader.getText("Error_NoPermissions"));
			}

		} else if (cmd.equalsIgnoreCase("spawn")) {

			// if (sender.hasPermission("treewarp.cmd.bonemeal")) {
			if (true) {
				cmdSpawn(sender, args);
			} else {
				PlayerMessager.msg(sender, LanguageReader.getText("Error_NoPermissions"));
			}
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
