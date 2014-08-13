package info.jbcs.minecraft.chisel;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import info.jbcs.minecraft.chisel.carving.Carving;

/**
 * Created by Pokefenn.
 * Licensed under MIT (If this is one of my Mods)
 */
public class Compatibility
{

    public static void init(FMLPostInitializationEvent event)
    {
        new ChiselModCompatibility().postInit(event);

        addSupport("ProjRed|Exploration", "projectred.exploration.stone", "marble", 0, 99);
        addSupport("bluepower", "marble", "marble", 0, 99);
    }

    public static void addSupport(String modname, String blockname, String name, int metadata, int order)
    {
        if(Loader.isModLoaded(modname) && GameRegistry.findBlock(modname, blockname) != null)
        {
            Carving.chisel.addVariation(name, GameRegistry.findBlock(modname, blockname), metadata, order);
            GameRegistry.findBlock(modname, blockname).setHarvestLevel("chisel", 0, 0);
        }
    }
}
