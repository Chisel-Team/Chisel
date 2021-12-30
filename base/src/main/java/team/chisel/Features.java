package team.chisel;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import com.tterrag.registrate.providers.RegistrateLangProvider;
import com.tterrag.registrate.providers.loot.RegistrateBlockLootTables;
import com.tterrag.registrate.util.entry.BlockEntry;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.WoodType;
import net.minecraft.world.level.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.data.ShapedRecipeBuilder;
import net.minecraft.data.ShapelessRecipeBuilder;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.DyeColor;
import net.minecraft.item.Items;
import net.minecraft.loot.IntClamper;
import net.minecraft.loot.ItemLootEntry;
import net.minecraft.loot.RandomValueRange;
import net.minecraft.loot.functions.ApplyBonus;
import net.minecraft.loot.functions.LimitCount;
import net.minecraft.loot.functions.SetCount;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraftforge.common.Tags;
import net.minecraftforge.registries.ForgeRegistries;
import team.chisel.api.block.BlockCreator;
import team.chisel.api.block.ChiselBlockFactory;
import team.chisel.client.data.ModelTemplates;
import team.chisel.client.data.VariantTemplates;
import team.chisel.client.sound.ChiselSoundTypes;
import team.chisel.common.block.BlockCarvable;
import team.chisel.common.block.BlockCarvableCarpet;
import team.chisel.common.init.ChiselCompatTags;

public class Features {

    private static final ChiselBlockFactory _FACTORY = ChiselBlockFactory.newFactory(Chisel.registrate());
    
    public static final Map<String, BlockEntry<BlockCarvable>> ALUMINUM = _FACTORY.newType(Material.METAL, "metals/aluminum")
            .applyTag(BlockTags.BEACON_BASE_BLOCKS)
            .setGroupName("Aluminum Block")
            .addTag(ChiselCompatTags.STORAGE_BLOCKS_ALUMINUM)
            .initialProperties(() -> Blocks.IRON_BLOCK)
            .color(MaterialColor.LIGHT_GRAY)
            .variations(VariantTemplates.METAL)
            .build();
    
    public static final Map<String, BlockEntry<BlockCarvable>> ANDESITE = _FACTORY.newType(Material.ROCK, "andesite")
            .addBlock(Blocks.ANDESITE)
            .addBlock(Blocks.POLISHED_ANDESITE)
            .initialProperties(() -> Blocks.ANDESITE)
            .variations(VariantTemplates.ROCK)
            .build();
    
    public static final Map<String, BlockEntry<BlockCarvable>> ANTIBLOCK = _FACTORY.newType(Material.ROCK, "antiblock", (p, v) -> new BlockCarvable(p, v))
            .layer(() -> RenderType::getCutout)
            .initialProperties(() -> Blocks.STONE)
            .color(MaterialColor.RED) // TODO colors per variant?
            .variations(VariantTemplates.colors(ModelTemplates.twoLayerWithTop("antiblock", false), (prov, block) -> 
                    new ShapedRecipeBuilder(block, 8)
                        .patternLine("SSS").patternLine("SGS").patternLine("SSS")
                        .key('S', Tags.Items.STONE)
                        .key('G', Tags.Items.DUSTS_GLOWSTONE)
                        .addCriterion("has_glowstone", prov.hasItem(Tags.Items.DUSTS_GLOWSTONE))
                        .build(prov)))
            .build();    
    
    public static final ImmutableList<WoodType> VANILLA_WOODS = ImmutableList.of(WoodType.OAK, WoodType.SPRUCE, WoodType.BIRCH, WoodType.ACACIA, WoodType.JUNGLE, WoodType.DARK_OAK);
 
    public static final Map<String, BlockEntry<BlockCarvable>> BRICKS = _FACTORY.newType(Material.ROCK, "bricks")
            .addBlock(Blocks.BRICKS)
            .initialProperties(() -> Blocks.BRICKS)
            .variations(VariantTemplates.ROCK)
            .build();
    
    public static final Map<String, BlockEntry<BlockCarvable>> BRONZE = _FACTORY.newType(Material.IRON, "metals/bronze")
            .applyTag(BlockTags.BEACON_BASE_BLOCKS)
            .setGroupName("Bronze Block")
            .addTag(ChiselCompatTags.STORAGE_BLOCKS_BRONZE)
            .initialProperties(() -> Blocks.IRON_BLOCK)
            .color(MaterialColor.ORANGE_TERRACOTTA)
            .variations(VariantTemplates.METAL)
            .build();
    
