package team.chisel.common.world;

import java.util.List;
import java.util.Random;

import com.mojang.serialization.Codec;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Plane;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.OreFeature;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;

public class ReplaceBlockDownwardsFeature extends Feature<ReplaceMultipleBlocksConfig> {

    public ReplaceBlockDownwardsFeature(Codec<ReplaceMultipleBlocksConfig> p_i51444_1_) {
        super(p_i51444_1_);
    }
    
    private final OreConfiguration _dummy = new OreConfiguration(List.of(), 0, 0);
    @Override
    public boolean place(FeaturePlaceContext<ReplaceMultipleBlocksConfig> ctx) {
        boolean ret = false;
        int max = 2;
        Random rand = ctx.random();
        if (rand.nextFloat() < 0.7f) {
            max++;
            if (rand.nextFloat() < 0.2f) {
                max++;
            }
        }
        ReplaceMultipleBlocksConfig config = ctx.config();
        WorldGenLevel world = ctx.level();
        BlockPos pos = ctx.origin();
        for (int i = 0; i < max; i++) {
            for (OreConfiguration.TargetBlockState target : config.targetStates) {
                if (OreFeature.canPlaceOre(world.getBlockState(pos), world::getBlockState, rand, _dummy, target, pos.mutable())) {
                    world.setBlock(pos, target.state, 2);
                    for (Direction dir : Plane.HORIZONTAL) {
                        BlockPos pos2 = pos.relative(dir);
                        if (rand.nextDouble() > 0.9 && OreFeature.canPlaceOre(world.getBlockState(pos2), world::getBlockState, rand, _dummy, target, pos2.mutable())) {
                            world.setBlock(pos2, target.state, 2);
                        }
                    }
                    ret = true;
                }
            }
            pos = pos.below();
        }
        return ret;
    }
}
