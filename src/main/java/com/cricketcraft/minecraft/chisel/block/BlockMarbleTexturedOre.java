package com.cricketcraft.minecraft.chisel.block;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.util.IIcon;

import com.cricketcraft.minecraft.chisel.api.ICarvable;
import com.cricketcraft.minecraft.chisel.carving.CarvableHelper;
import com.cricketcraft.minecraft.chisel.carving.CarvableVariation;
import com.cricketcraft.minecraft.chisel.init.ModTabs;

public class BlockMarbleTexturedOre extends BlockTexturedOre implements ICarvable
{
    public CarvableHelper carverHelper;

    public BlockMarbleTexturedOre(Material mat, String baseIcon)
    {
        super(mat, baseIcon);

        carverHelper = new CarvableHelper();

        setCreativeTab(ModTabs.tabChiselBlocks);
    }

    public BlockMarbleTexturedOre(Material mat, Block block) {
        super(mat, block);

        carverHelper = new CarvableHelper();

        setCreativeTab(ModTabs.tabChiselBlocks);
    }

    @Override
    public IIcon getIcon(int side, int metadata)
    {
        return carverHelper.getIcon(side, metadata);
    }

    @Override
    public int damageDropped(int i)
    {
        return i;
    }

    @Override
    public void registerBlockIcons(IIconRegister register)
    {
        super.registerBlockIcons(register);

        carverHelper.registerBlockIcons("Chisel", this, register);
    }

    @Override
    public void getSubBlocks(Item block, CreativeTabs tabs, List list)
    {
        carverHelper.registerSubBlocks(this, tabs, list);
    }

    @Override
    public CarvableVariation getVariation(int metadata)
    {
        return carverHelper.getVariation(metadata);
    }
}
