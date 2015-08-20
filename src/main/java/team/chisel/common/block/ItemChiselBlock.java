package team.chisel.common.block;

import team.chisel.client.render.IBlockResources;
import team.chisel.common.CarvableBlocks;
import team.chisel.common.util.SubBlockUtil;
import team.chisel.common.variation.Variation;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

/**
 * Class for the items for the chisel block
 *
 * @author minecreatr
 */
public class ItemChiselBlock extends ItemBlock {

    private Variation[] variations;

    public ItemChiselBlock(Block block) {
        super(block);
        BlockCarvable b = (BlockCarvable) block;
        CarvableBlocks bl = CarvableBlocks.getBlock(b);
        this.setHasSubtypes(true);
        if (b.getIndex() == 0) {
            this.variations = bl.getVariants();
        } else {
            int left = (bl.getVariants().length % 16);
            Variation[] var = new Variation[left];
            int index = b.getIndex() * 16;
            int cur = 0;
            for (int i = 0; i < bl.getVariants().length; i++) {
                if (i >= index && cur <= var.length) {
                    if (bl.getVariants()[i] == null) {
                        continue;
                    }
                    var[cur] = bl.getVariants()[i];
                    cur++;
                }
            }
            this.variations = var;
        }
    }


    @Override
    public String getUnlocalizedName(ItemStack stack) {
        try {
            Variation curVariation = this.variations[stack.getMetadata()];
            return super.getUnlocalizedName(stack) + "." + curVariation;
        } catch (IndexOutOfBoundsException e) {
            return super.getUnlocalizedName(stack) + "." + "null";
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, EntityPlayer playerIn, List tooltip, boolean advanced) {
        Variation v = variations[stack.getMetadata()];
        IBlockResources r = SubBlockUtil.getResources(Block.getBlockFromItem(stack.getItem()), v);
        if (r == null) {
            return;
        }
        for (String s : r.getLore()) {
            tooltip.add(StatCollector.translateToLocal(s));
        }
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
