package team.chisel.client.render;

import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Ordering;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import team.chisel.ctm.api.model.IModelCTM;
import team.chisel.ctm.api.texture.ICTMTexture;
import team.chisel.ctm.api.texture.IChiselFace;
import team.chisel.ctm.api.util.RenderContextList;
import team.chisel.ctm.client.model.AbstractCTMBakedModel;

/**
 * Model for all chisel blocks
 */
@Deprecated
public class ModelChiselBlock extends AbstractCTMBakedModel {

    public ModelChiselBlock(@Nonnull IModelCTM model, @Nonnull IBakedModel parent) {
        super(model, parent);
    }
    
    @Override
    protected AbstractCTMBakedModel createModel(IBlockState state, @Nonnull IModelCTM model, RenderContextList ctx, long rand) {
        IBakedModel baked = getParent();
        ModelChiselBlock ret = new ModelChiselBlock(model, baked);
        List<BakedQuad> quads = Lists.newArrayList();
        for (BlockRenderLayer layer : LAYERS) {
            for (EnumFacing facing : EnumFacing.VALUES) {
                IChiselFace face = model.getFace(facing);
                
                int quadGoal = ctx == null ? 1 : Ordering.natural().max(FluentIterable.from(face.getTextureList()).transform(tex -> tex.getType().getQuadsPerSide()));
                List<BakedQuad> temp = baked.getQuads(state, facing, rand);
                addAllQuads(temp, face, layer, ctx, quadGoal, quads);
                ret.faceQuads.put(layer, facing, ImmutableList.copyOf(quads));

                temp = FluentIterable.from(baked.getQuads(state, null, 0)).filter(q -> q.getFace() == facing).toList();
                addAllQuads(temp, face, layer, ctx, quadGoal, quads);
                ret.genQuads.putAll(layer, quads);
            }
        }
        return ret;
    }

    private void addAllQuads(List<BakedQuad> from, IChiselFace face, BlockRenderLayer layer, @Nullable RenderContextList ctx, int quadGoal, List<BakedQuad> to) {
        to.clear();
        for (BakedQuad q : from) {
            for (ICTMTexture<?> tex : face.getTextureList().stream().filter(t -> t.getLayer() == layer).collect(Collectors.toList())) {
                to.addAll(tex.transformQuad(q, ctx == null ? null : ctx.getRenderContext(tex), quadGoal));
            }
        }
    }
    
    @Override
    public boolean isAmbientOcclusion() {
        return ((ModelChisel)getModel()).ambientOcclusion();
    }
}
