package team.chisel.common.block;

import WayofTime.bloodmagic.api.altar.IAltarComponent;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Optional;
import team.chisel.api.block.VariationData;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

@Optional.Interface(iface = "WayofTime.bloodmagic.api.altar.IAltarComponent", modid = "BloodMagic")

@ParametersAreNonnullByDefault
public class BlockCarvableAltarComponent extends BlockCarvable implements IAltarComponent {

    public BlockCarvableAltarComponent(Material material, int index, int max, VariationData... variations) {
        super(material, index, max, variations);
    }

    @Nullable
    @Override
    @Optional.Method(modid = "BloodMagic")
    public WayofTime.bloodmagic.api.altar.EnumAltarComponent getType(World world, IBlockState state, BlockPos pos) {
        return WayofTime.bloodmagic.api.altar.EnumAltarComponent.BLOODRUNE;
    }
}