    /*
    public static final Map<String, BlockEntry<BlockCarvable>> BROWNSTONE = _FACTORY.newType(Material.ROCK, "brownstone")
            .variation("default")
                .recipe((prov, block) -> new ShapedRecipeBuilder(block, 4)
                        .patternLine(" S ").patternLine("SCS").patternLine(" S ")
                        .key('S', Tags.Items.SANDSTONE)
                        .key('C', Items.CLAY_BALL)
                        .addCriterion("has_clay", prov.hasItem(Items.CLAY_BALL))
                        .build(prov))
            .next("block")
            .next("doubleslab")
                .model(ModelTemplates.cubeColumn("doubleslab-side", "block"))
            .next("blocks")
            .next("weathered")
            .next("weathered_block")
            .next("weathered_doubleslab")
                .model(ModelTemplates.cubeColumn("weathered_doubleslab-side", "weathered_block"))
            .next("weathered_blocks")
            .next("weathered_half")
                .model(ModelTemplates.cubeBottomTop("weathered_half-side", "default", "weathered"))
            .next("weathered_block_half")
                .model(ModelTemplates.cubeBottomTop("weathered_block_half-side", "block", "weathered_block"))
            .build(b -> b.sound(SoundType.STONE).hardnessAndResistance(1.0F));
//      BlockSpeedHandler.speedupBlocks.add(b);
//  });
    */
    public static final Map<DyeColor, Map<String, BlockEntry<BlockCarvableCarpet>>> CARPET = Arrays.stream(DyeColor.values())
            .collect(Collectors.toMap(Function.identity(), color -> _FACTORY.newType(Material.WOOL, "carpet/" + (color.getString()), BlockCarvableCarpet::new)
                    .addBlock(new ResourceLocation(color.getString() + "_carpet")) // TODO improve this copied RL construction
                    .setGroupName(RegistrateLangProvider.toEnglishName(color.getString()) + " Carpet")
                    .model((prov, block) ->
                        prov.simpleBlock(block, prov.models()
                                .carpet("block/" + ModelTemplates.name(block), prov.modLoc("block/" + ModelTemplates.name(block).replace("carpet", "wool")))
                                .texture("particle", "#wool")))
                    .initialProperties(() -> ForgeRegistries.BLOCKS.getValue(new ResourceLocation(color.getString() + "_carpet")))
                    .variation("legacy")
                    .next("llama")
                    .build()));

    public static final Map<String, BlockEntry<BlockCarvable>> CHARCOAL = _FACTORY.newType(Material.ROCK, "charcoal")
            .addTag(ChiselCompatTags.STORAGE_BLOCKS_CHARCOAL)
            .initialProperties(() -> Blocks.COAL_BLOCK)
            .variation(VariantTemplates.withRecipe(VariantTemplates.RAW, (prov, block) -> new ShapelessRecipeBuilder(block, 1)
                    .addIngredient(Items.CHARCOAL)
                    .addCriterion("has_charcoal", prov.hasItem(Items.CHARCOAL))
                    .build(prov)))
            .variations(VariantTemplates.ROCK)
            .build();
    
    public static final Map<String, BlockEntry<BlockCarvable>> COAL = _FACTORY.newType(Material.ROCK, "coal")
            .addTag(Tags.Blocks.STORAGE_BLOCKS_COAL)
            .initialProperties(() -> Blocks.COAL_BLOCK)
            .variations(/*VariantTemplates.withUncraft(*/VariantTemplates.ROCK/*, Items.COAL)*/) // TODO
            .build();
    
    public static final Map<String, BlockEntry<BlockCarvable>> COBBLESTONE = _FACTORY.newType(Material.ROCK, "cobblestone")
            .addBlock(Blocks.COBBLESTONE)
            .initialProperties(() -> Blocks.COBBLESTONE)
            .variations(VariantTemplates.COBBLESTONE)
            .build();
    
    public static final Map<String, BlockEntry<BlockCarvable>> COBBLESTONE_MOSSY = _FACTORY.newType(Material.ROCK, "mossy_cobblestone")
            .addBlock(Blocks.MOSSY_COBBLESTONE)
            .initialProperties(() -> Blocks.MOSSY_COBBLESTONE)
            .variations(VariantTemplates.COBBLESTONE_MOSSY)
            .build();

