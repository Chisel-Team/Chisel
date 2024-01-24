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

import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraft.world.level.storage.loot.IntRange;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.functions.LimitCount;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.minecraftforge.common.Tags;
import net.minecraftforge.registries.ForgeRegistries;
import team.chisel.api.block.BlockCreator;
import team.chisel.api.block.ChiselBlockFactory;
import team.chisel.client.data.ModelTemplates;
import team.chisel.client.data.VariantTemplates;
import team.chisel.client.sound.ChiselSoundTypes;
import team.chisel.common.block.BlockCarvable;
import team.chisel.common.block.BlockCarvableCarpet;
import team.chisel.common.block.BlockCarvablePane;
import team.chisel.common.init.ChiselCompatTags;

public class Features {

    private static final ChiselBlockFactory _FACTORY = ChiselBlockFactory.newFactory(Chisel.registrate());
    
    public static final Map<String, BlockEntry<BlockCarvable>> ALUMINUM = _FACTORY.newType(Material.METAL, "metals/aluminum")
            .applyTag(BlockTags.BEACON_BASE_BLOCKS)
            .applyTag(BlockTags.MINEABLE_WITH_PICKAXE)
            .applyTag(BlockTags.NEEDS_STONE_TOOL)
            .setGroupName("Aluminum Block")
            .equivalentTo(ChiselCompatTags.STORAGE_BLOCKS_ALUMINUM)
            .initialProperties(() -> Blocks.IRON_BLOCK)
            .color(MaterialColor.COLOR_LIGHT_GRAY)
            .variations(VariantTemplates.METAL)
            .build();
    
    public static final Map<String, BlockEntry<BlockCarvable>> ANDESITE = _FACTORY.newType(Material.STONE, "andesite")
            .addBlock(Blocks.ANDESITE)
            .addBlock(Blocks.POLISHED_ANDESITE)
            .applyTag(BlockTags.MINEABLE_WITH_PICKAXE)
            .initialProperties(() -> Blocks.ANDESITE)
            .variations(VariantTemplates.ROCK)
            .build();
    
    public static final Map<String, BlockEntry<BlockCarvable>> ANTIBLOCK = _FACTORY.newType(Material.STONE, "antiblock", (p, v) -> new BlockCarvable(p, v))
            .applyTag(BlockTags.MINEABLE_WITH_PICKAXE)
            .layer(() -> RenderType::cutout)
            .initialProperties(() -> Blocks.STONE)
            .color(MaterialColor.COLOR_RED) // TODO colors per variant?
            .variations(VariantTemplates.colors(ModelTemplates.twoLayerWithTop("antiblock", false), (prov, block) -> 
                    new ShapedRecipeBuilder(block, 8)
                        .pattern("SSS").pattern("SGS").pattern("SSS")
                        .define('S', Tags.Items.STONE)
                        .define('G', Tags.Items.DUSTS_GLOWSTONE)
                        .unlockedBy("has_glowstone", prov.has(Tags.Items.DUSTS_GLOWSTONE))
                        .save(prov)))
            .build();    

    public static final Map<String, BlockEntry<BlockCarvable>> BASALT = _FACTORY.newType(Material.STONE, "basalt")
            .addBlock(Blocks.BASALT)
            .applyTag(BlockTags.MINEABLE_WITH_PICKAXE)
            .initialProperties(() -> Blocks.BASALT)
            .variations(VariantTemplates.ROCK)
            .build();
    public static final ImmutableList<WoodType> VANILLA_WOODS = ImmutableList.of(WoodType.OAK, WoodType.SPRUCE, WoodType.BIRCH, WoodType.ACACIA, WoodType.JUNGLE, WoodType.DARK_OAK);
 
    public static final Map<String, BlockEntry<BlockCarvable>> BRICKS = _FACTORY.newType(Material.STONE, "bricks")
            .addBlock(Blocks.BRICKS)
            .applyTag(BlockTags.MINEABLE_WITH_PICKAXE)
            .initialProperties(() -> Blocks.BRICKS)
            .variations(VariantTemplates.ROCK)
            .build();
    
