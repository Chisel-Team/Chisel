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

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.SoundType;
import net.minecraft.block.WoodType;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.data.ShapedRecipeBuilder;
import net.minecraft.data.ShapelessRecipeBuilder;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.DyeColor;
import net.minecraft.item.Items;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.IntClamper;
import net.minecraft.world.storage.loot.ItemLootEntry;
import net.minecraft.world.storage.loot.RandomValueRange;
import net.minecraft.world.storage.loot.functions.ApplyBonus;
import net.minecraft.world.storage.loot.functions.LimitCount;
import net.minecraft.world.storage.loot.functions.SetCount;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.registries.ForgeRegistries;
import team.chisel.api.block.ChiselBlockFactory;
import team.chisel.client.data.ModelTemplates;
import team.chisel.client.data.VariantTemplates;
import team.chisel.client.sound.ChiselSoundTypes;
import team.chisel.common.block.BlockCarvable;
import team.chisel.common.block.BlockCarvableCarpet;
import team.chisel.common.init.ChiselCompatTags;

public class Features {

    private static final ChiselBlockFactory _FACTORY = ChiselBlockFactory.newFactory(Chisel.registrate());
    
    public static final Map<String, BlockEntry<BlockCarvable>> ALUMINUM = _FACTORY.newType(Material.IRON, "metals/aluminum")
            .setGroupName("Aluminum Block")
            .addTag(ChiselCompatTags.STORAGE_BLOCKS_ALUMINUM)
            .variations(VariantTemplates.METAL)
            .build(b -> b.sound(SoundType.METAL).harvestTool(ToolType.PICKAXE).harvestLevel(1));
    
    public static final Map<String, BlockEntry<BlockCarvable>> ANDESITE = _FACTORY.newType(Material.ROCK, "andesite")
            .addBlock(Blocks.ANDESITE)
            .addBlock(Blocks.POLISHED_ANDESITE)
            .variations(VariantTemplates.ROCK)
            .build(b -> b.hardnessAndResistance(1.5F, 30.0F).sound(SoundType.STONE));
    
    public static final Map<String, BlockEntry<BlockCarvable>> ANTIBLOCK = _FACTORY.newType(Material.ROCK, "antiblock", (p, v) -> new BlockCarvable(p, v))
            .layer(() -> RenderType::getCutout)
            .variations(VariantTemplates.colors(ModelTemplates.twoLayerWithTop("antiblock", false), (prov, block) -> 
                    new ShapedRecipeBuilder(block, 8)
                        .patternLine("SSS").patternLine("SGS").patternLine("SSS")
                        .key('S', Tags.Items.STONE)
                        .key('G', Tags.Items.DUSTS_GLOWSTONE)
                        .addCriterion("has_glowstone", prov.hasItem(Tags.Items.DUSTS_GLOWSTONE))
                        .build(prov)))
            .build();
    
    public static final Map<String, BlockEntry<BlockCarvable>> BASALT = _FACTORY.newType(Material.ROCK, "basalt")
            .variation(VariantTemplates.RAW)
            .variations(VariantTemplates.ROCK)
            .addTag(ChiselCompatTags.STONE_BASALT)
            .build(b -> b.hardnessAndResistance(1.5F, 10.0F).sound(SoundType.STONE));
    
    
    public static final ImmutableList<WoodType> VANILLA_WOODS = ImmutableList.of(WoodType.OAK, WoodType.SPRUCE, WoodType.BIRCH, WoodType.ACACIA, WoodType.JUNGLE, WoodType.DARK_OAK);
 
    public static final Map<String, BlockEntry<BlockCarvable>> BRICKS = _FACTORY.newType(Material.ROCK, "bricks")
            .addBlock(Blocks.BRICKS)
            .variations(VariantTemplates.ROCK)
            .build(p -> p.hardnessAndResistance(2.0F, 6.0F));
    
