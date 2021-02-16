package team.chisel.legacy;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.tterrag.registrate.Registrate;
import com.tterrag.registrate.providers.ProviderType;
import com.tterrag.registrate.util.NonNullLazyValue;

import net.minecraftforge.fml.common.Mod;
import team.chisel.common.Reference;
import team.chisel.common.init.ChiselTabs;

@Mod("chisel-legacy")
public class ChiselLegacy implements Reference {

    public static final Logger logger = LogManager.getLogger("Chisel Legacy");

    private static final NonNullLazyValue<Registrate> REGISTRATE = new NonNullLazyValue<Registrate>(() -> {
        Registrate ret = Registrate.create("chisel-legacy").itemGroup(() -> ChiselTabs.legacy);
        ret.addDataGenerator(ProviderType.LANG, prov -> prov.add(ChiselTabs.legacy, "Chisel - Legacy"));
        return ret;
    });

	public ChiselLegacy() {
		LegacyFeatures.init();
	}
    
    public static Registrate registrate() {
        return REGISTRATE.getValue();
    }
}
