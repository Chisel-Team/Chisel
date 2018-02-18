package team.chisel.common.integration.bloodmagic;

import WayofTime.bloodmagic.api.BloodMagicPlugin;
import WayofTime.bloodmagic.api.IBloodMagicAPI;
import WayofTime.bloodmagic.api.IBloodMagicPlugin;
import net.minecraft.block.state.IBlockState;
import team.chisel.Features;
import team.chisel.common.block.BlockCarvable;
import team.chisel.common.init.ChiselBlocks;

@BloodMagicPlugin
public class ChiselBMPlugin implements IBloodMagicPlugin {

    private static IBloodMagicAPI api;

    @Override
    public void register(IBloodMagicAPI api) {
        ChiselBMPlugin.api = api;

        setAltarComponent(Features.BLOOD_MAGIC, ChiselBlocks.bloodmagic, "BLOODRUNE");
        setAltarComponent(Features.GLOWSTONE, ChiselBlocks.glowstone, "GLOWSTONE");
        setAltarComponent(Features.GLOWSTONE, ChiselBlocks.glowstone1, "GLOWSTONE");
        setAltarComponent(Features.GLOWSTONE, ChiselBlocks.glowstone2, "GLOWSTONE");

        setTranquility(Features.DIRT, ChiselBlocks.dirt, "EARTHEN", 0.25D);
        setTranquility(Features.NETHERRACK, ChiselBlocks.netherrack, "FIRE", 0.5D);
        setTranquility(Features.LAVASTONE, ChiselBlocks.lavastone, "LAVA", 1.2D);
        setTranquility(Features.LAVASTONE, ChiselBlocks.lavastone1, "LAVA", 1.2D);
        setTranquility(Features.LAVASTONE, ChiselBlocks.lavastone2, "LAVA", 1.2D);
        setTranquility(Features.WATERSTONE, ChiselBlocks.waterstone, "WATER", 1.0D);
        setTranquility(Features.WATERSTONE, ChiselBlocks.waterstone1, "WATER", 1.0D);
        setTranquility(Features.WATERSTONE, ChiselBlocks.waterstone2, "WATER", 1.0D);
    }

    private static void setAltarComponent(Features feature, BlockCarvable block, String componentType) {
        if (feature.enabled())
            for (IBlockState state : block.getBlockState().getValidStates())
                api.registerAltarComponent(state, componentType);
    }

    private static void setTranquility(Features feature, BlockCarvable block, String tranquilityType, double value) {
        if (feature.enabled())
            for (IBlockState state : block.getBlockState().getValidStates())
                api.getValueManager().setTranquility(state, tranquilityType, value);
    }
}