    public static final Map<String, BlockEntry<BlockCarvable>> BRONZE = _FACTORY.newType(Material.METAL, "metals/bronze")
            .applyTag(BlockTags.BEACON_BASE_BLOCKS)
            .applyTag(BlockTags.MINEABLE_WITH_PICKAXE)
            .applyTag(BlockTags.NEEDS_IRON_TOOL)
            .setGroupName("Bronze Block")
            .addTag(ChiselCompatTags.STORAGE_BLOCKS_BRONZE)
            .initialProperties(() -> Blocks.IRON_BLOCK)
            .color(MaterialColor.TERRACOTTA_ORANGE)
            .variations(VariantTemplates.METAL)
            .build();

    public static final Map<String, BlockEntry<BlockCarvable>> BROWNSTONE = _FACTORY.newType(Material.STONE, "brownstone")
            .variation("default")
                .recipe((prov, block) -> new ShapedRecipeBuilder(block, 4)
                        .pattern(" S ").pattern("SCS").pattern(" S ")
                        .define('S', Tags.Items.SANDSTONE)
                        .define('C', Items.CLAY_BALL)
                        .unlockedBy("has_clay", prov.has(Items.CLAY_BALL))
                        .save(prov))
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
            .build(b -> b.sound(SoundType.STONE).strength(1.0f));
    //BlockSpeedHandler.speedupBlocks.add(b);

    public static final Map<DyeColor, Map<String, BlockEntry<BlockCarvableCarpet>>> CARPET = Arrays.stream(DyeColor.values())
            .collect(Collectors.toMap(Function.identity(), color -> _FACTORY.newType(Material.WOOL, "carpet/" + (color.getSerializedName()), BlockCarvableCarpet::new)
                    .addBlock(new ResourceLocation(color.getSerializedName() + "_carpet")) // TODO improve this copied RL construction
                    .setGroupName(RegistrateLangProvider.toEnglishName(color.getSerializedName()) + " Carpet")
                    .model((prov, block) ->
                        prov.simpleBlock(block, prov.models()
                                .carpet("block/" + ModelTemplates.name(block), prov.modLoc("block/" + ModelTemplates.name(block).replace("carpet", "wool")))
                                .texture("particle", "#wool")))
                    .initialProperties(() -> ForgeRegistries.BLOCKS.getValue(new ResourceLocation(color.getSerializedName() + "_carpet")))
                    .variation("legacy")
                    .next("llama")
                    .build()));

    public static final Map<String, BlockEntry<BlockCarvable>> CHARCOAL = _FACTORY.newType(Material.STONE, "charcoal")
            .equivalentTo(ChiselCompatTags.STORAGE_BLOCKS_CHARCOAL)
            .applyTag(BlockTags.MINEABLE_WITH_PICKAXE)
            .applyTag(BlockTags.NEEDS_STONE_TOOL)
            .initialProperties(() -> Blocks.COAL_BLOCK)
            .variation(VariantTemplates.withRecipe(VariantTemplates.RAW, (prov, block) -> new ShapelessRecipeBuilder(block, 1)
                    .requires(Items.CHARCOAL, 9)
                    .unlockedBy("has_charcoal", prov.has(Items.CHARCOAL))
                    .save(prov)))
            .variations(VariantTemplates.ROCK)
            .build();
    
    public static final Map<String, BlockEntry<BlockCarvable>> COAL = _FACTORY.newType(Material.STONE, "coal")
            .equivalentTo(Tags.Blocks.STORAGE_BLOCKS_COAL)
            .applyTag(BlockTags.MINEABLE_WITH_PICKAXE)
            .applyTag(BlockTags.NEEDS_STONE_TOOL)
            .initialProperties(() -> Blocks.COAL_BLOCK)
            .variations(/*VariantTemplates.withUncraft(*/VariantTemplates.ROCK/*, Items.COAL)*/) // TODO
            .build();

    public static final Map<String, BlockEntry<BlockCarvable>> CLOUD = _FACTORY.newType(Material.CLOTH_DECORATION, "cloud")
            .variation("cloud")
            .next("large")
            .next("vertical")
            .next("grid")
            .build(b -> b.sound(SoundType.WOOL).explosionResistance(0.3F));

    public static final Map<String, BlockEntry<BlockCarvable>> COBBLESTONE = _FACTORY.newType(Material.STONE, "cobblestone")
            .addBlock(Blocks.COBBLESTONE)
            .applyTag(Tags.Blocks.COBBLESTONE)
            .applyTag(ItemTags.STONE_TOOL_MATERIALS)
            .applyTag(ItemTags.STONE_CRAFTING_MATERIALS)
            .applyTag(BlockTags.MINEABLE_WITH_PICKAXE)
            .initialProperties(() -> Blocks.COBBLESTONE)
            .variations(VariantTemplates.COBBLESTONE)
            .build();
    
