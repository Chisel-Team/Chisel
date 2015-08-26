package team.chisel.block;

import java.util.Random;

import team.chisel.Chisel;
import team.chisel.api.ChiselTabs;
import team.chisel.api.ICarvable;
import team.chisel.api.carving.CarvableHelper;
import team.chisel.api.carving.IVariationInfo;
import net.minecraft.block.BlockTorch;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockCarvableTorch extends BlockTorch implements ICarvable {

	public CarvableHelper carverHelper;
	private String texLocation;
	protected boolean emitsParticles = true;
	public int idx;

	public BlockCarvableTorch(int idx, String tex) {
		super();
		carverHelper = new CarvableHelper(this);
		setCreativeTab(ChiselTabs.tabOtherChiselBlocks);
		setLightLevel(0.9375F);
		setBlockName("torch");
		this.idx = idx;
		this.texLocation = tex;
	}

	@Override
	public IVariationInfo getManager(IBlockAccess world, int x, int y, int z, int metadata) {
		return carverHelper.getVariation(metadata);
	}

	@Override
	public IVariationInfo getManager(int meta) {
		return carverHelper.getVariation(meta);
	}

	@Override
	public void registerBlockIcons(IIconRegister icon) {
		this.blockIcon = icon.registerIcon(Chisel.MOD_ID + ":torch/" + texLocation);
	}

	public BlockCarvableTorch disableParticles() {
		this.emitsParticles = false;
		return this;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void randomDisplayTick(World p_149734_1_, int p_149734_2_, int p_149734_3_, int p_149734_4_, Random p_149734_5_) {
		if (emitsParticles) {
			int l = p_149734_1_.getBlockMetadata(p_149734_2_, p_149734_3_, p_149734_4_);
			double d0 = p_149734_2_ + 0.5F;
			double d1 = p_149734_3_ + 0.7F;
			double d2 = p_149734_4_ + 0.5F;
			double d3 = 0.2199999988079071D;
			double d4 = 0.27000001072883606D;

			if (l == 1) {
				p_149734_1_.spawnParticle("smoke", d0 - d4, d1 + d3, d2, 0.0D, 0.0D, 0.0D);
				p_149734_1_.spawnParticle("flame", d0 - d4, d1 + d3, d2, 0.0D, 0.0D, 0.0D);
			} else if (l == 2) {
				p_149734_1_.spawnParticle("smoke", d0 + d4, d1 + d3, d2, 0.0D, 0.0D, 0.0D);
				p_149734_1_.spawnParticle("flame", d0 + d4, d1 + d3, d2, 0.0D, 0.0D, 0.0D);
			} else if (l == 3) {
				p_149734_1_.spawnParticle("smoke", d0, d1 + d3, d2 - d4, 0.0D, 0.0D, 0.0D);
				p_149734_1_.spawnParticle("flame", d0, d1 + d3, d2 - d4, 0.0D, 0.0D, 0.0D);
			} else if (l == 4) {
				p_149734_1_.spawnParticle("smoke", d0, d1 + d3, d2 + d4, 0.0D, 0.0D, 0.0D);
				p_149734_1_.spawnParticle("flame", d0, d1 + d3, d2 + d4, 0.0D, 0.0D, 0.0D);
			} else {
				p_149734_1_.spawnParticle("smoke", d0, d1, d2, 0.0D, 0.0D, 0.0D);
				p_149734_1_.spawnParticle("flame", d0, d1, d2, 0.0D, 0.0D, 0.0D);
			}
		}
	}
}