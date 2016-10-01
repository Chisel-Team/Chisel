package team.chisel.client.render;

import javax.annotation.ParametersAreNonnullByDefault;

import lombok.experimental.Delegate;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.ReportedException;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.WorldType;
import net.minecraftforge.common.property.IExtendedBlockState;
import team.chisel.api.block.ICarvable;

@ParametersAreNonnullByDefault
public class BlockRendererDispatcherWrapper extends BlockRendererDispatcher {

    private interface RenderBlock {

        boolean renderBlock(IBlockState state, BlockPos pos, IBlockAccess blockAccess, VertexBuffer worldRendererIn);
    }

    @Delegate(excludes = RenderBlock.class)
    private BlockRendererDispatcher parent;

    public BlockRendererDispatcherWrapper(BlockRendererDispatcher parent) {
        super(null, null);
        this.parent = parent;
    }

    @Override
    public boolean renderBlock(IBlockState state, BlockPos pos, IBlockAccess blockAccess, VertexBuffer worldRendererIn) {
        // Copied from super, code injected after getExtendedState(...) call
        try {
            EnumBlockRenderType enumblockrendertype = state.getRenderType();

            if (enumblockrendertype == EnumBlockRenderType.INVISIBLE) {
                return false;
            } else {
                if (blockAccess.getWorldType() != WorldType.DEBUG_WORLD) {
                    try {
                        state = state.getActualState(blockAccess, pos);
                    } catch (Exception var8) {
                        ;
                    }
                }

                switch (enumblockrendertype) {
                case MODEL:
                    IBakedModel model = this.getModelForState(state);
                    state = state.getBlock().getExtendedState(state, blockAccess, pos);
                    if (model instanceof ModelChiselBlock) {
                        model = ((ModelChiselBlock) model).getModel(state, pos, blockAccess);
                    }
                    return this.getBlockModelRenderer().renderModel(blockAccess, model, state, pos, worldRendererIn, true);
                case ENTITYBLOCK_ANIMATED:
                    return false;
                case LIQUID:
                    return this.fluidRenderer.renderFluid(blockAccess, state, pos, worldRendererIn);
                default:
                    return false;
                }
            }
        } catch (Throwable throwable) {
            CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Tesselating block in world");
            CrashReportCategory crashreportcategory = crashreport.makeCategory("Block being tesselated");
            CrashReportCategory.addBlockInfo(crashreportcategory, pos, state.getBlock(), state.getBlock().getMetaFromState(state));
            throw new ReportedException(crashreport);
        }
    }
}
