package info.jbcs.minecraft.chisel.block;

import net.minecraft.block.material.Material;

public class BlockVoidstone extends BlockMarbleTexturedOre {

    public BlockVoidstone(String s) {
        super(Material.rock, s);
        setHardness(5.0F);
        setResistance(10.0F);
    }
}
