package info.jbcs.minecraft.chisel;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.feature.WorldGenMinable;
import cpw.mods.fml.common.IWorldGenerator;

public class MarbleWorldGenerator implements IWorldGenerator {
	WorldGenMinable gen;
	int amount;

	public MarbleWorldGenerator(Block id, int count, int am) {
		gen = new WorldGenMinable(id, count);
		amount = am;
	}

	/**
	 * Standard ore generation helper. Generates most ores.
	 */
	protected void genStandardOre1(World world, Random random, int x, int z, int bot, int top) {
		for (int l = 0; l < amount; ++l) {
			int i1 = x + random.nextInt(16);
			int j1 = bot + random.nextInt(top - bot);
			int k1 = z + random.nextInt(16);
			gen.generate(world, random, i1, j1, k1);
		}
	}

	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkProvider chunkGenerator, IChunkProvider chunkProvider) {
		genStandardOre1(world, random, chunkX*16, chunkZ*16, 40, 128);
	}
}

