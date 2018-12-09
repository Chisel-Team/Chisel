package team.chisel.client.render;

import java.util.Map;

import javax.annotation.Nonnull;

import com.google.common.collect.Maps;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import team.chisel.api.block.ICarvable;
import team.chisel.api.block.VariationData;
import team.chisel.common.Reference;

/**
 * Chisel's model registry
 */
public enum ChiselModelRegistry implements Reference {
    
    INSTANCE;
    
    @RequiredArgsConstructor
    private static class ChiselStateMapper<T extends Block & ICarvable> extends StateMapperBase {

        @Getter
        private final T block;
        
        ModelResourceLocation getModelResourceLocation(VariationData data, String variant) {
            String name = block.getRegistryName().getResourcePath();
            while (Character.isDigit(name.charAt(name.length() - 1))) {
                name = name.substring(0, name.length() - 1);
            }
            // This block has additional properties, so don't use a single blockstate file, instead use the provided variant path
            if (block.getBlockState().getProperties().size() > 1) {
                return new ModelResourceLocation(new ResourceLocation("chisel", data.path), variant);
            } else { // Otherwise, break the variant name off of the path to use as the variant path
                int lastslash = data.path.lastIndexOf('/');
                return new ModelResourceLocation(new ResourceLocation("chisel", data.path.substring(0, lastslash)), data.path.substring(lastslash + 1));
            }
        }

        @Override
        protected ModelResourceLocation getModelResourceLocation(@Nonnull IBlockState state) {
            VariationData data = block.getVariationData(block.getVariationIndex(state));
            Map<IProperty<?>, Comparable<?>> map = Maps.newLinkedHashMap(state.getProperties());
            map.remove(block.getMetaProp());
            return getModelResourceLocation(data, getPropertyString(map));
        }
    }

    public <T extends Block & ICarvable> void register(@Nonnull T block) {
        final ChiselStateMapper<T> mapper = new ChiselStateMapper<>(block);
        ModelLoader.setCustomStateMapper(block, mapper);

        Item item = Item.getItemFromBlock(block);
        if (item == null) {
            return;
        }
        
        for (VariationData var : block.getVariations()) {
            if (!var.name.isEmpty()) {
                ModelLoader.setCustomModelResourceLocation(item, var.index - block.getIndex() * 16, mapper.getModelResourceLocation(var, "inventory"));
            }
        }
        
        // Prevent vanilla searching for item JSONs
        ModelLoader.registerItemVariants(item);
    }

}
