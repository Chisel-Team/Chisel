package com.cricketcraft.chisel.block;

import net.minecraft.entity.Entity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;

import com.cricketcraft.chisel.client.GeneralChiselClient;
import com.cricketcraft.chisel.config.Configurations;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;

public class BlockConcrete extends BlockCarvable {

	public BlockConcrete() {
		super();
	}

	@Override
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World par1World, int par2, int par3, int par4) {
		if (Configurations.fullBlockConcrete)
			return AxisAlignedBB.getBoundingBox(par2, par3, par4, (par2 + 1), ((par3 + 1)), (par4 + 1));
		else
			return AxisAlignedBB.getBoundingBox(par2, par3, par4, (par2 + 1), ((par3 + 1) - 0.125F), (par4 + 1));
	}

	@Override
	public void onEntityCollidedWithBlock(World world, int x, int y, int z, Entity entity) {
		if (FMLCommonHandler.instance().getEffectiveSide() != Side.CLIENT)
			return;
		if (Configurations.concreteVelocity == 0)
			return;

		GeneralChiselClient.speedupPlayer(world, entity, Configurations.concreteVelocity);
	}

}
