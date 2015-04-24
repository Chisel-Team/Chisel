package com.cricketcraft.chisel.api.carving;

import com.cricketcraft.chisel.api.client.ISubmapManager;

public interface IVariationInfo {

	ISubmapManager getSubmapManager();

	ICarvingVariation getVariation();

	String getDescription();
}
