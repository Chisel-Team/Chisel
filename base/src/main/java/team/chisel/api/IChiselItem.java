package team.chisel.api;

import java.util.function.Consumer;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import team.chisel.api.carving.ICarvingVariation;
import team.chisel.api.carving.IChiselMode;

/**
 * Implement this on items which can be used to chisel blocks.
 */
@ParametersAreNonnullByDefault
public interface IChiselItem {

    /**
     * Checks whether the chisel can have its GUI opened, and will be called every tick to assure the GUI can <i>remain</i> open.
     * 
     * @param world
     *            {@link Level} object
     * @param player
     *            The player holding the chisel. It can always be assumed that the player's current item will be this.
     * @param hand
     *            The {@link InteractionHand} which the chisel is in. Use this and the {@code player} parameter to get stack context.
     * @return True if the GUI should open. False otherwise.
     */
    boolean canOpenGui(Level world, Player player, InteractionHand hand);

    /**
     * The type of GUI to open. Currently the only valid options are normal and hitech.
     * <p>
     * Use {@link IChiselGuiType.ChiselGuiType} as return value.
     * 
     * @param world
     *            {@link Level} object
     * @param player
     *            The player holding the chisel. It can always be assumed that the player's current item will be this.
     * @param hand
     *            The {@link InteractionHand} which the chisel is in. Use this and the {@code player} parameter to get stack context.
     * @return
     */
    IChiselGuiType<?> getGuiType(Level world, Player player, InteractionHand hand);

    /**
     * Called when an item is chiseled using this chisel
     * 
     * @param world
     *            {@link Level} object
     * @param player
     *            The {@link Player} performing the chiseling.
     * @param chisel
     *            The {@link ItemStack} representing the chisel
     * @param target
     *            The {@link ICarvingVariation} representing the target item
     * @return Unused.
     */
    boolean onChisel(Level world, Player player, ItemStack chisel, ICarvingVariation target);

    /**
     * Called to check if this {@link ItemStack} can be chiseled in this chisel. If not, there will be no possible variants displayed in the GUI.
     * <p>
     * It is not necessary to take into account whether this item <i>has</i> any variants, this method will only be called after that check.
     * 
     * @param world
     *            {@link Level} object
     * @param player
     *            The {@link Player} performing the chiseling.
     * @param chisel
     *            The {@link ItemStack} representing the chisel
     * @param target
     *            The {@link ICarvingVariation} representing the target item
     * @return True if the current target can be chiseled into anything. False otherwise.
     */
    boolean canChisel(Level world, Player player, ItemStack chisel, ICarvingVariation target);

    /**
     * Allows you to control if your item can chisel this block in the world.
     * 
     * @param world
     *            Level object
     * @param player
     *            {@link Player The player} holding the chisel.
     * @param hand
     *            The {@link InteractionHand} which the chisel is in. Use this and the {@code player} parameter to get stack context.
     * @param pos
     *            The {@link BlockPos position} of the block being chiseled.
     * @param state
     *            The {@link BlockState} of the block being chiseled.
     * @return True if the chiseling should take place. False otherwise.
     */
    boolean canChiselBlock(Level world, Player player, InteractionHand hand, BlockPos pos, BlockState state);

    /**
     * Allows you to control if your item supports a given chisel mode.
     * 
     * @param player
     *            {@link Player The player} holding the chisel.
     * @param chisel
     *            The {@link ItemStack} of the current chisel.
     * @param mode
     *            The current {@link IChiselMode}.
     * @return True if the chisel supports this mode. False otherwise.
     */
    boolean supportsMode(Player player, ItemStack chisel, IChiselMode mode);
    
    /**
     * Allows this chisel to provide a different sound for the given {@link ICarvingVariation}.
     * 
     * @param world
     *            Level object.
     * @param player
     *            {@link Player The player} holding the chisel
     * @param chisel
     *            The {@link ItemStack} representing the chisel
     * @param target
     *            The {@link Block} representing the target
     * @return A sound to play, instead of the variation's sound, or null for default.
     */
    default @Nullable SoundEvent getOverrideSound(Level world, Player player, ItemStack chisel, Block target) {
        return null;
    }
    
    /**
     * Chisels a stack into another stack, taking into consideration this chisel's damage
     * 
     * @param chisel
     *            The {@link ItemStack} being used as a chisel.
     * @param source
     *            The source {@link ItemStack}. This is the stack of items that is being chiseled into something else. This must be modified by this function. i.e. if the chisel has only 1 damage
     *            left, the source stack size should be decremented by 1.
     * @param target
     *            The target stack, the type of which the source stack is being chiseled into. Do NOT modify this stack, instead return the result of the craft.
     * @param player
     *            The player doing the chiseling.
     * @param onBreak
     *            A callback to perform when the chisel breaks.
     * @return The result of the craft.
     */
    default ItemStack craftItem(ItemStack chisel, ItemStack source, ItemStack target, Player player, Consumer<Player> onBreak) {
        if (chisel.isEmpty()) return ItemStack.EMPTY;
        int toCraft = Math.min(source.getCount(), target.getMaxStackSize());
        if (chisel.isDamageableItem()) {
            int damageLeft = chisel.getMaxDamage() - chisel.getDamageValue() + 1;
            toCraft = Math.min(toCraft, damageLeft);
            chisel.hurtAndBreak(toCraft, player, onBreak);
        }
        ItemStack res = target.copy();
        source.shrink(toCraft);
        res.setCount(toCraft);
        return res;
    }
}
