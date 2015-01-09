package com.cricketcraft.chisel.block.tileentity;

import java.util.ArrayList;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.StatCollector;

import com.cricketcraft.chisel.carving.Carving;
import com.cricketcraft.chisel.init.ChiselItems;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class TileEntityAutoChisel extends TileEntity implements ISidedInventory {

	public enum Upgrade {
		SPEED, AUTOMATION, STACK;

		public String getUnlocalizedName() {
			return ChiselItems.upgrade.getUnlocalizedName() + "_" + this.name().toLowerCase();
		}
		
		public String getLocalizedName() {
			return StatCollector.translateToLocal(getUnlocalizedName() + ".name");
		}
	}

	private final int BASE = 0, TARGET = 1, OUTPUT = 2, MIN_UPGRADE = 3;
	private static EntityItem ghostItem;
	boolean equal = false;
	private ItemStack[] inventory = new ItemStack[6];
	private String name = "autoChisel";
	
	public float xRot, yRot, zRot; // used for floating target client only

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

		int checkPeriod = hasUpgrade(Upgrade.SPEED) ? 10 : 40;

		ItemStack base = inventory[BASE], target = inventory[TARGET], output = inventory[OUTPUT];

		if (!worldObj.isRemote && worldObj.getWorldTime() % checkPeriod == 0) {
			if (base != null && target != null) {
				if (canBeMadeFrom(base, target)) {
					// the max possible for this craft
					int canBeMade = target.getMaxStackSize();

					// if there are items in the output, they count towards the max we can make
					if (output != null) {
						canBeMade -= output.stackSize;
					}

					// can't make more than we have
					canBeMade = Math.min(base.stackSize, canBeMade);

					// if we can't make any, forget it
					if (canBeMade <= 0) {
						return;
					}

					// result will always be a copy of the target, of course
					ItemStack chiseled = target.copy();
					// if we have the stack upgrade, boost the stack size to the max possible, otherwise just one
					chiseled.stackSize = hasUpgrade(Upgrade.STACK) ? canBeMade : 1;

					// if we can place the result in the output
					if (canMerge(chiseled)) {
						// if our output is empty, just use the current result
						if (output == null) {
							inventory[OUTPUT] = chiseled;
						} else {
							// otherwise just add our result to the existing stack
							inventory[OUTPUT].stackSize += chiseled.stackSize;
						}
						// remove what we made from the stack
						base.stackSize -= chiseled.stackSize;
						if (base.stackSize <= 0) {
							inventory[BASE] = null; // clear out 0 size itemstacks
						}
						// we changed something, so we need to tell the chunk to save
						markDirty();
					}
				}
			}
		}
	}

	// lets make sure the user isn't trying to make something from a block that doesn't have this as a valid target
	private boolean canBeMadeFrom(ItemStack from, ItemStack to) {
		ArrayList<ItemStack> results = Carving.chisel.getItems(from);
		for (ItemStack s : results) {
			if (s.getItem() == to.getItem() && s.getItemDamage() == to.getItemDamage()) {
				return true;
			}
		}
		return false;
	}

	private boolean canMerge(ItemStack toMerge) {
		// if the output slot is empty we can merge without checking
		if (inventory[OUTPUT] == null) {
			return true;
		}
		// need to check NBT as well as item
		if (toMerge.isItemEqual(inventory[OUTPUT]) && ItemStack.areItemStackTagsEqual(toMerge, inventory[OUTPUT])) {
			// we only care about metadata if the item has subtypes
			return toMerge.getHasSubtypes() && toMerge.getItemDamage() == inventory[OUTPUT].getItemDamage();
		}

		return false;
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
		if (hasUpgrade(Upgrade.AUTOMATION)) {
			if (slot == BASE) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean canExtractItem(int slot, ItemStack itemStack, int side) {
		if (hasUpgrade(Upgrade.AUTOMATION)) {
			if (slot == OUTPUT) {
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

	public boolean hasUpgrade(Upgrade upgrade) {
		ItemStack stack = inventory[MIN_UPGRADE + upgrade.ordinal()];
		if (stack != null) {
			return stack.getItem() == ChiselItems.upgrade && stack.getItemDamage() == upgrade.ordinal();
		}
		return false;
	}
}
