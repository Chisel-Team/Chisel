package team.chisel.client.render.texture;

import com.google.common.collect.Lists;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import team.chisel.api.render.IBlockRenderContext;
import team.chisel.api.render.IBlockRenderType;
import team.chisel.api.render.IChiselTexture;

import java.util.Arrays;
import java.util.List;

/**
 * Abstract implementation of IChiselTexture
 */
public abstract class AbstractChiselTexture<CTX extends IBlockRenderContext> implements IChiselTexture<CTX> {

    protected IBlockRenderType type;

    protected TextureAtlasSprite[] sprites;

    public AbstractChiselTexture(IBlockRenderType type, TextureAtlasSprite[] sprites) {
        this.type = type;
        this.sprites = sprites;
    }


    @Override
    public boolean isCombined(){
        return false;
    }

    @Override
    public List<IBlockRenderType> getBlockRenderTypes(){
        return Arrays.asList(type);
    }
}
