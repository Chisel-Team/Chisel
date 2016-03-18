package team.chisel.common.init;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import team.chisel.Chisel;

/**
 * Class file for the Chisel creative tabs
 */
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

    public static class ChiselCreativeTab extends CreativeTabs {

        public ChiselCreativeTab() {
            super("chiselCreativeTab");
        }

        @Override
        public boolean hasSearchBar() {
            return true;
        }

        @Override
        @SideOnly(Side.CLIENT)
        public Item getTabIconItem() {
            return Chisel.itemChiselIron;
        }
    }

    public static final CreativeTabs tab = new ChiselCreativeTab().setBackgroundImageName("item_search.png");
}
