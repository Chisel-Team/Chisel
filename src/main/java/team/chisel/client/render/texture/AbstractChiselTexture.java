package team.chisel.client.render.texture;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.EnumWorldBlockLayer;
import team.chisel.api.render.IBlockRenderType;
import team.chisel.api.render.IChiselTexture;
import team.chisel.api.render.TextureSpriteCallback;


/**
 * Abstract implementation of IChiselTexture
 */
public abstract class AbstractChiselTexture implements IChiselTexture {

    protected IBlockRenderType type;
    protected EnumWorldBlockLayer layer;

    protected TextureSpriteCallback[] sprites;

    public AbstractChiselTexture(IBlockRenderType type, EnumWorldBlockLayer layer, TextureSpriteCallback... sprites) {
        this.type = type;
        this.layer = layer;
        this.sprites = sprites;
    }

    @Override
    public IBlockRenderType getBlockRenderType(){
        return this.type;
    }

    @Override
    public TextureAtlasSprite getParticle(){
        return sprites[0].getSprite();
    }
    
    @Override
    public EnumWorldBlockLayer getLayer() {
        return layer;
    }
}
