package team.chisel.init;

import team.chisel.Features;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ChiselTabs {

	private static class CustomCreativeTab extends CreativeTabs {

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

	private static boolean atLeastOneModIsLoaded = false;

	public static final CustomCreativeTab tabChisel = new CustomCreativeTab("tabChisel", false);
	public static final CustomCreativeTab tabStoneChiselBlocks = new CustomCreativeTab("tabStoneChiselBlocks", true);
	public static final CustomCreativeTab tabWoodChiselBlocks = new CustomCreativeTab("tabWoodChiselBlocks", true);
	public static final CustomCreativeTab tabMetalChiselBlocks = new CustomCreativeTab("tabMetalChiselBlocks", true);
	public static final CustomCreativeTab tabOtherChiselBlocks = new CustomCreativeTab("tabOtherChiselBlocks", true);
	public static final CustomCreativeTab tabModdedChiselBlocks = new CustomCreativeTab("tabModdedChiselBlocks", true);
	public static final CustomCreativeTab tabStairChiselBlocks = new CustomCreativeTab("tabStairChiselBlocks", true);

	// this serves mostly just to load the static initializers
	public static void preInit() {
		atLeastOneModIsLoaded = Features.oneModdedFeatureLoaded();
	}

	public static void postInit() {

		if (Features.CHISEL.enabled())
			tabChisel.setTabIconItemStack(new ItemStack(ChiselItems.chisel));
		else
			tabChisel.setTabIconItemStack(new ItemStack(Items.stick));
		if (Features.HOLYSTONE.enabled())
			tabStoneChiselBlocks.setTabIconItemStack(new ItemStack(ChiselBlocks.holystone));
		else
			tabStoneChiselBlocks.setTabIconItemStack(new ItemStack(Blocks.stonebrick));
		if (Features.WOOD.enabled())
			tabWoodChiselBlocks.setTabIconItemStack(new ItemStack(ChiselBlocks.planks[0], 1, 1));
		else
			tabWoodChiselBlocks.setTabIconItemStack(new ItemStack(Blocks.planks));
		if (Features.TECHNICAL.enabled())
			tabMetalChiselBlocks.setTabIconItemStack(new ItemStack(ChiselBlocks.technical));
		else
			tabMetalChiselBlocks.setTabIconItemStack(new ItemStack(Blocks.iron_block));
		if (Features.JACKOLANTERN.enabled())
			tabOtherChiselBlocks.setTabIconItemStack(new ItemStack(ChiselBlocks.jackolantern[0]));
		else
			tabOtherChiselBlocks.setTabIconItemStack(new ItemStack(Blocks.lit_pumpkin));
		if (Features.ICE_STAIRS.enabled())
			tabStairChiselBlocks.setTabIconItemStack(new ItemStack(ChiselBlocks.iceStairs[0]));
		else
			tabStairChiselBlocks.setTabIconItemStack(new ItemStack(Blocks.nether_brick_stairs));

		if (atLeastOneModIsLoaded) {
			if (Features.ARCANE.enabled()) {
				tabModdedChiselBlocks.setTabIconItemStack(new ItemStack(ChiselBlocks.arcane));
			} else if (Features.BLOOD_RUNE.enabled()) {
				tabModdedChiselBlocks.setTabIconItemStack(new ItemStack(ChiselBlocks.bloodRune));
			} else {
				if (ChiselBlocks.voidstone != null)
					tabModdedChiselBlocks.setTabIconItemStack(new ItemStack(ChiselBlocks.voidstone));
				else
					tabModdedChiselBlocks.setTabIconItemStack(new ItemStack(Blocks.obsidian));
			}
		}
	}
}
