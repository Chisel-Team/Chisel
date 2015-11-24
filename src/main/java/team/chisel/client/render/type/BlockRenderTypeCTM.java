package team.chisel.client.render.type;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.BlockPos;
import net.minecraft.world.IBlockAccess;
import team.chisel.api.render.BlockRenderType;
import team.chisel.api.render.IBlockRenderType;
import team.chisel.api.render.IChiselTexture;
import team.chisel.client.render.ctx.CTMBlockRenderContext;
import team.chisel.client.render.texture.ChiselTextureCTM;
import team.chisel.common.connections.EnumConnection;

import java.util.ArrayList;
import java.util.List;

/**
 * Block RenderType for CTM
 */
@BlockRenderType("CTM")
public class BlockRenderTypeCTM implements IBlockRenderType<CTMBlockRenderContext> {

    @Override
    public IChiselTexture makeTexture(TextureAtlasSprite... sprites){
        return new ChiselTextureCTM(this, sprites);
    }

    @Override
    public CTMBlockRenderContext getBlockRenderContext(IBlockAccess world, BlockPos pos){
        List<EnumConnection> connections = new ArrayList<EnumConnection>();
        for (EnumConnection connection : EnumConnection.values()) {
            if (connection.isValid(pos, world)) {
                connections.add(connection);
            }
        }
        return new CTMBlockRenderContext(connections);
    }
}
