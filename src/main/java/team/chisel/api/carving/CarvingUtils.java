package team.chisel.api.carving;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import com.google.common.base.Objects;
import com.google.common.collect.Lists;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItem;
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

	public static @Nullable ICarvingRegistry chisel;
	public static @Nullable IModeRegistry modes;

	/**
	 * @return The instance of the chisel carving registry from the chisel mod.
	 *         <p>
	 *         If chisel is not installed this will return null.
	 */
	public static @Nullable ICarvingRegistry getChiselRegistry() {
		return chisel;
	}
	
	public static @Nullable IModeRegistry getModeRegistry() {
	    return modes;
	}
	
	public static boolean stacksEqual(ItemStack stack1, ItemStack stack2) {
        return ItemStack.areItemStacksEqual(stack1, stack2) && ItemStack.areItemStackTagsEqual(stack1, stack2);
	}
	
	public static int hashStack(ItemStack stack) {
	    return Objects.hashCode(stack.getItem(), stack.getTag());
	}

	/**
     * Creates a new variation for the given blockstate, with automatic (flawed) conversion to ItemStack when necessary.
     */
    public static ICarvingVariation variationFor(BlockState state, int order) {
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
    public static ICarvingVariation variationFor(ItemStack stack, @Nullable BlockState state, int order) {
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

	    @Override
	    public boolean equals(Object obj) {
	        if (!(obj instanceof ICarvingVariation)) return false;
	        ICarvingVariation other = (ICarvingVariation) obj;
	        BlockState state = getBlockState();
	        BlockState otherState = other.getBlockState();
	        if (state == null || otherState == null) {
	            return stacksEqual(getStack(), other.getStack());
	        }
	        return state == otherState;
	    }
	    
	    @Override
	    public int hashCode() {
	        BlockState state = getBlockState();
	        if (state != null) {
	            return state.hashCode();
	        }
	        return hashStack(getStack());
	    }
	}

	private static class SimpleCarvingVariation extends SimpleVariationBase {

		private BlockState state;

		public SimpleCarvingVariation(BlockState state, int order) {
		    super(order);
			this.state = state;
		}

		@Override
		public BlockState getBlockState() {
			return state;
		}

		@Override
		public @Nonnull ItemStack getStack() {
			return new ItemStack(getBlockState().getBlock());
		}
	}
	
	private static class VariationForState extends SimpleCarvingVariation {

        public VariationForState(BlockState state, int order) {
            super(state, order);
        }	    
	}
	
	private static class VariationForStack extends SimpleVariationBase {
	    
	    private final ItemStack stack;
	    private final boolean hasBlock;
	    
	    public VariationForStack(ItemStack stack, int order) {
	        super(order);
	        this.stack = stack;
	        this.hasBlock = stack.getItem() instanceof BlockItem;
	    }
	    
	    @Override
	    public @Nonnull ItemStack getStack() {
	        return stack.copy();
	    }

        @SuppressWarnings("deprecation")
        @Override
        @Nullable
        public BlockState getBlockState() {
            return hasBlock ? ((BlockItem) stack.getItem()).getBlock().getDefaultState() : null;
        }
    }
	
    private static class SimpleVariation extends SimpleVariationBase {

        private final ItemStack stack;
        @Nullable
        @Getter
        private final BlockState blockState;

        public SimpleVariation(ItemStack stack, @Nullable BlockState state, int order) {
            super(order);
            this.stack = stack;
            this.blockState = state;
        }
        
        @Override
        public @Nonnull ItemStack getStack() {
            return stack.copy();
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

//		@Override
//		public void addVariation(ICarvingVariation variation) {
//			variations.add(variation);
//			Collections.sort(variations, new Comparator<ICarvingVariation>() {
//
//				@SuppressWarnings("null")
//                @Override
//				public int compare(ICarvingVariation o1, ICarvingVariation o2) {
//					return CarvingUtils.compare(o1, o2);
//				}
//			});
//		}
//
//		@Override
//		public boolean removeVariation(ICarvingVariation variation) {
//		    return variations.removeIf(v -> 
//				           (ItemStack.areItemsEqual(v.getStack(), variation.getStack()) 
//				        && (v.getStack().getTagCompound() == null || ItemStack.areItemStackTagsEqual(v.getStack(), variation.getStack()))) 
//				||  (v.getBlockState() != null && v.getBlockState().equals(variation.getBlockState()))
//		    );
//		}

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
	}
}
