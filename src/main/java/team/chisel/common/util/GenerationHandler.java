package team.chisel.common.util;

import net.minecraft.world.World;
import net.minecraft.world.chunk.AbstractChunkProvider;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraftforge.fml.common.IWorldGenerator;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Random;

@ParametersAreNonnullByDefault
@Deprecated
public class GenerationHandler implements IWorldGenerator {
    @Override
    public void generate(Random random, int chunkX, int chunkZ, World world, ChunkGenerator chunkGenerator, AbstractChunkProvider chunkProvider) {
        // TODO Use Ore Feature instead
    }
}
