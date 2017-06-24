package team.chisel.common.init;

import javax.annotation.ParametersAreNonnullByDefault;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import team.chisel.Chisel;

/**
 * Class file for the Chisel creative tabs
 */
@ParametersAreNonnullByDefault
public class ChiselTabs {

    public static class CustomCreativeTab extends CreativeTabs {

        private ItemStack stack;

        public CustomCreativeTab(String label) {
            super(label);
            stack = new ItemStack(Blocks.STONE);
        }

        @Override
        public ItemStack getTabIconItem() {
            return stack;
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
        public ItemStack getTabIconItem() {
            return new ItemStack(ChiselItems.chisel_iron);
        }
    }

    public static final CreativeTabs tab = new ChiselCreativeTab().setBackgroundImageName("item_search.png");
}
