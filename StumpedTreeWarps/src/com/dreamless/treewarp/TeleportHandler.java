package com.dreamless.treewarp;

import java.util.Random;

import org.bukkit.Axis;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.checkerframework.checker.units.qual.s;

public class TeleportHandler extends BukkitRunnable {

	private static int RADIUS = 2;
	private static double DISTANCE = 4;
	private static Random random = new Random();

	// Spawn
	private static Location spawnCornerFirst;
	private static Location spawnCornerSecond;

	private Player player;
	private Location destination;
	private Location origin;
	// private Material material;

	public TeleportHandler(Player player, Location destination, Location origin, Material material) {
		this.player = player;
		this.destination = destination;
		this.origin = origin;
		// this.material = material;
	}

	public static boolean addCorner(Location location) {

		if (spawnCornerFirst == null) {
			spawnCornerFirst = location;
			return true;
		} else {
			if (location.getBlockY() == spawnCornerFirst.getBlockY()) {
				spawnCornerSecond = spawnCornerFirst;
				spawnCornerFirst = location;
				return true;
			} else {
				//reject
				return false;
			}
		}
	}

	public static void clearSpawn() {
		spawnCornerFirst = null;
		spawnCornerSecond = null;
	}

	public static void updateYML() {

	}

	public static Location getSpawnWarpDestination() {
		if (spawnCornerSecond == null || spawnCornerFirst == null) {
			return null;
		}

		int xAxisRange = Math.abs(spawnCornerFirst.getBlockX() - spawnCornerSecond.getBlockX());
		int zAxisRange = Math.abs(spawnCornerFirst.getBlockZ() - spawnCornerSecond.getBlockZ());

		int xOffset = random.nextInt(xAxisRange + 1);
		int zOffset = random.nextInt(zAxisRange + 1);

		return getLeftBottomLocation().add(xOffset, 0, zOffset);
	}

	private static Location getLeftBottomLocation() {
		if (spawnCornerSecond == null || spawnCornerFirst == null) {
			return null;
		}
		int xAxis = Math.min(spawnCornerFirst.getBlockX(), spawnCornerSecond.getBlockX());
		int zAxis = Math.min(spawnCornerFirst.getBlockZ(), spawnCornerSecond.getBlockZ());

		return new Location(spawnCornerFirst.getWorld(), xAxis, spawnCornerFirst.getBlockY(), zAxis);
	}

	@Override
	public void run() {
		double distance = origin.distanceSquared(player.getLocation());
		if (distance > DISTANCE) { // Cancel
			PlayerMessager.msg(player, LanguageReader.getText("Teleport_Canceled"));
			return;
		}

		PlayerMessager.msg(player, LanguageReader.getText("Teleport_Confirmed"));

		destination = calculateWarpLocation();

		prepareWarpLocation(destination);

		player.getWorld().spawnParticle(Particle.EXPLOSION_LARGE, player.getLocation(), 10, 0.5, 0.5, 0.5);
		player.teleport(destination);
		player.getWorld().spawnParticle(Particle.EXPLOSION_LARGE, player.getLocation(), 10, 0.5, 0.5, 0.5);

		new EffectHandler.EffectRunnable(destination, 30, 1, "activate").runTaskTimer(TreeWarp.treeWarp, 0, 1);
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
