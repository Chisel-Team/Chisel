package team.chisel.common.config;

import java.util.Locale;

import org.apache.commons.lang3.StringUtils;

import team.chisel.Features;

public class Configurations {

//    public static Configuration config;

    public static boolean configExists;

    public static double concreteVelocityMult;
    public static int factoryBlockAmount;
    public static boolean allowMossy;
    public static boolean allowSmoothStone;
    public static boolean chiselRecipe;
    public static boolean chiselStoneToCobbleBricks;
    public static boolean chiselBackToVanillaLeaves;

    public static int marbleAmount;
    public static int limestoneAmount;
    public static boolean basaltSpecialGen;
    public static int basaltSideThickness;
    public static int basaltBottomThickness;
    public static int basaltVeinAmount;

    public static boolean oldPillars;
    public static boolean blockDescriptions;
    public static boolean imTooGoodForDescriptions;

    public static boolean allowChiselDamage = true;
    public static int ironChiselMaxDamage = 512;
    public static int diamondChiselMaxDamage = 4096;
    public static int hitechChiselMaxDamage = 10048;
    public static boolean ironChiselCanLeftClick = true;
    public static boolean ironChiselHasModes;
    public static int ironChiselAttackDamage;
    public static int diamondChiselAttackDamage;
    public static int hitechChiselAttackDamage;
    public static boolean allowChiselCrossColors;

    public static boolean useRoadLineTool;
    public static String getRoadLineTool;
    public static int roadLineToolLevel;
    
    public static boolean autoChiselPowered = true;
    public static boolean autoChiselNeedsPower = false;

    /*
    public static boolean refreshConfig() {

        String category;

        /* general 
        category = "general";
        concreteVelocityMult = config.get(category, "concreteVelocityMult", 1.35, "The factor that concrete_white increases your velocity. Default is 1.35, set to 1 for no change.").getDouble();
        allowMossy = config.get(category, "allowBrickToMossyInChisel", true, "If true, you can chisel stone brick to mossy stone brick.").getBoolean(true);
        factoryBlockAmount = config.get(category, "amountYouGetFromFactoryBlockCrafting", 32).getInt(32);
        chiselRecipe = config.get(category, "chiselAlternateRecipe", false, "Use alternative crafting recipe for the chisel").getBoolean(false);
        chiselStoneToCobbleBricks = config.get(category, "chiselStoneToStoneBricks", true, "Allow stone to be chiseled to/from stone bricks.").getBoolean(true);
        chiselBackToVanillaLeaves = config
                .get(category, "chiselBackToVanillaLeaves", false, "If this is true, you can chisel from the chisel leaves back to vanilla ones. If it is false, you cannot.").getBoolean(false);

        /* worldgen 
        category = "worldgen";
        marbleAmount = config.getInt("marbleAmount", category, 20, 0, 30, "Amount of marble to generate in the world; use 0 for none");
        limestoneAmount = config.getInt("limestoneAmount", category, 18, 0, 30, "Amount of limestone to generate in the world; use 0 for none");

        category += ".basalt";
        basaltSpecialGen = config.getBoolean("specialGen", category, true, "True to generate basalt only around lava lakes. False to do standard vein generation.");
        basaltSideThickness = config.getInt("sideThickness", category, 1, 0, 5, "Thickness of the basalt around the sides of lava lakes. 0 for none.");
        basaltBottomThickness = config.getInt("bottomThickness", category, 3, 0, 5, "Thickness of the basalt at the bottom of lava lakes. 0 for none.");
        
        basaltVeinAmount = config.getInt("veinAmount", category, 15, 0, 30,
                "Amount of basalt to generate in the world if not using special generation. Has no effect if basaltSpecialGen is true. Use 0 for none");

        /* client 
        category = "client";
        oldPillars = config.get(category, "pillarOldGraphics", false, "Use old pillar textures").getBoolean(false);
        blockDescriptions = config.get(category, "tooltipsUseBlockDescriptions", true, "Make variations of blocks have the same name, and use the description in tooltip to distinguish them.")
                .getBoolean(true);

        /* chisel 
        category = "chisel";
        allowChiselDamage = config.get(category, "allowChiselDamage", true, "Should the chisel be damageable and take damage when it chisels something.").getBoolean();
        ironChiselMaxDamage = config.getInt("ironChiselMaxDamage", category, 512, 1, Short.MAX_VALUE, "The max damage of the standard iron chisel.");
        diamondChiselMaxDamage = config.getInt("diamondChiselMaxDamage", category, 5056, 1, Short.MAX_VALUE, "The max damage of the diamond chisel.");
        hitechChiselMaxDamage = config.getInt("hitechChiselMaxDamage", category, 10048, 1, Short.MAX_VALUE, "The max damage of the iChisel.");

        ironChiselCanLeftClick = config.get(category, "ironChiselCanLeftClick", true, "If this is true, the iron chisel can left click chisel blocks. If false, it cannot.").getBoolean();
        ironChiselHasModes = config.get(category, "ironChiselHasModes", false, "If this is true, the iron chisel can change its chisel mode just as the diamond chisel can.").getBoolean();
//        allowChiselCrossColors = config.get(category, "allowChiselCrossColors", true, "Should someone be able to chisel something into a different color.").getBoolean();

        ironChiselAttackDamage = config
                .get(category, "ironChiselAttackDamage", 2, "The extra attack damage points (in half hearts) that the iron chisel inflicts when it is used to attack an entity.").getInt();
        diamondChiselAttackDamage = config.get(category, "diamondChiselAttackDamage", 3,
                "The extra attack damage points (in half hearts) that the diamond chisel inflicts when it is used to attack an entity.").getInt();
        hitechChiselAttackDamage = config
                .get(category, "hitechChiselAttackDamage", 3, "The extra attack damage points (in half hearts) that the iChisel inflicts when it is used to attack an entity.").getInt();

        /* block 
        category = "block";
//        useRoadLineTool = config.get(category, "useRoadLineTool", false, "Should the road line require a tool to break (If false, road lines can be broken in Adventure)").getBoolean();
//        getRoadLineTool = config.get(category, "getRoadLineTool", "pickaxe", "The tool that is able to break roadLines (requires useRoadLineTool to be true to take effect)").getString();
//        roadLineToolLevel = config.get(category, "roadLineToolLevel", 0,
//                "The lowest harvest level of the tool able to break the road lines (requires useRoadLineTool to be true to take effect) (0 = Wood/Gold, 1 = Stone, 2 = Iron, 3 = Diamond) Default: 0")
//                .getInt();

        category = "autochisel";
        autoChiselPowered = config.get(category, "autoChiselTakesPower", autoChiselPowered, "If false, the auto chisel will always run at full speed, and will not accept FE.").getBoolean();
        autoChiselNeedsPower = config.get(category, "autoChiselNeedsPower", autoChiselNeedsPower, "If true, the auto chisel will not function at all without power.").getBoolean();
        if (config.hasChanged()) {
            config.save();
        }
        return true;
    }

    public static boolean featureEnabled(Features feature) {
        return config.get("features", featureName(feature), true).getBoolean(true) && refreshConfig();
    }
*/
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
}