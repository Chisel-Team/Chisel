package team.chisel.client.render.type;

import com.google.common.base.Optional;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import team.chisel.api.render.*;
import team.chisel.client.render.ctm.CTM;
import team.chisel.client.render.ctx.CTMBlockRenderContext;
import team.chisel.client.render.texture.ChiselTextureSimpleCTM;

@BlockRenderType("SCTM")
public class BlockRenderTypeSimpleCTM extends BlockRenderTypeCTM {

    @Override
    public IChiselTexture<BlockRenderTypeSimpleCTM> makeTexture(TextureInfo info) {
        return new ChiselTextureSimpleCTM(this, info);
    }

    @Override
    public CTMBlockRenderContext getBlockRenderContext(IBlockAccess world, BlockPos pos) {
        return new CTMBlockRenderContext(world, pos) {

            @Override
            protected CTM createCTM() {
                CTM ctm = super.createCTM();

                ctm.disableObscuredFaceCheck = Optional.of(true);

                return ctm;
            }
        };
    }

    @Override
    public int getQuadsPerSide() {
        return 1;
    }

    @Override
    public int requiredTextures() {
        return 1;
    }
}
