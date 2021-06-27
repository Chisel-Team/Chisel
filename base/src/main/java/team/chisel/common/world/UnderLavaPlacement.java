package team.chisel.common.world;

import java.util.Arrays;
import java.util.Random;
import java.util.stream.Stream;

import com.mojang.serialization.Codec;

import net.minecraft.block.material.Material;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.gen.feature.WorldDecoratingHelper;
import net.minecraft.world.gen.placement.NoPlacementConfig;
import net.minecraft.world.gen.placement.Placement;

public class UnderLavaPlacement extends Placement<NoPlacementConfig> {

	public static final Codec<UnderLavaPlacement> CODEC = Codec.unit(new UnderLavaPlacement());

    public UnderLavaPlacement() {
        super(NoPlacementConfig.CODEC);
    }

    @Override
    public Stream<BlockPos> getPositions(WorldDecoratingHelper world, Random p_212848_3_, NoPlacementConfig p_212848_4_, BlockPos pos) {
        BlockPos origin = new BlockPos(pos.getX() + 8, 0, pos.getZ() + 8);
        return BlockPos.getAllInBox(origin, origin.add(15, 11, 15))
                .filter(p -> world.func_242894_a(p).getMaterial() == Material.LAVA)
                .flatMap(p -> Arrays.stream(Direction.values())
                        .filter(d -> d != Direction.UP)
                        .map(d -> p.offset(d))
                        .filter(p2 -> world.func_242894_a(p2).getMaterial() != Material.LAVA));
    }
}
