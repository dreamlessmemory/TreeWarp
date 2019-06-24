package com.dreamless.treewarp;

import java.util.Collection;
import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class EffectHandler {

	public static int PARTICLE_COUNT = 7;
	public static double RADIUS = 1;
	public static int ANGLE = 25;

	public static int AMBIENT_PARTICLE_COUNT = 7;
	public static int AMBIENT_EFFECT_COUNT = 5;
	public static int AMBIENT_EFFECT_INTERVAL = 3;

	private static Random random = new Random();

	public static Vector teleportActivateEffect(Location location, Vector vec) {
		World world = location.getWorld();
		location.add(new Vector(0, 0.05, 0));

		Location tempLocation = location.clone();

		vec = rotateAroundAxisY(vec, ANGLE);
		Vector tempVector = vec.clone();
		for (int i = 0; i < PARTICLE_COUNT; i++) {
			tempVector = rotateAroundAxisY(tempVector, 360 / PARTICLE_COUNT);
			world.spawnParticle(Particle.VILLAGER_HAPPY, tempLocation.clone().add(tempVector), 1, 0, 0, 0);
		}

		return vec;
	}

	public static Vector teleportReadyEffect(Location location, Vector vec) {

		World world = location.getWorld();

		location.add(new Vector(0, 0.05, 0));

		Location tempLocation = location.clone();

		vec = rotateAroundAxisY(vec, ANGLE);
		Vector tempVector = vec.clone();
		for (int i = 0; i < PARTICLE_COUNT; i++) {
			tempVector = rotateAroundAxisY(tempVector, 360 / PARTICLE_COUNT);
			world.spawnParticle(Particle.VILLAGER_HAPPY, tempLocation.clone().add(tempVector), 1, 0, 0, 0);
		}

		return vec;
	}

	public static void teleportAreaEffects(Location location) {

		World world = location.getWorld();
		for (int i = 0; i < AMBIENT_EFFECT_COUNT; i++) {
			// Calculate X
			double xCoordinate = (random.nextDouble() * TeleportHandler.RADIUS + 1) * (random.nextBoolean() ? 1 : -1)
					+ 0.5;
			// Calculate Z
			double zCoordinate = (random.nextDouble() * TeleportHandler.RADIUS + 1) * (random.nextBoolean() ? 1 : -1)
					+ 0.5;
			world.spawnParticle(Particle.VILLAGER_HAPPY, location.clone().add(xCoordinate, 0.5, zCoordinate),
					AMBIENT_PARTICLE_COUNT, 0.75, 0.5, 0.75);
		}
		
		world.playSound(location, Sound.BLOCK_BEACON_AMBIENT, 1.5f, 1f);
	}

	// Math methods

	private static Vector rotateAroundAxisY(Vector v, int angle) {
		double yangle = Math.toRadians(angle); // note that here we do have to convert to radians.
		double yAxisCos = Math.cos(-yangle); // getting the cos value for the yaw.
		double yAxisSin = Math.sin(-yangle);
		double x = v.getX() * yAxisCos + v.getZ() * yAxisSin;
		double z = v.getX() * -yAxisSin + v.getZ() * yAxisCos;
		return v.setX(x).setZ(z);
	}

	public static class EffectRunnable extends BukkitRunnable {

		private Location location;
		private int duration;
		private int interval;
		private int time = 0;
		String type;
		Vector vec = new Vector(RADIUS, 0, RADIUS);

		public EffectRunnable(Location loc, int dur, int interv, String type) {
			location = loc;
			duration = dur;
			interval = interv;
			this.type = type;
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
				vec = teleportReadyEffect(location, vec);
				break;
			case "activate":
				vec = teleportActivateEffect(location, vec);
				break;
			default: // Do nothing?
				break;
			}

		}

	}

	public static class EffectContinuousRunnable extends BukkitRunnable {

		@Override
		public void run() {
			Collection<Location> warpTreesCollection = CacheHandler.getWarpLocations();
			for (Location location : warpTreesCollection) {
				teleportAreaEffects(location.clone());
				//PlayerMessager.debugLog("Playing effect?");
			}
		}

	}

}
