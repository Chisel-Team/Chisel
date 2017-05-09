package team.chisel.client.render.texture;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.vecmath.Point2i;

import com.google.common.base.Preconditions;
import com.google.gson.JsonObject;

import lombok.Getter;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.util.math.BlockPos;
import team.chisel.api.render.IBlockRenderContext;
import team.chisel.api.render.TextureInfo;
import team.chisel.client.render.Quad;
import team.chisel.client.render.ctm.ISubmap;
import team.chisel.client.render.ctm.Submap;
import team.chisel.client.render.ctx.BlockRenderContextGridTexture;
import team.chisel.client.render.ctx.BlockRenderContextPosition;
import team.chisel.client.render.type.BlockRenderTypeMap;

public class ChiselTextureMap extends AbstractChiselTexture<BlockRenderTypeMap> {

    public enum MapType {
        RANDOM {

            @Override
            protected List<BakedQuad> transformQuad(ChiselTextureMap tex, BakedQuad quad, @Nullable IBlockRenderContext context, int quadGoal) {

                Point2i textureCoords = context == null ? new Point2i(1, 1) : ((BlockRenderContextGridTexture)context).getTextureCoords(quad.getFace());
                
                float intervalX = 16f / tex.getXSize();
                float intervalY = 16f / tex.getYSize();
                
                float maxU = textureCoords.x * intervalX;
                float maxV = textureCoords.y * intervalY;
                ISubmap uvs = new Submap(intervalX, intervalY, maxU - intervalX, maxV - intervalY);

                Quad q = tex.makeQuad(quad).setFullbright(tex.fullbright);
                
                // TODO move this code somewhere else, it's copied from below
                if (quadGoal != 4) {
                    return Collections.singletonList(q.transformUVs(tex.sprites[0].getSprite(), uvs).setFullbright(tex.fullbright).rebake());
                } else {
                    Quad[] quads = q.subdivide(4);

                    for (int i = 0; i < quads.length; i++) {
                        if (quads[i] != null) {
                            quads[i] = quads[i].transformUVs(tex.sprites[0].getSprite(), uvs);
                        }
                    }
                    return Arrays.stream(quads).filter(Objects::nonNull).map(Quad::rebake).collect(Collectors.toList());
                }
            }
            
            @Override
            public IBlockRenderContext getContext(@Nonnull BlockPos pos, @Nonnull ChiselTextureMap tex) {
                return new BlockRenderContextGridTexture.Random(pos, tex, true);
            }
        },
        PATTERNED {

            @Override
            protected List<BakedQuad> transformQuad(ChiselTextureMap tex, BakedQuad quad, @Nullable IBlockRenderContext context, int quadGoal) {
                
                Point2i textureCoords = context == null ? new Point2i(0, 0) : ((BlockRenderContextGridTexture)context).getTextureCoords(quad.getFace());
                
                float intervalU = 16f / tex.xSize;
                float intervalV = 16f / tex.ySize;

                // throw new RuntimeException(index % variationSize+" and "+index/variationSize);
                float minU = intervalU * textureCoords.x;
                float minV = intervalV * textureCoords.y;

                ISubmap submap = new Submap(intervalU, intervalV, minU, minV);

                Quad q = tex.makeQuad(quad).setFullbright(tex.fullbright);
                if (quadGoal != 4) {
                    return Collections.singletonList(q.transformUVs(tex.sprites[0].getSprite(), submap).rebake());
                } else {
                    // Chisel.debug("V texture complying with quad goal of 4");
                    // Chisel.debug(new float[] { minU, minV, minU + intervalU, minV + intervalV });

                    Quad[] quads = q.subdivide(4);

                    for (int i = 0; i < quads.length; i++) {
                        if (quads[i] != null) {
                            quads[i] = quads[i].transformUVs(tex.sprites[0].getSprite(), submap);
                        }
                    }
                    return Arrays.stream(quads).filter(Objects::nonNull).map(Quad::rebake).collect(Collectors.toList());
                }
            }
            
            @Override
            public IBlockRenderContext getContext(@Nonnull BlockPos pos, @Nonnull ChiselTextureMap tex) {
                return new BlockRenderContextGridTexture.Patterned(pos, tex, true);
            }
        };

        protected abstract List<BakedQuad> transformQuad(ChiselTextureMap tex, BakedQuad quad, @Nullable IBlockRenderContext context, int quadGoal);
        
        public IBlockRenderContext getContext(@Nonnull BlockPos pos, @Nonnull ChiselTextureMap tex) {
            return new BlockRenderContextPosition(pos);
        }
    }

    @Getter
    private final int xSize;
    @Getter
    private final int ySize;

    private final MapType map;

    public ChiselTextureMap(BlockRenderTypeMap type, TextureInfo info, MapType map) {
        super(type, info);

        this.map = map;

        if (info.getInfo().isPresent()) {
            JsonObject object = info.getInfo().get();
            if (object.has("width") && object.has("height")) {
                Preconditions.checkArgument(object.get("width").isJsonPrimitive() && object.get("width").getAsJsonPrimitive().isNumber(), "width must be a number!");
                Preconditions.checkArgument(object.get("height").isJsonPrimitive() && object.get("height").getAsJsonPrimitive().isNumber(), "height must be a number!");

                this.xSize = object.get("width").getAsInt();
                this.ySize = object.get("height").getAsInt();

            } else if (object.has("size")) {
                Preconditions.checkArgument(object.get("size").isJsonPrimitive() && object.get("size").getAsJsonPrimitive().isNumber(), "size must be a number!");

                this.xSize = object.get("size").getAsInt();
                this.ySize = object.get("size").getAsInt();
            } else {
                xSize = ySize = 2;
            }
        } else {
            xSize = ySize = 2;
        }

        Preconditions.checkArgument(xSize > 0 && ySize > 0, "Cannot have a dimension of 0!");
    }

    @Override
    public List<BakedQuad> transformQuad(BakedQuad quad, IBlockRenderContext context, int quadGoal) {
        return map.transformQuad(this, quad, context, quadGoal);
    }
}
