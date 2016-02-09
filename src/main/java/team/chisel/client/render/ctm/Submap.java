package team.chisel.client.render.ctm;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Submap {
    
    private int width, height;
    private int xOffset, yOffset;

    public float getInterpolatedU(TextureAtlasSprite sprite, float u) {
        return sprite.getInterpolatedU(xOffset + (u / width));
    }

    public float getInterpolatedV(TextureAtlasSprite sprite, float v) {
        return sprite.getInterpolatedV(yOffset + (v / width));
    }

    public float[] toArray() {
        return new float[] { xOffset, yOffset, xOffset + width, yOffset + height };
    }
}
