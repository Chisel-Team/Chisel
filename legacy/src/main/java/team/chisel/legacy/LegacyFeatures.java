package team.chisel.legacy;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.tterrag.registrate.providers.RegistrateLangProvider;
import com.tterrag.registrate.providers.loot.RegistrateBlockLootTables;
import com.tterrag.registrate.util.RegistryEntry;

import net.minecraft.block.Blocks;
import net.minecraft.block.SoundType;
import net.minecraft.block.WoodType;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.data.ShapedRecipeBuilder;
import net.minecraft.item.Items;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.storage.loot.ConstantRange;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.ToolType;
import team.chisel.Features;
import team.chisel.api.block.ChiselBlockFactory;
import team.chisel.client.data.ModelTemplates;
import team.chisel.common.block.BlockCarvable;
import team.chisel.common.block.BlockCarvableBookshelf;

public class LegacyFeatures {

	private static final ChiselBlockFactory _FACTORY = ChiselBlockFactory.newFactory(ChiselLegacy.registrate());

    // Hardcode to vanilla wood types

    public static final Map<WoodType, Map<String, RegistryEntry<BlockCarvableBookshelf>>> BOOKSHELVES = Features.VANILLA_WOODS.stream()
            .collect(Collectors.toMap(Function.identity(), wood -> _FACTORY.newType(Material.WOOD, "bookshelf/" + wood.getName(), BlockCarvableBookshelf::new)
                    .loot((prov, block) -> prov.registerLootTable(block, RegistrateBlockLootTables.droppingWithSilkTouchOrRandomly(block, Items.BOOK, ConstantRange.of(3))))
                    .applyIf(() -> wood == WoodType.OAK, f -> f.addBlock(Blocks.BOOKSHELF))
                    .model((prov, block) -> {
                        prov.simpleBlock(block, prov.models().withExistingParent("block/" + ModelTemplates.name(block), prov.modLoc("cube_2_layer_sides"))
                                .texture("all", "minecraft:block/" + wood.getName() + "_planks")
                                .texture("side", "block/" + ModelTemplates.name(block).replace(wood.getName() + "/", "")));
                    })
                    .layer(() -> RenderType::getCutout)
                    .setGroupName(RegistrateLangProvider.toEnglishName(wood.getName()) + " Bookshelf")
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

    public static final Map<String, RegistryEntry<BlockCarvable>> DIRT = _FACTORY.newType(Material.EARTH, "dirt")
            .addBlock(Blocks.DIRT)
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
            .build(b -> b.hardnessAndResistance(0.5F, 0.0F).sound(SoundType.GROUND).harvestTool(ToolType.SHOVEL).harvestLevel(0));

    public static final Map<String, RegistryEntry<BlockCarvable>> OBSIDIAN = _FACTORY.newType(Material.ROCK, "obsidian")
            .addTag(Tags.Blocks.OBSIDIAN)
            .addTag(BlockTags.DRAGON_IMMUNE) //TODO: Is this right?
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
            .build(b -> b.hardnessAndResistance(50.0F, 2000.0F).sound(SoundType.STONE).harvestTool(ToolType.PICKAXE).harvestLevel(3));

    public static final Map<String, RegistryEntry<BlockCarvable>> PAPER = _FACTORY.newType(Material.PLANTS, "paper")
            .recipe((prov, block) -> new ShapedRecipeBuilder(block, 32)
                    .patternLine("ppp").patternLine("psp").patternLine("ppp")
                    .key('p', Items.PAPER)
                    .key('s', Tags.Items.RODS_WOODEN)
                    .addCriterion("has_stick", prov.hasItem(Tags.Items.RODS_WOODEN))
                    .build(prov))
            .variation("box")
            .next("throughmiddle")
            .next("cross")
            .next("sixsections")
            .next("vertical")
            .next("horizontal")
            .next("floral")
            .next("plain")
            .next("door")
            .build(b -> b.hardnessAndResistance(1.5F, 0.0F).sound(SoundType.PLANT));

	public static void init() {}
}
