package team.chisel.common.block;

import java.util.IllegalFormatException;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import net.minecraft.block.Block;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import team.chisel.Chisel;
import team.chisel.api.block.ICarvable;
import team.chisel.api.block.VariationData;
import team.chisel.common.config.Configurations;

/**
 * Class for the items for the chisel block
 */
@ParametersAreNonnullByDefault
public class ItemChiselBlock extends BlockItem {

    private ICarvable block;

    public ItemChiselBlock(Block block) {
        super(block);
        this.block = (ICarvable) block;
        this.setHasSubtypes(true);
    }
    
    private static String getTooltipUnloc(ICarvable block, int index) {
        VariationData varData = block.getVariationData(index);
        return ((Block)block).getUnlocalizedName() + "." + varData.name + ".desc.";
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag advanced) {
        addTooltips(stack, tooltip);
    }
    
    @SideOnly(Side.CLIENT)
    public void addTooltips(ItemStack stack, List<String> tooltip) {
        addTooltips(block, stack.getItemDamage(), tooltip);
    }
    
    @SideOnly(Side.CLIENT)
    public static void addTooltips(ICarvable block, int index, List<String> tooltip) {
        try {
            // Skip first line if this config is deactivated, as it will be part of display name
            int line = Configurations.blockDescriptions ? 1 : 2;
            String desc = getTooltipUnloc(block, index);
            while (I18n.hasKey(desc + line) || line == 1) {
                tooltip.add(I18n.format(desc + line));
                desc.replaceAll(line++ + "$", "." + line);
            }
        } catch (Exception ignored) {
            tooltip.add(Chisel.MOD_ID + ".tooltip.invalid");
        }
    }

    @Override
    public int getMetadata(int meta) {
        return meta;
    }

    @SuppressWarnings("deprecation")
    @Override
    public String getItemStackDisplayName(@Nonnull ItemStack stack) {
        if (!Configurations.blockDescriptions) {
            String unlocpattern = "chisel.tooltip.blockname";
            String ret = null;
            try {
                ret = net.minecraft.util.text.translation.I18n.translateToLocalFormatted(
                        unlocpattern, 
                        super.getItemStackDisplayName(stack), 
                        net.minecraft.util.text.translation.I18n.translateToLocalFormatted(getTooltipUnloc(block, stack.getItemDamage()) + "1")
                );
            } catch (IllegalFormatException e) {
                String raw = net.minecraft.util.text.translation.I18n.translateToLocal(unlocpattern);
                Chisel.logger.error("Invalid name pattern {}, check your resource pack lang key for {}", raw, unlocpattern);
            }
            if (ret != null) {
                return ret;
            }
        }
        return super.getItemStackDisplayName(stack);
    }
}
