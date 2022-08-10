package team.chisel.client.handler;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.Input;
import net.minecraft.client.player.KeyboardInput;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.event.TickEvent.PlayerTickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import team.chisel.common.config.Configurations;

import java.util.HashSet;
import java.util.Set;

@EventBusSubscriber(Dist.CLIENT)
public class BlockSpeedHandler {

    // FIXME temporary hack
    public static final Set<Block> speedupBlocks = new HashSet<>();

    private static Input manualInputCheck;

    @SubscribeEvent
    public static void speedupPlayer(PlayerTickEvent event) {
        if (event.phase == Phase.START && event.side.isClient() && event.player.isOnGround() && event.player instanceof LocalPlayer player) {
            if (manualInputCheck == null) {
                manualInputCheck = new KeyboardInput(Minecraft.getInstance().options);
            }
            BlockState below = player.getCommandSenderWorld().getBlockState(new BlockPos(player.position().subtract(0, 1 / 16D, 0)));
            if (speedupBlocks.contains(below.getBlock())) {
                manualInputCheck.tick(false);
                if ((manualInputCheck.forwardImpulse != 0 || manualInputCheck.leftImpulse != 0) && !player.isInWater()) {
                    player.setDeltaMovement(player.getDeltaMovement().multiply(Configurations.concreteVelocityMult + 0.05, 1, Configurations.concreteVelocityMult + 0.05));
                }
            }
        }
    }
}

