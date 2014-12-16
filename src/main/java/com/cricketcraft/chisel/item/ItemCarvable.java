package com.cricketcraft.chisel.item;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

import com.cricketcraft.chisel.Configurations;
import com.cricketcraft.chisel.api.ICarvable;
import com.cricketcraft.chisel.carving.CarvableVariation;
import com.cricketcraft.chisel.utils.General;

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

    @Override
    public IIcon getIconFromDamage(int damage)
    {
        return Block.getBlockFromItem(this).getIcon(2, damage);
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List lines, boolean advancedTooltips)
    {
        if(!Configurations.blockDescriptions) return;

        Item item = General.getItem(stack);
        if(item == null) return;

        Block block = Block.getBlockFromItem(this);
        if(!(block instanceof ICarvable)) return;

        ICarvable carvable = (ICarvable) block;
        CarvableVariation var = carvable.getVariation(stack.getItemDamage());
        if(var == null)
            return;

        lines.add(var.description);
    }

}
