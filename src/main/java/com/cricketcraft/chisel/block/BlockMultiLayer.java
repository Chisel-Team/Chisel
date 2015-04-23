package com.cricketcraft.chisel.block;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;

import com.cricketcraft.chisel.api.ICarvable;
import com.cricketcraft.chisel.carving.CarvableHelper;
import com.cricketcraft.chisel.carving.CarvableVariation;

public class BlockMultiLayer extends BlockMultiLayerBase implements ICarvable {

	public CarvableHelper carverHelper;

	public BlockMultiLayer(Material mat, String baseIcon) {
		super(mat, baseIcon);

		carverHelper = new CarvableHelper();
	}

	public BlockMultiLayer(Material mat, Block block) {
		super(mat, block);

		carverHelper = new CarvableHelper();
	}

	@Override
	public IIcon getIcon(int side, int metadata) {
		return carverHelper.getIcon(side, metadata);
	}

	@Override
	public int damageDropped(int i) {
		return i;
	}

	@Override
	public void registerBlockIcons(IIconRegister register) {
		super.registerBlockIcons(register);

		carverHelper.registerBlockIcons("Chisel", this, register);
	}

	@Override
	public void getSubBlocks(Item block, CreativeTabs tabs, List list) {
		carverHelper.registerSubBlocks(this, tabs, list);
	}

	@Override
	public CarvableVariation getVariation(IBlockAccess world, int x, int y, int z, int metadata) {
		return carverHelper.getVariation(metadata);
	}

	@Override
	public CarvableVariation getVariation(ItemStack stack) {
		return carverHelper.getVariation(stack.getItemDamage());
	}
}
