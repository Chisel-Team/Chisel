package com.cricketcraft.chisel.item;

import com.cricketcraft.chisel.Chisel;
import com.cricketcraft.chisel.api.ChiselMode;
import com.cricketcraft.chisel.api.IChiselMode;
import com.cricketcraft.chisel.carving.CarvableHelper;
import com.cricketcraft.chisel.carving.Carving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ItemDiamondChisel extends ItemTool implements IChiselMode{
    public static Carving carving = Carving.chisel;
    private static final HashSet<String> toolSet = new HashSet<String>();

    public ItemDiamondChisel() {
        super(1, ToolMaterial.IRON, CarvableHelper.getChiselBlockSet());
        setMaxStackSize(1);
        setMaxDamage(-1);
        efficiencyOnProperMaterial = 100F;
        setUnlocalizedName("diamondChisel");
        toolSet.add("chisel");
    }

    @Override
    public ItemStack onItemRightClick(ItemStack itemstack, World world, EntityPlayer entityplayer) {
        if(!entityplayer.isSneaking())
            entityplayer.openGui(Chisel.instance, 0, world, 0, 0, 0);



        return itemstack;
    }

    @Override
    public Set<String> getToolClasses(ItemStack itemStack){
        return toolSet;
    }

    @Override
    public boolean onBlockStartBreak(ItemStack itemStack, int x, int y, int z, EntityPlayer player){
        return true;
    }

    @Override
    public ChiselMode getChiselMode(ItemStack itemStack){
        return ChiselMode.SINGLE;
    }

    @Override
    public void addInformation(ItemStack itemStack, EntityPlayer player, List list, boolean par4){
        StatCollector.translateToLocal("item.diamondChisel.desc");
    }
}
