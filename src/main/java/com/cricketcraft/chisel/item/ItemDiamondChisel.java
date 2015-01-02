package com.cricketcraft.chisel.item;

import com.cricketcraft.chisel.Chisel;
import com.cricketcraft.chisel.api.ICarvable;
import com.cricketcraft.chisel.carving.CarvableVariation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.world.World;

import java.util.Set;

public class ItemDiamondChisel extends ItemTool implements ICarvable{
    protected ItemDiamondChisel(float p_i45333_1_, ToolMaterial p_i45333_2_, Set p_i45333_3_) {
        super(p_i45333_1_, p_i45333_2_, p_i45333_3_);
    }

    @Override
    public ItemStack onItemRightClick(ItemStack itemstack, World world, EntityPlayer entityplayer) {
        entityplayer.openGui(Chisel.instance, 0, world, 0, 0, 0);

        return itemstack;
    }

    @Override
    public CarvableVariation getVariation(int metadata) {
        return null;
    }
}
