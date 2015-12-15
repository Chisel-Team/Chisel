package team.chisel.common.util.json;

import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.util.ResourceLocation;
import team.chisel.Chisel;
import team.chisel.api.render.ChiselFace;
import team.chisel.api.render.ChiselFaceRegistry;
import team.chisel.api.render.ChiselTextureRegistry;

import java.util.Locale;

/**
 * Json version of ChiselFace
 */
public class JsonFace extends JsonObjectBase<ChiselFace> {

    /**
     * If this is the type COMBINED then these are the identifiers of the child textures
     */
    private String[] children;

    private String layer;


    @Override
    protected ChiselFace create() {
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
                    face.addChildFace(JsonHelper.getOrCreateFace(loc));
                } else {
                    face.addTexture(JsonHelper.getOrCreateTexture(loc));
                }
            } else {
                Chisel.debug("Skipping child " + child + " because it is invalid");
            }
        }
        if (layer != null){
            if (layer.equalsIgnoreCase("Cutout")){
                face.setLayer(EnumWorldBlockLayer.CUTOUT);
            }
            else if (layer.equalsIgnoreCase("Mipped Cutout") || layer.equalsIgnoreCase("Mipped_Cutout")){
                face.setLayer(EnumWorldBlockLayer.CUTOUT_MIPPED);
            }
            else if (layer.equalsIgnoreCase("Translucent")){
                face.setLayer(EnumWorldBlockLayer.TRANSLUCENT);
            }
        }
        return face;
    }

    private static boolean checkNull(Object[] array) {
        return array == null || array.length == 0;
    }
}