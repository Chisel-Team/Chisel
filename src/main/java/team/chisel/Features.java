package team.chisel;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;

import com.tterrag.registrate.providers.RegistrateLangProvider;
import com.tterrag.registrate.providers.loot.RegistrateBlockLootTables;
import com.tterrag.registrate.util.RegistryEntry;

import net.minecraft.block.Blocks;
import net.minecraft.block.SoundType;
import net.minecraft.block.WoodType;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.data.ShapedRecipeBuilder;
import net.minecraft.item.DyeColor;
import net.minecraft.item.Items;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.ConstantRange;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.ToolType;
import team.chisel.api.block.ChiselBlockFactory;
import team.chisel.client.data.ModelTemplates;
import team.chisel.client.data.VariantTemplates;
import team.chisel.client.sound.ChiselSoundTypes;
import team.chisel.common.block.BlockCarvable;
import team.chisel.common.block.BlockCarvableBookshelf;

public class Features {

    private static final ChiselBlockFactory _FACTORY = ChiselBlockFactory.newFactory(Chisel.registrate());
    
    public static final Map<String, RegistryEntry<BlockCarvable>> ALUMINUM = _FACTORY.newType(Material.IRON, "metals/aluminum")
            .setGroupName("Aluminum Block")
            .variations(VariantTemplates.METAL)
            .build(b -> b.sound(SoundType.METAL).harvestTool(ToolType.PICKAXE).harvestLevel(1));
    
    public static final Map<String, RegistryEntry<BlockCarvable>> ANDESITE = _FACTORY.newType(Material.ROCK, "andesite")
            .addBlock(Blocks.ANDESITE)
            .addBlock(Blocks.POLISHED_ANDESITE)
            .variations(VariantTemplates.ROCK)
            .build(b -> b.hardnessAndResistance(1.5F, 30.0F).sound(SoundType.STONE));
    
    public static final Map<String, RegistryEntry<BlockCarvable>> ANTIBLOCK = _FACTORY.newType(Material.ROCK, "antiblock", (p, v) -> new BlockCarvable(p, v))
            .layer(() -> RenderType::getCutout)
            .variations(VariantTemplates.colors(ModelTemplates.twoLayerWithTop("antiblock", false), (prov, block) -> 
                    new ShapedRecipeBuilder(block, 8)
                        .patternLine("SSS").patternLine("SGS").patternLine("SSS")
                        .key('S', Tags.Items.STONE)
                        .key('G', Tags.Items.DUSTS_GLOWSTONE)
                        .addCriterion("has_glowstone", prov.hasItem(Tags.Items.DUSTS_GLOWSTONE))
                        .build(prov)))
            .build();
    
    public static final Map<String, RegistryEntry<BlockCarvable>> BASALT = _FACTORY.newType(Material.ROCK, "basalt")
            .variations(VariantTemplates.ROCK)
            .build(b -> b.hardnessAndResistance(1.5F, 10.0F).sound(SoundType.STONE));
    
    // Hardcode to vanilla wood types
    public static final Map<WoodType, Map<String, RegistryEntry<BlockCarvableBookshelf>>> BOOKSHELVES = Stream.of(WoodType.OAK, WoodType.SPRUCE, WoodType.BIRCH, WoodType.ACACIA, WoodType.JUNGLE, WoodType.DARK_OAK)
            .collect(Collectors.toMap(Function.identity(), wood -> _FACTORY.newType(Material.WOOD, "bookshelf/" + wood.getName(), BlockCarvableBookshelf::new)
                    .loot((prov, block) -> prov.registerLootTable(block, RegistrateBlockLootTables.droppingWithSilkTouchOrRandomly(block, Items.BOOK, ConstantRange.of(3))))
                    .applyIf(() -> wood == WoodType.OAK, f -> f.addBlock(Blocks.BOOKSHELF))
                    .model((prov, block) -> {
                        prov.simpleBlock(block, prov.models().withExistingParent("block/" + ModelTemplates.name(block), prov.modLoc("cube_2_layer_sides"))
                                .texture("all", "minecraft:block/" + wood.getName() + "_planks")
                                .texture("side", "block/" + ModelTemplates.name(block).replace(wood.getName() + "/", "")));
                    })
                    .layer(() -> RenderType::getCutout)
                    .setGroupName(StringUtils.capitalize(wood.getName()) + " Bookshelf")
                    .variation("rainbow")
                    .next("novice_necromancer")
                    .next("necromancer")
                    .next("redtomes")
                    .next("abandoned")
                    .next("hoarder")
                    .next("brim")
                    .next("historian")
                    .next("cans")
                    .next("papers")
                    .build(b -> b.sound(SoundType.WOOD).hardnessAndResistance(1.5f))));
            
    
//  String[] woodTypes = new String[]{"Oak", "Spruce", "Birch", "Jungle", "Acacia", "DarkOak"};
//
//  Carving.chisel.addVariation("bookshelf_oak", CarvingUtils.variationFor(Blocks.BOOKSHELF.getDefaultState(), -1));
//
//  BlockCreator<BlockCarvable> bookshelfCreator = 
//
//  for (String woodType : woodTypes) {
//      factory.newType(Material.WOOD, "bookshelf_" + woodType.toLowerCase(), new ChiselBlockProvider<>(bookshelfCreator, BlockCarvable.class))
//              .newVariation("rainbow")
//              .next("necromancer-novice")
//              .next("necromancer")
//              .next("redtomes")
//              .next("abandoned")
//              .next("hoarder")
//              .next("brim")
//              .next("historician")
//              .next("cans")
//              .next("papers")
//              .addOreDict("bookshelf")
//              .addOreDict("bookshelf" + woodType)
//              .build(b -> b.setSoundType(SoundType.WOOD).setHardness(1.5f));
//
//  }
    
