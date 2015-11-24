package team.chisel.client.render.type;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.BlockPos;
import net.minecraft.world.IBlockAccess;
import team.chisel.api.render.IBlockRenderType;
import team.chisel.api.render.IChiselTexture;
import team.chisel.client.render.ctx.ModuleBlockRenderContext;

/**
 * Abstract block render type for blocks that need to keep track of x/y/z modules
 */
public abstract class BlockRenderTypeWithModules implements IBlockRenderType<ModuleBlockRenderContext> {

    private int variationSize;

    protected BlockRenderTypeWithModules(int variationSize){
        this.variationSize = variationSize;
    }

    @Override
    public ModuleBlockRenderContext getBlockRenderContext(IBlockAccess world, BlockPos pos){
        int xModulus = Math.abs(pos.getX() % variationSize);
        int zModulus = Math.abs(pos.getZ() % variationSize);
        int yModules = Math.abs(pos.getY() % variationSize);
        return new ModuleBlockRenderContext(xModulus, yModules, zModulus, this.variationSize);
    }
}
