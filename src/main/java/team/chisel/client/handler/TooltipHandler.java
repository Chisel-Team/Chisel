package team.chisel.client.handler;

import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.oredict.OreDictionary;

public class TooltipHandler {

    @SubscribeEvent
    public void onTooltip(ItemTooltipEvent event){
        if (!event.isShowAdvancedItemTooltips()){
            return;
        }
        if (OreDictionary.getOreIDs(event.getItemStack()).length != 0){
            event.getToolTip().add("Ore Dictionary:");
            for (int oreId : OreDictionary.getOreIDs(event.getItemStack())){
                event.getToolTip().add("-"+OreDictionary.getOreName(oreId));
            }
        }
    }
}
