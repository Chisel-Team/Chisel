package info.jbcs.minecraft.chisel.item;

import info.jbcs.minecraft.chisel.api.ICarvable;
import info.jbcs.minecraft.chisel.carving.CarvableVariation;
import info.jbcs.minecraft.chisel.Chisel;
import info.jbcs.minecraft.utilities.General;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

public class ItemCarvable extends ItemBlock
{

    public ItemCarvable(Block block)
    {
        super(block);
        setMaxDamage(0);
        setHasSubtypes(true);
    }

    @Override
    public int getMetadata(int i)
    {
        return i;
    }

    //TODO- to fix some, one must break some
    /*@Override
    public String getUnlocalizedName(ItemStack itemstack)
    {
        return "item." + Block.getBlockFromItem(this).getUnlocalizedName() + "." + itemstack.getItemDamage();
    }*/

    @Override
    public IIcon getIconFromDamage(int damage)
    {
        return Block.getBlockFromItem(this).getIcon(2, damage);
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List lines, boolean advancedTooltips)
    {
        if(!Chisel.blockDescriptions) return;

        Item item = General.getItem(stack);
        if(item == null) return;

        Block block = Block.getBlockFromItem(this);
        if(!(block instanceof ICarvable)) return;

        ICarvable carvable = (ICarvable) block;
        CarvableVariation var = carvable.getVariation(stack.getItemDamage());
        if(var == null) return;

        lines.add(var.description);
    }

}
