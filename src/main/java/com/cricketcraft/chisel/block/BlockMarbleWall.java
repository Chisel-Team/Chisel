package com.cricketcraft.chisel.block;

import java.util.List;

import com.cricketcraft.chisel.carving.CarvableHelper;
import com.cricketcraft.chisel.init.ModTabs;

import net.minecraft.block.Block;
import net.minecraft.block.BlockWall;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.util.IIcon;

public class BlockMarbleWall extends BlockWall
{
    CarvableHelper carverHelper;

    public BlockMarbleWall(Block block)
    {
        super(block);

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
        carverHelper.registerBlockIcons("Chisel", this, register);
    }

    @Override
    public void getSubBlocks(Item block, CreativeTabs tabs, List list)
    {
        carverHelper.registerSubBlocks(this, tabs, list);
    }
}
