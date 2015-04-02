package com.cricketcraft.chisel.compat;

import java.util.Map;

import net.minecraft.block.Block;

import com.cricketcraft.chisel.carving.Carving;
import com.google.common.collect.Maps;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;

public class Compatibility {

	public static String[] rockColorNames = { "gray", "lightgray", "brown", "tan", "reddish", "bluish", "greenish" };

    public static Map<Integer, String> tconMap = Maps.newHashMap();

    static
    {
        tconMap.put(0, "obsidian");
        tconMap.put(1, "sandstone");
        tconMap.put(2, "netherrack");
        tconMap.put(3, "stonebricksmooth");
        tconMap.put(12, "end_stone");
    }

    public static void init(FMLPostInitializationEvent event) {

		/* Proj Red */
		addSupport("ProjRed|Exploration", "projectred.exploration.stone", "marble", 0, 99);
		addSupport("ProjRed|Exploration", "projectred.exploration.stone", "marble", 1, 99);

		/* Bluepower */
		addSupport("bluepower", "marble", "marble", 0, 99);

		/* Artifice */
		addSupport("Artifice", "artifice.marble", "marble", 0, 99);
		addSupport("Artifice", "artifice.marble.slab", "marble_slab", 0, 99);
		for (String s : rockColorNames) {
			addSupport("Artifice", "artifice.limestone." + s, "limestone", 0, 99);
		}

		/* Mariculture */
		Block block = GameRegistry.findBlock("mariculture", "limestone");
		if (block != null) {
			for (int i = 0; i < 16; i++) {
				addSupport("limestone", block, i, 99);
			}
		}

		/* Geologica */
		addSupport("PFAAGeologica", "strongStone", "marble", 5, 99);
		addSupport("PFAAGeologica", "strongStoneSlab", "marbleSlab", 5, 99);
		addSupport("PFAAGeologica", "strongStoneBrickStairs.marble", "marbleStairs", 0, 99);
		addSupport("PFAAGeologica", "mediumStone", "limestone", 0, 99);
		addSupport("PFAAGeologica", "mediumStoneSlab", "limestoneSlab", 0, 99);
		addSupport("PFAAGeologica", "mediumStoneBrickStairs.limestone", "limestoneStairs", 0, 99);
		addSupport("PFAAGeologica", "strongStoneBrick", "stoneBrick", 3, 99);
		addSupport("PFAAGeologica", "strongCobble", "cobblestone", 3, 99);

		/* Thaumcraft TODO There is probably a cleaner way of doing this */
		if (Loader.isModLoaded("Thaumcraft")) {
			loadThaumcraftAspects();
        }

        for (Integer i : tconMap.keySet())
        {
            addSupport("TConstruct", "decoration.multibrick", tconMap.get(i), i, 99);
            addSupport("TConstruct", "decoration.multibrickfancy", tconMap.get(i), i, 99);
        }

        addSupport("TConstruct", "decoration.multibrickfancy", "stonebricksmooth", 14, 99);
        addSupport("TConstruct", "decoration.multibrickfancy", "stonebricksmooth", 15, 99);

        addSupport("Botania", "endStoneBrick", "end_stone", 12, 0);
    }

	public static void addSupport(String modname, String blockname, String name, int metadata, int order) {
		if (Loader.isModLoaded(modname) && GameRegistry.findBlock(modname, blockname) != null) {
			addSupport(name, GameRegistry.findBlock(modname, blockname), metadata, order);
		}
	}

	public static void addSupport(String name, Block block, int metadata, int order) {
		Carving.chisel.addVariation(name, block, metadata, order);
	}

	private static void loadThaumcraftAspects() {
//		ThaumcraftApi.registerObjectTag(new ItemStack(ChiselBlocks.cobblestone, 1, OreDictionary.WILDCARD_VALUE), (new AspectList()).add(Aspect.ENTROPY, 1).add(Aspect.EARTH, 1));
//		ThaumcraftApi.registerObjectTag(new ItemStack(ChiselBlocks.cobblestoneWall, 1, OreDictionary.WILDCARD_VALUE), (new AspectList()).add(Aspect.ENTROPY, 1).add(Aspect.EARTH, 1));
	}
}
