package team.chisel.client.render.texture;

import com.google.common.collect.Lists;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import team.chisel.api.render.IBlockRenderContext;
import team.chisel.api.render.IBlockRenderType;
import team.chisel.api.render.IChiselTexture;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Abstract implementation of IChiselTexture
 */
public abstract class AbstractChiselTexture implements IChiselTexture {

    protected IBlockRenderType type;

    protected TextureAtlasSprite[] sprites;

    public AbstractChiselTexture(IBlockRenderType type, TextureAtlasSprite[] sprites) {
        this.type = type;
        this.sprites = sprites;
    }


    @Override
    public IBlockRenderType getBlockRenderType(){
        return this.type;
    }

    @Override
    public TextureAtlasSprite getParticle(){
        return sprites[0];
    }
}
