package com.cricketcraft.chisel.inventory;

import com.cricketcraft.chisel.block.tileentity.TileEntityDeChisel;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerDeChisel extends Container{

    public EntityPlayer player;
    public TileEntityDeChisel deChisel;

    public ContainerDeChisel(InventoryPlayer player, TileEntityDeChisel deChisel){
        this.player = player.player;
        this.deChisel = deChisel;

        addSlot(deChisel, 0, 53, 15);
        addSlot(deChisel, 1, 103, 15);

        bindPlayerInventory(player);
    }

    private void addSlot(IInventory inv, final int id, int x, int y) {
        addSlotToContainer(new Slot(inv, id, x, y) {

            @Override
            public boolean isItemValid(ItemStack arg0) {
                return inventory.isItemValidForSlot(id, arg0);
            }
        });
    }

    protected void bindPlayerInventory(InventoryPlayer invPlayer) {
        for (int row = 0; row < 3; row++) {
            for (int column = 0; column < 9; column++) {
                addSlotToContainer(new Slot(invPlayer, column + row * 9 + 9, 8 + column * 18, 84 + row * 18));
            }
        }

        for (int slotNumber = 0; slotNumber < 9; slotNumber++) {
            addSlotToContainer(new Slot(invPlayer, slotNumber, 8 + slotNumber * 18, 142));
        }
    }

    @Override
    public boolean canInteractWith(EntityPlayer player) {
        return deChisel.isUseableByPlayer(player);
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int slotNumber) {
        ItemStack itemStack = null;
        Slot slot = (Slot) this.inventorySlots.get(slotNumber);

        if (slot != null && slot.getHasStack()) {
            ItemStack itemStack1 = slot.getStack();
            itemStack = itemStack1.copy();

            // if we are in the auto chisel
            if (slotNumber <= 6) {
                if (!this.mergeItemStack(itemStack1, 7, 42, false)) {
                    return null;
                }
                // otherwise just put it in one of the machine slots
            } else if (!this.mergeItemStack(itemStack1, 0, 7, false)) {
                return null;
            }

            if (itemStack1.stackSize == 0) {
                slot.putStack(null);
            } else {
                slot.onSlotChanged();
            }

            if (itemStack1.stackSize == itemStack.stackSize) {
                return null;
            }

            slot.onPickupFromSlot(player, itemStack1);
        }

        return itemStack;
    }

    /**
     * Added validation of slot input
     *
     * @author CrazyPants
     */
    @Override
    protected boolean mergeItemStack(ItemStack par1ItemStack, int fromIndex, int toIndex, boolean reversOrder) {

        boolean result = false;
        int checkIndex = fromIndex;

        if (reversOrder) {
            checkIndex = toIndex - 1;
        }

        Slot slot;
        ItemStack itemstack1;

        if (par1ItemStack.isStackable()) {

            while (par1ItemStack.stackSize > 0 && (!reversOrder && checkIndex < toIndex || reversOrder && checkIndex >= fromIndex)) {
                slot = (Slot) this.inventorySlots.get(checkIndex);
                itemstack1 = slot.getStack();

                if (itemstack1 != null && itemstack1.getItem() == par1ItemStack.getItem() && (!par1ItemStack.getHasSubtypes() || par1ItemStack.getItemDamage() == itemstack1.getItemDamage())
                        && ItemStack.areItemStackTagsEqual(par1ItemStack, itemstack1) && slot.isItemValid(par1ItemStack)) {

                    int mergedSize = itemstack1.stackSize + par1ItemStack.stackSize;
                    int maxStackSize = Math.min(par1ItemStack.getMaxStackSize(), slot.getSlotStackLimit());
                    if (mergedSize <= maxStackSize) {
                        par1ItemStack.stackSize = 0;
                        itemstack1.stackSize = mergedSize;
                        slot.onSlotChanged();
                        result = true;
                    } else if (itemstack1.stackSize < maxStackSize) {
                        par1ItemStack.stackSize -= maxStackSize - itemstack1.stackSize;
                        itemstack1.stackSize = maxStackSize;
                        slot.onSlotChanged();
                        result = true;
                    }
                }

                if (reversOrder) {
                    --checkIndex;
                } else {
                    ++checkIndex;
                }
            }
        }

        if (par1ItemStack.stackSize > 0) {
            if (reversOrder) {
                checkIndex = toIndex - 1;
            } else {
                checkIndex = fromIndex;
            }

            while (!reversOrder && checkIndex < toIndex || reversOrder && checkIndex >= fromIndex) {
                slot = (Slot) this.inventorySlots.get(checkIndex);
                itemstack1 = slot.getStack();

                if (itemstack1 == null && slot.isItemValid(par1ItemStack)) {
                    ItemStack in = par1ItemStack.copy();
                    in.stackSize = Math.min(in.stackSize, slot.getSlotStackLimit());

                    slot.putStack(in);
                    slot.onSlotChanged();
                    if (in.stackSize >= par1ItemStack.stackSize) {
                        par1ItemStack.stackSize = 0;
                    } else {
                        par1ItemStack.stackSize -= in.stackSize;
                    }
                    result = true;
                    break;
                }

                if (reversOrder) {
                    --checkIndex;
                } else {
                    ++checkIndex;
                }
            }
        }

        return result;
    }
}
