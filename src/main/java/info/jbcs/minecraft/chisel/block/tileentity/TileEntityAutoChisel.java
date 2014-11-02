package info.jbcs.minecraft.chisel.block.tileentity;

import info.jbcs.minecraft.chisel.carving.Carving;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;

public class TileEntityAutoChisel extends TileEntity implements ISidedInventory{

	private ItemStack[] inventory = new ItemStack[3];
    private String name = "autoChisel";
    Carving carving = new Carving();
    boolean equal = false;

    @Override
    public boolean canUpdate(){
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

        if (nbt.hasKey("CustomName", 8))
        {
            this.name = nbt.getString("CustomName");
        }
    }

    @Override
    public void updateEntity(){
        if(!worldObj.isRemote && worldObj.getWorldTime() % 20 == 0) {

            ItemStack[] stacksInSlots = new ItemStack[inventory.length];

            for (int c = 0; c < getSizeInventory(); c++) {
                stacksInSlots[c] = getStackInSlot(c);
            }

            ItemStack input = stacksInSlots[0];
            ItemStack target = stacksInSlots[1];

            ItemStack output = stacksInSlots[2];

            if (input != null && target != null) {

                if(input.getItem() instanceof ItemBlock && target.getItem() instanceof ItemBlock){
                    if (carving.getGroup(Block.getBlockFromItem(input.getItem()), input.getItemDamage()) == carving.getGroup(Block.getBlockFromItem(target.getItem()), target.getItemDamage())) {
                        equal = true;
                    }

                    if (equal) {
                        if (output == null) {
                            setInventorySlotContents(2, new ItemStack(Block.getBlockFromItem(target.getItem()), 1, target.getItemDamage()));
                        } else {
                            if (input.stackSize != 0 || output.stackSize < getInventoryStackLimit()) {
                                decrStackSize(0, 1);
                                output.stackSize++;
                            } else {
                                inventory[0] = null;
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

        if (this.hasCustomInventoryName())
        {
            nbt.setString("CustomName", this.name);
        }
    }

    @Override
    public int getSizeInventory(){
        return inventory.length;
    }

    @Override
    public ItemStack getStackInSlot(int slot){
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
    public void setInventorySlotContents(int slot, ItemStack stack){
        inventory[slot] = stack;

        if(stack != null && stack.stackSize > getInventoryStackLimit()){
            stack.stackSize = getInventoryStackLimit();
        }
        markDirty();
    }

    @Override
    public String getInventoryName(){
        return name;
    }

    @Override
    public final boolean isUseableByPlayer(EntityPlayer player){
        return true;
    }

    @Override
    public int getInventoryStackLimit(){
        return 64;
    }

    @Override
    public boolean hasCustomInventoryName(){
        return false;
    }

    @Override
    public int[] getAccessibleSlotsFromSide(int side){
        int[] slots = new int[getSizeInventory()];

        for(int c = 0; c < slots.length; c++){
            slots[c] = c;
        }

        if(side != 0){
            return slots;
        } else if(side == 0){
            return new int[] {2};
        } else {
            return null;
        }
    }

    @Override
    public boolean isItemValidForSlot(int slot, ItemStack itemStack){
        return slot != 1;
    }

    @Override
    public boolean canInsertItem(int slot, ItemStack itemStack, int side){
        if(side != 0){
            if(slot == 0){
                return true;
            } else {
                return false;
            }
        } else if(side == 0){
            if(slot == 1){
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    @Override
    public boolean canExtractItem(int slot, ItemStack itemStack, int side){
        return true;
    }

    @Override
    public void openInventory(){}

    @Override
    public void closeInventory(){
        NBTTagList tags = new NBTTagList();
        System.out.print(tags);
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
