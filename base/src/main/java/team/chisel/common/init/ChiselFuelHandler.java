package team.chisel.common.init;

import net.minecraftforge.event.furnace.FurnaceFuelBurnTimeEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import team.chisel.Features;

@EventBusSubscriber
public class ChiselFuelHandler {

    @SubscribeEvent
    public static void onBurnTime(FurnaceFuelBurnTimeEvent event) {
        Features.COAL.values().forEach(e -> {
            if (event.getItemStack().getItem() == e.asStack().getItem()) {
                event.setBurnTime(1600);
            }
        });

        Features.CHARCOAL.values().forEach(e -> {
            if (event.getItemStack().getItem() == e.asStack().getItem()) {
                event.setBurnTime(1600);
            }
        });

        Features.CARPET.values().forEach(m -> m.values().forEach(e -> {
            if (event.getItemStack().getItem() == e.asStack().getItem()) {
                event.setBurnTime(67);
            }
        }));

        Features.PLANKS.values().forEach(m -> m.values().forEach(e -> {
            if (event.getItemStack().getItem() == e.asStack().getItem()) {
                event.setBurnTime(300);
            }
        }));

        Features.WOOL.values().forEach(m -> m.values().forEach(e -> {
            if (event.getItemStack().getItem() == e.asStack().getItem()) {
                event.setBurnTime(100);
            }
        }));
    }
}
