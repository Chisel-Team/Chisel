package com.cricketcraft.chisel.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;

import com.cricketcraft.chisel.api.IChiselItem;
import com.cricketcraft.chisel.carving.Carving;
import com.cricketcraft.chisel.utils.General;
import com.cricketcraft.chisel.utils.GeneralClient;

public class SlotChiselSelection extends Slot {

	private final ContainerChisel container;
	private final InventoryChiselSelection selInventory;

	public SlotChiselSelection(ContainerChisel container, InventoryChiselSelection inv, IInventory iinventory, int i, int j, int k) {
		super(iinventory, i, j, k);

		this.container = container;
		selInventory = inv;
	}

	@Override
	public boolean isItemValid(ItemStack itemstack) {
		return false;
	}

	@Override
	public boolean canTakeStack(EntityPlayer par1EntityPlayer) {
		if (container.finished)
			return false;

		return par1EntityPlayer.inventory.getItemStack() == null;
	}

	@Override
	public void onPickupFromSlot(EntityPlayer player, ItemStack itemstack) {
		ItemStack heldStack = player.inventory.getItemStack();
		ItemStack crafted = selInventory.inventory[InventoryChiselSelection.normalSlots];

		if (heldStack == null) {
			selInventory.decrStackSize(InventoryChiselSelection.normalSlots, 1);
		} else {
			putStack(itemstack.copy());

			player.inventory.setItemStack(null);

			if (selInventory.inventory[InventoryChiselSelection.normalSlots] == null)
				return;

			player.inventory.setItemStack(new ItemStack(itemstack.getItem(), selInventory.inventory[InventoryChiselSelection.normalSlots].stackSize, itemstack.getItemDamage()));
			selInventory.setInventorySlotContents(InventoryChiselSelection.normalSlots, null);
		}

		selInventory.updateItems();

		if (((IChiselItem) container.chisel.getItem()).onChisel(player.worldObj, container.chisel, General.getVariation(crafted))) {
			container.chisel.damageItem(1, player);
			if (container.chisel.stackSize <= 0) {
				player.inventory.mainInventory[player.inventory.currentItem] = null;
			}
		}

		if (player.worldObj.isRemote) {
			String sound = Carving.chisel.getVariationSound(crafted.getItem(), crafted.getItemDamage());
			GeneralClient.playChiselSound(player.worldObj, MathHelper.floor_double(player.posX), MathHelper.floor_double(player.posY), MathHelper.floor_double(player.posZ), sound);
		}
	}
}
