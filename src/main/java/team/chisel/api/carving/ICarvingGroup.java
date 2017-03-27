package team.chisel.api.carving;

import java.util.Iterator;
import java.util.List;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

/**
 * Represents a group of chiselable blocks.
 * <p>
 * This is an object that contains a collection of {@link ICarvingVariation} objects, and keeps them sorted. You may sort them in any way (or not at all), and {@link ICarvingVariation#getOrder()} will
 * likely be of use to do so.
 * <p>
 * It also defines what sound and oredict name the group as a whole has.
 */
@ParametersAreNonnullByDefault
public interface ICarvingGroup extends Iterable<ICarvingVariation> {

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
	@Nullable String getSound();

	/**
	 * Sets the sound of this group
	 * 
	 * @param sound
	 *            A string resource path for the sound this group makes when chiseled
	 */
	void setSound(@Nullable String sound);

	/**
	 * The oredict name to match to this group. All items with this oredict name will be assumed to be part of this group.
	 * 
	 * @return An ore dictionary name
	 */
	@Nullable String getOreName();

	/**
	 * Sets the oredict name for this group.
	 * 
	 * @param oreName
	 *            The String oredict name to be associated with this group.
	 */
	void setOreName(@Nullable String oreName);

	/**
	 * Gets all carving variations associated with this group.
	 * 
	 * @return A {@link List} of {@link ICarvingVariation}s
	 */
	List<ICarvingVariation> getVariations();
	
	@SuppressWarnings("null")
    default Iterator<ICarvingVariation> iterator() {
	    return getVariations().iterator();
	}

	/**
	 * Adds a variation to this group. Do not call this from external code, as it will fail to remove the inverse lookup from the registry.
	 * 
	 * @param variation
	 *            An {@link ICarvingVariation} to add to this group
	 */
	@Deprecated
	void addVariation(ICarvingVariation variation);

	/**
	 * Removes a variation to this group. Do not call this from external code, as it will fail to remove the inverse lookup from the registry.
	 * 
	 * @param variation
	 *            An {@link ICarvingVariation} to add to this group
	 */
	@Deprecated
	boolean removeVariation(ICarvingVariation variation);
}
