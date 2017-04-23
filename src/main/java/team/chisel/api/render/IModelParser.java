package team.chisel.api.render;

import javax.annotation.Nonnull;

import com.google.gson.JsonObject;

import net.minecraft.util.ResourceLocation;

public interface IModelParser {

    @Nonnull
    IModelChisel fromJson(ResourceLocation res, JsonObject json);
}
