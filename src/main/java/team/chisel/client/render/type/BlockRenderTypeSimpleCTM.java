package team.chisel.client.render.type;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import team.chisel.client.render.texture.ChiselTextureSimpleCTM;
import team.chisel.ctm.api.texture.ICTMTexture;
import team.chisel.ctm.api.texture.TextureType;
import team.chisel.ctm.api.util.TextureInfo;
import team.chisel.ctm.client.texture.ctx.TextureContextCTM;
import team.chisel.ctm.client.texture.type.TextureTypeCTM;
import team.chisel.ctm.client.util.CTMLogic;

import com.google.common.base.Optional;

@TextureType("SCTM")
public class BlockRenderTypeSimpleCTM extends TextureTypeCTM {

    @Override
    public ICTMTexture<BlockRenderTypeSimpleCTM> makeTexture(TextureInfo info) {
        return new ChiselTextureSimpleCTM(this, info);
    }

    @Override
    public TextureContextCTM getBlockRenderContext(IBlockState state, IBlockAccess world, BlockPos pos, ICTMTexture<?> tex) {
        return new TextureContextCTM(state, world, pos) {

            @Override
            protected CTMLogic createCTM(IBlockState state) {
                CTMLogic ctm = super.createCTM(state);

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
