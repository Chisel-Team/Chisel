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

    static BlockCarvable antiblock = null;
    static BlockCarvable basalt = null;
    static BlockCarvable basaltextra = null;
    static BlockCarvable bookshelf_spruce = null;
    static BlockCarvable bookshelf_birch = null;
    static BlockCarvable bookshelf_jungle = null;
    static BlockCarvable bookshelf_acacia = null;
    static BlockCarvable bookshelf_darkoak = null;
    static BlockCarvable carpet = null;
    static BlockCarvable cloud = null;
    static BlockCarvable concrete = null;
    static BlockCarvable ender_pearl_block = null;
    static BlockCarvable factory = null;
    static BlockCarvable fantasy = null;
    static BlockCarvable futura = null;
    static BlockCarvable grimstone = null;
    static BlockCarvable holystone = null;
    static BlockCarvable laboratory = null;
    static BlockCarvable lavastone = null;
    static BlockCarvable limestone = null;
    static BlockCarvable limestoneextra = null;
    static BlockCarvable line_marking = null;
    static BlockCarvable marble = null;
    static BlockCarvable marbleextra = null;
    static BlockCarvable military = null;
    static BlockCarvable paper = null;
    static BlockCarvable temple = null;
    static BlockCarvable tyrian = null;
    static BlockCarvable valentines = null;
    static BlockCarvable voidstone = null;
    static BlockCarvable warningSign = null;
    static BlockCarvable waterstone = null;

}
