package com.cricketcraft.chisel;

import net.minecraftforge.common.config.Configuration;

public class Configurations {

	public static Configuration config;

	public static boolean configExists;
	public static boolean oldPillars;
	public static boolean disableCTM;
	public static boolean blockDescriptions;
	public static boolean ghostCloud;
	public static boolean chiselStoneToCobbleBricks;
	public static boolean allowMossy;
	public static boolean chiselRecipe;
	public static boolean autoChisel;
	public static boolean fancy;
	public static boolean enableFMP;

	public static int factoryBlockAmount;
	public static int particlesTickrate;
	public static int marbleAmount;
	public static int limestoneAmount;
	public static int graniteAmount;
	public static int dioriteAmount;
	public static int andesiteAmount;

	public static double concreteVelocity;

	public static boolean refreshConfig() {
		concreteVelocity = config.get("general", "concreteVelocity", 0.45,
				"Traversing concrete roads, players will acceleration to this velocity. For reference, normal running speed is about 0.28. Set to 0 to disable acceleration.").getDouble(0.45);
		particlesTickrate = config.get("client", "particleTickrate", 1, "Particle tick rate. Greater value = less particles.").getInt(1);
		oldPillars = config.get("client", "pillarOldGraphics", false, "Use old pillar textures").getBoolean(false);
		disableCTM = !config.get("client", "connectedTextures", true, "Enable connected textures").getBoolean(true);
		fancy = config.get("client", "fancyLeaves", true, "Enable fancy textures").getBoolean(true);
		blockDescriptions = config.get("client", "tooltipsUseBlockDescriptions", true, "Make variations of blocks have the same name, and use the description in tooltip to distinguish them.")
				.getBoolean(true);
		ghostCloud = config.get("general", "doesCloudRenderLikeGhost", true).getBoolean(true);
		factoryBlockAmount = config.get("general", "amountYouGetFromFactoryBlockCrafting", 32).getInt(32);
		allowMossy = config.get("general", "allowCobbleToMossyInChisel", true).getBoolean(true);
		marbleAmount = config.get("worldgen", "marbleAmount", 7, "Amount of marble to generate in the world; use 0 for none").getInt(7);
		limestoneAmount = config.get("worldgen", "limestoneAmount", 8, "Amount of limestone to generate in the world; use 0 for none").getInt(8);
		graniteAmount = config.get("worldgen", "graniteAmount", 8, "Amount of granite to generate in the world; use 0 for none.").getInt(8);
		dioriteAmount = config.get("worldgen", "dioriteAmount", 8, "Amount of diorite to generate in the world; use 0 for none.").getInt(8);
		andesiteAmount = config.get("worldgen", "andesiteAmount", 8, "Amount of andesite to generate in the world; use 0 for none.").getInt(8);
		chiselRecipe = config.get("general", "chiselAlternateRecipe", false, "Use alternative crafting recipe for the chisel").getBoolean(false);
		autoChisel = config.get("general", "autoChisel", true, "Should people be allowed to use the auto chisel").getBoolean(true);
		enableFMP = config.get("general", "enableFMP", true, "Do you want to enable FMP").getBoolean(true);
		chiselStoneToCobbleBricks = config.get("general", "chiselStoneToCobbleBricks", true, "Chisel stone to cobblestone and bricks by left clicking.").getBoolean(false);

		if (config.hasChanged()) {
			config.save();
		}
		return true;
	}

	public static boolean featureEnabled(String feature) {
		return config.get("features", feature, true).getBoolean(true) && refreshConfig();
	}
}
