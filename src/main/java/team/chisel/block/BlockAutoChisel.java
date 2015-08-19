package team.chisel.block;

import team.chisel.Chisel;
import team.chisel.block.tileentity.TileEntityAutoChisel;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockAutoChisel extends BlockContainer {

	public BlockAutoChisel() {
		super(Material.rock);
		setHardness(1F);
		setBlockBounds(0.125f, 0, 0.125f, 0.875f, 0.8125f, 0.875f);
	}

	@Override
	public int getRenderType() {
		return Chisel.renderAutoChiselId;
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
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int metadata, float x1, float y1, float z1) {
		TileEntity tile = world.getTileEntity(x, y, z);
		if (world.isRemote)
			return true;

		if (tile != null && tile instanceof TileEntityAutoChisel && !player.isSneaking()) {
			player.openGui(Chisel.instance, 1, world, x, y, z);
		}

		return true;
	}

	@Override
	public void breakBlock(World world, int x, int y, int z, Block block, int par6) {
		dropItems(world, x, y, z);
		super.breakBlock(world, x, y, z, block, par6);
	}

	private void dropItems(World world, int x, int y, int z) {
		TileEntity tileEntity = world.getTileEntity(x, y, z);

		if (!(tileEntity instanceof TileEntityAutoChisel)) {
			return;
		}

		TileEntityAutoChisel inventory = (TileEntityAutoChisel) world.getTileEntity(x, y, z);

		for (int c = 0; c < inventory.getSizeInventory(); c++) {
			ItemStack stack = inventory.getStackInSlot(c);
			if (stack != null) {
				dropBlockAsItem(world, x, y, z, stack);
			}
		}
	}

	@Override
	public TileEntity createNewTileEntity(World var1, int var2) {
		return new TileEntityAutoChisel();
	}
}
