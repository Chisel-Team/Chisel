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

    private final InventoryChiselSelection inventoryChisel;
    private final InventoryPlayer inventoryPlayer;
    
    private final int chiselSlot;
    private final ItemStack chisel;
    private final ICarvingRegistry carving;
    
    private final EnumHand hand;
    
    private boolean chiselExists = false;

    public ContainerChisel(InventoryPlayer inventoryplayer, InventoryChiselSelection inv, EnumHand hand) {
        this.inventoryChisel = inv;
        this.inventoryPlayer = inventoryplayer;
        
        this.chiselSlot = getInventoryPlayer().currentItem;
        this.chisel = inventoryplayer.player.getHeldItem(hand);
        this.carving = Carving.chisel;

        inv.container = this;
        this.hand = hand;

        int top = 8, left = 62;

        // selection slots
        for (int i = 0; i < InventoryChiselSelection.normalSlots; i++) {
            addSlotToContainer(new SlotChiselSelection(this, inventoryChisel, inventoryChisel, i, left + ((i % 10) * 18), top + ((i / 10) * 18)));
        }

        // main slot
        addSlotToContainer(new SlotChiselInput(this, inventoryChisel, InventoryChiselSelection.normalSlots, 24, 24));

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

        if (chisel != null && chisel.getTagCompound() != null) {
            ItemStack stack = ItemStack.loadItemStackFromNBT(chisel.getTagCompound().getCompoundTag("chiselTarget"));
            inventoryChisel.setInventorySlotContents(InventoryChiselSelection.normalSlots, stack);
        }
        
        inventoryChisel.updateItems();
    }

    @Override
    public ItemStack slotClick(int slotId, int dragType, ClickType clickTypeIn, EntityPlayer player) {
        if (clickTypeIn != ClickType.QUICK_CRAFT) {
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
            if (hand == EnumHand.MAIN_HAND && (clickedSlot == chiselSlot || (clickTypeIn == ClickType.SWAP && dragType == chiselSlot)))
                return null;
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

                if (!this.mergeItemStack(itemstack1, InventoryChiselSelection.normalSlots + 1, InventoryChiselSelection.normalSlots + 1 + 36, true)) {
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
