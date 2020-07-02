package com.dreamless.treewarp;

import java.io.File;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

import com.dreamless.laithorn.api.LaithornRegister;
import com.dreamless.laithorn.api.RecipeType;
import com.dreamless.treewarp.listeners.BlockListener;
import com.dreamless.treewarp.listeners.CommandListener;
import java.sql.Connection;

public class TreeWarp extends JavaPlugin {

	public static TreeWarp treeWarp;

	// Connection vars
	public static Connection connection; // This is the variable we will use to connect to database

	// DataBase vars.
	private String username;
	private String password;
	private String url;
	private static String database;
	private static String testdatabase;

	// Listeners
	public BlockListener blockListener;
	// public CraftingBenchListener craftListener;

	// debug
	public static boolean debug;
	public static boolean development;

	// Language
	public LanguageReader languageReader;

	@Override
	public void onEnable() {

		treeWarp = this;

		// Load Config
		try {
			if (!readConfig()) {
				getServer().getPluginManager().disablePlugin(this);
				return;
			}
		} catch (Exception e) {
			e.printStackTrace();
			getServer().getPluginManager().disablePlugin(this);
			return;
		}

		// Load data
		DataHandler.loadSpawnArea();

		// SQL Setup
		try { // We use a try catch to avoid errors, hopefully we don't get any.
			Class.forName("com.mysql.jdbc.Driver"); // this accesses Driver in jdbc.
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			System.err.println("jdbc driver unavailable!");
			return;
		}
		try {
			connection = (Connection) DriverManager.getConnection(url, username, password);
		} catch (SQLException e) { // catching errors)
			e.printStackTrace(); // prints out SQLException errors to the console (if any)
		}

		// Load Cache
		CacheHandler.loadCaches();

		// Listeners
		blockListener = new BlockListener();
		// craftListener = new CraftingBenchListener();
		getCommand("TreeWarp").setExecutor(new CommandListener());

		treeWarp.getServer().getPluginManager().registerEvents(blockListener, treeWarp);
		// treeWarp.getServer().getPluginManager().registerEvents(craftListener,
		// treeWarp);

		PlayerMessager.log(this.getDescription().getName() + " enabled!");
	}

	@Override
	public void onDisable() {

		// Disable listeners
		HandlerList.unregisterAll(this);

		// Stop shedulers
		getServer().getScheduler().cancelTasks(this);

		if (treeWarp == null) {
			return;
		}

		// Disable Server
		try { // using a try catch to catch connection errors (like wrong sql password...)
			if (connection != null && !connection.isClosed()) { // checking if connection isn't null to
				// avoid receiving a nullpointer
				connection.close(); // closing the connection field variable.
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		PlayerMessager.log(this.getDescription().getName() + " disabled!");

	}

	private boolean readConfig() {

		/*** config.yml ***/
		File currentFile = new File(treeWarp.getDataFolder(), "config.yml");
		if (!currentFile.exists()) {
			return false;
		}
		FileConfiguration currentConfig = YamlConfiguration.loadConfiguration(currentFile);

		// Database settings
		username = currentConfig.getString("username");
		password = currentConfig.getString("password");
		url = currentConfig.getString("url");
		database = currentConfig.getString("prefix");
		testdatabase = currentConfig.getString("testprefix");

		// Dev/Debug control
		debug = currentConfig.getBoolean("debug", false);
		development = currentConfig.getBoolean("development", false);

		// Effects
		EffectHandler.ANGLE = currentConfig.getInt("angle", 25);
		EffectHandler.PARTICLE_COUNT = currentConfig.getInt("particles", 7);
		EffectHandler.RADIUS = currentConfig.getDouble("radius", 1);
		EffectHandler.AMBIENT_EFFECT_COUNT = currentConfig.getInt("ambientcount", 5);
		EffectHandler.AMBIENT_EFFECT_INTERVAL = currentConfig.getInt("ambientinterval", 3);
		EffectHandler.AMBIENT_PARTICLE_COUNT = currentConfig.getInt("ambientparticles", 7);

		// Balancing
		BlockListener.durabilityLoss = currentConfig.getInt("shearsusecost", 1);
		TeleportHandler.DISTANCE = currentConfig.getDouble("distancesquared", 4);

		/*** text.yml ***/
		currentFile = new File(treeWarp.getDataFolder(), "text.yml");
		if (!currentFile.exists()) {
			return false;
		}

		LanguageReader.loadEntries(currentFile);
		
		registerRecipes(currentConfig);

		// Continuous
		new EffectHandler.EffectContinuousRunnable().runTaskTimer(treeWarp, 20,
				EffectHandler.AMBIENT_EFFECT_INTERVAL * 20);

		return true;
	}

	public void reload() {
		try {
			if (!treeWarp.readConfig()) {
				treeWarp = null;
				getServer().getPluginManager().disablePlugin(this);
				return;
			}
		} catch (Exception e) {
			e.printStackTrace();
			treeWarp = null;
			getServer().getPluginManager().disablePlugin(this);
			return;
		}
	}

	public static String getDatabase() {
		return development ? testdatabase : database;
	}
	
	private static void registerRecipes(FileConfiguration currentConfig) {
		
		// Magic Bonemeal
		LaithornRegister.registerItemCrafting(CustomRecipes.magicBonemealItem(), Material.BONE_MEAL,
				currentConfig.getInt("bonemeal_create_level", 0), currentConfig.getInt("bonemeal_create_exp", 10),
				RecipeType.ONE_FRAGMENT, null, "magicked_bonemeal", treeWarp);
		
		// Magic Shears
		LaithornRegister.registerItemCrafting(CustomRecipes.shearsItem(), Material.SHEARS,
				currentConfig.getInt("shears_create_level", 0), currentConfig.getInt("shears_create_exp", 10),
				RecipeType.CENTERED, null, "magicked_shears", treeWarp);
		
		// Laithorn Leaves
		
		ArrayList<Material> leaves = new ArrayList<Material>();
		leaves.add(Material.BIRCH_LEAVES);
		leaves.add(Material.OAK_LEAVES);
		leaves.add(Material.DARK_OAK_LEAVES);
		leaves.add(Material.JUNGLE_LEAVES);
		leaves.add(Material.ACACIA_LEAVES);
		leaves.add(Material.SPRUCE_LEAVES);
		
		LaithornRegister.registerItemCrafting(CustomRecipes.spawnLeafItem(), leaves,
				currentConfig.getInt("leaves_create_level", 0), currentConfig.getInt("leaves_create_exp", 10),
				RecipeType.CENTERED, null, "laithorn_leaf", treeWarp);
		
		// Repair Shears
		LaithornRegister.registerItemRepair(Material.SHEARS, currentConfig.getInt("shears_repair_level", 0),
				currentConfig.getInt("shears_repair_exp", 3), currentConfig.getInt("shears_repair_rate", 50), true,
				null);
	}

}
