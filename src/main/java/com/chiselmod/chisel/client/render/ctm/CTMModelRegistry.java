package com.chiselmod.chisel.client.render.ctm;

import com.chiselmod.chisel.Chisel;
import com.chiselmod.chisel.common.Reference;
import com.chiselmod.chisel.common.block.BlockCarvable;
import com.chiselmod.chisel.common.block.subblocks.CTMSubBlock;
import com.chiselmod.chisel.common.util.SubBlockUtil;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.HashMap;
import java.util.Map;

/**
 * Registers all the ctm models/blocks
 *
 * @author minecreatr
 */
public class CTMModelRegistry implements Reference{

    private static final Map<ModelResourceLocation, ModelCTM> models = new HashMap<ModelResourceLocation, ModelCTM>();

    public static void register(ModelResourceLocation location, ModelCTM model){
        models.put(location, model);
    }

    public static class BakedEventListener{

        @SubscribeEvent
        public void onModelBake(ModelBakeEvent event){
            for (Map.Entry<ModelResourceLocation, ModelCTM> entry : models.entrySet()){
                Chisel.logger.info("Registering CTM model for "+entry.getKey().toString());
                event.modelRegistry.putObject(entry.getKey(), entry.getValue());
            }
        }
    }

    public static void register(BlockCarvable block){
        final ModelResourceLocation location = new ModelResourceLocation(MOD_ID.toLowerCase()+":"+block.getName(), "all");
        ModelLoader.setCustomStateMapper(block, new StateMapperBase() {
            protected ModelResourceLocation getModelResourceLocation(IBlockState state) {
                return location;
            }
        });
        register(location, new ModelCTM(block.getDefaultState()));
    }
}
