package team.chisel.common.init;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.IFuelHandler;
import net.minecraftforge.oredict.OreDictionary;

public class ChiselFuelHandler implements IFuelHandler {
    @Override
    public int getBurnTime(ItemStack itemStack) {
        if (itemStack.isEmpty()) {
            return 0;
        }
        for(int id : OreDictionary.getOreIDs(itemStack))
        {
            if(OreDictionary.getOreName(id).matches("blockFuelCoke"))
            {
                return 32000;
            }
            else if(OreDictionary.getOreName(id).matches("blockCoal"))
            {
                return 16000;
            }
            else if(OreDictionary.getOreName(id).matches("blockCharcoal"))
            {
                return 16000;
            }
        }

        return 0;
    }
}
