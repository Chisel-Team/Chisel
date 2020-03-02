package team.chisel.api.carving;

import java.util.Locale;

import javax.annotation.ParametersAreNonnullByDefault;
import javax.vecmath.Point2i;

import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import team.chisel.Chisel;
import team.chisel.common.util.NonnullType;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
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
	
	default String getUnlocDescription() {
	    return getUnlocName() + ".desc";
	}
	
	default ITextComponent getLocalizedName() {
	    return new TranslationTextComponent(getUnlocName());
	}
	
	default ITextComponent getLocalizedDescription() {
	    return new TranslationTextComponent(getUnlocDescription());
	}

    default long[] getCacheState(BlockPos origin, Direction side) {
        return new long[] {origin.toLong(), side.ordinal()};
    }
    
    ResourceLocation SPRITES = new ResourceLocation(Chisel.MOD_ID, "textures/mode_icons.png");
    
    default ResourceLocation getSpriteSheet() {
        return SPRITES;
    }
    
    Point2i getSpritePos();
} 
