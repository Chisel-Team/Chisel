package team.chisel.client.render;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import team.chisel.Chisel;
import team.chisel.api.block.ICarvable;
import team.chisel.api.block.VariationData;
import team.chisel.common.Reference;

/**
 * Chisel's model registry
 */
public enum ChiselModelRegistry implements Reference {
    
    INSTANCE;

    private final Map<ModelResourceLocation, ModelChiselBlock> models = new HashMap<ModelResourceLocation, ModelChiselBlock>();

    public void register(ModelResourceLocation loc, ModelChiselBlock block) {
//        ((Map<ResourceLocation, ModelBlockDefinition>)blockDefinitions.get(event.modelBakery)).put(loc)
        models.put(loc, block);
    }

    public <T extends Block & ICarvable> void register(T block) {
        ModelLoader.setCustomStateMapper(block, new StateMapperBase() {
            
            @Override
            protected ModelResourceLocation getModelResourceLocation(IBlockState state) {
                VariationData data = block.getVariationData(block.getVariationIndex(state));
                String name = block.getRegistryName().getResourcePath();
                while (Character.isDigit(name.charAt(name.length() - 1))) {
                    name = name.substring(0, name.length() - 1);
                }
                return new ModelResourceLocation(new ResourceLocation("chisel", "default"), name + "/" + data.name);
            }
        });
        ModelLoader.setCustomMeshDefinition(Item.getItemFromBlock(block), new ItemMeshDefinition() {
            
            @Override
            @SuppressWarnings("unchecked")
            public ModelResourceLocation getModelLocation(ItemStack stack) {
                T block = (T) Block.getBlockFromItem(stack.getItem());
                VariationData data = block.getVariationData(stack.getItemDamage());
                String name = block.getRegistryName().getResourcePath();
                while (Character.isDigit(name.charAt(name.length() - 1))) {
                    name = name.substring(0, name.length() - 1);
                }
                return new ModelResourceLocation(new ResourceLocation("chisel", "default"), name + "/" + data.name);
            }
        });
    }

    @SubscribeEvent
    public void onModelBake(ModelBakeEvent event) throws IOException {
        for (Map.Entry<ModelResourceLocation, ModelChiselBlock> entry : models.entrySet()) {
            Chisel.debug("Registering model for " + entry.getKey().toString());
            event.getModelRegistry().putObject(entry.getKey(), entry.getValue());
        }
    }
}
