package team.chisel.common.item;

import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Queue;
import java.util.Set;

import javax.vecmath.Point2i;

import org.apache.commons.lang3.ArrayUtils;

import com.google.common.collect.Sets;

import lombok.Value;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumFacing.AxisDirection;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import team.chisel.api.carving.CarvingUtils;
import team.chisel.api.carving.IChiselMode;

@SuppressWarnings("null")
public enum ChiselMode implements IChiselMode {

    SINGLE {

        @Override
        public Iterable<BlockPos> getCandidates(EntityPlayer player, BlockPos pos, EnumFacing side) {
            return Collections.singleton(pos);
        }
        
        @Override
        public AxisAlignedBB getBounds(EnumFacing side) {
            return new AxisAlignedBB(0, 0, 0, 1, 1, 1);
        }
    },
    PANEL {

        private final BlockPos ONE = new BlockPos(1, 1, 1);
        private final BlockPos NEG_ONE = new BlockPos(-1, -1, -1);

        @Override
        public Iterable<BlockPos> getCandidates(EntityPlayer player, BlockPos pos, EnumFacing side) {
            if (side.getAxisDirection() == AxisDirection.NEGATIVE) {
                side = side.getOpposite();
            }
            Vec3i offset = side.getDirectionVec();
            return filteredIterable(Sets.newHashSet(BlockPos.getAllInBox(NEG_ONE.add(offset).add(pos), ONE.subtract(offset).add(pos))), player.world, player.world.getBlockState(pos));
        }
        
        @Override
        public AxisAlignedBB getBounds(EnumFacing side) {
            switch (side.getAxis()) {
            case X:
            default:
                return new AxisAlignedBB(0, -1, -1, 1, 2, 2);
            case Y:
                return new AxisAlignedBB(-1, 0, -1, 2, 1, 2);
            case Z:
                return new AxisAlignedBB(-1, -1, 0, 2, 2, 1);
            }
        }
    },
    COLUMN {

        @Override
        public Iterable<BlockPos> getCandidates(EntityPlayer player, BlockPos pos, EnumFacing side) {
            int facing = MathHelper.floor(player.rotationYaw * 4.0F / 360.0F + 0.5D) & 3;
            Set<BlockPos> ret = new LinkedHashSet<>();
            for (int i = -1; i <= 1; i++) {
                if (side != EnumFacing.DOWN && side != EnumFacing.UP) {
                    ret.add(pos.up(i));
                } else {
                    if (facing == 0 || facing == 2) {
                        ret.add(pos.south(i));
                    } else {
                        ret.add(pos.east(i));
                    }
                }
            }
            return filteredIterable(ret, player.world, player.world.getBlockState(pos));
        }
        
        @Override
        public AxisAlignedBB getBounds(EnumFacing side) {
            return PANEL.getBounds(side);
        }
        
        @Override
        public long[] getCacheState(BlockPos origin, EnumFacing side) {
            return ArrayUtils.add(super.getCacheState(origin, side), Minecraft.getMinecraft().player.getHorizontalFacing().ordinal());
        }
    },
    ROW {

        @Override
        public Iterable<BlockPos> getCandidates(EntityPlayer player, BlockPos pos, EnumFacing side) {
            int facing = MathHelper.floor(player.rotationYaw * 4.0F / 360.0F + 0.5D) & 3;
            Set<BlockPos> ret = new LinkedHashSet<>();
            for (int i = -1; i <= 1; i++) {
                if (side != EnumFacing.DOWN && side != EnumFacing.UP) {
                    if (side == EnumFacing.EAST || side == EnumFacing.WEST) {
                        ret.add(pos.south(i));
                    } else {
                        ret.add(pos.east(i));
                    }
                } else {
                    if (facing == 0 || facing == 2) {
                        ret.add(pos.east(i));
                    } else {
                        ret.add(pos.south(i));
                    }
                }
            }
            return filteredIterable(ret, player.world, player.world.getBlockState(pos));
        }
        
        @Override
        public AxisAlignedBB getBounds(EnumFacing side) {
            return PANEL.getBounds(side);
        }
        
        @Override
        public long[] getCacheState(BlockPos origin, EnumFacing side) {
            return COLUMN.getCacheState(origin, side);
        }
    }, 
    CONTIGUOUS {

        
        @Override
        public Iterable<? extends BlockPos> getCandidates(EntityPlayer player, BlockPos pos, EnumFacing side) {
            return () -> getContiguousIterator(pos, player.world, EnumFacing.VALUES);
        }
        
        @Override
        public AxisAlignedBB getBounds(EnumFacing side) {
            int r = CONTIGUOUS_RANGE;
            return new AxisAlignedBB(-r - 1, -r - 1, -r - 1, r + 2, r + 2, r + 2);
        }
    },
    CONTIGUOUS_2D {
        
        
        @Override
        public Iterable<? extends BlockPos> getCandidates(EntityPlayer player, BlockPos pos, EnumFacing side) {
            return () -> getContiguousIterator(pos, player.world, ArrayUtils.removeElements(EnumFacing.VALUES, side, side.getOpposite()));
        }
        
        @Override
        public AxisAlignedBB getBounds(EnumFacing side) {
            int r = CONTIGUOUS_RANGE;
            switch (side.getAxis()) {
            case X:
            default:
                return new AxisAlignedBB(0, -r - 1, -r - 1, 1, r + 2, r + 2);
            case Y:
                return new AxisAlignedBB(-r - 1, 0, -r - 1, r + 2, 1, r + 2);
            case Z:
                return new AxisAlignedBB(-r - 1, -r - 1, 0, r + 2, r + 2, 1);
            }
        }
    };
    
    
    @Value
    private static class Node {
        private BlockPos pos;
        int distance;
    }
    
    public static final int CONTIGUOUS_RANGE = 10;
    
    private static Iterator<BlockPos> getContiguousIterator(BlockPos origin, World world, EnumFacing[] directionsToSearch) {
        final IBlockState state = world.getBlockState(origin);
        return new Iterator<BlockPos>() {

            private Set<BlockPos> seen = Sets.newHashSet(origin);
            private Queue<Node> search = new ArrayDeque<>();
            { search.add(new Node(origin, 0)); }

            @Override
            public boolean hasNext() {
                return !search.isEmpty();
            }

            @Override
            public BlockPos next() {
                Node ret = search.poll();
                if (ret.getDistance() < CONTIGUOUS_RANGE) {
                    for (EnumFacing face : directionsToSearch) {
                        BlockPos bp = ret.getPos().offset(face);
                        if (!seen.contains(bp) && world.getBlockState(bp) == state) {
                            search.offer(new Node(bp, ret.getDistance() + 1));
                        }
                        seen.add(bp);
                    }
                }
                return ret.getPos();
            }
        };
    }
    
    private static Iterable<BlockPos> filteredIterable(Collection<BlockPos> source, World world, IBlockState state) {
        return source.stream().filter(p -> world.getBlockState(p) == state)::iterator;
    }
    
    // Register all enum constants to the mode registry
    {
        CarvingUtils.getModeRegistry().registerMode(this);
    }
    
    @Override
    public Point2i getSpritePos() {
        return new Point2i((ordinal() % 10) * 24, (ordinal() / 10) * 24);
    }
}