package com.cricketcraft.chisel.api.carving;

import net.minecraft.util.StatCollector;

import com.cricketcraft.chisel.api.rendering.TextureType;
import com.cricketcraft.ctmlib.ISubmapManager;

public class VariationInfoBase implements IVariationInfo {

	private ICarvingVariation variation;
	private String unlocDesc;
	private ISubmapManager manager;
	private TextureType type;

	public VariationInfoBase(ICarvingVariation variation, String unlocDesc, ISubmapManager manager) {
		this(variation, unlocDesc, manager, TextureType.CUSTOM);
	}

	public VariationInfoBase(ICarvingVariation variation, String unlocDesc, ISubmapManager manager, TextureType type) {
		this.variation = variation;
		this.unlocDesc = unlocDesc;
		this.manager = manager;
		this.type = type;
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

	@Override
	public TextureType getType() {
		return type;
	}
}
