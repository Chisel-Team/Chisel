package team.chisel.common.block;

import team.chisel.api.block.VariationData;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

/**
 * Class for the items for the chisel block
 *
 * @author minecreatr
 */
public class ItemChiselBlock extends ItemBlock {

    private BlockCarvable block;

    public ItemChiselBlock(Block block) {
        super(block);
        BlockCarvable b = (BlockCarvable) block;
    }


    @Override
    public String getUnlocalizedName(ItemStack stack) {
        try {
            VariationData varData = block.getBlockData().getVariation(stack.getItemDamage());
            return super.getUnlocalizedName(stack) + "." + varData.name;
        } catch (Exception e) {
            return super.getUnlocalizedName(stack) + "." + "null";
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, EntityPlayer playerIn, List tooltip, boolean advanced) {
        //VariationData varData = block.getBlockData().getVariation(stack.getItemDamage());
//        for (String s : r.getLore()) {
//            tooltip.add(StatCollector.translateToLocal(s));
//        }
//        if (isShifting()){
//            tooltip.add("Sneaking");
//        }
//        if (advanced){
//            tooltip.add("F3+h active");
//        }
    }

//    @SideOnly(Side.CLIENT)
//    private static boolean isShifting(){
//        return Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT);
//    }

    @Override
    public int getMetadata(int meta) {
        return meta;
    }

//    public int getMetadata(ItemStack stack){
//        return stack.getMetadata();
//    }
}
