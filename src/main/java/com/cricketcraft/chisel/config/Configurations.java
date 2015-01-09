package com.cricketcraft.chisel.config;

import java.util.Locale;

import org.apache.commons.lang3.StringUtils;

import com.cricketcraft.chisel.init.Features;

import net.minecraftforge.common.config.Configuration;

public class Configurations {

	public static Configuration config;

	public static boolean configExists;

	public static double concreteVelocity;
	public static boolean ghostCloud;
	public static int factoryBlockAmount;
	public static boolean allowMossy;
	public static boolean chiselRecipe;
	public static boolean enableFMP;
	public static boolean chiselStoneToCobbleBricks;

	public static int marbleAmount;
	public static int limestoneAmount;
	public static int graniteAmount;
	public static int dioriteAmount;
	public static int andesiteAmount;

	public static int particlesTickrate;
	public static boolean oldPillars;
	public static boolean disableCTM;
	public static boolean fancy;
	public static boolean blockDescriptions;

	public static boolean allowChiselDamage;
	public static int ironChiselMaxDamage;
	public static int diamondChiselMaxDamage;

	public static boolean refreshConfig() {

		String category;

		/* general */
		category = "general";
		concreteVelocity = config.get(category, "concreteVelocity", 0.45,
				"Traversing concrete roads, players will acceleration to this velocity. For reference, normal running speed is about 0.28. Set to 0 to disable acceleration.").getDouble(0.45);
		ghostCloud = config.get(category, "doesCloudRenderLikeGhost", true).getBoolean(true);
		factoryBlockAmount = config.get(category, "amountYouGetFromFactoryBlockCrafting", 32).getInt(32);
		allowMossy = config.get(category, "allowCobbleToMossyInChisel", true).getBoolean(true);
		chiselRecipe = config.get(category, "chiselAlternateRecipe", false, "Use alternative crafting recipe for the chisel").getBoolean(false);
		enableFMP = config.get(category, "enableFMP", true, "Do you want to enable FMP").getBoolean(true);
		chiselStoneToCobbleBricks = config.get(category, "chiselStoneToCobbleBricks", true, "Chisel stone to cobblestone and bricks by left clicking.").getBoolean(false);

		/* worldgen */
		category = "worldgen";
		marbleAmount = config.get(category, "marbleAmount", 7, "Amount of marble to generate in the world; use 0 for none").getInt(7);
		limestoneAmount = config.get(category, "limestoneAmount", 8, "Amount of limestone to generate in the world; use 0 for none").getInt(8);
		graniteAmount = config.get(category, "graniteAmount", 8, "Amount of granite to generate in the world; use 0 for none.").getInt(8);
		dioriteAmount = config.get(category, "dioriteAmount", 8, "Amount of diorite to generate in the world; use 0 for none.").getInt(8);
		andesiteAmount = config.get(category, "andesiteAmount", 8, "Amount of andesite to generate in the world; use 0 for none.").getInt(8);

		/* client */
		category = "client";
		particlesTickrate = config.get(category, "particleTickrate", 1, "Particle tick rate. Greater value = less particles.").getInt(1);
		oldPillars = config.get(category, "pillarOldGraphics", false, "Use old pillar textures").getBoolean(false);
		disableCTM = !config.get(category, "connectedTextures", true, "Enable connected textures").getBoolean(true);
		fancy = config.get(category, "fancyLeaves", true, "Enable fancy textures").getBoolean(true);
		blockDescriptions = config.get(category, "tooltipsUseBlockDescriptions", true, "Make variations of blocks have the same name, and use the description in tooltip to distinguish them.")
				.getBoolean(true);

		/* chisel */

		category = "chisel";
		allowChiselDamage = config.get(category, "allowChiselDamage", true, "Should the chisel be damageable and take damage when it chisels something.").getBoolean();
		ironChiselMaxDamage = config.get(category, "ironChiselMaxDamage", 500, "The max damage of the standard iron chisel. Default: 500.").getInt();
		diamondChiselMaxDamage = config.get(category, "diamondChiselMaxDamage", 5000, "The max damage of the diamond chisel. Default: 5000").getInt();

		if (config.hasChanged()) {
			config.save();
		}
		return true;
	}

	public static boolean featureEnabled(Features feature) {
		return config.get("features", featureName(feature), true).getBoolean(true) && refreshConfig();
	}

	// this makes the old camelCase names from the new CONSTANT_CASE names
	private static String featureName(Features feature) {
		String[] words = feature.name().toLowerCase(Locale.ENGLISH).split("_");
		if (words.length == 1) {
			return words[0];
		}

		String ret = words[0];
		for (int i = 1; i < words.length; i++) {
			ret += StringUtils.capitalize(words[i]);
		}
		return ret;
	}

	@Deprecated
	public static boolean featureEnabled(String feature) {
		return false;
	}
}
