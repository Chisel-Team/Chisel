package team.chisel.client.render;

import java.awt.*;
import java.util.Collection;
import java.util.UUID;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IIcon;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.common.MinecraftForge;

import org.lwjgl.opengl.GL11;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;

import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import team.chisel.init.ChiselItems;

public class DevPlayer {

    private boolean resetRender;

    private interface IDevRenderer {

        void renderPlayer(EntityPlayer player, boolean post, RenderPlayerEvent.Specials event);

        void renderExtras(EntityPlayer player, boolean post, RenderPlayerEvent.Specials event);
    }

    private class RenderHolstered implements IDevRenderer {

        private ItemStack toRender;

        private RenderHolstered(ItemStack toRender) {
            this.toRender = toRender;
        }

        @Override
        public void renderExtras(EntityPlayer player, boolean post, RenderPlayerEvent.Specials event) {
            if (!post) {
                GL11.glPushMatrix();
                Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.locationItemsTexture);
                if (player.isSneaking()) {
                    GL11.glRotatef(28.64789F, 1.0F, 0.0F, 0.0F);
                }
                boolean armor = player.getCurrentArmor(1) != null;
                GL11.glRotatef(180.0F, 1.0F, 0.0F, 0.0F);
                GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
                GL11.glTranslatef(-0.25F, -0.85F, armor ? 0.2F : 0.28F);
                GL11.glScalef(0.5F, 0.5F, 0.5F);
                GL11.glColor3f(1.0F, 1.0F, 1.0F);
                int light = 0xF000F0;
                int lightmapX = light % 0x10000;
                int lightmapY = light / 0x10000;
                OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, lightmapX, lightmapY);

                IIcon icon = toRender.getIconIndex();
                ItemRenderer.renderItemIn2D(Tessellator.instance, icon.getMaxU(), icon.getMinV(), icon.getMinU(), icon.getMaxV(), icon.getIconWidth(), icon.getIconHeight(), 1.0F / 16.0F);
                GL11.glPopMatrix();
            }
        }

        @Override
        public void renderPlayer(EntityPlayer player, boolean post, RenderPlayerEvent.Specials event) {
        }
    }

    /*private class RenderTranslucent implements IDevRenderer	{

        @Override
        public void renderExtras(EntityPlayer player, boolean post, RenderPlayerEvent.Specials event) {
        }

        @Override
        public void renderPlayer(EntityPlayer player, boolean post, RenderPlayerEvent.Specials event) {
            if (!post) {
                GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.75F);
                GL11.glEnable(GL11.GL_BLEND);
                GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
                resetRender = true;
            } else {
                GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                GL11.glDisable(GL11.GL_BLEND);
                resetRender = false;
            }
        }
    }*/

    private class RenderRainbow implements IDevRenderer {

        @Override
        public void renderExtras(EntityPlayer player, boolean post, RenderPlayerEvent.Specials event) {
        }

        @Override
        public void renderPlayer(EntityPlayer player, boolean post, RenderPlayerEvent.Specials event) {
            if (!post) {
                Color color;
                float hue = ((float) event.entityPlayer.getEntityWorld().getTotalWorldTime() % 256) / 256;
                color = new Color(Color.HSBtoRGB(hue + (event.partialRenderTick / 256), 1, 1));

                GL11.glColor4f(((float)color.getRed()) / 256, ((float)color.getGreen()) / 256, ((float)color.getBlue()) / 256, 1.0F);
                GL11.glEnable(GL11.GL_BLEND);
                GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
                resetRender = true;
            } else {
                GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                GL11.glDisable(GL11.GL_BLEND);
                resetRender = false;
            }
        }
    }

    public static final DevPlayer INSTANCE = new DevPlayer();

    private Multimap<UUID, IDevRenderer> renderMap = ArrayListMultimap.create();

    private DevPlayer() {
        RenderHolstered backChisel = new RenderHolstered(new ItemStack(ChiselItems.obsidianChisel));
        RenderRainbow translucent = new RenderRainbow();


        renderMap.putAll(UUID.fromString("a7529984-8cb2-4fb9-b799-97980f770101"), Lists.newArrayList(backChisel, translucent)); // Cricket
        renderMap.putAll(UUID.fromString("a1d2532b-ee11-4ca3-b4c5-76e168d4c98e"), Lists.newArrayList(backChisel, translucent)); // TheCricket26
        renderMap.putAll(UUID.fromString("5399b615-3440-4c66-939d-ab1375952ac3"), Lists.newArrayList(new RenderHolstered(new ItemStack(ChiselItems.diamondChisel)))); // Drullkus

        renderMap.put(UUID.fromString("671516b1-4fb3-4c03-aa7c-9c88cfab3ae8"), new RenderHolstered(new ItemStack(ChiselItems.diamondChisel))); // tterrag
        renderMap.put(UUID.fromString("ad18f501-08fa-4e7e-b324-86750009106e"), new RenderHolstered(new ItemStack(ChiselItems.chisel)));//minecreatr

        MinecraftForge.EVENT_BUS.register(this);
    }

    public static void init() {
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onPlayerRenderPre(RenderLivingEvent.Pre event) {
        if(nameIsGood(event.entity)){
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.75F);
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        }
    }

    @SubscribeEvent
    public void onPlayerRenderPost(RenderLivingEvent.Post event) {
        if (nameIsGood(event.entity)){
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            GL11.glDisable(GL11.GL_BLEND);
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onPlayerRenderSpecialPre(RenderPlayerEvent.Specials.Pre event) {
        /*Collection<IDevRenderer> renders = renderMap.get(event.entityPlayer.getUniqueID());
        for (IDevRenderer r : renders) {
            r.renderExtras(event.entityPlayer, false, event);
        }*/

        if (EnumChatFormatting.getTextWithoutFormattingCodes(event.entity.getCommandSenderName()).equals("Drullkus")) {

            Color color;
            float hue = ((float) event.entityPlayer.getEntityWorld().getTotalWorldTime() % 256) / 256;
            color = new Color(Color.HSBtoRGB(hue + (event.partialRenderTick / 256), 1, 1));

            GL11.glColor4f(((float)color.getRed()) / 256, ((float)color.getGreen()) / 256, ((float)color.getBlue()) / 256, 1.0F);
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            this.resetRender = true;
        }
    }

    @SubscribeEvent
    public void onPlayerRenderSpecialPost(RenderPlayerEvent.Specials.Post event) {
        /*Collection<IDevRenderer> renders = renderMap.get(event.entityPlayer.getUniqueID());
        for (IDevRenderer r : renders) {
            r.renderExtras(event.entityPlayer, true, event);
        }*/

        if (this.resetRender && EnumChatFormatting.getTextWithoutFormattingCodes(event.entity.getCommandSenderName()).equals("Drullkus")) {
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            GL11.glDisable(GL11.GL_BLEND);
            this.resetRender = false;
        }
    }

    private boolean nameIsGood(Entity entity){
        if(EnumChatFormatting.getTextWithoutFormattingCodes(entity.getCommandSenderName()).equals("Cricket")
            || EnumChatFormatting.getTextWithoutFormattingCodes(entity.getCommandSenderName()).equals("Drullkus")
            || EnumChatFormatting.getTextWithoutFormattingCodes(entity.getCommandSenderName()).equals("minecreatr"))
        {
            return true;
        }
        return false;
    }
}
