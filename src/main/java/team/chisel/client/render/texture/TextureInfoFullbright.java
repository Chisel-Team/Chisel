package team.chisel.client.render.texture;

import java.util.Optional;

import javax.annotation.ParametersAreNonnullByDefault;

import com.google.gson.JsonObject;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.BlockRenderLayer;
import team.chisel.ctm.api.util.TextureInfo;

@Deprecated
@ParametersAreNonnullByDefault
public class TextureInfoFullbright extends TextureInfo {

    private final boolean fullbright;
    
    public TextureInfoFullbright(TextureAtlasSprite[] sprites, Optional<JsonObject> info, BlockRenderLayer layer, boolean fullbright) {
        super(sprites, info, layer);
        this.fullbright = fullbright;
    }
    
    @Override
    public boolean getFullbright() {
        return fullbright;
    }
}
