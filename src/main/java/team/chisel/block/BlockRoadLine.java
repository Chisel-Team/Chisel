package team.chisel.block;

import team.chisel.Chisel;
import team.chisel.config.Configurations;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.init.Blocks;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockRoadLine extends BlockCarvable {

	public IIcon aloneIcon[] = new IIcon[4];
	public IIcon halfLineIcon[] = new IIcon[4];
	public IIcon fullLineIcon[] = new IIcon[4];

	public BlockRoadLine() {
		super(Material.circuits);

		if (Configurations.useRoadLineTool) {
			this.setHarvestLevel(Configurations.getRoadLineTool, Configurations.roadLineToolLevel);
		}
		this.setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, 0.00390625f, 1.0f);
		// this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.0625F, 1.0F);
	}

	@Override
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World par1World, int par2, int par3, int par4) {
		return null;
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
		return Chisel.roadLineId;
	}

	@Override
	public boolean canPlaceBlockAt(World par1World, int par2, int par3, int par4) {
		return World.doesBlockHaveSolidTopSurface(par1World, par2, par3 - 1, par4) || par1World.getBlock(par2, par3 - 1, par4).equals(Blocks.glowstone);
	}

	@Override
	public void onNeighborBlockChange(World par1World, int par2, int par3, int par4, Block block) {
		if (par1World.isRemote)
			return;

		if (!this.canPlaceBlockAt(par1World, par2, par3, par4)) {
			this.dropBlockAsItem(par1World, par2, par3, par4, 0, 0);
			par1World.setBlockToAir(par2, par3, par4);
		}

		super.onNeighborBlockChange(par1World, par2, par3, par4, block);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister reg) {
		for (int i = 0; i < 4; i++) {
			String[] texNames = { "white", "yellow", "double-white", "double-yellow" };
			aloneIcon[i] = reg.registerIcon("Chisel:line-marking/" + texNames[i] + "-center");
			halfLineIcon[i] = reg.registerIcon("Chisel:line-marking/" + texNames[i] + "-side");
			fullLineIcon[i] = reg.registerIcon("Chisel:line-marking/" + texNames[i] + "-long");
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int side, int metadata) {
		return aloneIcon[metadata % aloneIcon.length];
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(IBlockAccess world, int x, int y, int z, int side) {
		return getIcon(side, world.getBlockMetadata(x, y, z));
	}
}
