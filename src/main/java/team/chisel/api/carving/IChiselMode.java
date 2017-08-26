package team.chisel.api.carving;

import javax.annotation.ParametersAreNonnullByDefault;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import team.chisel.common.util.NonnullType;

@ParametersAreNonnullByDefault
public interface IChiselMode {
    
    Iterable<@NonnullType ? extends BlockPos> getCandidates(EntityPlayer player, BlockPos pos, EnumFacing side);
    
    AxisAlignedBB getBounds(EnumFacing side);
    
	/**
	 * Implemented implicitly by enums. If your IChiselMode is not an enum constant, this needs to be implemented explicitly.
	 * 
	 * @return The name of the mode.
	 */
	String name();

    default long[] getCacheState(BlockPos origin, EnumFacing side) {
        return new long[] {origin.toLong(), side.ordinal()};
    }
}
