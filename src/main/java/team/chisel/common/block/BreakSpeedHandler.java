package team.chisel.common.block;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import team.chisel.Chisel;

@EventBusSubscriber(modid = Chisel.MOD_ID)
public class BreakSpeedHandler {

    @SubscribeEvent
    public static void onBreakSpeed(PlayerEvent.BreakSpeed event) {
        Block block = event.getState().getBlock();
        if (block instanceof BlockCarvable && block.getRegistryName().getPath().startsWith("wool_")) {
            event.setNewSpeed(event.getNewSpeed() * event.getPlayer().getMainHandItem().getDestroySpeed(Blocks.WHITE_WOOL.defaultBlockState()));
        }
    }
}
