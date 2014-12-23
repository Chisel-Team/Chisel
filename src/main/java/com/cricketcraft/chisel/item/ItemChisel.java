package com.cricketcraft.chisel.item;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

import com.cricketcraft.chisel.Chisel;
import com.cricketcraft.chisel.api.ChiselMode;
import com.cricketcraft.chisel.api.IChiselMode;
import com.cricketcraft.chisel.carving.CarvableHelper;
import com.cricketcraft.chisel.carving.Carving;

public class ItemChisel extends Item implements IChiselMode {
    public static Carving carving = Carving.chisel;

    public ItemChisel() {
        super();
        setMaxStackSize(1);
        setUnlocalizedName("chisel");
    }

    @Override
    public ItemStack onItemRightClick(ItemStack itemstack, World world, EntityPlayer entityplayer) {
        entityplayer.openGui(Chisel.instance, 0, world, 0, 0, 0);

        return itemstack;
    }

    @Override
    public ChiselMode getChiselMode(ItemStack itemStack) {
        return ChiselMode.SINGLE;
    }
    @Override
    public void addInformation(ItemStack itemStack, EntityPlayer player, List list, boolean par4){
    	StatCollector.translateToLocal(itemStack.getUnlocalizedName() + ".desc");
    }
}
