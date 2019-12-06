package team.chisel.common.init;

import javax.annotation.ParametersAreNonnullByDefault;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.minecraft.item.Item;
import net.minecraftforge.registries.ObjectHolder;
import team.chisel.Chisel;

@ObjectHolder(Chisel.MOD_ID)
@ParametersAreNonnullByDefault
@SuppressWarnings("null")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ChiselItems {
    
    public static final Item chisel_iron = null;
    public static final Item chisel_diamond = null;
    public static final Item chisel_hitech = null;

    public static final Item offsettool = null;
}