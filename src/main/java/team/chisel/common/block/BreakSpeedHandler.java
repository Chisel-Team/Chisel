package team.chisel.common.block;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import team.chisel.Chisel;

@EventBusSubscriber(modid = Chisel.MOD_ID)
public class BreakSpeedHandler {

    @SubscribeEvent
    public static void onBreakSpeed(PlayerEvent.BreakSpeed event) {
        Block block = event.getState().getBlock();
        if (block instanceof BlockCarvable && block.getRegistryName().getResourcePath().startsWith("wool_")) {
            event.setNewSpeed(event.getNewSpeed() * event.getEntityPlayer().getHeldItemMainhand().getStrVsBlock(Blocks.WOOL.getDefaultState()));
        }
    }
}
