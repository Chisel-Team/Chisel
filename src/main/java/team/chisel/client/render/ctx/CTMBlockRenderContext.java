package team.chisel.client.render.ctx;

import java.util.EnumMap;

import javax.annotation.Nonnull;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import team.chisel.api.render.IBlockRenderContext;
import team.chisel.client.render.ModelChiselBlock;
import team.chisel.client.render.RegionCache;
import team.chisel.client.render.ctm.CTM;

public class CTMBlockRenderContext implements IBlockRenderContext {
    
    private EnumMap<EnumFacing, CTM> ctmData = new EnumMap<>(EnumFacing.class);

    private long data;

    public CTMBlockRenderContext(@Nonnull IBlockState state, IBlockAccess world, BlockPos pos) {
        world = new RegionCache(pos, 1, world);
        for (EnumFacing face : EnumFacing.VALUES) {
            CTM ctm = createCTM(state);
            ctm.createSubmapIndices(world, pos, face);
            ctmData.put(face, ctm);
            this.data |= ctm.serialized() << (face.ordinal() * 8);
        }
    }

    public CTMBlockRenderContext(long data){
        this.data = data;
        for(EnumFacing face : EnumFacing.VALUES){
            CTM ctm = createCTM(null); // FIXME
            ctm.createSubmapIndices(data, face);
            ctmData.put(face, ctm);
        }
    }
    
    protected CTM createCTM(@Nonnull IBlockState state) {
        ModelChiselBlock model = (ModelChiselBlock) Minecraft.getMinecraft().getBlockRendererDispatcher().getModelForState(state);
        return CTM.getInstance().ignoreStates(model.getModel().ignoreStates());
    }

    public CTM getCTM(EnumFacing face) {
        return ctmData.get(face);
    }

    @Override
    public long getCompressedData(){
        return this.data;
    }
}
