package com.cricketcraft.chisel.api.carving;

import java.util.List;

public interface ICarvingGroup {

	/**
	 * The name of this group. Used for internal identification.
	 * 
	 * @return A string name for the group
	 */
	String getName();

	/**
	 * Can return null. If null, fallback sound will be used (the standard chisel sound).
	 * 
	 * @return The string resource path of the sound to use for chiseling items in this group
	 */
	String getSound();

	/**
	 * Sets the sound of this group
	 * 
	 * @param sound
	 *            A string resource path for the sound this group makes when chiseled
	 */
	void setSound(String sound);

	/**
	 * The oredict name to match to this group. All items with this oredict name will be assumed to be part of this group.
	 * 
	 * @return An ore dictionary name
	 */
	String getOreName();

	/**
	 * Sets the oredict name for this group.
	 * 
	 * @param oreName
	 *            The String oredict name to be associated with this group.
	 */
	void setOreName(String oreName);

	/**
	 * Gets all carving variations associated with this group.
	 * 
	 * @return A {@link List} of {@link ICarvingVariation}s
	 */
	List<ICarvingVariation> getVariations();

	/**
	 * Adds a variation to this group.
	 * 
	 * @param variation
	 *            An {@link ICarvingVariation} to add to this group
	 */
	void addVariation(ICarvingVariation variation);
}
