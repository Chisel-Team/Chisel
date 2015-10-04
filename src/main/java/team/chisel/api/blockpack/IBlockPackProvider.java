package team.chisel.api.blockpack;

import cpw.mods.fml.common.event.FMLPreInitializationEvent;

import java.util.List;

/**
 * Allows Block packs to be provided dynamically. For example if you want to load a block pack from a file
 */
public interface IBlockPackProvider {

    List<IProvidedBlockPack> getProvidedPacks(FMLPreInitializationEvent event);
}
