package team.chisel.common.init;

import javax.annotation.ParametersAreNonnullByDefault;

import net.minecraft.block.Block;
import net.minecraftforge.fml.common.registry.GameRegistry;
import team.chisel.Chisel;

@GameRegistry.ObjectHolder(Chisel.MOD_ID)
@ParametersAreNonnullByDefault
@SuppressWarnings("null") // ObjectHolder will assure these fields are nonnull. Outside callers can assume the same.
public final class ChiselBlocks
{
    /*
        The ObjectHolder annotation allows this class's fields to be used as block/item objects. The field names must
        be verbatim to the gameregistry names for the respective components or else they would have been capitalized as
        per string final conventions.

        "@ObjectHolder is dark magic, very dark magic indeed."
            - minecreatr
    */

    public static final Block antiblock = null;
    public static final Block carpet = null;
    public static final Block cloud = null;
    public static final Block concrete = null;
    public static final Block ender_pearl_block = null;
    public static final Block factory = null;
    public static final Block fantasy = null;
    public static final Block futura = null;
    public static final Block grimstone = null;
    public static final Block holystone = null;
    public static final Block laboratory = null;
    public static final Block lavastone = null;
    public static final Block limestone = null;
    public static final Block line_marking = null;
    public static final Block marble = null;
    public static final Block military = null;
    public static final Block paper = null;
    public static final Block temple = null;
    public static final Block tyrian = null;
    public static final Block valentines = null;
    public static final Block voidstone = null;
    public static final Block warningSign = null;
    public static final Block waterstone = null;

}
