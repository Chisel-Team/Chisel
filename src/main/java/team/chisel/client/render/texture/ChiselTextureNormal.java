package team.chisel.client.render.texture;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.BlockRenderLayer;
import team.chisel.api.render.IBlockRenderContext;
import team.chisel.api.render.TextureSpriteCallback;
import team.chisel.client.render.Quad;
import team.chisel.client.render.type.BlockRenderTypeNormal;

import com.google.common.collect.Lists;

/**
 * Chisel texture for a normal texture
 */
public class ChiselTextureNormal extends AbstractChiselTexture<BlockRenderTypeNormal> {

    public ChiselTextureNormal(BlockRenderTypeNormal type, BlockRenderLayer layer, TextureSpriteCallback... sprites){
        super(type, layer, sprites);
    }

    @Override
    public List<BakedQuad> transformQuad(BakedQuad quad, IBlockRenderContext context, int quadGoal) {
        if (quadGoal == 4) {
            return Arrays.stream(Quad.from(quad, DefaultVertexFormats.ITEM).transformUVs(sprites[0].getSprite()).subdivide(4)).filter(Objects::nonNull).map(qu -> qu.rebake()).collect(Collectors.toList());
        }
        return Lists.newArrayList(Quad.from(quad, DefaultVertexFormats.ITEM).transformUVs(sprites[0].getSprite()).rebake());
    }
}
