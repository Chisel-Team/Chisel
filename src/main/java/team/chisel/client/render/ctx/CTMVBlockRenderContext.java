package team.chisel.client.render.ctx;

import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import team.chisel.api.render.IBlockRenderContext;


public class CTMVBlockRenderContext implements IBlockRenderContext {

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

        public Connections getConnections(EnumFacing facing) {
            return connectionConnections.get(facing);
        }
    }

    @Getter
    private ConnectionData data;
    
    public CTMVBlockRenderContext(IBlockAccess world, BlockPos pos) {
        data = new ConnectionData(world, pos);
    }
}
