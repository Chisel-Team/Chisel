package team.chisel.legacy;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.tterrag.registrate.Registrate;
import com.tterrag.registrate.providers.ProviderType;

import net.minecraftforge.common.util.Lazy;
import net.minecraftforge.fml.common.Mod;
import team.chisel.common.Reference;
import team.chisel.common.init.ChiselTabs;

@Mod("chisel_legacy")
public class ChiselLegacy implements Reference {

    public static final Logger logger = LogManager.getLogger("Chisel Legacy");

    private static final Lazy<Registrate> REGISTRATE = Lazy.of(() -> {
        Registrate ret = Registrate.create("chisel_legacy").creativeModeTab(() -> ChiselTabs.legacy);
        ret.addDataGenerator(ProviderType.LANG, prov -> prov.add(ChiselTabs.legacy, "Chisel - Legacy"));
        return ret;
    });

	public ChiselLegacy() {
		LegacyFeatures.init();
	}
    
    public static Registrate registrate() {
        return REGISTRATE.get();
    }
}
