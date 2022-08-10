package team.chisel.api.carving;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import team.chisel.Chisel;
import team.chisel.common.util.NonnullType;
import team.chisel.common.util.Point2i;

import java.util.Locale;

public interface IChiselMode {

    ResourceLocation SPRITES = new ResourceLocation(Chisel.MOD_ID, "textures/mode_icons.png");

    /**
     * Retrieve all valid positions that can be chiseled from where the player is targeting. Must consider state equality, if necessary.
     *
     * @param player The player.
     * @param pos    The position of the targeted block.
     * @param side   The side of the block being targeted.
     * @return All valid positions to be chiseled.
     */
    Iterable<@NonnullType ? extends BlockPos> getCandidates(Player player, BlockPos pos, Direction side);

    AABB getBounds(Direction side);

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

    default Component getLocalizedName() {
        return new TranslatableComponent(getUnlocName());
    }

    default Component getLocalizedDescription() {
        return new TranslatableComponent(getUnlocDescription());
    }

    default long[] getCacheState(BlockPos origin, Direction side) {
        return new long[]{origin.asLong(), side.ordinal()};
    }

    default ResourceLocation getSpriteSheet() {
        return SPRITES;
    }

    Point2i getSpritePos();
} 
