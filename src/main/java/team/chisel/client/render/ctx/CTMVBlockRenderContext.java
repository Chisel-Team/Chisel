package team.chisel.client.render.ctx;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import team.chisel.api.render.IBlockRenderContext;
import team.chisel.client.render.ConnectionLocations;

public class CTMVBlockRenderContext implements IBlockRenderContext {

    private enum ConnectionLocs {
        NORTH(0, EnumFacing.NORTH, null),
        NORTH_NORTH(1, EnumFacing.NORTH, EnumFacing.NORTH),
        NORTH_UP(2, EnumFacing.NORTH, EnumFacing.UP),
        NORTH_DOWN(3, EnumFacing.NORTH, EnumFacing.DOWN),
        NORTH_EAST(4, EnumFacing.NORTH, EnumFacing.EAST),
        NORTH_WEST(5, EnumFacing.NORTH, EnumFacing.WEST),
        SOUTH(6, EnumFacing.SOUTH, null),
        SOUTH_SOUTH(7, EnumFacing.SOUTH, EnumFacing.SOUTH),
        SOUTH_UP(8, EnumFacing.SOUTH, EnumFacing.UP),
        SOUTH_DOWN(9, EnumFacing.SOUTH, EnumFacing.DOWN),
        SOUTH_EAST(10, EnumFacing.SOUTH, EnumFacing.EAST),
        SOUTH_WEST(11, EnumFacing.SOUTH, EnumFacing.EAST),
        EAST(12, EnumFacing.EAST, null),
        EAST_EAST(13, EnumFacing.EAST, EnumFacing.EAST),
        EAST_UP(14, EnumFacing.EAST, EnumFacing.UP),
        EAST_DOWN(15, EnumFacing.EAST, EnumFacing.DOWN),
        WEST(16, EnumFacing.WEST, null),
        WEST_WEST(17, EnumFacing.WEST, EnumFacing.WEST),
        WEST_UP(18, EnumFacing.WEST, EnumFacing.UP),
        WEST_DOWN(19, EnumFacing.WEST, EnumFacing.DOWN),
        UP(20, EnumFacing.UP, null),
        UP_UP(21, EnumFacing.UP, EnumFacing.UP),
        DOWN(22, EnumFacing.DOWN, null),
        DOWN_DOWN(23, EnumFacing.DOWN, EnumFacing.DOWN);

        private int index;
        private EnumFacing dir1;
        private EnumFacing dir2;

        public static final List<ConnectionLocs> base = new ArrayList<ConnectionLocs>() {{
            base.add(NORTH);
            base.add(SOUTH);
            base.add(EAST);
            base.add(WEST);
            base.add(UP);
            base.add(DOWN);
        }};

        private ConnectionLocs(int index, EnumFacing dir1, EnumFacing dir2){
            this.index = index;
            this.dir1 = dir1;
            this.dir2 = dir2;
        }

        public boolean isEqual(EnumFacing one, EnumFacing two){
            return ((dir1 == one) && (dir2 == two)) | ((dir1 == two) && (dir2 == one));
        }

        public ConnectionLocs trim(EnumFacing facing){
            if (this.dir2 == null){
                return this;
            }
            if (this.dir1 == facing){
                return fromDir(this.dir2);
            }
            else if (this.dir2 == facing){
                return fromDir(this.dir1);
            }
            return this;
        }

        public EnumFacing clipOrDestroy(EnumFacing direction){
            if (this.dir1 == direction){
                return this.dir2;
            }
            else if (this.dir2 == direction){
                return this.dir1;
            }
            else {
                return null;
            }
        }

        public long getMask(){
            return 1 << index;
        }

        public BlockPos transform(BlockPos posIn){
            if (this.dir1 != null){
                posIn = posIn.offset(this.dir1);
            }
            if (this.dir2 != null){
                posIn = posIn.offset(this.dir2);
            }
            return posIn;
        }

