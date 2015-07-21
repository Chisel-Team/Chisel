package com.cricketcraft.chisel.client;

import com.cricketcraft.chisel.Chisel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
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
            TextureAtlasSprite sprite = event.map.registerSprite(new ResourceLocation(s));
            Chisel.logger.info("Stitching texture "+s);
        }
        //CTMBlockResources.refreshAll(event.map);
    }

    public static void register(String s){
        toBeRegistered.add(s);
    }

}
