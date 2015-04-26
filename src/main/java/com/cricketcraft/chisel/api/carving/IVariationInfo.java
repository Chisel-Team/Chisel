package com.cricketcraft.chisel.api.carving;

import net.minecraft.client.renderer.RenderBlocks;

import com.cricketcraft.chisel.api.rendering.ISubmapManager;
import com.cricketcraft.chisel.api.rendering.TextureType;

public interface IVariationInfo {

	ISubmapManager<? extends RenderBlocks> getSubmapManager();

	ICarvingVariation getVariation();

	String getDescription();
	
	TextureType getType();
}
