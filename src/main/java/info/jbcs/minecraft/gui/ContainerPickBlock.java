package info.jbcs.minecraft.gui;

import info.jbcs.minecraft.utilities.InventoryStatic;

import java.util.ArrayList;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ContainerPickBlock extends Container
{
    ArrayList<ItemStack> items = new ArrayList<ItemStack>();
    GuiPickBlock gui;
    EntityPlayer player;
    int width = 9;
    int height = 7;

    SlotPickBlock resultSlot;

    public ContainerPickBlock(EntityPlayer p)
    {
        for (Object o : Item.itemRegistry.getKeys())
        {
            Item item = (Item)Item.itemRegistry.getObject((String)o);

            if (item != null && item.getCreativeTab() != null)
            {
                item.getSubItems(item, null, items);
            }
        }

        int index = 0;

        for (int y = 0; y < height; y++)
        {
            for (int x = 0; x < width; x++)
            {
                addSlotToContainer(new SlotPickBlock(this, index++, 9 + x * 18, 18 + y * 18));
            }
        }

        addSlotToContainer(resultSlot = new SlotPickBlock(this, index++, 18, 153));
        player = p;
        scrollTo(0);
    }

    public InventoryStatic inventory = new InventoryStatic(width * height + 1)
    {
        @Override
        public boolean isItemValidForSlot(int i, ItemStack itemstack)
        {
            return false;
        }
    };

    public void scrollTo(float offset)
    {
        int columnsNotFitting = items.size() / width - height + 1;

        if (columnsNotFitting < 0)
        {
            columnsNotFitting = 0;
        }

        int columnOffset = (int)(offset * columnsNotFitting + 0.5D);

        for (int y = 0; y < height; ++y)
        {
            for (int x = 0; x < width; ++x)
            {
                int index = x + (y + columnOffset) * width;

                if (index >= 0 && index < items.size())
                {
                    inventory.setInventorySlotContents(x + y * width, items.get(index));
                }
                else
                {
                    inventory.setInventorySlotContents(x + y * width, (ItemStack) null);
                }
            }
        }
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int index)
    {
        SlotPickBlock slot = (SlotPickBlock) this.inventorySlots.get(index);
        return slot.transferStackInSlot(player);
    }

    @Override
    public boolean canInteractWith(EntityPlayer entityplayer)
    {
        return true;
    }
}
