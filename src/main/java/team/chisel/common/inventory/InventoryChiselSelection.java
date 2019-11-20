package team.chisel.common.inventory;

import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import team.chisel.api.IChiselItem;
import team.chisel.common.item.ItemChisel;

@ParametersAreNonnullByDefault
public class InventoryChiselSelection implements IInventory {

    ItemStack chisel = ItemStack.EMPTY;
    public final int size;
    public int activeVariations = 0;
    @Nullable ContainerChisel container;
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
    public int getSizeInventory() {
        return size + 1;
    }

    @Override
    public ItemStack getStackInSlot(int var1) {
        return inventory.get(var1);
    }

    public void updateInventoryState(int slot) {
        onInventoryUpdate(slot);
    }

    @Override
    public ItemStack decrStackSize(int slot, int amount) {
        ItemStack stack = inventory.get(slot);
        if (!stack.isEmpty()) {
            if (stack.getCount() <= amount) {
                setInventorySlotContents(slot, ItemStack.EMPTY);
                updateInventoryState(slot);
                return stack;
            } else {
                ItemStack split = stack.splitStack(amount);

                if (stack.getCount() == 0) {
                    setInventorySlotContents(slot, ItemStack.EMPTY);
                }
                
                updateInventoryState(slot);

                return split;
            }
        }
        return ItemStack.EMPTY;
    }

    @Override
    public ItemStack removeStackFromSlot(int slot){
        ItemStack stack = getStackInSlot(slot);

        setInventorySlotContents(slot, ItemStack.EMPTY);
        inventory.set(slot, ItemStack.EMPTY);

        updateInventoryState(slot);
        return stack;
    }

    @Override
    public String getName() {
        return "container.chisel";
    }

    @Override
    public boolean hasCustomName() {
        return false;
    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    @Override
    public void markDirty() {

    }

    @Override
    public @Nonnull ITextComponent getDisplayName() {
        return new TextComponentTranslation(getName());
    }

    @Override
    public boolean isUsableByPlayer(PlayerEntity player) {
        ItemStack held = player.inventory.getStackInSlot(container.getChiselSlot());
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
    public int getField(int var1) {
        return var1;
    }

    @Override
    public void setField(int var1, int var2) {
    }

    @Override
    public int getFieldCount() {
        return 0;
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