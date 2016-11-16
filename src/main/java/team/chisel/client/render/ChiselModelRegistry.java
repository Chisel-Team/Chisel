package team.chisel.client.render;

import javax.annotation.Nonnull;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.ItemMeshDefinition;
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

    public <T extends Block & ICarvable> void register(@Nonnull T block) {
        ModelLoader.setCustomStateMapper(block, new StateMapperBase() {
            
            @Override
            protected @Nonnull ModelResourceLocation getModelResourceLocation(@Nonnull IBlockState state) {
                VariationData data = block.getVariationData(block.getVariationIndex(state));
                String name = block.getRegistryName().getResourcePath();
                while (Character.isDigit(name.charAt(name.length() - 1))) {
                    name = name.substring(0, name.length() - 1);
                }
                return new ModelResourceLocation(new ResourceLocation("chisel", "default"), data.path);
            }
        });
        ModelLoader.setCustomMeshDefinition(Item.getItemFromBlock(block), new ItemMeshDefinition() {
            
            @Override
            @SuppressWarnings("unchecked")
            public @Nonnull ModelResourceLocation getModelLocation(@Nonnull ItemStack stack) {
                T block = (T) Block.getBlockFromItem(stack.getItem());
                VariationData data = block.getVariationData(stack.getItemDamage());
                String name = block.getRegistryName().getResourcePath();
                while (Character.isDigit(name.charAt(name.length() - 1))) {
                    name = name.substring(0, name.length() - 1);
                }
                return new ModelResourceLocation(new ResourceLocation("chisel", "default"), data.path);
            }
        });
    }
}
