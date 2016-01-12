package team.chisel.client.render;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import team.chisel.Chisel;
import team.chisel.api.block.ICarvable;
import team.chisel.common.Reference;

/**
 * Chisel's model registry
 */
public enum ChiselModelRegistry implements Reference {

    INSTANCE;

    private final Map<ModelResourceLocation, ModelChiselBlock> models = new HashMap<ModelResourceLocation, ModelChiselBlock>();

    public void register(ModelResourceLocation loc, ModelChiselBlock block) {
        models.put(loc, block);
    }

    public <T extends Block & ICarvable> void register(T block) {
        for (int i = 0; i < block.getTotalVariations(); i++) {
            ModelResourceLocation location;
            if (i == 0) {
                location = new ModelResourceLocation(block.getRegistryName(), "variation=" + i);
            } else {
                location = new ModelResourceLocation(block.getRegistryName() + i, "variation=" + i);
            }
            register(location, new ModelChiselBlock());
            ModelResourceLocation inventoryLocation;
            if (i == 0) {
                inventoryLocation = new ModelResourceLocation(block.getRegistryName(), "inventory");
            } else {
                inventoryLocation = new ModelResourceLocation(block.getRegistryName() + i, "inventory");
            }
            register(inventoryLocation, new ModelChiselBlock());
            Chisel.debug("Setting custom resource location for Item " + Item.getItemFromBlock(block) + " with meta " + i + " to location " + inventoryLocation);
            ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(block), i, inventoryLocation);
        }
    }

    @SubscribeEvent
    public void onModelBake(ModelBakeEvent event) {
        for (Map.Entry<ModelResourceLocation, ModelChiselBlock> entry : models.entrySet()) {
            Chisel.debug("Registering model for " + entry.getKey().toString());
            event.modelRegistry.putObject(entry.getKey(), entry.getValue());
        }
    }
}