    public static final Map<DyeColor, Map<String, BlockEntry<BlockCarvable>>> CONCRETE = Arrays.stream(DyeColor.values())
            .collect(Collectors.toMap(Function.identity(), color -> _FACTORY.newType(Material.ROCK, "concrete/" + color.getString())
                    .addBlock(new ResourceLocation(color.getString() + "_concrete")) // TODO improve this copied RL construction
                    .setGroupName(RegistrateLangProvider.toEnglishName(color.getString()) + " Concrete")
                    .initialProperties(() -> ForgeRegistries.BLOCKS.getValue(new ResourceLocation(color.getString() + "_concrete")))
                    .variations(VariantTemplates.ROCK)
                    .build()));
//  BlockSpeedHandler.speedupBlocks.add(b);

    public static final Map<String, BlockEntry<BlockCarvable>> DIABASE = _FACTORY.newType(Material.ROCK, "diabase")
            .initialProperties(() -> Blocks.BASALT)
            .variation(VariantTemplates.RAW)
            .variations(VariantTemplates.ROCK)
            .addTag(ChiselCompatTags.STONE_DIABASE)
            .build();

    public static final Map<String, BlockEntry<BlockCarvable>> DIORITE = _FACTORY.newType(Material.ROCK, "diorite")
            .addBlock(Blocks.DIORITE)
            .addBlock(Blocks.POLISHED_DIORITE)
            .initialProperties(() -> Blocks.DIORITE)
            .variations(VariantTemplates.ROCK)
            .build();

    public static final Map<String, BlockEntry<BlockCarvable>> EMERALD = _FACTORY.newType(Material.IRON, "emerald") 
            .applyTag(BlockTags.BEACON_BASE_BLOCKS)
            .initialProperties(() -> Blocks.EMERALD_BLOCK)
            /*.recipe((prov, block) -> new ShapelessRecipeBuilder(Items.EMERALD, 9)
                    .addIngredient(block)
                    .addCriterion("has_emerald_block", prov.hasItem(block))
                    .build(prov)) TODO: Match recipe seen in FeaturesOld*/
            .variation("panel")
            .next("panelclassic")
            .next("smooth")
            .next("chunk")
            .next("goldborder")
            .next("zelda")
            .next("cell")
            .next("cellbismuth")
            .next("four")
            .next("fourornate")
            .next("ornate")
            .next("masonryemerald")
            .next("emeraldcircle")
            .next("emeraldprismatic")
            .build();

    public static final Map<String, BlockEntry<BlockCarvable>> ENDSTONE = _FACTORY.newType(Material.ROCK, "end_stone")
            .addTag(Tags.Blocks.END_STONES)
            .addBlock(Blocks.END_STONE_BRICKS)
            .initialProperties(() -> Blocks.END_STONE)
            .variations(VariantTemplates.ROCK)
            .build();
    
    public static final Map<String, BlockEntry<BlockCarvable>> FACTORY = _FACTORY.newType(Material.IRON, "factory")
            .initialProperties(() -> Blocks.IRON_BLOCK)
            .color(MaterialColor.BROWN_TERRACOTTA)
            .variation("dots")
                .recipe((prov, block) -> new ShapedRecipeBuilder(block, 32)
                        .patternLine("SXS").patternLine("X X").patternLine("SXS")
                        .key('S', Tags.Items.STONE)
                        .key('X', Tags.Items.INGOTS_IRON)
                        .addCriterion("has_iron", prov.hasItem(Tags.Items.INGOTS_IRON))
                        .build(prov))
            .next("rust2")
            .next("rust")
            .next("platex")
            .next("wireframewhite")
            .next("wireframe")
            .next("hazard")
            .next("hazardorange")
            .next("circuit")
            .next("metalbox")
                .model(ModelTemplates.cubeColumn())
            .next("goldplate")
            .next("goldplating")
            .next("grinder")
            .next("plating")
            .next("rustplates")
            .next("column")
                .model(ModelTemplates.cubeColumn())
            .next("frameblue")
            .next("iceiceice")
            .next("tilemosaic")
            .next("vent")
                .model(ModelTemplates.cubeColumn())
            .next("wireframeblue")
            .build(b -> b.sound(ChiselSoundTypes.METAL));

