package team.chisel.block;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovementInput;
import net.minecraft.util.MovementInputFromOptions;
import team.chisel.config.Configurations;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent.Phase;
import cpw.mods.fml.common.gameevent.TickEvent.PlayerTickEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockConcrete extends BlockCarvable {

	public BlockConcrete() {
		super();
		FMLCommonHandler.instance().bus().register(this);
	}

	@SideOnly(Side.CLIENT)
	private static MovementInput manualInputCheck;

	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void speedupPlayer(PlayerTickEvent event) {
		if (event.phase == Phase.START && event.side.isClient() && event.player.onGround) {
			if (manualInputCheck == null) {
				manualInputCheck = new MovementInputFromOptions(Minecraft.getMinecraft().gameSettings);
			}
			EntityPlayerSP player = (EntityPlayerSP) event.player;
			Block below = player.worldObj.getBlock(MathHelper.floor_double(player.posX), MathHelper.floor_double(player.posY) - 2, MathHelper.floor_double(player.posZ));
			if (below == this) {
				manualInputCheck.updatePlayerMoveState();
				if (manualInputCheck.moveForward != 0 || manualInputCheck.moveStrafe != 0) {
					player.motionX *= Configurations.concreteVelocityMult + 0.05;
					player.motionZ *= Configurations.concreteVelocityMult + 0.05;
				}
			}
		}
	}
}