    public static final Map<String, BlockEntry<BlockCarvable>> BRONZE = _FACTORY.newType(Material.IRON, "metals/bronze")
            .setGroupName("Bronze Block")
            .addTag(ChiselCompatTags.STORAGE_BLOCKS_BRONZE)
            .variations(VariantTemplates.METAL)
            .build(p -> p.sound(SoundType.METAL).hardnessAndResistance(5.0F).harvestTool(ToolType.PICKAXE).harvestLevel(1));
    
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
            .collect(Collectors.toMap(Function.identity(), color -> _FACTORY.newType(Material.WOOL, "carpet/" + (color.getName()), BlockCarvableCarpet::new)
                    .addBlock(new ResourceLocation(color.getName() + "_carpet"))
                    .setGroupName(RegistrateLangProvider.toEnglishName(color.getName()) + " Carpet")
                    .model((prov, block) ->
                        prov.simpleBlock(block, prov.models()
                                .carpet("block/" + ModelTemplates.name(block), prov.modLoc("block/" + ModelTemplates.name(block).replace("carpet", "wool")))
                                .texture("particle", "#wool")))
                    .variation("legacy")
                    .next("llama")
                    .build(b -> b.sound(SoundType.CLOTH).hardnessAndResistance(0.8F))));
    
    public static final Map<String, BlockEntry<BlockCarvable>> CHARCOAL = _FACTORY.newType(Material.ROCK, "charcoal")
            .addTag(ChiselCompatTags.STORAGE_BLOCKS_CHARCOAL)
            .variation(VariantTemplates.withRecipe(VariantTemplates.RAW, (prov, block) -> new ShapelessRecipeBuilder(block, 1)
                    .addIngredient(Items.CHARCOAL)
                    .addCriterion("has_charcoal", prov.hasItem(Items.CHARCOAL))
                    .build(prov)))
            .variations(VariantTemplates.ROCK)
            .build(b -> b.hardnessAndResistance(5.0F, 10.0F).sound(SoundType.STONE));
    
    public static final Map<String, BlockEntry<BlockCarvable>> COAL = _FACTORY.newType(Material.ROCK, "coal")
            .addTag(Tags.Blocks.STORAGE_BLOCKS_COAL)
            .variations(/*VariantTemplates.withUncraft(*/VariantTemplates.ROCK/*, Items.COAL)*/) // TODO
            .build(b -> b.hardnessAndResistance(5.0F, 10.0F).sound(SoundType.STONE));
    
    public static final Map<String, BlockEntry<BlockCarvable>> COBBLESTONE = _FACTORY.newType(Material.ROCK, "cobblestone")
            .addBlock(Blocks.COBBLESTONE)
            .variations(VariantTemplates.COBBLESTONE)
            .build(b -> b.hardnessAndResistance(2.0F, 10.0F).sound(SoundType.STONE));
    
    public static final Map<String, BlockEntry<BlockCarvable>> COBBLESTONE_MOSSY = _FACTORY.newType(Material.ROCK, "mossy_cobblestone")
            .addBlock(Blocks.MOSSY_COBBLESTONE)
            .variations(VariantTemplates.COBBLESTONE_MOSSY)
            .build(b -> b.hardnessAndResistance(2.0F, 10.0F).sound(SoundType.STONE));
    
    public static final Map<DyeColor, Map<String, BlockEntry<BlockCarvable>>> CONCRETE = Arrays.stream(DyeColor.values())
            .collect(Collectors.toMap(Function.identity(), color -> _FACTORY.newType(Material.ROCK, "concrete/" + color.getName())
                    .addBlock(new ResourceLocation(color.getName() + "_concrete"))
                    .setGroupName(RegistrateLangProvider.toEnglishName(color.getName()) + " Concrete")
                    .variations(VariantTemplates.ROCK)
                    .build(p -> p.sound(SoundType.STONE).hardnessAndResistance(1.8F))));
//  BlockSpeedHandler.speedupBlocks.add(b);

    public static final Map<String, BlockEntry<BlockCarvable>> DIORITE = _FACTORY.newType(Material.ROCK, "diorite")
            .addBlock(Blocks.DIORITE)
            .addBlock(Blocks.POLISHED_DIORITE)
            .variations(VariantTemplates.ROCK)
            .build(b -> b.hardnessAndResistance(1.5F, 6.0F).sound(SoundType.STONE));

    public static final Map<String, BlockEntry<BlockCarvable>> ENDSTONE = _FACTORY.newType(Material.ROCK, "end_stone")
            .addTag(Tags.Blocks.END_STONES)
            .addBlock(Blocks.END_STONE_BRICKS)
            .variations(VariantTemplates.ROCK)
            .build(b -> b.hardnessAndResistance(3.0F, 15.0F).sound(SoundType.STONE));
    
