package team.chisel.api.block;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.registries.IForgeRegistry;
import team.chisel.common.block.ItemChiselBlock;

/**
 * Factory to create builders to create blocks
 */
@ParametersAreNonnullByDefault
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ChiselBlockFactory {

    @Getter
    private final IForgeRegistry<Block> registry;
    private final String domain;

    public static ChiselBlockFactory newFactory(IForgeRegistry<Block> registry, String domain) {
        return new ChiselBlockFactory(registry, domain);
    }

    public <T extends Block & ICarvable> ChiselBlockBuilder<T> newBlock(Material material, String blockName, BlockCreator<T> creator, Class<T> blockClass) {
        return newBlock(material, blockName, creator, blockClass, ItemBlock.class);
    }

    public <T extends Block & ICarvable> ChiselBlockBuilder<T> newBlock(Material material, String blockName, BlockCreator<T> creator, Class<T> blockClass, Class<? extends ItemBlock> itemBlockClass) {
        return newBlock(material, blockName, new BlockProvider<T>() {

            @Override
            public T createBlock(Material material, int index, int totalBlocks, VariationData... data) {
                return creator.createBlock(material, index, totalBlocks, data);
            }

            @Override
            public Class<T> getBlockClass() {
                return blockClass;
            }

            @Override
            public ItemBlock createItemBlock(T block) {
                return (ItemBlock) new ItemChiselBlock(block).setRegistryName(block.getRegistryName());
            }
        });
    }

    public <T extends Block & ICarvable> ChiselBlockBuilder<T> newBlock(Material material, String blockName, BlockProvider<T> provider) {
        return newBlock(material, blockName, blockName, provider);
    }
    
    public <T extends Block & ICarvable> ChiselBlockBuilder<T> newBlock(Material material, String blockName, @Nullable String group, BlockProvider<T> provider) {
        return new ChiselBlockBuilder<T>(registry, material, domain, blockName, group, provider);
    }
}
