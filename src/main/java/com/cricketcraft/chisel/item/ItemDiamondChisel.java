package com.cricketcraft.chisel.item;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import com.cricketcraft.chisel.api.ChiselMode;
import com.cricketcraft.chisel.api.IChiselMode;
import com.cricketcraft.chisel.carving.Carving;

public class ItemDiamondChisel extends Item implements IChiselMode{
    public static Carving carving = Carving.chisel;

    public ItemDiamondChisel() {
        super();
        setMaxStackSize(1);
        setUnlocalizedName("diamondChisel");
    }

    @Override
    public ChiselMode getChiselMode(ItemStack itemStack){
        return ChiselMode.SINGLE;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
    public void addInformation(ItemStack itemStack, EntityPlayer player, List list, boolean par4){
        list.add("Deprecated. Craft to get new version.");
    }
}
