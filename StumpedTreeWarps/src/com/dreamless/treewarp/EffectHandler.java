package com.dreamless.treewarp;

import java.util.Collection;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.block.data.BlockData;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class EffectHandler {

	public static int PARTICLE_COUNT = 7;
	public static double RADIUS = 1;
	public static int ANGLE = 25;

	public static void teleportActivateEffect(Location location, BlockData blockData) {
		World world = location.getWorld();
		location.add(new Vector(0, 0.25, 0));
	
		Location tempLocation = location.clone();

		double yangle = Math.toRadians(ANGLE); // note that here we do have to convert to radians.
		double yAxisCos = Math.cos(-yangle); // getting the cos value for the yaw.
		double yAxisSin = Math.sin(-yangle); // getting the sin value for the yaw.
		
		Vector vec = new Vector(RADIUS, 0, RADIUS);
		
		for (double a = 0; a < Math.PI * 2; a += Math.PI / PARTICLE_COUNT) {
			// calculate x and z.[/SIZE]

			vec = rotateAroundAxisY(vec, yAxisCos, yAxisSin);
			// add vec to center, display particle and subtract vec from center.
			
			world.spawnParticle(Particle.BLOCK_CRACK, tempLocation.clone().add(vec), 1, 0, 0, 0, blockData);
			world.spawnParticle(Particle.VILLAGER_HAPPY, tempLocation.clone().add(vec), 1, 0, 0, 0);
		}
	}

	public static void teleportReadyEffect(Location location, BlockData blockData) {

		World world = location.getWorld();

		location.add(new Vector(0, 0.125, 0));
		
		Location tempLocation = location.clone();

		double yangle = Math.toRadians(ANGLE); // note that here we do have to convert to radians.
		double yAxisCos = Math.cos(-yangle); // getting the cos value for the yaw.
		double yAxisSin = Math.sin(-yangle); // getting the sin value for the yaw.
		
		Vector vec = new Vector(RADIUS, 0, RADIUS);
		
		for (double a = 0; a < Math.PI * 2; a += Math.PI / PARTICLE_COUNT) {
			// calculate x and z.[/SIZE]

			vec = rotateAroundAxisY(vec, yAxisCos, yAxisSin);
			// add vec to center, display particle and subtract vec from center.
			
			world.spawnParticle(Particle.BLOCK_CRACK, tempLocation.clone().add(vec), 1, 0, 0, 0, blockData);
		}

	}

	public static void teleportAreaEffects(Location location) {

	}

	// Math methods

	private static Vector rotateAroundAxisY(Vector v, double cos, double sin) {
		double x = v.getX() * cos + v.getZ() * sin;
		double z = v.getX() * -sin + v.getZ() * cos;
		return v.setX(x).setZ(z);
	}

	public static class EffectRunnable extends BukkitRunnable {

		private Location location;
		private int duration;
		private int interval;
		private int time = 0;
		String type;
		private BlockData blockData;

		public EffectRunnable(Location loc, int dur, int interv, String type, BlockData blockData) {
			location = loc;
			duration = dur;
			interval = interv;
			this.type = type;
			this.blockData = blockData;
		}

		@Override
		public void run() {
			if (time > duration) {
				this.cancel();
				return;
			}

			// Increment time
			time += interval;

			switch (type) {
			case "ready":
				teleportReadyEffect(location, blockData);
				break;
			case "activate":
				teleportActivateEffect(location, blockData);
				break;
			default: // Do nothing?
				break;
			}

		}

	}

	public static class EffectContinuousRunnable extends BukkitRunnable {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			Collection<Location> warpTreesCollection = CacheHandler.getWarpLocations();
			for (Location location : warpTreesCollection) {
				teleportAreaEffects(location);
			}
		}

	}

}
