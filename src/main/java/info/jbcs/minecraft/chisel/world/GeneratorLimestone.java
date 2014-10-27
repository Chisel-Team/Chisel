package info.jbcs.minecraft.chisel.world;

import cpw.mods.fml.common.IWorldGenerator;
import net.minecraft.block.Block;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.feature.WorldGenMinable;

import java.util.Random;

public class GeneratorLimestone implements IWorldGenerator
{
    WorldGenMinable gen;
    int amount;

    public GeneratorLimestone(Block block, int count, int am)
    {
        gen = new WorldGenMinable(block, count);
        amount = am;
    }

    protected void genStandardOre1(World world, Random random, int x, int z, int bot, int top)
    {
        for(int l = 0; l < amount; ++l)
        {
            int i1 = x + random.nextInt(15) - 1;
            int j1 = bot + random.nextInt(top - bot);
            int k1 = z + random.nextInt(15);
            gen.generate(world, random, i1, j1, k1);
        }
    }

    @Override
    public void generate(Random random, int chunkX, int chunkZ, World world, IChunkProvider chunkGenerator, IChunkProvider chunkProvider)
    {
        genStandardOre1(world, random, chunkX * 16, chunkZ * 16, 40, 128);
    }
}

