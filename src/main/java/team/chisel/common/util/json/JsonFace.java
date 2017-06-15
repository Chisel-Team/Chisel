package team.chisel.common.util.json;

import com.google.common.base.Function;
import com.google.common.base.Preconditions;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.ResourceLocation;
import team.chisel.client.ChiselFace;
import team.chisel.ctm.api.texture.IChiselFace;

/**
 * Json version of ChiselFace
 */
@Deprecated
public class JsonFace extends JsonObjectBase<IChiselFace> {

    /**
     * If this is the type COMBINED then these are the identifiers of the child textures
     */
    private String[] textures;
    
    private String particle;

    @Override
    protected IChiselFace create(ResourceLocation loc) {
        Preconditions.checkNotNull(textures, JsonHelper.FACE_EXTENSION + " files must have a textures field!");
        ChiselFace face = new ChiselFace(loc);
        for (String child : textures) {
            if (JsonHelper.isLocalPath(child)) {
                child = JsonHelper.toAbsolutePath(child, loc);
            }
            ResourceLocation childLoc = new ResourceLocation(child);
            if (JsonHelper.isFace(childLoc)) {
                face.addChildFace(JsonHelper.getOrCreateFace(childLoc));
            } else if (JsonHelper.isTex(childLoc)) {
                face.addTexture(JsonHelper.getOrCreateTexture(childLoc));
            } else {
                if (JsonHelper.isValidFace(childLoc)) {
                    face.addChildFace(JsonHelper.getOrCreateFace(childLoc));
                } else {
                    face.addTexture(JsonHelper.getOrCreateTexture(childLoc));
                }
            }
        }
        
        if (particle != null) {
            if (JsonHelper.isLocalPath(particle)) {
                particle = JsonHelper.toAbsolutePath(particle, loc);
            }
            face.setParticle(JsonHelper.getOrCreateTexture(new ResourceLocation(particle)).getParticle());
        }

        return face;
    }
}