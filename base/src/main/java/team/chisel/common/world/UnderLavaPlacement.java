package team.chisel.common.world;

import java.util.Arrays;
import java.util.Random;
import java.util.stream.Stream;

import com.mojang.serialization.Codec;

import net.minecraft.world.level.material.Material;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.levelgen.placement.DecorationContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneDecoratorConfiguration;
import net.minecraft.world.level.levelgen.placement.FeatureDecorator;

public class UnderLavaPlacement extends FeatureDecorator<NoneDecoratorConfiguration> {

	public static final Codec<UnderLavaPlacement> CODEC = Codec.unit(new UnderLavaPlacement());

    public UnderLavaPlacement() {
        super(NoneDecoratorConfiguration.CODEC);
    }

    @Override
    public Stream<BlockPos> getPositions(DecorationContext world, Random p_212848_3_, NoneDecoratorConfiguration p_212848_4_, BlockPos pos) {
        BlockPos origin = new BlockPos(pos.getX() + 8, 0, pos.getZ() + 8);
        return BlockPos.betweenClosedStream(origin, origin.offset(15, 11, 15))
                .filter(p -> world.getBlockState(p).getMaterial() == Material.LAVA)
                .flatMap(p -> Arrays.stream(Direction.values())
                        .filter(d -> d != Direction.UP)
                        .map(d -> p.relative(d))
                        .filter(p2 -> world.getBlockState(p2).getMaterial() != Material.LAVA));
    }
}
