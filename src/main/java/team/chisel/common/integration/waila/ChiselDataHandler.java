package team.chisel.common.integration.waila;

import java.util.List;

import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import mcp.mobius.waila.api.IWailaDataProvider;
import mcp.mobius.waila.api.IWailaPlugin;
import mcp.mobius.waila.api.IWailaRegistrar;
import mcp.mobius.waila.api.WailaPlugin;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import team.chisel.api.block.ICarvable;
import team.chisel.common.block.ItemChiselBlock;

@WailaPlugin
public class ChiselDataHandler implements IWailaPlugin, IWailaDataProvider {

    @Override
    public ItemStack getWailaStack(IWailaDataAccessor accessor, IWailaConfigHandler configHandler) {
        return null;
    }

    @Override
    public List<String> getWailaHead(ItemStack stack, List<String> strings, IWailaDataAccessor accessor, IWailaConfigHandler configHandler) {
        return strings;
    }

    @SuppressWarnings("null")
    @Override
    @SideOnly(Side.CLIENT)
    public List<String> getWailaBody(ItemStack stack, List<String> strings, IWailaDataAccessor accessor, IWailaConfigHandler configHandler) {
        if (accessor.getBlock() instanceof ICarvable) {
            if (stack.getItem() instanceof ItemChiselBlock) {
                ((ItemChiselBlock)stack.getItem()).addTooltips(stack, strings);
            } else {
                ICarvable block = (ICarvable) accessor.getBlock();
                int variation = block.getVariationIndex(accessor.getBlockState());
                ItemChiselBlock.addTooltips(block, variation, strings);
            }
        }
        return strings;
    }

    @Override
    public List<String> getWailaTail(ItemStack stack, List<String> strings, IWailaDataAccessor accessor, IWailaConfigHandler configHandler) {
        return strings;
    }

    @Override
    public NBTTagCompound getNBTData(EntityPlayerMP player, TileEntity te, NBTTagCompound tag, World world, BlockPos pos) {
        return tag;
    }

    @Override
    public void register(IWailaRegistrar registrar) {
        registrar.registerBodyProvider(this, ICarvable.class);
    }
}
