package info.jbcs.minecraft.chisel.block;

import info.jbcs.minecraft.chisel.Chisel;
import info.jbcs.minecraft.chisel.GeneralChiselClient;
import net.minecraft.entity.Entity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;

public class BlockConcrete extends BlockMarble {

	public BlockConcrete() {
		super();
	}

	@Override
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World par1World, int par2, int par3, int par4) {
        float f = 0.125F;
        return AxisAlignedBB.getAABBPool().getAABB(par2, par3, par4, (par2 + 1), ((par3 + 1) - f), (par4 + 1));
	}

	@Override
	public void onEntityCollidedWithBlock(World world, int x, int y, int z, Entity entity) {
		if (FMLCommonHandler.instance().getEffectiveSide() != Side.CLIENT)
			return;
		if(Chisel.concreteVelocity==0)
			return;
		
		GeneralChiselClient.speedupPlayer(world,entity,Chisel.concreteVelocity);
	}

}
