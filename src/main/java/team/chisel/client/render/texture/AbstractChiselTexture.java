package team.chisel.client.render.texture;

import lombok.Getter;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.EnumWorldBlockLayer;
import team.chisel.api.render.IBlockRenderType;
import team.chisel.api.render.IChiselTexture;
import team.chisel.api.render.TextureSpriteCallback;


/**
 * Abstract implementation of IChiselTexture
 */
public abstract class AbstractChiselTexture<T extends IBlockRenderType> implements IChiselTexture<T> {

    @Getter
    protected T type;
    @Getter
    protected EnumWorldBlockLayer layer;

    protected TextureSpriteCallback[] sprites;

    public AbstractChiselTexture(T type, EnumWorldBlockLayer layer, TextureSpriteCallback... sprites) {
        this.type = type;
        this.layer = layer;
        this.sprites = sprites;
    }

    @Override
    public TextureAtlasSprite getParticle(){
        return sprites[0].getSprite();
    }
}
