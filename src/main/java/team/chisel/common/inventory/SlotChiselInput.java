package team.chisel.common.inventory;


import net.minecraft.inventory.Slot;

public class SlotChiselInput extends Slot {

    private final ContainerChisel container;

    public SlotChiselInput(ContainerChisel container, InventoryChiselSelection inv, int i, int j, int k) {
        super(inv, i, j, k);
        this.container = container;
    }

    @Override
    public void onSlotChanged() {
        super.onSlotChanged();
        container.getInventoryChisel().updateItems();
    }
}
