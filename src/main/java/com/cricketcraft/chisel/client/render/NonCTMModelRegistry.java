package com.cricketcraft.chisel.client.render;

import com.cricketcraft.chisel.Chisel;
import com.cricketcraft.chisel.client.render.ctm.ModelCTM;
import com.cricketcraft.chisel.common.CarvableBlocks;
import com.cricketcraft.chisel.common.Reference;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.HashMap;
import java.util.Map;

/**
 * Registry for non ctm models
 *
 * @author minecreatr
 */
public class NonCTMModelRegistry implements Reference{

    private static final Map<ModelResourceLocation, ModelNonCTM> models = new HashMap<ModelResourceLocation, ModelNonCTM>();

    private static void register(ModelResourceLocation location, ModelNonCTM model){
        models.put(location, model);
    }

    public static class BakedEventListener{

        @SubscribeEvent
        public void onModelBake(ModelBakeEvent event){
            for (Map.Entry<ModelResourceLocation, ModelNonCTM> entry : models.entrySet()){
                Chisel.logger.info("Registering Non CTM model for "+entry.getKey().toString());
                event.modelRegistry.putObject(entry.getKey(), entry.getValue());
                //event.modelManager.getMissingModel()
            }
        }
    }

    public static void register(String block, String variation, int amount){
        for (int i=0;i<amount;i++) {
            ModelResourceLocation location;
            if (i==0){
                location = new ModelResourceLocation(MOD_ID.toLowerCase() + ":" + block, "variation=" + variation);
            }
            else {
                location = new ModelResourceLocation(MOD_ID.toLowerCase() + ":" + block+i, "variation=" + variation);
            }
            register(location, new ModelNonCTM());
        }
    }

    public static void registerInventory(CarvableBlocks block, int amount){
        for (int i=0;i<amount;i++) {
            ModelResourceLocation location;
            if (i==0){
                location = new ModelResourceLocation(MOD_ID.toLowerCase() + ":" + block.getName(), "inventory");
            }
            else {
                location = new ModelResourceLocation(MOD_ID.toLowerCase() + ":" + block.getName()+i, "inventory");
            }
            register(location, new ModelNonCTM());
        }
    }


}
