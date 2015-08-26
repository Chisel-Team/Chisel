package team.chisel.api.carving;

import team.chisel.api.rendering.TextureType;
import team.chisel.ctmlib.ISubmapManager;

public interface IVariationInfo extends ISubmapManager {

	ICarvingVariation getVariation();

	String getDescription();

	TextureType getType();
}
