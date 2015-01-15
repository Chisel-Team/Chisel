package com.cricketcraft.chisel.block;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;

import com.cricketcraft.chisel.carving.CarvableHelper;
import com.cricketcraft.chisel.carving.CarvableVariation;
import com.cricketcraft.chisel.item.ItemCarvable;

import cpw.mods.fml.common.registry.GameRegistry;

public class CarvableStairsMaker {

	public CarvableHelper carverHelper;
	// int idStart;
	Block blockBase;
	String blockName;

	public CarvableStairsMaker(Block base) {
		carverHelper = new CarvableHelper();
		blockBase = base;
	}

	public void create(String name, Block[] blocks) {
		create(null, name, blocks);
	}

	public void create(IStairsCreator creator, String name, Block[] blocks) {
		for (int i = 0; i < blocks.length; i++) {
			String n = name + "." + i;
			blocks[i] = creator == null ? new BlockCarvableStairs(blockBase, i * 2, carverHelper) : creator.create(blockBase, i * 2, carverHelper);

			blocks[i].setBlockName(n);
			GameRegistry.registerBlock(blocks[i], ItemCarvable.class, n);

			for (int meta = 0; meta < 2 && i * 2 + meta < carverHelper.variations.size(); meta++) {
				CarvableVariation variation = carverHelper.variations.get(i * 2 + meta);

				carverHelper.registerVariation(name, variation, blocks[i], meta * 8);

				GameRegistry.addRecipe(new ItemStack(blocks[i], 4, meta * 8), "*  ", "** ", "***", '*', new ItemStack(blockBase, 1, i * 2 + meta));
			}
		}
	}
}
