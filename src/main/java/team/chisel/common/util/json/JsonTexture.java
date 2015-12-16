package team.chisel.common.util.json;

import java.util.Locale;

import javax.annotation.Nullable;

import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.util.ResourceLocation;
import team.chisel.api.render.IBlockRenderType;
import team.chisel.api.render.IChiselTexture;
import team.chisel.api.render.TextureSpriteCallback;
import team.chisel.client.TextureStitcher;
import team.chisel.common.init.TextureTypeRegistry;

import com.google.common.base.Preconditions;


/**
 * Raw version of IChiselTexture
 */
public class JsonTexture extends JsonObjectBase<IChiselTexture> {

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

    @Override
    protected IChiselTexture create(ResourceLocation loc) {
        Preconditions.checkArgument(TextureTypeRegistry.isValid(this.type), "Texture Type " + this.type + " is not valid");

        IBlockRenderType type = TextureTypeRegistry.getType(this.type);
        Preconditions.checkArgument(textures != null || type.requiredTextures() == 1, "Texture type %s requires %d textures, the texture name can only be inferred for textures that require 1.",
                this.type, type.requiredTextures());
        if (textures != null) {
            Preconditions
                    .checkArgument(textures.length == type.requiredTextures(), "Texture type %s requires %d textures. Only %d were provided.", this.type, type.requiredTextures(), textures.length);
        }

        TextureSpriteCallback[] callbacks = new TextureSpriteCallback[type.requiredTextures()];
        if (textures == null) {
            callbacks[0] = new TextureSpriteCallback(new ResourceLocation(loc.getResourceDomain(), loc.getResourcePath().replace("textures/", "").replace(".json", "")));
            TextureStitcher.register(callbacks[0]);
        } else {
            for (int i = 0; i < this.textures.length; i++) {
                String tex = this.textures[i];
                callbacks[i] = new TextureSpriteCallback(new ResourceLocation(tex));
                TextureStitcher.register(callbacks[i]);
            }
        }
        
        EnumWorldBlockLayer layerObj = layer == null ? EnumWorldBlockLayer.SOLID : EnumWorldBlockLayer.valueOf(layer.toUpperCase(Locale.US));
        return type.makeTexture(layerObj, callbacks);
    }
}
