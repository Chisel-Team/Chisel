package info.jbcs.minecraft.utilities;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;

public abstract class InventoryStatic implements IInventory {
	public final ItemStack items[];

	public InventoryStatic(int size) {
		items = new ItemStack[size];
	}

	@Override
	public String getInventoryName() {
		return null;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer entityplayer) {
		return false;
	}

	public void onInventoryChanged(int slot) {
	}

	@Override
	public int getSizeInventory() {
		return items.length;
	}

	@Override
	public ItemStack getStackInSlot(int i) {
		return items[i];
	}

	@Override
	public ItemStack decrStackSize(int i, int j) {
		if (items[i] != null) {
			if (items[i].stackSize <= j) {
				ItemStack itemstack = items[i];
				items[i] = null;
				onInventoryChanged();
				onInventoryChanged(i);
				return itemstack;
			}

			ItemStack itemstack1 = items[i].splitStack(j);

			if (items[i].stackSize == 0) {
				items[i] = null;
			}

			onInventoryChanged();
			onInventoryChanged(i);
			return itemstack1;
		} else {
			return null;
		}
	}

	@Override
	public void setInventorySlotContents(int i, ItemStack itemstack) {
		items[i] = itemstack;

		if (itemstack != null && itemstack.stackSize > getInventoryStackLimit()) {
			itemstack.stackSize = getInventoryStackLimit();
		}

		onInventoryChanged();
		onInventoryChanged(i);
	}

	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

	public void readFromNBT(NBTTagCompound nbttagcompound) {
		NBTTagList nbttaglist = nbttagcompound.getTagList("Items", 10);

		for (int i = 0; i < nbttaglist.tagCount(); i++) {
			NBTTagCompound nbttagcompound1 = (NBTTagCompound) nbttaglist.getCompoundTagAt(i);
			int j = nbttagcompound1.getByte("slot") & 0xff;

			if (j >= 0 && j < items.length) {
				items[j] = ItemStack.loadItemStackFromNBT(nbttagcompound1);
			}
		}
		
		onInventoryChanged();
	}

	public void writeToNBT(NBTTagCompound nbttagcompound) {
		NBTTagList nbttaglist = new NBTTagList();

		for (int i = 0; i < items.length; i++) {
			if (items[i] == null) {
				continue;
			}

			NBTTagCompound nbttagcompound1 = new NBTTagCompound();
			nbttagcompound1.setByte("slot", (byte) i);
			items[i].writeToNBT(nbttagcompound1);
			nbttaglist.appendTag(nbttagcompound1);
		}

		nbttagcompound.setTag("items", nbttaglist);
	}

	private int getFirstEmptyStack(int start, int end) {
		for (int i = start; i <= end; i++) {
			if (items[i] == null) {
				return i;
			}
		}

		return -1;
	}

	private int storeItemStack(ItemStack itemstack, int start, int end) {
		for (int i = start; i <= end; i++) {
			if (items[i] != null && items[i].getItem().equals(itemstack.getItem() )&& items[i].isStackable() && items[i].stackSize < items[i].getMaxStackSize() && items[i].stackSize < getInventoryStackLimit() && (!items[i].getHasSubtypes() || items[i].getItemDamage() == itemstack.getItemDamage())) {
				return i;
			}
		}

		return -1;
	}

	private int storePartialItemStack(ItemStack itemstack, int start, int end) {
		Item i = itemstack.getItem();
		int j = itemstack.stackSize;
		int k = storeItemStack(itemstack, start, end);

		if (k < 0) {
			k = getFirstEmptyStack(start, end);
		}

		if (k < 0) {
			return j;
		}

		if (items[k] == null) {
			items[k] = new ItemStack(i, 0, itemstack.getItemDamage());
		}

		int l = j;

		if (l > items[k].getMaxStackSize() - items[k].stackSize) {
			l = items[k].getMaxStackSize() - items[k].stackSize;
		}

		if (l > getInventoryStackLimit() - items[k].stackSize) {
			l = getInventoryStackLimit() - items[k].stackSize;
		}

		if (l == 0) {
			return j;
		} else {
			j -= l;
			items[k].stackSize += l;
			items[k].animationsToGo = 5;
			onInventoryChanged();
			onInventoryChanged(k);
			return j;
		}
	}

