package team.chisel.client.handler;

import java.util.HashSet;
import java.util.Set;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.util.MovementInput;
import net.minecraft.util.MovementInputFromOptions;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.event.TickEvent.PlayerTickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import team.chisel.common.config.Configurations;

@EventBusSubscriber(Dist.CLIENT)
public class BlockSpeedHandler {
    
    // FIXME temporary hack
    public static final Set<Block> speedupBlocks = new HashSet<>();

    private static MovementInput manualInputCheck;

    @SubscribeEvent
    public static void speedupPlayer(PlayerTickEvent event) {
        if (event.phase == Phase.START && event.side.isClient() && event.player.isOnGround() && event.player instanceof ClientPlayerEntity) {
            if (manualInputCheck == null) {
                manualInputCheck = new MovementInputFromOptions(Minecraft.getInstance().gameSettings);
            }
            ClientPlayerEntity player = (ClientPlayerEntity) event.player;
            BlockState below = player.getEntityWorld().getBlockState(new BlockPos(player.getPositionVec().subtract(0, 1 / 16D, 0)));
            if (speedupBlocks.contains(below.getBlock())) {
                manualInputCheck.tickMovement(false);
                if ((manualInputCheck.moveForward != 0 || manualInputCheck.moveStrafe != 0) && !player.isInWater()) {
                    player.setMotion(player.getMotion().mul(Configurations.concreteVelocityMult + 0.05, 1, Configurations.concreteVelocityMult + 0.05));
                }
            }
        }
    }
}

