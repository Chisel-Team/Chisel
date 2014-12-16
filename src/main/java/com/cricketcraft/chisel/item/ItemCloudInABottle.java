package com.cricketcraft.chisel.item;

import java.util.List;

import com.cricketcraft.chisel.entity.EntityCloudInABottle;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

public class ItemCloudInABottle extends BaseItem
{

    public ItemCloudInABottle()
    {
        super();
        setUnlocalizedName("cloudBottle");
    }
    @Override
    public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer)
    {
        if(!par3EntityPlayer.capabilities.isCreativeMode)
        {
            --par1ItemStack.stackSize;
        }

        if(par2World.isRemote)
            par2World.playSoundAtEntity(par3EntityPlayer, "random.bow", 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));

        if(!par2World.isRemote)
        {
            par2World.spawnEntityInWorld(new EntityCloudInABottle(par2World, par3EntityPlayer));
        }

        return par1ItemStack;
    }
}
