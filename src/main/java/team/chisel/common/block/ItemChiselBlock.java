package team.chisel.common.block;

import java.util.IllegalFormatException;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.translation.I18n;
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
public class ItemChiselBlock extends ItemBlock {

    private ICarvable block;

    public ItemChiselBlock(Block block) {
        super(block);
        this.block = (ICarvable) block;
        this.setHasSubtypes(true);
    }
    
    private String getTooltipUnloc(ItemStack stack) {
        VariationData varData = block.getVariationData(stack.getItemDamage());
        return stack.getUnlocalizedName() + "." + varData.name + ".desc.";
    }

    @SuppressWarnings("deprecation")
    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced) {
        try {
            // Skip first line if this config is deactivated, as it will be part of display name
            int line = Configurations.blockDescriptions ? 1 : 2;
            String desc = getTooltipUnloc(stack);
            while (I18n.canTranslate(desc + line) || line == 1) {
                tooltip.add(net.minecraft.client.resources.I18n.format(desc + line));
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
                ret = I18n.translateToLocalFormatted(
                        unlocpattern, 
                        super.getItemStackDisplayName(stack), 
                        I18n.translateToLocalFormatted(getTooltipUnloc(stack) + "1")
                );
            } catch (IllegalFormatException e) {
                String raw = I18n.translateToLocal(unlocpattern);
                Chisel.logger.error("Invalid name pattern {}, check your resource pack lang key for {}", raw, unlocpattern);
            }
            if (ret != null) {
                return ret;
            }
        }
        return super.getItemStackDisplayName(stack);
    }
}
