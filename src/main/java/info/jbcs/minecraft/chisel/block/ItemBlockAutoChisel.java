package info.jbcs.minecraft.chisel.block;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class ItemBlockAutoChisel extends ItemBlock{

    private final String[] names = {"base", "speed", "automation", "stack"};
    private int metadata;

    public ItemBlockAutoChisel(Block block) {
        super(block);
        setMaxDamage(0);
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

}
