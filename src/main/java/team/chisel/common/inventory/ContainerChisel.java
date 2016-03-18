package team.chisel.common.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import team.chisel.Chisel;
import team.chisel.api.carving.ICarvingRegistry;
import team.chisel.common.carving.Carving;

public class ContainerChisel extends Container {

    public final InventoryChiselSelection inventory;
    public InventoryPlayer playerInventory;
    int chiselSlot;
    public ItemStack chisel;
    public boolean finished;
    public ICarvingRegistry carving;

    private int totalSlots = 0;

    public ContainerChisel(InventoryPlayer inventoryplayer, InventoryChiselSelection inv) {
        inventory = inv;
        playerInventory = inventoryplayer;
        chiselSlot = playerInventory.currentItem;
        inv.container = this;

        int top = 8, left = 62;

        // selection slots
        for (int i = 0; i < InventoryChiselSelection.normalSlots; i++) {
            addSlotToContainer(new SlotChiselSelection(this, inventory, inventory, i, left + ((i % 10) * 18), top + ((i / 10) * 18)));
        }

        // main slot
        addSlotToContainer(new SlotChiselInput(this, inventory, InventoryChiselSelection.normalSlots, 24, 24));

        top += 112;
        left += 9;
        // main inv
        for (int i = 0; i < 27; i++) {
            addSlotToContainer(new Slot(inventoryplayer, i + 9, left + ((i % 9) * 18), top + (i / 9) * 18));
        }

        top += 58;
        for (int i = 0; i < 9; i++) {
            addSlotToContainer(new Slot(inventoryplayer, i, left + ((i % 9) * 18), top + (i / 9) * 18));
        }

        chisel = inventoryplayer.getCurrentItem();
        if (chisel != null && chisel.getTagCompound() != null) {
            ItemStack stack = ItemStack.loadItemStackFromNBT(chisel.getTagCompound().getCompoundTag("chiselTarget"));
            inventory.setInventorySlotContents(InventoryChiselSelection.normalSlots, stack);
        }
        Chisel.debug("There are " + totalSlots + " slots but list is " + inventorySlots.size());

//        Item item = chisel.getItem();
        carving = Carving.chisel;
        
        inventory.updateItems();
    }

    @Override
    protected Slot addSlotToContainer(Slot slotIn){
        totalSlots++;
        return super.addSlotToContainer(slotIn);
    }

    // TODO this has changed a lot, probaly won't work
    @Override
    public ItemStack /* slotClick */ func_184996_a(int slotId, int dragType, ClickType clickTypeIn, EntityPlayer player) {
        // we need to subtract away all the other slots
        int clickedSlot = slotId - inventory.getSizeInventory() - 27;
        Chisel.debug("Slot clicked is " + slotId + " and slot length is " + inventorySlots.size());
        try {
            Slot slot = (Slot)inventorySlots.get(slotId);
            Chisel.debug("Slot is "+slot);
        } catch (Exception exception){
            Chisel.debug("Exception getting slot");
            exception.printStackTrace();
        }

        // if the player has clicked on the chisel or is trying to use a number key to force an itemstack into the slot the chisel is in
        if (clickedSlot == chiselSlot || (clickTypeIn == ClickType.PICKUP && dragType == chiselSlot))
            return null;

        return super.func_184996_a(slotId, dragType, clickTypeIn, player);
    }

    @Override
    public void onContainerClosed(EntityPlayer entityplayer) {
        inventory.clearItems();
        super.onContainerClosed(entityplayer);
    }

    @Override
    public boolean canInteractWith(EntityPlayer entityplayer) {
        return inventory.isUseableByPlayer(entityplayer);
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer entity, int slotIdx) {
        ItemStack itemstack = null;
        Slot slot = (Slot) this.inventorySlots.get(slotIdx);

        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            if (slotIdx > InventoryChiselSelection.normalSlots) {
                if (!this.mergeItemStack(itemstack1, InventoryChiselSelection.normalSlots, InventoryChiselSelection.normalSlots + 1, false)) {
                    return null;
                }
            } else {
                if (slotIdx < InventoryChiselSelection.normalSlots + 1 && itemstack1 != null) {
                    entity.inventory.setItemStack(itemstack1.copy());
                    slot.onPickupFromSlot(entity, itemstack1);
                    itemstack1 = entity.inventory.getItemStack();
                    entity.inventory.setItemStack(null);
                }

                if (!this.mergeItemStack(itemstack1, InventoryChiselSelection.normalSlots + 1, InventoryChiselSelection.normalSlots + 1 + 36, false)) {
                    return null;
                }
            }
            slot.onSlotChange(itemstack1, itemstack);

            if (itemstack1.stackSize == 0) {
                slot.putStack((ItemStack) null);
            } else {
                slot.onSlotChanged();
            }
            if (itemstack1.stackSize == itemstack.stackSize) {
                return null;
            }
            if (slotIdx >= InventoryChiselSelection.normalSlots) {
                slot.onPickupFromSlot(entity, itemstack1);
            }
            if (itemstack1.stackSize == 0) {
                slot.putStack(null);
                return null;
            }
        }
        return itemstack;
    }

    public void onChiselSlotChanged() {
        ItemStack stack = playerInventory.mainInventory[chiselSlot];
        if (stack == null || !stack.isItemEqual(chisel))
            finished = true;

        if (finished)
            return;

        if (!chisel.hasTagCompound()) {
            chisel.setTagCompound(new NBTTagCompound());
        }
        NBTTagCompound targetTag = new NBTTagCompound();
        if (inventory.getStackInSpecialSlot() != null) {
            inventory.getStackInSpecialSlot().writeToNBT(targetTag);
        }
        chisel.getTagCompound().setTag("chiselTarget", targetTag);

        playerInventory.mainInventory[chiselSlot] = chisel;
    }

}
