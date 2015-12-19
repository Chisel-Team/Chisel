package team.chisel.client;

import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.IResourceManagerReloadListener;
import team.chisel.Chisel;
import team.chisel.common.block.BlockCarvable;
import team.chisel.common.init.BlockRegistry;
import team.chisel.common.util.json.JsonHelper;


public class ChiselPackReloadListener implements IResourceManagerReloadListener {

    public void onResourceManagerReload(IResourceManager resourceManager){
        Chisel.debug("Reloading textures");
        JsonHelper.flushCaches();
        for (BlockCarvable block : BlockRegistry.getAllBlocks()){
            block.setBlockFaceData(new BlockFaceData(block.getBlockData().variations));
        }
    }
}
