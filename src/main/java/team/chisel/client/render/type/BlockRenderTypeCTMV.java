package team.chisel.client.render.type;

import net.minecraft.util.BlockPos;
import net.minecraft.world.IBlockAccess;
import team.chisel.api.render.BlockRenderType;
import team.chisel.client.render.ctx.CTMBlockRenderContext;
import team.chisel.common.connections.EnumConnection;

import java.util.ArrayList;
import java.util.List;

@BlockRenderType("CTMV")
public class BlockRenderTypeCTMV extends BlockRenderTypeCTM {

    @Override
    public CTMBlockRenderContext getBlockRenderContext(IBlockAccess world, BlockPos pos){
        List<EnumConnection> connections = new ArrayList<EnumConnection>();
        for (EnumConnection connection : EnumConnection.values()) {
            if (connection.isValid(pos, world) && (connection == EnumConnection.UP || connection == EnumConnection.DOWN) ) {
                connections.add(connection);
            }
        }
        return new CTMBlockRenderContext(connections);
    }
}