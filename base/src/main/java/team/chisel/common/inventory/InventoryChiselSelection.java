package team.chisel.common.inventory;

import java.util.List;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.Container;
import net.minecraft.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.NonNullList;
import team.chisel.api.IChiselItem;
import team.chisel.common.item.ItemChisel;

@ParametersAreNonnullByDefault
public class InventoryChiselSelection implements Container {

    ItemStack chisel = ItemStack.EMPTY;
    public final int size;
    public int activeVariations = 0;
    @Nullable ChiselContainer container;
    NonNullList<ItemStack> inventory;

    public InventoryChiselSelection(ItemStack c, int size) {
        super();
        this.size = size;
        inventory = NonNullList.withSize(size + 1, ItemStack.EMPTY);
        chisel = c;
    }

    public void onInventoryUpdate(int slot) {
    }

    @Override
    public int getContainerSize() {
        return size + 1;
    }

    @Override
    public ItemStack getItem(int var1) {
        return inventory.get(var1);
    }

    public void updateInventoryState(int slot) {
        onInventoryUpdate(slot);
    }

    @Override
    public ItemStack removeItem(int slot, int amount) {
        ItemStack stack = inventory.get(slot);
        if (!stack.isEmpty()) {
            if (stack.getCount() <= amount) {
                setItem(slot, ItemStack.EMPTY);
                updateInventoryState(slot);
                return stack;
            } else {
                ItemStack split = stack.split(amount);

                if (stack.getCount() == 0) {
                    setItem(slot, ItemStack.EMPTY);
                }
                
                updateInventoryState(slot);

                return split;
            }
        }
        return ItemStack.EMPTY;
    }

    @Override
    public ItemStack removeItemNoUpdate(int slot){
        ItemStack stack = getItem(slot);

        setItem(slot, ItemStack.EMPTY);
        inventory.set(slot, ItemStack.EMPTY);

        updateInventoryState(slot);
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
        ItemStack held = player.inventory.getItem(container.getChiselSlot());
        return !held.isEmpty() && held.getItem() instanceof IChiselItem && ((IChiselItem)held.getItem()).canOpenGui(player.world, player, container.hand);
    }

    public void clearItems() {
        activeVariations = 0;
        for (int i = 0; i < size; i++) {
            setInventorySlotContents(i, ItemStack.EMPTY);
        }
    }

    public ItemStack getStackInSpecialSlot() {
        return inventory.get(size);
    }
    
    public void setStackInSpecialSlot(ItemStack stack) {
        setInventorySlotContents(size, stack);
    }

    public void updateItems() {
        ItemStack chiseledItem = getStackInSpecialSlot();
        clearItems();

        if (chiseledItem.isEmpty()) {
            return;
        }

        Item item = chiseledItem.getItem();

        List<ItemStack> list = container.getCarving().getItemsForChiseling(chiseledItem);

        activeVariations = 0;
        while (activeVariations < size && activeVariations < list.size()) {
            setInventorySlotContents(activeVariations, list.get(activeVariations));
            activeVariations++;
        }
    }

    @Override
    public void setInventorySlotContents(int slot, ItemStack stack) {
        inventory.set(slot, stack);
        updateInventoryState(slot);
    }


    @Override
    public boolean isItemValidForSlot(int i, ItemStack stack) {
        return !(!stack.isEmpty() && (stack.getItem() instanceof ItemChisel)) && i == size;
    }

    @Override
    public void clear() {
        inventory.clear();
    }

    @Override
    public void openInventory(PlayerEntity var1) {

    }

    @Override
    public void closeInventory(PlayerEntity var1) {

    }

    @Override
    public boolean isEmpty() {
        return inventory.isEmpty();
    }
}