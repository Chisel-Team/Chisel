package com.cricketcraft.chisel.client.render.ctm;

import com.cricketcraft.chisel.Chisel;
import com.cricketcraft.chisel.common.Reference;
import com.cricketcraft.chisel.common.block.BlockCarvable;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockModelShapes;
import net.minecraft.client.renderer.block.statemap.StateMap;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.client.resources.IReloadableResourceManager;
import net.minecraft.client.resources.SimpleReloadableResourceManager;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.command.CommandBase;
import net.minecraft.command.server.CommandTeleport;
import net.minecraft.util.RegistrySimple;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.property.ExtendedBlockState;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * Registers all the ctm models/blocks
 *
 * @author minecreatr
 */
public class CTMModelRegistry implements Reference{

    private static final Map<ModelResourceLocation, ModelCTM> models = new HashMap<ModelResourceLocation, ModelCTM>();


    private static void register(ModelResourceLocation location, ModelCTM model){
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
                //throw new RuntimeException("Stuff is happening");
                return location;
            }
        });
        register(location, new ModelCTM(block.getDefaultState()));
    }
}
