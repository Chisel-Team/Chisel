package team.chisel.client.render.ctx;

import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

import org.apache.commons.lang3.ArrayUtils;

import team.chisel.api.render.IBlockRenderContext;
import team.chisel.client.render.ConnectionLocations;

import com.google.common.collect.ObjectArrays;

import static team.chisel.client.render.ConnectionLocations.*;

public class CTMVBlockRenderContext implements IBlockRenderContext {

    private static ConnectionLocations[] MAIN_VALUES = { UP, DOWN, NORTH, SOUTH, EAST, WEST };
    private static ConnectionLocations[] OFFSET_VALUES = ArrayUtils.removeElements(ConnectionLocations.VALUES, ObjectArrays.concat(
            new ConnectionLocations[] { NORTH_EAST_UP, NORTH_EAST_DOWN, NORTH_WEST_UP, NORTH_WEST_DOWN, SOUTH_WEST_UP, SOUTH_WEST_DOWN, SOUTH_EAST_UP, SOUTH_EAST_DOWN, },
            MAIN_VALUES,
            ConnectionLocations.class
    ));
    private static ConnectionLocations[] ALL_VALUES = ObjectArrays.concat(MAIN_VALUES, OFFSET_VALUES, ConnectionLocations.class);

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

        public static Connections forData(long data, EnumFacing offset) {
            EnumSet<EnumFacing> connections = EnumSet.noneOf(EnumFacing.class);
            if (offset == null) {
                for (ConnectionLocations loc : MAIN_VALUES) {
                    if ((data & loc.getMask()) != 0) {
                        connections.add(ConnectionLocations.toFacing(loc));
                    }
                }
            } else {
                for (ConnectionLocations loc : OFFSET_VALUES) {
                    if ((data & loc.getMask()) != 0) {
                        EnumFacing facing = loc.clipOrDestroy(offset);
                        if (facing != null) {
                            connections.add(facing);
                        }
                    }
                }
            }
            return new Connections(connections);
        }

        public static Connections forPos(IBlockAccess world, IBlockState baseState, BlockPos pos) {
            EnumSet<EnumFacing> connections = EnumSet.noneOf(EnumFacing.class);
            IBlockState state = world.getBlockState(pos);
            if (state == baseState) {
                for (EnumFacing f : EnumFacing.VALUES) {
                    if (world.getBlockState(pos.offset(f)) == baseState) {
                        connections.add(f);
                    }
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

        IBlockState state = world.getBlockState(pos);
        for (ConnectionLocations loc : ALL_VALUES) {
            if (state == world.getBlockState(loc.transform(pos))){
                compressedData = compressedData | loc.getMask();
            }
        }
    }

    public CTMVBlockRenderContext(long data){
        this.data = new ConnectionData(data);
    }

    @Override
    public long getCompressedData(){
        return this.compressedData;
    }
}
