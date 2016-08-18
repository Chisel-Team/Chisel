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

    public final static BlockCarvable antiblock = null;
    public final static BlockCarvable basalt = null;
    public final static BlockCarvable basaltextra = null;
    public final static BlockCarvable carpet = null;
    public final static BlockCarvable cloud = null;
    public final static BlockCarvable concrete = null;
    public final static BlockCarvable ender_pearl_block = null;
    public final static BlockCarvable factory = null;
    public final static BlockCarvable fantasy = null;
    public final static BlockCarvable futura = null;
    public final static BlockCarvable grimstone = null;
    public final static BlockCarvable holystone = null;
    public final static BlockCarvable laboratory = null;
    public final static BlockCarvable lavastone = null;
    public final static BlockCarvable limestone = null;
    public final static BlockCarvable limestoneextra = null;
    public final static BlockCarvable line_marking = null;
    public final static BlockCarvable marble = null;
    public final static BlockCarvable marbleextra = null;
    public final static BlockCarvable military = null;
    public final static BlockCarvable paper = null;
    public final static BlockCarvable temple = null;
    public final static BlockCarvable tyrian = null;
    public final static BlockCarvable valentines = null;
    public final static BlockCarvable voidstone = null;
    public final static BlockCarvable warningSign = null;
    public final static BlockCarvable waterstone = null;
    public final static BlockCarvable icecream = null;

}
