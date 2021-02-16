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
import net.minecraft.item.Items;
import net.minecraft.world.storage.loot.ConstantRange;
import team.chisel.Features;
import team.chisel.api.block.ChiselBlockFactory;
import team.chisel.client.data.ModelTemplates;
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

	public static void init() {}
}
