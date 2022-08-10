package team.chisel.common.inventory;

import net.minecraft.core.NonNullList;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import team.chisel.api.IChiselItem;
import team.chisel.common.item.ItemChisel;

import javax.annotation.Nullable;
import java.util.List;

public class InventoryChiselSelection implements Container {

    public final int size;
    public int activeVariations = 0;
    ItemStack chisel;
    @Nullable
    ChiselContainer container;
    NonNullList<ItemStack> inventory;

    public InventoryChiselSelection(ItemStack c, int size) {
        super();
        this.size = size;
        inventory = NonNullList.withSize(size + 1, ItemStack.EMPTY);
        chisel = c;
    }

    public void onInventoryUpdate() {
    }

    @Override
    public int getContainerSize() {
        return size + 1;
    }

    @Override
    public ItemStack getItem(int var1) {
        return inventory.get(var1);
    }

    public void updateInventoryState() {
        onInventoryUpdate();
    }

    @Override
    public ItemStack removeItem(int slot, int amount) {
        ItemStack stack = inventory.get(slot);
        if (!stack.isEmpty()) {
            if (stack.getCount() <= amount) {
                setItem(slot, ItemStack.EMPTY);
                updateInventoryState();
                return stack;
            } else {
                ItemStack split = stack.split(amount);

                if (stack.getCount() == 0) {
                    setItem(slot, ItemStack.EMPTY);
                }

                updateInventoryState();

                return split;
            }
        }
        return ItemStack.EMPTY;
    }

    @Override
    public ItemStack removeItemNoUpdate(int slot) {
        ItemStack stack = getItem(slot);

        setItem(slot, ItemStack.EMPTY);
        inventory.set(slot, ItemStack.EMPTY);

        updateInventoryState();
        return stack;
    }

    @Override
    public int getMaxStackSize() {
        return 64;
    }

    @Override
    public void setChanged() {

    }

    @Override
    public boolean stillValid(Player player) {
        assert container != null;
        ItemStack held = player.getInventory().getItem(container.getChiselSlot());
        return !held.isEmpty() && held.getItem() instanceof IChiselItem && ((IChiselItem) held.getItem()).canOpenGui(player.level, player, container.hand);
    }

    public void clearItems() {
        activeVariations = 0;
        for (int i = 0; i < size; i++) {
            setItem(i, ItemStack.EMPTY);
        }
    }

    public ItemStack getStackInSpecialSlot() {
        return inventory.get(size);
    }

    public void setStackInSpecialSlot(ItemStack stack) {
        setItem(size, stack);
    }

    public void updateItems() {
        ItemStack chiseledItem = getStackInSpecialSlot();
        clearItems();

        if (chiseledItem.isEmpty()) {
            return;
        }

        chiseledItem.getItem();

        assert container != null;
        List<ItemStack> list = container.getCarving().getItemsForChiseling(chiseledItem);

        activeVariations = 0;
        while (activeVariations < size && activeVariations < list.size()) {
            setItem(activeVariations, list.get(activeVariations));
            activeVariations++;
        }
    }

    @Override
    public void setItem(int slot, ItemStack stack) {
        inventory.set(slot, stack);
        updateInventoryState();
    }

    @Override
    public boolean canPlaceItem(int i, ItemStack stack) {
        return !(!stack.isEmpty() && (stack.getItem() instanceof ItemChisel)) && i == size;
    }

    @Override
    public void clearContent() {
        inventory.clear();
    }

    @Override
    public void startOpen(Player var1) {

    }

    @Override
    public void stopOpen(Player var1) {

    }

    @Override
    public boolean isEmpty() {
        return inventory.isEmpty();
    }
}