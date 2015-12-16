package team.chisel.common.util.json;

import java.util.List;

import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.util.ResourceLocation;
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
    protected ChiselFace create(ResourceLocation loc) {
        Preconditions.checkNotNull(children, "COMBINED texture type must have children textures!");
        ChiselFace face = new ChiselFace();
        for (String child : children) {
            ResourceLocation childLoc = new ResourceLocation(child);
            if (ChiselFaceRegistry.isFace(childLoc)) {
                face.addChildFace(ChiselFaceRegistry.getFace(childLoc));
            } else if (ChiselTextureRegistry.isTex(childLoc)) {
                face.addTexture(ChiselTextureRegistry.getTex(childLoc));
            } else {
                if (JsonHelper.isCombined(childLoc)) {
                    face.addChildFace(JsonHelper.getOrCreateFace(childLoc));
                } else {
                    face.addTexture(JsonHelper.getOrCreateTexture(childLoc));
                }
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