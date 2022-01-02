package team.chisel.api.carving;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import com.google.common.base.Objects;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.Tag;
import net.minecraft.tags.Tag.Named;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import team.chisel.Chisel;
import team.chisel.common.init.ChiselSounds;

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
        return ItemStack.matches(stack1, stack2) && ItemStack.tagMatches(stack1, stack2);
	}
	
	public static int hashStack(ItemStack stack) {
	    return Objects.hashCode(stack.getItem(), stack.getTag());
	}
	
	private static TranslatableComponent getDisplayName(Named<?> tag, String name) {
	    return Chisel.registrate().addLang("group", tag.getName(), name);
	}

    public static ICarvingGroup itemGroup(Named<Block> blocks, String name) {
        return new BlockTagGroup(blocks, getDisplayName(blocks, name));
    }

    public static ICarvingGroup blockGroup(Named<Item> items, String name) {
        return new ItemTagGroup(items, getDisplayName(items, name));
    }
	
	@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
	@Getter(onMethod = @__({@Override}))
	private static abstract class AbstractGroup implements ICarvingGroup {
	    
	    private final ResourceLocation id;

	    private final SoundEvent sound = ChiselSounds.fallback;
	    
	    private final TranslatableComponent displayName;
	    
	    @Override
	    public String getTranslationKey() {
	        return displayName.getKey();
	    }

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

		public BlockTagGroup(Tag.Named<Block> tag, TranslatableComponent displayName) {
		    super(tag.getName(), displayName);
		}
	}

	private static class ItemTagGroup extends AbstractGroup {
	    
	    public ItemTagGroup(Tag.Named<Item> tag, TranslatableComponent displayName) {
	        super(tag.getName(), displayName);
	    }
	}
}
