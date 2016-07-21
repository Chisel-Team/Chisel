package team.chisel.common.init;

import javax.annotation.ParametersAreNonnullByDefault;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import net.minecraftforge.fml.common.registry.GameRegistry;
import team.chisel.Chisel;
import team.chisel.common.block.BlockCarvable;

@GameRegistry.ObjectHolder(Chisel.MOD_ID)
@ParametersAreNonnullByDefault
@SuppressWarnings("null") // ObjectHolder will assure these fields are nonnull. Outside callers can assume the same.
//@FieldDefaults(makeFinal = true, level = AccessLevel.PUBLIC)
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
    public static final BlockCarvable basaltextra = null;
    public static final BlockCarvable carpet = null;
    public static final BlockCarvable cloud = null;
    public static final BlockCarvable concrete = null;
    public static final BlockCarvable ender_pearl_block = null;
    public static final BlockCarvable factory = null;
    public static final BlockCarvable fantasy = null;
    public static final BlockCarvable futura = null;
    public static final BlockCarvable grimstone = null;
    public static final BlockCarvable holystone = null;
    public static final BlockCarvable laboratory = null;
    public static final BlockCarvable lavastone = null;
    public static final BlockCarvable limestone = null;
    public static final BlockCarvable limestoneextra = null;
    public static final BlockCarvable line_marking = null;
    public static final BlockCarvable marble = null;
    public static final BlockCarvable marbleextra = null;
    public static final BlockCarvable military = null;
    public static final BlockCarvable paper = null;
    public static final BlockCarvable temple = null;
    public static final BlockCarvable tyrian = null;
    public static final BlockCarvable valentines = null;
    public static final BlockCarvable voidstone = null;
    public static final BlockCarvable warningSign = null;
    public static final BlockCarvable waterstone = null;
    public static final BlockCarvable glowstone = null;

}
