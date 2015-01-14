package com.cricketcraft.chisel.compat.nei;

import com.cricketcraft.chisel.Chisel;

import codechicken.nei.api.API;
import codechicken.nei.api.IConfigureNEI;

public class NEIChiselConfig implements IConfigureNEI {

	@Override
	public String getName() {
		return Chisel.MOD_NAME;
	}

	@Override
	public String getVersion() {
		return Chisel.VERSION;
	}

	@Override
	public void loadConfig() {
		RecipeHandlerChisel handler = new RecipeHandlerChisel();
		API.registerRecipeHandler(handler);
		API.registerUsageHandler(handler);
	}

}
