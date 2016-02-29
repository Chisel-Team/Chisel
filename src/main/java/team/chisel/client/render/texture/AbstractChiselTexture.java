package team.chisel.client.render.texture;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

import lombok.Getter;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.util.ResourceLocation;
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
    
    @Override
    public Collection<ResourceLocation> getTextures() {
        return Arrays.stream(sprites).map(TextureSpriteCallback::getLocation).collect(Collectors.toList());
    }
}
