package com.cricketcraft.chisel.init;

import com.cricketcraft.chisel.Chisel;
import cpw.mods.fml.common.Loader;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class ModTabs {

    private static boolean atLeastOneModIsLoaded = false;

    public static CreativeTabs tabChisel;
    public static CreativeTabs tabStoneChiselBlocks, tabWoodChiselBlocks,tabMetalChiselBlocks, tabOtherChiselBlocks, tabModdedChiselBlocks;

    public static void load(){

        tabChisel = new CreativeTabs("tabChisel")
        {
            @Override
            public Item getTabIconItem() {
                return ModItems.chisel;
            }
        };

        for(String s : Chisel.modsSupported){
            if(Loader.isModLoaded(s)){
                atLeastOneModIsLoaded = true;
            }
        }

        if(atLeastOneModIsLoaded){
            tabModdedChiselBlocks = new CreativeTabs("tabModdedChiselBlocks") {
                @Override
                public Item getTabIconItem() {
                    return Item.getItemFromBlock(ModBlocks.arcane);
                }
            };
        }

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
            	return Item.getItemFromBlock(ModBlocks.planks[0]);
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
