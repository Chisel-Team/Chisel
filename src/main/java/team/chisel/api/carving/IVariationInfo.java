package team.chisel.api.carving;

import team.chisel.api.rendering.TextureType;
import team.chisel.ctmlib.ISubmapManager;

public interface IVariationInfo extends ISubmapManager {

	ICarvingVariation getVariation();

	String getDescription();

	TextureType getType();

	/**
	 * Gets the wrapped {@link ISubmapManager}. If this object does not wrap a submap manager, it is valid to return itself.
	 * 
	 * @return The wrapped {@link ISubmapManager}, or itself if it does not wrap one.
	 */
	ISubmapManager getManager();
}
