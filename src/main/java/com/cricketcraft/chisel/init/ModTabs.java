package com.cricketcraft.chisel.init;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class ModTabs {

    public static CreativeTabs tabChisel;
    public static CreativeTabs tabChiselBlocks;

    public static void load(){
        tabChisel = new CreativeTabs("tabChisel")
        {
            @Override
            public Item getTabIconItem() {
                return ModItems.chisel;
            }
        };

        tabChiselBlocks = new CreativeTabs("tabChiselBlocks") {
            @Override
            public Item getTabIconItem()
            {
                return Item.getItemFromBlock(ModBlocks.holystone);
            }
        };
    }
}
