package team.chisel.api.carving;

import java.util.List;
import java.util.Optional;

import javax.annotation.ParametersAreNonnullByDefault;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.resources.ResourceLocation;

/**
 * Represents a registry of {@link ICarvingGroup}s
 * <p>
 * To obtain chisel's instance of this class, use {@link CarvingUtils#getChiselRegistry()}
 */
@ParametersAreNonnullByDefault
public interface IVariationRegistry {

    /* Getters */
    
    Optional<ICarvingGroup> getGroup(ResourceLocation id);

    Optional<ICarvingGroup> getGroup(Item item);
    
    Optional<ICarvingGroup> getGroup(Block block);

    Optional<ICarvingVariation> getVariation(Block block);

    Optional<ICarvingVariation> getVariation(Item item);

    List<ItemStack> getItemsForChiseling(ItemStack chiseled);

    /**
     * A convenience version of {@link IVariationRegistry#getItemsForChiseling(ItemStack)} which takes an already computed group.
     * 
     * @see IVariationRegistry#getItemsForChiseling(ItemStack)
     */
    List<ItemStack> getItemsForChiseling(ResourceLocation group);

    List<ICarvingGroup> getGroups();

    /* Setters */

    void addGroup(ICarvingGroup group);
    /**
     * Adds a variation to the registry.
     * 
     * @param groupName
     *            The name of the group to add to
     * @param variation
     *            The {@link ICarvingVariation} to add
     */
//    default void addVariation(ResourceLocation groupName, ICarvingVariation variation) {
//        addVariations(groupName, () -> Collections.singletonList(variation));
//    }
//    
//    void addVariations(ResourceLocation groupName, ICarvingVariationProvider provider);

    /**
     * Removes a group from the registry.
     * <p>
     * This in effect removes all variations associated with the group, though they are not explicitly removed from the object. If you maintain a reference to the {@link ICarvingGroup} that is
     * removed, it will still contain its variations.
     * 
     * @param groupName
     *            The name of the group to remove.
     * @return The variations removed, if any.
     */
    ICarvingGroup removeGroup(ResourceLocation groupName);

    /**
     * Removes a variation with the passed {@link Block} and metadata from the registry. If this variation is registered with multiple groups, it will remove it from all of them.
     * 
     * @param state
     *            The {@link BlockState} of the {@link ICarvingVariation variation}
     * @return The ICarvingVariation that was removed. Null if nothing was removed.
     */
//    @Nullable
//    ICarvingVariation removeVariation(BlockState state);

    /**
     * Removes a variation with the passed {@link Block} and metadata from the registry, but only from the specified {@link ICarvingGroup} name.
     * 
     * @param state
     *            The {@link BlockState} of the {@link ICarvingVariation variation}
     * @param group
     *            The name of the group that the variation should be removed from
     * @return The ICarvingVariation that was removed. Null if nothing was removed.
     */
//    @Nullable
//    ICarvingVariation removeVariation(BlockState state, ResourceLocation group);
    
    /**
     * Removes a variation with the passed ItemStack from the registry. If this variation is registered with multiple groups, it will remove it from all of them.
     * 
     * @param stack
     *            The {@link ItemStack} of the {@link ICarvingVariation variation}
     * @return The ICarvingVariation that was removed. Null if nothing was removed.
     */
//    @Nullable
//    ICarvingVariation removeVariation(ItemStack stack);

    /**
     * Removes a variation with the passed ItemStack from the registry, but only from the specified {@link ICarvingGroup} name.
     * 
     * @param stack
     *            The {@link ItemStack} of the {@link ICarvingVariation variation}
     * @param group
     *            The name of the group that the variation should be removed from
     * @return The ICarvingVariation that was removed. Null if nothing was removed.
     */
//    @Nullable
//    ICarvingVariation removeVariation(ItemStack stack, ResourceLocation group);
}
