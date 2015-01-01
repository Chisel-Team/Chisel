package com.cricketcraft.chisel.block;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.BlockStairs;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

import com.cricketcraft.chisel.api.ICarvable;
import com.cricketcraft.chisel.carving.CarvableHelper;
import com.cricketcraft.chisel.carving.CarvableVariation;
import com.cricketcraft.chisel.client.render.BlockMarbleStairsRenderer;
import com.cricketcraft.chisel.init.ModTabs;

public class BlockMarbleStairs extends BlockStairs implements ICarvable
{
    CarvableHelper carverHelper;
    int blockMeta;

    public BlockMarbleStairs(Block block, int meta, CarvableHelper helper)
    {
        super(block, meta);

        this.useNeighborBrightness = true;
        carverHelper = helper;
        blockMeta = meta;
    }

    @Override
    public IIcon getIcon(int side, int metadata)
    {
        return carverHelper.getIcon(side, blockMeta + metadata / 8);
    }

    @Override
    public int damageDropped(int i)
    {
        return i & 0x8;
    }

    @Override
    public void registerBlockIcons(IIconRegister register)
    {
        if(blockMeta == 0)
            carverHelper.registerBlockIcons("Chisel", this, register);
    }

    @Override
    public void getSubBlocks(Item block, CreativeTabs tabs, List list)
    {
        list.add(new ItemStack(block, 1, 0));
        list.add(new ItemStack(block, 1, 8));
    }

    @Override
    public int getRenderType()
    {
        return BlockMarbleStairsRenderer.id;
    }

    @Override
    public void onBlockPlacedBy(World par1World, int par2, int par3, int par4, EntityLivingBase par5EntityLiving, ItemStack par6ItemStack)
    {
        int l = MathHelper.floor_double((par5EntityLiving.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
        int i1 = par1World.getBlockMetadata(par2, par3, par4) & 4;
        int odd = par6ItemStack.getItemDamage();

        if(l == 0)
        {
            par1World.setBlockMetadataWithNotify(par2, par3, par4, 2 | i1 + odd, 2);
        }

        if(l == 1)
        {
            par1World.setBlockMetadataWithNotify(par2, par3, par4, 1 | i1 + odd, 2);
        }

        if(l == 2)
        {
            par1World.setBlockMetadataWithNotify(par2, par3, par4, 3 | i1 + odd, 2);
        }

        if(l == 3)
        {
            par1World.setBlockMetadataWithNotify(par2, par3, par4, 0 | i1 + odd, 2);
        }
    }


    @Override
    public int onBlockPlaced(World world, int x, int y, int z, int side, float hx, float hy, float hz, int damage)
    {
        // int res=super.onBlockPlaced();
        return side != 0 && (side == 1 || hy <= 0.5D) ? damage : damage | 4;
    }

    @Override
    public CarvableVariation getVariation(int metadata)
    {
        return carverHelper.getVariation(metadata);
    }
}
