package team.chisel.client.handler;

import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.oredict.OreDictionary;

public class TooltipHandler {

    @SubscribeEvent
    public void onTooltip(ItemTooltipEvent event){
        if (!event.showAdvancedItemTooltips){
            return;
        }
        if (OreDictionary.getOreIDs(event.itemStack).length != 0){
            event.toolTip.add("Ore Dictionary:");
            for (int oreId : OreDictionary.getOreIDs(event.itemStack)){
                event.toolTip.add("-"+OreDictionary.getOreName(oreId));
            }
        }
    }
}
