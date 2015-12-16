package team.chisel.common.util.json;

import net.minecraft.util.ResourceLocation;

public interface JsonSupplier<T> {

    T get(ResourceLocation loc);

}
