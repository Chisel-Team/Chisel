package com.cricketcraft.chisel.client;

import com.cricketcraft.chisel.Chisel;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

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
    public void onTextureStitch(TextureStitchEvent.Pre event) {
        Chisel.proxy.preTextureStitch();
        for (String s : toBeRegistered) {
            event.map.registerSprite(new ResourceLocation(s));
            Chisel.debug("Stitching texture " + s);
        }
        //CTMBlockResources.refreshAll(event.map);
    }

    public static void register(String s) {
        toBeRegistered.add(s);
    }

}
