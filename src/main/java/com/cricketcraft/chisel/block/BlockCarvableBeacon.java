package com.cricketcraft.chisel.block;

import net.minecraft.block.BlockBeacon;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import com.cricketcraft.chisel.api.ICarvable;
import com.cricketcraft.chisel.api.carving.CarvableHelper;
import com.cricketcraft.chisel.api.carving.IVariationInfo;
import com.cricketcraft.chisel.block.tileentity.TileEntityCarvableBeacon;
import com.cricketcraft.chisel.init.ChiselTabs;

public class BlockCarvableBeacon extends BlockBeacon implements ICarvable {

	public static int renderId;

	public CarvableHelper carverHelper;

	public BlockCarvableBeacon() {
		super();
		setCreativeTab(ChiselTabs.tabOtherChiselBlocks);
		setLightLevel(5.0F);
		setBlockTextureName("beacon");
		carverHelper = new CarvableHelper(this);
	}

	@Override
	public TileEntity createNewTileEntity(World world, int meta) {
		return new TileEntityCarvableBeacon();
	}

	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int par5, float hitX, float hitY, float hitZ) {
		if (world.isRemote) {
			return true;
		} else {
			TileEntityCarvableBeacon tileentitybeacon = (TileEntityCarvableBeacon) world.getTileEntity(x, y, z);

			if (tileentitybeacon != null) {
				player.func_146104_a(tileentitybeacon);
			}

			return true;
		}
	}

	@Override
	public int getRenderType() {
		return renderId;
	}

	@Override
	public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase player, ItemStack stack) {
		super.onBlockPlacedBy(world, x, y, z, player, stack);

		if (stack.hasDisplayName()) {
			((TileEntityCarvableBeacon) world.getTileEntity(x, y, z)).func_145999_a(stack.getDisplayName());
		}
	}

	@Override
	public IVariationInfo getManager(IBlockAccess world, int x, int y, int z, int metadata) {
		return carverHelper.getVariation(metadata);
	}

	@Override
	public IVariationInfo getManager(int meta) {
		return carverHelper.getVariation(meta);
	}
}
