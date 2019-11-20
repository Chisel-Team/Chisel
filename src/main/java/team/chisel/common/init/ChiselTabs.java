package team.chisel.common.init;

import javax.annotation.ParametersAreNonnullByDefault;

import net.minecraft.block.Blocks;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Class file for the Chisel creative tabs
 */
@ParametersAreNonnullByDefault
public class ChiselTabs {

    public static class CustomCreativeTab extends ItemGroup {

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

    public static class ChiselCreativeTab extends ItemGroup {

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

    public static final ItemGroup tab = new ChiselCreativeTab().setBackgroundImageName("item_search.png");
}
