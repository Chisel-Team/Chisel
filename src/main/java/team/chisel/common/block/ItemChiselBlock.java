package team.chisel.common.block;

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
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import team.chisel.Chisel;
import team.chisel.api.block.ICarvable;
import team.chisel.client.util.ChiselLangKeys;
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

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        if (Configurations.blockDescriptions) {
            tooltip.add(block.getVariation().getDisplayName().applyTextStyle(TextFormatting.GRAY));
        }
        addTooltips(stack, block, tooltip);
    }
    
    public static void addTooltips(ItemStack stack, List<ITextComponent> tooltip) {
        addTooltips(stack, ((ItemChiselBlock)stack.getItem()).block, tooltip);
    }
    
    public static void addTooltips(ItemStack stack, ICarvable block, List<ITextComponent> tooltip) {
        try {
            int line = 1;
            String desc = block.getVariation().getDisplayName().getKey() + ".desc.";
            while (I18n.hasKey(desc + line)) {
                tooltip.add(new TranslationTextComponent(desc + line).applyTextStyle(TextFormatting.GRAY));
                desc.replaceAll(line++ + "$", "." + line);
            }
        } catch (Exception ignored) {
            tooltip.add(new TranslationTextComponent(Chisel.MOD_ID + ".tooltip.invalid"));
        }
    }

    @Override
    public ITextComponent getDisplayName(ItemStack stack) {
        ITextComponent ret = super.getDisplayName(stack);
        if (!Configurations.blockDescriptions) {
            ret = ChiselLangKeys.TT_BLOCK_NAME.format(ret, block.getVariation().getDisplayName());
        }
        return ret;
    }
}
