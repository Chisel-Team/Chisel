package team.chisel.common.block;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import team.chisel.Chisel;
import team.chisel.api.block.ICarvable;
import team.chisel.api.block.VariationData;

/**
 * Class for the items for the chisel block
 *
 * @author minecreatr
 */
public class ItemChiselBlock extends ItemBlock {

    private ICarvable block;

    public ItemChiselBlock(Block block) {
        super(block);
        this.block = (ICarvable) block;
        this.setHasSubtypes(true);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced) {
        try {
            VariationData varData = block.getVariationData(stack.getItemDamage());
            int line = 1;
            String desc = stack.getUnlocalizedName() + "." + varData.name + ".desc.";
            String loc;
            while (!(loc = StatCollector.translateToLocal(desc + line)).equals(desc + line)) {
                tooltip.add(loc);
                desc.replace("." + line++, "." + line);
            }
        } catch (Exception ignored) {
            tooltip.add(Chisel.MOD_ID + ".tooltip.invalid");
        }
    }

    @Override
    public int getMetadata(int meta) {
        return meta;
    }
}