    public static final Map<String, BlockEntry<BlockCarvable>> FUTURA = _FACTORY.newType(Material.ROCK, "futura")
            .recipe((prov, block) -> new ShapedRecipeBuilder(block, 8)
                .patternLine("SSS").patternLine("SGS").patternLine("SSS")
                .key('S', Tags.Items.STONE)
                .key('G', Tags.Items.DUSTS_REDSTONE)
                .addCriterion("has_redstone", prov.hasItem(Tags.Items.DUSTS_REDSTONE))
                .build(prov))
            .initialProperties(() -> Blocks.STONE)
            .color(MaterialColor.YELLOW)
            .variation("screen_metallic")
                .model(ModelTemplates.twoLayerTopShaded("screen_metallic", "screen_discoherent"))
            .next("screen_cyan")
                .model(ModelTemplates.twoLayerTopShaded("screen_cyan", "screen_discoherent"))
            .next("controller")
                .model(ModelTemplates.threeLayerTopShaded("controller_particle", "lines_plating", "rainbow_lines", "lines_invalid"))
            .next("wavy")
                .model(ModelTemplates.twoLayerTopShaded("rainbow_wave_particle", "rainbow_wave_base", "rainbow_wave"))
            .next("controller_purple")
                .model(ModelTemplates.threeLayerTopShaded("controller_unity_particle", "unity_lines_plating", "unity_lines", "lines_invalid"))
            .next("uber_wavy")
                .model(ModelTemplates.threeLayerTopShaded("orange_frame_particle", "orange_frame", "uber_rainbow", "screen_discoherent"))
            .build();

    public static final Map<String, BlockEntry<BlockCarvable>> GLOWSTONE = _FACTORY.newType(Material.GLASS, "glowstone")
            .addBlock(Blocks.GLOWSTONE)
            .loot((prov, block) -> prov.registerLootTable(block, RegistrateBlockLootTables.droppingWithSilkTouch(block,
                RegistrateBlockLootTables.withExplosionDecay(block,
                    ItemLootEntry.builder(Items.GLOWSTONE_DUST)
                        .acceptFunction(SetCount.builder(RandomValueRange.of(2.0F, 4.0F)))
                        .acceptFunction(ApplyBonus.uniformBonusCount(Enchantments.FORTUNE))
                        .acceptFunction(LimitCount.limitCount(IntClamper.clamp(1, 4)))))))
            .initialProperties(() -> Blocks.GLOWSTONE)
            .variations(VariantTemplates.STONE)
            .variation("extra/bismuth").localizedName("Bismuth")
            .next("extra/tiles_large_bismuth").localizedName("Tiles Large Bismuth")
            .next("extra/tiles_medium_bismuth").localizedName("Tiles Medium Bismuth")
            .next("extra/neon").localizedName("Neon")
            .next("extra/neon_panel").localizedName("Neon Panel")
            .build();

    public static final Map<String, BlockEntry<BlockCarvable>> GRANITE = _FACTORY.newType(Material.ROCK, "granite")
            .initialProperties(() -> Blocks.GRANITE)
            .variations(VariantTemplates.ROCK)
            .addBlock(Blocks.GRANITE)
            .addBlock(Blocks.POLISHED_GRANITE)
            .build();

    public static final Map<String, BlockEntry<BlockCarvable>> LABORATORY = _FACTORY.newType(Material.ROCK, "laboratory")
            .recipe((prov, block) -> new ShapedRecipeBuilder(block, 8)
                    .patternLine("***").patternLine("*X*").patternLine("***")
                    .key('*', Blocks.STONE)
                    .key('X', Items.QUARTZ)
                    .addCriterion("has_quartz", prov.hasItem(Items.QUARTZ))
                    .build(prov))
            .initialProperties(() -> Blocks.STONE)
            .color(MaterialColor.SNOW)
            .variation("wallpanel")
            .next("dottedpanel")
            .next("roundel")
            .next("wallvents")
                .model(ModelTemplates.cubeColumn())
            .next("largetile")
            .next("smalltile")
            .next("floortile")
            .next("checkertile")
            .next("fuzzscreen")
            .next("largesteel")
                .model(ModelTemplates.cubeColumn())
            .next("smallsteel")
                .model(ModelTemplates.cubeColumn())
            .next("directionleft")
                .model(ModelTemplates.cubeColumn())
            .next("directionright")
                .model(ModelTemplates.cubeColumn())
            .next("infocon")
                .model(ModelTemplates.cubeColumn())
            .build(b -> b.sound(ChiselSoundTypes.METAL));

