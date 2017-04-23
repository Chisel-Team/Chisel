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
            callback.stitch(event.getMap());
        }
//        if (Minecraft.getMinecraft().getTextureMapBlocks() != null) {
//            Map<String, TextureAtlasSprite> mapRegisteredSprites = ReflectionHelper.getPrivateValue(TextureMap.class, Minecraft.getMinecraft().getTextureMapBlocks(), "mapRegisteredSprites");
//            for (String res : ImmutableMap.copyOf(mapRegisteredSprites).keySet()) {
//                try {
//                    ResourceLocation rel = new ResourceLocation(res);
//                    rel = new ResourceLocation(rel.getResourceDomain(), "textures/" + rel.getResourcePath() + ".png");
//                    MetadataSectionChisel.V1 metadata = Minecraft.getMinecraft().getResourceManager().getResource(rel).getMetadata(MetadataSectionChisel.SECTION_NAME);
//                    if (metadata != null) {
//                        for (TextureSpriteCallback callback : metadata.additionalTextures) {
//                            callback.stitch(event.getMap());
//                        }
//                    }
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
    }

    public static void register(TextureSpriteCallback callback) {
        textures.add(callback);
    }

}
