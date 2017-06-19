package team.chisel.client;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.ParametersAreNonnullByDefault;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import team.chisel.Chisel;

public class TextureStitcher {
    
    /**
     * This sprite stitches itself! Wow!
     * 
     * But seriously, this is a bad way of doing things. I can't deprecate it enough.
     */
    @Deprecated
    @ParametersAreNonnullByDefault
    public static class MagicStitchingSprite extends TextureAtlasSprite {
        
        private TextureAtlasSprite parent = Minecraft.getMinecraft().getTextureMapBlocks().getMissingSprite();
        
        public MagicStitchingSprite(ResourceLocation name) {
            this(name.toString());
        }
        
        public MagicStitchingSprite(String spriteName) {
            super(spriteName);
            TextureStitcher.register(this);
        }

        void preStitch(TextureMap map) {
            parent = map.registerSprite(new ResourceLocation(this.getIconName()));
        }
        
        void postStitch() {
            this.copyFrom(parent);
        }
    }

    private static List<MagicStitchingSprite> textures = new ArrayList<MagicStitchingSprite>();

    @SubscribeEvent
    public void onTextureStitchPre(TextureStitchEvent.Pre event) {
        Chisel.proxy.preTextureStitch();
        for (MagicStitchingSprite callback : textures) {
            callback.preStitch(event.getMap());
        }
    }
    
    @SubscribeEvent
    public void onTextureStitchPost(TextureStitchEvent.Post event) {
        textures.forEach(MagicStitchingSprite::postStitch);
    }

    public static void register(MagicStitchingSprite callback) {
        textures.add(callback);
    }
}
