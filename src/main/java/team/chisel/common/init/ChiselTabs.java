package team.chisel.common.init;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nonnull;

/**
 * Class file for the Chisel creative tabs
 */
@SuppressWarnings("deprecation")
public class ChiselTabs {

    @Nonnull
    public static final CreativeModeTab base = new ChiselCreativeTab("chisel").setBackgroundSuffix("item_search.png");
    @Nonnull
    public static final CreativeModeTab legacy = new ChiselCreativeTab("chisel.legacy").setBackgroundSuffix("item_search.png");

    public static class ChiselCreativeTab extends CreativeModeTab {

        public ChiselCreativeTab(String name) {
            super(name);
        }

        @Override
        public boolean hasSearchBar() {
            return true;
        }

        @Override
        public ItemStack makeIcon() {
            return new ItemStack(ChiselItems.IRON_CHISEL.get());
        }
    }
}