    public static final Map<String, BlockEntry<BlockCarvable>> COBBLESTONE_MOSSY = _FACTORY.newType(Material.STONE, "mossy_cobblestone")
            .addBlock(Blocks.MOSSY_COBBLESTONE)
            .applyTag(BlockTags.MINEABLE_WITH_PICKAXE)
            .applyTag(Tags.Blocks.COBBLESTONE_MOSSY)
            .initialProperties(() -> Blocks.MOSSY_COBBLESTONE)
            .variations(VariantTemplates.COBBLESTONE_MOSSY)
            .build();

    public static final Map<DyeColor, Map<String, BlockEntry<BlockCarvable>>> CONCRETE = Arrays.stream(DyeColor.values())
            .collect(Collectors.toMap(Function.identity(), color -> _FACTORY.newType(Material.STONE, "concrete/" + color.getSerializedName())
                    .addBlock(new ResourceLocation(color.getSerializedName() + "_concrete")) // TODO improve this copied RL construction
                    .applyTag(BlockTags.MINEABLE_WITH_PICKAXE)
                    .setGroupName(RegistrateLangProvider.toEnglishName(color.getSerializedName()) + " Concrete")
                    .initialProperties(() -> ForgeRegistries.BLOCKS.getValue(new ResourceLocation(color.getSerializedName() + "_concrete")))
                    .variations(VariantTemplates.ROCK)
                    .build()));
//  BlockSpeedHandler.speedupBlocks.add(b);

//    public static final Map<String, BlockEntry<BlockCarvable>> COPPER = _FACTORY.newType(Material.METAL, "copper")
//            .addBlock(Blocks.COPPER_BLOCK)
//            .applyTag(BlockTags.BEACON_BASE_BLOCKS)
//            .applyTag(BlockTags.MINEABLE_WITH_PICKAXE)
//            .applyTag(BlockTags.NEEDS_IRON_TOOL)
//            .setGroupName("Copper Block")
//            .addTag(Tags.Blocks.STORAGE_BLOCKS_COPPER)
//            .initialProperties(() -> Blocks.COPPER_BLOCK)
//            .color(MaterialColor.COLOR_ORANGE)
//            .variations(VariantTemplates.METAL)
//            .build();

    public static final Map<String, BlockEntry<BlockCarvable>> DIABASE = _FACTORY.newType(Material.STONE, "diabase")
            .applyTag(BlockTags.MINEABLE_WITH_PICKAXE)
            .applyTag(BlockTags.NEEDS_STONE_TOOL)
            .initialProperties(() -> Blocks.BASALT)
            .variation(VariantTemplates.RAW)
            .variations(VariantTemplates.ROCK)
            .addTag(ChiselCompatTags.STONE_DIABASE)
            .build();

    public static final Map<String, BlockEntry<BlockCarvable>> DIAMOND = _FACTORY.newType(Material.METAL, "diamond")
            .addBlock(Blocks.DIAMOND_BLOCK)
            .equivalentTo(Tags.Blocks.STORAGE_BLOCKS_DIAMOND)
            .applyTag(BlockTags.MINEABLE_WITH_PICKAXE)
            .applyTag(BlockTags.NEEDS_IRON_TOOL)
            .applyTag(BlockTags.BEACON_BASE_BLOCKS)
            .initialProperties(() -> Blocks.DIAMOND_BLOCK)
            .variation("terrain_diamond_embossed")
                .model(ModelTemplates.cubeColumn())
            .next("terrain_diamond_gem")
                .model(ModelTemplates.cubeColumn())
            .next("terrain_diamond_cells")
            .next("terrain_diamond_space")
            .next("terrain_diamond_spaceblack")
            .next("terrain_diamond_simple")
                .model(ModelTemplates.cubeColumn())
            .next("terrain_diamond_bismuth")
            .next("terrain_diamond_crushed")
            .next("terrain_diamond_four")
            .next("terrain_diamond_fourornate")
            .next("terrain_diamond_zelda")
            .next("terrain_diamond_ornatelayer")
            .build();

    public static final Map<String, BlockEntry<BlockCarvable>> DIORITE = _FACTORY.newType(Material.STONE, "diorite")
            .addBlock(Blocks.DIORITE)
            .addBlock(Blocks.POLISHED_DIORITE)
            .applyTag(BlockTags.MINEABLE_WITH_PICKAXE)
            .initialProperties(() -> Blocks.DIORITE)
            .variations(VariantTemplates.ROCK)
            .build();

