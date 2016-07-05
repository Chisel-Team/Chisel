package team.chisel.common.config;

import java.util.Locale;

import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemDye;
import net.minecraftforge.common.config.Configuration;

import org.apache.commons.lang3.StringUtils;

import team.chisel.Chisel;
import team.chisel.Features;
import team.chisel.client.render.ctm.CTM;

public class Configurations {

    public static Configuration config;

    public static boolean configExists;

    public static double concreteVelocityMult;
    public static boolean ghostCloud;
    public static int factoryBlockAmount;
    public static boolean allowMossy;
    public static boolean allowSmoothStone;
    public static boolean chiselRecipe;
    public static boolean chiselStoneToCobbleBricks;
    public static boolean chiselBackToVanillaLeaves;

    public static int basaltAmount;
    public static int marbleAmount;
    public static int limestoneAmount;

    public static int particlesTickrate;
    public static boolean oldPillars;
    public static boolean disableCTM;
    public static boolean connectInsideCTM;
    public static boolean blockDescriptions;
    public static boolean imTooGoodForDescriptions;

    public static boolean allowChiselDamage;
    public static int ironChiselMaxDamage;
    public static int diamondChiselMaxDamage;
    public static boolean ironChiselCanLeftClick;
    public static boolean ironChiselHasModes;
    public static int ironChiselAttackDamage;
    public static int diamondChiselAttackDamage;
    public static boolean allowChiselCrossColors;

    public static boolean useRoadLineTool;
    public static String getRoadLineTool;
    public static int roadLineToolLevel;

    public static int[] configColors = new int[ItemDye.DYE_COLORS.length];

    public static boolean fullBlockConcrete;

    public static boolean refreshConfig() {

        String category;

        /* general */
        category = "general";
        concreteVelocityMult = config.get(category, "concreteVelocityMult", 1.35, "The factor that concrete increases your velocity. Default is 1.35, set to 1 for no change.").getDouble();
        fullBlockConcrete = config.get(category, "fullBlockConcrete", false, "Should concrete be a full block. This will also unavoidably disable speed increase if set to true.").getBoolean(false);
        ghostCloud = config.get(category, "doesCloudRenderLikeGhost", true).getBoolean(true);
        factoryBlockAmount = config.get(category, "amountYouGetFromFactoryBlockCrafting", 32).getInt(32);
        allowMossy = config.get(category, "allowBrickToMossyInChisel", true, "If true, you can chisel stone brick to mossy stone brick.").getBoolean(true);
        allowSmoothStone = config.get(category, "allowSmoothStoneToStoneBricksAndBack", true).getBoolean(true);
        chiselRecipe = config.get(category, "chiselAlternateRecipe", false, "Use alternative crafting recipe for the chisel").getBoolean(false);
        chiselStoneToCobbleBricks = config.get(category, "chiselStoneToCobbleBricks", true, "Chisel stone to cobblestone and bricks by left clicking.").getBoolean(false);
        chiselBackToVanillaLeaves = config
                .get(category, "chiselBackToVanillaLeaves", false, "If this is true, you can chisel from the chisel leaves back to vanilla ones. If it is false, you cannot.").getBoolean(false);

        /* worldgen */
        category = "worldgen";
        basaltAmount = config.get(category, "basaltAmount", 25, "Amount of basalt to generate in the world; use 0 for none").getInt(25);
        marbleAmount = config.get(category, "marbleAmount", 25, "Amount of marble to generate in the world; use 0 for none").getInt(25);
        limestoneAmount = config.get(category, "limestoneAmount", 15, "Amount of limestone to generate in the world; use 0 for none").getInt(15);
        
        /* client */
        category = "client";
        particlesTickrate = config.get(category, "particleTickrate", 1, "Particle tick rate. Greater value = less particles.").getInt(1);
        oldPillars = config.get(category, "pillarOldGraphics", false, "Use old pillar textures").getBoolean(false);
        disableCTM = !config.get(category, "connectedTextures", true, "Enable connected textures").getBoolean(true);
        CTM.disableObscuredFaceCheckConfig = connectInsideCTM = config.get(category, "connectInsideCTM", false,
                "Choose whether the inside corner is disconnected on a CTM block - http://imgur.com/eUywLZ4").getBoolean(false);
        blockDescriptions = config.get(category, "tooltipsUseBlockDescriptions", true, "Make variations of blocks have the same name, and use the description in tooltip to distinguish them.")
                .getBoolean(true);
        imTooGoodForDescriptions = config.get(category, "imTooGoodForBlockDescriptions", false, "For those people who just hate block descriptions on the world gen!").getBoolean();

        /* chisel */
        category = "chisel";
        allowChiselDamage = config.get(category, "allowChiselDamage", true, "Should the chisel be damageable and take damage when it chisels something.").getBoolean();
        ironChiselMaxDamage = config.getInt("ironChiselMaxDamage", category, 500, 1, Short.MAX_VALUE, "The max damage of the standard iron chisel.");
        diamondChiselMaxDamage = config.getInt("diamondChiselMaxDamage", category, 5000, 1, Short.MAX_VALUE, "The max damage of the diamond chisel.");
        ironChiselCanLeftClick = config.get(category, "ironChiselCanLeftClick", true, "If this is true, the iron chisel can left click chisel blocks. If false, it cannot.").getBoolean();
        ironChiselHasModes = config.get(category, "ironChiselHasModes", false, "If this is true, the iron chisel can change its chisel mode just as the diamond chisel can.").getBoolean();
        allowChiselCrossColors = config.get(category, "allowChiselCrossColors", true, "Should someone be able to chisel something into a different color.").getBoolean();

        ironChiselAttackDamage = config
                .get(category, "ironChiselAttackDamage", 2, "The extra attack damage points (in half hearts) that the iron chisel inflicts when it is used to attack an entity.").getInt();
        diamondChiselAttackDamage = config.get(category, "diamondChiselAttackDamage", 2,
                "The extra attack damage points (in half hearts) that the diamond chisel inflicts when it is used to attack an entity.").getInt();

        /* block */
        category = "block";
        useRoadLineTool = config.get(category, "useRoadLineTool", false, "Should the road line require a tool to break (If false, road lines can be broken in Adventure)").getBoolean();
        getRoadLineTool = config.get(category, "getRoadLineTool", "pickaxe", "The tool that is able to break roadLines (requires useRoadLineTool to be true to take effect)").getString();
        roadLineToolLevel = config.get(category, "roadLineToolLevel", 0,
                "The lowest harvest level of the tool able to break the road lines (requires useRoadLineTool to be true to take effect) (0 = Wood/Gold, 1 = Stone, 2 = Iron, 3 = Diamond) Default: 0")
                .getInt();

        /* hexColors */
        category = "hexColors";

        for (EnumDyeColor c : EnumDyeColor.values()) {
            // tterrag... don't kill me over this formatting.
            String temp = config.get(category, "hex" + c.getName(), "#" + Integer.toHexString(ItemDye.DYE_COLORS[c.ordinal()]),
                    StringUtils.capitalize(c.getName()) + " color for hex block overlay #RRGGBB").getString();
            // Or this
            try {
                configColors[c.ordinal()] = Integer.decode(temp);
            } catch (NumberFormatException e) {
                Chisel.logger.warn("Configuration error, " + temp + " was not recognized as a color.  Using default: #" + Integer.toHexString(ItemDye.DYE_COLORS[c.ordinal()]));
                configColors[c.ordinal()] = ItemDye.DYE_COLORS[c.ordinal()];
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