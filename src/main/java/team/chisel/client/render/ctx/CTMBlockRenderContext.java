package team.chisel.client.render.ctx;

import java.util.EnumMap;

import com.google.common.base.Optional;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import team.chisel.api.render.IBlockRenderContext;
import team.chisel.client.render.ConnectionLocations;
import team.chisel.client.render.ctm.CTM;

public class CTMBlockRenderContext implements IBlockRenderContext {

    private EnumMap<EnumFacing, CTM> ctmData = new EnumMap<>(EnumFacing.class);

    private long data;

    public CTMBlockRenderContext(IBlockAccess world, BlockPos pos) {
        for (EnumFacing face : EnumFacing.VALUES) {
            CTM ctm = createCTM();
            ctm.createSubmapIndices(world, pos, face);
            ctmData.put(face, ctm);
        }
        this.data = ConnectionLocations.getData(world, pos);
    }

    public CTMBlockRenderContext(long data){
        this.data = data;
        for(EnumFacing face : EnumFacing.VALUES){
            CTM ctm = createCTM();
            ctm.createSubmapIndices(data, face);
            ctmData.put(face, ctm);
        }
    }
    
    protected CTM createCTM() {
        return CTM.getInstance();
    }

    public CTM getCTM(EnumFacing face) {
        return ctmData.get(face);
    }

    @Override
    public long getCompressedData(){
        return this.data;
    }
}
