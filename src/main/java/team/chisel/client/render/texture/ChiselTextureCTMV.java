package team.chisel.client.render.texture;

import java.util.EnumSet;
import java.util.List;

import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumFacing.Axis;
import net.minecraft.util.EnumWorldBlockLayer;
import team.chisel.api.render.IBlockRenderContext;
import team.chisel.api.render.TextureSpriteCallback;
import team.chisel.client.render.ctx.CTMVBlockRenderContext.ConnectionData;
import team.chisel.client.render.ctx.CTMVBlockRenderContext.Connections;
import team.chisel.client.render.type.BlockRenderTypeCTMV;

import com.google.common.collect.Lists;

import static net.minecraft.util.EnumFacing.*;
import static team.chisel.client.render.QuadHelper.*;

public class ChiselTextureCTMV extends AbstractChiselTexture<BlockRenderTypeCTMV> {

    public ChiselTextureCTMV(BlockRenderTypeCTMV type, EnumWorldBlockLayer layer, TextureSpriteCallback[] sprites) {
        super(type, layer, sprites);
    }

    @Override
    public List<BakedQuad> transformQuad(BakedQuad quad, IBlockRenderContext context, int quadGoal) {
        return Lists.newArrayList(quad);
        /*
        if (context == null) {
            return Lists.newArrayList(makeUVFaceQuad(side, sprites[1].getSprite(), new float[] { 0, 0, 8, 8 }));
        }
        return Lists.newArrayList(getQuad(side, ((CTMVBlockRenderContext) context).getData()));
        */
    }

    private BakedQuad getQuad(EnumFacing side, ConnectionData data) {
        Connections cons = data.getConnections();
        
        // This is the order of operations for connections
        EnumSet<EnumFacing> realConnections = EnumSet.copyOf(data.getConnections().getConnections());
        if (cons.connectedOr(UP, DOWN)) {
            // If connected up or down, ignore all other connections
            realConnections.removeIf(f -> f.getAxis().isHorizontal());
        } else if (cons.connectedOr(EAST, WEST)) {
            // If connected east or west, ignore any north/south connections, and any connections that are already connected up or down
            realConnections.removeIf(f -> f == NORTH || f == SOUTH);
            realConnections.removeIf(f -> blockConnectionZ(f, data));
        } else {
            // Otherwise, remove every connection that is already connected to something else
            realConnections.removeIf(f -> blockConnectionY(f, data));
        }

        // Replace our initial connection data with the new info
        cons = new Connections(realConnections);

        int rotation = 0;
        float[] uvs = UVS_TOP_LEFT;
        if (side.getAxis().isHorizontal() && cons.connectedOr(UP, DOWN)) {
            uvs = getUVs(UP, DOWN, cons);
        } else if (cons.connectedOr(EAST, WEST)) {
            rotation = 90;
            uvs = getUVs(EAST, WEST, cons);
        } else if (cons.connectedOr(NORTH, SOUTH)) {
            uvs = getUVs(NORTH, SOUTH, cons);
            if (side == DOWN) {
                rotation += 180;
            }
        }

        boolean connected = !cons.getConnections().isEmpty();

        // Side textures need to be rotated to look correct
        if (connected && !cons.connectedOr(UP, DOWN)) {
            if (side == EAST) {
                rotation += 90;
            }
            if (side == NORTH) {
                rotation += 180;
            }
            if (side == WEST) {
                rotation += 270;
            }
        }

        // If there is a connection opposite this side, it is an end-cap, so render as unconnected
        if (cons.connected(side.getOpposite())) {
            connected = false;
        }
        // If there are no connections at all, and this is not the top or bottom, render the "short" column texture
        if (cons.getConnections().isEmpty() && side.getAxis().isHorizontal()) {
            connected = true;
        }
        
        if (connected) {
            return makeUVFaceQuad(side, sprites[1].getSprite(), uvs, rotation);
        }
        return makeNormalFaceQuad(side, sprites[0].getSprite());
    }

    private float[] getUVs(EnumFacing face1, EnumFacing face2, Connections cons) {
        float[] uvs;
        if (cons.connectedAnd(face1, face2)) {
            uvs = UVS_BOTTOM_LEFT;
        } else {
            if (cons.connected(face1)) {
                uvs = UVS_BOTTOM_RIGHT;
            } else {
                uvs = UVS_TOP_RIGHT;
            }
        }
        return uvs;
    }

    private boolean blockConnectionY(EnumFacing dir, ConnectionData data) {
        return blockConnection(dir, Axis.Y, data) || blockConnection(dir, dir.rotateAround(Axis.Y).getAxis(), data);
    }

    private boolean blockConnectionZ(EnumFacing dir, ConnectionData data) {
        return blockConnection(dir, Axis.Z, data);
    }

    private boolean blockConnection(EnumFacing dir, Axis axis, ConnectionData data) {
        EnumFacing rot = dir.rotateAround(axis);
        return data.getConnections(dir).connectedOr(rot, rot.getOpposite());
    }
}
