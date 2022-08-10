package team.chisel.common.block;

import net.minecraft.ChatFormatting;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import team.chisel.Chisel;
import team.chisel.api.block.ICarvable;
import team.chisel.client.util.ChiselLangKeys;
import team.chisel.common.config.Configurations;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Class for the items for the chisel block
 */
public class ItemChiselBlock extends BlockItem {

    private final ICarvable block;

    public ItemChiselBlock(Block block, Item.Properties properties) {
        super(block, properties);
        this.block = (ICarvable) block;
    }

    public static void addTooltips(ItemStack stack, List<Component> tooltip) {
        addTooltips(((ItemChiselBlock) stack.getItem()).block, tooltip);
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static void addTooltips(ICarvable block, List<Component> tooltip) {
        try {
            int line = 1;
            String desc = block.getVariation().getDisplayName().getKey() + ".desc.";
            while (I18n.exists(desc + line)) {
                tooltip.add(new TranslatableComponent(desc + line).withStyle(ChatFormatting.GRAY));
                desc.replaceAll(line++ + "$", "." + line);
            }
        } catch (Exception ignored) {
            tooltip.add(new TranslatableComponent(Chisel.MOD_ID + ".tooltip.invalid"));
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        if (Configurations.blockDescriptions) {
            tooltip.add(block.getVariation().getDisplayName().withStyle(ChatFormatting.GRAY));
        }
        addTooltips(block, tooltip);
    }

    @Override
    public Component getName(ItemStack stack) {
        Component ret = super.getName(stack);
        if (!Configurations.blockDescriptions) {
            ret = ChiselLangKeys.TT_BLOCK_NAME.format(ret, block.getVariation().getDisplayName());
        }
        return ret;
    }
}
