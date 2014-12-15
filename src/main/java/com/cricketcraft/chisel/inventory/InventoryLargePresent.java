package com.cricketcraft.chisel.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

public class InventoryLargePresent implements IInventory {
    private String name;
    private IInventory upper, lower;

    public InventoryLargePresent(String name, IInventory upper, IInventory lower) {
        this.name = name;

        if (upper == null) {
            upper = lower;
        }

        if (lower == null) {
            lower = upper;
        }

        this.upper = upper;
        this.lower = lower;
    }

    @Override
    public int getSizeInventory() {
        return upper.getSizeInventory() + lower.getSizeInventory();
    }

    public boolean isPartOfLargePresent(IInventory inventory) {
        return upper == inventory || lower == inventory;
    }

    @Override
    public ItemStack getStackInSlot(int slot) {
        return slot >= upper.getSizeInventory() ? lower.getStackInSlot(slot - upper.getSizeInventory()) : upper.getStackInSlot(slot);
    }

    @Override
    public ItemStack decrStackSize(int slot, int size) {
        return slot >= upper.getSizeInventory() ? lower.getStackInSlot(slot - upper.getSizeInventory()) : upper.getStackInSlot(slot);
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int slot) {
        return slot >= upper.getSizeInventory() ? lower.getStackInSlotOnClosing(slot - upper.getSizeInventory()) : upper.getStackInSlotOnClosing(slot);
    }

    @Override
    public void setInventorySlotContents(int slot, ItemStack stack) {
        if (slot >= upper.getSizeInventory()) {
            lower.setInventorySlotContents(slot - upper.getSizeInventory(), stack);
        } else {
            upper.setInventorySlotContents(slot, stack);
        }
    }

    @Override
    public String getInventoryName() {
        return upper.hasCustomInventoryName() ? upper.getInventoryName() : (lower.hasCustomInventoryName() ? lower.getInventoryName() : name);
    }

    @Override
    public boolean hasCustomInventoryName() {
        return upper.hasCustomInventoryName() || lower.hasCustomInventoryName();
    }

    @Override
    public int getInventoryStackLimit() {
        return upper.getInventoryStackLimit();
    }

    @Override
    public void markDirty() {
        upper.markDirty();
        lower.markDirty();
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer player) {
        return upper.isUseableByPlayer(player) && lower.isUseableByPlayer(player);
    }

    @Override
    public void openInventory() {
        upper.openInventory();
        lower.openInventory();
    }

    @Override
    public void closeInventory() {
        upper.closeInventory();
        ;
        lower.closeInventory();
    }

    @Override
    public boolean isItemValidForSlot(int slot, ItemStack stack) {
        return true;
    }
}
