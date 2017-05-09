package team.chisel.client.render.texture;

import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import lombok.Getter;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import team.chisel.api.render.IBlockRenderType;
import team.chisel.api.render.IChiselTexture;
import team.chisel.api.render.TextureInfo;
import team.chisel.api.render.TextureSpriteCallback;
import team.chisel.client.render.Quad;


/**
 * Abstract implementation of IChiselTexture
 */
public abstract class AbstractChiselTexture<T extends IBlockRenderType> implements IChiselTexture<T> {

    @Getter
    protected T type;
    @Getter
    protected BlockRenderLayer layer;

    protected TextureSpriteCallback[] sprites;

    @Deprecated
    protected boolean fullbright;

    protected boolean hasLight;
    protected int skylight, blocklight;

    @Deprecated
    public AbstractChiselTexture(T type, BlockRenderLayer layer, TextureSpriteCallback... sprites) {
        this.type = type;
        this.layer = layer;
        this.sprites = sprites;
        this.skylight = this.blocklight = 0;
    }

    public AbstractChiselTexture(T type, TextureInfo info){
        this.type = type;
        this.layer = info.getRenderLayer();
        this.sprites = info.getSprites();
        this.fullbright = info.getFullbright();
        if (info.getInfo().isPresent()) {
            JsonElement light = info.getInfo().get().get("light");
            if (light != null) {
                if (light.isJsonPrimitive()) {
                    this.hasLight = true;
                    this.skylight = this.blocklight = parseLightValue(light);
                } else if (light.isJsonObject()) {
                    this.hasLight = true;
                    JsonObject lightObj = light.getAsJsonObject();
                    this.blocklight = parseLightValue(lightObj.get("block"));
                    this.skylight = parseLightValue(lightObj.get("sky"));
                }
            }
        }
    }
    
    private final int parseLightValue(JsonElement data) {
        if (data != null && data.isJsonPrimitive() && data.getAsJsonPrimitive().isNumber()) {
            return MathHelper.clamp(data.getAsInt(), 0, 15);
        }
        return 0;
    }

    @Override
    public TextureAtlasSprite getParticle(){
        return sprites[0].getSprite();
    }
    
    @Override
    public Collection<ResourceLocation> getTextures() {
        return Arrays.stream(sprites).map(TextureSpriteCallback::getLocation).collect(Collectors.toList());
    }
    
    protected Quad makeQuad(BakedQuad q) {
        return Quad.from(q).setLight(blocklight, skylight);
    }
}
