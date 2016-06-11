package team.chisel.common.util.json;

import java.util.Locale;
import java.util.Optional;

import javax.annotation.Nullable;

import com.google.gson.JsonObject;

import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.ResourceLocation;
import team.chisel.api.render.IBlockRenderType;
import team.chisel.api.render.IChiselTexture;
import team.chisel.api.render.TextureInfo;
import team.chisel.api.render.TextureSpriteCallback;
import team.chisel.client.TextureStitcher;
import team.chisel.common.init.TextureTypeRegistry;

import com.google.common.base.Preconditions;


/**
 * Raw version of IChiselTexture
 */
public class JsonTexture extends JsonObjectBase<IChiselTexture<?>> {

    /**
     * The String for the type of texture
     */
    private String type;

    /**
     * The Actual path to the different png textures
     * Is an Array because some texture types need more than one
     * For example in CTM the first one is the plain block texture and the second is the special
     * ctm png
     */
    private String[] textures;

    @Nullable
    private String layer;

    @Nullable
    private JsonObject info;

    private boolean fullbright;

    @Override
    protected IChiselTexture<?> create(ResourceLocation loc) {
        Preconditions.checkArgument(TextureTypeRegistry.isValid(this.type), "Error loading texture %s. Texture Type " + this.type + " is not valid", loc);

        IBlockRenderType type = TextureTypeRegistry.getType(this.type);
        Preconditions.checkArgument(textures != null || type.requiredTextures() == 1,
                "Error loading texture %s. Texture type %s requires %s textures, the texture name can only be inferred for textures that require 1.", loc, this.type, type.requiredTextures());
        if (textures != null) {
            Preconditions.checkArgument(textures.length == type.requiredTextures(), "Error loading texture %s. Texture type %s requires exactly %s textures. %s were provided.", loc, this.type,
                    type.requiredTextures(), textures.length);
        }

        TextureSpriteCallback[] callbacks = new TextureSpriteCallback[type.requiredTextures()];
        if (textures == null) {
            callbacks[0] = new TextureSpriteCallback(new ResourceLocation(loc.getResourceDomain(), JsonHelper.toTexturePath(loc.getResourcePath())));
            TextureStitcher.register(callbacks[0]);
        } else {
            for (int i = 0; i < this.textures.length; i++) {
                String tex = this.textures[i];
                if (JsonHelper.isLocalPath(tex)) {
                    // TODO a better way to fix prefixing
                    String path = JsonHelper.toAbsolutePath(tex, loc);
                    path = path.substring(path.indexOf(':') + 1);
                    tex = JsonHelper.toTexturePath(path);
                }
                callbacks[i] = new TextureSpriteCallback(new ResourceLocation(loc.getResourceDomain(), tex));
                TextureStitcher.register(callbacks[i]);
            }
        }

        BlockRenderLayer layerObj = layer == null ? BlockRenderLayer.SOLID : BlockRenderLayer.valueOf(layer.toUpperCase(Locale.US));
        TextureInfo textureInfo;
        if (info != null){
            textureInfo = new TextureInfo(callbacks, Optional.of(info), layerObj, fullbright);
        }
        else {
            textureInfo = new TextureInfo(callbacks, Optional.empty(), layerObj, fullbright);
        }

        try {
            return type.makeTexture(textureInfo);
        } catch (RuntimeException e) {
            throw new RuntimeException("Failed loading texture " + loc, e);
        }
    }
}
