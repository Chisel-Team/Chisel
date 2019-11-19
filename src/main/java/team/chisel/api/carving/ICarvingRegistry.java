package team.chisel.api.carving;

import java.util.List;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;

/**
 * Represents a registry of {@link ICarvingGroup}s
 * <p>
 * To obtain chisel's instance of this class, use {@link CarvingUtils#getChiselRegistry()}
 */
@ParametersAreNonnullByDefault
public interface ICarvingRegistry {

    /* Getters */

    /**
     * Finds the group the block/meta pair belongs to in the registry.
     * 
     * @param state
     *            The state of the variation
     * @return The {@link ICarvingGroup} that the block/meta pair belongs to
     */
    @Nullable
    ICarvingGroup getGroup(IBlockState state);

    /**
     * Finds the group the ItemStack belongs to in the registry.
     * 
     * @param stack
     *            The ItemStack of the variation
     * @return The {@link ICarvingGroup} that the ItemStack pair belongs to
     */
    @Nullable
    ICarvingGroup getGroup(ItemStack stack);

    /**
     * Gets an {@link ICarvingGroup} by its name.
     * 
     * @param name
     *            The name of the group
     * @return An {@link ICarvingGroup}
     */
    @Nullable
    ICarvingGroup getGroup(String name);

    /**
     * Gets the {@link ICarvingVariation} instance represented by this block/meta pair.
     * 
     * @param state
     *            The state of the variation
     * @return The {@link ICarvingVariation} containing this block/meta pair
     */
    @Nullable
    ICarvingVariation getVariation(IBlockState state);

    /**
     * Gets the {@link ICarvingVariation} instance represented by this stack.
     * 
     * @param stack
     *            The ItemStack of the variation
     * 
     * @return The {@link ICarvingVariation} containing this stack
     */
    @Nullable
    ICarvingVariation getVariation(ItemStack stack);

    /**
     * Gets the list of {@link ICarvingVariation}s from the group that contains this block/meta pair.
     * 
     * @param state
     *            The state of the variation
     * @return All of the {@link ICarvingVariation}s in the group that contains this block/meta pair
     */
    List<ICarvingVariation> getGroupVariations(IBlockState state);

    /**
     * Gets the possible output items for this {@link ItemStack}. To be used for machines/GUIs that chisel items.
     * 
     * @param chiseled
     *            The {@link ItemStack} being chiseled
     * @return A list of stacks that can be chiseled from the passed {@link ItemStack stack}
     */
    List<ItemStack> getItemsForChiseling(ItemStack chiseled);

    /**
     * A convenience version of {@link ICarvingRegistry#getItemsForChiseling(ItemStack)} which takes an already computed group.
     * 
     * @see ICarvingRegistry#getItemsForChiseling(ItemStack)
     */
    List<ItemStack> getItemsForChiseling(ICarvingGroup group);

    /**
     * Gets the sound resource string for the group represented by this block/meta pair.
     * 
     * @param state
     *            The state of the variation
     * @return The string resource for the sound that can be used in {@link World#playSound(double, double, double, String, float, float, boolean)} and other methods.
     */
    public SoundEvent getVariationSound(IBlockState state);

    /**
     * Gets the sound resource string for the group represented by this ItemStack.
     * 
     * @param stack
     *            The ItemStack of the variation
     * 
     * @return The string resource for the sound that can be used in {@link World#playSound(double, double, double, String, float, float, boolean)} and other methods.
     */
    public SoundEvent getVariationSound(ItemStack stack);

    /**
     * Gets the sound resource string for the group represented by this {@link ICarvingVariation}.
     * 
     * @param variation
     * 
     * @return The string resource for the sound that can be used in {@link World#playSound(double, double, double, String, float, float, boolean)} and other methods.
     */
    SoundEvent getVariationSound(@Nullable ICarvingVariation variation);

    /**
     * @return A list of all registered group names, sorted alphabetically.
     */
    List<String> getSortedGroupNames();

    /* Setters */

    /**
     * Adds a variation to the registry.
     * 
     * @param groupName
     *            The name of the group to add to
     * @param variation
     *            The {@link ICarvingVariation} to add
     */
    void addVariation(String groupName, ICarvingVariation variation);

    /**
     * Adds a group to the registry.
     * 
     * @param group
     *            The {@link ICarvingGroup} to add.
     */
    void addGroup(ICarvingGroup group);

    /**
     * Removes a group from the registry.
     * <p>
     * This in effect removes all variations associated with the group, though they are not explicitly removed from the object. If you maintain a reference to the {@link ICarvingGroup} that is
     * removed, it will still contain its variations.
     * 
     * @param groupName
     *            The name of the group to remove.
     * @return The {@link ICarvingGroup} that was removed.
     */
    @Nullable
    ICarvingGroup removeGroup(String groupName);

    /**
     * Removes a variation with the passed {@link Block} and metadata from the registry. If this variation is registered with multiple groups, it will remove it from all of them.
     * 
     * @param state
     *            The {@link IBlockState} of the {@link ICarvingVariation variation}
     * @return The ICarvingVariation that was removed. Null if nothing was removed.
     */
    @Nullable
    ICarvingVariation removeVariation(IBlockState state);

    /**
     * Removes a variation with the passed {@link Block} and metadata from the registry, but only from the specified {@link ICarvingGroup} name.
     * 
     * @param state
     *            The {@link IBlockState} of the {@link ICarvingVariation variation}
     * @param group
     *            The name of the group that the variation should be removed from
     * @return The ICarvingVariation that was removed. Null if nothing was removed.
     */
    @Nullable
    ICarvingVariation removeVariation(IBlockState state, String group);
    
    /**
     * Removes a variation with the passed ItemStack from the registry. If this variation is registered with multiple groups, it will remove it from all of them.
     * 
     * @param stack
     *            The {@link ItemStack} of the {@link ICarvingVariation variation}
     * @return The ICarvingVariation that was removed. Null if nothing was removed.
     */
    @Nullable
    ICarvingVariation removeVariation(ItemStack stack);

    /**
     * Removes a variation with the passed ItemStack from the registry, but only from the specified {@link ICarvingGroup} name.
     * 
     * @param stack
     *            The {@link ItemStack} of the {@link ICarvingVariation variation}
     * @param group
     *            The name of the group that the variation should be removed from
     * @return The ICarvingVariation that was removed. Null if nothing was removed.
     */
    @Nullable
    ICarvingVariation removeVariation(ItemStack stack, String group);

    /**
     * Sets the sound resource for a group.
     * <p>
     * This is the sound that is used when a block from this group is chiseled. <br/>
     * Does <i>not</i> need to be explicitly set.
     * 
     * @param name
     *            The name of the group
     * @param sound
     *            The resource string for the sound
     */
    void setVariationSound(String name, SoundEvent sound);

    /**
     * Adds a reverse mapping from an oredict name to a group.
     * <p>
     * This is required whenever a group's contents rely on an oredict entry.
     * <p>
     * You do not need to call this if using {@link CarvingUtils#addOreGroup(String)}.
     * 
     * @param group
     *            The group which should be mapped to the ore name
     * @param ore
     *            The ore name
     */
    void setOreName(ICarvingGroup group, String ore);
}
