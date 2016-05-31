package team.chisel.client.render.texture;

import java.util.EnumSet;
import java.util.List;

import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumFacing.Axis;
import team.chisel.api.render.IBlockRenderContext;
import team.chisel.api.render.TextureInfo;
import team.chisel.client.render.Quad;
import team.chisel.client.render.ctm.ISubmap;
import team.chisel.client.render.ctx.CTMVBlockRenderContext;
import team.chisel.client.render.ctx.CTMVBlockRenderContext.ConnectionData;
import team.chisel.client.render.ctx.CTMVBlockRenderContext.Connections;
import team.chisel.client.render.type.BlockRenderTypeCTMV;

import com.google.common.collect.Lists;

import static net.minecraft.util.EnumFacing.*;
import static team.chisel.client.render.Quad.*;

public class ChiselTextureCTMV extends AbstractChiselTexture<BlockRenderTypeCTMV> {

    public ChiselTextureCTMV(BlockRenderTypeCTMV type, TextureInfo info) {
        super(type, info);
    }

    @Override
    public List<BakedQuad> transformQuad(BakedQuad quad, IBlockRenderContext context, int quadGoal) {
        if (context == null) {
            return Lists.newArrayList(from(quad).transformUVs(sprites[1].getSprite(), TOP_LEFT).setFullbright(fullbright).rebake());
        }
        return Lists.newArrayList(getQuad(quad, ((CTMVBlockRenderContext) context).getData()));
    }

    private BakedQuad getQuad(BakedQuad in, ConnectionData data) {
        Quad q = from(in).setFullbright(fullbright);
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
        ISubmap uvs = TOP_LEFT;
        if (in.getFace().getAxis().isHorizontal() && cons.connectedOr(UP, DOWN)) {
            uvs = getUVs(UP, DOWN, cons);
        } else if (cons.connectedOr(EAST, WEST)) {
            rotation = 1;
            uvs = getUVs(EAST, WEST, cons);
        } else if (cons.connectedOr(NORTH, SOUTH)) {
            uvs = getUVs(NORTH, SOUTH, cons);
            if (in.getFace() == DOWN) {
                rotation += 2;
            }
        }

        boolean connected = !cons.getConnections().isEmpty();

        // Side textures need to be rotated to look correct
        if (connected && !cons.connectedOr(UP, DOWN)) {
            if (in.getFace() == EAST) {
                rotation += 1;
            }
            if (in.getFace() == NORTH) {
                rotation += 2;
            }
            if (in.getFace() == WEST) {
                rotation += 3;
            }
        }

        // If there is a connection opposite this side, it is an end-cap, so render as unconnected
        if (cons.connected(in.getFace().getOpposite())) {
            connected = false;
        }
        // If there are no connections at all, and this is not the top or bottom, render the "short" column texture
        if (cons.getConnections().isEmpty() && in.getFace().getAxis().isHorizontal()) {
            connected = true;
        }
        
        q = q.rotate(rotation);
        if (connected) {
            return q.transformUVs(sprites[1].getSprite(), uvs).rebake();
        }
        return q.transformUVs(sprites[0].getSprite()).rebake();
    }

    private ISubmap getUVs(EnumFacing face1, EnumFacing face2, Connections cons) {
        ISubmap uvs;
        if (cons.connectedAnd(face1, face2)) {
            uvs = BOTTOM_LEFT;
        } else {
            if (cons.connected(face1)) {
                uvs = BOTTOM_RIGHT;
            } else {
                uvs = TOP_RIGHT;
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
