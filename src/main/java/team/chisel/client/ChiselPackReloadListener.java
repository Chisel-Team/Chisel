package team.chisel.client;

import java.io.IOException;
import java.util.Map;

import lombok.SneakyThrows;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.IResourceManagerReloadListener;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import team.chisel.client.render.ModelChiselBlockOld;
import team.chisel.client.render.texture.MetadataSectionChisel;
import team.chisel.common.util.json.JsonHelper;


public enum ChiselPackReloadListener implements IResourceManagerReloadListener {
    INSTANCE;
    
    public void onResourceManagerReload(IResourceManager resourceManager){
        JsonHelper.flushCaches();
        ModelChiselBlockOld.invalidateCaches();
    }
}
