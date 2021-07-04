package team.chisel.common.inventory;

import javax.annotation.ParametersAreNonnullByDefault;

import lombok.Getter;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.ClickType;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import team.chisel.Chisel;
import team.chisel.api.carving.CarvingUtils;
import team.chisel.api.carving.IVariationRegistry;
import team.chisel.common.util.NBTUtil;
import team.chisel.common.util.SoundUtil;

@Getter
@ParametersAreNonnullByDefault
public class ChiselContainer extends Container {

    protected final InventoryChiselSelection inventoryChisel;
    protected final PlayerInventory inventoryPlayer;
    
    protected final Hand hand;
    protected final int chiselSlot;
    protected final ItemStack chisel;
    protected final IVariationRegistry carving;
    protected Slot inputSlot;

    public ChiselContainer(ContainerType<? extends ChiselContainer> type, int windowId, PlayerInventory inventoryplayer) {
        this(type, windowId, inventoryplayer, new InventoryChiselSelection(ItemStack.EMPTY, 60), Hand.MAIN_HAND);
    }
    
    public ChiselContainer(ContainerType<? extends ChiselContainer> type, int windowId, PlayerInventory inventoryplayer, InventoryChiselSelection inv, Hand hand) {
        super(type, windowId);
        this.inventoryChisel = inv;
        this.inventoryPlayer = inventoryplayer;
        
        this.hand = hand;
        this.chiselSlot = hand == Hand.MAIN_HAND ? inventoryplayer.currentItem : inventoryplayer.getSizeInventory() - 1;
        this.chisel = inventoryplayer.getStackInSlot(chiselSlot);
        this.carving = CarvingUtils.getChiselRegistry();

        inv.container = this;

        addSlots();

        if (!chisel.isEmpty() && chisel.hasTag()) {
            ItemStack stack = NBTUtil.getChiselTarget(chisel);
            inventoryChisel.setInventorySlotContents(getInventoryChisel().size, stack);
        }
        
        inventoryChisel.updateItems();
    }
    
    protected void addSlots() {
        int top = 8, left = 62;

        // selection slots
        for (int i = 0; i < getInventoryChisel().size; i++) {
            addSlot(new SlotChiselSelection(this, inventoryChisel, inventoryChisel, i, left + ((i % 10) * 18), top + ((i / 10) * 18)));
        }

        // main slot
        addSlot(inputSlot = new SlotChiselInput(this, inventoryChisel, getInventoryChisel().size, 24, 24));

        top += 112;
        left += 9;
        // main inv
        for (int i = 0; i < 27; i++) {
            addSlot(new Slot(inventoryPlayer, i + 9, left + ((i % 9) * 18), top + (i / 9) * 18));
        }

        top += 58;
        for (int i = 0; i < 9; i++) {
            addSlot(new Slot(inventoryPlayer, i, left + ((i % 9) * 18), top + (i / 9) * 18));
        }
    }
    
    ClickType currentClickType;

    @Override
    public ItemStack slotClick(int slotId, int dragType, ClickType clickTypeIn, PlayerEntity player) {
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
            if (clickedSlot == chiselSlot || clickTypeIn == ClickType.SWAP) {
                return ItemStack.EMPTY;
            }
        }
        
        this.currentClickType = clickTypeIn;
        return super.slotClick(slotId, dragType, clickTypeIn, player);
    }

    @Override
    public void onContainerClosed(PlayerEntity entityplayer) {
        inventoryChisel.clearItems();
        super.onContainerClosed(entityplayer);
    }

    @Override
    public boolean canInteractWith(PlayerEntity entityplayer) {
        return inventoryChisel.isUsableByPlayer(entityplayer);
    }

    @Override
    public ItemStack transferStackInSlot(PlayerEntity entity, int slotIdx) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = (Slot) this.inventorySlots.get(slotIdx);

        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            if (slotIdx > getInventoryChisel().size) {
                if (!this.mergeItemStack(itemstack1, getInventoryChisel().size, getInventoryChisel().size + 1, false)) {
                    return ItemStack.EMPTY;
                }
            } else {
                if (slotIdx < getInventoryChisel().size && !itemstack1.isEmpty()) {
                    SlotChiselSelection selectslot = (SlotChiselSelection) slot;
                    ItemStack check = SlotChiselSelection.craft(this, entity, itemstack1, true);
                    if (check.isEmpty()) {
                        return ItemStack.EMPTY;
                    }
                    if (!this.mergeItemStack(check, getInventoryChisel().size + 1, getInventoryChisel().size + 1 + 36, true)) {
                        return ItemStack.EMPTY;
                    }
                    SoundUtil.playSound(entity, getChisel(), itemstack1);
                    itemstack1 = SlotChiselSelection.craft(this, entity, itemstack1, false);
                    itemstack1.shrink(check.getCount());
                    getInventoryChisel().setStackInSpecialSlot(check);

                } else if (!this.mergeItemStack(itemstack1, getInventoryChisel().size + 1, getInventoryChisel().size + 1 + 36, true)) {
                    return ItemStack.EMPTY;
                }
            }
            
            boolean clearSlot = slotIdx >= getInventoryChisel().size || getInventoryChisel().getStackInSpecialSlot().isEmpty();

            slot.onSlotChange(itemstack1, itemstack);

            if (itemstack1.isEmpty()) {
                if (clearSlot) {
                    slot.putStack(ItemStack.EMPTY);
                }
            } else {
                slot.onSlotChanged();
            }

            getInventoryChisel().updateItems();

            if (itemstack1.getCount() == itemstack.getCount()) {
                return ItemStack.EMPTY;
            }
            if (slotIdx >= getInventoryChisel().size) {
                slot.onTake(entity, itemstack1);
            }
            if (itemstack1.isEmpty()) {
                if (clearSlot) {
                    slot.putStack(ItemStack.EMPTY);
                }
                return ItemStack.EMPTY;
            } else {
                if (!clearSlot) {
                    slot.putStack(itemstack1);
                }
                return itemstack1;
            }
        }
        return itemstack;
    }

    public void onChiselSlotChanged() {
        NBTUtil.setChiselTarget(chisel, inventoryChisel.getStackInSpecialSlot());
    }

    public void onChiselBroken() {
        if (!getInventoryPlayer().player.world.isRemote) {
            getInventoryPlayer().player.dropItem(inventoryChisel.getStackInSpecialSlot(), false);
            inventoryChisel.setStackInSpecialSlot(ItemStack.EMPTY);
        }
    }
}

