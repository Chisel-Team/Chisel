package team.chisel.common.integration.waila;

import mcp.mobius.waila.api.*;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import team.chisel.Chisel;
import team.chisel.api.block.ICarvable;
import team.chisel.common.block.ItemChiselBlock;

import java.util.List;

@WailaPlugin(id = Chisel.MOD_ID)
public class ChiselDataHandler implements IWailaPlugin, IBlockComponentProvider {

    @Override
    public void register(IRegistrar registrar) {
        registrar.addComponent(this, TooltipPosition.HEAD, ICarvable.class);
        registrar.addComponent(this, TooltipPosition.BODY, ICarvable.class);
    }

    @Override
    public void appendHead(ITooltip tooltip, IBlockAccessor accessor, IPluginConfig config) {
        if (accessor.getBlock() instanceof ICarvable) {
            ItemStack stack = accessor.getStack();

            IWailaConfig.Formatter formatter = IWailaConfig.get().getFormatter();
            tooltip.setLine(WailaConstants.OBJECT_NAME_TAG, formatter.blockName(stack.getHoverName().getString()));
        }
    }

    @Override
    public void appendBody(List<Component> tooltip, IBlockAccessor accessor, IPluginConfig config) {
        if (accessor.getBlock() instanceof ICarvable) {
            ItemStack stack = accessor.getStack();
            if (stack.getItem() instanceof ItemChiselBlock) {
                ((ItemChiselBlock) stack.getItem()).addTooltips(stack, tooltip);
            } else {
                ICarvable block = (ICarvable) accessor.getBlock();
                ItemChiselBlock.addTooltips(stack, block, tooltip);
            }
        }
    }
}
