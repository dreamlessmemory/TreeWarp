package com.dreamless.treewarp;

import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class TeleportHandler extends BukkitRunnable {

	private static int RADIUS = 2;
	private static double DISTANCE = 2.0;
	private static Random random = new Random();

	private Player player;
	private Location destination;
	private Location origin;
	private Material material;

	public TeleportHandler(Player player, Location destination, Location origin, Material material) {
		// TODO Auto-generated constructor stub
		this.player = player;
		this.destination = destination;
		this.origin = origin;
		this.material = material;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub

		double distance = origin.distanceSquared(player.getLocation());
		if (distance > DISTANCE) {
			// Cancel
			PlayerMessager.msg(player, LanguageReader.getText("Teleport_Canceled"));
			return;
		}

		PlayerMessager.msg(player, LanguageReader.getText("Teleport_Confirmed"));
		
		destination = calculateWarpLocation();
		
		prepareWarpLocation(destination);
		
		new EffectHandler.EffectRunnable(player.getLocation(), 30, 5, "activate", material.createBlockData()).runTaskTimer(TreeWarp.treeWarp, 0, 5);
		
		player.teleport(destination);
		new EffectHandler.EffectRunnable(destination, 30, 5, "activate", material.createBlockData()).runTaskTimer(TreeWarp.treeWarp, 0, 5);
	}

	private Location calculateWarpLocation() {
		// Calculate X
		double xCoordinate = destination.getX() + randomizeDirection() + 0.5;
		// Calculate Z
		double zCoordinate = destination.getZ() + randomizeDirection() + 0.5;

		Location finalDestination = new Location(destination.getWorld(), xCoordinate, destination.getY(), zCoordinate,
				player.getLocation().getYaw(), player.getLocation().getPitch());

		return finalDestination;
	}

	private double randomizeDirection() {
		return (random.nextDouble() * RADIUS + 1) * (random.nextBoolean() ? 1 : -1);
	}

	private void prepareWarpLocation(Location location) {
		Block destination = location.getWorld().getBlockAt(location);
		Block aboveDestination = destination.getRelative(BlockFace.UP);
		Block belowDestination = destination.getRelative(BlockFace.DOWN);

		if (destination.getType().isSolid())
			destination.setType(Material.AIR);
		if (aboveDestination.getType().isSolid())
			aboveDestination.setType(Material.AIR);
		if (!belowDestination.getType().isSolid())
			belowDestination.setType(Material.GRASS_BLOCK);
	}
}
