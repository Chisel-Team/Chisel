package team.chisel.common.inventory;


import javax.annotation.Nonnull;

import net.minecraft.inventory.Slot;

public class SlotChiselInput extends Slot {

    private final ContainerChisel container;

    public SlotChiselInput(ContainerChisel container, @Nonnull InventoryChiselSelection inv, int i, int j, int k) {
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
