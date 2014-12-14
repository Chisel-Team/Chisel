package com.cricketcraft.minecraft.chisel.block;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

import com.cricketcraft.minecraft.chisel.Chisel;
import com.cricketcraft.minecraft.chisel.api.ICarvable;
import com.cricketcraft.minecraft.chisel.carving.CarvableHelper;
import com.cricketcraft.minecraft.chisel.carving.CarvableVariation;
import com.cricketcraft.minecraft.chisel.client.GeneralChiselClient;
import com.cricketcraft.minecraft.chisel.utils.General;

public class BlockVoidstone extends Block implements ICarvable{
    public CarvableHelper carverHelper;
    public IIcon iconParticle;

    public BlockVoidstone() {
        super(Material.rock);
        carverHelper = new CarvableHelper();
        setHardness(5.0F);
        setResistance(10.0F);
    }

    @Override
    public void registerBlockIcons(IIconRegister icon){
        iconParticle = icon.registerIcon(Chisel.MOD_ID + ":voidstone/particles/star");
    }

    @Override
    public void randomDisplayTick(World world, int x, int y, int z, Random random)
    {
        if(General.rand.nextInt(4) == 0 && world.isRemote)
            GeneralChiselClient.spawnVoidstoneFX(world, this, x, y, z);
    }

    @Override
    public CarvableVariation getVariation(int metadata) {
        return carverHelper.getVariation(metadata);
    }
}
