package team.chisel.client.render;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import lombok.Getter;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.client.resources.model.ModelRotation;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.model.Attributes;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.TRSRTransformation;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import team.chisel.Chisel;
import team.chisel.api.block.ICarvable;
import team.chisel.common.Reference;

/**
 * Chisel's model registry
 */
public enum ChiselModelRegistry implements Reference {

    INSTANCE;
    
    public final ResourceLocation BASE_MODEL_LOC = new ResourceLocation(Chisel.MOD_ID, "block/chisel_block");
    
    @Getter
    private IBakedModel baseModel;

    private final Map<ModelResourceLocation, ModelChiselBlock> models = new HashMap<ModelResourceLocation, ModelChiselBlock>();

    public void register(ModelResourceLocation loc, ModelChiselBlock block) {
        models.put(loc, block);
    }

    public <T extends Block & ICarvable> void register(T block) {
        for (int i = 0; i < block.getTotalVariations(); i++) {
            ModelResourceLocation location;
            int blockNum = i / 16;
            if (blockNum == 0) {
                location = new ModelResourceLocation(block.getRegistryName(), "variation=" + i);
            } else {
                location = new ModelResourceLocation(block.getRegistryName() + blockNum, "variation=" + i);
            }
            register(location, new ModelChiselBlock());
            ModelResourceLocation inventoryLocation;
            if (i == 0) {
                inventoryLocation = new ModelResourceLocation(block.getRegistryName(), "inventory");
            } else {
                inventoryLocation = new ModelResourceLocation(block.getRegistryName() + blockNum, "inventory");
            }
            register(inventoryLocation, new ModelChiselBlock());
            Chisel.debug("Setting custom resource location for Item " + Item.getItemFromBlock(block) + " with meta " + i + " to location " + inventoryLocation);
            ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(block), i, inventoryLocation);
        }
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
