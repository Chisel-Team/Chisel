package com.chiselmod.chisel.client;

import com.chiselmod.chisel.Chisel;
import com.chiselmod.chisel.client.render.CTMBlockResources;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * To stitch textures
 *
 * @author minecreatr
 */
public class TextureStitcher {

    public static List<String> toBeRegistered = new ArrayList<String>();


    @SubscribeEvent
    public void onTextureStitch(TextureStitchEvent event){
        for (String s : toBeRegistered){
            event.map.registerSprite(new ResourceLocation(s));
            Chisel.logger.info("Stitching texture "+s);
        }
        CTMBlockResources.refreshAll(event.map);
    }

    public static void register(String s){
        toBeRegistered.add(s);
    }

}
