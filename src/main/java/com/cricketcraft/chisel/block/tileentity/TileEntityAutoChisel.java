package com.cricketcraft.chisel.block.tileentity;

import java.util.List;

import com.cricketcraft.chisel.api.carving.CarvingUtils;
import net.minecraft.block.Block;
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

import com.cricketcraft.chisel.api.IChiselItem;
import com.cricketcraft.chisel.carving.Carving;
import com.cricketcraft.chisel.client.GeneralChiselClient;
import com.cricketcraft.chisel.init.ChiselItems;
import com.cricketcraft.chisel.network.PacketHandler;
import com.cricketcraft.chisel.network.message.MessageAutoChisel;
import com.cricketcraft.chisel.network.message.MessageSlotUpdate;
import com.cricketcraft.chisel.utils.General;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class TileEntityAutoChisel extends TileEntity implements ISidedInventory {

	public enum Upgrade {
		SPEED, AUTOMATION, STACK, REVERSION;

		public String getUnlocalizedName() {
			return ChiselItems.upgrade.getUnlocalizedName() + "_" + this.name().toLowerCase();
		}

		public String getLocalizedName() {
			return StatCollector.translateToLocal(getUnlocalizedName() + ".name");
		}
	}

	public static final int BASE = 0, TARGET = 1, OUTPUT = 2, CHISEL = 3, MIN_UPGRADE = 4;
	private static final int FAST_SPEED = 1, SLOW_SPEED = 4;

	private int progress = 0;

	private static EntityItem ghostItem;
	boolean equal = false;
	private ItemStack[] inventory = new ItemStack[8];
	private String name = "autoChisel";

	// client animation fields

	// used for floating target
	public float xRot, yRot, zRot;

	// used for chisel item rotation
	public static final int maxRot = 60;
	public static final int rotAmnt = 15;
	public float chiselRot;
	public boolean chiseling = false, breakChisel = false;;
	public int toChisel = 1;

	private ItemStack lastBase;

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

		ItemStack base = inventory[BASE], target = inventory[TARGET], output = inventory[OUTPUT];

		if (!worldObj.isRemote && hasChisel() && base != null && target != null) {
			if (canBeMadeFrom(base, target)) {
				if (progress >= (hasUpgrade(Upgrade.SPEED) ? FAST_SPEED : SLOW_SPEED)) {

					// reset progress
					progress = 0;

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

					if (canChisel(chiseled)) {
						// if our output is empty, just use the current result
						if (output == null) {
							setInventorySlotContents(OUTPUT, chiseled);
						} else {
							// otherwise just add our result to the existing stack
							inventory[OUTPUT].stackSize += chiseled.stackSize;
							slotChanged(OUTPUT);
						}

						chiselItem(chiseled.stackSize);

						// remove what we made from the stack
						base.stackSize -= chiseled.stackSize;
						if (base.stackSize <= 0) {
							setInventorySlotContents(BASE, null); // clear out 0 size itemstacks
						}
					}
				} else if (worldObj.getTotalWorldTime() % 10 == 0) {
					progress++;
				}
			}
		} else if(!worldObj.isRemote && hasChisel() && base != null && hasUpgrade(Upgrade.REVERSION)){
			if(Carving.chisel.getItemsForChiseling(base) != null){
				if (progress >= (hasUpgrade(Upgrade.SPEED) ? FAST_SPEED : SLOW_SPEED)) {

					// reset progress
					progress = 0;

					// the max possible for this craft
					int canBeMade = base.getMaxStackSize();

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
					ItemStack chiseled;
					if(Carving.chisel.getItemsForChiseling(inventory[TARGET]).get(0) != null){

						// result will always be a copy of the target, of course
						chiseled = inventory[TARGET].copy();
						// if we have the stack upgrade, boost the stack size to the max possible, otherwise just one
						chiseled.stackSize = hasUpgrade(Upgrade.STACK) ? canBeMade : 1;
					} else {
						// result will always be a copy of the target, of course
						chiseled = base.copy();
						// if we have the stack upgrade, boost the stack size to the max possible, otherwise just one
						chiseled.stackSize = hasUpgrade(Upgrade.STACK) ? canBeMade : 1;
					}


					if (canChisel(chiseled)) {
						// if our output is empty, just use the current result
						if (output == null) {
							setInventorySlotContents(OUTPUT, chiseled);
						} else {
							// otherwise just add our result to the existing stack
							inventory[OUTPUT].stackSize += chiseled.stackSize;
							slotChanged(OUTPUT);
						}

						chiselItem(chiseled.stackSize);

						// remove what we made from the stack
						base.stackSize -= chiseled.stackSize;
						if (base.stackSize <= 0) {
							setInventorySlotContents(BASE, null); // clear out 0 size itemstacks
						}
					}
				} else if (worldObj.getTotalWorldTime() % 10 == 0) {
					progress++;
				}
			}
		} else if (worldObj.isRemote) {
			if (chiseling) {
				chiselRot += rotAmnt;
				if (chiselRot >= maxRot) {
					chiselItem(0);
				}
			} else {
				chiselRot = Math.max(chiselRot - rotAmnt, 0);
			}
		}
	}

	// lets make sure the user isn't trying to make something from a block that doesn't have this as a valid target
	private boolean canBeMadeFrom(ItemStack from, ItemStack to) {
		List<ItemStack> results = Carving.chisel.getItemsForChiseling(from);
		for (ItemStack s : results) {
			if (s.getItem() == to.getItem() && s.getItemDamage() == to.getItemDamage()) {
				return true;
			}
		}
		return false;
	}

	private boolean canChisel(ItemStack toMerge) {
		// if the output slot is empty we can merge without checking
		if (inventory[OUTPUT] == null) {
			return true;
		}
		// need to check NBT as well as item
		if (toMerge.isItemEqual(inventory[OUTPUT]) && ItemStack.areItemStackTagsEqual(toMerge, inventory[OUTPUT])) {
			// we only care about metadata if the item has subtypes
			return !toMerge.getHasSubtypes() || toMerge.getItemDamage() == inventory[OUTPUT].getItemDamage();
		}

		if(inventory[TARGET] == null){
			return ((IChiselItem)inventory[CHISEL].getItem()).canChisel(worldObj, inventory[CHISEL], General.getVariation(inventory[BASE]));
		} else {
			return ((IChiselItem)inventory[CHISEL].getItem()).canChisel(worldObj, inventory[CHISEL], General.getVariation(inventory[TARGET]));
		}
	}

	private boolean hasChisel() {
		return inventory[CHISEL] != null && inventory[CHISEL].getItem() instanceof IChiselItem && inventory[CHISEL].stackSize >= 1;
	}

	/** Calls IChiselItem#onChisel() and sends the chisel packet for sound/animation */
	private void chiselItem(int chiseled) {
		if (!worldObj.isRemote) {
			boolean breakChisel = false;
			if(inventory[TARGET] == null){
				if (((IChiselItem) inventory[CHISEL].getItem()).onChisel(worldObj, inventory[CHISEL], CarvingUtils.getDefaultVariationFor(Block.getBlockFromItem(inventory[TARGET].getItem()), inventory[TARGET].getItemDamage(), General.getVariation(inventory[TARGET]).getOrder()))) {
					inventory[CHISEL].setItemDamage(inventory[CHISEL].getItemDamage() + 1);
					if (inventory[CHISEL].getItemDamage() >= inventory[CHISEL].getMaxDamage()) {
						setInventorySlotContents(CHISEL, null);
						breakChisel = true;
					}
				}
				PacketHandler.INSTANCE.sendToDimension(new MessageAutoChisel(this, chiseled, true, breakChisel), worldObj.provider.dimensionId);
			} else {
				if (((IChiselItem) inventory[CHISEL].getItem()).onChisel(worldObj, inventory[CHISEL], General.getVariation(inventory[TARGET]))) {
					inventory[CHISEL].setItemDamage(inventory[CHISEL].getItemDamage() + 1);
					if (inventory[CHISEL].getItemDamage() >= inventory[CHISEL].getMaxDamage()) {
						setInventorySlotContents(CHISEL, null);
						breakChisel = true;
					}
				}
				PacketHandler.INSTANCE.sendToDimension(new MessageAutoChisel(this, chiseled, true, breakChisel), worldObj.provider.dimensionId);
			}


		} else {
			if (breakChisel) {
				worldObj.playSound(xCoord + 0.5, yCoord + 0.5, zCoord + 0.5, "random.break", 0.8F, 0.8F + worldObj.rand.nextFloat() * 0.4F, false);
			}
			GeneralChiselClient.spawnAutoChiselFX(this, lastBase != null ? lastBase : inventory[BASE]);
			chiseling = false;
			if (lastBase != null) {
				lastBase.stackSize -= toChisel;
				if (lastBase.stackSize <= 0) {
					lastBase = null;
				}
			}
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

		if (worldObj.isRemote && slot == BASE && stack != null) {
			lastBase = stack.copy();
		}

		if (!worldObj.isRemote) {
			slotChanged(slot);
		}
	}

	private void slotChanged(int slot) {
		PacketHandler.INSTANCE.sendToDimension(new MessageSlotUpdate(this, slot, inventory[slot]), worldObj.provider.dimensionId);
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
		return new int[] { BASE, TARGET, OUTPUT, CHISEL };
	}

	@Override
	public boolean isItemValidForSlot(int slot, ItemStack itemStack) {
		if (itemStack == null) {
			return false;
		}

		switch (slot) {
		case BASE:
		case TARGET:
			return !Carving.chisel.getItemsForChiseling(itemStack).isEmpty();
		case OUTPUT:
			return false;
		case CHISEL:
			return itemStack.getItem() instanceof IChiselItem;
		default:
			return itemStack.getItem() == ChiselItems.upgrade && Upgrade.values()[slot - MIN_UPGRADE].ordinal() == itemStack.getItemDamage();
		}
	}

	@Override
	public boolean canInsertItem(int slot, ItemStack itemStack, int side) {
		return hasUpgrade(Upgrade.AUTOMATION) && (slot == BASE || slot == TARGET || slot == CHISEL);
	}

	@Override
	public boolean canExtractItem(int slot, ItemStack itemStack, int side) {
		return hasUpgrade(Upgrade.AUTOMATION) && slot == OUTPUT;
	}

	@Override
	public void openInventory() {
		;
	}

	@Override
	public void closeInventory() {
		;
	}

	@SideOnly(Side.CLIENT)
	public EntityItem getItemForRendering(int slot) {
		if (ghostItem == null) {
			ghostItem = new EntityItem(worldObj);
			ghostItem.hoverStart = 0.0F;
		}

		if (slot == BASE && lastBase != null) {
			ghostItem.setEntityItemStack(lastBase.copy());
			return ghostItem;
		} else if (inventory[slot] == null) {
			return null;
		} else {
			ghostItem.setEntityItemStack(inventory[slot].copy());
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

	public String getSlotTooltipUnloc(int slotNumber) {
		String base = "autochisel.slot.%s.tooltip";
		String name = null;
		if (slotNumber < MIN_UPGRADE) {
			if (slotNumber == TARGET) {
				name = "target";
			} else if (slotNumber == CHISEL) {
				name = "chisel";
			}
			return name == null ? null : String.format(base, name);
		} else {
			return Upgrade.values()[slotNumber - MIN_UPGRADE].getUnlocalizedName() + ".name";
		}
	}

	public void doChiselAnim(ItemStack lastChiseled, int chiseled, boolean playSound, boolean breakChisel) {
		this.lastBase = lastChiseled == null ? null : lastChiseled.copy();
		this.toChisel = chiseled;
		if (playSound) {
			this.chiseling = true;
		}
		this.breakChisel = breakChisel;
	}

	public ItemStack getLastBase() {
		return lastBase == null ? null : lastBase.copy();
	}
}
