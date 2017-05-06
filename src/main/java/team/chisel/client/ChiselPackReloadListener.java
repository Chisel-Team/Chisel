package team.chisel.client;

import javax.annotation.Nonnull;

import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.IResourceManagerReloadListener;
import team.chisel.client.render.AbstractChiselBakedModel;
import team.chisel.client.render.ModelLoaderChisel;
import team.chisel.common.util.json.JsonHelper;


public enum ChiselPackReloadListener implements IResourceManagerReloadListener {
    INSTANCE;
    
    public void onResourceManagerReload(@Nonnull IResourceManager resourceManager){
        JsonHelper.flushCaches();
        AbstractChiselBakedModel.invalidateCaches();
        ClientUtil.invalidateCaches();
        ModelLoaderChisel.parsedLocations.clear();
    }
}
