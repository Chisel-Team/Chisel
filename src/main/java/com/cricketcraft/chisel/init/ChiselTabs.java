package com.cricketcraft.chisel.init;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ChiselTabs {

	private static class CustomCreativeTab extends CreativeTabs {

		private ItemStack stack;

		public CustomCreativeTab(String label) {
			super(label);
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
	}

	private static boolean atLeastOneModIsLoaded = false;

	public static final CustomCreativeTab tabChisel = new CustomCreativeTab("tabChisel");
	public static final CustomCreativeTab tabStoneChiselBlocks = new CustomCreativeTab("tabStoneChiselBlocks");
	public static final CustomCreativeTab tabWoodChiselBlocks = new CustomCreativeTab("tabWoodChiselBlocks");
	public static final CustomCreativeTab tabMetalChiselBlocks = new CustomCreativeTab("tabMetalChiselBlocks");
	public static final CustomCreativeTab tabOtherChiselBlocks = new CustomCreativeTab("tabOtherChiselBlocks");
	public static final CustomCreativeTab tabModdedChiselBlocks = new CustomCreativeTab("tabModdedChiselBlocks");

	// this serves mostly just to load the static initializers
	public static void preInit() {
		atLeastOneModIsLoaded = Features.oneModdedFeatureLoaded();
	}

	public static void postInit() {

		tabChisel.setTabIconItemStack(new ItemStack(ChiselItems.chisel));
		tabStoneChiselBlocks.setTabIconItemStack(new ItemStack(ChiselBlocks.holystone));
		tabWoodChiselBlocks.setTabIconItemStack(new ItemStack(ChiselBlocks.planks[0], 1, 1));
		tabMetalChiselBlocks.setTabIconItemStack(new ItemStack(ChiselBlocks.technical));
		tabOtherChiselBlocks.setTabIconItemStack(new ItemStack(ChiselBlocks.jackolantern[0]));

		if (atLeastOneModIsLoaded) {
			if (Features.ARCANE.enabled()) {
				tabModdedChiselBlocks.setTabIconItemStack(new ItemStack(ChiselBlocks.arcane));
			} else if (Features.BLOOD_RUNE.enabled()) {
				tabModdedChiselBlocks.setTabIconItemStack(new ItemStack(ChiselBlocks.bloodRune));
			} else {
				tabModdedChiselBlocks.setTabIconItemStack(new ItemStack(ChiselBlocks.voidstone));
			}
		}
	}
}
