package team.chisel.api.carving;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

/**
 * Represents a variation of a chiselable block.
 */
public interface ICarvingVariation {
    
    ResourceLocation getGroup();

    /**
     * The base blockstate of this variation.
     * 
     * @return A {@link BlockState} that is the base of this variation, or null if none exists.
     */
    @Nullable
    BlockState getBlockState();

    /**
     * The {@link ItemStack} of this variation. This can be customized to allow for variations that differ on NBT alone.
     * <p>
     * This ItemStack should be a copy (or a new instance) of the stack, callers of this method are not required to leave the stack unmodified.
     * 
     * @return An {@link ItemStack} that represents this variation.
     */
	@Nonnull
	ItemStack getStack();

	/**
	 * The "order" of this variation. Represents its position in the list of variations held by a group.
	 * 
	 * @return An integer to represent the position of this variation in the list of all variations in the group
	 */
	int getOrder();
}
