package team.chisel.client.render.type;

import com.google.gson.JsonObject;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import team.chisel.api.render.BlockRenderType;
import team.chisel.api.render.IBlockRenderType;
import team.chisel.api.render.IChiselTexture;
import team.chisel.api.render.TextureInfo;
import team.chisel.api.render.TextureSpriteCallback;
import team.chisel.client.render.ctx.CTMBlockRenderContext;
import team.chisel.client.render.texture.ChiselTextureCTM;

import javax.annotation.Nullable;

@BlockRenderType("CTM")
public class BlockRenderTypeCTM implements IBlockRenderType {

    @Override
    public IChiselTexture<? extends BlockRenderTypeCTM> makeTexture(TextureInfo info) {
      return new ChiselTextureCTM(this, info);
    }

    @Override
    public CTMBlockRenderContext getBlockRenderContext(IBlockAccess world, BlockPos pos) {
        return new CTMBlockRenderContext(world, pos);
    }

    @Override
    public int getQuadsPerSide() {
        return 4;
    }

    @Override
    public int requiredTextures() {
        return 2;
    }
}
