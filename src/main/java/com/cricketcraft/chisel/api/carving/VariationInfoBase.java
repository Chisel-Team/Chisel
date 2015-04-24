package com.cricketcraft.chisel.api.carving;

import com.cricketcraft.chisel.api.client.ISubmapManager;

import net.minecraft.util.StatCollector;

public class VariationInfoBase implements IVariationInfo {

	private ICarvingVariation variation;
	private String unlocDesc;
	private ISubmapManager manager;

	public VariationInfoBase(ICarvingVariation variation, String unlocDesc, ISubmapManager manager) {
		this.variation = variation;
		this.unlocDesc = unlocDesc;
		this.manager = manager;
	}

	@Override
	public ICarvingVariation getVariation() {
		return variation;
	}

	@Override
	public String getDescription() {
		return StatCollector.translateToLocal(unlocDesc);
	}

	@Override
	public ISubmapManager getSubmapManager() {
		return manager;
	}
}
