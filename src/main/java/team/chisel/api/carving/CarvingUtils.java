package team.chisel.api.carving;

import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import com.google.common.base.Objects;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tags.Tag;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;
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
//	public static int compare(ICarvingVariation v1, ICarvingVariation v2) {
//		return v1.getOrder() - v2.getOrder();
//	}

	public static @Nullable IVariationRegistry chisel;
	public static @Nullable IModeRegistry modes;

	/**
	 * @return The instance of the chisel carving registry from the chisel mod.
	 *         <p>
	 *         If chisel is not installed this will return null.
	 */
	public static @Nullable IVariationRegistry getChiselRegistry() {
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
    public static ICarvingGroup blockGroup(Tag<Block> blocks) {
        return new BlockTagGroup(blocks);
    }

    /**
     * Creates a new variation for the given ItemStack, with automatic (flawed) conversion to blockstate when necessary.
     */
    public static ICarvingGroup itemGroup(Tag<Item> items) {
        return new ItemTagGroup(items);
    }

    /**
     * Creates a new variation for the given ItemStack and blockstate. Use this for full control over ItemStack/blockstate conversion.
     */
    public static ICarvingGroup group(Tag<Block> blocks, Tag<Item> items) {
        return new SimpleGroup(items, blocks);
    }
	
	@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
	@Getter(onMethod = @__({@Override}))
	private static abstract class AbstractGroup implements ICarvingGroup {
	    
	    @Getter(onMethod = @__({@Override}))
	    private final SoundEvent sound = null;

//	    @Override
//	    public boolean equals(Object obj) {
//	        if (!(obj instanceof ICarvingVariation)) return false;
//	        ICarvingVariation other = (ICarvingVariation) obj;
//	        BlockState state = getBlockState();
//	        BlockState otherState = other.getBlockState();
//	        if (state == null || otherState == null) {
//	            return stacksEqual(getStack(), other.getStack());
//	        }
//	        return state == otherState;
//	    }
//	    
//	    @Override
//	    public int hashCode() {
//	        BlockState state = getBlockState();
//	        if (state != null) {
//	            return state.hashCode();
//	        }
//	        return hashStack(getStack());
//	    }
	}

	private static class BlockTagGroup extends AbstractGroup {

		private final Tag<Block> tag;

		public BlockTagGroup(Tag<Block> tag) {
		    super();
		    this.tag = tag;
		}

		@Override
		public Collection<Block> getBlocks() {
		    return tag.getAllElements();
		}

		@Override
		public Collection<Item> getItems() {
		    return getBlocks().stream().map(IItemProvider::asItem).collect(Collectors.toList());
		}
	}

	private static class ItemTagGroup extends AbstractGroup {
	    
	    private final Tag<Item> tag;
	    
	    public ItemTagGroup(Tag<Item> tag) {
	        super();
	        this.tag = tag;
	    }
	    
	    @Override
	    public Collection<Block> getBlocks() {
	        return Collections.emptyList();
	    }
	    
	    @Override
	    public Collection<Item> getItems() {
	        return tag.getAllElements();
	    }
    }
	
	@RequiredArgsConstructor
    private static class SimpleGroup extends AbstractGroup {

        private final Tag<Item> itemTag;
        private final Tag<Block> blockTag;
        
        @Override
        public Collection<Block> getBlocks() {
            return blockTag.getAllElements();
        }
        
        @Override
        public Collection<Item> getItems() {
            return itemTag.getAllElements();
        }
    }
}
