package com.cricketcraft.chisel.compat;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;

import com.cricketcraft.chisel.compat.ModIntegration.IModIntegration;
import com.cricketcraft.chisel.init.ChiselBlocks;

import cpw.mods.fml.common.event.FMLInterModComms;

public class FMPIntegration implements IModIntegration {
	
	@Override
	public String getModId() {
		return "ForgeMicroblock";
	}

	@Override
	public void onInit() {
		registerMaterial(ChiselBlocks.marble);
		registerMaterial(ChiselBlocks.limestone);
		registerMaterial(ChiselBlocks.cobblestone, 1, 15);
		registerMaterial(ChiselBlocks.glass, 1, 15);
		registerMaterial(ChiselBlocks.sandstone, 3, 15);
		registerMaterial(ChiselBlocks.sand_snakestone, 1, 1);
		registerMaterial(ChiselBlocks.sand_snakestone, 13, 13);
		registerMaterial(ChiselBlocks.sandstone_scribbles);
		registerMaterial(ChiselBlocks.concrete, 0, 10);
		registerMaterial(ChiselBlocks.iron_block, 1, 15);
		registerMaterial(ChiselBlocks.gold_block, 1, 14);
		registerMaterial(ChiselBlocks.diamond_block, 1, 12);
		registerMaterial(ChiselBlocks.glowstone, 1, 15); // This is glowstone
		registerMaterial(ChiselBlocks.lapis_block, 1, 8);
		registerMaterial(ChiselBlocks.emerald_block, 1, 11);
		registerMaterial(ChiselBlocks.nether_brick, 1, 5);
		registerMaterial(ChiselBlocks.netherrack, 1, 14);
		registerMaterial(ChiselBlocks.mossy_cobblestone, 1, 15);
		registerMaterial(ChiselBlocks.stonebricksmooth, 4, 15);
		registerMaterial(ChiselBlocks.stone_snakestone, 1, 1);
		registerMaterial(ChiselBlocks.stone_snakestone, 13, 13);
		registerMaterial(ChiselBlocks.dirt, 0, 3);
		registerMaterial(ChiselBlocks.dirt, 6, 7);
		registerMaterial(ChiselBlocks.dirt, 11, 11);
		registerMaterial(ChiselBlocks.ice, 1, 15);
		registerMaterial(ChiselBlocks.ice_pillar);
		for (int x = 0; x < 4; x++) {
			registerMaterial(ChiselBlocks.planks[x], 1, 15); // Oak, Spruce, Birch
															// and Jungle
		}
		// Accacia
		registerMaterial(ChiselBlocks.planks[4], 1, 1);
		registerMaterial(ChiselBlocks.planks[4], 2, 2);
		registerMaterial(ChiselBlocks.planks[4], 6, 6);
		registerMaterial(ChiselBlocks.planks[4], 8, 11);
		registerMaterial(ChiselBlocks.planks[4], 13, 13);

		// Dark Oak
		registerMaterial(ChiselBlocks.planks[5], 1, 1);
		registerMaterial(ChiselBlocks.planks[5], 2, 2);
		registerMaterial(ChiselBlocks.planks[5], 6, 6);
		registerMaterial(ChiselBlocks.planks[5], 8, 11);
		registerMaterial(ChiselBlocks.planks[5], 13, 13);

		registerMaterial(ChiselBlocks.obsidian, 1, 15);
		registerMaterial(ChiselBlocks.obsidian_snakestone, 1, 1);
		registerMaterial(ChiselBlocks.obsidian_snakestone, 13, 13);
		registerMaterial(ChiselBlocks.redstone_block, 1, 15);
		registerMaterial(ChiselBlocks.holystone, 0, 13);
		registerMaterial(ChiselBlocks.carpet_block);
		registerMaterial(ChiselBlocks.lavastone, 0, 6);
		registerMaterial(ChiselBlocks.fantasyblock, 0, 1);
		registerMaterial(ChiselBlocks.fantasyblock, 3, 14);
		registerMaterial(ChiselBlocks.tyrian);
		registerMaterial(ChiselBlocks.templeblock, 1, 2);
		registerMaterial(ChiselBlocks.templeblock, 4, 15);
		registerMaterial(ChiselBlocks.mossy_templeblock, 1, 2);
		registerMaterial(ChiselBlocks.mossy_templeblock, 4, 15);
		registerMaterial(ChiselBlocks.factoryblock, 0, 12);
		registerMaterial(ChiselBlocks.factoryblock, 14, 14);
		registerMaterial(ChiselBlocks.stainedGlass[0]); // White
		registerMaterial(ChiselBlocks.woolen_clay);
		registerMaterial(ChiselBlocks.laboratoryblock, 2, 3);
		registerMaterial(ChiselBlocks.laboratoryblock, 5, 12);
		registerMaterial(ChiselBlocks.voidstone, 0, 3);
		registerMaterial(ChiselBlocks.voidstone, 12, 15);
		registerMaterial(ChiselBlocks.waterstone);
	}

	@Override
	public void onPostInit() {

	}

	private void registerMaterial(Block block) {
		registerMaterial(block, 0, 15);
	}

	private void registerMaterial(Block block, int minMeta, int maxMeta) {
		for (int c = minMeta; c <= maxMeta; c++) {
			FMLInterModComms.sendMessage(getModId(), "microMaterial", new ItemStack(block, 1, c));
		}
	}
}
