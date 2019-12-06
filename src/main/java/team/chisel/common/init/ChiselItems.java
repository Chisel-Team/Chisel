package team.chisel.common.init;

import java.util.Locale;

import javax.annotation.ParametersAreNonnullByDefault;

import com.tterrag.registrate.Registrate;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.minecraft.item.Item;
import net.minecraftforge.fml.RegistryObject;
import team.chisel.Chisel;
import team.chisel.common.item.ItemChisel;
import team.chisel.common.item.ItemChisel.ChiselType;

@ParametersAreNonnullByDefault
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ChiselItems {
    
    private static final Registrate REGISTRATE = Chisel.registrate();
    
    public static final RegistryObject<ItemChisel> chisel_iron = chisel(ChiselType.IRON);
    public static final RegistryObject<ItemChisel> chisel_diamond = chisel(ChiselType.DIAMOND);
    public static final RegistryObject<ItemChisel> chisel_hitech = chisel(ChiselType.HITECH);

    public static final Item offsettool = null;
    
    private static RegistryObject<ItemChisel> chisel(ChiselType type) {
        return REGISTRATE.item(type.name().toLowerCase(Locale.ROOT) + "_chisel", p -> new ItemChisel(type, p)).register(); 
    }
    
    public static void init() {}
}