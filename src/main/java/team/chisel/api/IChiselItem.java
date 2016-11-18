package team.chisel.api;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import team.chisel.api.carving.ICarvingVariation;

/**
 * Implement this on items which can be used to chisel blocks.
 */
public interface IChiselItem {

    /**
     * Checks whether the chisel can have its GUI opened
     * 
     * @param world
     *            {@link World} object
     * @param player
     *            The player holding the chisel. It can always be assumed that the player's current item will be this.
     * @param chisel
     *            The {@link ItemStack} representing your chisel
     * @return True if the GUI should open. False otherwise.
     */
    boolean canOpenGui(World world, EntityPlayer player, ItemStack chisel);

    /**
     * Called when an item is chiseled using this chisel
     * 
     * @param world
     *            {@link World} object
     * @param player
     *            The {@link EntityPlayer} performing the chiseling.
     * @param chisel
     *            The {@link ItemStack} representing the chisel
     * @param target
     *            The {@link ICarvingVariation} representing the target item
     * @return True if the chisel should be damaged. False otherwise.
     */
    boolean onChisel(World world, EntityPlayer player, ItemStack chisel, ICarvingVariation target);

    /**
     * Called to check if this {@link ItemStack} can be chiseled in this chisel. If not, there will be no possible variants displayed in the GUI.
     * <p>
     * It is not necessary to take into account whether this item <i>has</i> any variants, this method will only be called after that check.
     * 
     * @param world
     *            {@link World} object
     * @param player
     *            The {@link EntityPlayer} performing the chiseling.
     * @param chisel
     *            The {@link ItemStack} representing the chisel
     * @param target
     *            The {@link ICarvingVariation} representing the target item
     * @return True if the current target can be chiseled into anything. False otherwise.
     */
    boolean canChisel(World world, EntityPlayer player, ItemStack chisel, ICarvingVariation target);

    /**
     * Allows you to control if your item can chisel this block in the world.
     * 
     * @param world
     *            World object
     * @param player
     *            {@link EntityPlayer The player} holding the chisel.
     * @param pos
     *            The {@link BlockPos position} of the block being chiseled.
     * @param state
     *            The {@link IBlockState} of the block being chiseled.
     * @return True if the chiseling should take place. False otherwise.
     */
    boolean canChiselBlock(World world, EntityPlayer player, BlockPos pos, IBlockState state);

    /**
     * Allows you to control if your item has chiseling modes.
     * 
     * @param player
     *            {@link EntityPlayer The player} holding the chisel.
     * @param chisel
     *            The {@link ItemStack} representing the chisel.
     * @return True if the chisel supports modes. False otherwise.
     */
    boolean hasModes(EntityPlayer player, ItemStack chisel);
    
    default ItemStack craftItem(ItemStack chisel, ItemStack source, ICarvingVariation target, EntityPlayer player) {
        int toCraft = source.getCount();
        if (chisel.isItemStackDamageable()) {
            int damageLeft = chisel.getMaxDamage() - chisel.getItemDamage() + 1;
            toCraft = Math.min(toCraft, damageLeft);
            chisel.damageItem(toCraft, player);
        }
        ItemStack res = target.getStack().copy();
        source.shrink(toCraft);
        res.setCount(toCraft);
        return res;
    }
}
