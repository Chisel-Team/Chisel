package com.cricketcraft.chisel.api.carving;

import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.util.StatCollector;

import com.cricketcraft.chisel.api.rendering.ISubmapManager;
import com.cricketcraft.chisel.api.rendering.TextureType;

public class VariationInfoBase implements IVariationInfo {

	private ICarvingVariation variation;
	private String unlocDesc;
	private ISubmapManager<? extends RenderBlocks> manager;
	private TextureType type;

	public VariationInfoBase(ICarvingVariation variation, String unlocDesc, ISubmapManager<? extends RenderBlocks> manager) {
		this(variation, unlocDesc, manager, TextureType.CUSTOM);
	}

	public VariationInfoBase(ICarvingVariation variation, String unlocDesc, ISubmapManager<? extends RenderBlocks> manager, TextureType type) {
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
	public ISubmapManager<? extends RenderBlocks> getSubmapManager() {
		return manager;
	}

	@Override
	public TextureType getType() {
		return type;
	}
}
