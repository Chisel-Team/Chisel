package team.chisel.block.tileentity;

import java.util.Set;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import team.chisel.init.ChiselBlocks;
import team.chisel.network.PacketHandler;
import team.chisel.network.message.MessagePresentConnect;

import com.google.common.collect.Sets;

public class TileEntityPresent extends TileEntity implements IInventory, IDoubleChest {

	private TileEntityPresent connection = null;
	// If cachedDir is non-null, but connection is null, this present must be
	// connected to an unloaded present. This is handled appropriately.
	private ForgeDirection cachedDir = null;
	private boolean isParent;
	private final ItemStack[] inventory = new ItemStack[27];
	private int rotation;
	private boolean autoSearch = true;
	
	private Set<EntityPlayer> playersUsing = Sets.newHashSet();
	
	private static final float MAX_LID_POS = 1;
	private float lidPos, prevLidPos;

	@Override
	public void updateEntity() {
		if (!isConnected() && autoSearch && worldObj != null /* ugh */) {
			if (cachedDir != null) {
				connectTo(cachedDir);
			} else if (!worldObj.isRemote) {
				findConnections();
			}
			autoSearch = false;
		}
		this.prevLidPos = lidPos;
		if (playersUsing.size() > 0) {
			if (lidPos == 0) {
				this.worldObj.playSoundEffect(this.xCoord + 0.5, this.yCoord + 0.5, this.zCoord + 0.5, "random.chestopen", 0.5F, this.worldObj.rand.nextFloat() * 0.1F + 0.9F);
			}
			
			lidPos = Math.min(MAX_LID_POS, lidPos + 0.1f);
		} else {
			lidPos = Math.max(0, lidPos - 0.1f);
			
			if (getLidPos() < MAX_LID_POS / 2f && getPrevLidPos() >= MAX_LID_POS / 2f) {
				this.worldObj.playSoundEffect(this.xCoord + 0.5, this.yCoord + 0.5, this.zCoord + 0.5, "random.chestclosed", 0.5F, this.worldObj.rand.nextFloat() * 0.1F + 0.9F);
			}
		}
	}

	public boolean isConnected() {
		return connection != null;
	}

	private boolean connectTo(TileEntityPresent present, ForgeDirection dir) {
		if (present.getBlockMetadata() == getBlockMetadata() && !present.isConnected() && (present.cachedDir == null || present.cachedDir == dir.getOpposite())
				&& Math.abs(present.xCoord - xCoord + present.yCoord - yCoord + present.zCoord - zCoord) == 1) {
			connection = present;
			connection.connection = this;
			connection.cachedDir = dir.getOpposite();
			connection.markDirty();
			cachedDir = dir;
			isParent = !present.isParent;
			markDirty();
			PacketHandler.INSTANCE.sendToDimension(new MessagePresentConnect(this, dir, true), worldObj.provider.dimensionId);
			return true;
		}
		return false;
	}

	public boolean connectTo(ForgeDirection dir) {
		int x = xCoord + dir.offsetX, y = yCoord + dir.offsetY, z = zCoord + dir.offsetZ;
		TileEntity te = getTileSafe(x, y, z);
		if (te instanceof TileEntityPresent) {
			return connectTo((TileEntityPresent) te, dir);
		}
		return !exists(x, y, z);
	}

	/**
	 * @param preserveDir
	 *            If true, this is only a virtual disconnect, meaning the other chest still exists, but is still loaded, so maintain a cachedDir for reconnection upon chunk load.
	 */
	public void disconnect(boolean preserveDir) {
		if (isConnected()) {
			if (!preserveDir) {
				this.connection.cachedDir = null;
				this.cachedDir = null;
			}
			this.connection.connection = null;
			this.connection.markDirty();
			this.connection = null;
			this.markDirty();
			PacketHandler.INSTANCE.sendToDimension(new MessagePresentConnect(this, ForgeDirection.UNKNOWN, false, preserveDir), worldObj.provider.dimensionId);
		}
	}

	public TileEntityPresent getConnection() {
		return connection;
	}

	public ForgeDirection getConnectionDir() {
		return cachedDir == null ? ForgeDirection.UNKNOWN : cachedDir;
	}

	public boolean isParent() {
		return isParent || !isConnected();
	}

	public int getRotation() {
		return rotation;
	}

	public void setRotation(int rot) {
		this.rotation = rot;
	}

	public void findConnections() {
		if (!isConnected()) {
			if (cachedDir != null) {
				if (connectTo(cachedDir)) {
					return;
				}
			}
			for (ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS) {
				if (dir != ForgeDirection.UP && dir != ForgeDirection.DOWN) {
					if (connectTo(dir)) {
						return;
					}
				}
			}
		}
	}

	public void addPlayerUsing(EntityPlayer player) {
		playersUsing.add(player);
	}

	public void removePlayerUsing(EntityPlayer player) {
		playersUsing.remove(player);
	}

	public float getLidPos() {
		return lidPos;
	}

	public float getPrevLidPos() {
		return prevLidPos;
	}

	public TileEntityPresent getParent() {
		return isParent || connection == null ? this : connection;
	}

	@Override
	public AxisAlignedBB getRenderBoundingBox() {
		return ChiselBlocks.present.getBoundingBox(this);
	}

