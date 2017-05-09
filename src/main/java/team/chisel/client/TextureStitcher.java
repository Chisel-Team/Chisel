package team.chisel.client;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.common.collect.ImmutableMap;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.common.ProgressManager;
import net.minecraftforge.fml.common.ProgressManager.ProgressBar;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import team.chisel.Chisel;
import team.chisel.api.render.TextureSpriteCallback;
import team.chisel.client.render.texture.MetadataSectionChisel;

public class TextureStitcher {

    private static List<TextureSpriteCallback> textures = new ArrayList<TextureSpriteCallback>();

    @SubscribeEvent
    public void onTextureStitch(TextureStitchEvent.Pre event) {
        Chisel.proxy.preTextureStitch();
        for (TextureSpriteCallback callback : textures) {
            callback.stitch(event.getMap());
        }
        
        if (Minecraft.getMinecraft().getTextureMapBlocks() != null) {
            Map<String, TextureAtlasSprite> mapRegisteredSprites = ReflectionHelper.getPrivateValue(TextureMap.class, Minecraft.getMinecraft().getTextureMapBlocks(), "mapRegisteredSprites");
            ProgressBar prog = ProgressManager.push("Loading Chisel metadata", mapRegisteredSprites.size());
            for (String res : ImmutableMap.copyOf(mapRegisteredSprites).keySet()) {
                try {
                    ResourceLocation rel = new ResourceLocation(res);
                    prog.step(rel.toString());
                    rel = new ResourceLocation(rel.getResourceDomain(), "textures/" + rel.getResourcePath() + ".png");
                    MetadataSectionChisel metadata = ClientUtil.getMetadata(rel);
                    if (metadata != null) {
                        for (ResourceLocation r : metadata.getAdditionalTextures()) {
                            event.getMap().registerSprite(r);
                        }
                    }
                }
                catch (FileNotFoundException e) {} // Ignore these, they are reported by vanilla
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
            ProgressManager.pop(prog);
        }
    }

    public static void register(TextureSpriteCallback callback) {
        textures.add(callback);
    }

}
