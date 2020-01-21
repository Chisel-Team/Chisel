package team.chisel.common.init;

import java.util.Arrays;
import java.util.Locale;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.annotation.ParametersAreNonnullByDefault;

import com.tterrag.registrate.Registrate;
import com.tterrag.registrate.util.RegistryEntry;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import team.chisel.Chisel;
import team.chisel.api.IChiselGuiType.ChiselGuiType;
import team.chisel.common.item.ItemChisel;
import team.chisel.common.item.ItemChisel.ChiselType;
import team.chisel.common.item.ItemOffsetTool;

@ParametersAreNonnullByDefault
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ChiselItems {
    
    private static final Registrate REGISTRATE = Chisel.registrate();
    
    public static final Map<ChiselType, RegistryEntry<ItemChisel>> CHISELS = Arrays.stream(ChiselType.values())
            .collect(Collectors.toMap(Function.identity(), ChiselItems::chisel));
    
    static { ChiselGuiType.values(); } // Init container types
    
    public static final RegistryEntry<ItemOffsetTool> offsettool = REGISTRATE.item("offset_tool", ItemOffsetTool::new)
            .properties(p -> p.group(ChiselTabs.tab))
            .register();
    
    private static RegistryEntry<ItemChisel> chisel(ChiselType type) {
        return REGISTRATE.item(type.name().toLowerCase(Locale.ROOT) + "_chisel", p -> new ItemChisel(type, p))
                .properties(p -> p.group(ChiselTabs.tab))
                .register();
    }
    
    public static void init() {}
}