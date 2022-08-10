package team.chisel.common.init;

import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.furnace.FurnaceFuelBurnTimeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import team.chisel.common.Reference;

@EventBusSubscriber(modid = Reference.MOD_ID)
public class ChiselFuelHandler {
    
    public static void onBurnTime(FurnaceFuelBurnTimeEvent event) {
        ItemStack itemStack = event.getItemStack();
        if (itemStack.isEmpty()) {
            return;
        }
        // TODO 1.14 tags
//        for(int id : OreDictionary.getOreIDs(itemStack))
//        {
//            if(OreDictionary.getOreName(id).matches("blockFuelCoke"))
//            {
//                return 32000;
//            }
//            else if(OreDictionary.getOreName(id).matches("blockCoal"))
//            {
//                return 16000;
//            }
//            else if(OreDictionary.getOreName(id).matches("blockCharcoal"))
//            {
//                return 16000;
//            }
//        }
    }
}
