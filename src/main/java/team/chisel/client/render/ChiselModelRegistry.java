package team.chisel.client.render;

import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import team.chisel.Chisel;
import team.chisel.common.Reference;
import team.chisel.common.block.BlockCarvable;

import java.util.HashMap;
import java.util.Map;

/**
 * Chisel's model registry
 */
public class ChiselModelRegistry implements Reference {

    private static final Map<ModelResourceLocation, ModelChiselBlock> models = new HashMap<ModelResourceLocation, ModelChiselBlock>();


    public static class BakedEventListener {

        @SubscribeEvent
        public void onModelBake(ModelBakeEvent event) {
            for (Map.Entry<ModelResourceLocation, ModelChiselBlock> entry : models.entrySet()) {
                Chisel.debug("Registering model for " + entry.getKey().toString());
                event.modelRegistry.putObject(entry.getKey(), entry.getValue());
            }
        }
    }

    public static void register(ModelResourceLocation loc, ModelChiselBlock block){
        models.put(loc, block);
    }

    public static void register(BlockCarvable block){
        for (int i = 0 ; i < block.getTotalMetaVariations() ; i++){
            ModelResourceLocation location;
            if (i == 0){
                location = new ModelResourceLocation(MOD_ID.toLowerCase()+":"+block.getName(), "variation="+i);
            }
            else {
                location = new ModelResourceLocation(MOD_ID.toLowerCase()+":"+block.getName()+i, "variation="+i);
            }
            register(location, new ModelChiselBlock());
            ModelResourceLocation inventoryLocation;
            if (i == 0){
                inventoryLocation = new ModelResourceLocation(MOD_ID.toLowerCase()+":"+block.getName(), "inventory");
            }
            else {
                inventoryLocation = new ModelResourceLocation(MOD_ID.toLowerCase()+":"+block.getName()+i, "inventory");
            }
            register(inventoryLocation, new ModelChiselBlock());
            Chisel.debug("Setting custom resource location for Item "+Item.getItemFromBlock(block)+
                    " with meta "+i+ " to location "+inventoryLocation);
            ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(block), i, inventoryLocation);
        }
    }

}
