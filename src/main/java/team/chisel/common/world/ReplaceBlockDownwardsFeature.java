package team.chisel.common.world;

import java.util.Random;
import java.util.function.Function;

import com.mojang.datafixers.Dynamic;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationSettings;
import net.minecraft.world.gen.feature.Feature;

public class ReplaceBlockDownwardsFeature extends Feature<ReplaceMultipleBlocksConfig> {

    public ReplaceBlockDownwardsFeature(Function<Dynamic<?>, ? extends ReplaceMultipleBlocksConfig> p_i51444_1_) {
        super(p_i51444_1_);
    }
    
    @Override
    public boolean place(IWorld world, ChunkGenerator<? extends GenerationSettings> generator, Random rand, BlockPos pos, ReplaceMultipleBlocksConfig config) {
        boolean ret = false;
        int max = 2;
        if (rand.nextFloat() < 0.7f) {
            max++;
            if (rand.nextFloat() < 0.2f) {
                max++;
            }
        }
        for (int i = 0; i < max; i++) {
            if (config.toReplace.contains(world.getBlockState(pos))) {
                world.setBlockState(pos, config.result, 2);
                ret = true;
            }
            pos = pos.down();
        }
        return ret;
    }
}
