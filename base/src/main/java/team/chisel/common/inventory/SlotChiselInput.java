package team.chisel.common.inventory;


import javax.annotation.Nonnull;

import net.minecraft.inventory.container.Slot;

public class SlotChiselInput extends Slot {

    private final ChiselContainer container;

    public SlotChiselInput(ChiselContainer container, @Nonnull InventoryChiselSelection inv, int i, int j, int k) {
        super(inv, i, j, k);
        this.container = container;
    }

    @Override
    public void onSlotChanged() {
        super.onSlotChanged();
        container.onChiselSlotChanged();
        container.getInventoryChisel().updateItems();
    }
}
