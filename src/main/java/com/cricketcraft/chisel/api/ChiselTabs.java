package com.cricketcraft.chisel.api;

import team.chisel.Features;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import team.chisel.init.ChiselBlocks;
import team.chisel.init.ChiselItems;

public class ChiselTabs {

	public static class CustomCreativeTab extends CreativeTabs {

		private boolean search;

		private ItemStack stack;

		public CustomCreativeTab(String label, boolean searchbar) {
			super(label);
			this.search = searchbar;
		}

		@Override
		public Item getTabIconItem() {
			return stack.getItem();
		}

		public void setTabIconItemStack(ItemStack stack) {
			this.stack = stack;
		}

		@Override
		public ItemStack getIconItemStack() {
			return stack;
		}

		@Override
		@SideOnly(Side.CLIENT)
		public String getBackgroundImageName() {

			return search ? "item_search.png" : super.getBackgroundImageName();
		}

		@Override
		public int getSearchbarWidth() {

			return 89;
		}

		@Override
		public boolean hasSearchBar() {

			return search;
		}
	}


	public static final CustomCreativeTab tabChisel = new CustomCreativeTab("tabChisel", false);
	public static final CustomCreativeTab tabStoneChiselBlocks = new CustomCreativeTab("tabStoneChiselBlocks", true);
	public static final CustomCreativeTab tabWoodChiselBlocks = new CustomCreativeTab("tabWoodChiselBlocks", true);
	public static final CustomCreativeTab tabMetalChiselBlocks = new CustomCreativeTab("tabMetalChiselBlocks", true);
	public static final CustomCreativeTab tabOtherChiselBlocks = new CustomCreativeTab("tabOtherChiselBlocks", true);
	public static final CustomCreativeTab tabModdedChiselBlocks = new CustomCreativeTab("tabModdedChiselBlocks", true);
	public static final CustomCreativeTab tabStairChiselBlocks = new CustomCreativeTab("tabStairChiselBlocks", true);


}
