package team.chisel.common.util;

import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import org.apache.commons.lang3.tuple.Pair;

import com.google.common.base.Predicate;
import com.google.common.collect.Lists;

import lombok.Value;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.state.pattern.BlockMatcher;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkGenerator;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraftforge.event.terraingen.PopulateChunkEvent;
import net.minecraftforge.fml.common.IWorldGenerator;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import team.chisel.Features;
import team.chisel.common.config.Configurations;
import team.chisel.common.init.ChiselBlocks;

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
        addGeneration(new WorldGenMinable(state, info.getAmount(), info.getReplaceable()), info);
    }

    public void addGeneration(WorldGenMinable gen, WorldGenInfo info) {
        generators.add(Pair.of(gen, info));
    }

    @SuppressWarnings("null")
    @Override
    public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {
        for (Pair<WorldGenMinable, WorldGenInfo> p : generators) {
            generateSurface(world, random, p.getLeft(), p.getRight(), chunkX * 16, chunkZ * 16);
        }
    }

    @SuppressWarnings("null")
    @SubscribeEvent
    public void onLavaLakes(PopulateChunkEvent.Post event) {
        if (Configurations.basaltSpecialGen && Features.BASALT.enabled()) {
            BlockPos origin = new BlockPos(event.getChunkX() * 16 + 8, 0, event.getChunkZ() * 16 + 8);
            for (BlockPos pos : BlockPos.getAllInBoxMutable(origin, origin.add(15, 11, 15))) {
                IBlockState here = event.getWorld().getBlockState(pos);
                if (here.getMaterial() == Material.LAVA) {
                    BlockPos p = pos.toImmutable();
                    World w = event.getWorld();

                    int size = Configurations.basaltSideThickness;
                    for (BlockPos p2 : BlockPos.getAllInBoxMutable(p.offset(EnumFacing.EAST, size).offset(EnumFacing.SOUTH, size), p.offset(EnumFacing.WEST, size).offset(EnumFacing.NORTH, size))) {
                        replace(w, p2);
                    }

                    for (int i = 1; i <= Configurations.basaltBottomThickness; i++) {
                        replace(w, p.offset(EnumFacing.DOWN, i));
                    }
                }
            }
        }
    }

    private static @Nullable IBlockState basaltstate;
    private static final Predicate<IBlockState> replacecheck = BlockMatcher.forBlock(Blocks.STONE);

    @SuppressWarnings("null")
    private void replace(World world, BlockPos pos) {
        if (basaltstate == null) {
            basaltstate = ChiselBlocks.basalt.getDefaultState().withProperty(ChiselBlocks.basalt.getMetaProp(), 7);
        }
        IBlockState toReplace = world.getBlockState(pos);
        if (toReplace.getBlock().isReplaceableOreGen(toReplace, world, pos, replacecheck)) {
            world.setBlockState(pos, basaltstate);
        }
    }

    private void generateSurface(World world, Random rand, WorldGenMinable gen, WorldGenInfo info, int x, int z) {
        for (int k = 0; k < info.getAmount(); k++) {
            int firstX = x + rand.nextInt(16);
            int firstY = info.getMinY() + rand.nextInt(info.getMaxY() - info.getMinY()) + 1;
            int firstZ = z + rand.nextInt(16);

            gen.generate(world, rand, new BlockPos(firstX, firstY, firstZ));
        }
    }
}
