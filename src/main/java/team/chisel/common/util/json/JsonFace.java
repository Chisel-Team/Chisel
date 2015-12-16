package team.chisel.common.util.json;

import java.util.List;

import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.util.ResourceLocation;
import team.chisel.Chisel;
import team.chisel.api.render.ChiselFace;
import team.chisel.api.render.ChiselFaceRegistry;
import team.chisel.api.render.ChiselTextureRegistry;
import team.chisel.api.render.IChiselTexture;

import com.google.common.base.Preconditions;

/**
 * Json version of ChiselFace
 */
public class JsonFace extends JsonObjectBase<ChiselFace> {

    /**
     * If this is the type COMBINED then these are the identifiers of the child textures
     */
    private String[] children;

    @Override
    protected ChiselFace create() {
        Preconditions.checkNotNull(children, "COMBINED texture type must have children textures!");
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
        face.setLayer(getLayer(face.getTextureList()));
        return face;
    }

    private EnumWorldBlockLayer getLayer(List<IChiselTexture> textures) {
        EnumWorldBlockLayer layer = EnumWorldBlockLayer.SOLID;
        for (IChiselTexture tex : textures) {
            EnumWorldBlockLayer texLayer = tex.getLayer();
            if (texLayer.ordinal() > layer.ordinal()) {
                layer = texLayer;
            }
        }
        return layer;
    }
}