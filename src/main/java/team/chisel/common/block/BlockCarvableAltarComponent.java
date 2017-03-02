package team.chisel.common.block;

import WayofTime.bloodmagic.api.altar.EnumAltarComponent;
import WayofTime.bloodmagic.api.altar.IAltarComponent;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Optional;
import team.chisel.api.block.VariationData;
import team.chisel.common.init.ChiselBlocks;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

@Optional.InterfaceList({
        @Optional.Interface(iface = "WayofTime.bloodmagic.api.incense.EnumAltarComponent", modid = "BloodMagic"),
        @Optional.Interface(iface = "WayofTime.bloodmagic.api.incense.IAltarComponent", modid = "BloodMagic")
})

@ParametersAreNonnullByDefault
public class BlockCarvableAltarComponent extends BlockCarvable implements IAltarComponent {

    public BlockCarvableAltarComponent(Material material, int index, int max, VariationData... variations) {
        super(material, index, max, variations);
    }

    @Nullable
    @Override
    @Optional.Method(modid = "BloodMagic")
    public WayofTime.bloodmagic.api.altar.EnumAltarComponent getType(World world, IBlockState state, BlockPos pos) {
        IBlockState blockFromWorld = world.getBlockState(pos);

        Block block = blockFromWorld.getBlock();

        if (block == ChiselBlocks.bloodMagic)
        {
            return EnumAltarComponent.BLOODRUNE;
        } else if (block == ChiselBlocks.glowstone || block == ChiselBlocks.glowstone1 || block == ChiselBlocks.glowstone2)
        {
            return EnumAltarComponent.GLOWSTONE;
        }
        return null;
    }
}
