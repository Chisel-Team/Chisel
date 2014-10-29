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

    public ContainerAutoChisel(InventoryPlayer player, TileEntityAutoChisel tileEntityAutoChisel){

        this.player = player.player;
        autoChisel = tileEntityAutoChisel;

        addSlotToContainer(new Slot(tileEntityAutoChisel, 0, 53, 15));
        addSlotToContainer(new Slot(tileEntityAutoChisel, 1, 78, 51));
        addSlotToContainer(new Slot(tileEntityAutoChisel, 2, 103, 15));

        bindPlayerInventory(player);
    }

    protected void bindPlayerInventory(InventoryPlayer invPlayer){
        for(int row = 0; row < 3; row++){
            for(int column = 0; column < 9; column++){
                addSlotToContainer(new Slot(invPlayer, column + row * 9 + 9, 8 + column * 18, 84 + row * 18));
            }
        }

        for(int slotNumber = 0; slotNumber < 9; slotNumber++){
            addSlotToContainer(new Slot(invPlayer, slotNumber, 8 + slotNumber * 18, 142));
        }
    }

    @Override
    public boolean canInteractWith(EntityPlayer player) {
        return autoChisel.isUseableByPlayer(player);
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int slot){
        ItemStack stack = null;
        Slot slotObject = (Slot) inventorySlots.get(slot);

        if(slotObject != null && slotObject.getHasStack()){
            ItemStack stackInSlot = slotObject.getStack();
            stack = stackInSlot.copy();

            if(slot == 2){
                if(!this.mergeItemStack(stackInSlot, 3, 39, true)){
                    return null;
                }
            }

            if(slot < autoChisel.getSizeInventory()){
                if(!this.mergeItemStack(stackInSlot, 0, 35, true)){
                    return null;
                }
            } else if(!this.mergeItemStack(stackInSlot, 0, 3, false)){
                return null;
            }

            if(stackInSlot.stackSize == 0){
                slotObject.putStack(null);
            } else {
                slotObject.onSlotChanged();
            }

            if(stackInSlot.stackSize == stack.stackSize){
                return null;
            }

            slotObject.onPickupFromSlot(player, stackInSlot);
        }

        return stack;
    }
}