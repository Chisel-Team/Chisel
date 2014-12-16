package com.cricketcraft.chisel.block;

import java.util.Random;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

import com.cricketcraft.chisel.client.GeneralChiselClient;
import com.cricketcraft.chisel.utils.General;

public class BlockHolystone extends BlockCarvable {
    public IIcon iconStar;

    public BlockHolystone(Material m) {
        super(m);

        this.setLightLevel(0.25F);
    }

    @Override
    public void randomDisplayTick(World world, int x, int y, int z, Random random) {
        if (General.rand.nextInt(4) == 0 && world.isRemote)
            GeneralChiselClient.spawnHolystoneFX(world, this, x, y, z);
    }

    @Override
    public void registerBlockIcons(IIconRegister register) {
        super.registerBlockIcons(register);

        iconStar = register.registerIcon("Chisel:holystone/particles/star");
    }

}
