package info.jbcs.minecraft.chisel.inventory;

import net.minecraft.inventory.ContainerChest;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;

public class ContainerPresent extends ContainerChest {
    private IInventory lower;
    private int rows;

    public ContainerPresent(IInventory player, IInventory chest) {
        super(player, chest);
        lower = chest;
        rows = chest.getSizeInventory() / 9;
        chest.openInventory();
        int a = (rows - 4) * 18;

        for (int d = 0; d < rows; d++) {
            for (int e = 0; e < 9; e++) {
                addSlotToContainer(new Slot(chest, e + d * 9, 8 + e * 18, 18 + d * 18));
            }
        }

        for (int d = 0; d < 3; d++) {
            for (int e = 0; e < 9; e++) {
                addSlotToContainer(new Slot(player, e + d * 9 + 9, 8 + e * 18, 103 + d * 18 + a));
            }
        }

        for (int d = 0; d < 9; d++) {
            addSlotToContainer(new Slot(player, d, 8 + d * 18, 161 + a));
        }
    }

    public IInventory getLowerPresentInventory() {
        return lower;
    }
}
