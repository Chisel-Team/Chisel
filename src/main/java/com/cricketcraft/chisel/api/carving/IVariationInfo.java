package com.cricketcraft.chisel.api.carving;

import com.cricketcraft.chisel.api.rendering.ISubmapManager;
import com.cricketcraft.chisel.api.rendering.TextureType;

public interface IVariationInfo {

	ISubmapManager getSubmapManager();

	ICarvingVariation getVariation();

	String getDescription();
	
	TextureType getType();
}