    public static final Map<String, BlockEntry<BlockCarvable>> LIMESTONE = _FACTORY.newType(Material.ROCK, "limestone")
            .initialProperties(() -> Blocks.STONE)
            .color(MaterialColor.GREEN_TERRACOTTA)
            .variation(VariantTemplates.RAW)
            .variations(VariantTemplates.ROCK)
            .addTag(ChiselCompatTags.STONE_LIMESTONE)
            .build(b -> b.hardnessAndResistance(1F, 5F));

    public static final Map<String, BlockEntry<BlockCarvable>> MARBLE = _FACTORY.newType(Material.ROCK, "marble")
            .initialProperties(() -> Blocks.STONE)
            .color(MaterialColor.QUARTZ)
            .variation(VariantTemplates.RAW)
            .variations(VariantTemplates.ROCK)
            .addTag(ChiselCompatTags.STONE_MARBLE)
            .build(b -> b.hardnessAndResistance(1.75F, 10.0F));

    public static final Map<WoodType, Map<String, BlockEntry<BlockCarvable>>> PLANKS = VANILLA_WOODS.stream()
            .map(t -> Pair.of(t, new ResourceLocation(t.getName() + "_planks")))
            .collect(Collectors.toMap(Pair::getFirst, pair -> _FACTORY.newType(Material.WOOD, "planks/" + pair.getFirst().getName())
                    .addBlock(pair.getSecond())
                    .setGroupName(RegistrateLangProvider.toEnglishName(pair.getFirst().getName()) + " Planks")
                    .initialProperties(() -> ForgeRegistries.BLOCKS.getValue(pair.getSecond()))
                    .variations(VariantTemplates.PLANKS)
                    .build()));

    // TODO Temp Merge with above once 1.16 hits
    /*public static final Map<String, Map<String, BlockEntry<BlockCarvable>>> PLANKS_116 = ImmutableList.of("crimson", "warped").stream()
            .map(t -> Pair.of(t, new ResourceLocation(t + "_planks")))
            .collect(Collectors.toMap(Pair::getFirst, pair -> _FACTORY.newType(Material.WOOD, "planks/" + pair.getFirst())
                    .addBlock(pair.getSecond())
                    .setGroupName(RegistrateLangProvider.toEnglishName(pair.getFirst()) + " Planks")
                    .variations(VariantTemplates.PLANKS)
                    .build($ -> Block.Properties.from(ForgeRegistries.BLOCKS.getValue(pair.getSecond())))));//*/

    public static final Map<String, BlockEntry<BlockCarvable>> PRISMARINE = _FACTORY.newType(Material.ROCK, "prismarine")
            .initialProperties(() -> Blocks.PRISMARINE)
            .addBlock(Blocks.PRISMARINE)
            .addBlock(Blocks.PRISMARINE_BRICKS)
            .addBlock(Blocks.DARK_PRISMARINE)
            .variations(VariantTemplates.ROCK)
            .build();

    public static final Map<String, BlockEntry<BlockCarvable>> PURPUR = _FACTORY.newType(Material.ROCK, "purpur")
            .addBlock(Blocks.PURPUR_BLOCK)
            .addBlock(Blocks.PURPUR_PILLAR)
            .initialProperties(() -> Blocks.PURPUR_BLOCK)
            .variations(VariantTemplates.ROCK)
            .build();

    public static final Map<String, BlockEntry<BlockCarvable>> QUARTZ = _FACTORY.newType(Material.ROCK, "quartz")
            .addTag(Tags.Blocks.STORAGE_BLOCKS_QUARTZ)
            .addBlock(Blocks.QUARTZ_PILLAR)
            .addBlock(Blocks.CHISELED_QUARTZ_BLOCK)
            .addBlock(Blocks.SMOOTH_QUARTZ)
            .initialProperties(() -> Blocks.QUARTZ_BLOCK)
            .variations(VariantTemplates.ROCK)
            .build();

    public static final Map<String, BlockEntry<BlockCarvable>> RED_SANDSTONE = _FACTORY.newType(Material.ROCK, "red_sandstone")
            .addBlock(Blocks.RED_SANDSTONE)
            .addBlock(Blocks.CHISELED_RED_SANDSTONE)
            .addBlock(Blocks.CUT_RED_SANDSTONE)
            .addBlock(Blocks.SMOOTH_RED_SANDSTONE)
            .initialProperties(() -> Blocks.SANDSTONE)
            .color(MaterialColor.ADOBE)
            .variations(VariantTemplates.ROCK)
            .variation("extra/bevel_skeleton").localizedName("Bevel Skeleton")
            .next("extra/glyphs").localizedName("Glyphs")
            .next("extra/small").localizedName("Small")
            .build();

