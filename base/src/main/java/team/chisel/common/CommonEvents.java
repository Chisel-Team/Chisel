package team.chisel.common;

import net.minecraftforge.event.furnace.FurnaceFuelBurnTimeEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import team.chisel.Features;

@Mod.EventBusSubscriber
public class CommonEvents {

    @SubscribeEvent
    public static void furnaceFuel(FurnaceFuelBurnTimeEvent event) {
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
