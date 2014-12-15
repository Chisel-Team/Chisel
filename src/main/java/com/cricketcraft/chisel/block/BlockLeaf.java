package com.cricketcraft.chisel.block;

import net.minecraft.block.material.Material;

public class BlockLeaf extends BlockCarvable {

    public BlockLeaf(Material material) {
        super(material);
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }
}
