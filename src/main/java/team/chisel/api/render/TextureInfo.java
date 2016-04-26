package team.chisel.api.render;

import com.google.gson.JsonObject;
import net.minecraft.util.BlockRenderLayer;

import java.util.Optional;

/**
 * Bean to hold information that the IBlockRenderType should use to make an IChiselTexture
 */
public class TextureInfo {

    private TextureSpriteCallback[] sprites;

    private Optional<JsonObject> info;

    private BlockRenderLayer renderLayer;

    private boolean fullbright;

    public TextureInfo(TextureSpriteCallback[] sprites, Optional<JsonObject> info, BlockRenderLayer layer, boolean fullbright){
        this.sprites = sprites;
        this.info = info;
        this.renderLayer = layer;
        this.fullbright = fullbright;
    }

    /**
     * Gets the sprites to use for this texture
     */
    public TextureSpriteCallback[] getSprites(){
        return this.sprites;
    }

    /**
     * Gets a JsonObject that had the key "info" for extra texture information
     * This JsonObject might not exist
     */
    public Optional<JsonObject> getInfo()
    {
        return this.info;
    }

    /**
     * Returns the render layer for this texture
     */
    public BlockRenderLayer getRenderLayer(){
        return this.renderLayer;
    }

    /**
     * Get whether this block should be rendered in fullbright
     */
    public boolean getFullbright(){
        return this.fullbright;
    }
}
