package team.chisel.legacy;

import com.tterrag.registrate.util.entry.BlockEntry;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.common.Tags;
import team.chisel.Chisel;
import team.chisel.api.block.ChiselBlockFactory;
import team.chisel.client.data.ModelTemplates;
import team.chisel.common.block.BlockCarvable;

import java.util.Map;

@SuppressWarnings({"CommentedOutCode", "unused", "deprecation"})
public class LegacyFeatures {

    private static final ChiselBlockFactory _FACTORY = ChiselBlockFactory.newFactory(Chisel.registrateLegacy());

    // Hardcode to vanilla wood types

    //public static final Map<WoodType, Map<String, BlockEntry<BlockCarvableBookshelf>>> BOOKSHELVES = Features.VANILLA_WOODS.stream()
    //        .collect(Collectors.toMap(Function.identity(), wood -> _FACTORY.newType(Material.WOOD, "bookshelf/" + wood.name(), BlockCarvableBookshelf::new)
    //                .loot((prov, block) -> prov.add(block, RegistrateBlockLootTables.createSingleItemTableWithSilkTouch(block, Items.BOOK, ConstantValue.exactly(3))))
    //                .applyIf(() -> wood == WoodType.OAK, f -> f.addBlock(Blocks.BOOKSHELF))
    //                .model((prov, block) -> {
    //                    prov.simpleBlock(block, prov.models().withExistingParent("block/" + ModelTemplates.name(block), prov.modLoc("cube_2_layer_sides"))
    //                            .texture("all", "minecraft:block/" + wood.name() + "_planks")
    //                            .texture("side", "block/" + ModelTemplates.name(block).replace(wood.name() + "/", "")));
    //                })
    //                .layer(() -> RenderType::cutout)
    //                .setGroupName(RegistrateLangProvider.toEnglishName(wood.name()) + " Bookshelf")
    //                .applyTag(BlockTags.MINEABLE_WITH_AXE)
    //                .variation("rainbow")
    //                .next("novice_necromancer")
    //                .next("necromancer")
    //                .next("redtomes")
    //                .next("abandoned")
    //                .next("hoarder")
    //                .next("brim")
    //                .next("historian")
    //                .next("cans")
    //                .next("papers")
    //                .build(b -> b.sound(SoundType.WOOD).strength(1.5f))));

    public static final Map<String, BlockEntry<BlockCarvable>> DIRT = _FACTORY.newType(Material.DIRT, "dirt")
            .addBlock(Blocks.DIRT)
            .applyTag(BlockTags.MINEABLE_WITH_SHOVEL)
            .variation("bricks")
            .next("netherbricks")
            .next("bricks3")
            .next("cobble")
            .next("reinforcedcobbledirt")
            .next("reinforceddirt")
            .next("happy")
            .next("bricks2")
            .next("bricks_dirt2")
            .next("hor")
            .model(ModelTemplates.cubeColumn("hor-ctmh", "hor-top"))
            .next("vert")
            .next("layers")
            .next("vertical")
            .next("chunky")
            .next("horizontal")
            .next("plate")
            .build(b -> b.strength(0.5F, 0.0F).sound(SoundType.GRAVEL));

    public static final Map<String, BlockEntry<BlockCarvable>> OBSIDIAN = _FACTORY.newType(Material.STONE, "obsidian")
            .addTag(Tags.Blocks.OBSIDIAN)
            .applyTag(BlockTags.DRAGON_IMMUNE)
            .applyTag(BlockTags.MINEABLE_WITH_PICKAXE)
            .applyTag(BlockTags.NEEDS_DIAMOND_TOOL)
            .variation("pillar")
            .model(ModelTemplates.cubeColumn())
            .next("pillar-quartz")
            .model(ModelTemplates.cubeColumn())
            .next("chiseled")
            .model(ModelTemplates.cubeColumn())
            .next("panel_shiny")
            .next("panel")
            .next("chunks")
            .next("growth")
            .next("crystal")
            .next("map-a")
            .next("map-b")
            .next("panel_light")
            .next("blocks")
            .next("tiles")
            .next("greek")
            .model(ModelTemplates.cubeColumn())
            .next("crate")
            .model(ModelTemplates.cubeBottomTop())
            .build(b -> b.strength(50.0F, 2000.0F).sound(SoundType.STONE));

    //public static final Map<String, BlockEntry<BlockCarvable>> PAPER = _FACTORY.newType(Material.PLANT, "paper")
    //        .recipe((prov, block) -> new ShapedRecipeBuilder(block, 32)
    //                .pattern("ppp").pattern("psp").pattern("ppp")
    //                .define('p', Items.PAPER)
    //                .define('s', Tags.Items.RODS_WOODEN)
    //                .unlockedBy("has_stick", prov.has(Tags.Items.RODS_WOODEN))
    //                .save(prov))
    //        .variation("box")
    //        .next("throughmiddle")
    //        .next("cross")
    //        .next("sixsections")
    //        .next("vertical")
    //        .next("horizontal")
    //        .next("floral")
    //        .next("plain")
    //        .next("door")
    //        .build(b -> b.strength(1.5F, 0.0F).sound(SoundType.GRASS));

    public static void init() {
    }
}
