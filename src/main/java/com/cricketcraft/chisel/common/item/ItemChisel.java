package com.cricketcraft.chisel.common.item;

import com.cricketcraft.chisel.Chisel;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

/**
 * Item for the chisel
 *
 * @author minecreatr
 */
public class ItemChisel extends Item{

    public ItemChisel(){
        super();
        setMaxStackSize(1);
        setUnlocalizedName("itemChisel");
    }

    @Override
    public ItemStack onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn) {
        playerIn.openGui(Chisel.instance, 0, worldIn, playerIn.getPosition().getX(), playerIn.getPosition().getY(), playerIn.getPosition().getZ());
        return itemStackIn;
    }
}
