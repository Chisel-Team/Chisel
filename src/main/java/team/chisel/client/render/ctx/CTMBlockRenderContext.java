package team.chisel.client.render.ctx;

import java.util.EnumMap;

import org.apache.commons.lang3.ArrayUtils;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import team.chisel.api.render.IBlockRenderContext;
import team.chisel.client.render.ConnectionLocations;
import team.chisel.client.render.ctm.CTM;

import static team.chisel.client.render.ConnectionLocations.*;

public class CTMBlockRenderContext implements IBlockRenderContext {

    private static final ConnectionLocations[] CACHED_LOCATIONS = ArrayUtils.removeElements(ConnectionLocations.VALUES, UP_UP, DOWN_DOWN, EAST_EAST, WEST_WEST, NORTH_NORTH, SOUTH_SOUTH);
    
    private EnumMap<EnumFacing, CTM> ctmData = new EnumMap<>(EnumFacing.class);

    private long data;

    public CTMBlockRenderContext(IBlockAccess world, BlockPos pos) {
        for (EnumFacing face : EnumFacing.VALUES) {
            CTM ctm = createCTM();
            ctm.createSubmapIndices(world, pos, face);
            ctmData.put(face, ctm);
        }
        this.data = ConnectionLocations.getData(world, pos, CACHED_LOCATIONS);
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
