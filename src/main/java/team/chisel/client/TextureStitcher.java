package team.chisel.client;

import java.util.ArrayList;
import java.util.List;

import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import team.chisel.Chisel;
import team.chisel.api.render.TextureSpriteCallback;

public class TextureStitcher {

    private static List<TextureSpriteCallback> textures = new ArrayList<TextureSpriteCallback>();

    @SubscribeEvent
    public void onTextureStitch(TextureStitchEvent.Pre event) {
        Chisel.proxy.preTextureStitch();
        for (TextureSpriteCallback callback : textures) {
            callback.stitch(event.map);
        }
    }

    public static void register(TextureSpriteCallback callback) {
        textures.add(callback);
    }

}
