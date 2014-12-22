package com.cricketcraft.chisel.block.tileentity;

import com.cricketcraft.chisel.block.BlockCarvable;
import com.cricketcraft.chisel.carving.CarvableHelper;
import com.cricketcraft.chisel.init.ModItems;
import net.minecraft.block.Block;
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
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

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
            if(!worldObj.isRemote && worldObj.getWorldTime() % 10 == 0){
                doTransaction();
            }
        } else {
            if(!worldObj.isRemote && worldObj.getWorldTime() % 20 == 0){
                doTransaction();
            }
        }

        markDirty();
    }

    private void doTransaction(){
        if(!(isSlotNull(BASE) && !(isSlotNull(TARGET)))){
            if(inventory[BASE].getItem() instanceof ItemBlock && inventory[TARGET].getItem() instanceof ItemBlock){
                Block base = Block.getBlockFromItem(inventory[BASE].getItem());
                Block target = Block.getBlockFromItem(inventory[TARGET].getItem());

                if(base instanceof BlockCarvable){
                    BlockCarvable baseCarvable = (BlockCarvable) base;

                    if(baseCarvable.carverHelper.variations.contains(target)){
                        if(isStackMode){
                            if(inventory[BASE].stackSize == inventory[BASE].getMaxStackSize()){
                                if(isSlotNull(OUTPUT)){
                                    inventory[OUTPUT] = inventory[BASE];
                                    inventory[BASE] = null;
                                }
                            }
                        } else {
                            if(isSlotNull(OUTPUT)){
                                inventory[OUTPUT] = new ItemStack(base, 1, inventory[BASE].getItemDamage());
                                inventory[BASE].stackSize--;
                            } else if(inventory[BASE].stackSize != 0){
                                inventory[OUTPUT].stackSize++;
                                inventory[BASE].stackSize--;
                            }
                        }
                    }
                } else if(target instanceof BlockCarvable){
                    BlockCarvable targetCarvable = (BlockCarvable) target;

                    if(targetCarvable.carverHelper.variations.contains(base)){
                        if(isStackMode){
                            if(inventory[BASE].stackSize == inventory[BASE].getMaxStackSize()){
                                if(isSlotNull(OUTPUT)){
                                    inventory[OUTPUT] = inventory[BASE];
                                    inventory[BASE] = null;
                                }
                            }
                        } else {
                            if(isSlotNull(OUTPUT)){
                                inventory[OUTPUT] = new ItemStack(base, 1, inventory[BASE].getItemDamage());
                                inventory[BASE].stackSize--;
                            } else if(inventory[BASE].stackSize != 0){
                                inventory[OUTPUT].stackSize++;
                                inventory[BASE].stackSize--;
                            }
                        }
                    }
                }
            }
        }
    }

    private boolean isSlotNull(int slot){
        if(inventory[slot] == null){
            return true;
        } else {
            return false;
        }
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
