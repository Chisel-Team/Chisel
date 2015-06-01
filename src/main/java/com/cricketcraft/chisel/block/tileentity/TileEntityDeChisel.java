package com.cricketcraft.chisel.block.tileentity;

import com.cricketcraft.chisel.api.IChiselItem;
import com.cricketcraft.chisel.carving.Carving;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

public class TileEntityDeChisel extends TileEntity implements ISidedInventory{

    private final int CHISEL = 0, INPUT = 1;
    private ItemStack[] inventory = new ItemStack[2];

    private String name = "deChisel";

    @Override
    public void updateEntity(){
        if(worldObj.getWorldTime() % 20 == 0){
            if(inventory[CHISEL] != null && inventory[INPUT] != null){
                ItemStack target = inventory[INPUT];
                ItemStack output = new ItemStack(Carving.chisel.getGroupVariations(Block.getBlockFromItem(target.getItem()), target.getItemDamage()).get(0).getBlock(), target.stackSize);
                if(worldObj.getTileEntity(xCoord, yCoord - 1, zCoord) instanceof IInventory) {
                    IInventory inventory = (IInventory) worldObj.getTileEntity(xCoord, yCoord - 1, zCoord);
                    pushToThisInventory(inventory, output);
                }
            }
        }
    }

    private void pushToThisInventory(IInventory inv, ItemStack output){
        for(int slot = 0; slot < inv.getSizeInventory(); slot++){
            if(inv.getStackInSlot(slot) == null){
                setInventorySlotContents(slot, output);
            }
        }
    }

    public ItemStack[] getInventory() {
        return inventory;
    }

    @Override
    public int getSizeInventory() {
        return inventory.length;
    }

    @Override
    public ItemStack getStackInSlot(int slot) {
        return inventory[slot];
    }

    @Override
    public ItemStack decrStackSize(int slot, int size) {
        if (inventory[slot] != null) {
            ItemStack is;
            if (inventory[slot].stackSize <= size) {
                is = inventory[slot];
                inventory[slot] = null;
                return is;
            } else {
                is = inventory[slot].splitStack(size);
                if (inventory[slot].stackSize == 0)
                    inventory[slot] = null;
                return is;
            }
        } else
            return null;
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int slot) {
        if (inventory[slot] != null) {
            ItemStack is = inventory[slot];
            inventory[slot] = null;
            return is;
        } else
            return null;
    }

    @Override
    public void setInventorySlotContents(int slot, ItemStack stack) {
        inventory[slot] = stack;

        if (stack != null && stack.stackSize > getInventoryStackLimit()) {
            stack.stackSize = getInventoryStackLimit();
        }
    }

    @Override
    public String getInventoryName(){
        return name;
    }

    @Override
    public boolean canUpdate(){
        return true;
    }

    @Override
    public void readFromNBT(NBTTagCompound tagCompound){

    }

    @Override
    public void writeToNBT(NBTTagCompound tagCompound){

    }

    @Override
    public boolean canExtractItem(int slot, ItemStack stack, int side){
        return (side == 0 && slot == 1) ? true : false;
    }

    @Override
    public int[] getAccessibleSlotsFromSide(int side) {
        if(side == 0) return new int[] {INPUT};
        if(side == 1) return new int[] {CHISEL, INPUT};
        return new int[0];
    }

    @Override
    public boolean canInsertItem(int slot, ItemStack stack, int side){
        return side == 1 ? true : false;
    }

    @Override
    public void openInventory(){

    }

    @Override
    public void closeInventory(){

    }

    @Override
    public boolean isItemValidForSlot(int slot, ItemStack stack) {
        if(stack == null){
            return false;
        }
        switch (slot){
            case INPUT:
                return !Carving.chisel.getItemsForChiseling(stack).isEmpty();
            case CHISEL:
                return stack.getItem() instanceof IChiselItem;
            default:
                return false;
        }
    }

    @Override
    public int getInventoryStackLimit(){
        return 64;
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer player) {
        return true;
    }

    @Override
    public boolean hasCustomInventoryName(){
        return false;
    }
}
