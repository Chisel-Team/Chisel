package team.chisel.block;

import java.util.Random;

import team.chisel.config.Configurations;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockCloud extends BlockCarvable {

	public BlockCloud() {
		super(Material.ice);
		useNeighborBrightness = true;
	}

	@Override
	public void onEntityCollidedWithBlock(World world, int i, int j, int k, Entity entity) {
		entity.fallDistance = 0.0F;

		if (entity.motionY < 0.0D) {
			entity.motionY *= 0.0050000000000000001D;
		} else if (entity.motionY > 0) {
			entity.motionY += 0.0285;
		}
	}

	@Override
	public boolean isOpaqueCube() {
		return false;
	}

	@Override
	public int getRenderBlockPass() {
		return Configurations.ghostCloud ? 1 : 0;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean shouldSideBeRendered(IBlockAccess world, int x, int y, int z, int side) {
		Block block = world.getBlock(x, y, z);
		if (block == this) {
			return false;
		}

		return super.shouldSideBeRendered(world, x, y, z, side);
	}

	@Override
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int i, int j, int k) {
		return AxisAlignedBB.getBoundingBox(i + 0.2, j, k + 0.2, i + 0.8, j + 0.2, k + 0.8);
	}

	@Override
	public Item getItemDropped(int par1, Random rand, int par3) {
		return Item.getItemFromBlock(this);
	}

	@Override
	public int quantityDropped(Random rand) {
		return rand.nextInt(2);
	}

	@Override
	public int damageDropped(int meta) {
		return meta;
	}
}
