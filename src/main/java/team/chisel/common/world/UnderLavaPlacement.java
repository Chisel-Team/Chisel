package team.chisel.common.world;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.levelgen.placement.PlacementContext;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;
import net.minecraft.world.level.levelgen.placement.PlacementModifierType;
import net.minecraft.world.level.material.Material;
import team.chisel.common.init.ChiselWorldGen;

import java.util.Arrays;
import java.util.Random;
import java.util.stream.Stream;

public class UnderLavaPlacement extends PlacementModifier {

    public static final Codec<UnderLavaPlacement> CODEC = Codec.unit(new UnderLavaPlacement());

    @SuppressWarnings("null")
    @Override
    public Stream<BlockPos> getPositions(PlacementContext world, Random p_212848_3_, BlockPos pos) {
        BlockPos origin = new BlockPos(pos.getX() + 8, world.getMinGenY(), pos.getZ() + 8);
        return BlockPos.betweenClosedStream(origin, origin.offset(15, 0, 15).atY(11))
                .filter(p -> world.getBlockState(p).getMaterial() == Material.LAVA)
                .flatMap(p -> Arrays.stream(Direction.values())
                        .filter(d -> d != Direction.UP)
                        .map(d -> p.relative(d))
                        .filter(p2 -> world.getBlockState(p2).getMaterial() != Material.LAVA));
    }

    @Override
    public PlacementModifierType<?> type() {
        return ChiselWorldGen.PLACE_UNDER_LAVA.get();
    }
}
