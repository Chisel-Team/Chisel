package team.chisel.common;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class CommonProxy implements Reference {

    public void construct(FMLPreInitializationEvent event) {
    }

    public void preInit(FMLPreInitializationEvent event) {
    }

    public void init() {
    }

    public void postInit() {        
    }

    public void preTextureStitch() {
    }

    public void registerTileEntities() {
    }

    public World getClientWorld() {
        return null;
    }
    
    public EntityPlayer getClientPlayer() {
        return null;
    }
}
