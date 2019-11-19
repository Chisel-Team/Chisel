package team.chisel.api.carving;

import java.util.Iterator;
import java.util.List;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import net.minecraft.util.SoundEvent;

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
	@Nullable SoundEvent getSound();

	/**
	 * Sets the sound of this group
	 * 
	 * @param sound
	 *            A string resource path for the sound this group makes when chiseled
	 */
    void setSound(@Nullable SoundEvent sound);

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
}
