package com.cricketcraft.chisel.item;

import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

import com.cricketcraft.chisel.Chisel;
import com.cricketcraft.chisel.block.tileentity.TileEntityAutoChisel.Upgrade;

public class ItemUpgrade extends BaseItem {

	public IIcon[] icons = new IIcon[4];

	public ItemUpgrade(String unlocalizedName) {
		super();
		this.setUnlocalizedName(unlocalizedName);
		this.setCreativeTab(CreativeTabs.tabMaterials);
		setHasSubtypes(true);
		setMaxStackSize(1); // it's easier this way
	}

	@Override
	public IIcon getIconFromDamage(int meta) {
		// using modulo throughout to prevent AIOB
		return this.icons[meta % Upgrade.values().length];
	}

	@Override
	public void registerIcons(IIconRegister reg) {
		Upgrade[] upgrades = Upgrade.values();
		for (int i = 0; i < upgrades.length; i++) {
			this.icons[i] = reg.registerIcon(Chisel.MOD_ID + ":upgrade_" + upgrades[i].name().toLowerCase());
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void getSubItems(Item item, CreativeTabs tab, List list) {
		for (int i = 0; i < Upgrade.values().length; i++) {
			list.add(new ItemStack(item, 1, i));
		}
	}

	@Override
	public String getUnlocalizedName(ItemStack stack) {
		Upgrade[] upgrades = Upgrade.values();
		return upgrades[stack.getItemDamage() % upgrades.length].getUnlocalizedName();
	}
}
