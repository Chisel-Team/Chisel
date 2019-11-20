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
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
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
    
    private static String getTooltipUnloc(ICarvable block) {
        VariationData varData = block.getVariationData();
        return ((Block)block).getTranslationKey() + "." + varData.name + ".desc.";
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag advanced) {
        addTooltips(stack, tooltip);
    }
    
    public void addTooltips(ItemStack stack, List<ITextComponent> tooltip) {
        addTooltips(block, tooltip);
    }
    
    public static void addTooltips(ICarvable block, List<ITextComponent> tooltip) {
        try {
            // Skip first line if this config is deactivated, as it will be part of display name
            int line = Configurations.blockDescriptions ? 1 : 2;
            String desc = getTooltipUnloc(block);
            while (I18n.hasKey(desc + line) || line == 1) {
                tooltip.add(new TranslationTextComponent(desc + line));
                desc.replaceAll(line++ + "$", "." + line);
            }
        } catch (Exception ignored) {
            tooltip.add(new TranslationTextComponent(Chisel.MOD_ID + ".tooltip.invalid"));
        }
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
