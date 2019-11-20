package team.chisel.api.carving;

import java.util.Locale;

import javax.annotation.ParametersAreNonnullByDefault;
import javax.vecmath.Point2i;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import team.chisel.Chisel;
import team.chisel.common.util.NonnullType;

@ParametersAreNonnullByDefault
public interface IChiselMode {

    /**
     * Retrieve all valid positions that can be chiseled from where the player is targeting. Must consider state equality, if necessary.
     * 
     * @param player
     *            The player.
     * @param pos
     *            The position of the targeted block.
     * @param side
     *            The side of the block being targeted.
     * @return All valid positions to be chiseled.
     */
    Iterable<@NonnullType ? extends BlockPos> getCandidates(PlayerEntity player, BlockPos pos, Direction side);

    AxisAlignedBB getBounds(Direction side);
    
	/**
	 * Implemented implicitly by enums. If your IChiselMode is not an enum constant, this needs to be implemented explicitly.
	 * 
	 * @return The name of the mode.
	 */
	String name();
	
	default String getUnlocName() {
	    return "chisel.mode." + name().toLowerCase(Locale.ROOT);
	}

    default long[] getCacheState(BlockPos origin, Direction side) {
        return new long[] {origin.toLong(), side.ordinal()};
    }
    
    ResourceLocation SPRITES = new ResourceLocation(Chisel.MOD_ID, "textures/modeIcons.png");
    
    default ResourceLocation getSpriteSheet() {
        return SPRITES;
    }
    
    Point2i getSpritePos();
} 
