package team.chisel.client.render;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import com.google.common.collect.ObjectArrays;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import team.chisel.api.render.IBlockRenderContext;
import team.chisel.api.render.IBlockRenderType;
import team.chisel.api.render.IChiselTexture;
import team.chisel.api.render.IModelChisel;
import team.chisel.api.render.RenderContextList;
import team.chisel.client.ChiselExtendedState;

@ParametersAreNonnullByDefault
public class ModelChiselBlock extends AbstractChiselBakedModel {
    
    public ModelChiselBlock(IModelChisel model) {
        super(model);
    }

    private static final EnumFacing[] FACINGS = ObjectArrays.concat(EnumFacing.VALUES, (EnumFacing) null);

    @Override
    protected AbstractChiselBakedModel createModel(@Nullable IBlockState state, IModelChisel model, @Nullable RenderContextList ctx) {
        AbstractChiselBakedModel ret = new ModelChiselBlock(model);
        for (BlockRenderLayer layer : LAYERS) {
            for (EnumFacing facing : FACINGS) {
                List<BakedQuad> parentQuads = model.getModel(null).getQuads(state, facing, 0);
                List<BakedQuad> quads;
                if (facing != null) {
                    ret.faceQuads.put(layer, facing, quads = new ArrayList<>());
                } else {
                    quads = ret.genQuads.get(layer);
                }
                for (BakedQuad q : parentQuads) {
                    IChiselTexture<?> tex = this.getModel().getTexture(q.getSprite().getIconName());

                    if (!(state instanceof ChiselExtendedState) || (tex == null && layer == state.getBlock().getBlockLayer())) {
                        quads.add(q);
                    } else if (tex != null && layer == tex.getLayer()) {
                        IBlockRenderType type = tex.getType();
                        ChiselExtendedState extstate = (ChiselExtendedState) state;

                        IBlockRenderContext brc = type.getBlockRenderContext(extstate.getClean(), extstate.getWorld(), extstate.getPos(), tex);
                        quads.addAll(tex.transformQuad(q, brc, type.getQuadsPerSide()));
                    }
                }
            }
        }
        return ret;
    }

    @Override
    public @Nonnull TextureAtlasSprite getParticleTexture() {
        return getModel().getModel(null).getParticleTexture();
    }
}
