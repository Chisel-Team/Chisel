package team.chisel.client.render;

import java.util.function.IntFunction;

import javax.annotation.Nonnull;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import team.chisel.api.block.ICarvable;
import team.chisel.api.block.VariationData;
import team.chisel.common.Reference;
import team.chisel.common.util.NonnullType;

/**
 * Chisel's model registry
 */
public enum ChiselModelRegistry implements Reference {
    
    INSTANCE;
    
    @RequiredArgsConstructor
    private static class BlockStateMapper<T extends Block & ICarvable> implements IntFunction<@NonnullType ModelResourceLocation> {

        @Getter
        private final T block;

        @Override
        public @Nonnull ModelResourceLocation apply(int variation) {
            VariationData data = block.getVariationData(variation);
            String name = block.getRegistryName().getResourcePath();
            while (Character.isDigit(name.charAt(name.length() - 1))) {
                name = name.substring(0, name.length() - 1);
            }
            return new ModelResourceLocation(new ResourceLocation("chisel", name), data.name);
        }
    }

    public <T extends Block & ICarvable> void register(@Nonnull T block) {
        final BlockStateMapper<T> mapper = new BlockStateMapper<>(block);
        ModelLoader.setCustomStateMapper(block, new StateMapperBase() {
            
            @Override
            protected @Nonnull ModelResourceLocation getModelResourceLocation(@Nonnull IBlockState state) {
                return mapper.apply(block.getVariationIndex(state));
            }
        });

        Item item = Item.getItemFromBlock(block);
        if (item == null) {
            return;
        }

        ModelLoader.setCustomMeshDefinition(item, stack -> mapper.apply(stack.getItemDamage()));
        
        // Prevent vanilla searching for item JSONs
        ModelLoader.registerItemVariants(item);
    }

}
