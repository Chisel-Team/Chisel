package com.cricketcraft.chisel.client.handler;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.UnmodifiableIterator;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MovingObjectPosition;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.opengl.GL11;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Handler to debug stuff
 *
 * @author minecreatr
 */
public class DebugHandler {

    @SubscribeEvent
    public void onDrawScreen(RenderGameOverlayEvent.Post event){
        if (!Minecraft.getMinecraft().gameSettings.advancedItemTooltips){
            return;
        }
        IBlockState state = getBlockStateLooking();
        if (state==null){
            return;
        }
        List<String> text = new ArrayList<String>();
        text.add(EnumChatFormatting.BLUE+"Block: "+EnumChatFormatting.GREEN+state.getBlock().getUnlocalizedName());
        text.add(EnumChatFormatting.BLUE+"Meta: "+EnumChatFormatting.GREEN+state.getBlock().getMetaFromState(state));
        text.add(EnumChatFormatting.BLUE+"State Class: "+EnumChatFormatting.GREEN+state.getClass().toString());
        for (IProperty  p : (ImmutableSet<IProperty>)state.getProperties().keySet()){
            String s = p.getName()+": ";
            text.add(s+state.getValue(p));
        }

        int cur = 0;
        for (String l : text){
            Minecraft.getMinecraft().fontRendererObj.drawString(l, 10, 10+(cur*10), 1);
            cur++;
        }
    }

    private static IBlockState getBlockStateLooking(){
        Minecraft mc = Minecraft.getMinecraft();
        if(mc.objectMouseOver != null && mc.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK && mc.objectMouseOver.getBlockPos() != null) {
            BlockPos pos = mc.objectMouseOver.getBlockPos();
            return mc.theWorld.getBlockState(pos).getBlock().getExtendedState(mc.theWorld.getBlockState(pos), mc.theWorld, pos);
        }
        return null;
    }
}
