package team.chisel.common.block;

import java.util.IllegalFormatException;
import java.util.List;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import net.minecraft.block.Block;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
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

    public ItemChiselBlock(Block block, Item.Properties properties) {
        super(block, properties);
        this.block = (ICarvable) block;
    }
    
    private static String getTooltipUnloc(ICarvable block) {
        VariationData varData = block.getVariation();
        return ((Block)block).getTranslationKey() + "." + varData.name + ".desc.";
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        addTooltips(block, tooltip);
    }
    
    public static void addTooltips(ItemStack stack, List<ITextComponent> tooltip) {
        return addTooltips(((ItemChiselBlock)stack.getItem()).block, tooltip);
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

    @Override
    public ITextComponent getDisplayName(ItemStack stack) {
        if (!Configurations.blockDescriptions) {
            String unlocpattern = "chisel.tooltip.blockname";
            return new TranslationTextComponent(unlocpattern, super.getDisplayName(stack), new TranslationTextComponent(getTooltipUnloc(block) + "1"));
        }
        return super.getDisplayName(stack);
    }
}