        public static ConnectionLocs fromDir(EnumFacing dir){
            switch (dir){
                case NORTH: return NORTH;
                case SOUTH: return SOUTH;
                case EAST: return EAST;
                case WEST: return WEST;
                case UP: return UP;
                case DOWN: return DOWN;
                default: return NORTH;
            }
        }

        public static EnumFacing fromLoc(ConnectionLocs loc){
            switch (loc){
                case NORTH: return EnumFacing.NORTH;
                case SOUTH: return EnumFacing.SOUTH;
                case EAST: return EnumFacing.EAST;
                case WEST: return EnumFacing.WEST;
                case UP: return EnumFacing.UP;
                case DOWN: return EnumFacing.DOWN;
                default: return EnumFacing.NORTH;
            }
        }
    }

    @AllArgsConstructor
    @Getter
    @ToString
    public static class Connections {
        
        private EnumSet<EnumFacing> connections;        
        
        public boolean connected(EnumFacing facing) {
            return connections.contains(facing);
        }
        
        public boolean connectedAnd(EnumFacing... facings) {
            for (EnumFacing f : facings) {
                if (!connected(f)) {
                    return false;
                }
            }
            return true;
        }
        
        public boolean connectedOr(EnumFacing... facings) {
            for (EnumFacing f : facings) {
                if (connected(f)) {
                    return true;
                }
            }
            return false;
        }
        
        public static Connections forPos(IBlockAccess world, BlockPos pos) {
            IBlockState state = world.getBlockState(pos);
            return forPos(world, state, pos);
        }

        public static Connections forData(long data, EnumFacing offset){
            EnumSet<EnumFacing> connections = EnumSet.noneOf(EnumFacing.class);
            if (offset == null){
                for (ConnectionLocs loc : ConnectionLocs.base){
                    if ((data & loc.getMask()) != 0){
                        connections.add(ConnectionLocs.fromLoc(loc));
                    }
                }
            }
            else {
                for (ConnectionLocs loc : ConnectionLocs.values()){
                    if ((data & loc.getMask()) != 0){
                        EnumFacing facing = loc.clipOrDestroy(offset);
                        if (facing != null){
                            connections.add(facing);
                        }
                    }
                }
            }
            return new Connections(connections);
        }


        
        
        public static Connections forPos(IBlockAccess world, IBlockState baseState, BlockPos pos) {
            EnumSet<EnumFacing> connections = EnumSet.noneOf(EnumFacing.class);
            for (EnumFacing f : EnumFacing.VALUES) {
                if (world.getBlockState(pos) == baseState && world.getBlockState(pos.offset(f)) == baseState) {
                    connections.add(f);
                }
            }
            return new Connections(connections);
        }
    }

    @ToString
    public static class ConnectionData {

        @Getter
        private Connections connections;
        private Map<EnumFacing, Connections> connectionConnections = new EnumMap<>(EnumFacing.class);

        public ConnectionData(IBlockAccess world, BlockPos pos) {
            connections = Connections.forPos(world, pos);
            IBlockState state = world.getBlockState(pos);
            for (EnumFacing f : EnumFacing.VALUES) {
                connectionConnections.put(f, Connections.forPos(world, state, pos.offset(f)));
            }
        }

        public ConnectionData(long data){
            connections = Connections.forData(data, null);
            for (EnumFacing f : EnumFacing.VALUES){
                connectionConnections.put(f, Connections.forData(data, f));
            }
        }

        public Connections getConnections(EnumFacing facing) {
            return connectionConnections.get(facing);
        }
    }

    @Getter
    private ConnectionData data;

    private long compressedData;
    
    public CTMVBlockRenderContext(IBlockAccess world, BlockPos pos) {
        data = new ConnectionData(world, pos);

        compressedData = 0;
        for (ConnectionLocs loc : ConnectionLocs.values()){
            if (world.getBlockState(pos) == world.getBlockState(loc.transform(pos))){
                compressedData = compressedData | loc.getMask();
            }
        }
    }

    public CTMVBlockRenderContext(long data){
        this.data = new ConnectionData(data);
        this.compressedData = data;
    }

    @Override
    public long getCompressedData(){
        return this.compressedData;
    }
}