    public static final Map<String, BlockEntry<BlockCarvable>> EMERALD = _FACTORY.newType(Material.METAL, "emerald")
            .equivalentTo(Tags.Blocks.STORAGE_BLOCKS_EMERALD)
            .applyTag(BlockTags.MINEABLE_WITH_PICKAXE)
            .applyTag(BlockTags.NEEDS_IRON_TOOL)
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

    public static final Map<String, BlockEntry<BlockCarvable>> ENDSTONE = _FACTORY.newType(Material.STONE, "end_stone")
            .equivalentTo(Tags.Blocks.END_STONES)
            .addBlock(Blocks.END_STONE_BRICKS)
            .applyTag(BlockTags.MINEABLE_WITH_PICKAXE)
            .applyTag(BlockTags.NEEDS_STONE_TOOL)
            .initialProperties(() -> Blocks.END_STONE)
            .variations(VariantTemplates.ROCK)
            .build();
    
    public static final Map<String, BlockEntry<BlockCarvable>> FACTORY = _FACTORY.newType(Material.METAL, "factory")
            .applyTag(BlockTags.MINEABLE_WITH_PICKAXE)
            .applyTag(BlockTags.NEEDS_STONE_TOOL)
            .initialProperties(() -> Blocks.IRON_BLOCK)
            .color(MaterialColor.TERRACOTTA_BROWN)
            .variation("dots")
                .recipe((prov, block) -> new ShapedRecipeBuilder(block, 32)
                        .pattern("SXS").pattern("X X").pattern("SXS")
                        .define('S', Tags.Items.STONE)
                        .define('X', Tags.Items.INGOTS_IRON)
                        .unlockedBy("has_iron", prov.has(Tags.Items.INGOTS_IRON))
                        .save(prov))
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

    public static final Map<String, BlockEntry<BlockCarvable>> FUTURA = _FACTORY.newType(Material.STONE, "futura")
            .applyTag(BlockTags.MINEABLE_WITH_PICKAXE)
            .recipe((prov, block) -> new ShapedRecipeBuilder(block, 8)
                .pattern("SSS").pattern("SGS").pattern("SSS")
                .define('S', Tags.Items.STONE)
                .define('G', Tags.Items.DUSTS_REDSTONE)
                .unlockedBy("has_redstone", prov.has(Tags.Items.DUSTS_REDSTONE))
                .save(prov))
            .initialProperties(() -> Blocks.STONE)
            .color(MaterialColor.COLOR_YELLOW)
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

    public static final Map<String, BlockEntry<BlockCarvable>> GLASS = _FACTORY.newType(Material.GLASS, "glass")
            .addBlock(Blocks.GLASS)
            .addTag(Tags.Blocks.GLASS)
            .addTag(Tags.Blocks.GLASS_COLORLESS)
            .initialProperties(() -> Blocks.GLASS)
            .variation("terrain_glassbubble")
//            .next("chinese")
            .next("japanese")
            .next("terrain_glassdungeon")
            .next("terrain_glasslight")
            .next("terrain_glassnoborder")
            .next("terrain_glass_ornatesteel")
            .next("terrain_glass_screen")
            .next("terrain_glassshale")
            .next("terrain_glass_steelframe")
            .next("terrain_glassstone")
            .next("terrain_glassstreak")
            .next("terrain_glass_thickgrid")
            .next("terrain_glass_thingrid")
            .next("a1_glasswindow_ironfencemodern")
            .next("chrono")
            .next("chinese2")
            .next("japanese2")
            .build(b -> b.sound(SoundType.GLASS).explosionResistance(0.3F));

