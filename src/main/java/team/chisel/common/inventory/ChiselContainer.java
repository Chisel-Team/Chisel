package team.chisel.common.inventory;

import javax.annotation.ParametersAreNonnullByDefault;

import lombok.Getter;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import team.chisel.Chisel;
import team.chisel.api.carving.CarvingUtils;
import team.chisel.api.carving.IVariationRegistry;
import team.chisel.common.util.NBTUtil;
import team.chisel.common.util.SoundUtil;

@Getter
public class ChiselContainer extends AbstractContainerMenu {

    protected final InventoryChiselSelection inventoryChisel;
    protected final Inventory inventoryPlayer;
    
    protected final InteractionHand hand;
    protected final int chiselSlot;
    protected final ItemStack chisel;
    protected final IVariationRegistry carving;
    protected Slot inputSlot;

    public ChiselContainer(MenuType<? extends ChiselContainer> type, int windowId, Inventory inventoryplayer) {
        this(type, windowId, inventoryplayer, new InventoryChiselSelection(ItemStack.EMPTY, 60), InteractionHand.MAIN_HAND);
    }
    
    public ChiselContainer(MenuType<? extends ChiselContainer> type, int windowId, Inventory inventoryplayer, InventoryChiselSelection inv, InteractionHand hand) {
        super(type, windowId);
        this.inventoryChisel = inv;
        this.inventoryPlayer = inventoryplayer;
        
        this.hand = hand;
        this.chiselSlot = hand == InteractionHand.MAIN_HAND ? inventoryplayer.selected : inventoryplayer.getContainerSize() - 1;
        this.chisel = inventoryplayer.getItem(chiselSlot);
        this.carving = CarvingUtils.getChiselRegistry();

        inv.container = this;

        addSlots();

        if (!chisel.isEmpty() && chisel.hasTag()) {
            ItemStack stack = NBTUtil.getChiselTarget(chisel);
            inventoryChisel.setItem(getInventoryChisel().size, stack);
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
    public void clicked(int slotId, int dragType, ClickType clickTypeIn, Player player) {
        if (clickTypeIn != ClickType.QUICK_CRAFT && slotId >= 0) {
            // we need to subtract away all the other slots
            int clickedSlot = slotId - inventoryChisel.getContainerSize() - 27;
            Chisel.debug("Slot clicked is " + slotId + " and slot length is " + slots.size());
            try {
                Slot slot = (Slot) slots.get(slotId);
                Chisel.debug("Slot is " + slot);
            } catch (Exception exception) {
                Chisel.debug("Exception getting slot");
                exception.printStackTrace();
            }

            // if the player has clicked on the chisel or is trying to use a number key to force an itemstack into the slot the chisel is in
            if (clickedSlot == chiselSlot || (clickTypeIn == ClickType.SWAP && !inventoryPlayer.items.get(dragType).isEmpty())) {
                return;
            }
        }
        
        this.currentClickType = clickTypeIn;
        super.clicked(slotId, dragType, clickTypeIn, player);
    }

    
    @Override
    public void removed(Player entityplayer) {
        inventoryChisel.clearItems();
        super.removed(entityplayer);
    }

    @Override
    public boolean stillValid(Player entityplayer) {
        return inventoryChisel.stillValid(entityplayer);
    }

    @Override
    public ItemStack quickMoveStack(Player entity, int slotIdx) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = (Slot) this.slots.get(slotIdx);

        if (slot != null && slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();

            if (slotIdx > getInventoryChisel().size) {
                if (!this.moveItemStackTo(itemstack1, getInventoryChisel().size, getInventoryChisel().size + 1, false)) {
                    return ItemStack.EMPTY;
                }
            } else {
                if (slotIdx < getInventoryChisel().size && !itemstack1.isEmpty()) {
                    SlotChiselSelection selectslot = (SlotChiselSelection) slot;
                    ItemStack check = SlotChiselSelection.craft(this, entity, itemstack1, true);
                    if (check.isEmpty()) {
                        return ItemStack.EMPTY;
                    }
                    if (!this.moveItemStackTo(check, getInventoryChisel().size + 1, getInventoryChisel().size + 1 + 36, true)) {
                        return ItemStack.EMPTY;
                    }
                    SoundUtil.playSound(entity, getChisel(), itemstack1);
                    itemstack1 = SlotChiselSelection.craft(this, entity, itemstack1, false);
                    itemstack1.shrink(check.getCount());
                    getInventoryChisel().setStackInSpecialSlot(check);

                } else if (!this.moveItemStackTo(itemstack1, getInventoryChisel().size + 1, getInventoryChisel().size + 1 + 36, true)) {
                    return ItemStack.EMPTY;
                }
            }
            
            boolean clearSlot = slotIdx >= getInventoryChisel().size || getInventoryChisel().getStackInSpecialSlot().isEmpty();

            slot.onQuickCraft(itemstack1, itemstack);

            if (itemstack1.isEmpty()) {
                if (clearSlot) {
                    slot.set(ItemStack.EMPTY);
                }
            } else {
                slot.setChanged();
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
                    slot.set(ItemStack.EMPTY);
                }
                return ItemStack.EMPTY;
            } else {
                if (!clearSlot) {
                    slot.set(itemstack);
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
        if (!getInventoryPlayer().player.level.isClientSide) {
            getInventoryPlayer().player.drop(inventoryChisel.getStackInSpecialSlot(), false);
            inventoryChisel.setStackInSpecialSlot(ItemStack.EMPTY);
        }
    }
}