    public static final Map<String, BlockEntry<BlockCarvable>> FACTORY = _FACTORY.newType(Material.IRON, "factory")
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
            .loot((prov, block) -> prov.registerLootTable(block, RegistrateBlockLootTables.droppingWithSilkTouch(block, RegistrateBlockLootTables.withExplosionDecay(block, ItemLootEntry.builder(Items.GLOWSTONE_DUST).acceptFunction(SetCount.builder(RandomValueRange.of(2.0F, 4.0F))).acceptFunction(ApplyBonus.uniformBonusCount(Enchantments.FORTUNE)).acceptFunction(LimitCount.func_215911_a(IntClamper.func_215843_a(1, 4)))))))
            .variation("cracked")
            .next("soft_bricks").localizedName("Weathered Bricks")
            .next("cracked_bricks")
            .next("triple_bricks").localizedName("Wide Bricks")
            .next("encased_bricks")
            .next("braid")
            .next("array").localizedName("Arrayed Bricks")
            .next("tiles_large").localizedName("Big Tile")
            .next("tiles_small").localizedName("Small Tiles")
            .next("chaotic_medium").localizedName("Disordered Tiles")
            .next("chaotic_small").localizedName("Small Disordered Tiles")
            .next("dent")
            .next("french_1")
            .next("french_2")
            .next("jellybean")
            .next("layers")
            .next("mosaic")
            .next("ornate")
            .next("panel")
            .next("road")
            .next("slanted")
            .next("solid_bricks").localizedName("Bricks")
            .next("small_bricks")
            .next("circular")
            .next("tiles_medium").localizedName("Tiles")
            .next("pillar")
                .model(ModelTemplates.cubeColumn())
            .next("twisted")
                .model(ModelTemplates.cubeColumn())
            .next("prism")
            .next("extra/bismuth").localizedName("Bismuth")
            .next("extra/tiles_large_bismuth").localizedName("Tiles Large Bismuth")
            .next("extra/tiles_medium_bismuth").localizedName("Tiles Medium Bismuth")
            .next("extra/neon").localizedName("Neon")
            .next("extra/neon_panel").localizedName("Neon Panel")
            .build(b -> b.hardnessAndResistance(0.3F, 1.5F).sound(SoundType.GLASS).lightValue(15));

    public static final Map<String, BlockEntry<BlockCarvable>> GRANITE = _FACTORY.newType(Material.ROCK, "granite")
            .variations(VariantTemplates.ROCK)
            .addBlock(Blocks.GRANITE)
            .addBlock(Blocks.POLISHED_GRANITE)
            .build(b -> b.hardnessAndResistance(1.5F, 30.0F).sound(SoundType.STONE));

    public static final Map<String, BlockEntry<BlockCarvable>> LIMESTONE = _FACTORY.newType(Material.ROCK, "limestone")
            .variation(VariantTemplates.RAW)
            .variations(VariantTemplates.ROCK)
            .addTag(ChiselCompatTags.STONE_LIMESTONE)
            .build(b -> b.hardnessAndResistance(1.5F, 10.0F).sound(SoundType.STONE));

    public static final Map<String, BlockEntry<BlockCarvable>> MARBLE = _FACTORY.newType(Material.ROCK, "marble")
            .variation(VariantTemplates.RAW)
            .variations(VariantTemplates.ROCK)
            .addTag(ChiselCompatTags.STONE_MARBLE)
            .build(b -> b.hardnessAndResistance(1.5F, 10.0F).sound(SoundType.STONE));

    public static final Map<WoodType, Map<String, BlockEntry<BlockCarvable>>> PLANKS = VANILLA_WOODS.stream()
            .map(t -> Pair.of(t, new ResourceLocation(t.getName() + "_planks")))
            .collect(Collectors.toMap(Pair::getFirst, pair -> _FACTORY.newType(Material.WOOD, "planks/" + pair.getFirst().getName())
                    .addBlock(pair.getSecond())
                    .setGroupName(RegistrateLangProvider.toEnglishName(pair.getFirst().getName()) + " Planks")
                    .variations(VariantTemplates.PLANKS)
                    .build($ -> Block.Properties.from(ForgeRegistries.BLOCKS.getValue(pair.getSecond())))));

