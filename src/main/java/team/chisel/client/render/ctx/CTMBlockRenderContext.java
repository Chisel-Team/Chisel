package team.chisel.client.render.ctx;

import java.util.EnumMap;

import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import team.chisel.api.render.IBlockRenderContext;
import team.chisel.client.render.ctm.CTM;

public class CTMBlockRenderContext implements IBlockRenderContext {

    private EnumMap<EnumFacing, CTM> ctmData = new EnumMap<>(EnumFacing.class);

    public CTMBlockRenderContext(IBlockAccess world, BlockPos pos) {
        for (EnumFacing face : EnumFacing.VALUES) {
            CTM ctm = CTM.getInstance();
            ctm.createSubmapIndices(world, pos, face);
            ctmData.put(face, ctm);
        }
    }

    public CTM getCTM(EnumFacing face) {
        return ctmData.get(face);
    }
}
