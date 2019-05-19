package team.chisel.common.inventory;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import team.chisel.api.IChiselItem;
import team.chisel.api.carving.CarvingUtils;
import team.chisel.api.carving.ICarvingVariation;
import team.chisel.common.util.SoundUtil;

@ParametersAreNonnullByDefault
public class SlotChiselSelection extends Slot {

    private final @Nonnull ContainerChisel container;

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
    
    public static ItemStack craft(ContainerChisel container, EntityPlayer player, ItemStack itemstack, boolean simulate) {
        ItemStack crafted = container.getInventoryChisel().getStackInSpecialSlot();
        ItemStack chisel = container.getChisel();
        if (simulate) {
            itemstack = itemstack.copy();
            crafted = crafted.isEmpty() ? ItemStack.EMPTY : crafted.copy();
            chisel = chisel.copy();
        }
        ItemStack res = ItemStack.EMPTY;
        if (!chisel.isEmpty() && !crafted.isEmpty()) {                
            IChiselItem item = (IChiselItem) container.getChisel().getItem();
            ICarvingVariation variation = CarvingUtils.getChiselRegistry().getVariation(itemstack);
            if (!item.canChisel(player.world, player, chisel, variation)) {
                return res;
            }
            res = item.craftItem(chisel, crafted, itemstack, player);
            if (!simulate) {
                container.getInventoryChisel().setStackInSpecialSlot(crafted.getCount() == 0 ? ItemStack.EMPTY : crafted);
                container.onChiselSlotChanged();
                item.onChisel(player.world, player, chisel, variation);
                if (chisel.getCount() == 0) {
                    container.getInventoryPlayer().setInventorySlotContents(container.getChiselSlot(), ItemStack.EMPTY);
                }
                if (!crafted.isEmpty() && !item.canChisel(player.world, player, chisel, variation)) {
                    container.onChiselBroken();
                }

                container.getInventoryChisel().updateItems();
                container.detectAndSendChanges();
            }
        }
        
        return res;
    }

    @Override
    public ItemStack onTake(EntityPlayer player, ItemStack itemstack) {
        ItemStack chisel = container.getChisel().copy();
        ItemStack res = craft(container, player, itemstack, false);
        if (container.currentClickType != ClickType.PICKUP) {
            res.shrink(1);
        }
        if (!res.isEmpty()) {
            SoundUtil.playSound(player, chisel, itemstack);
            player.inventory.setItemStack(res);
        }
        return ItemStack.EMPTY; // Return value seems to be ignored?
    }
}