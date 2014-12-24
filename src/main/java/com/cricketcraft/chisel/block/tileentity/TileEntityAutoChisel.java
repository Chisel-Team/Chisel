package com.cricketcraft.chisel.block.tileentity;

import com.cricketcraft.chisel.init.ModItems;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;

public class TileEntityAutoChisel extends TileEntity implements ISidedInventory {

    private final int BASE = 0, TARGET = 1, OUTPUT = 2, AUTOMATION = 3, STACK = 4, SPEED = 5;
    private boolean isAutomated, isStackMode, isFaster;
    private static EntityItem ghostItem;
    boolean equal = false;
    private ItemStack[] inventory = new ItemStack[6];
    private String name = "autoChisel";

    @Override
    public boolean canUpdate() {
        return true;
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
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        NBTTagList tags = nbt.getTagList("Items", 10);
        inventory = new ItemStack[getSizeInventory()];

        for (int i = 0; i < tags.tagCount(); i++) {
            NBTTagCompound data = tags.getCompoundTagAt(i);
            int j = data.getByte("Slot") & 255;

            if (j >= 0 && j < inventory.length) {
                inventory[j] = ItemStack.loadItemStackFromNBT(data);
            }
        }

        if (nbt.hasKey("CustomName", 8)) {
            this.name = nbt.getString("CustomName");
        }
    }

    @Override
    public void updateEntity() {

        if(inventory[SPEED] == new ItemStack(ModItems.upgrade, 1, 0)){
            isFaster = true;
        } else {
            isFaster = false;
        }

        if(inventory[AUTOMATION] == new ItemStack(ModItems.upgrade, 1, 1)){
            isAutomated = true;
        } else {
            isAutomated = false;
        }

        if(inventory[STACK] == new ItemStack(ModItems.upgrade, 1, 2)){
            isStackMode = true;
        } else {
            isStackMode = false;
        }

        if(isFaster){
            if(!worldObj.isRemote && worldObj.getWorldTime() % 20 == 0){
                if(inventory[BASE] != null && inventory[TARGET] != null){
                    if(inventory[BASE].getItem() instanceof ItemBlock && inventory[TARGET].getItem() instanceof ItemBlock){
                        if(inventory[BASE].getUnlocalizedName().equalsIgnoreCase(inventory[TARGET].getUnlocalizedName())){
                            if(isStackMode){
                                if(inventory[BASE].stackSize == inventory[BASE].getMaxStackSize()){
                                    if(inventory[OUTPUT] == null){
                                        inventory[OUTPUT] = inventory[BASE];
                                        inventory[BASE] = null;
                                    }
                                }
                            } else {
                                if(inventory[OUTPUT] == null){
                                    inventory[OUTPUT] = inventory[BASE];
                                    inventory[BASE].stackSize--;
                                } else if(inventory[BASE].stackSize != 0){
                                    inventory[OUTPUT].stackSize++;
                                    inventory[BASE].stackSize--;
                                } else {
                                    inventory[BASE] = null;
                                }
                            }
                        }
                    }
                }
            }
        } else {
            if(!worldObj.isRemote && worldObj.getWorldTime() % 100 == 0){
                if(inventory[BASE] != null && inventory[TARGET] != null){
                    if(inventory[BASE].getItem() instanceof ItemBlock && inventory[TARGET].getItem() instanceof ItemBlock){
                        if(inventory[BASE].getUnlocalizedName().equalsIgnoreCase(inventory[TARGET].getUnlocalizedName())){
                            if(isStackMode){
                                if(inventory[BASE].stackSize == inventory[BASE].getMaxStackSize()){
                                    if(inventory[OUTPUT] == null){
                                        inventory[OUTPUT] = inventory[BASE];
                                        inventory[BASE] = null;
                                    }
                                }
                            } else {
                                if(inventory[OUTPUT] == null){
                                    inventory[OUTPUT] = inventory[BASE];
                                    inventory[BASE].stackSize--;
                                } else if(inventory[BASE].stackSize != 0){
                                    inventory[OUTPUT].stackSize++;
                                    inventory[BASE].stackSize--;
                                } else {
                                    inventory[BASE] = null;
                                }
                            }
                        }
                    }
                }
            }
        }

        markDirty();
    }

    @Override
    public void writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        NBTTagList tags = new NBTTagList();

        for (int i = 0; i < inventory.length; i++) {
            if (inventory[i] != null) {
                NBTTagCompound data = new NBTTagCompound();
                data.setByte("Slot", (byte) i);
                inventory[i].writeToNBT(data);
                tags.appendTag(data);
            }
        }

        nbt.setTag("Items", tags);

        if (this.hasCustomInventoryName()) {
            nbt.setString("CustomName", this.name);
        }
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
        markDirty();
    }

    @Override
    public String getInventoryName() {
        return name;
    }

    @Override
    public final boolean isUseableByPlayer(EntityPlayer player) {
        return true;
    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    @Override
    public boolean hasCustomInventoryName() {
        return false;
    }

    @Override
    public int[] getAccessibleSlotsFromSide(int side) {
        return new int[6];
    }

    @Override
    public boolean isItemValidForSlot(int slot, ItemStack itemStack) {
        return true;
    }

    @Override
    public boolean canInsertItem(int slot, ItemStack itemStack, int side) {
        if(isAutomated){
            if(side != 0 || side != 1 && slot == BASE){
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean canExtractItem(int slot, ItemStack itemStack, int side) {
        if(isAutomated){
            if(side == 0 || side == 1 && slot == OUTPUT){
                return true;
            }
        }
        return false;
    }

    @Override
    public void openInventory() {
    }

    @Override
    public void closeInventory() {
        NBTTagList tags = new NBTTagList();
        System.out.print(tags);
    }

    @SideOnly(Side.CLIENT)
    public EntityItem getItemForRendering(int slot) {
        if (ghostItem == null) {
            ghostItem = new EntityItem(worldObj);
            ghostItem.hoverStart = 0.0F;
        }

        if (inventory[slot] == null) {
            return null;
        } else {
            ghostItem.setEntityItemStack(new ItemStack(inventory[slot].getItem(), 1, inventory[slot].getItemDamage()));
            return ghostItem;
        }
    }

    @Override
    public Packet getDescriptionPacket() {
        NBTTagCompound tag = new NBTTagCompound();
        writeToNBT(tag);
        return new S35PacketUpdateTileEntity(xCoord, yCoord, zCoord, 1, tag);
    }

    @Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity packet) {
        readFromNBT(packet.func_148857_g());
    }
}
