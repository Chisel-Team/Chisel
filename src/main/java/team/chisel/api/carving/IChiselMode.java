package team.chisel.api.carving;

import java.util.Locale;

import javax.annotation.ParametersAreNonnullByDefault;
import javax.vecmath.Point2i;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import team.chisel.Chisel;
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
	
	default String getUnlocName() {
	    return "chisel.mode." + name().toLowerCase(Locale.ROOT);
	}

    default long[] getCacheState(BlockPos origin, EnumFacing side) {
        return new long[] {origin.toLong(), side.ordinal()};
    }
    
    ResourceLocation SPRITES = new ResourceLocation(Chisel.MOD_ID, "textures/modeIcons.png");
    
    default ResourceLocation getSpriteSheet() {
        return SPRITES;
    }
    
    Point2i getSpritePos();
} 
