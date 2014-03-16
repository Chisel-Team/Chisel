package info.jbcs.minecraft.chisel.block;


import java.util.Random;

import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockCloud extends BlockMarble {

	public BlockCloud() {
		super(Material.ice);
		
		useNeighborBrightness = true;
	}

	@Override
	public void onEntityCollidedWithBlock(World world, int i, int j, int k, Entity entity) {
		entity.fallDistance = 0.0F;
		
		if (entity.motionY < 0.0D) {
			entity.motionY *= 0.0050000000000000001D;
		} else if(entity.motionY>0){
			entity.motionY+=0.0285;
		}
	}

	@Override
	public boolean isOpaqueCube() {
		return false;
	}

	@Override
	public int getRenderBlockPass() {
		return 1;
	}


	@Override
	public boolean shouldSideBeRendered(IBlockAccess iblockaccess, int i, int j, int k, int l) {
		return super.shouldSideBeRendered(iblockaccess, i, j, k, 1 - l);
	}

	@Override
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int i, int j, int k) {
		return AxisAlignedBB.getBoundingBox(i+0.2, j, k+0.2, i+0.8, j+0.2, k+0.8);
	}

	@Override
	public Item getItemDropped(int par1, Random rand, int par3) {
		return Item.getItemFromBlock(this);
	}

	@Override
	public int quantityDropped(Random rand) {
		return 0;
	}

	@Override
	public int damageDropped(int meta) {
		return 0;
	}
}
