package team.chisel.common.integration.bloodmagic;

import WayofTime.bloodmagic.api.BloodMagicPlugin;
import WayofTime.bloodmagic.api.IBloodMagicAPI;
import WayofTime.bloodmagic.api.IBloodMagicPlugin;
import team.chisel.common.block.BlockCarvable;
import team.chisel.common.init.ChiselBlocks;

@BloodMagicPlugin
public class ChiselBMPlugin implements IBloodMagicPlugin {

    private static IBloodMagicAPI api;

    @Override
    public void register(IBloodMagicAPI api) {
        ChiselBMPlugin.api = api;

        setAltarComponent(ChiselBlocks.bloodmagic, "BLOODRUNE");
        setAltarComponent(ChiselBlocks.glowstone, "GLOWSTONE");
        setAltarComponent(ChiselBlocks.glowstone1, "GLOWSTONE");
        setAltarComponent(ChiselBlocks.glowstone2, "GLOWSTONE");

        setTranquility(ChiselBlocks.dirt, "EARTHEN", 0.25D);
        setTranquility(ChiselBlocks.netherrack, "FIRE", 0.5D);
        setTranquility(ChiselBlocks.lavastone, "LAVA", 1.2D);
        setTranquility(ChiselBlocks.lavastone1, "LAVA", 1.2D);
        setTranquility(ChiselBlocks.lavastone2, "LAVA", 1.2D);
        setTranquility(ChiselBlocks.waterstone, "WATER", 1.0D);
        setTranquility(ChiselBlocks.waterstone1, "WATER", 1.0D);
        setTranquility(ChiselBlocks.waterstone2, "WATER", 1.0D);
    }

    private static void setAltarComponent(BlockCarvable block, String componentType) {
        for (int i = 0; i < block.getTotalVariations(); i++)
            api.registerAltarComponent(block.getStateFromMeta(i), componentType);
    }

    private static void setTranquility(BlockCarvable block, String tranquilityType, double value) {
        for (int i = 0; i < block.getTotalVariations(); i++)
            api.getValueManager().setTranquility(block.getStateFromMeta(i), tranquilityType, value);
    }
}