    public static final Map<String, BlockEntry<BlockCarvable>> REDSTONE = _FACTORY.newType(Material.IRON, "redstone", BlockCreators.redstoneCreator)
            .addTag(Tags.Blocks.STORAGE_BLOCKS_REDSTONE)
            .initialProperties(() -> Blocks.REDSTONE_BLOCK)
            .variations(VariantTemplates.STONE)
            .build(); //TODO: Recipe from FeaturesOld

    public static final Map<String, BlockEntry<BlockCarvable>> SANDSTONE = _FACTORY.newType(Material.ROCK, "sandstone")
            .addTag(Tags.Blocks.SANDSTONE)
            .initialProperties(() -> Blocks.SANDSTONE)
            .variations(VariantTemplates.ROCK)
            .variation("extra/bevel_creeper").localizedName("Bevel Creeper")
            .next("extra/glyphs").localizedName("Glyphs")
            .next("extra/small").localizedName("Small")
            //TODO: column-ctm
            .build();

    public static final Map<String, BlockEntry<BlockCarvable>> STONE_BRICKS = _FACTORY.newType(Material.ROCK, "stone_bricks")
            .addBlock(Blocks.STONE)
            .addBlock(Blocks.STONE_BRICKS)
            .addBlock(Blocks.CHISELED_STONE_BRICKS)
            .addBlock(Blocks.CRACKED_STONE_BRICKS)
            .initialProperties(() -> Blocks.STONE_BRICKS)
            .variations(VariantTemplates.ROCK)
            .variation("extra/largeornate").localizedName("Large Ornate")
            .next("extra/poison").localizedName("Poison")
            .next("extra/sunken").localizedName("Sunken")
            .build();

    public static final Map<String, BlockEntry<BlockCarvable>> TERRACOTTA = _FACTORY.newType(Material.ROCK, "terracotta")
            .addBlock(Blocks.TERRACOTTA)
            .initialProperties(() -> Blocks.TERRACOTTA)
            .variations(VariantTemplates.ROCK)
            .build();

    public static final Map<String, BlockEntry<BlockCarvable>> TYRIAN = _FACTORY.newType(Material.IRON, "tyrian")
            .initialProperties(() -> Blocks.IRON_BLOCK)
            .color(MaterialColor.CYAN_TERRACOTTA)
            .variation("shining")
                .recipe((prov, block) -> new ShapedRecipeBuilder(block, 32)
                        .patternLine("SSS").patternLine("SXS").patternLine("SSS")
                        .key('S', Tags.Items.STONE)
                        .key('X', Tags.Items.INGOTS_IRON)
                        .addCriterion("has_iron", prov.hasItem(Tags.Items.INGOTS_IRON))
                        .build(prov))
            .next("tyrian")
            .next("chaotic")
            .next("softplate")
            .next("rust")
            .next("elaborate")
            .next("routes")
            .next("platform")
            .next("platetiles")
            .next("diagonal")
            .next("dent")
            .next("blueplating")
            .next("black")
            .next("black2")
            .next("opening")
            .next("plate") // FIXME temporary texture
            .build();
    
    public static final Map<DyeColor, Map<String, BlockEntry<BlockCarvable>>> WOOL = Arrays.stream(DyeColor.values())
            .collect(Collectors.toMap(Function.identity(), color -> _FACTORY.newType(Material.WOOL, "wool/" + (color.getString()))
                    .addBlock(new ResourceLocation(color.getString() + "_wool")) // TODO improve this copied RL construction
                    .setGroupName(RegistrateLangProvider.toEnglishName(color.getString()) + " Wool")
                    .initialProperties(() -> ForgeRegistries.BLOCKS.getValue(new ResourceLocation(color.getString() + "_wool")))
                    .variation("legacy")
                    .next("llama")
                    .build()));
    
    public static void init() {}

    private static class BlockCreators {

        private static final BlockCreator<BlockCarvable> redstoneCreator = (props, data) -> new BlockCarvable(props, data) {
            @Override
            @Deprecated
            public boolean canProvidePower(BlockState state) {
                return true;
            }

            @Override
            @Deprecated
            public int getWeakPower(BlockState state, IBlockReader reader, BlockPos pos, Direction direction) {
                return 15;
            }
        };
    }
}
