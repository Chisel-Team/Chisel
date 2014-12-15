package com.cricketcraft.chisel.block;

import java.util.Random;

import com.cricketcraft.chisel.Chisel;
import com.cricketcraft.chisel.client.GeneralChiselClient;
import com.cricketcraft.chisel.init.ModTabs;
import com.cricketcraft.chisel.utils.General;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

public class BlockGrimstone extends BlockCarvable {
    public IIcon iconStar;

    public BlockGrimstone(Material m) {
        super(m);
        this.setCreativeTab(ModTabs.tabChiselBlocks);
    }

    @Override
    public void randomDisplayTick(World world, int x, int y, int z, Random random) {
        if (General.rand.nextInt(4) == 0 && world.isRemote)
            GeneralChiselClient.spawnGrimstoneFX(world, this, x, y, z);
    }

    @Override
    public void registerBlockIcons(IIconRegister register) {
        super.registerBlockIcons(register);

        iconStar = register.registerIcon(Chisel.MOD_ID + ":grimstone/particles/star");
    }

}
