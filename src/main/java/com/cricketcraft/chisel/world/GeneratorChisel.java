package com.cricketcraft.chisel.world;

import java.util.Map;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.feature.WorldGenMinable;

import com.google.common.collect.Maps;

import cpw.mods.fml.common.IWorldGenerator;

public class GeneratorChisel implements IWorldGenerator {

	private class WorldGenInfo {

		private int amount;
		private int minY, maxY;
		private double chance;

		private WorldGenInfo(int amount, int minY, int maxY, double chance) {
			this.amount = amount;
			this.minY = minY;
			this.maxY = maxY;
			this.chance = chance;
		}
	}

	public static final GeneratorChisel INSTANCE = new GeneratorChisel();

	private final Map<WorldGenMinable, WorldGenInfo> map = Maps.newHashMap();

	public void addFeature(Block block, int count, int amount) {
		addFeature(block, count, amount, 40, 128);
	}

	public void addFeature(Block block, int count, int amount, int minY, int maxY) {
		addFeature(block, count, amount, minY, maxY, 1);
	}

	public void addFeature(Block block, int count, int amount, int minY, int maxY, double chance) {
		map.put(new WorldGenMinable(block, count), new WorldGenInfo(amount, minY, maxY, chance));
	}

	protected void genStandardOre(WorldGenMinable gen, WorldGenInfo info, World world, Random random, int x, int z) {
		for (int l = 0; l < info.amount; ++l) {
			if (random.nextDouble() < info.chance) {
				int avgX = x + random.nextInt(16);
				int avgY = info.minY + random.nextInt(info.maxY - info.minY) + 1;
				int avgZ = z + random.nextInt(16);
				gen.generate(world, random, avgX, avgY, avgZ);
			}
		}
	}

	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkProvider chunkGenerator, IChunkProvider chunkProvider) {
		for (WorldGenMinable gen : map.keySet()) {
			genStandardOre(gen, map.get(gen), world, random, chunkX * 16, chunkZ * 16);
		}
	}
}
