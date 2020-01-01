package team.chisel.common.init;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

/**
 * Class file for the Chisel creative tabs
 */
@ParametersAreNonnullByDefault
public class ChiselTabs {

    public static class ChiselCreativeTab extends ItemGroup {

        public ChiselCreativeTab() {
            super("chiselCreativeTab");
        }

        @Override
        public boolean hasSearchBar() {
            return true;
        }
        
        @Override
        public ItemStack createIcon() {
            return new ItemStack(ChiselItems.CHISEL_IRON.get());
        }
    }

    @Nonnull
    public static final ItemGroup tab = new ChiselCreativeTab().setBackgroundImageName("item_search.png");
}
