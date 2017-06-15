package team.chisel.common.util.json;

import com.google.common.base.Function;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.ResourceLocation;

@Deprecated
public interface JsonSupplier<T> {

    T get(ResourceLocation loc);

}
