package team.chisel.api.render;

import javax.annotation.ParametersAreNonnullByDefault;

import lombok.Getter;
import lombok.ToString;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.ResourceLocation;

/**
 * Callback when textures are stitched
 */
@Getter
@ParametersAreNonnullByDefault
@ToString
@Deprecated
public class TextureSpriteCallback {

    private ResourceLocation location;
    private TextureAtlasSprite sprite;

    public TextureSpriteCallback(ResourceLocation loc) {
        this.location = loc;
        this.sprite = Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(loc.toString());
    }

    public TextureSpriteCallback(TextureAtlasSprite sprite) {
        this(new ResourceLocation(sprite.getIconName()));
        this.sprite = sprite;
    }

    public void stitch(TextureMap map) {
        this.sprite = map.registerSprite(location);
    }
}
