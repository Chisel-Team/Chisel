package team.chisel.client.handler;

import team.chisel.client.render.IBlockResources;
import team.chisel.common.block.BlockCarvable;
import team.chisel.common.util.SubBlockUtil;
import team.chisel.common.variation.PropertyVariation;
import team.chisel.common.variation.Variation;
import com.google.common.collect.ImmutableSet;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MovingObjectPosition;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.model.IFlexibleBakedModel;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;

/**
 * Handler to debug stuff
 *
 * @author minecreatr
 */
public class DebugHandler {

    @SubscribeEvent
    public void onDrawScreen(RenderGameOverlayEvent.Post event) {
        if (!Minecraft.getMinecraft().gameSettings.advancedItemTooltips) {
            return;
        }
        IBlockState state = getBlockStateLooking();
        if (state == null) {
            return;
        }
        List<String> text = new ArrayList<String>();
        text.add(EnumChatFormatting.BLUE + "Block: " + EnumChatFormatting.GREEN + GameRegistry.findUniqueIdentifierFor(state.getBlock()).toString());
        text.add(EnumChatFormatting.BLUE + "Meta: " + EnumChatFormatting.GREEN + state.getBlock().getMetaFromState(state));
        text.add(EnumChatFormatting.BLUE + "State Class: " + EnumChatFormatting.GREEN + state.getClass().toString());
        if (state.getBlock() instanceof BlockCarvable) {
            BlockCarvable block = (BlockCarvable) state.getBlock();
            PropertyVariation VARIATION = ((BlockCarvable) state.getBlock()).getType().getPropertyVariation();
            IBlockResources r = SubBlockUtil.getResources(block, (Variation) state.getValue(VARIATION));
            text.add(EnumChatFormatting.BLUE + "Type: " + EnumChatFormatting.GREEN + r.getType());
        }
        Minecraft mc = Minecraft.getMinecraft();
        IBakedModel m = mc.getBlockRendererDispatcher().getModelFromBlockState(state, mc.theWorld, mc.objectMouseOver.getBlockPos());
        if (m instanceof IFlexibleBakedModel.Wrapper) {
            m = ReflectionHelper.getPrivateValue(IFlexibleBakedModel.Wrapper.class, (IFlexibleBakedModel.Wrapper) m, "parent");
        }
        text.add(EnumChatFormatting.BLUE + "Model Class: " + m.getClass());
        for (IProperty p : (ImmutableSet<IProperty>) state.getProperties().keySet()) {
            String s = p.getName() + ": ";
            text.add(s + state.getValue(p));
        }

        int cur = 0;
        for (String l : text) {
            Minecraft.getMinecraft().fontRendererObj.drawString(l, 10, 10 + (cur * 10), 1);
            cur++;
        }
        GL11.glColor3f(1f, 1f, 1f);
        Minecraft.getMinecraft().getTextureManager().bindTexture(Gui.icons);

    }

    private static IBlockState getBlockStateLooking() {
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.objectMouseOver != null && mc.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK && mc.objectMouseOver.getBlockPos() != null) {
            BlockPos pos = mc.objectMouseOver.getBlockPos();
            return mc.theWorld.getBlockState(pos).getBlock().getExtendedState(mc.theWorld.getBlockState(pos), mc.theWorld, pos);
        }
        return null;
    }

}
