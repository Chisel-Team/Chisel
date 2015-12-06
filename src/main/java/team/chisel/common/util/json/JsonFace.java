package team.chisel.common.util.json;

import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.ResourceLocation;
import team.chisel.Chisel;
import team.chisel.api.render.ChiselFace;
import team.chisel.api.render.ChiselFaceRegistry;
import team.chisel.api.render.ChiselTextureRegistry;

/**
 * Json version of ChiselFace
 */
public class JsonFace extends JsonObjectBase<ChiselFace, TextureMap> {

    /**
     * If this is the type COMBINED then these are the identifiers of the child textures
     */
    private String[] children;


    @Override
    protected ChiselFace create(TextureMap map) {
        if (checkNull(children)) {
            throw new IllegalArgumentException("COMBINED texture type must have children textures!");
        }
        ChiselFace face = new ChiselFace();
        for (String child : children) {
            ResourceLocation loc = new ResourceLocation(child);
            if (ChiselFaceRegistry.isFace(loc)) {
                face.addChildFace(ChiselFaceRegistry.getFace(loc));
            } else if (ChiselTextureRegistry.isTex(loc)) {
                face.addTexture(ChiselTextureRegistry.getTex(loc));
            } else if (JsonHelper.isValid(loc)) {
                if (JsonHelper.isCombined(loc)) {
                    face.addChildFace(JsonHelper.getFaceFromResource(loc));
                } else {
                    face.addTexture(JsonHelper.getTextureFromResource(loc));
                }
            } else {
                Chisel.debug("Skipping child " + child + " because it is invalid");
            }
        }
        return face;
    }

    private static boolean checkNull(Object[] array) {
        return array == null || array.length == 0;
    }
}