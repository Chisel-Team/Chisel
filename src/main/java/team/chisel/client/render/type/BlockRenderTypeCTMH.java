package team.chisel.client.render.type;

import net.minecraft.util.BlockPos;
import net.minecraft.world.IBlockAccess;
import team.chisel.api.render.BlockRenderType;
import team.chisel.client.render.ctx.CTMBlockRenderContext;
import team.chisel.common.util.EnumConnection;

import java.util.ArrayList;
import java.util.List;

@BlockRenderType("CTMH")
public class BlockRenderTypeCTMH extends BlockRenderTypeCTM {

    @Override
    public CTMBlockRenderContext getBlockRenderContext(IBlockAccess world, BlockPos pos){
        List<EnumConnection> connections = new ArrayList<EnumConnection>();
        boolean sideConnect = false;
        for (EnumConnection connection : EnumConnection.values()) {
            if (connection.isValid(pos, world) ) {
                connections.add(connection);
                if (connection != EnumConnection.DOWN && connection != EnumConnection.UP){
                    sideConnect = true;
                }
            }
        }
        if (sideConnect){
            connections.remove(EnumConnection.DOWN);
            connections.remove(EnumConnection.UP);
        }
        return new CTMBlockRenderContext(connections);
    }
}