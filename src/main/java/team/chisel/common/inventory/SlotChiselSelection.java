package team.chisel.common.inventory;

import javax.annotation.ParametersAreNonnullByDefault;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import team.chisel.api.IChiselItem;
import team.chisel.api.carving.CarvingUtils;
import team.chisel.api.carving.ICarvingVariation;
import team.chisel.common.util.SoundUtil;

@ParametersAreNonnullByDefault
public class SlotChiselSelection extends Slot {

    private final ContainerChisel container;

    public SlotChiselSelection(ContainerChisel container, InventoryChiselSelection inv, IInventory iinventory, int i, int j, int k) {
        super(iinventory, i, j, k);
        this.container = container;
    }

    @Override
    public boolean isItemValid(ItemStack itemstack) {
        return false;
    }

    @Override
    public boolean canTakeStack(EntityPlayer par1EntityPlayer) {
        return par1EntityPlayer.inventory.getItemStack().isEmpty();
    }
    
    public ItemStack craft(EntityPlayer player, ItemStack itemstack, boolean simulate) {
        ItemStack heldStack = player.inventory.getItemStack();
        ItemStack crafted = container.getInventoryChisel().getStackInSpecialSlot();
        ItemStack chisel = container.getChisel();
        if (simulate) {
            itemstack = itemstack.copy();
            crafted = crafted.isEmpty() ? ItemStack.EMPTY : crafted.copy();
            chisel = chisel.copy();
        }
        ItemStack res = ItemStack.EMPTY;
        if (!chisel.isEmpty() && !crafted.isEmpty()) {

//        if (heldStack == null) {
//            if (!simulate) {
//                container.getInventoryChisel().decrStackSize(container.getInventoryChisel().size, 1);
//                container.onChiselSlotChanged();
//            }
//        } else {
//            player.inventory.setItemStack(null);

            ItemStack source = crafted.copy();
                
            IChiselItem item = (IChiselItem) container.getChisel().getItem();
            res = item.craftItem(chisel, crafted, itemstack, player);
            if (!simulate) {
                container.getInventoryChisel().setStackInSpecialSlot(crafted.getCount() == 0 ? ItemStack.EMPTY : crafted);
                container.onChiselSlotChanged();
                item.onChisel(player.world, player, chisel, CarvingUtils.getChiselRegistry().getVariation(itemstack));
                if (chisel.getCount() == 0) {
                    container.getInventoryPlayer().setInventorySlotContents(container.getChiselSlot(), ItemStack.EMPTY);
                    container.onChiselBroken();
                }

                container.getInventoryChisel().updateItems();
                container.detectAndSendChanges();

//            if (player.world.isRemote) {
                ICarvingVariation v = CarvingUtils.getChiselRegistry().getVariation(source);
                IBlockState state = v == null ? null : v.getBlockState();
                if (state == null) {
                    if (source.getItem() instanceof ItemBlock) {
                        state = ((ItemBlock) source.getItem()).getBlock().getStateFromMeta(source.getItem().getMetadata(source.getItemDamage()));
                    } else {
                        state = Blocks.STONE.getDefaultState(); // fallback
                    }
                }
                SoundUtil.playSound(player, chisel, state);
//            } else {
//                // container.getInventoryPlayer().player.addStat(Statistics.blocksChiseled, crafted.stackSize);
//            }
            }
        }
        
        return res;
    }

    @Override
    public ItemStack onTake(EntityPlayer player, ItemStack itemstack) {
        player.inventory.setItemStack(craft(player, itemstack, false));
        return ItemStack.EMPTY; // TODO 1.11 ???
    }
}