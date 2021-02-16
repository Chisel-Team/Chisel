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

        public ChiselCreativeTab(String name) {
            super(name);
        }

        @Override
        public boolean hasSearchBar() {
            return true;
        }
        
        @Override
        public ItemStack createIcon() {
            return new ItemStack(ChiselItems.IRON_CHISEL.get());
        }
    }

    @Nonnull
    public static final ItemGroup base = new ChiselCreativeTab("chisel").setBackgroundImageName("item_search.png");
    
    @Nonnull
    public static final ItemGroup legacy = new ChiselCreativeTab("chisel.legacy").setBackgroundImageName("item_search.png");
}
