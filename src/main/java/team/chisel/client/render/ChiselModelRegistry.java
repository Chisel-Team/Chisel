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
import team.chisel.common.block.BlockCarvablePane;

/**
 * Chisel's model registry
 */
public enum ChiselModelRegistry implements Reference {
    
    INSTANCE;

    public <T extends Block & ICarvable> void register(@Nonnull T block) {
        if (block instanceof BlockCarvablePane) {
            ModelLoader.setCustomStateMapper(block, new StateMapperBase() {
            
                @Override
                protected @Nonnull ModelResourceLocation getModelResourceLocation(@Nonnull IBlockState state) {
                    VariationData data = block.getVariationData(block.getVariationIndex(state));
                    String name = block.getRegistryName().getResourcePath();
                    while (Character.isDigit(name.charAt(name.length() - 1))) {
                        name = name.substring(0, name.length() - 1);
                    }
                    return new ModelResourceLocation(new ResourceLocation("chisel", data.path), "normal");
                }
            });
            for (int i = 0; i < block.getVariations().length; i++) {
                ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(block), i, new ModelResourceLocation(new ResourceLocation("chisel", block.getVariationData(i).path), "inventory"));
            }
            return;
        }
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
        // Nope
        ModelLoader.registerItemVariants(Item.getItemFromBlock(block));
    }

}
