package team.chisel.common.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class Configurations {

    public static ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static ForgeConfigSpec CONFIG;

    //TODO make sure it's loaded on time or not use property but override method.
    public static ForgeConfigSpec.ConfigValue<Double> concreteVelocityMult;
    public static ForgeConfigSpec.IntValue factoryBlockAmount;
    public static ForgeConfigSpec.BooleanValue allowMossy;
    public static ForgeConfigSpec.BooleanValue allowSmoothStone;
    public static ForgeConfigSpec.BooleanValue chiselRecipe;
    public static ForgeConfigSpec.BooleanValue chiselStoneToCobbleBricks;
    public static ForgeConfigSpec.BooleanValue chiselBackToVanillaLeaves;

    public static ForgeConfigSpec.IntValue marbleAmount;
    public static ForgeConfigSpec.IntValue limestoneAmount;
    public static ForgeConfigSpec.BooleanValue basaltSpecialGen;
    public static ForgeConfigSpec.IntValue basaltSideThickness;
    public static ForgeConfigSpec.IntValue basaltBottomThickness;
    public static ForgeConfigSpec.IntValue basaltVeinAmount;

    public static ForgeConfigSpec.BooleanValue oldPillars;
    public static ForgeConfigSpec.BooleanValue blockDescriptions;
    public static ForgeConfigSpec.BooleanValue imTooGoodForDescriptions;

    public static ForgeConfigSpec.BooleanValue allowChiselDamage;
    public static ForgeConfigSpec.IntValue ironChiselMaxDamage;
    public static ForgeConfigSpec.IntValue diamondChiselMaxDamage;
    public static ForgeConfigSpec.IntValue hitechChiselMaxDamage;
    public static ForgeConfigSpec.BooleanValue ironChiselCanLeftClick;
    public static ForgeConfigSpec.BooleanValue ironChiselHasModes;
    public static ForgeConfigSpec.ConfigValue<Integer> ironChiselAttackDamage;
    public static ForgeConfigSpec.ConfigValue<Integer> diamondChiselAttackDamage;
    public static ForgeConfigSpec.ConfigValue<Integer> hitechChiselAttackDamage;
    public static ForgeConfigSpec.BooleanValue allowChiselCrossColors;

    public static ForgeConfigSpec.BooleanValue useRoadLineTool;
    public static ForgeConfigSpec.ConfigValue<String> getRoadLineTool;
    public static ForgeConfigSpec.IntValue roadLineToolLevel;
    
    public static ForgeConfigSpec.BooleanValue autoChiselPowered;
    public static ForgeConfigSpec.BooleanValue autoChiselNeedsPower;

    static {

        // general
        BUILDER.push("general");
        concreteVelocityMult = BUILDER.comment("The factor that concrete_white increases your velocity. Default is 1.35, set to 1 for no change.").define("concreteVelocityMult", 1.35d);
        allowMossy = BUILDER.comment("If true, you can chisel stone brick to mossy stone brick.").define("allowBrickToMossyInChisel", true);
        //factoryBlockAmount = BUILDER.comment("Amount of blocks you get from").define("amountYouGetFromFactoryBlockCrafting", 32).getInt(32); JSON recipes
        //chiselRecipe = config.get(category, "chiselAlternateRecipe", false, "Use alternative crafting recipe for the chisel").getBoolean(false); JSON recipes
        chiselStoneToCobbleBricks = BUILDER.comment("Allow stone to be chiseled to/from stone bricks.").define("chiselStoneToStoneBricks", true);
        chiselBackToVanillaLeaves = BUILDER.comment("If this is true, you can chisel from the chisel leaves back to vanilla ones. If it is false, you cannot.").define("chiselBackToVanillaLeaves", false);
        BUILDER.pop();

        // worldgen
        BUILDER.push("worldgen");
        marbleAmount = BUILDER.comment("Amount of marble to generate in the world; use 0 for none").defineInRange("marbleAmount", 20, 0, 30);
        limestoneAmount = BUILDER.comment("Amount of limestone to generate in the world; use 0 for none").defineInRange("limestoneAmount", 18, 0, 30);

        BUILDER.push("basalt");
        basaltSpecialGen = BUILDER.comment("True to generate basalt only around lava lakes. False to do standard vein generation.").define("specialGen", true);
        basaltSideThickness = BUILDER.comment("Thickness of the basalt around the sides of lava lakes. 0 for none.").defineInRange("sideThickness", 1, 0, 5);
        basaltBottomThickness = BUILDER.comment("Thickness of the basalt at the bottom of lava lakes. 0 for none.").defineInRange("bottomThickness", 3, 0, 5);
        
        basaltVeinAmount = BUILDER.comment( "Amount of basalt to generate in the world if not using special generation. Has no effect if basaltSpecialGen is true. Use 0 for none").defineInRange("veinAmount", 15, 0, 30);
        BUILDER.pop();

        BUILDER.pop();

        // client
        BUILDER.push("client");
        oldPillars = BUILDER.comment("Use old pillar textures").define("pillarOldGraphics", false);
        blockDescriptions = BUILDER.comment("Make variations of blocks have the same name, and use the description in tooltip to distinguish them.").define("tooltipsUseBlockDescriptions", true);
        BUILDER.pop();

        // chisel
        BUILDER.push("chisel");
        allowChiselDamage = BUILDER.comment("Should the chisel be damageable and take damage when it chisels something.").define("allowChiselDamage", true);
        ironChiselMaxDamage = BUILDER.comment("The max damage of the standard iron chisel.").defineInRange("ironChiselMaxDamage", 512, 1, Short.MAX_VALUE);
        diamondChiselMaxDamage = BUILDER.comment("The max damage of the diamond chisel.").defineInRange("diamondChiselMaxDamage", 5056, 1, Short.MAX_VALUE);
        hitechChiselMaxDamage = BUILDER.comment("The max damage of the iChisel.").defineInRange("hitechChiselMaxDamage", 10048, 1, Short.MAX_VALUE);

        ironChiselCanLeftClick = BUILDER.comment("If this is true, the iron chisel can left click chisel blocks. If false, it cannot.").define("ironChiselCanLeftClick", true);
        ironChiselHasModes = BUILDER.comment("If this is true, the iron chisel can change its chisel mode just as the diamond chisel can.").define("ironChiselHasModes", false);
//        allowChiselCrossColors = config.get(category, "allowChiselCrossColors", true, "Should someone be able to chisel something into a different color.").getBoolean();

        ironChiselAttackDamage = BUILDER.comment("The extra attack damage points (in half hearts) that the iron chisel inflicts when it is used to attack an entity.").define("ironChiselAttackDamage", 2);
        diamondChiselAttackDamage = BUILDER.comment("The extra attack damage points (in half hearts) that the diamond chisel inflicts when it is used to attack an entity.").define("diamondChiselAttackDamage", 3);
        hitechChiselAttackDamage = BUILDER.comment("The extra attack damage points (in half hearts) that the iChisel inflicts when it is used to attack an entity.").define("hitechChiselAttackDamage", 3);
        BUILDER.pop();

        // block
        //category = "block";
//        useRoadLineTool = config.get(category, "useRoadLineTool", false, "Should the road line require a tool to break (If false, road lines can be broken in Adventure)").getBoolean();
//        getRoadLineTool = config.get(category, "getRoadLineTool", "pickaxe", "The tool that is able to break roadLines (requires useRoadLineTool to be true to take effect)").getString();
//        roadLineToolLevel = config.get(category, "roadLineToolLevel", 0,
//                "The lowest harvest level of the tool able to break the road lines (requires useRoadLineTool to be true to take effect) (0 = Wood/Gold, 1 = Stone, 2 = Iron, 3 = Diamond) Default: 0")
//                .getInt();

        BUILDER.push("autochisel");
        autoChiselPowered = BUILDER.comment("If false, the auto chisel will always run at full speed, and will not accept FE.").define("autoChiselTakesPower", true);
        autoChiselNeedsPower = BUILDER.comment("If true, the auto chisel will not function at all without power.").define("autoChiselNeedsPower", false);
        BUILDER.pop();

        CONFIG = BUILDER.build();
    }

//    public static boolean featureEnabled(Features feature) {
//        return CONFIG.get("features", featureName(feature), true).getBoolean(true) && refreshConfig();
//    }
}