package com.cricketcraft.chisel.init;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class ModTabs {

    public static CreativeTabs tabChisel;
    public static CreativeTabs tabStoneChiselBlocks, tabWoodChiselBlocks,tabMetalChiselBlocks, tabOtherChiselBlocks;

    public static void load(){

        tabChisel = new CreativeTabs("tabChisel")
        {
            @Override
            public Item getTabIconItem() {
                return ModItems.chisel;
            }
        };
        tabStoneChiselBlocks = new CreativeTabs("tabStoneChiselBlocks") {
            @Override
            public Item getTabIconItem()
            {
                return Item.getItemFromBlock(ModBlocks.holystone);
            }
        };
        tabWoodChiselBlocks = new CreativeTabs("tabWoodChiselBlocks") {
            @Override
            public Item getTabIconItem()
            {
            	//Broken
                //return Item.getItemFromBlock(ModBlocks.bookshelf);
            	return ModItems.chisel;
            }
        };
        tabMetalChiselBlocks = new CreativeTabs("tabMetalChiselBlocks") {
            @Override
            public Item getTabIconItem()
            {
                return Item.getItemFromBlock(ModBlocks.technical);
            }
        };
        tabOtherChiselBlocks = new CreativeTabs("tabOtherChiselBlocks") {
            @Override
            public Item getTabIconItem()
            {
                return Item.getItemFromBlock(ModBlocks.jackolantern[0]);
            }
        };
    }
}
