package team.chisel.common.inventory;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import team.chisel.api.carving.ICarvingGroup;
import team.chisel.common.util.NBTUtil;

import javax.annotation.Nullable;
import java.util.List;

@Getter
@Setter
public class ContainerChiselHitech extends ChiselContainer {

    private @Nullable Slot selection, target;
    private List<Slot> selectionDuplicates = ImmutableList.of();
    private @Nullable ICarvingGroup currentGroup;

    public ContainerChiselHitech(MenuType<? extends ChiselContainer> type, int windowId, Inventory inventoryplayer) {
        this(type, windowId, inventoryplayer, new InventoryChiselSelection(ItemStack.EMPTY, 63), InteractionHand.MAIN_HAND);
    }

    public ContainerChiselHitech(MenuType<? extends ChiselContainer> type, int windowId, Inventory inventoryplayer, InventoryChiselSelection inv, InteractionHand hand) {
        super(type, windowId, inventoryplayer, inv, hand);

        int selectionSlot = NBTUtil.getHitechSelection(chisel);
        if (selectionSlot >= inv.getContainerSize()) {
            setSelection(getSlot(selectionSlot));
        }

        int targetSlot = NBTUtil.getHitechTarget(chisel);
        if (targetSlot >= 0 && targetSlot < inv.getContainerSize() - 1) {
            setTarget(getSlot(targetSlot));
        }
    }

    public void setTarget(@Nullable Slot slot) {
        this.target = slot;
        NBTUtil.setHitechTarget(chisel, Optional.fromNullable(getTarget()).transform(s -> s.index).or(-1));
    }

    public void setSelection(@Nullable Slot slot) {
        this.selection = slot;

        if (slot == null || !slot.hasItem()) {
            currentGroup = null;
            selectionDuplicates = ImmutableList.of();
            setTarget(null);
        } else {
            ImmutableList.Builder<Slot> builder = ImmutableList.builder();
            for (int i = getInventoryChisel().size + 1; i < slots.size(); i++) {
                Slot s = getSlot(i);
                if (slot != s && ItemStack.isSame(slot.getItem(), s.getItem())) {
                    builder.add(s);
                }
            }
            selectionDuplicates = builder.build();

            ICarvingGroup group = carving.getGroup(slot.getItem().getItem()).orElse(null);
            if (currentGroup != null && group != currentGroup) {
                setTarget(null);
            }
            currentGroup = group;
        }

        ItemStack stack = slot == null ? ItemStack.EMPTY : slot.getItem();
        getInventoryChisel().setStackInSpecialSlot(stack);
        getInventoryChisel().updateItems();
        NBTUtil.setHitechSelection(chisel, Optional.fromNullable(getSelection()).transform(s -> s.index).or(-1));
    }

    public @Nullable ItemStack getSelectionStack() {
        Slot slot = getSelection();
        return slot == null ? ItemStack.EMPTY : slot.getItem();
    }

    public ItemStack getTargetItem() {
        Slot slot = getTarget();
        return slot == null ? ItemStack.EMPTY : slot.getItem();
    }

    @Override
    protected void addSlots() {
        int top = 8, left = 88;

        // selection slots
        for (int i = 0; i < getInventoryChisel().size; i++) {
            addSlot(new SlotChiselSelection(this, inventoryChisel, inventoryChisel, i, left + ((i % 9) * 18), top + ((i / 9) * 18)));
        }

        // main slot
        addSlot(inputSlot = new SlotChiselInput(this, inventoryChisel, getInventoryChisel().size, -1000, 0));

        top += 130;
        // main inv
        for (int i = 0; i < 27; i++) {
            addSlot(new Slot(inventoryPlayer, i + 9, left + ((i % 9) * 18), top + (i / 9) * 18));
        }

        top += 58;
        for (int i = 0; i < 9; i++) {
            addSlot(new Slot(inventoryPlayer, i, left + ((i % 9) * 18), top + (i / 9) * 18));
        }
    }

    @Override
    public void removed(Player entityplayer) {
        NBTUtil.setChiselTarget(getChisel(), getTargetItem());
        super.removed(entityplayer);
    }

    @Override
    public void clicked(int slotId, int dragType, ClickType clickTypeIn, Player player) {
        if (slotId >= 0) {
            Slot slot = getSlot(slotId);
            if (slotId < getInventoryChisel().size) {
                setTarget(slot);
            } else if (dragType == 1) {
                ItemStack toFind = slot.getItem();
                if (!toFind.isEmpty()) {
                    for (int i = 0; i < getInventoryChisel().size; i++) {
                        if (ItemStack.isSame(toFind, getInventoryChisel().getItem(i))) {
                            setTarget(getSlot(i));
                        }
                    }
                }
            } else {
                setSelection(slot);
            }
        }
    }
}