    public static final Map<String, RegistryEntry<BlockCarvable>> BROWNSTONE = _FACTORY.newType(Material.ROCK, "brownstone")
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
    
    public static final Map<String, RegistryEntry<BlockCarvable>> COAL = _FACTORY.newType(Material.ROCK, "coal")
            .addBlock(Blocks.COAL_BLOCK)
            .variations(/*VariantTemplates.withUncraft(*/VariantTemplates.ROCK/*, Items.COAL)*/) // TODO
            .build(b -> b.hardnessAndResistance(5.0F, 10.0F).sound(SoundType.STONE));
    
    public static final Map<String, RegistryEntry<BlockCarvable>> COBBLESTONE = _FACTORY.newType(Material.ROCK, "cobblestone")
            .variations(VariantTemplates.ROCK)
            .variation("extra/emboss")
            .next("extra/indent")
            .next("extra/marker")
            .build(b -> b.hardnessAndResistance(2.0F, 10.0F).sound(SoundType.STONE));
    
    public static final Map<DyeColor, Map<String, RegistryEntry<BlockCarvable>>> CONCRETE = Arrays.stream(DyeColor.values())
            .collect(Collectors.toMap(Function.identity(), color -> _FACTORY.newType(Material.ROCK, "concrete/" + color.getName())
                    .addBlock(new ResourceLocation(color.getName() + "_concrete"))
                    .setGroupName(RegistrateLangProvider.toEnglishName(color.getName()) + " Concrete")
                    .variations(VariantTemplates.ROCK)
                    .build(p -> p.sound(SoundType.STONE).hardnessAndResistance(1.8F))));
//  BlockSpeedHandler.speedupBlocks.add(b);
    
    public static final Map<String, RegistryEntry<BlockCarvable>> FACTORY = _FACTORY.newType(Material.IRON, "factory")
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

    public static final Map<String, RegistryEntry<BlockCarvable>> LIMESTONE = _FACTORY.newType(Material.ROCK, "limestone")
            .variations(VariantTemplates.ROCK)
            .build(b -> b.hardnessAndResistance(1.5F, 10.0F).sound(SoundType.STONE));

    public static final Map<String, RegistryEntry<BlockCarvable>> MARBLE = _FACTORY.newType(Material.ROCK, "marble")
            .variations(VariantTemplates.ROCK)
            .build(b -> b.hardnessAndResistance(1.5F, 10.0F).sound(SoundType.STONE));
    
    public static final Map<String, RegistryEntry<BlockCarvable>> TYRIAN = _FACTORY.newType(Material.IRON, "tyrian")
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
    
    public static final Map<DyeColor, Map<String, RegistryEntry<BlockCarvable>>> WOOL = Arrays.stream(DyeColor.values())
            .collect(Collectors.toMap(Function.identity(), color -> _FACTORY.newType(Material.WOOL, "wool/" + (color.getName()))
                    .addBlock(new ResourceLocation(color.getName() + "_wool"))
                    .setGroupName(RegistrateLangProvider.toEnglishName(color.getName()) + " Wool")
                    .variation("legacy")
                    .next("llama")
                    .build(b -> b.sound(SoundType.CLOTH).hardnessAndResistance(0.8F))));
    
    public static void init() {}
}
