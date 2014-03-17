package info.jbcs.minecraft.chisel.inventory;

import info.jbcs.minecraft.chisel.item.ItemChisel;
import info.jbcs.minecraft.utilities.General;

import java.util.ArrayList;

import pl.asie.lib.block.InventoryBase;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class InventoryChiselSelection extends InventoryBase {
	ItemStack chisel = null;
	final static int normalSlots=32;
	int activeVariations=0;
	ContainerChisel container;
	
	public InventoryChiselSelection(ItemStack c) {
		super();

		chisel = c;
	}

	@Override
	public int getSizeInventory() {
		return normalSlots+1;
	}

	@Override
	public String getInventoryName() {
		return "Carve blocks";
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer entityplayer) {
		return true;
	}

	public void clearItems() {
		activeVariations=0;
		for (int i = 0; i < normalSlots; i++) {
			inventory[i] = null;
		}
	}

	public ItemStack getStackInSpecialSlot() {
		return inventory[normalSlots];
	}

	public void updateItems() {
		ItemStack chiseledItem = inventory[normalSlots];

		clearItems();

		if (chiseledItem == null){
			container.onChiselSlotChanged();
			return;
		}

		Item item = chiseledItem.getItem();
		if(item == null) return;
		
		if(Block.getBlockFromItem(item) == null)
			return;
		
		ArrayList<ItemStack> list=container.carving.getItems(chiseledItem);
		
		activeVariations=0;
		while(activeVariations<normalSlots && activeVariations<list.size()){
			inventory[activeVariations]=list.get(activeVariations);
			activeVariations++;
		}
				
		container.onChiselSlotChanged();
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int i) {
		return null;
	}

	@Override
	public void openInventory() {
	}

	@Override
	public void closeInventory() {
	}

	@Override
	public boolean isItemValidForSlot(int i, ItemStack stack) {
		if(stack!=null && (stack.getItem() instanceof ItemChisel)){
			return false;
		}
		
		return i==normalSlots;
	}

	@Override
	public void onInventoryUpdate(int slot) {
		
	}
}