	public boolean addItemStackToInventory(ItemStack itemstack, int start, int end) {
		if (itemstack == null) {
			return true;
		}

		if (!itemstack.isItemDamaged()) {
			int i;

			do {
				i = itemstack.stackSize;
				itemstack.stackSize = storePartialItemStack(itemstack, start, end);
			} while (itemstack.stackSize > 0 && itemstack.stackSize < i);

			return itemstack.stackSize < i;
		}

		int j = getFirstEmptyStack(start, end);

		if (j >= 0) {
			items[j] = ItemStack.copyItemStack(itemstack);
			items[j].animationsToGo = 5;
			itemstack.stackSize = 0;
			onInventoryChanged();
			onInventoryChanged(j);
			return true;
		} else {
			return false;
		}
	}

	public boolean addItemStackToInventory(ItemStack itemstack) {
		return addItemStackToInventory(itemstack, 0, items.length - 1);
	}

	public ItemStack takeItems(Item item, int damage, int count) {
		ItemStack res = null;

		for (int i = 0; i < items.length; i++) {
			if (items[i] == null || !items[i].getItem().equals(item) || items[i].getItemDamage() != damage) {
				continue;
			}

			if (res == null) {
				res = new ItemStack(item, 0, damage);
			}

			while (items[i] != null && res.stackSize < count && items[i].stackSize > 0) {
				res.stackSize++;
				items[i].stackSize--;

				if (items[i].stackSize == 0) {
					items[i] = null;
				}

				onInventoryChanged(i);
			}

			if (res.stackSize >= count) {
				break;
			}
		}

		onInventoryChanged();
		return res;
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int i) {
		return null;
	}
	
	public void onInventoryChanged() {
	}

	@Override
	public void openInventory() {
	}

	@Override
	public void closeInventory() {
	}

	@Override
	public boolean isItemValidForSlot(int i, ItemStack itemstack) {
		return true;
	}

	public boolean isEmpty() {
		for (int i = 0; i < items.length; i++) {
			if (items[i] != null && items[i].getItem() != null) {
				return false;
			}
		}

		return true;
	}

	public void clear() {
		for (int i = 0; i < items.length; i++) {
			items[i] = null;
		}
	}
	
	public void throwItems(World world, int x, int y, int z){
		if(world.isRemote) return;
		
		for (int i = 0; i < items.length; i++) {
			ItemStack itemstack = items[i];
			if (itemstack == null) continue;
			
			items[i]=null;

			float xx = world.rand.nextFloat() * 0.8F + 0.1F;
			float yy = world.rand.nextFloat() * 0.8F + 0.1F;
			float zz = world.rand.nextFloat() * 0.8F + 0.1F;
			while (itemstack.stackSize > 0) {
				int c = world.rand.nextInt(21) + 10;
				if (c > itemstack.stackSize) {
					c = itemstack.stackSize;
				}

				itemstack.stackSize -= c;
				EntityItem entityitem = new EntityItem(world, x + xx, y + yy, z + zz, new ItemStack(itemstack.getItem(), c, itemstack.getItemDamage()));
				float f3 = 0.05F;
				entityitem.motionX = (float) world.rand.nextGaussian() * f3;
				entityitem.motionY = (float) world.rand.nextGaussian() * f3 + 0.2F;
				entityitem.motionZ = (float) world.rand.nextGaussian() * f3;
				world.spawnEntityInWorld(entityitem);
			}
		}

		onInventoryChanged();
	}

	@Override
	public boolean hasCustomInventoryName() {
		return false;
	}

	@Override
	public void markDirty() {
		// TODO
	}
}
