package com.cricketcraft.chisel.block;

import java.util.List;

import net.minecraft.block.BlockGlass;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.util.IIcon;

import com.cricketcraft.chisel.Chisel;
import com.cricketcraft.chisel.api.ICarvable;
import com.cricketcraft.chisel.carving.CarvableHelper;
import com.cricketcraft.chisel.carving.CarvableVariation;
import com.cricketcraft.chisel.init.ModTabs;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockCarvableGlass extends BlockGlass implements ICarvable
{
    public CarvableHelper carverHelper;
    private boolean isAlpha = false;

    public BlockCarvableGlass()
    {
        super(Material.glass, false);

        carverHelper = new CarvableHelper();
        setCreativeTab(ModTabs.tabChiselBlocks);
    }

    public BlockCarvableGlass setStained(boolean a)
    {
        this.isAlpha = a;
        return this;
    }

    @Override
    public boolean renderAsNormalBlock()
    {
        return false;
    }

    @Override
    public int getRenderType()
    {
        return Chisel.RenderCTMId;
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

    @Override
    @SideOnly(Side.CLIENT)
    public int getRenderBlockPass()
    {
        return isAlpha ? 1 : 0;
    }
}
