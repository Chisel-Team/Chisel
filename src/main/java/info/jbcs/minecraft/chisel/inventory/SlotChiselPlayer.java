package info.jbcs.minecraft.chisel.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;


public class SlotChiselPlayer extends Slot
{

    public SlotChiselPlayer(ContainerChisel container, InventoryPlayer inv, int i, int j, int k)
    {
        super(inv, i, j, k);

        this.container = container;
        selInventory = inv;
    }

    @Override
    public boolean isItemValid(ItemStack par1ItemStack)
    {
        return false;
    }

    @Override
    public void onPickupFromSlot(EntityPlayer player, ItemStack itemstack)
    {
        container.finished = true;
    }

    @Override
    public boolean getHasStack()
    {
        return false;
    }

    @Override
    public ItemStack decrStackSize(int i)
    {
        return null;
    }

    ContainerChisel container;
    InventoryPlayer selInventory;
}
