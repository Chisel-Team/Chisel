package team.chisel.common.asm;

import java.util.Map;

import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;

// Should work with anything 1.8+, so no @MCVersion
@IFMLLoadingPlugin.SortingIndex(Integer.MAX_VALUE)
public class ChiselCorePlugin implements IFMLLoadingPlugin {

    @Override
    public String[] getASMTransformerClass() {
        return new String[] {
                "team.chisel.common.asm.ChiselTransformer",
                "team.chisel.common.asm.ChiselGlowTransformer"
        };
    }

    @Override
    public String getModContainerClass() {
        return null;
    }

    @Override
    public String getSetupClass() {
        return null;
    }

    @Override
    public void injectData(Map<String, Object> data) {        
    }

    @Override
    public String getAccessTransformerClass() {
        return null;
    }

}
