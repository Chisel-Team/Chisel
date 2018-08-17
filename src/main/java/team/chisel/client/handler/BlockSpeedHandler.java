package team.chisel.client.handler;

import java.util.HashSet;
import java.util.Set;
import java.lang.Math;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.MovementInput;
import net.minecraft.util.MovementInputFromOptions;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import team.chisel.common.config.Configurations;

@EventBusSubscriber(Side.CLIENT)
public class BlockSpeedHandler {
    
    // FIXME temporary hack
    public static final Set<Block> speedupBlocks = new HashSet<>();

    @SideOnly(Side.CLIENT)
    private static MovementInput manualInputCheck;

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public static void speedupPlayer(PlayerTickEvent event) {
        if (event.phase == Phase.START && event.side.isClient() && event.player.onGround && event.player instanceof EntityPlayerSP) {
            if (manualInputCheck == null) {
                manualInputCheck = new MovementInputFromOptions(Minecraft.getMinecraft().gameSettings);
            }
            EntityPlayerSP player = (EntityPlayerSP) event.player;
            BlockPos pos = new BlockPos(
                Math.floor(player.posX),
                Math.floor(player.posY),
                Math.floor(player.posZ));
            IBlockState below = player.getEntityWorld().getBlockState(pos.down());
            if (speedupBlocks.contains(below.getBlock())) {
                manualInputCheck.updatePlayerMoveState();
                if ((manualInputCheck.moveForward != 0 || manualInputCheck.moveStrafe != 0) && !player.isInWater()) {
                    player.motionX *= Configurations.concreteVelocityMult + 0.05;
                    player.motionZ *= Configurations.concreteVelocityMult + 0.05;
                }
            }
        }
    }
}

