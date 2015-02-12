package com.cricketcraft.chisel.block;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import com.cricketcraft.chisel.Chisel;
import com.cricketcraft.chisel.api.ICarvable;
import com.cricketcraft.chisel.block.tileentity.TileEntityPresent;
import com.cricketcraft.chisel.carving.CarvableHelper;
import com.cricketcraft.chisel.carving.CarvableVariation;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockPresent extends Block implements ICarvable {

	public CarvableHelper carverHelper;
	
	private float minX, minY, minZ, maxX, maxY, maxZ;

	public BlockPresent() {
		super(Material.wood);
		carverHelper = new CarvableHelper();
		minX = 0.0625F;
		minZ = 0.0625F;
		maxX = 0.9375F;
		maxY = 0.875F;
		maxZ = 0.9375F;
		setBlockBounds(minX, minY, minZ, maxX, maxY, maxZ);
	}

	public String getModelTexture(int type) {
		return "textures/blocks/present/presentChest" + type;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void getSubBlocks(Item item, CreativeTabs tab, List list) {
		for (int i = 0; i < 16; i++) {
			list.add(new ItemStack(item, 1, i));
		}
	}

	@Override
	public boolean isOpaqueCube() {
		return false;
	}

	@Override
	public boolean renderAsNormalBlock() {
		return false;
	}

	@Override
	public int getRenderType() {
		return -1;
	}

	@Override
	public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase player, ItemStack stack) {
		super.onBlockPlacedBy(world, x, y, z, player, stack);
		int heading = MathHelper.floor_double(player.rotationYaw * 4.0F / 360.0F + 0.5D) & 3;
		TileEntityPresent te = (TileEntityPresent) world.getTileEntity(x, y, z);
		world.setBlockMetadataWithNotify(x, y, z, stack.getItemDamage(), 3);
		te.setRotation(heading);
		te.findConnections();
		if (te.isConnected()) {
			TileEntityPresent other = te.getConnection();
			other.setRotation(heading);
		}
		world.markBlockForUpdate(x, y, z);
	}

	@Override
	public void breakBlock(World world, int x, int y, int z, Block present, int meta) {

		TileEntityPresent tileEntityPresent = (TileEntityPresent) world.getTileEntity(x, y, z);

		if (tileEntityPresent != null) {
			for (int c = 0; c < tileEntityPresent.getTrueSizeInventory(); c++) {
				ItemStack itemStack = tileEntityPresent.getTrueStackInSlot(c);

				if (itemStack != null) {
					dropBlockAsItem(world, x, y, z, itemStack);
				}
			}

			world.func_147453_f(x, y, z, this);
		}

		super.breakBlock(world, x, y, z, this, meta);
	}

	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int hitX, float hitY, float hitZ, float p_149727_9_) {
		if (!world.isSideSolid(x, y + 1, z, ForgeDirection.DOWN)) {
			player.openGui(Chisel.instance, 2, world, x, y, z);
			return true;
		}
		return false;
	}

	@Override
	public AxisAlignedBB getSelectedBoundingBoxFromPool(World world, int x, int y, int z) {
		return getBoundingBox((TileEntityPresent) world.getTileEntity(x, y, z));
	}

	@Override
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z) {
		return getBoundingBox((TileEntityPresent) world.getTileEntity(x, y, z));
	}

	public AxisAlignedBB getBoundingBox(TileEntityPresent me) {
		if (me == null) {
			return AxisAlignedBB.getBoundingBox(minX, minY, minZ, maxX, maxY, maxZ);
		}
		int x = me.xCoord, y = me.yCoord, z = me.zCoord;
		if (me.isConnected()) {
			ForgeDirection dir = me.getConnectionDir();
			// warning: magic below! Do not change this conditional
			if (dir == ForgeDirection.EAST || (me.isParent() && dir == ForgeDirection.SOUTH) || (!me.isParent() && dir == ForgeDirection.SOUTH)) {
				return AxisAlignedBB.getBoundingBox(x + minX, y + minY, z + minZ, x + maxX + dir.offsetX, y + maxY, z + maxZ + dir.offsetZ);
			} else {
				return AxisAlignedBB.getBoundingBox(x + dir.offsetX + minX, y + minY, z + dir.offsetZ + minZ, x + maxX, y + maxY, z + maxZ);
			}
		}
		return AxisAlignedBB.getBoundingBox(x + minX, y + minY, z + minZ, x + maxX, y + maxY, z + maxZ);
	}

    public void setBlockBoundsBasedOnState(IBlockAccess world, int x, int y, int z)
	{
		TileEntityPresent te = (TileEntityPresent) world.getTileEntity(x, y, z);
		if (!te.isConnected()) {
			setBlockBounds(0.0625F, 0, 0.0625F, 0.9375F, 0.875F, 0.9365F);
		} else {
			ForgeDirection dir = te.getConnectionDir();
			switch (dir) {
			case EAST:
				setBlockBounds(0.0625F, 0, 0.0625F, 1, 0.875F, 0.9365F);
				break;
			case NORTH:
				setBlockBounds(0.0625F, 0, 0, 0.9375F, 0.875F, 0.9365F);
				break;
			case SOUTH:
				setBlockBounds(0.0625F, 0, 0.0625F, 0.9375F, 0.875F, 1);
				break;
			case WEST:
				setBlockBounds(0, 0, 0.0625F, 0.9375F, 0.875F, 0.9365F);
				break;
			default:
				setBlockBounds(0.0625F, 0, 0.0625F, 0.9375F, 0.875F, 0.9365F);
				break;
			}
		}
	}
    
	@Override
	public boolean hasTileEntity(int metadata) {
		return true;
	}

	@Override
	public TileEntity createTileEntity(World world, int metadata) {
		return new TileEntityPresent();
	}

	@Override
	public boolean hasComparatorInputOverride() {
		return true;
	}

	@Override
	public int getComparatorInputOverride(World world, int x, int y, int z, int strength) {
		TileEntity te = world.getTileEntity(x, y, z);
		if (te instanceof TileEntityPresent) {
			return Container.calcRedstoneFromInventory((TileEntityPresent) te);
		}
		return 0;
	}
	
	@Override
	public int damageDropped(int meta) {
		return meta;
	}

	@Override
	public ItemStack getPickBlock(MovingObjectPosition target, World world, int x, int y, int z) {
		return new ItemStack(this, 1, world.getBlockMetadata(x, y, z));
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister register) {
		this.blockIcon = register.registerIcon("planks_oak");
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