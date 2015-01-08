package com.cricketcraft.chisel.init;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;

import com.cricketcraft.chisel.Chisel;

import cpw.mods.fml.common.Loader;

public class ChiselTabs {

	private static boolean atLeastOneModIsLoaded = false;

	public static CreativeTabs tabChisel;
	public static CreativeTabs tabStoneChiselBlocks, tabWoodChiselBlocks, tabMetalChiselBlocks, tabOtherChiselBlocks, tabModdedChiselBlocks;

	public static void load() {

		tabChisel = new CreativeTabs("tabChisel") {

			@Override
			public Item getTabIconItem() {
				return ChiselItems.chisel;
			}
		};

		for (String s : Chisel.modsSupported) {
			if (Loader.isModLoaded(s)) {
				atLeastOneModIsLoaded = true;
			}
		}

		if (atLeastOneModIsLoaded) {
			tabModdedChiselBlocks = new CreativeTabs("tabModdedChiselBlocks") {

				@Override
				public Item getTabIconItem() {

					if (Loader.isModLoaded("Thaumcraft")) {
						return Item.getItemFromBlock(ChiselBlocks.arcane);
					} else if (Loader.isModLoaded("AWWayOfTime")) {
						return Item.getItemFromBlock(ChiselBlocks.bloodRune);
					} else {
						return Item.getItemFromBlock(ChiselBlocks.voidstone);
					}
				}
			};
		}

		tabStoneChiselBlocks = new CreativeTabs("tabStoneChiselBlocks") {

			@Override
			public Item getTabIconItem() {
				return Item.getItemFromBlock(ChiselBlocks.holystone);
			}
		};

		tabWoodChiselBlocks = new CreativeTabs("tabWoodChiselBlocks") {

			@Override
			public Item getTabIconItem() {
				return Item.getItemFromBlock(Blocks.bookshelf);
			}
		};

		tabMetalChiselBlocks = new CreativeTabs("tabMetalChiselBlocks") {

			@Override
			public Item getTabIconItem() {
				return Item.getItemFromBlock(ChiselBlocks.technical);
			}
		};

		tabOtherChiselBlocks = new CreativeTabs("tabOtherChiselBlocks") {

			@Override
			public Item getTabIconItem() {
				return Item.getItemFromBlock(ChiselBlocks.jackolantern[0]);
			}
		};
	}
}
