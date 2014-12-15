package com.cricketcraft.chisel.block;

import java.util.List;

import com.cricketcraft.chisel.api.ICarvable;
import com.cricketcraft.chisel.carving.CarvableHelper;
import com.cricketcraft.chisel.carving.CarvableVariation;
import com.cricketcraft.chisel.client.render.BlockMarblePaneRenderer;
import com.cricketcraft.chisel.init.ModTabs;

import net.minecraft.block.BlockPane;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.util.IIcon;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockCarvablePane extends BlockPane implements ICarvable
{
    public CarvableHelper carverHelper;
    public boolean isAlpha;

    public BlockCarvablePane(Material material, boolean drops)
    {
        super("", "", material, drops);

        carverHelper = new CarvableHelper();

        setCreativeTab(ModTabs.tabChiselBlocks);
    }

    public BlockCarvablePane setStained(boolean a)
    {
        this.isAlpha = a;
        return this;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int getRenderBlockPass()
    {
        return isAlpha ? 1 : 0;
    }

    @Override
    public int getRenderType()
    {
        return BlockMarblePaneRenderer.id;
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
    public void getSubBlocks(Item item, CreativeTabs tabs, List list)
    {
        carverHelper.registerSubBlocks(this, tabs, list);
    }

    @Override
    public CarvableVariation getVariation(int metadata)
    {
        return carverHelper.getVariation(metadata);
    }
}
