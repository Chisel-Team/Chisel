package team.chisel.common.init;

import javax.annotation.ParametersAreNonnullByDefault;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import net.minecraft.block.Block;
import net.minecraftforge.fml.common.registry.GameRegistry;
import team.chisel.Chisel;

@GameRegistry.ObjectHolder(Chisel.MOD_ID)
@ParametersAreNonnullByDefault
@SuppressWarnings("null") // ObjectHolder will assure these fields are nonnull. Outside callers can assume the same.
@FieldDefaults(makeFinal = true, level = AccessLevel.PUBLIC)
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

    static Block antiblock = null;
    static Block basalt = null;
    static Block basaltextra = null;
    static Block carpet = null;
    static Block cloud = null;
    static Block concrete = null;
    static Block ender_pearl_block = null;
    static Block factory = null;
    static Block fantasy = null;
    static Block futura = null;
    static Block grimstone = null;
    static Block holystone = null;
    static Block laboratory = null;
    static Block lavastone = null;
    static Block limestone = null;
    static Block limestoneextra = null;
    static Block line_marking = null;
    static Block marble = null;
    static Block marbleextra = null;
    static Block military = null;
    static Block paper = null;
    static Block temple = null;
    static Block tyrian = null;
    static Block valentines = null;
    static Block voidstone = null;
    static Block warningSign = null;
    static Block waterstone = null;

}
