package team.chisel.common.inventory;

import team.chisel.common.item.ItemChisel;
import team.chisel.common.util.OreDictionaryUtil;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.IChatComponent;

import java.util.List;

public class InventoryChiselSelection implements IInventory {

    ItemStack chisel = null;
    public final static int normalSlots = 60;
    public int activeVariations = 0;
    ContainerChisel container;
    ItemStack[] inventory;

    public InventoryChiselSelection(ItemStack c) {
        super();

        inventory = new ItemStack[normalSlots + 1];
        chisel = c;
        if (chisel.getTagCompound() !=null && chisel.getTagCompound().getTag("chiselTarget") != null) {
            ItemStack target = ItemStack.loadItemStackFromNBT(chisel.getTagCompound().getCompoundTag("chiselTarget"));
            if (target != null) {
                inventory[normalSlots] = target;
            }
        }
    }

    public void onInventoryUpdate(int slot) {
    }

    @Override
    public int getSizeInventory() {
        return normalSlots + 1;
    }

    @Override
    public ItemStack getStackInSlot(int var1) {
        return inventory[var1];
    }

    public void updateInventoryState(int slot) {
        onInventoryUpdate(slot);
    }

    @Override
    public ItemStack decrStackSize(int slot, int amount) {
        if (this.inventory[slot] != null) {
            ItemStack stack;
            if (this.inventory[slot].stackSize <= amount) {
                stack = this.inventory[slot];
                this.inventory[slot] = null;
                updateInventoryState(slot);
                return stack;
            } else {
                stack = this.inventory[slot].splitStack(amount);

                if (this.inventory[slot].stackSize == 0)
                    this.inventory[slot] = null;

                updateInventoryState(slot);

                return stack;
            }
        } else
            return null;
    }

    @Override
    public String getCommandSenderName() {
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
    public IChatComponent getDisplayName() {
        return new ChatComponentTranslation(getCommandSenderName());
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer entityplayer) {
        return true;
    }

    public void clearItems() {
        activeVariations = 0;
        for (int i = 0; i < normalSlots; i++) {
            inventory[i] = null;
        }
    }

    public ItemStack getStackInSpecialSlot() {
        return inventory[normalSlots];
    }

    public void updateItems() {
        ItemStack chiseledItem = inventory[normalSlots];
        clearItems();

        if (chiseledItem == null) {
            container.onChiselSlotChanged();
            return;
        }

        Item item = chiseledItem.getItem();
        if (item == null)
            return;

        if (Block.getBlockFromItem(item) == null)
            return;

        List<ItemStack> list = OreDictionaryUtil.getVariations(chiseledItem);

        activeVariations = 0;
        while (activeVariations < normalSlots && activeVariations < list.size()) {
            if (Block.blockRegistry.getNameForObject(Block.getBlockFromItem(list.get(activeVariations).getItem())) != null) {
                inventory[activeVariations] = list.get(activeVariations);
                activeVariations++;
            }
        }

        container.onChiselSlotChanged();
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int slot) {
        ItemStack stack = getStackInSlot(slot);

        if (stack == null)
            return null;
        NBTTagCompound targetItem = new NBTTagCompound();
        inventory[slot].writeToNBT(targetItem);
        chisel.getTagCompound().setTag("chiselTarget", targetItem);
        inventory[slot] = null;

        updateInventoryState(slot);
        return stack;
    }

    @Override
    public void setInventorySlotContents(int slot, ItemStack stack) {
        inventory[slot] = stack;
        updateInventoryState(slot);
    }


    @Override
    public boolean isItemValidForSlot(int i, ItemStack stack) {
        //Really didn't think people would chisel a shovel
        if (stack.getItem() instanceof ItemTool) {
            return false;
        }

        return !(stack != null && (stack.getItem() instanceof ItemChisel)) && i == normalSlots;
    }

    @Override
    public void clear() {
        inventory = new ItemStack[inventory.length];
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
    public void openInventory(EntityPlayer var1) {

    }

    @Override
    public void closeInventory(EntityPlayer var1) {

    }

}