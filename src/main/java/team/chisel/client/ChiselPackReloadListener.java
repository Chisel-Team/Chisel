package team.chisel.client;

import java.util.HashSet;
import java.util.Set;

import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.IResourceManagerReloadListener;
import team.chisel.Chisel;
import team.chisel.api.block.ICarvable;
import team.chisel.common.util.json.JsonHelper;


public enum ChiselPackReloadListener implements IResourceManagerReloadListener {
    INSTANCE;
    
    private final Set<ICarvable> listeners = new HashSet<>();
    
    public void registerListener(ICarvable block) {
        this.listeners.add(block);
    }

    public void onResourceManagerReload(IResourceManager resourceManager){
        Chisel.debug("Reloading textures");
        JsonHelper.flushCaches();
        for (ICarvable block : listeners){
            block.setBlockFaceData(new BlockFaceData(block.getVariations()));
        }
    }
}
