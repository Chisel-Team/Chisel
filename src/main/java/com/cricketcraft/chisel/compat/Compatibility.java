package com.cricketcraft.chisel.compat;

import com.cricketcraft.chisel.carving.Carving;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;

public class Compatibility {

	public static void init(FMLPostInitializationEvent event) {
		new VariationCompat().postInit(event);

		addSupport("ProjRed|Exploration", "projectred.exploration.stone", "marble", 0, 99);
		addSupport("bluepower", "marble", "marble", 0, 99);

		addSupport("PFAAGeologica", "strongStone", "marble", 5, 99);
		addSupport("PFAAGeologica", "strongStoneSlab", "marbleSlab", 5, 99);
		addSupport("PFAAGeologica", "strongStoneBrickStairs.marble", "marbleStairs", 0, 99);
		addSupport("PFAAGeologica", "mediumStone", "limestone", 0, 99);
		addSupport("PFAAGeologica", "mediumStoneSlab", "limestoneSlab", 0, 99);
		addSupport("PFAAGeologica", "mediumStoneBrickStairs.limestone", "limestoneStairs", 0, 99);
		addSupport("PFAAGeologica", "strongStoneBrick", "stoneBrick", 3, 99);
		addSupport("PFAAGeologica", "strongCobble", "cobblestone", 3, 99);
	}

	public static void addSupport(String modname, String blockname, String name, int metadata, int order) {
		if (Loader.isModLoaded(modname) && GameRegistry.findBlock(modname, blockname) != null) {
			Carving.chisel.addVariation(name, GameRegistry.findBlock(modname, blockname), metadata, order);
		}
	}
}
