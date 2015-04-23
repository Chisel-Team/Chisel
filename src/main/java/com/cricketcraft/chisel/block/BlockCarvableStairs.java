package com.cricketcraft.chisel.block;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.BlockStairs;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import com.cricketcraft.chisel.api.ICarvable;
import com.cricketcraft.chisel.carving.CarvableHelper;
import com.cricketcraft.chisel.carving.CarvableVariation;
import com.cricketcraft.chisel.client.render.RendererStairs;

public class BlockCarvableStairs extends BlockStairs implements ICarvable {

	CarvableHelper carverHelper;
	int blockMeta;

	public BlockCarvableStairs(Block block, int meta, CarvableHelper helper) {
		super(block, meta);

		this.useNeighborBrightness = true;
		carverHelper = helper;
		blockMeta = meta;
	}

	@Override
	public IIcon getIcon(int side, int metadata) {
		return carverHelper.getIcon(side, blockMeta + metadata / 8);
	}

	@Override
	public int damageDropped(int i) {
		return i / 8;
	}

	@Override
	public void registerBlockIcons(IIconRegister register) {
		if (blockMeta == 0)
			carverHelper.registerBlockIcons("Chisel", this, register);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void getSubBlocks(Item block, CreativeTabs tabs, List list) {
		list.add(new ItemStack(block, 1, 0));
		list.add(new ItemStack(block, 1, 8));
	}

	@Override
	public int getRenderType() {
		return RendererStairs.id;
	}

	@Override
	public void onBlockPlacedBy(World par1World, int par2, int par3, int par4, EntityLivingBase par5EntityLiving, ItemStack par6ItemStack) {
		int meta = MathHelper.floor_double((par5EntityLiving.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;

		// 0->2 2->3 3->0 1->1
		meta = meta == 0 ? 2 : meta == 2 ? 3 : meta == 3 ? 0 : 1;

		meta += par1World.getBlockMetadata(par2, par3, par4) % 8; // add upside-down-ness

		if (par6ItemStack.getItemDamage() >= 8) {
			meta += 8;
		}
		
		par1World.setBlockMetadataWithNotify(par2, par3, par4, meta, 2);
	}

	@Override
	public int onBlockPlaced(World world, int x, int y, int z, int side, float hx, float hy, float hz, int damage) {
		return side != 0 && (side == 1 || hy <= 0.5D) ? damage : damage + 4;
	}

	@Override
	public CarvableVariation getVariation(IBlockAccess world, int x, int y, int z, int metadata) {
		return carverHelper.getVariation(blockMeta + (metadata / 8));
	}

	@Override
	public CarvableVariation getVariation(ItemStack stack) {
		return carverHelper.getVariation(blockMeta + (stack.getItemDamage() / 8));
	}
}
