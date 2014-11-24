package info.jbcs.minecraft.chisel.block;

import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;

import java.util.Random;

public class BlockLightstoneCarvable extends BlockCarvable
{
    public BlockLightstoneCarvable()
    {
        super(Material.glass, false);
    }

    @Override
    public int quantityDropped(Random random)
    {
        return Blocks.glowstone.quantityDropped(random);
    }

    @Override
    public Item getItemDropped(int i, Random random, int a)
    {
        return Items.glowstone_dust;
    }

    @Override
    public int damageDropped(int i)
    {
        return 0;
    }
}
