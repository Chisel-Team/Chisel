package com.cricketcraft.chisel.compat;

import java.util.List;

import net.minecraft.block.Block;

import org.apache.commons.lang3.tuple.Triple;

import com.cricketcraft.chisel.carving.Carving;
import com.google.common.collect.Lists;

import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;

public class VariationCompat {

	public static String[] rockColorNames = { "gray", "lightgray", "brown", "tan", "reddish", "bluish", "greenish" };

	public void postInit(FMLPostInitializationEvent event) {
		// block, metadata, category
		List<Triple<Block, Integer, String>> blocks = Lists.newArrayList();

		/* Artifice */
		blocks.add(Triple.of(find("Artifice", "artifice.marble"), 0, "marble"));
		blocks.add(Triple.of(find("Artifice", "artifice.marble.slab"), 0, "marble_slab"));
		for (String s : rockColorNames) {
			blocks.add(Triple.of(find("Artifice", "artifice.limestone." + s), 0, "limestone"));
		}

		/* BluePower */
		blocks.add(Triple.of(find("bluepower", "marble"), 0, "marble"));

		/* Mariculture */
		Block block = find("mariculture", "limestone");
		for (int i = 0; i < 16; i++) {
			blocks.add(Triple.of(block, i, "limestone"));
		}

		// TODO geostrata

		registerAll(blocks);
	}

	private void registerAll(List<Triple<Block, Integer, String>> blocks) {
		for (Triple<Block, Integer, String> t : blocks) {
			Carving.chisel.addVariation(t.getRight(), t.getLeft(), t.getMiddle(), 99);
		}
	}

	private Block find(String modid, String block) {
		return GameRegistry.findBlock(modid, block);
	}
}
