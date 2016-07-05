package team.chisel.common.util;

import java.util.Random;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkGenerator;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraftforge.fml.common.IWorldGenerator;
import team.chisel.common.config.Configurations;
import team.chisel.common.init.ChiselBlocks;

public class GenerationHandler implements IWorldGenerator {

	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {
		switch (world.provider.getDimension()) {
		case -1:
			generateNether(world, random, chunkX * 16, chunkZ * 16);
			break;
		case 0:
			generateSurface(world, random, chunkX * 16, chunkZ * 16);
			break;
		case 1:
			generateEnd(world, random, chunkX * 16, chunkZ * 16);
			break;
		}
		
	}
	
	int basaltHeight = 35;
    int marbleHeight = 100;
    int limestoneHeight = 40;
	
    private void generateNether(World world, Random rand, int chunkX, int chunkZ) {
    }
    
    private void generateSurface(World world, Random rand, int chunkX, int chunkZ) {
        for (int k = 0; k < 16; k++)
        {
            int firstBlockXCoord = chunkX + rand.nextInt(16);
            int firstBlockZCoord = chunkZ + rand.nextInt(16);
            
            //TODO for when we do finally have an easy Block reference class
            
            //Basalt Generation
            int basaltY = rand.nextInt(basaltHeight);
            BlockPos basaltPos = new BlockPos(firstBlockXCoord, basaltY, firstBlockZCoord);            
            if (Configurations.basaltAmount > 0)
            	(new WorldGenMinable(ChiselBlocks.basaltextra.getStateFromMeta(7), Configurations.basaltAmount)).generate(world, rand, basaltPos);
            
            //Marble Generation
            int marbleY = rand.nextInt(marbleHeight);
            BlockPos marblePos = new BlockPos(firstBlockXCoord, marbleY, firstBlockZCoord);            
            if (Configurations.marbleAmount > 0)
            	(new WorldGenMinable(ChiselBlocks.marbleextra.getStateFromMeta(7), Configurations.marbleAmount)).generate(world, rand, marblePos);
            
            //Limestone Generation
            int limestoneY = rand.nextInt(limestoneHeight);
            BlockPos limestonePos = new BlockPos(firstBlockXCoord, limestoneY, firstBlockZCoord);            
            if (Configurations.limestoneAmount > 0)
            	(new WorldGenMinable(ChiselBlocks.limestoneextra.getStateFromMeta(7), Configurations.limestoneAmount)).generate(world, rand, limestonePos);
        }
    }
    
    private void generateEnd(World world, Random rand, int chunkX, int chunkZ) {
    }
}

