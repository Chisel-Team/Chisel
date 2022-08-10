package team.chisel.common.item;

import com.google.common.collect.Sets;
import com.tterrag.registrate.providers.RegistrateLangProvider;
import lombok.Getter;
import lombok.Value;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.AxisDirection;
import net.minecraft.core.Vec3i;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.SupportType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import org.apache.commons.lang3.ArrayUtils;
import team.chisel.Chisel;
import team.chisel.api.carving.CarvingUtils;
import team.chisel.api.carving.IChiselMode;
import team.chisel.common.util.Point2i;

import javax.annotation.Nullable;
import java.util.*;
import java.util.stream.Stream;

@SuppressWarnings("null")
public enum ChiselMode implements IChiselMode {

    SINGLE("Chisel a single block.") {
        @Override
        public Iterable<BlockPos> getCandidates(Player player, BlockPos pos, Direction side) {
            return Collections.singleton(pos);
        }

        @Override
        public AABB getBounds(Direction side) {
            return new AABB(0, 0, 0, 1, 1, 1);
        }
    },
    PANEL("Chisel a 3x3 square of blocks.") {

        private final BlockPos ONE = new BlockPos(1, 1, 1);
        private final BlockPos NEG_ONE = new BlockPos(-1, -1, -1);

        @Override
        public Iterable<BlockPos> getCandidates(Player player, BlockPos pos, Direction side) {
            if (side.getAxisDirection() == AxisDirection.NEGATIVE) {
                side = side.getOpposite();
            }
            Vec3i offset = side.getNormal();
            return filteredIterable(BlockPos.betweenClosedStream(NEG_ONE.offset(offset).offset(pos), ONE.subtract(offset).offset(pos)), player.level, player.level.getBlockState(pos));
        }

        @Override
        public AABB getBounds(Direction side) {
            switch (side.getAxis()) {
                case X:
                default:
                    return new AABB(0, -1, -1, 1, 2, 2);
                case Y:
                    return new AABB(-1, 0, -1, 2, 1, 2);
                case Z:
                    return new AABB(-1, -1, 0, 2, 2, 1);
            }
        }
    },
    COLUMN("Chisel a 3x1 column of blocks.") {
        @Override
        public Iterable<BlockPos> getCandidates(Player player, BlockPos pos, Direction side) {
            int facing = Mth.floor(player.getYRot() * 4.0F / 360.0F + 0.5D) & 3;
            Set<BlockPos> ret = new LinkedHashSet<>();
            for (int i = -1; i <= 1; i++) {
                if (side != Direction.DOWN && side != Direction.UP) {
                    ret.add(pos.above(i));
                } else {
                    if (facing == 0 || facing == 2) {
                        ret.add(pos.south(i));
                    } else {
                        ret.add(pos.east(i));
                    }
                }
            }
            return filteredIterable(ret.stream(), player.level, player.level.getBlockState(pos));
        }

        @Override
        public AABB getBounds(Direction side) {
            return PANEL.getBounds(side);
        }

        @Override
        public long[] getCacheState(BlockPos origin, Direction side) {
            return ArrayUtils.add(super.getCacheState(origin, side), Minecraft.getInstance().player.getDirection().ordinal());
        }
    },
    ROW("Chisel a 1x3 row of blocks.") {
        @Override
        public Iterable<BlockPos> getCandidates(Player player, BlockPos pos, Direction side) {
            int facing = Mth.floor(player.getYRot() * 4.0F / 360.0F + 0.5D) & 3;
            Set<BlockPos> ret = new LinkedHashSet<>();
            for (int i = -1; i <= 1; i++) {
                if (side != Direction.DOWN && side != Direction.UP) {
                    if (side == Direction.EAST || side == Direction.WEST) {
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
            return filteredIterable(ret.stream(), player.level, player.level.getBlockState(pos));
        }

        @Override
        public AABB getBounds(Direction side) {
            return PANEL.getBounds(side);
        }

        @Override
        public long[] getCacheState(BlockPos origin, Direction side) {
            return COLUMN.getCacheState(origin, side);
        }
    },
    CONTIGUOUS("Chisel an area of alike blocks, extending 10 blocks in any direction.") {
        @Override
        public Iterable<? extends BlockPos> getCandidates(Player player, BlockPos pos, Direction side) {
            return () -> getContiguousIterator(pos, player.level, Direction.values());
        }

        @Override
        public AABB getBounds(Direction side) {
            int r = CONTIGUOUS_RANGE;
            return new AABB(-r - 1, -r - 1, -r - 1, r + 2, r + 2, r + 2);
        }
    },
    CONTIGUOUS_2D("Contiguous (2D)", "Chisel an area of alike blocks, extending 10 blocks along the plane of the current side.") {
        @Override
        public Iterable<? extends BlockPos> getCandidates(Player player, BlockPos pos, Direction side) {
            return () -> getContiguousIterator(pos, player.level, ArrayUtils.removeElements(Direction.values(), side, side.getOpposite()));
        }

        @Override
        public AABB getBounds(Direction side) {
            int r = CONTIGUOUS_RANGE;
            switch (side.getAxis()) {
                case X:
                default:
                    return new AABB(0, -r - 1, -r - 1, 1, r + 2, r + 2);
                case Y:
                    return new AABB(-r - 1, 0, -r - 1, r + 2, 1, r + 2);
                case Z:
                    return new AABB(-r - 1, -r - 1, 0, r + 2, r + 2, 1);
            }
        }
    };


    public static final int CONTIGUOUS_RANGE = 10;
    @Getter(onMethod = @__({@Override}))
    private final TranslatableComponent localizedName;
    @Getter(onMethod = @__({@Override}))
    private final TranslatableComponent localizedDescription;

    // Register all enum constants to the mode registry
    {
        CarvingUtils.getModeRegistry().registerMode(this);
    }
    ChiselMode(String desc) {
        this(null, desc);
    }

    ChiselMode(@Nullable String name, String desc) {
        this.localizedName = Chisel.registrate().addRawLang(getUnlocName(), name == null ? RegistrateLangProvider.toEnglishName(name()) : name);
        this.localizedDescription = Chisel.registrate().addRawLang(getUnlocDescription(), desc);
    }

    private static Iterator<BlockPos> getContiguousIterator(BlockPos origin, Level world, Direction[] directionsToSearch) {
        final BlockState state = world.getBlockState(origin);
        return new Iterator<BlockPos>() {

            private final Set<BlockPos> seen = Sets.newHashSet(origin);
            private final Queue<Node> search = new ArrayDeque<>();

            {
                search.add(new Node(origin, 0));
            }

            @Override
            public boolean hasNext() {
                return !search.isEmpty();
            }

            @Override
            public BlockPos next() {
                Node ret = search.poll();
                if (ret.getDistance() < CONTIGUOUS_RANGE) {
                    for (Direction face : directionsToSearch) {
                        BlockPos bp = ret.getPos().relative(face);
                        BlockState newState = world.getBlockState(bp);
                        if (!seen.contains(bp) && newState == state) {
                            for (Direction obscureCheck : Direction.values()) {
                                BlockPos obscuringPos = bp.relative(obscureCheck);
                                if (!newState.isFaceSturdy(world, bp, obscureCheck.getOpposite(), SupportType.FULL)) {
                                    search.offer(new Node(bp, ret.getDistance() + 1));
                                    break;
                                }
                            }
                        }
                        seen.add(bp);
                    }
                }
                return ret.getPos();
            }
        };
    }

    private static Iterable<BlockPos> filteredIterable(Stream<BlockPos> source, Level world, BlockState state) {
        return source.filter(p -> world.getBlockState(p) == state)::iterator;
    }

    @Override
    public Point2i getSpritePos() {
        return new Point2i((ordinal() % 10) * 24, (ordinal() / 10) * 24);
    }

    @Value
    private static class Node {
        int distance;
        private BlockPos pos;
    }
}