package team.chisel.common.init;

import javax.annotation.ParametersAreNonnullByDefault;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.minecraftforge.fml.common.registry.GameRegistry;
import team.chisel.Chisel;
import team.chisel.common.block.BlockCarvable;

@GameRegistry.ObjectHolder(Chisel.MOD_ID)
@ParametersAreNonnullByDefault
@SuppressWarnings("null") // ObjectHolder will assure these fields are nonnull. Outside callers can assume the same.
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ChiselBlocks
{
    /*
        The ObjectHolder annotation allows this class's fields to be used as block/item objects. The field names must
        be verbatim to the gameregistry names for the respective components or else they would have been capitalized as
        per string final conventions.

        "@ObjectHolder is dark magic, very dark magic indeed."
            - minecreatr
    */

    public static final BlockCarvable antiblock = null;
    public static final BlockCarvable basalt = null;
    public static final BlockCarvable basalt1 = null;
    public static final BlockCarvable basaltextra = null;
    public static final BlockCarvable block_charcoal2 = null;
    public static final BlockCarvable bloodMagic = null;
    public static final BlockCarvable bookshelf_spruce = null;
    public static final BlockCarvable bookshelf_birch = null;
    public static final BlockCarvable bookshelf_jungle = null;
    public static final BlockCarvable bookshelf_acacia = null;
    public static final BlockCarvable bookshelf_darkoak = null;
    public static final BlockCarvable brownstone = null;
    public static final BlockCarvable carpet = null;
    public static final BlockCarvable cloud = null;
    public static final BlockCarvable concrete = null;
    public static final BlockCarvable concrete_white = null;
    public static final BlockCarvable concrete_orange = null;
    public static final BlockCarvable concrete_lightblue = null;
    public static final BlockCarvable concrete_magenta = null;
    public static final BlockCarvable concrete_lime = null;
    public static final BlockCarvable concrete_yellow = null;
    public static final BlockCarvable concrete_pink = null;
    public static final BlockCarvable concrete_gray = null;
    public static final BlockCarvable concrete_lightgray = null;
    public static final BlockCarvable concrete_blue = null;
    public static final BlockCarvable concrete_cyan = null;
    public static final BlockCarvable concrete_purple = null;
    public static final BlockCarvable concrete_green = null;
    public static final BlockCarvable concrete_brown = null;
    public static final BlockCarvable concrete_red = null;
    public static final BlockCarvable concrete_black = null;
    public static final BlockCarvable concrete_powder = null;
    public static final BlockCarvable dirt = null;
    public static final BlockCarvable factory = null;
    public static final BlockCarvable futura = null;
    public static final BlockCarvable glowstone = null;
    public static final BlockCarvable glowstone1 = null;
    public static final BlockCarvable glowstone2 = null;
    public static final BlockCarvable laboratory = null;
    public static final BlockCarvable lavastone = null;
    public static final BlockCarvable lavastone1 = null;
    public static final BlockCarvable lavastoneextra = null;
    public static final BlockCarvable limestone = null;
    public static final BlockCarvable limestone1 = null;
    public static final BlockCarvable limestoneextra = null;
    public static final BlockCarvable marble = null;
    public static final BlockCarvable marble1 = null;
    public static final BlockCarvable marbleextra = null;
    public static final BlockCarvable netherrack = null;
    public static final BlockCarvable paper = null;
    public static final BlockCarvable temple = null;
    public static final BlockCarvable tyrian = null;
    public static final BlockCarvable valentines = null;
    public static final BlockCarvable voidstone = null;
    public static final BlockCarvable waterstone = null;
    public static final BlockCarvable waterstone1 = null;
    public static final BlockCarvable waterstoneextra = null;
}
