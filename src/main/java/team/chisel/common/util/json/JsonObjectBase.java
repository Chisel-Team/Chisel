package team.chisel.common.util.json;

import com.google.common.base.Function;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.ResourceLocation;

@Deprecated
public abstract class JsonObjectBase<T> implements JsonSupplier<T> {

    private T cache;

    @Override
    public final T get(ResourceLocation loc) {
        return cache != null ? cache : (cache = create(loc));
    }

    protected abstract T create(ResourceLocation loc);
}
