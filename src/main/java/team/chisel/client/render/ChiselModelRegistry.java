package team.chisel.client.render;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import lombok.Getter;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelDragon;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.model.ModelBlockDefinition;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.client.resources.model.ModelRotation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.model.Attributes;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.TRSRTransformation;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import team.chisel.Chisel;
import team.chisel.api.block.ICarvable;
import team.chisel.api.block.VariationData;
import team.chisel.common.Reference;

/**
 * Chisel's model registry
 */
public enum ChiselModelRegistry implements Reference {
    
    INSTANCE;
    
    public final ResourceLocation BASE_MODEL_LOC = new ResourceLocation(Chisel.MOD_ID, "block/chisel_block");
    
    private final Field blockDefinitions = ReflectionHelper.findField(ModelBakery.class, "field_177614_t", "blockDefinitions");
    
    @Getter
    private IBakedModel baseModel;

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
                String name = block.getRegistryName();
                name = name.substring(name.indexOf(":") + 1, name.length());
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
                String name = block.getRegistryName();
                name = name.substring(name.indexOf(":") + 1, name.length());
                while (Character.isDigit(name.charAt(name.length() - 1))) {
                    name = name.substring(0, name.length() - 1);
                }
                return new ModelResourceLocation(new ResourceLocation("chisel", "default"), name + "/" + data.name);
            }
        });
    }

    @SubscribeEvent
    public void onModelBake(ModelBakeEvent event) throws IOException {
        IModel model = event.modelLoader.getModel(BASE_MODEL_LOC);
        baseModel = model.bake(new TRSRTransformation(ModelRotation.X0_Y0), Attributes.DEFAULT_BAKED_FORMAT, r -> Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(r.toString()));
        for (Map.Entry<ModelResourceLocation, ModelChiselBlock> entry : models.entrySet()) {
            Chisel.debug("Registering model for " + entry.getKey().toString());
            event.modelRegistry.putObject(entry.getKey(), entry.getValue());
        }
    }
}