    public static final Map<String, BlockEntry<BlockCarvablePane>> GLASSPAnE = _FACTORY.newType(Material.GLASS, "glasspane", BlockCarvablePane::new)
            .addBlock(Blocks.GLASS_PANE)
            .addTag(Tags.Blocks.GLASS)
            .addTag(Tags.Blocks.GLASS_COLORLESS)
            .initialProperties(() -> Blocks.GLASS)
            .variation("terrain_glassbubble")
                .model(ModelTemplates.paneBlockCTM("block/glass/edge"))
                .itemModel((ctx, prov) -> prov.withExistingParent("item/" + prov.name(ctx::getEntry), new ResourceLocation("item/generated")).texture("layer0", "block/" + prov.name(ctx::getEntry).replaceFirst("pane", "")))
//            .next("chinese")
//                .model(ModelTemplates.paneblock("block/glass/chinese-top"))
//                .itemModel((ctx, prov) -> prov.withExistingParent("item/" + prov.name(ctx::getEntry), new ResourceLocation("item/generated")).texture("layer0", "block/" + prov.name(ctx::getEntry).replaceFirst("pane", "")))
            .next("japanese")
                .model(ModelTemplates.paneBlock("block/glass/japanese-top"))
                .itemModel((ctx, prov) -> prov.withExistingParent("item/" + prov.name(ctx::getEntry), new ResourceLocation("item/generated")).texture("layer0", "block/" + prov.name(ctx::getEntry).replaceFirst("pane", "")))
            .next("terrain_glassdungeon")
                .model(ModelTemplates.paneBlock("block/glass/edge_steel"))
                .itemModel((ctx, prov) -> prov.withExistingParent("item/" + prov.name(ctx::getEntry), new ResourceLocation("item/generated")).texture("layer0", "block/" + prov.name(ctx::getEntry).replaceFirst("pane", "")))
            .next("terrain_glasslight")
                .model(ModelTemplates.paneBlockCTM("block/glass/edge"))
                .itemModel((ctx, prov) -> prov.withExistingParent("item/" + prov.name(ctx::getEntry), new ResourceLocation("item/generated")).texture("layer0", "block/" + prov.name(ctx::getEntry).replaceFirst("pane", "")))
            .next("terrain_glassnoborder")
                .model(ModelTemplates.paneBlockCTM("block/glass/edge"))
                .itemModel((ctx, prov) -> prov.withExistingParent("item/" + prov.name(ctx::getEntry), new ResourceLocation("item/generated")).texture("layer0", "block/" + prov.name(ctx::getEntry).replaceFirst("pane", "")))
            .next("terrain_glass_ornatesteel")
                .model(ModelTemplates.paneBlockCTM("block/glass/edge_steel"))
                .itemModel((ctx, prov) -> prov.withExistingParent("item/" + prov.name(ctx::getEntry), new ResourceLocation("item/generated")).texture("layer0", "block/" + prov.name(ctx::getEntry).replaceFirst("pane", "")))
            .next("terrain_glass_screen")
                .model(ModelTemplates.paneBlock("block/glass/terrain_glass_screen"))
                .itemModel((ctx, prov) -> prov.withExistingParent("item/" + prov.name(ctx::getEntry), new ResourceLocation("item/generated")).texture("layer0", "block/" + prov.name(ctx::getEntry).replaceFirst("pane", "")))
            .next("terrain_glassshale")
                .model(ModelTemplates.paneBlockCTM("block/glass/edge_steel"))
                .itemModel((ctx, prov) -> prov.withExistingParent("item/" + prov.name(ctx::getEntry), new ResourceLocation("item/generated")).texture("layer0", "block/" + prov.name(ctx::getEntry).replaceFirst("pane", "")))
            .next("terrain_glass_steelframe")
                .model(ModelTemplates.paneBlockCTM("block/glass/edge_steel"))
                .itemModel((ctx, prov) -> prov.withExistingParent("item/" + prov.name(ctx::getEntry), new ResourceLocation("item/generated")).texture("layer0", "block/" + prov.name(ctx::getEntry).replaceFirst("pane", "")))
            .next("terrain_glassstone")
                .model(ModelTemplates.paneBlockCTM("block/glass/edge_steel"))
                .itemModel((ctx, prov) -> prov.withExistingParent("item/" + prov.name(ctx::getEntry), new ResourceLocation("item/generated")).texture("layer0", "block/" + prov.name(ctx::getEntry).replaceFirst("pane", "")))
            .next("terrain_glassstreak")
                .model(ModelTemplates.paneBlock("block/glass/terrain_glassstreak-top"))
                .itemModel((ctx, prov) -> prov.withExistingParent("item/" + prov.name(ctx::getEntry), new ResourceLocation("item/generated")).texture("layer0", "block/" + prov.name(ctx::getEntry).replaceFirst("pane", "")))
            .next("terrain_glass_thickgrid")
                .model(ModelTemplates.paneBlock("block/glass/terrain_glass_thickgrid"))
                .itemModel((ctx, prov) -> prov.withExistingParent("item/" + prov.name(ctx::getEntry), new ResourceLocation("item/generated")).texture("layer0", "block/" + prov.name(ctx::getEntry).replaceFirst("pane", "")))
            .next("terrain_glass_thingrid")
                .model(ModelTemplates.paneBlock("block/glass/terrain_glass_thingrid"))
                .itemModel((ctx, prov) -> prov.withExistingParent("item/" + prov.name(ctx::getEntry), new ResourceLocation("item/generated")).texture("layer0", "block/" + prov.name(ctx::getEntry).replaceFirst("pane", "")))
            .next("a1_glasswindow_ironfencemodern")
                .model(ModelTemplates.paneBlock("block/glass/edge_steel"))
                .itemModel((ctx, prov) -> prov.withExistingParent("item/" + prov.name(ctx::getEntry), new ResourceLocation("item/generated")).texture("layer0", "block/" + prov.name(ctx::getEntry).replaceFirst("pane", "")))
            .next("chrono")
                .model(ModelTemplates.paneBlockCTM("block/glass/edge_steel"))
                .itemModel((ctx, prov) -> prov.withExistingParent("item/" + prov.name(ctx::getEntry), new ResourceLocation("item/generated")).texture("layer0", "block/" + prov.name(ctx::getEntry).replaceFirst("pane", "")))
            .next("chinese2")
                .model(ModelTemplates.paneBlock("block/glass/chinese2-top"))
                .itemModel((ctx, prov) -> prov.withExistingParent("item/" + prov.name(ctx::getEntry), new ResourceLocation("item/generated")).texture("layer0", "block/" + prov.name(ctx::getEntry).replaceFirst("pane", "")))
            .next("japanese2")
                .model(ModelTemplates.paneBlock("block/glass/japanese2-top"))
                .itemModel((ctx, prov) -> prov.withExistingParent("item/" + prov.name(ctx::getEntry), new ResourceLocation("item/generated")).texture("layer0", "block/" + prov.name(ctx::getEntry).replaceFirst("pane", "")))
            .build(b -> b.sound(SoundType.GLASS).explosionResistance(0.3F));

