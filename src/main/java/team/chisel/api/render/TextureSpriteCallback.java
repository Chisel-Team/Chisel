package team.chisel.api.render;

import javax.annotation.ParametersAreNonnullByDefault;

import lombok.Getter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.ResourceLocation;

/**
 * Callback when textures are stitched
 */
@Getter
@ParametersAreNonnullByDefault
public class TextureSpriteCallback {

    private ResourceLocation location;
    private TextureAtlasSprite sprite;

    public TextureSpriteCallback(ResourceLocation loc) {
        this.location = loc;
    }

    public void stitch(TextureMap map) {
        this.sprite = map.registerSprite(location);
    }
}
