package team.chisel.common.util;

import java.util.List;
import java.util.Random;

import javax.annotation.ParametersAreNonnullByDefault;

import org.apache.commons.lang3.tuple.Pair;

import com.google.common.base.Predicate;
import com.google.common.collect.Lists;

import lombok.Value;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkGenerator;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraftforge.fml.common.IWorldGenerator;

@ParametersAreNonnullByDefault
public enum GenerationHandler implements IWorldGenerator {

    INSTANCE;
    
    @Value
    public static class WorldGenInfo {

        private int amount;
        private int minY, maxY;
        private double chance;
        private Predicate<IBlockState> replaceable;
    }
    
    private final List<Pair<WorldGenMinable, WorldGenInfo>> generators = Lists.newArrayList();
    
    public void addGeneration(IBlockState state, WorldGenInfo info) {
        generators.add(Pair.of(new WorldGenMinable(state, info.getAmount(), info.getReplaceable()), info));
    }
    
	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {
	    for (Pair<WorldGenMinable, WorldGenInfo> p : generators) {
	        generateSurface(world, random, p.getLeft(), p.getRight(), chunkX * 16, chunkZ * 16);
	    }
	}
	
	int basaltHeight = 35;
    int marbleHeight = 100;
    int limestoneHeight = 40;
    
    private void generateSurface(World world, Random rand, WorldGenMinable gen, WorldGenInfo info, int x, int z) {
        for (int k = 0; k < info.getAmount(); k++) {
            int firstX = x + rand.nextInt(16);
            int firstY = info.getMinY() + rand.nextInt(info.getMaxY() - info.getMinY()) + 1;
            int firstZ = z + rand.nextInt(16);

            gen.generate(world, rand, new BlockPos(firstX, firstY, firstZ));

//            // Basalt Generation
//            int basaltY = rand.nextInt(basaltHeight);
//            BlockPos basaltPos = new BlockPos(firstBlockXCoord, basaltY, firstBlockZCoord);
//            if (Configurations.basaltAmount > 0)
//                (new WorldGenMinable(ChiselBlocks.basaltextra.getStateFromMeta(7), Configurations.basaltAmount)).generate(world, rand, basaltPos);
//
//            // Marble Generation
//            int marbleY = rand.nextInt(marbleHeight);
//            BlockPos marblePos = new BlockPos(firstBlockXCoord, marbleY, firstBlockZCoord);
//            if (Configurations.marbleAmount > 0)
//                (new WorldGenMinable(ChiselBlocks.marbleextra.getStateFromMeta(7), Configurations.marbleAmount)).generate(world, rand, marblePos);
//
//            // Limestone Generation
//            int limestoneY = rand.nextInt(limestoneHeight);
//            BlockPos limestonePos = new BlockPos(firstBlockXCoord, limestoneY, firstBlockZCoord);
//            if (Configurations.limestoneAmount > 0)
//                (new WorldGenMinable(ChiselBlocks.limestoneextra.getStateFromMeta(7), Configurations.limestoneAmount)).generate(world, rand, limestonePos);
        }
    }
}

