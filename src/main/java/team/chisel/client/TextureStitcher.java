package team.chisel.client;

import team.chisel.Chisel;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import team.chisel.api.render.TextureSpriteCallback;

import java.util.ArrayList;
import java.util.List;

/**
 * To stitch textures
 *
 * @author minecreatr
 */
public class TextureStitcher {

    private static List<TextureSpriteCallback> textures = new ArrayList<TextureSpriteCallback>();

    @SubscribeEvent
    public void onTextureStitch(TextureStitchEvent.Pre event) {
        Chisel.proxy.preTextureStitch();
        for (TextureSpriteCallback callback : textures) {
            callback.stitch(event.map);
        }
        //CTMBlockResources.refreshAll(event.map);
    }

    public static void register(TextureSpriteCallback callback) {
        textures.add(callback);
    }

}
