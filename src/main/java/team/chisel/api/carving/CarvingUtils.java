package team.chisel.api.carving;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import com.google.common.collect.Lists;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundEvent;

@ParametersAreNonnullByDefault
public class CarvingUtils {

	/**
	 * A simple way to compare two {@link ICarvingVariation} objects based on the {@link ICarvingVariation#getOrder() getOrder()} method.
	 * 
	 * @param v1
	 *            The first {@link ICarvingVariation variation}.
	 * @param v2
	 *            The second {@link ICarvingVariation variation}.
	 * @return A positive integer if the first's order is greater, a negative integer if the second's is greater, and 0 if they are equal.
	 */
	public static int compare(ICarvingVariation v1, ICarvingVariation v2) {
		return v1.getOrder() - v2.getOrder();
	}

	/**
	 * @deprecated Use {@link ICarvingVariation#getStack()}
	 */
	@Deprecated
	public static ItemStack getStack(ICarvingVariation variation) {
		return variation.getStack();
	}

	public static @Nullable ICarvingRegistry chisel;

	/**
	 * @return The instance of the chisel carving registry from the chisel mod.
	 *         <p>
	 *         If chisel is not installed this will return null.
	 */
	public static @Nullable ICarvingRegistry getChiselRegistry() {
		return chisel;
	}

	/**
	 * Creates a standard {@link ICarvingVariation} for the given data. Use this if you do not need any custom behavior in your own variation.
	 * 
	 * @param block
	 *            The block of the variation
	 * @param metadata
	 *            The metadata of both the block and item
	 * @param order
	 *            The sorting order.
	 * @return A standard {@link ICarvingVariation} instance.
	 */
	@Deprecated
	public static ICarvingVariation getDefaultVariationFor(IBlockState state, int order) {
		return new SimpleCarvingVariation(state, order);
	}

	/**
     * Creates a new variation for the given blockstate, with automatic (flawed) conversion to ItemStack when necessary.
     */
    public static ICarvingVariation variationFor(IBlockState state, int order) {
        return new VariationForState(state, order);
    }

    /**
     * Creates a new variation for the given ItemStack, with automatic (flawed) conversion to blockstate when necessary.
     */
    public static ICarvingVariation variationFor(ItemStack stack, int order) {
        return new VariationForStack(stack, order);
    }

    /**
     * Creates a new variation for the given ItemStack and blockstate. Use this for full control over ItemStack/blockstate conversion.
     */
    public static ICarvingVariation variationFor(ItemStack stack, @Nullable IBlockState state, int order) {
        return new SimpleVariation(stack, state, order);
    }

	/**
	 * Creates a standard {@link ICarvingGroup} for the given name. Use this if you do not need any custom behavior in your own group.
	 * 
	 * @param name
	 *            The name of the group.
	 * @return A standard {@link ICarvingGroup} instance.
	 */
	public static ICarvingGroup getDefaultGroupFor(String name) {
		return new SimpleCarvingGroup(name);
	}
	
	@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
	@Getter
	private static abstract class SimpleVariationBase implements ICarvingVariation {
	    private final int order;
	}

	@Deprecated
	private static class SimpleCarvingVariation extends SimpleVariationBase {

		private IBlockState state;

		public SimpleCarvingVariation(IBlockState state, int order) {
		    super(order);
			this.state = state;
		}

		@Override
		@Deprecated
		public Block getBlock() {
			return state.getBlock();
		}

		@Override
		public IBlockState getBlockState() {
			return state;
		}

		@Override
		public ItemStack getStack() {
			return new ItemStack(state.getBlock(), 1, state.getBlock().damageDropped(state));
		}
	}
	
	private static class VariationForState extends SimpleCarvingVariation {

        public VariationForState(IBlockState state, int order) {
            super(state, order);
        }	    
	}
	
	private static class VariationForStack extends SimpleVariationBase {
	    
	    @Getter
	    private final ItemStack stack;
	    private final boolean hasBlock;
	    
	    public VariationForStack(ItemStack stack, int order) {
	        super(order);
	        this.stack = stack;
	        this.hasBlock = stack.getItem() instanceof ItemBlock;
	    }

        @Override
        @Nullable
        @Deprecated
        public Block getBlock() {
            return hasBlock ? ((ItemBlock)stack.getItem()).getBlock() : null;
        }

        @SuppressWarnings("deprecation")
        @Override
        @Nullable
        public IBlockState getBlockState() {
            return hasBlock ? ((ItemBlock)stack.getItem()).getBlock().getStateFromMeta(stack.getItemDamage()) : null;
        }	    
	}
	
    @Getter
    private static class SimpleVariation extends SimpleVariationBase {

        private final ItemStack stack;
        @Nullable
        private final IBlockState blockState;

        public SimpleVariation(ItemStack stack, @Nullable IBlockState state, int order) {
            super(order);
            this.stack = stack;
            this.blockState = state;
        }

        @Override
        @Nullable
        public Block getBlock() {
            return blockState == null ? null : blockState.getBlock();
        }
    }

	private static class SimpleCarvingGroup implements ICarvingGroup {

		private String name;
		private @Nullable SoundEvent sound;

		private List<ICarvingVariation> variations = Lists.newArrayList();

		public SimpleCarvingGroup(String name) {
			this.name = name;
		}

		@Override
		public List<ICarvingVariation> getVariations() {
			return Lists.newArrayList(variations);
		}

		@Override
		public void addVariation(ICarvingVariation variation) {
			variations.add(variation);
			Collections.sort(variations, new Comparator<ICarvingVariation>() {

				@SuppressWarnings("null")
                @Override
				public int compare(ICarvingVariation o1, ICarvingVariation o2) {
					return CarvingUtils.compare(o1, o2);
				}
			});
		}

		@Override
		public boolean removeVariation(ICarvingVariation variation) {
		    return variations.removeIf(v -> 
				           (ItemStack.areItemsEqual(v.getStack(), variation.getStack()) 
				        && (v.getStack().getTagCompound() == null || ItemStack.areItemStackTagsEqual(v.getStack(), variation.getStack()))) 
				||  (v.getBlockState() != null && v.getBlockState().equals(variation.getBlockState()))
		    );
		}

		@Override
		public String getName() {
			return name;
		}

		@Override
		public @Nullable SoundEvent getSound() {
			return sound;
		}

		@Override
		public void setSound(@Nullable SoundEvent sound) {
			this.sound = sound;
		}

		@Override
		@Deprecated
		public @Nullable String getOreName() {
			return null;
		}

		@Override
		@Deprecated
		public void setOreName(@Nullable String oreName) {}
	}
}
