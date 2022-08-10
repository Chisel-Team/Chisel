package team.chisel.common.inventory;


import net.minecraft.world.inventory.Slot;

import javax.annotation.Nonnull;

public class SlotChiselInput extends Slot {

    private final ChiselContainer container;

    public SlotChiselInput(ChiselContainer container, @Nonnull InventoryChiselSelection inv, int i, int j, int k) {
        super(inv, i, j, k);
        this.container = container;
    }

    @Override
    public void setChanged() {
        super.setChanged();
        container.onChiselSlotChanged();
        container.getInventoryChisel().updateItems();
    }
}
