package team.chisel.block;

import java.util.List;

import net.minecraft.block.BlockBeacon;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import team.chisel.block.tileentity.TileEntityCarvableBeacon;
import team.chisel.init.ChiselBlocks;

import com.cricketcraft.chisel.api.ChiselTabs;
import com.cricketcraft.chisel.api.ICarvable;
import com.cricketcraft.chisel.api.carving.CarvableHelper;
import com.cricketcraft.chisel.api.carving.IVariationInfo;

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

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void getSubBlocks(Item item, CreativeTabs p_149666_2_, List list) {
		for (int i = 0; i < 16; i++) {
			list.add(new ItemStack(item, 1, i));
		}
	}

	@Override
	public ItemStack getPickBlock(MovingObjectPosition target, World world, int x, int y, int z) {
		return new ItemStack(ChiselBlocks.beacon, 1, world.getBlockMetadata(x, y, z));
	}
	
	@Override
	public int damageDropped(int meta) {
		return meta;
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
