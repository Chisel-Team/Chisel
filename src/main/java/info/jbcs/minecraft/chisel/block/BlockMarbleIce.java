package info.jbcs.minecraft.chisel.block;

import info.jbcs.minecraft.chisel.Carvable;
import info.jbcs.minecraft.chisel.CarvableHelper;
import info.jbcs.minecraft.chisel.CarvableVariation;
import info.jbcs.minecraft.chisel.Chisel;

import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockIce;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

public class BlockMarbleIce extends BlockIce implements Carvable  {
	public CarvableHelper carverHelper;

	public BlockMarbleIce() {
		super();

		carverHelper = new CarvableHelper();

		setCreativeTab(Chisel.tabChisel);
	}

	@Override
	public IIcon getIcon(int side, int metadata) {
		return carverHelper.getIcon(side, metadata);
	}

	@Override
	public int damageDropped(int i) {
		return 0;
	}

	@Override
	public void registerBlockIcons(IIconRegister register) {
		carverHelper.registerBlockIcons("Chisel", this, register);
	}

	@Override
	public void getSubBlocks(Item item, CreativeTabs tabs, List list) {
		carverHelper.registerSubBlocks(this, tabs, list);
	}

	/**
	 * Called when the player destroys a block with an item that can harvest it.
	 * (i, j, k) are the coordinates of the block and l is the block's
	 * subtype/damage.
	 */
	@Override
	public void harvestBlock(World par1World, EntityPlayer par2EntityPlayer, int par3, int par4, int par5, int par6) {
		if (!Chisel.dropIceShards){
			super.harvestBlock(par1World, par2EntityPlayer, par3, par4, par5, par6);
			return;
		}
		
        par2EntityPlayer.addStat(StatList.mineBlockStatArray[Block.getIdFromBlock(this)], 1);
        par2EntityPlayer.addExhaustion(0.025F);
        
        if(par1World.isRemote)
        	return;

		if (this.canSilkHarvest(par1World, par2EntityPlayer, par3, par4, par5, par6) && EnchantmentHelper.getSilkTouchModifier(par2EntityPlayer)) {
			ItemStack itemstack = this.createStackedBlock(par6);

			if (itemstack != null) {
				this.dropBlockAsItem(par1World, par3, par4, par5, itemstack);
			}
		} else {
			int i1 = EnchantmentHelper.getFortuneModifier(par2EntityPlayer);
			this.dropBlockAsItem(par1World, par3, par4, par5, par6, i1);
		}
	}

	@Override
	public Item getItemDropped(int par1, Random par2Random, int par3) {
		return Chisel.dropIceShards ? Chisel.itemIceshard : null;
	}

	@Override
	public int quantityDropped(Random par1Random) {
		return Chisel.dropIceShards ? 1 + par1Random.nextInt(5) : 0;
	}

	@Override
	public CarvableVariation getVariation(int metadata) {
		return carverHelper.getVariation(metadata);
	}

}
