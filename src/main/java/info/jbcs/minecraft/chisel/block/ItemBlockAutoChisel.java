package info.jbcs.minecraft.chisel.block;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

import java.util.List;

public class ItemBlockAutoChisel extends ItemBlock{

    private final String[] names = {"base", "speed", "automation", "stack"};
    private int metadata;

    public ItemBlockAutoChisel(Block block) {
        super(block);
        setHasSubtypes(true);
    }

    @Override
    public int getMetadata(int meta){
        return meta;
    }

    @Override
    public String getUnlocalizedName(ItemStack itemStack){
        return getUnlocalizedName() + "." + names[itemStack.getItemDamage()];
    }

    @Override
    public void addInformation(ItemStack itemStack, EntityPlayer player, List list, boolean par4){
        list.add(StatCollector.translateToLocal(getUnlocalizedName() + "." + names[itemStack.getItemDamage()] + ".desc"));
    }
}
