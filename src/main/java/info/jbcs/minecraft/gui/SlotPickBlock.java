package info.jbcs.minecraft.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotPickBlock extends Slot
{
    ContainerPickBlock container;

    public SlotPickBlock(ContainerPickBlock c, int index, int x, int y)
    {
        super(c.inventory, index, x, y);
        container = c;
    }

    void click(EntityPlayer player, ItemStack itemstack, int count)
    {
        player.inventory.setItemStack(null);

        if (itemstack == null)
        {
            return;
        }

        if (container.gui == null)
        {
            return;
        }

        putStack(new ItemStack(itemstack.getItem(), itemstack.stackSize, itemstack.getItemDamage()));
        int newSize;

        if (container.resultSlot == this)
        {
            newSize = itemstack.stackSize - count;
        }
        else
        {
            newSize = itemstack.stackSize;
            ItemStack otherstack = container.resultSlot.getStack();

            if (otherstack != null && otherstack.getItem().equals(itemstack.getItem()) && otherstack.getItemDamage() == itemstack.getItemDamage())
            {
                newSize = otherstack.stackSize + count;
            }
            else
            {
                newSize = count;
            }
        }

        if (newSize > 64)
        {
            newSize = 64;
        }

        container.resultSlot.putStack(newSize <= 0 ? null : new ItemStack(itemstack.getItem(), newSize, itemstack.getItemDamage()));
    }

    @Override
    public void onPickupFromSlot(EntityPlayer player, ItemStack itemstack)
    {
        super.onPickupFromSlot(player, itemstack);
        click(player, itemstack, 1);
    }

    public ItemStack transferStackInSlot(EntityPlayer player)
    {
        click(player, getStack(), 64);
        return null;
    }
}
