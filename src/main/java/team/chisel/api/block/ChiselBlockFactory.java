package team.chisel.api.block;

import com.tterrag.registrate.Registrate;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Material;
import team.chisel.common.Reference;
import team.chisel.common.block.BlockCarvable;
import team.chisel.common.block.ItemChiselBlock;

import javax.annotation.Nullable;

/**
 * Factory to create builders to create blocks
 */
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ChiselBlockFactory {

    private final Registrate registrate;

    public static ChiselBlockFactory newFactory(Registrate registrate) {
        return new ChiselBlockFactory(registrate);
    }

    public ChiselBlockBuilder<BlockCarvable> newType(Material material, String blockName) {
        return newType(material, blockName, BlockCarvable::new);
    }

    public <T extends Block & ICarvable> ChiselBlockBuilder<T> newType(Material material, String blockName, BlockCreator<T> creator) {
        return newType(material, blockName, new BlockProvider<T>() {

            @Override
            public T createBlock(Block.Properties properties, VariationData data) {
                return creator.createBlock(properties, data);
            }

            @Override
            public BlockItem createBlockItem(T block, Item.Properties properties) {
                return new ItemChiselBlock(block, properties);
            }
        });
    }

    public <T extends Block & ICarvable> ChiselBlockBuilder<T> newType(Material material, String blockName, BlockProvider<T> provider) {
        return newType(material, blockName, blockName, provider);
    }

    public <T extends Block & ICarvable> ChiselBlockBuilder<T> newType(Material material, String blockName, @Nullable String group, BlockProvider<T> provider) {
        return new ChiselBlockBuilder<T>(this, registrate, material, blockName, group == null ? null : BlockTags.create(new ResourceLocation(Reference.MOD_ID, group)), provider);
    }

    public Registrate getRegistrate() {
        return registrate;
    }
}
