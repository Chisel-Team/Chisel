package team.chisel.client.render.texture;

import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

import lombok.Getter;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.ResourceLocation;
import team.chisel.api.render.IBlockRenderType;
import team.chisel.api.render.IChiselTexture;
import team.chisel.api.render.TextureInfo;
import team.chisel.api.render.TextureSpriteCallback;

import com.google.gson.JsonObject;


/**
 * Abstract implementation of IChiselTexture
 */
public abstract class AbstractChiselTexture<T extends IBlockRenderType> implements IChiselTexture<T> {

    @Getter
    protected T type;
    @Getter
    protected BlockRenderLayer layer;

    protected TextureSpriteCallback[] sprites;

    protected boolean fullbright;

    protected Optional<JsonObject> info;

    public AbstractChiselTexture(T type, BlockRenderLayer layer, TextureSpriteCallback... sprites) {
        this.type = type;
        this.layer = layer;
        this.sprites = sprites;
    }

    public AbstractChiselTexture(T type, TextureInfo info){
        this.type = type;
        this.layer = info.getRenderLayer();
        this.sprites = info.getSprites();
        this.fullbright = info.getFullbright();
        this.info = info.getInfo();
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
