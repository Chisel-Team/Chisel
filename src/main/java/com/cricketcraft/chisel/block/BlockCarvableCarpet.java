package com.cricketcraft.chisel.block;

import java.util.List;

import net.minecraft.block.BlockCarpet;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;

import com.cricketcraft.chisel.Chisel;
import com.cricketcraft.chisel.api.ICarvable;
import com.cricketcraft.chisel.api.carving.CarvableHelper;
import com.cricketcraft.chisel.api.carving.IVariationInfo;

public class BlockCarvableCarpet extends BlockCarpet implements ICarvable {

	public CarvableHelper carverHelper;

	public BlockCarvableCarpet(Material m) {
		carverHelper = new CarvableHelper(this);
	}

	@Override
	public IIcon getIcon(int side, int metadata) {
		return carverHelper.getIcon(side, metadata);
	}

	@Override
	public IIcon getIcon(IBlockAccess world, int x, int y, int z, int side) {
		return carverHelper.getIcon(world, x, y, z, side);
	}

	@Override
	public int damageDropped(int i) {
		return i;
	}

	@Override
	public void registerBlockIcons(IIconRegister register) {
		carverHelper.registerBlockIcons("Chisel", this, register);
	}

	@Override
	public void getSubBlocks(Item item, CreativeTabs tabs, List list) {
		carverHelper.registerSubBlocks(this, tabs, list);
	}
	
	@Override
	public int getRenderType() {
		return Chisel.renderCTMId;
	}

	@Override
	public IVariationInfo getVariation(IBlockAccess world, int x, int y, int z, int metadata) {
		return carverHelper.getVariation(metadata);
	}

	@Override
	public IVariationInfo getVariation(ItemStack stack) {
		return carverHelper.getVariation(stack.getItemDamage());
	}
}
