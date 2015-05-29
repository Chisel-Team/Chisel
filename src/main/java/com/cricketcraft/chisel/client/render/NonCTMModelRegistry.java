package com.cricketcraft.chisel.client.render;

import com.cricketcraft.chisel.Chisel;
import com.cricketcraft.chisel.client.render.ctm.ModelCTM;
import com.cricketcraft.chisel.common.CarvableBlocks;
import com.cricketcraft.chisel.common.Reference;
import com.cricketcraft.chisel.common.variation.Variation;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.client.resources.model.WeightedBakedModel;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.HashMap;
import java.util.Map;

/**
 * Registry for non ctm models
 *
 * @author minecreatr
 */
public class NonCTMModelRegistry implements Reference{

    private static final Map<ModelResourceLocation, IBakedModel> models = new HashMap<ModelResourceLocation, IBakedModel>();

    private static void register(ModelResourceLocation location, IBakedModel model){
        models.put(location, model);
    }

    public static class BakedEventListener{

        @SubscribeEvent
        public void onModelBake(ModelBakeEvent event){
            for (Map.Entry<ModelResourceLocation, IBakedModel> entry : models.entrySet()){
                Chisel.logger.info("Registering Non CTM model for "+entry.getKey().toString()+" model class is "+entry.getValue().getClass());
                if (entry.getValue() instanceof WeightedBakedModel){
                    Chisel.logger.info("Model above is weighted");
                }
                event.modelRegistry.putObject(entry.getKey(), entry.getValue());
                ModelResourceLocation l = (ModelResourceLocation)entry.getKey();
                //event.modelManager.getMissingModel()
            }
        }
    }

    public static void register(String block, String variation, int amount, int type){
        for (int i=0;i<amount;i++) {
            ModelResourceLocation location;
            if (i==0){
                location = new ModelResourceLocation(MOD_ID.toLowerCase() + ":" + block, "variation=" + variation);
            }
            else {
                location = new ModelResourceLocation(MOD_ID.toLowerCase() + ":" + block+i, "variation=" + variation);
            }
            if (type!=IBlockResources.NORMAL){
                Chisel.logger.info("Registering model coordinate variation for "+block+" variation "+variation);
                register(location, ModelCoordinateVariations.newModel(variation));
                return;
            }
            register(location, new ModelNonCTM());
        }
    }

    public static void registerInventory(CarvableBlocks block, int index) {
        ModelResourceLocation location;
        if (index == 0) {
            location = new ModelResourceLocation(MOD_ID.toLowerCase() + ":" + block.getName(), "inventory");
        } else {
            location = new ModelResourceLocation(MOD_ID.toLowerCase() + ":" + block.getName() + index, "inventory");
        }
        register(location, new ModelNonCTM());
//        if (index!=0) {
//            if (block.getBlock(index)==null){
//                throw new RuntimeException("Null block for index "+index+" and block "+block.getName());
//            }
//            if (Item.getItemFromBlock(block.getBlock(index))==null){
//                throw new RuntimeException("null item");
//            }
////            Item item = Item.getItemFromBlock(block.getBlock(index));
////            ModelLoader.setCustomModelResourceLocation(item, -1, location);
////            int totalMeta = block.getVariants().length-(index*16);
////            for (int i=0;i<totalMeta;i++) {
////                ModelLoader.setCustomModelResourceLocation(item, i, location);
////            }
//        }

    }


}
