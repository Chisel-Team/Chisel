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
import net.minecraft.data.loot.BlockLoot;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
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
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.minecraftforge.common.Tags;
import net.minecraftforge.registries.ForgeRegistries;
import team.chisel.api.block.BlockCreator;
import team.chisel.api.block.ChiselBlockFactory;
import team.chisel.client.data.ModelTemplates;
import team.chisel.client.data.VariantTemplates;
import team.chisel.client.sound.ChiselSoundTypes;
import team.chisel.common.Reference;
import team.chisel.common.block.BlockCarvable;
import team.chisel.common.block.BlockCarvableBookshelf;
import team.chisel.common.block.BlockCarvableCarpet;
import team.chisel.common.block.BlockCarvableIce;
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
            .addBlock(Blocks.POLISHED_BASALT)
            .applyTag(BlockTags.MINEABLE_WITH_PICKAXE)
            .initialProperties(() -> Blocks.BASALT)
            .variations(VariantTemplates.ROCK)
            .build();

    //TODO new wood textures
    public static final ImmutableList<WoodType> VANILLA_WOODS = ImmutableList.of(WoodType.OAK, WoodType.SPRUCE, WoodType.BIRCH, WoodType.ACACIA, WoodType.JUNGLE, WoodType.DARK_OAK);

    public static final Map<WoodType, Map<String, BlockEntry<BlockCarvableBookshelf>>> BOOKSHELVES = Features.VANILLA_WOODS.stream()
            .collect(Collectors.toMap(Function.identity(), wood -> _FACTORY.newType(Material.WOOD, "bookshelf/" + wood.name(), BlockCarvableBookshelf::new)
                    .loot((prov, block) -> prov.add(block, RegistrateBlockLootTables.createSingleItemTableWithSilkTouch(block, Items.BOOK, ConstantValue.exactly(3))))
                    .applyIf(() -> wood == WoodType.OAK, f -> f.addBlock(Blocks.BOOKSHELF))
                    .model((prov, block) -> {
                        prov.simpleBlock(block, prov.models().withExistingParent("block/" + ModelTemplates.name(block), prov.modLoc("cube_2_layer_sides"))
                                .texture("all", "minecraft:block/" + wood.name() + "_planks")
                                .texture("side", "block/" + ModelTemplates.name(block).replace(wood.name() + "/", "")));
                    })
                    .layer(() -> RenderType::cutout)
                    .setGroupName(RegistrateLangProvider.toEnglishName(wood.name()) + " Bookshelf")
                    .applyTag(BlockTags.MINEABLE_WITH_AXE)
                    .variation("rainbow")
                    .next("novice_necromancer")
                    .next("necromancer")
                    .next("redtomes").localizedName("Red Tomes")
                    .next("abandoned")
                    .next("hoarder")
                    .next("brim")
                    .next("historian")
                    .next("cans")
                    .next("papers").localizedName("Stacks Of Papers")
                    .build(b -> b.sound(SoundType.WOOD).strength(1.5f))));

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
            .equivalentTo(ChiselCompatTags.STORAGE_BLOCKS_BRONZE)
            .initialProperties(() -> Blocks.IRON_BLOCK)
            .color(MaterialColor.TERRACOTTA_ORANGE)
            .variations(VariantTemplates.METAL)
            .build();

    public static final Map<String, BlockEntry<BlockCarvable>> BROWNSTONE = _FACTORY.newType(Material.STONE, "brownstone")
            .applyTag(BlockTags.MINEABLE_WITH_PICKAXE)
            .recipe((prov, block) -> new ShapedRecipeBuilder(block, 4)
                    .pattern(" S ").pattern("SCS").pattern(" S ")
                    .define('S', Tags.Items.SANDSTONE)
                    .define('C', Items.CLAY_BALL)
                    .unlockedBy("has_clay", prov.has(Items.CLAY_BALL))
                    .save(prov))
            .variation("default").localizedName("Raw")
            .next("block").localizedName("Big Tiled")
            .next("blocks").localizedName("Tiled")
            .next("doubleslab").localizedName("Bisected")
                .model(ModelTemplates.cubeColumn("doubleslab-side", "block"))
            .next("block").localizedName("Big Tiled")
            .next("weathered")
            .next("weathered_block").localizedName("Weathered Big Tile")
            .next("weathered_doubleslab").localizedName("Weathered Bisected")
                .model(ModelTemplates.cubeColumn("weathered_doubleslab-side", "weathered_block"))
            .next("weathered_blocks").localizedName("Weathered Tiles")
            .next("weathered_half").localizedName("Half-Weathered")
                .model(ModelTemplates.cubeBottomTop("weathered_half-side", "default", "weathered"))
            .next("weathered_block_half").localizedName("Half-Weathered Big Tile")
                .model(ModelTemplates.cubeBottomTop("weathered_block_half-side", "block", "weathered_block"))
            .build(b -> b.sound(SoundType.STONE).strength(1.0f));
    //BlockSpeedHandler.speedupBlocks.add(b);

    public static final Map<DyeColor, Map<String, BlockEntry<BlockCarvableCarpet>>> CARPET = Arrays.stream(DyeColor.values())
            .collect(Collectors.toMap(Function.identity(), color -> _FACTORY.newType(Material.CLOTH_DECORATION, "carpet/" + (color.getSerializedName()), BlockCarvableCarpet::new)
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
            .recipe((prov, block) -> new ShapelessRecipeBuilder(Items.CHARCOAL, 9)
                    .requires(block)
                    .unlockedBy("has_charcoal_block", prov.has(block))
                    .save(prov, block.getRegistryName() + "_uncraft"))
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
            .recipe((prov, block) -> new ShapelessRecipeBuilder(Items.COAL, 9)
                    .requires(block)
                    .unlockedBy("has_coal_block", prov.has(block))
                    .save(prov,block.getRegistryName() + "_uncraft"))
            .variation(VariantTemplates.withRecipe(VariantTemplates.RAW, (prov, block) -> new ShapelessRecipeBuilder(block, 1)
                    .requires(Items.COAL, 9)
                    .unlockedBy("has_charcoal", prov.has(Items.COAL))
                    .save(prov)))
            .variations(VariantTemplates.ROCK)
            .build();

    public static final Map<String, BlockEntry<BlockCarvable>> CLOUD = _FACTORY.newType(Material.CLOTH_DECORATION, "cloud")
            .variation("cloud")
            .next("large").localizedName("Bricks")
            .next("small").localizedName("Small Bricks")
            .next("vertical").localizedName("Vertical Bricks")
            .next("grid").localizedName("Small Tiles")
            .build(b -> b.sound(SoundType.WOOL).explosionResistance(0.3F));

    public static final Map<String, BlockEntry<BlockCarvable>> COBALT = _FACTORY.newType(Material.METAL, "metals/cobalt")
            .applyTag(BlockTags.BEACON_BASE_BLOCKS)
            .applyTag(BlockTags.MINEABLE_WITH_PICKAXE)
            .applyTag(BlockTags.NEEDS_IRON_TOOL)
            .setGroupName("Cobalt Block")
            .equivalentTo(ChiselCompatTags.STORAGE_BLOCKS_COBALT)
            .initialProperties(() -> Blocks.IRON_BLOCK)
            .color(MaterialColor.TERRACOTTA_CYAN)
            .variations(VariantTemplates.METAL)
            .build();

    public static final Map<String, BlockEntry<BlockCarvable>> COBBLESTONE = _FACTORY.newType(Material.STONE, "cobblestone")
            .addBlock(Blocks.COBBLESTONE)
            .applyTag(Tags.Blocks.COBBLESTONE)
            .applyTag(ItemTags.STONE_TOOL_MATERIALS)
            .applyTag(ItemTags.STONE_CRAFTING_MATERIALS)
            .applyTag(BlockTags.MINEABLE_WITH_PICKAXE)
            .applyTag(BlockTags.NEEDS_STONE_TOOL)
            .initialProperties(() -> Blocks.COBBLESTONE)
            .variations(VariantTemplates.COBBLESTONE)
            .build();
    
    public static final Map<String, BlockEntry<BlockCarvable>> COBBLESTONE_MOSSY = _FACTORY.newType(Material.STONE, "mossy_cobblestone")
            .addBlock(Blocks.MOSSY_COBBLESTONE)
            .applyTag(BlockTags.MINEABLE_WITH_PICKAXE)
            .applyTag(BlockTags.NEEDS_STONE_TOOL)
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

    public static final Map<String, BlockEntry<BlockCarvable>> COPPER = _FACTORY.newType(Material.METAL, "metals/copper")
            .addBlock(Blocks.COPPER_BLOCK)
            .applyTag(BlockTags.BEACON_BASE_BLOCKS)
            .applyTag(BlockTags.MINEABLE_WITH_PICKAXE)
            .applyTag(BlockTags.NEEDS_STONE_TOOL)
            .setGroupName("Copper Block")
            .addTag(Tags.Blocks.STORAGE_BLOCKS_COPPER)
            .initialProperties(() -> Blocks.COPPER_BLOCK)
            .variations(VariantTemplates.METAL)
            .recipe((prov, block) -> new ShapelessRecipeBuilder(Items.COPPER_INGOT, 9)
                    .requires(block)
                    .unlockedBy("has_copper_block", prov.has(block))
                    .save(prov, block.getRegistryName() + "_uncraft"))
            .build();

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
            .recipe((prov, block) -> new ShapelessRecipeBuilder(Items.DIAMOND, 9)
                    .requires(block)
                    .unlockedBy("has_diamond_block", prov.has(block))
                    .save(prov, block.getRegistryName() + "_uncraft"))
            .variation("terrain_diamond_embossed").localizedName("Embossed")
                .model(ModelTemplates.cubeColumn())
            .next("terrain_diamond_gem").localizedName("Gem")
                .model(ModelTemplates.cubeColumn())
            .next("terrain_diamond_cells").localizedName("Cells")
            .next("terrain_diamond_space").localizedName("Purple Space")
            .next("terrain_diamond_spaceblack").localizedName("Black Space")
            .next("terrain_diamond_simple").localizedName("Simple")
                .model(ModelTemplates.cubeColumn())
            .next("terrain_diamond_bismuth").localizedName("Bismuth")
            .next("terrain_diamond_crushed").localizedName("Crushed")
            .next("terrain_diamond_four").localizedName("Tiles")
            .next("terrain_diamond_fourornate").localizedName("Ornate Tiles")
            .next("terrain_diamond_zelda").localizedName("Zelde")
            .next("terrain_diamond_ornatelayer").localizedName("Gold-Encrusted")
            .build();

    public static final Map<String, BlockEntry<BlockCarvable>> DIORITE = _FACTORY.newType(Material.STONE, "diorite")
            .addBlock(Blocks.DIORITE)
            .addBlock(Blocks.POLISHED_DIORITE)
            .applyTag(BlockTags.MINEABLE_WITH_PICKAXE)
            .initialProperties(() -> Blocks.DIORITE)
            .variations(VariantTemplates.ROCK)
            .build();

    public static final Map<String, BlockEntry<BlockCarvable>> ELECTRUM = _FACTORY.newType(Material.METAL, "metals/electrum")
            .equivalentTo(ChiselCompatTags.STORAGE_BLOCKS_ELECTRUM)
            .applyTag(BlockTags.BEACON_BASE_BLOCKS)
            .applyTag(BlockTags.MINEABLE_WITH_PICKAXE)
            .applyTag(BlockTags.NEEDS_STONE_TOOL)
            .setGroupName("Electrum Block")
            .initialProperties(() -> Blocks.IRON_BLOCK)
            .variations(VariantTemplates.METAL)
            .color(MaterialColor.TERRACOTTA_YELLOW)
            .build();

    public static final Map<String, BlockEntry<BlockCarvable>> EMERALD = _FACTORY.newType(Material.METAL, "emerald")
            .equivalentTo(Tags.Blocks.STORAGE_BLOCKS_EMERALD)
            .applyTag(BlockTags.MINEABLE_WITH_PICKAXE)
            .applyTag(BlockTags.NEEDS_IRON_TOOL)
            .applyTag(BlockTags.BEACON_BASE_BLOCKS)
            .initialProperties(() -> Blocks.EMERALD_BLOCK)
            .recipe((prov, block) -> new ShapelessRecipeBuilder(Items.EMERALD, 9)
                    .requires(block)
                    .unlockedBy("has_emerald_block", prov.has(block))
                    .save(prov, block.getRegistryName() + "_uncraft"))
            .variation("panel") //.localizedName("Tile") TODO name conflict
            .next("panelclassic").localizedName("Panel")
            .next("smooth") //.localizedName("Raw") TODO name conflict
            .next("chunk").localizedName("Cobble")
            .next("goldborder").localizedName("Gold-Encrusted")
            .next("zelda")
            .next("cell")
            .next("cellbismuth")
            .next("four").localizedName("Tiles")
            .next("fourornate").localizedName("Ornate Tiles")
            .next("ornate")
            .next("masonryemerald").localizedName("Masonry")
            .next("emeraldcircle").localizedName("Circular")
            .next("emeraldprismatic").localizedName("Prism")
            .build();

    public static final Map<String, BlockEntry<BlockCarvable>> ENDSTONE = _FACTORY.newType(Material.STONE, "end_stone")
            .equivalentTo(Tags.Blocks.END_STONES)
            .addBlock(Blocks.END_STONE_BRICKS)
            .applyTag(BlockTags.MINEABLE_WITH_PICKAXE)
            .applyTag(BlockTags.NEEDS_STONE_TOOL)
            .initialProperties(() -> Blocks.END_STONE)
            .variations(VariantTemplates.ROCK)
            .build();

    public static final TagKey<Block> FACTORY1 = BlockTags.create(new ResourceLocation(Reference.MOD_ID, "factory"));

    public static final Map<String, BlockEntry<BlockCarvable>> FACTORY = _FACTORY.newType(Material.METAL, "factory")
            .setGroup(FACTORY1)
            .applyTag(BlockTags.MINEABLE_WITH_PICKAXE)
            .applyTag(BlockTags.NEEDS_STONE_TOOL)
            .initialProperties(() -> Blocks.IRON_BLOCK)
            .color(MaterialColor.TERRACOTTA_BROWN)
            .recipe((prov, block) -> new ShapedRecipeBuilder(block, 32)
                    .pattern("SXS").pattern("X X").pattern("SXS")
                    .define('S', Tags.Items.STONE)
                    .define('X', Tags.Items.INGOTS_IRON)
                    .unlockedBy("has_iron", prov.has(Tags.Items.INGOTS_IRON))
                    .save(prov))
            .variation("dots").localizedName("Dotted Rusty Plate")
            .next("rust2").localizedName("Rusty Plate")
            .next("rust").localizedName("Rusty Plate")
            .next("platex").localizedName("Slightly Rusty Plate")
            .next("wireframewhite").localizedName("Wireframe")
            .next("wireframe").localizedName("Purple Wireframe")
            .next("hazard").localizedName("Yellow-Black Caution Stripes")
            .next("hazardorange").localizedName("Orange-White Caution Stripes")
            .next("circuit")
            .next("metalbox").localizedName("Metal Box")
                .model(ModelTemplates.cubeColumn())
            .next("goldplate").localizedName("Gold-Plated Circuit")
            .next("goldplating").localizedName("Gold-Framed Purple Plates")
            .next("grinder")
            .next("plating").localizedName("Old Vents")
            .next("rustplates").localizedName("Segmented Rusty Plates")
            .next("column").localizedName("Metal Column")
                .model(ModelTemplates.cubeColumn())
            .next("frameblue").localizedName("Blue-Framed Circuit")
            .next("iceiceice").localizedName("Ice Ice Ice")
            .next("tilemosaic").localizedName("Blue Circuits")
            .next("vent") //.localizedName("Vents") TODO name conflict
                .model(ModelTemplates.cubeColumn())
            .next("wireframeblue").localizedName("Blue Wireframe")
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
            .variation("screen_metallic").localizedName("Gray Screen")
                .model(ModelTemplates.twoLayerTopShaded("screen_metallic", "screen_discoherent"))
            .next("screen_cyan").localizedName("Cyan Screen")
                .model(ModelTemplates.twoLayerTopShaded("screen_cyan", "screen_discoherent"))
            .next("controller").localizedName("ME Controller")
                .model(ModelTemplates.threeLayerTopShaded("controller_particle", "lines_plating", "rainbow_lines", "lines_invalid"))
            .next("wavy").localizedName("Rainbowliciously Wavy")
                .model(ModelTemplates.twoLayerTopShaded("rainbow_wave_particle", "rainbow_wave_base", "rainbow_wave"))
            .next("controller_purple").localizedName("Purple ME Controller")
                .model(ModelTemplates.threeLayerTopShaded("controller_unity_particle", "unity_lines_plating", "unity_lines", "lines_invalid"))
            .next("uber_wavy").localizedName("Fabulously Wavy")
                .model(ModelTemplates.threeLayerTopShaded("orange_frame_particle", "orange_frame", "uber_rainbow", "screen_discoherent"))
            .build();

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

    public static final Map<String, BlockEntry<BlockCarvable>> GOLD = _FACTORY.newType(Material.METAL, "metals/gold")
            .addBlock(Blocks.GOLD_BLOCK)
            .applyTag(BlockTags.BEACON_BASE_BLOCKS)
            .applyTag(BlockTags.MINEABLE_WITH_PICKAXE)
            .applyTag(BlockTags.NEEDS_IRON_TOOL)
            .setGroupName("Gold Block")
            .addTag(Tags.Blocks.STORAGE_BLOCKS_GOLD)
            .initialProperties(() -> Blocks.GOLD_BLOCK)
            .color(MaterialColor.GOLD)
            .variations(VariantTemplates.METAL)
            .recipe((prov, block) -> new ShapelessRecipeBuilder(Items.GOLD_INGOT, 9)
                    .requires(block)
                    .unlockedBy("has_gold_block", prov.has(block))
                    .save(prov, block.getRegistryName() + "_uncraft"))
            .build();

    public static final Map<String, BlockEntry<BlockCarvable>> GOLD_TERRAIN = _FACTORY.newType(Material.METAL, "gold")
            .addBlock(Blocks.GOLD_BLOCK)
            .addTag(Tags.Blocks.STORAGE_BLOCKS_GOLD)
            .initialProperties(() -> Blocks.GOLD_BLOCK)
            .applyTag(BlockTags.NEEDS_IRON_TOOL)
            .applyTag(BlockTags.MINEABLE_WITH_PICKAXE)
            .recipe((prov, block) -> new ShapelessRecipeBuilder(Items.GOLD_INGOT, 9)
                    .requires(block)
                    .unlockedBy("has_gold_block", prov.has(block))
                    .save(prov, block.getRegistryName() + "_uncraft"))
            .variations(VariantTemplates.METAL_TERRAIN)
            .variation("cart")
                .model(ModelTemplates.cubeBottomTop())
            .next("star").localizedName("Stars")
            .model(ModelTemplates.cubeBottomTop())
            .next("goldeye")
            .build();

    public static final Map<String, BlockEntry<BlockCarvable>> GRANITE = _FACTORY.newType(Material.STONE, "granite")
            .applyTag(BlockTags.MINEABLE_WITH_PICKAXE)
            .initialProperties(() -> Blocks.GRANITE)
            .variations(VariantTemplates.ROCK)
            .addBlock(Blocks.GRANITE)
            .addBlock(Blocks.POLISHED_GRANITE)
            .build();

    public static final Map<DyeColor, Map<String, BlockEntry<BlockCarvable>>> HEX_PLATING = Arrays.stream(DyeColor.values())
            .collect(Collectors.toMap(Function.identity(), color ->  _FACTORY.newType(Material.STONE, "hexplating/" + color.getSerializedName())
                    .setGroupName(RegistrateLangProvider.toEnglishName(color.getSerializedName()) + " Hex Plating")
                    .applyTag(BlockTags.MINEABLE_WITH_PICKAXE)
                    .applyTag(BlockTags.NEEDS_STONE_TOOL)
                    .initialProperties(() -> Blocks.STONE)
                    .recipe((prov, block) -> new ShapedRecipeBuilder(block, 8)
                            .pattern("SSS").pattern("SCS").pattern("SSS")
                            .define('S', Tags.Items.STONE)
                            .define('C', ItemTags.COALS)
                            .unlockedBy("has_coal", prov.has(ItemTags.COALS))
                            .save(prov))
                    .variation("hexbase")
                        .model(ModelTemplates.hexPlate("hexbase"))
                    .next("hexnew")
                        .model(ModelTemplates.hexPlate("hexnew"))
                    .build()
            ));


    public static final TagKey<Block> ICE1 = BlockTags.create(new ResourceLocation(Reference.MOD_ID, "ice"));

    public static final Map<String, BlockEntry<BlockCarvableIce>> ICE = _FACTORY.newType(Material.ICE, "ice", BlockCarvableIce::new)
            .addBlock(Blocks.ICE)
            .setGroup(ICE1)
            .applyTag(BlockTags.MINEABLE_WITH_PICKAXE)
            .addTag(BlockTags.ICE)
            .variations(VariantTemplates.ROCK)
            .initialProperties(() -> Blocks.ICE)
            .loot(BlockLoot::dropWhenSilkTouch)
            .build();

    public static final Map<String, BlockEntry<BlockCarvableIce>> ICE_PILLAR = _FACTORY.newType(Material.ICE, "icepillar", BlockCarvableIce::new)
            .addBlock(Blocks.ICE)
            .setGroup(ICE1)
            .setGroupName("")
            .applyTag(BlockTags.MINEABLE_WITH_PICKAXE)
            .addTag(BlockTags.ICE)
            .variations(VariantTemplates.PILLAR)
            .initialProperties(() -> Blocks.ICE)
            .loot(BlockLoot::dropWhenSilkTouch)
            .build();

    public static final Map<String, BlockEntry<BlockCarvable>> INVAR = _FACTORY.newType(Material.METAL, "metals/invar")
            .equivalentTo(ChiselCompatTags.STORAGE_BLOCKS_INVAR)
            .applyTag(BlockTags.BEACON_BASE_BLOCKS)
            .applyTag(BlockTags.MINEABLE_WITH_PICKAXE)
            .applyTag(BlockTags.NEEDS_STONE_TOOL)
            .setGroupName("Invar Block")
            .initialProperties(() -> Blocks.IRON_BLOCK)
            .variations(VariantTemplates.METAL)
            .build();

    public static final TagKey<Block> IRON1 = BlockTags.create(new ResourceLocation(Reference.MOD_ID, "metals/iron"));

    public static final Map<String, BlockEntry<BlockCarvable>> IRON = _FACTORY.newType(Material.METAL, "metals/iron")
            .setGroup(IRON1)
            .setGroupName("Iron Block")
            .addBlock(Blocks.IRON_BLOCK)
            .initialProperties(() -> Blocks.IRON_BLOCK)
            .applyTag(BlockTags.MINEABLE_WITH_PICKAXE)
            .applyTag(BlockTags.NEEDS_IRON_TOOL)
            .applyTag(BlockTags.BEACON_BASE_BLOCKS)
            .addTag(Tags.Blocks.STORAGE_BLOCKS_IRON)
            .recipe((prov, block) -> new ShapelessRecipeBuilder(Items.IRON_INGOT, 9)
                    .requires(block)
                    .unlockedBy("has_iron_block", prov.has(block))
                    .save(prov, block.getRegistryName() + "_uncraft"))
            .variations(VariantTemplates.METAL)
            .build();

    public static final Map<String, BlockEntry<BlockCarvable>> IRON_TERRAIN = _FACTORY.newType(Material.METAL, "iron")
            .setGroup(IRON1)
            .setGroupName("")
            .addBlock(Blocks.IRON_BLOCK)
            .initialProperties(() -> Blocks.IRON_BLOCK)
            .applyTag(BlockTags.MINEABLE_WITH_PICKAXE)
            .applyTag(BlockTags.NEEDS_IRON_TOOL)
            .applyTag(BlockTags.BEACON_BASE_BLOCKS)
            .addTag(Tags.Blocks.STORAGE_BLOCKS_IRON)
            .recipe((prov, block) -> new ShapelessRecipeBuilder(Items.IRON_INGOT, 9)
                    .requires(block)
                    .unlockedBy("has_iron_block", prov.has(block))
                    .save(prov, block.getRegistryName() + "_uncraft"))
            .variations(VariantTemplates.METAL_TERRAIN)
            .variation("moon")
                .model(ModelTemplates.cubeBottomTop())
            .next("vents")
                .model(ModelTemplates.cubeColumn())
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

    public static final Map<String, BlockEntry<BlockCarvable>> LAPIS = _FACTORY.newType(Material.STONE, "lapis")
            .applyTag(BlockTags.MINEABLE_WITH_PICKAXE)
            .applyTag(BlockTags.NEEDS_STONE_TOOL)
            .addTag(Tags.Blocks.STORAGE_BLOCKS_LAPIS)
            .addBlock(Blocks.LAPIS_BLOCK)
            .initialProperties(() -> Blocks.LAPIS_BLOCK)
            .recipe((prov, block) -> new ShapelessRecipeBuilder(Items.LAPIS_LAZULI, 9)
                    .requires(block)
                    .unlockedBy("has_lapis_block", prov.has(block))
                    .save(prov, block.getRegistryName() + "_uncraft"))
            .variation("terrain_lapisblock_chunky").localizedName("Cobble")
            .next("terrain_lapisblock_panel").localizedName("Tile")
            .next("terrain_lapisblock_zelda").localizedName("Zelda")
            .next("terrain_lapisornate").localizedName("Wood-Framed")
            .next("terrain_lapistile").localizedName("Panel")
            .next("a1_blocklapis_panel").localizedName("Shiny Panel")
            .next("a1_blocklapis_smooth").localizedName("Raw")
            .next("a1_blocklapis_ornatelayer").localizedName("Gold-Encrusted")
            .next("masonrylapis").localizedName("Masonry")
            .build();

    public static final Map<String, BlockEntry<BlockCarvable>> LAVASTONE = _FACTORY.newType(Material.STONE, "lavastone")
            .applyTag(BlockTags.MINEABLE_WITH_PICKAXE)
            .addTag(ChiselCompatTags.LAVASTONE)
            .initialProperties(() -> Blocks.STONE)
            .recipe((prov, block) -> new ShapedRecipeBuilder(block, 8)
                    .pattern("***").pattern("*X*").pattern("***")
                    .define('*', Blocks.STONE)
                    .define('X', Items.LAVA_BUCKET)
                    .unlockedBy("has_stone", prov.has(Items.STONE))
                    .save(prov))
            .variation("cracked")
                .model(ModelTemplates.fluidCube("lava"))
            .next("soft_bricks").localizedName("Weathered Bricks")
                .model(ModelTemplates.fluidCube("lava"))
            .next("cracked")
                .model(ModelTemplates.fluidCube("lava"))
            .next("triple_bricks").localizedName("Wide Bricks")
                .model(ModelTemplates.fluidCube("lava"))
            .next("encased_bricks")
                .model(ModelTemplates.fluidPassCube("lava"))
            .next("braid")
                .model(ModelTemplates.fluidCube("lava"))
            .next("array").localizedName("Arrayed Bricks")
                .model(ModelTemplates.fluidPassCube("lava"))
            .next("tiles_large").localizedName("Big Tile")
                .model(ModelTemplates.fluidPassCube("lava"))
            .next("tiles_small").localizedName("Small Tiles")
                .model(ModelTemplates.fluidCube("lava"))
            .next("chaotic_medium").localizedName("Disordered Tiles")
                .model(ModelTemplates.fluidCube("lava"))
            .next("chaotic_small").localizedName("Small Disordered Tiles")
                .model(ModelTemplates.fluidCube("lava"))
            .next("dent")
                .model(ModelTemplates.fluidPassCube("lava"))
            .next("french_1")
                .model(ModelTemplates.fluidCube("lava"))
            .next("french_2")
                .model(ModelTemplates.fluidCube("lava"))
            .next("jellybean")
                .model(ModelTemplates.fluidPassCube("lava"))
            .next("layers")
                .model(ModelTemplates.fluidCube("lava"))
            .next("mosaic")
                .model(ModelTemplates.fluidPassCube("lava"))
            .next("ornate")
                .model(ModelTemplates.fluidCube("lava"))
            .next("panel")
                .model(ModelTemplates.fluidCube("lava"))
            .next("road")
                .model(ModelTemplates.fluidCube("lava"))
            .next("slanted")
                .model(ModelTemplates.fluidPassCube("lava"))
            .next("zag")
                .model(ModelTemplates.fluidPassCube("lava"))
            .next("circularct").localizedName("Circular")
                .model(ModelTemplates.fluidCubeCTM("lava", "circular"))
            .next("weaver").localizedName("Celtic")
                .model(ModelTemplates.fluidPassCube("lava"))
            .next("solid_bricks").localizedName("Bricks")
                .model(ModelTemplates.fluidCube("lava"))
            .next("small_bricks")
                .model(ModelTemplates.fluidCube("lava"))
            .next("circular")
                .model(ModelTemplates.fluidCube("lava"))
            .next("tiles_medium").localizedName("Tiles")
                .model(ModelTemplates.fluidPassCube("lava"))
            .next("pillar")
                .model(ModelTemplates.fluidPassColume("lava"))
            .next("twisted")
                .model(ModelTemplates.fluidPassColume("lava"))
            .next("prism")
                .model(ModelTemplates.fluidCube("lava"))
            .next("chaotic_bricks").localizedName("Trodden Bricks")
                .model(ModelTemplates.fluidPassCube("lava"))
            .next("cuts")
                .model(ModelTemplates.fluidPassCube("lava"))
            .build(b -> b.strength(4.0F).explosionResistance(50.0F).sound(SoundType.STONE));

    public static final Map<String, BlockEntry<BlockCarvable>> LEAD = _FACTORY.newType(Material.METAL, "metals/lead")
            .equivalentTo(ChiselCompatTags.STORAGE_BLOCKS_LEAD)
            .applyTag(BlockTags.BEACON_BASE_BLOCKS)
            .applyTag(BlockTags.MINEABLE_WITH_PICKAXE)
            .applyTag(BlockTags.NEEDS_STONE_TOOL)
            .setGroupName("Lead Block")
            .initialProperties(() -> Blocks.IRON_BLOCK)
            .variations(VariantTemplates.METAL)
            .color(MaterialColor.TERRACOTTA_LIGHT_BLUE)
            .build();

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

    public static final Map<String, BlockEntry<BlockCarvable>> NICKEL = _FACTORY.newType(Material.METAL, "metals/nickel")
            .equivalentTo(ChiselCompatTags.STORAGE_BLOCKS_NICKEL)
            .applyTag(BlockTags.BEACON_BASE_BLOCKS)
            .applyTag(BlockTags.MINEABLE_WITH_PICKAXE)
            .applyTag(BlockTags.NEEDS_STONE_TOOL)
            .setGroupName("Nickel Block")
            .initialProperties(() -> Blocks.IRON_BLOCK)
            .variations(VariantTemplates.METAL)
            .color(MaterialColor.TERRACOTTA_LIGHT_BLUE)
            .build();

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

    public static final Map<String, BlockEntry<BlockCarvable>> PLATINUM = _FACTORY.newType(Material.METAL, "metals/platinum")
            .equivalentTo(ChiselCompatTags.STORAGE_BLOCKS_PLATINUM)
            .applyTag(BlockTags.BEACON_BASE_BLOCKS)
            .applyTag(BlockTags.MINEABLE_WITH_PICKAXE)
            .applyTag(BlockTags.NEEDS_STONE_TOOL)
            .setGroupName("Platinum Block")
            .initialProperties(() -> Blocks.IRON_BLOCK)
            .variations(VariantTemplates.METAL)
            .color(MaterialColor.TERRACOTTA_LIGHT_BLUE)
            .build();

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
            .recipe((prov, block) -> new ShapelessRecipeBuilder(Items.REDSTONE, 9)
                    .requires(block)
                    .unlockedBy("has_redstone_block", prov.has(block))
                    .save(prov, block.getRegistryName() + "_uncraft"))
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

    public static final Map<String, BlockEntry<BlockCarvable>> SILVER = _FACTORY.newType(Material.METAL, "metals/silver")
            .equivalentTo(ChiselCompatTags.STORAGE_BLOCKS_SILVER)
            .applyTag(BlockTags.BEACON_BASE_BLOCKS)
            .applyTag(BlockTags.MINEABLE_WITH_PICKAXE)
            .applyTag(BlockTags.NEEDS_STONE_TOOL)
            .setGroupName("Silver Block")
            .initialProperties(() -> Blocks.IRON_BLOCK)
            .variations(VariantTemplates.METAL)
            .color(MaterialColor.COLOR_LIGHT_GRAY)
            .build();

    public static final Map<String, BlockEntry<BlockCarvable>> STEEL = _FACTORY.newType(Material.METAL, "metals/steel")
            .equivalentTo(ChiselCompatTags.STORAGE_BLOCKS_STEEL)
            .applyTag(BlockTags.BEACON_BASE_BLOCKS)
            .applyTag(BlockTags.MINEABLE_WITH_PICKAXE)
            .applyTag(BlockTags.NEEDS_STONE_TOOL)
            .setGroupName("Steel Block")
            .initialProperties(() -> Blocks.IRON_BLOCK)
            .variations(VariantTemplates.METAL)
            .color(MaterialColor.COLOR_GRAY)
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

    public static final Map<String, BlockEntry<BlockCarvable>> TECHNICAL = _FACTORY.newType(Material.METAL, "technical")
            .applyTag(BlockTags.MINEABLE_WITH_PICKAXE)
            .applyTag(BlockTags.NEEDS_STONE_TOOL)
            .setGroup(FACTORY1)
            .setGroupName("")
            .initialProperties(() -> Blocks.IRON_BLOCK)
            .color(MaterialColor.TERRACOTTA_LIGHT_GRAY)
            .variation("scaffold") //.localizedName("Rusty Scaffold") TODO name conflict
            .next("cautiontape").localizedName("Caution-Framed Plates")
            .next("industrialrelic").localizedName("Industrial Relic").tooltip("An Old Relic From The", "Land Of OneTeuFyv")
            .next("pipeslarge").localizedName("Large Pipes")
            .next("fanfast").localizedName("Fan (Fast)")
                .model(ModelTemplates.cubeColumn())
            .next("pipessmall").localizedName("Small Pipes")
            .next("fanstill").localizedName("Fan (Off)")
                .model(ModelTemplates.cubeColumn())
            .next("vent")
            .next("ventglowing").localizedName("Glowing Vent")
            .next("insulationv2").localizedName("Insulation")
            .next("spinningstuffanim").localizedName("Gears & Flywheels")
            .next("cables")
            .next("rustyboltedplates").localizedName("Rusty Bolted Plates")
            .next("grate")
            .next("malfunctionfan").localizedName("Fan (Malfunctioning)")
                .model(ModelTemplates.cubeColumn())
            .next("graterusty").localizedName("Rusty Grate")
            .next("scaffoldtransparent").localizedName("Rusty Scaffold - Transparent")
                .opaque(true)
            .next("fanfasttransparent").localizedName("Fan (Fast) - Transparent")
                .opaque(true)
                .model(ModelTemplates.cubeColumn())
            .next("fanstilltransparent").localizedName("Fan (Still) - Transparent")
                .model(ModelTemplates.cubeColumn())
                .opaque(true)
            .next("massivefan").localizedName("Massive Fan").tooltip("I'm a big fan of this!")
            .next("massivehexplating").localizedName("Massive Hexagonal Plating")
            .build(b -> b.sound(SoundType.METAL));

    public static final Map<String, BlockEntry<BlockCarvable>> TECHNICAL_NEW = _FACTORY.newType(Material.METAL, "technical/new")
            .setGroup(FACTORY1)
            .setGroupName("")
            .applyTag(BlockTags.MINEABLE_WITH_PICKAXE)
            .applyTag(BlockTags.NEEDS_STONE_TOOL)
            .initialProperties(() -> Blocks.IRON_BLOCK)
            .color(MaterialColor.TERRACOTTA_LIGHT_GRAY)
            .variation("weatheredgreenpanels").localizedName("Extremely Corroded Panels")
            .next("weatheredorangepanels").localizedName("Extremely Rusted Panels")
            .next("sturdy")
            .next("megacell").localizedName("Megacell™ Battery")
                .model(ModelTemplates.cubeBottomTop())
            .next("exhaustplating").localizedName("Exhaust Plating")
            .next("makeshiftpanels").localizedName("Sloppy Plating")
            .next("engineering").localizedName("Engineer's Pipes")
            .next("scaffoldlarge").localizedName("Large Rusty Scaffold")
            .next("piping").localizedName("Pipes").tooltip("It's a pipe dream.")
            .next("oldetimeyserveranim").localizedName("Oldetimey Server")
            .next("tapedrive")
            .build(b -> b.sound(SoundType.METAL));

    public static final Map<String, BlockEntry<BlockCarvable>> TIN = _FACTORY.newType(Material.METAL, "metals/tin")
            .equivalentTo(ChiselCompatTags.STORAGE_BLOCKS_TIN)
            .applyTag(BlockTags.BEACON_BASE_BLOCKS)
            .applyTag(BlockTags.MINEABLE_WITH_PICKAXE)
            .applyTag(BlockTags.NEEDS_STONE_TOOL)
            .setGroupName("Tin Block")
            .initialProperties(() -> Blocks.IRON_BLOCK)
            .variations(VariantTemplates.METAL)
            .color(MaterialColor.COLOR_LIGHT_BLUE)
            .build();

    public static final Map<String, BlockEntry<BlockCarvable>> URANIUM = _FACTORY.newType(Material.METAL, "metals/uranium")
            .equivalentTo(ChiselCompatTags.STORAGE_BLOCKS_URANIUM)
            .applyTag(BlockTags.BEACON_BASE_BLOCKS)
            .applyTag(BlockTags.MINEABLE_WITH_PICKAXE)
            .applyTag(BlockTags.NEEDS_STONE_TOOL)
            .setGroupName("Uranium Block")
            .initialProperties(() -> Blocks.IRON_BLOCK)
            .color(MaterialColor.TERRACOTTA_GREEN)
            .variations(VariantTemplates.METAL)
            .build();

    public static final Map<String, BlockEntry<BlockCarvable>> VALENTINES = _FACTORY.newType(Material.STONE, "valentines")
            .recipe((prov, block) -> new ShapedRecipeBuilder(block, 32)
                    .pattern("SSS").pattern("SXS").pattern("SSS")
                    .define('S', Tags.Items.STONE)
                    .define('X', Tags.Items.DYES_PINK)
                    .unlockedBy("has_dye", prov.has(Tags.Items.DYES_PINK))
                    .save(prov)) //TODO selective recipe
            .variation("1").localizedName("Pink Marker")
            .next("2").localizedName("Gray Rocky")
            .next("3").localizedName("Pink Heart")
            .next("4").localizedName("Pink Rocky")
            .next("5").localizedName("Pink Tile")
            .next("6").localizedName("Pink Cracks")
            .next("7").localizedName("Pink Studded")
            .next("8").localizedName("Flame")
            .next("9").localizedName("Pink Steel")
            .next("companion").tooltip("It will never threaten to stab you and,", "in fact, cannot speak.")
            .build(b -> b.strength(1.5F).explosionResistance(20.0F).sound(SoundType.STONE));

    public static final TagKey<Block> VOIDSTONE1 = BlockTags.create(new ResourceLocation(Reference.MOD_ID, "voidstone"));

    public static final Map<String, BlockEntry<BlockCarvable>> VOIDSTONE = _FACTORY.newType(Material.STONE, "voidstone")
            .setGroup(VOIDSTONE1)
            .applyTag(BlockTags.MINEABLE_WITH_PICKAXE)
            .applyTag(BlockTags.NEEDS_STONE_TOOL)
            .recipe((prov, block) ->  {
                new ShapedRecipeBuilder(block, 16)
                        .pattern(" E ").pattern("OOO").pattern(" E ")
                        .define('E', Items.ENDER_EYE)
                        .define('O', Tags.Items.OBSIDIAN)
                        .unlockedBy("has_eye", prov.has(Items.ENDER_EYE))
                        .save(prov);
                new ShapedRecipeBuilder(block, 48)
                        .pattern(" P ").pattern("PEP").pattern(" P ")
                        .define('E', Items.ENDER_EYE)
                        .define('P', Blocks.PURPUR_BLOCK)
                        .unlockedBy("has_eye", prov.has(Items.ENDER_EYE))
                        .save(prov, new ResourceLocation(Chisel.MOD_ID, block.getRegistryName().getPath() + "_voidstone2"));
            })
            .variation(VariantTemplates.RAW)
            .variation("quarters").localizedName("Medium Tiles")
            .next("smooth")
            .next("skulls")
            .next("rune")
            .next("metalborder").localizedName("Metallic")
            .next("eye")
            .next("bevel")
            .build();

    public static final Map<String, BlockEntry<BlockCarvable>> VOIDSTONE_ENERGISED = _FACTORY.newType(Material.STONE, "voidstone/animated")
            .setGroup(VOIDSTONE1)
            .setGroupName("")
            .applyTag(BlockTags.MINEABLE_WITH_PICKAXE)
            .applyTag(BlockTags.NEEDS_STONE_TOOL)
            .variation(VariantTemplates.RAW)
                .model(ModelTemplates.twoLayerTopShaded("raw", "background"))
            .variation("quarters").localizedName("Medium Tiles")
                .model(ModelTemplates.twoLayerTopShaded("quarters", "background"))
            .next("smooth")
                .model(ModelTemplates.twoLayerTopShaded("smooth", "background"))
            .next("skulls")
                .model(ModelTemplates.twoLayerTopShaded("skulls", "background"))
            .next("rune")
                .model(ModelTemplates.twoLayerTopShaded("rune", "background"))
            .next("metalborder").localizedName("Metallic")
                .model(ModelTemplates.twoLayerTopShaded("metalborder", "background"))
            .next("eye")
                .model(ModelTemplates.twoLayerTopShaded("eye", "background"))
            .next("bevel")
                .model(ModelTemplates.twoLayerTopShaded("bevel", "background"))
            .build();

    public static final Map<String, BlockEntry<BlockCarvable>> VOIDSTONE_RUNIC = _FACTORY.newType(Material.STONE, "voidstone/runes")
            .setGroup(VOIDSTONE1)
            .setGroupName("")
            .applyTag(BlockTags.MINEABLE_WITH_PICKAXE)
            .applyTag(BlockTags.NEEDS_STONE_TOOL)
            .variation("black")
            .next("red")
            .next("green")
            .next("brown")
            .next("blue")
            .next("purple")
            .next("cyan")
            .next("lightgray").localizedName("Light Gray")
            .next("pink")
            .next("lime")
            .next("yellow")
            .next("lightblue").localizedName("Light Blue")
            .next("magenta")
            .next("orange")
            //.next("white")
            .build();

    public static final Map<String, BlockEntry<BlockCarvable>> WATERSTONE = _FACTORY.newType(Material.STONE, "waterstone")
            .applyTag(BlockTags.MINEABLE_WITH_PICKAXE)
            .applyTag(BlockTags.NEEDS_STONE_TOOL)
            .addTag(ChiselCompatTags.WATERSTONE)
            .initialProperties(() -> Blocks.STONE)
            .recipe((prov, block) -> new ShapedRecipeBuilder(block, 8)
                    .pattern("***").pattern("*X*").pattern("***")
                    .define('*', Blocks.STONE)
                    .define('X', Items.WATER_BUCKET)
                    .unlockedBy("has_stone", prov.has(Items.STONE))
                    .save(prov))
            .variation("cracked")
                .model(ModelTemplates.fluidCube("water"))
            .next("soft_bricks").localizedName("Weathered Bricks")
                .model(ModelTemplates.fluidCube("water"))
            .next("cracked")
                .model(ModelTemplates.fluidCube("water"))
            .next("triple_bricks").localizedName("Wide Bricks")
                .model(ModelTemplates.fluidCube("water"))
            .next("encased_bricks")
                .model(ModelTemplates.fluidPassCube("water"))
            .next("braid")
                .model(ModelTemplates.fluidCube("water"))
            .next("array").localizedName("Arrayed Bricks")
                .model(ModelTemplates.fluidPassCube("water"))
            .next("tiles_large").localizedName("Big Tile")
                .model(ModelTemplates.fluidPassCube("water"))
            .next("tiles_small").localizedName("Small Tiles")
                .model(ModelTemplates.fluidCube("water"))
            .next("chaotic_medium").localizedName("Disordered Tiles")
                .model(ModelTemplates.fluidCube("water"))
            .next("chaotic_small").localizedName("Small Disordered Tiles")
                .model(ModelTemplates.fluidCube("water"))
            .next("dent")
                .model(ModelTemplates.fluidPassCube("water"))
            .next("french_1")
                .model(ModelTemplates.fluidCube("water"))
            .next("french_2")
                .model(ModelTemplates.fluidCube("water"))
            .next("jellybean")
                .model(ModelTemplates.fluidPassCube("water"))
            .next("layers")
                .model(ModelTemplates.fluidCube("water"))
            .next("mosaic")
                .model(ModelTemplates.fluidPassCube("water"))
            .next("ornate")
                .model(ModelTemplates.fluidCube("water"))
            .next("panel")
                .model(ModelTemplates.fluidCube("water"))
            .next("road")
                .model(ModelTemplates.fluidCube("water"))
            .next("slanted")
                .model(ModelTemplates.fluidPassCube("water"))
            .next("zag")
                .model(ModelTemplates.fluidPassCube("water"))
            .next("circularct").localizedName("Circular")
                .model(ModelTemplates.fluidCubeCTM("water", "circular"))
            .next("weaver").localizedName("Celtic")
                .model(ModelTemplates.fluidPassCube("water"))
            .next("solid_bricks").localizedName("Bricks")
                .model(ModelTemplates.fluidCube("water"))
            .next("small_bricks")
                .model(ModelTemplates.fluidCube("water"))
            .next("circular")
                .model(ModelTemplates.fluidCube("water"))
            .next("tiles_medium").localizedName("Tiles")
                .model(ModelTemplates.fluidPassCube("water"))
            .next("pillar")
                .model(ModelTemplates.fluidPassColume("water"))
            .next("twisted")
                .model(ModelTemplates.fluidPassColume("water"))
            .next("prism")
                .model(ModelTemplates.fluidCube("water"))
            .next("chaotic_bricks").localizedName("Trodden Bricks")
                .model(ModelTemplates.fluidPassCube("water"))
            .next("cuts")
                .model(ModelTemplates.fluidPassCube("water"))
            .build(b -> b.strength(4.0F).explosionResistance(50.0F).sound(SoundType.STONE));

    public static final Map<DyeColor, Map<String, BlockEntry<BlockCarvable>>> WOOL = Arrays.stream(DyeColor.values())
            .collect(Collectors.toMap(Function.identity(), color -> _FACTORY.newType(Material.WOOL, "wool/" + (color.getSerializedName()))
                    .addBlock(new ResourceLocation(color.getSerializedName() + "_wool")) // TODO improve this copied RL construction
                    .applyTag(BlockTags.WOOL)
                    .applyTag(BlockTags.OCCLUDES_VIBRATION_SIGNALS)
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
