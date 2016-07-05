package team.chisel.common.inventory;

import lombok.Getter;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumHand;
import team.chisel.Chisel;
import team.chisel.api.carving.ICarvingRegistry;
import team.chisel.common.carving.Carving;

@Getter
public class ContainerChisel extends Container {

    protected final InventoryChiselSelection inventoryChisel;
    protected final InventoryPlayer inventoryPlayer;
    
    protected final int chiselSlot;
    protected final ItemStack chisel;
    protected final ICarvingRegistry carving;
    
    private boolean chiselExists = false;

    public ContainerChisel(InventoryPlayer inventoryplayer, InventoryChiselSelection inv, EnumHand hand) {
        this.inventoryChisel = inv;
        this.inventoryPlayer = inventoryplayer;
        
        this.chiselSlot = hand == EnumHand.MAIN_HAND ? inventoryplayer.currentItem : inventoryplayer.getSizeInventory() - 1;
        this.chisel = inventoryplayer.getStackInSlot(chiselSlot);
        this.carving = Carving.chisel;

        inv.container = this;

        addSlots();

        if (chisel != null && chisel.getTagCompound() != null) {
            ItemStack stack = ItemStack.loadItemStackFromNBT(chisel.getTagCompound().getCompoundTag("chiselTarget"));
            inventoryChisel.setInventorySlotContents(getInventoryChisel().size, stack);
        }
        
        inventoryChisel.updateItems();
    }
    
    protected void addSlots() {
        int top = 8, left = 62;

        // selection slots
        for (int i = 0; i < getInventoryChisel().size; i++) {
            addSlotToContainer(new SlotChiselSelection(this, inventoryChisel, inventoryChisel, i, left + ((i % 10) * 18), top + ((i / 10) * 18)));
        }

        // main slot
        addSlotToContainer(new SlotChiselInput(this, inventoryChisel, getInventoryChisel().size, 24, 24));

        top += 112;
        left += 9;
        // main inv
        for (int i = 0; i < 27; i++) {
            addSlotToContainer(new Slot(inventoryPlayer, i + 9, left + ((i % 9) * 18), top + (i / 9) * 18));
        }

        top += 58;
        for (int i = 0; i < 9; i++) {
            addSlotToContainer(new Slot(inventoryPlayer, i, left + ((i % 9) * 18), top + (i / 9) * 18));
        }
    }

    @Override
    public ItemStack slotClick(int slotId, int dragType, ClickType clickTypeIn, EntityPlayer player) {
        if (clickTypeIn != ClickType.QUICK_CRAFT && slotId >= 0) {
            // we need to subtract away all the other slots
            int clickedSlot = slotId - inventoryChisel.getSizeInventory() - 27;
            Chisel.debug("Slot clicked is " + slotId + " and slot length is " + inventorySlots.size());
            try {
                Slot slot = (Slot) inventorySlots.get(slotId);
                Chisel.debug("Slot is " + slot);
            } catch (Exception exception) {
                Chisel.debug("Exception getting slot");
                exception.printStackTrace();
            }

            // if the player has clicked on the chisel or is trying to use a number key to force an itemstack into the slot the chisel is in
            if (clickedSlot == chiselSlot || (clickTypeIn == ClickType.SWAP && dragType == chiselSlot)) {
                return null;
            }
        }
        
        return super.slotClick(slotId, dragType, clickTypeIn, player);
    }

    @Override
    public void onContainerClosed(EntityPlayer entityplayer) {
        inventoryChisel.clearItems();
        if (!chiselExists && inventoryChisel.getStackInSpecialSlot() != null) {
            entityplayer.dropItem(inventoryChisel.getStackInSpecialSlot(), false);
        }
        super.onContainerClosed(entityplayer);
    }

    @Override
    public boolean canInteractWith(EntityPlayer entityplayer) {
        return chiselExists = inventoryChisel.isUseableByPlayer(entityplayer);
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer entity, int slotIdx) {
        ItemStack itemstack = null;
        Slot slot = (Slot) this.inventorySlots.get(slotIdx);

        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            if (slotIdx > getInventoryChisel().size) {
                if (!this.mergeItemStack(itemstack1, getInventoryChisel().size, getInventoryChisel().size + 1, false)) {
                    return null;
                }
            } else {
                if (slotIdx < getInventoryChisel().size + 1 && itemstack1 != null) {
                    entity.inventory.setItemStack(itemstack1.copy());
                    slot.onPickupFromSlot(entity, itemstack1);
                    itemstack1 = entity.inventory.getItemStack();
                    entity.inventory.setItemStack(null);
                }

                if (!this.mergeItemStack(itemstack1, getInventoryChisel().size + 1, getInventoryChisel().size + 1 + 36, true)) {
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
            if (slotIdx >= getInventoryChisel().size) {
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
        if (!chisel.hasTagCompound()) {
            chisel.setTagCompound(new NBTTagCompound());
        }
        NBTTagCompound targetTag = new NBTTagCompound();
        if (inventoryChisel.getStackInSpecialSlot() != null) {
            inventoryChisel.getStackInSpecialSlot().writeToNBT(targetTag);
        }
        chisel.getTagCompound().setTag("chiselTarget", targetTag);
    }

    
}
