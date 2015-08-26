package com.cricketcraft.chisel.api.carving;

import team.chisel.ctmlib.ISubmapManager;

import com.cricketcraft.chisel.api.rendering.TextureType;

public interface IVariationInfo extends ISubmapManager {

	ICarvingVariation getVariation();

	String getDescription();

	TextureType getType();
}
