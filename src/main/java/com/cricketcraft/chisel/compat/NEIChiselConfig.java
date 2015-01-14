package com.cricketcraft.chisel.compat;

import net.minecraft.item.ItemStack;
import codechicken.nei.api.API;
import codechicken.nei.api.IConfigureNEI;

import com.cricketcraft.chisel.Chisel;

import cpw.mods.fml.common.registry.GameRegistry;

public class NEIChiselConfig implements IConfigureNEI {

	@Override
	public String getName() {
		return "Chisel NEI Plugin";
	}

	@Override
	public String getVersion() {
		return Chisel.VERSION;
	}

	@Override
	public void loadConfig() {
		API.hideItem(new ItemStack(GameRegistry.findBlock("chisel", "limestone_slab_top")));
		API.hideItem(new ItemStack(GameRegistry.findBlock("chisel", "marble_slab_top")));
		API.hideItem(new ItemStack(GameRegistry.findBlock("chisel", "marble_pillar_slab_top")));
	}
}
