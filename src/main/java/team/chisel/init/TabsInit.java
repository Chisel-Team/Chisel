package team.chisel.init;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import team.chisel.Features;
import team.chisel.api.ChiselTabs;

public class TabsInit {

    private static boolean atLeastOneModIsLoaded = false;

    // this serves mostly just to load the static initializers
    public static void preInit() {
        atLeastOneModIsLoaded = Features.oneModdedFeatureLoaded();
    }

    public static void postInit() {

        if (Features.CHISEL.enabled())
            ChiselTabs.tabChisel.setTabIconItemStack(new ItemStack(ChiselItems.chisel));
        else
            ChiselTabs.tabChisel.setTabIconItemStack(new ItemStack(Items.stick));
        if (Features.HOLYSTONE.enabled())
            ChiselTabs.tabStoneChiselBlocks.setTabIconItemStack(new ItemStack(ChiselBlocks.holystone));
        else
            ChiselTabs.tabStoneChiselBlocks.setTabIconItemStack(new ItemStack(Blocks.stonebrick));
        if (Features.WOOD.enabled())
            ChiselTabs.tabWoodChiselBlocks.setTabIconItemStack(new ItemStack(ChiselBlocks.planks[0], 1, 1));
        else
            ChiselTabs.tabWoodChiselBlocks.setTabIconItemStack(new ItemStack(Blocks.planks));
        if (Features.TECHNICAL.enabled())
            ChiselTabs.tabMetalChiselBlocks.setTabIconItemStack(new ItemStack(ChiselBlocks.technical));
        else
            ChiselTabs.tabMetalChiselBlocks.setTabIconItemStack(new ItemStack(Blocks.iron_block));
        if (Features.JACKOLANTERN.enabled())
            ChiselTabs.tabOtherChiselBlocks.setTabIconItemStack(new ItemStack(ChiselBlocks.jackolantern[0]));
        else
            ChiselTabs.tabOtherChiselBlocks.setTabIconItemStack(new ItemStack(Blocks.lit_pumpkin));
        if (Features.ICE_STAIRS.enabled())
            ChiselTabs.tabStairChiselBlocks.setTabIconItemStack(new ItemStack(ChiselBlocks.iceStairs[0]));
        else
            ChiselTabs.tabStairChiselBlocks.setTabIconItemStack(new ItemStack(Blocks.nether_brick_stairs));

        if (atLeastOneModIsLoaded) {
            if (Features.ARCANE.enabled()) {
                ChiselTabs.tabModdedChiselBlocks.setTabIconItemStack(new ItemStack(ChiselBlocks.arcane));
            } else if (Features.BLOOD_RUNE.enabled()) {
                ChiselTabs.tabModdedChiselBlocks.setTabIconItemStack(new ItemStack(ChiselBlocks.bloodRune));
            } else {
                if (ChiselBlocks.voidstone != null)
                    ChiselTabs.tabModdedChiselBlocks.setTabIconItemStack(new ItemStack(ChiselBlocks.voidstone));
                else
                    ChiselTabs.tabModdedChiselBlocks.setTabIconItemStack(new ItemStack(Blocks.obsidian));
            }
        }
    }
}