    public static final Map<String, BlockEntry<BlockCarvable>> GLOWSTONE = _FACTORY.newType(Material.GLASS, "glowstone")
            .addBlock(Blocks.GLOWSTONE)
            .loot((prov, block) -> prov.add(block, RegistrateBlockLootTables.createSilkTouchDispatchTable(block,
                RegistrateBlockLootTables.applyExplosionDecay(block,
                    LootItem.lootTableItem(Items.GLOWSTONE_DUST)
                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0F, 4.0F)))
                        .apply(ApplyBonusCount.addUniformBonusCount(Enchantments.BLOCK_FORTUNE))
                        .apply(LimitCount.limitCount(IntRange.range(1, 4)))))))
            .initialProperties(() -> Blocks.GLOWSTONE)
            .variations(VariantTemplates.STONE)
            .variation("extra/bismuth").localizedName("Bismuth")
            .next("extra/tiles_large_bismuth").localizedName("Tiles Large Bismuth")
            .next("extra/tiles_medium_bismuth").localizedName("Tiles Medium Bismuth")
            .next("extra/neon").localizedName("Neon")
            .next("extra/neon_panel").localizedName("Neon Panel")
            .build();

    public static final Map<String, BlockEntry<BlockCarvable>> GRANITE = _FACTORY.newType(Material.STONE, "granite")
            .applyTag(BlockTags.MINEABLE_WITH_PICKAXE)
            .initialProperties(() -> Blocks.GRANITE)
            .variations(VariantTemplates.ROCK)
            .addBlock(Blocks.GRANITE)
            .addBlock(Blocks.POLISHED_GRANITE)
            .build();

    public static final Map<String, BlockEntry<BlockCarvable>> LABORATORY = _FACTORY.newType(Material.STONE, "laboratory")
            .applyTag(BlockTags.MINEABLE_WITH_PICKAXE)
            .recipe((prov, block) -> new ShapedRecipeBuilder(block, 8)
                    .pattern("***").pattern("*X*").pattern("***")
                    .define('*', Blocks.STONE)
                    .define('X', Items.QUARTZ)
                    .unlockedBy("has_quartz", prov.has(Items.QUARTZ))
                    .save(prov))
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

    public static final Map<String, BlockEntry<BlockCarvable>> LIMESTONE = _FACTORY.newType(Material.STONE, "limestone")
            .applyTag(BlockTags.MINEABLE_WITH_PICKAXE)
            .initialProperties(() -> Blocks.STONE)
            .color(MaterialColor.TERRACOTTA_GREEN)
            .variation(VariantTemplates.RAW)
            .variations(VariantTemplates.ROCK)
            .addTag(ChiselCompatTags.STONE_LIMESTONE)
            .build(b -> b.strength(1F, 5F));

    public static final Map<String, BlockEntry<BlockCarvable>> MARBLE = _FACTORY.newType(Material.STONE, "marble")
            .applyTag(BlockTags.MINEABLE_WITH_PICKAXE)
            .initialProperties(() -> Blocks.STONE)
            .color(MaterialColor.QUARTZ)
            .variation(VariantTemplates.RAW)
            .variations(VariantTemplates.ROCK)
            .addTag(ChiselCompatTags.STONE_MARBLE)
            .build(b -> b.strength(1.75F, 10.0F));

    public static final Map<WoodType, Map<String, BlockEntry<BlockCarvable>>> PLANKS = VANILLA_WOODS.stream()
            .map(t -> Pair.of(t, new ResourceLocation(t.name() + "_planks")))
            .collect(Collectors.toMap(Pair::getFirst, pair -> _FACTORY.newType(Material.WOOD, "planks/" + pair.getFirst().name())
                    .addBlock(pair.getSecond())
                    .applyTag(BlockTags.MINEABLE_WITH_AXE)
                    .applyTag(BlockTags.PLANKS)
                    .setGroupName(RegistrateLangProvider.toEnglishName(pair.getFirst().name()) + " Planks")
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

    public static final Map<String, BlockEntry<BlockCarvable>> PRISMARINE = _FACTORY.newType(Material.STONE, "prismarine")
            .addBlock(Blocks.PRISMARINE)
            .addBlock(Blocks.PRISMARINE_BRICKS)
            .addBlock(Blocks.DARK_PRISMARINE)
            .applyTag(BlockTags.MINEABLE_WITH_PICKAXE)
            .initialProperties(() -> Blocks.PRISMARINE)
            .variations(VariantTemplates.ROCK)
            .build();

    public static final Map<String, BlockEntry<BlockCarvable>> PURPUR = _FACTORY.newType(Material.STONE, "purpur")
            .addBlock(Blocks.PURPUR_BLOCK)
            .addBlock(Blocks.PURPUR_PILLAR)
            .applyTag(BlockTags.MINEABLE_WITH_PICKAXE)
            .initialProperties(() -> Blocks.PURPUR_BLOCK)
            .variations(VariantTemplates.ROCK)
            .build();

    public static final Map<String, BlockEntry<BlockCarvable>> QUARTZ = _FACTORY.newType(Material.STONE, "quartz")
            .equivalentTo(Tags.Blocks.STORAGE_BLOCKS_QUARTZ)
            .addBlock(Blocks.QUARTZ_PILLAR)
            .addBlock(Blocks.CHISELED_QUARTZ_BLOCK)
            .addBlock(Blocks.SMOOTH_QUARTZ)
            .applyTag(BlockTags.MINEABLE_WITH_PICKAXE)
            .initialProperties(() -> Blocks.QUARTZ_BLOCK)
            .variations(VariantTemplates.ROCK)
            .build();

    public static final Map<String, BlockEntry<BlockCarvable>> RED_SANDSTONE = _FACTORY.newType(Material.STONE, "red_sandstone")
            .addBlock(Blocks.RED_SANDSTONE)
            .addBlock(Blocks.CHISELED_RED_SANDSTONE)
            .addBlock(Blocks.CUT_RED_SANDSTONE)
            .addBlock(Blocks.SMOOTH_RED_SANDSTONE)
            .applyTag(BlockTags.MINEABLE_WITH_PICKAXE)
            .applyTag(Tags.Blocks.SANDSTONE)
            .initialProperties(() -> Blocks.SANDSTONE)
            .color(MaterialColor.COLOR_ORANGE)
            .variations(VariantTemplates.ROCK)
            .variation("extra/bevel_skeleton").localizedName("Bevel Skeleton")
            .next("extra/glyphs").localizedName("Glyphs")
            .next("extra/small").localizedName("Small")
            .build();

    public static final Map<String, BlockEntry<BlockCarvable>> REDSTONE = _FACTORY.newType(Material.METAL, "redstone", BlockCreators.redstoneCreator)
            .applyTag(BlockTags.MINEABLE_WITH_PICKAXE)
            .equivalentTo(Tags.Blocks.STORAGE_BLOCKS_REDSTONE)
            .initialProperties(() -> Blocks.REDSTONE_BLOCK)
            .variations(VariantTemplates.STONE)
            .build(); //TODO: Recipe from FeaturesOld

    public static final Map<String, BlockEntry<BlockCarvable>> SANDSTONE = _FACTORY.newType(Material.STONE, "sandstone")
            .addBlock(Blocks.SANDSTONE)
            .addBlock(Blocks.CHISELED_SANDSTONE)
            .addBlock(Blocks.CUT_SANDSTONE)
            .addBlock(Blocks.SMOOTH_SANDSTONE)
            .applyTag(BlockTags.MINEABLE_WITH_PICKAXE)
            .applyTag(Tags.Blocks.SANDSTONE)
            .initialProperties(() -> Blocks.SANDSTONE)
            .variations(VariantTemplates.ROCK)
            .variation("extra/bevel_creeper").localizedName("Bevel Creeper")
            .next("extra/glyphs").localizedName("Glyphs")
            .next("extra/small").localizedName("Small")
            //TODO: column-ctm
            .build();

    public static final Map<String, BlockEntry<BlockCarvable>> STONE_BRICKS = _FACTORY.newType(Material.STONE, "stone_bricks")
            .addBlock(Blocks.STONE)
            .addBlock(Blocks.STONE_BRICKS)
            .addBlock(Blocks.CHISELED_STONE_BRICKS)
            .addBlock(Blocks.CRACKED_STONE_BRICKS)
            .applyTag(BlockTags.MINEABLE_WITH_PICKAXE)
            .initialProperties(() -> Blocks.STONE_BRICKS)
            .variations(VariantTemplates.ROCK)
            .variation("extra/largeornate").localizedName("Large Ornate")
            .next("extra/poison").localizedName("Poison")
            .next("extra/sunken").localizedName("Sunken")
            .build();

    public static final Map<String, BlockEntry<BlockCarvable>> TERRACOTTA = _FACTORY.newType(Material.STONE, "terracotta")
            .addBlock(Blocks.TERRACOTTA)
            .applyTag(BlockTags.MINEABLE_WITH_PICKAXE)
            .initialProperties(() -> Blocks.TERRACOTTA)
            .variations(VariantTemplates.ROCK)
            .build();

    public static final Map<String, BlockEntry<BlockCarvable>> TYRIAN = _FACTORY.newType(Material.METAL, "tyrian")
            .applyTag(BlockTags.MINEABLE_WITH_PICKAXE)
            .applyTag(BlockTags.NEEDS_STONE_TOOL)
            .initialProperties(() -> Blocks.IRON_BLOCK)
            .color(MaterialColor.TERRACOTTA_CYAN)
            .variation("shining")
                .recipe((prov, block) -> new ShapedRecipeBuilder(block, 32)
                        .pattern("SSS").pattern("SXS").pattern("SSS")
                        .define('S', Tags.Items.STONE)
                        .define('X', Tags.Items.INGOTS_IRON)
                        .unlockedBy("has_iron", prov.has(Tags.Items.INGOTS_IRON))
                        .save(prov))
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
            .collect(Collectors.toMap(Function.identity(), color -> _FACTORY.newType(Material.WOOL, "wool/" + (color.getSerializedName()))
                    .addBlock(new ResourceLocation(color.getSerializedName() + "_wool")) // TODO improve this copied RL construction
                    .applyTag(BlockTags.WOOL)
                    .setGroupName(RegistrateLangProvider.toEnglishName(color.getSerializedName()) + " Wool")
                    .initialProperties(() -> ForgeRegistries.BLOCKS.getValue(new ResourceLocation(color.getSerializedName() + "_wool")))
                    .variation("legacy")
                    .next("llama")
                    .build()));
    
    public static void init() {}

    private static class BlockCreators {

        private static final BlockCreator<BlockCarvable> redstoneCreator = (props, data) -> new BlockCarvable(props, data) {
            @Override
            @Deprecated
            public boolean isSignalSource(BlockState state) {
                return true;
            }

            @Override
            @Deprecated
            public int getSignal(BlockState state, BlockGetter reader, BlockPos pos, Direction direction) {
                return 15;
            }
        };
    }
}
