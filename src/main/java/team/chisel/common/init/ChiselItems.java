package team.chisel.common.init;

import javax.annotation.ParametersAreNonnullByDefault;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.minecraftforge.registries.ObjectHolder;
import team.chisel.Chisel;
import team.chisel.common.item.ItemChisel;
import team.chisel.common.item.ItemOffsetTool;

@ObjectHolder(Chisel.MOD_ID)
@ParametersAreNonnullByDefault
@SuppressWarnings("null")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ChiselItems {
    
    public static final ItemChisel chisel_iron = null;
    public static final ItemChisel chisel_diamond = null;
    public static final ItemChisel chisel_hitech = null;

    public static final ItemOffsetTool offsettool = null;
}