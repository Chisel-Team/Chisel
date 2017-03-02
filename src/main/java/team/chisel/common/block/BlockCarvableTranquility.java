package team.chisel.common.block;

import WayofTime.bloodmagic.api.incense.EnumTranquilityType;
import WayofTime.bloodmagic.api.incense.ITranquilityHandler;
import WayofTime.bloodmagic.api.incense.IncenseTranquilityRegistry;
import WayofTime.bloodmagic.api.incense.TranquilityStack;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Optional;
import team.chisel.api.block.VariationData;
import team.chisel.common.init.ChiselBlocks;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

@Optional.InterfaceList({
        @Optional.Interface(iface = "WayofTime.bloodmagic.api.incense.EnumTranquilityType", modid = "BloodMagic"),
        @Optional.Interface(iface = "WayofTime.bloodmagic.api.incense.ITranquilityHandler", modid = "BloodMagic"),
        @Optional.Interface(iface = "WayofTime.bloodmagic.api.incense.IncenseTranquilityRegistry", modid = "BloodMagic"),
        @Optional.Interface(iface = "WayofTime.bloodmagic.api.incense.TranquilityStack", modid = "BloodMagic")
    })

@ParametersAreNonnullByDefault
public class BlockCarvableTranquility extends BlockCarvable implements ITranquilityHandler {

    public BlockCarvableTranquility(Material material, int index, int max, VariationData... variations) {
        super(material, index, max, variations);

        if (Loader.isModLoaded("BloodMagic"))
        {
            addHandler();
        }
    }

    @Optional.Method(modid = "BloodMagic")
    private void addHandler() {
        IncenseTranquilityRegistry.registerTranquilityHandler(this);
    }

    @Nonnull
    @Override
    @Optional.Method(modid = "BloodMagic")
    public TranquilityStack getTranquilityOfBlock(World world, BlockPos pos, Block block, IBlockState state) {
        if (block instanceof BlockCarvableTranquility)
        {
            if (block == ChiselBlocks.dirt) {
                return new TranquilityStack(EnumTranquilityType.EARTHEN, 0.25);
            } else if (block == ChiselBlocks.netherrack) {
                return new TranquilityStack(EnumTranquilityType.FIRE, 0.5);
            } else if (block == ChiselBlocks.lavastone || block == ChiselBlocks.lavastone1 || block == ChiselBlocks.lavastoneextra) {
                return new TranquilityStack(EnumTranquilityType.LAVA, 1.2);
            } else if (block == ChiselBlocks.waterstone || block == ChiselBlocks.waterstone1 || block == ChiselBlocks.waterstoneextra) {
                return new TranquilityStack(EnumTranquilityType.WATER, 1);
            }
        }

        return null;
    }
}