    // TODO Temp Merge with above once 1.16 hits
    /*public static final Map<String, Map<String, BlockEntry<BlockCarvable>>> PLANKS_116 = ImmutableList.of("crimson", "warped").stream()
            .map(t -> Pair.of(t, new ResourceLocation(t + "_planks")))
            .collect(Collectors.toMap(Pair::getFirst, pair -> _FACTORY.newType(Material.WOOD, "planks/" + pair.getFirst())
                    .addBlock(pair.getSecond())
                    .setGroupName(RegistrateLangProvider.toEnglishName(pair.getFirst()) + " Planks")
                    .variations(VariantTemplates.PLANKS)
                    .build($ -> Block.Properties.from(ForgeRegistries.BLOCKS.getValue(pair.getSecond())))));//*/

    public static final Map<String, BlockEntry<BlockCarvable>> PURPUR = _FACTORY.newType(Material.ROCK, "purpur")
            .addBlock(Blocks.PURPUR_BLOCK)
            .addBlock(Blocks.PURPUR_PILLAR)
            .variations(VariantTemplates.ROCK)
            .build(b -> b.hardnessAndResistance(1.5F, 10.0F).sound(SoundType.STONE));

    public static final Map<String, BlockEntry<BlockCarvable>> QUARTZ = _FACTORY.newType(Material.ROCK, "quartz")
            .addTag(Tags.Blocks.STORAGE_BLOCKS_QUARTZ)
            .addBlock(Blocks.QUARTZ_PILLAR)
            .addBlock(Blocks.CHISELED_QUARTZ_BLOCK)
            .addBlock(Blocks.SMOOTH_QUARTZ)
            .variations(VariantTemplates.ROCK)
            .build(b -> b.hardnessAndResistance(0.8F, 4.0F).sound(SoundType.STONE));

    public static final Map<String, BlockEntry<BlockCarvable>> RED_SANDSTONE = _FACTORY.newType(Material.ROCK, "red_sandstone")
            .addBlock(Blocks.RED_SANDSTONE)
            .addBlock(Blocks.CHISELED_RED_SANDSTONE)
            .addBlock(Blocks.CUT_RED_SANDSTONE)
            .addBlock(Blocks.SMOOTH_RED_SANDSTONE)
            .variations(VariantTemplates.ROCK)
            .variation("extra/bevel_skeleton").localizedName("Bevel Skeleton")
            .next("extra/glyphs").localizedName("Glyphs")
            .next("extra/small").localizedName("Small")
            .build(b -> b.hardnessAndResistance(0.8F).sound(SoundType.STONE));

    public static final Map<String, BlockEntry<BlockCarvable>> SANDSTONE = _FACTORY.newType(Material.ROCK, "sandstone")
            .addTag(Tags.Blocks.SANDSTONE)
            .variations(VariantTemplates.ROCK)
            .variation("extra/bevel_creeper").localizedName("Bevel Creeper")
            .next("extra/glyphs").localizedName("Glyphs")
            .next("extra/small").localizedName("Small")
            //TODO: column-ctm
            .build(b -> b.hardnessAndResistance(0.8F).sound(SoundType.STONE));

    public static final Map<String, BlockEntry<BlockCarvable>> STONE_BRICKS = _FACTORY.newType(Material.ROCK, "stone_bricks")
            .addBlock(Blocks.STONE)
            .addBlock(Blocks.STONE_BRICKS)
            .addBlock(Blocks.CHISELED_STONE_BRICKS)
            .addBlock(Blocks.CRACKED_STONE_BRICKS)
            .variations(VariantTemplates.ROCK)
            .variation("extra/largeornate").localizedName("Large Ornate")
            .next("extra/poison").localizedName("Poison")
            .next("extra/sunken").localizedName("Sunken")
            .build(p -> p.hardnessAndResistance(1.5F, 10.0F));

    public static final Map<String, BlockEntry<BlockCarvable>> TYRIAN = _FACTORY.newType(Material.IRON, "tyrian")
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
            .build(b -> b.sound(SoundType.METAL));
    
    public static final Map<DyeColor, Map<String, BlockEntry<BlockCarvable>>> WOOL = Arrays.stream(DyeColor.values())
            .collect(Collectors.toMap(Function.identity(), color -> _FACTORY.newType(Material.WOOL, "wool/" + (color.getName()))
                    .addBlock(new ResourceLocation(color.getName() + "_wool"))
                    .setGroupName(RegistrateLangProvider.toEnglishName(color.getName()) + " Wool")
                    .variation("legacy")
                    .next("llama")
                    .build(b -> b.sound(SoundType.CLOTH).hardnessAndResistance(0.8F))));
    
    public static void init() {}
}