	@Override
	public void writeToNBT(NBTTagCompound tag) {
		super.writeToNBT(tag);
		NBTTagList nbttaglist = new NBTTagList();

		for (int i = 0; i < getTrueSizeInventory(); ++i) {
			if (inventory[i] != null) {
				NBTTagCompound nbttagcompound1 = new NBTTagCompound();
				nbttagcompound1.setByte("Slot", (byte) i);
				inventory[i].writeToNBT(nbttagcompound1);
				nbttaglist.appendTag(nbttagcompound1);
			}
		}

		tag.setTag("Items", nbttaglist);
		tag.setBoolean("isParent", isParent);
		tag.setInteger("rotation", rotation);
		if (cachedDir != null) {
			tag.setInteger("conDir", cachedDir.ordinal());
		}
	}

	@Override
	public void readFromNBT(NBTTagCompound tag) {
		super.readFromNBT(tag);
		NBTTagList nbttaglist = tag.getTagList("Items", 10);

		for (int i = 0; i < nbttaglist.tagCount(); ++i) {
			NBTTagCompound nbttagcompound1 = nbttaglist.getCompoundTagAt(i);
			int j = nbttagcompound1.getByte("Slot") & 255;

			if (j >= 0 && j < getTrueSizeInventory()) {
				inventory[j] = ItemStack.loadItemStackFromNBT(nbttagcompound1);
			}
		}

		this.isParent = tag.getBoolean("isParent");

		this.rotation = tag.getInteger("rotation");
		if (tag.hasKey("conDir")) {
			cachedDir = ForgeDirection.values()[tag.getInteger("conDir")];
		}
		autoSearch = true;
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

	@Override
	public boolean shouldRefresh(Block oldBlock, Block newBlock, int oldMeta, int newMeta, World world, int x, int y, int z) {
		// prevent losing TE data when in-world chiseling
		return oldBlock != newBlock;
	}

	@Override
	public void invalidate() {
		super.invalidate();
		if (isConnected()) {
			disconnect(false);
		}
	}

	@Override
	public void onChunkUnload() {
		super.onChunkUnload();
		if (isConnected()) {
			disconnect(true);
		}
	}

	private int getAdjustedSlot(int slot) {
		slot %= getSizeInventory();
		if (isConnected() && !isParent) {
			slot = (slot + getTrueSizeInventory()) % getSizeInventory();
		}
		return slot;
	}

	private TileEntity getTileSafe(int x, int y, int z) {
		if (exists(x, y, z)) {
			return worldObj.getTileEntity(x, y, z);
		}
		return null;
	}

	// private Block getBlockSafe(int x, int y, int z) {
	// if (exists(x, y, z)) {
	// return worldObj.getBlock(x, y, z);
	// }
	// return null;
	// }
	//
	// private int getMetaSafe(int x, int y, int z) {
	// if (exists(x, y, z)) {
	// return worldObj.getBlockMetadata(x, y, z);
	// }
	// return 0;
	// }
	//
	private boolean exists(int x, int y, int z) {
		return worldObj.blockExists(x, y, z);
	}

	/* IInventory */

	@Override
	public int getSizeInventory() {
		return isConnected() ? getTrueSizeInventory() + connection.getTrueSizeInventory() : getTrueSizeInventory();
	}

	@Override
	public ItemStack getStackInSlot(int slot) {
		slot = getAdjustedSlot(slot);
		if (slot >= getTrueSizeInventory()) {
			return isConnected() ? connection.inventory[slot % getTrueSizeInventory()] : null;
		} else {
			return inventory[slot];
		}
	}

	@Override
	public ItemStack decrStackSize(int slot, int amount) {
		slot = getAdjustedSlot(slot);
		ItemStack[] inv = inventory;
		if (isConnected() && slot >= getTrueSizeInventory()) {
			inv = connection.inventory;
			slot %= getTrueSizeInventory();
			connection.markDirty();
		}

		if (inv[slot] != null) {
			ItemStack itemstack;

			if (inv[slot].stackSize <= amount) {
				itemstack = inv[slot];
				inv[slot] = null;
				this.markDirty();
				return itemstack;
			} else {
				itemstack = inv[slot].splitStack(amount);

				if (inv[slot].stackSize == 0) {
					inv[slot] = null;
				}

				this.markDirty();
				return itemstack;
			}
		} else {
			return null;
		}
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int slot) {
		return getStackInSlot(slot);
	}

	@Override
	public void setInventorySlotContents(int slot, ItemStack stack) {
		slot = getAdjustedSlot(slot);
		if (slot < getTrueSizeInventory()) {
			inventory[slot] = stack;
		} else if (isConnected()) {
			connection.inventory[slot % getTrueSizeInventory()] = stack;
			connection.markDirty();
		}
	}

	@Override
	public String getInventoryName() {
		return "chisel.present";
	}

	@Override
	public boolean hasCustomInventoryName() {
		return false;
	}

	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer p_70300_1_) {
		return true;
	}

	@Override
	public void openInventory() {
		;
	}

	@Override
	public void closeInventory() {
		;
	}

	@Override
	public boolean isItemValidForSlot(int p_94041_1_, ItemStack p_94041_2_) {
		return true;
	}

	/* IDoubleChest */

	@Override
	public int getTrueSizeInventory() {
		return inventory.length;
	}

	@Override
	public ItemStack getTrueStackInSlot(int slot) {
		return inventory[slot % getTrueSizeInventory()];
	}

	@Override
	public void putStackInTrueSlot(int slot, ItemStack stack) {
		inventory[slot % getTrueSizeInventory()] = stack;
	}
}
