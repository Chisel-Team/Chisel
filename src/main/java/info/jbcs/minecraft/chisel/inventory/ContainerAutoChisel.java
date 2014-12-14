package info.jbcs.minecraft.chisel.inventory;

import info.jbcs.minecraft.chisel.block.tileentity.TileEntityAutoChisel;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerAutoChisel extends Container {

    public static TileEntityAutoChisel autoChisel;
    public static EntityPlayer player;

    public ContainerAutoChisel(InventoryPlayer player, TileEntityAutoChisel tileEntityAutoChisel) {

        this.player = player.player;
        autoChisel = tileEntityAutoChisel;

        addSlotToContainer(new Slot(tileEntityAutoChisel, 0, 53, 15));
        addSlotToContainer(new Slot(tileEntityAutoChisel, 1, 78, 51));
        addSlotToContainer(new Slot(tileEntityAutoChisel, 2, 103, 15));

        bindPlayerInventory(player);
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
        return autoChisel.isUseableByPlayer(player);
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int slotNumber) {
        ItemStack itemStack = null;
        Slot slot = (Slot) this.inventorySlots.get(slotNumber);

        if (slot != null && slot.getHasStack()) {
            ItemStack itemStack1 = slot.getStack();
            itemStack = itemStack1.copy();

            if (slotNumber == 2) {
                if (!this.mergeItemStack(itemStack1, 3, 39, true)) {
                    return null;
                }

                slot.onSlotChange(itemStack1, itemStack);
            } else if (slotNumber != 1 && slotNumber != 0) {
                if (!this.mergeItemStack(itemStack1, 0, 1, true)) {
                    return null;
                } else if (slotNumber >= 3 && slotNumber < 30) {
                    if (this.mergeItemStack(itemStack1, 30, 39, false)) {
                        return null;
                    }
                } else if (slotNumber >= 30 && slotNumber < 39 && !this.mergeItemStack(itemStack1, 3, 30, false)) {
                    return null;
                }
            } else if (this.mergeItemStack(itemStack1, 3, 39, false)) {
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
}