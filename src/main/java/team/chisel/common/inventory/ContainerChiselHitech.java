package team.chisel.common.inventory;

import lombok.Getter;
import lombok.Setter;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;

@Getter
@Setter
public class ContainerChiselHitech extends ContainerChisel {

    private Slot selection, target;
    
    public ContainerChiselHitech(InventoryPlayer inventoryplayer, InventoryChiselSelection inv, EnumHand hand) {
        super(inventoryplayer, inv, hand);
    }

    @Override
    protected void addSlots() {
        int top = 8, left = 88;

        // selection slots
        for (int i = 0; i < getInventoryChisel().size; i++) {
            addSlotToContainer(new SlotChiselSelection(this, inventoryChisel, inventoryChisel, i, left + ((i % 9) * 18), top + ((i / 9) * 18)));
        }

        // main slot
        addSlotToContainer(new SlotChiselInput(this, inventoryChisel, getInventoryChisel().size, -1000, 0));

        top += 130;
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
        if (slotId >= 0) {
            Slot slot = getSlot(slotId);
            if (slotId < getInventoryChisel().size) {
                target = slot;
            } else {
                target = null;
                selection = slot;
                ItemStack stack = slot.getStack();
                getInventoryChisel().setInventorySlotContents(getInventoryChisel().size, stack == null ? null : stack.copy());
                getInventoryChisel().updateItems();
            }
        }
        return null;
    }
}
