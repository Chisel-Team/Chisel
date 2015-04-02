package com.cricketcraft.chisel.config;

import com.cricketcraft.chisel.Chisel;
import com.cricketcraft.chisel.Features;
import net.minecraft.item.ItemDye;
import net.minecraftforge.common.config.Configuration;
import org.apache.commons.lang3.StringUtils;

import java.util.Locale;

public class Configurations {

	public static Configuration config;

	public static boolean configExists;

	public static double concreteVelocity;
	public static boolean ghostCloud;
	public static int factoryBlockAmount;
	public static boolean allowMossy;
	public static boolean allowSmoothStone;
	public static boolean chiselRecipe;
	public static boolean enableFMP;
	public static boolean chiselStoneToCobbleBricks;
	public static boolean chiselBackToVanillaLeaves;

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
	public static int obsidianChiselMaxDamage;
	public static boolean ironChiselCanLeftClick;
	public static boolean ironChiselHasModes;
	public static int ironChiselAttackDamage;
	public static int diamondChiselAttackDamage;
	public static int obsidianChiselAttackDamage;
	public static boolean allowChiselCrossColors;

	public static boolean useRoadLineTool;
	public static String getRoadLineTool;
	public static int roadLineToolLevel;

	public static int[] configColors = new int[ItemDye.field_150923_a.length];

	public static boolean refreshConfig() {

		String category;

		/* general */
		category = "general";
		concreteVelocity = config.get(category, "concreteVelocity", 0.45,
				"Traversing concrete roads, players will acceleration to this velocity. For reference, normal running speed is about 0.28. Set to 0 to disable acceleration.").getDouble(0.45);
		ghostCloud = config.get(category, "doesCloudRenderLikeGhost", true).getBoolean(true);
		factoryBlockAmount = config.get(category, "amountYouGetFromFactoryBlockCrafting", 32).getInt(32);
		allowMossy = config.get(category, "allowBrickToMossyInChisel", true, "If true, you can chisel stone brick to mossy stone brick.").getBoolean(true);
		allowSmoothStone = config.get(category, "allowSmoothStoneToStoneBricksAndBack", true).getBoolean(true);
		chiselRecipe = config.get(category, "chiselAlternateRecipe", false, "Use alternative crafting recipe for the chisel").getBoolean(false);
		enableFMP = config.get(category, "enableFMP", true, "Do you want to enable FMP").getBoolean(true);
		chiselStoneToCobbleBricks = config.get(category, "chiselStoneToCobbleBricks", true, "Chisel stone to cobblestone and bricks by left clicking.").getBoolean(false);
		chiselBackToVanillaLeaves = config
				.get(category, "chiselBackToVanillaLeaves", false, "If this is true, you can chisel from the chisel leaves back to vanilla ones. If it is false, you cannot.").getBoolean(false);

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
		ironChiselMaxDamage = config.getInt("ironChiselMaxDamage", category, 500, 1, Short.MAX_VALUE, "The max damage of the standard iron chisel.");
		diamondChiselMaxDamage = config.getInt("diamondChiselMaxDamage", category, 5000, 1, Short.MAX_VALUE, "The max damage of the diamond chisel.");
		obsidianChiselMaxDamage = config.getInt("obsidianChiselMaxDamage", category, 2500, 1, Short.MAX_VALUE, "The max damage of the obsidian chisel.");
		ironChiselCanLeftClick = config.get(category, "ironChiselCanLeftClick", true, "If this is true, the iron chisel can left click chisel blocks. If false, it cannot.").getBoolean();
		ironChiselHasModes = config.get(category, "ironChiselHasModes", false, "If this is true, the iron chisel can change its chisel mode just as the diamond chisel can.").getBoolean();
		allowChiselCrossColors = config.get(category, "allowChiselCrossColors", true, "Should someone be able to chisel something into a different color.").getBoolean();
		ironChiselAttackDamage = config
				.get(category, "ironChiselAttackDamage", 2, "The extra attack damage points (in half hearts) that the iron chisel inflicts when it is used to attack an entity.").getInt();
		diamondChiselAttackDamage = config.get(category, "diamondChiselAttackDamage", 2,
				"The extra attack damage points (in half hearts) that the diamond chisel inflicts when it is used to attack an entity.").getInt();
		obsidianChiselAttackDamage = config.get(category, "obsidianChiselAttackDamage", 4,
				"The extra attack damage points (in half hearts) that the obsidian chisel inflicts when it is used to attack an entity.").getInt();

		/* block */
		category = "block";
		useRoadLineTool = config.get(category, "useRoadLineTool", false, "Should the road line require a tool to break (If false, road lines can be broken in Adventure)").getBoolean();
		getRoadLineTool = config.get(category, "getRoadLineTool", "pickaxe", "The tool that is able to break roadLines (requires useRoadLineTool to be true to take effect)").getString();
		roadLineToolLevel = config.get(category, "roadLineToolLevel", 0,
				"The lowest harvest level of the tool able to break the road lines (requires useRoadLineTool to be true to take effect) (0 = Wood/Gold, 1 = Stone, 2 = Iron, 3 = Diamond) Default: 0")
				.getInt();

		/* hexColors */
		category = "hexColors";

		for (int i = 0; i < ItemDye.field_150923_a.length; i++) {
			// tterrag... don't kill me over this formatting.
			String temp = config.get(category, "hex" + ItemDye.field_150923_a[i], "#" + Integer.toHexString(ItemDye.field_150922_c[i]),
					Character.toUpperCase(ItemDye.field_150923_a[i].charAt(0)) + ItemDye.field_150923_a[i].substring(1) + " color for hex block overlay #RRGGBB").getString();
			// Or this
			try {
				configColors[i] = Integer.decode(temp);
			} catch (NumberFormatException e) {
				Chisel.logger.warn("Configuration error, " + temp + " was not recognized as a color.  Using default: #" + Integer.toHexString(ItemDye.field_150922_c[i]));
				configColors[i] = ItemDye.field_150922_c[i];
			}
		}

		if (config.hasChanged()) {
			config.save();
		}
		return true;
	}

	public static boolean featureEnabled(Features feature) {
		return config.get("features", featureName(feature), true).getBoolean(true) && refreshConfig();
	}

	/**
	 * Makes the old camelCase names from the new CONSTANT_CASE names
	 */
	public static String featureName(Features feature) {
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
