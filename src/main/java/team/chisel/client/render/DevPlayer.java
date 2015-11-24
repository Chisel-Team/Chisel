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

    private float renderTick;

    private interface IDevRenderer {
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
                GL11.glTranslatef(-0.35F, -0.85F, armor ? 0.2F : 0.28F);
                GL11.glScalef(0.6F, 0.6F, 0.6F);
                GL11.glColor3f(1.0F, 1.0F, 1.0F);
                int light = 0xF000F0;
                int lightmapX = light % 0x10000;
                int lightmapY = light / 0x10000;
                OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, lightmapX, lightmapY);

                IIcon icon = toRender.getIconIndex();
                ItemRenderer.renderItemIn2D(Tessellator.instance, icon.getMaxU(), icon.getMinV(), icon.getMinU(), icon.getMaxV(), icon.getIconWidth(), icon.getIconHeight(), 1.0F / 16.0F);
                GL11.glPopMatrix();
            }
            renderTick = event.partialRenderTick;
        }
    }

    public static final DevPlayer INSTANCE = new DevPlayer();

    private Multimap<UUID, IDevRenderer> renderMap = ArrayListMultimap.create();

    private DevPlayer() {
        renderMap.put(UUID.fromString("a7529984-8cb2-4fb9-b799-97980f770101"), new RenderHolstered(new ItemStack(ChiselItems.obsidianChisel))); // Cricket
        renderMap.put(UUID.fromString("a1d2532b-ee11-4ca3-b4c5-76e168d4c98e"), new RenderHolstered(new ItemStack(ChiselItems.obsidianChisel))); // TheCricket26
        renderMap.put(UUID.fromString("5399b615-3440-4c66-939d-ab1375952ac3"), new RenderHolstered(new ItemStack(ChiselItems.diamondChisel))); // Drullkus

        renderMap.put(UUID.fromString("671516b1-4fb3-4c03-aa7c-9c88cfab3ae8"), new RenderHolstered(new ItemStack(ChiselItems.diamondChisel))); // tterrag
        renderMap.put(UUID.fromString("ad18f501-08fa-4e7e-b324-86750009106e"), new RenderHolstered(new ItemStack(ChiselItems.chisel))); // minecreatr

        MinecraftForge.EVENT_BUS.register(this);
    }

    public static void init() {
        //Blank method to just run the class
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onPlayerRenderPre(RenderLivingEvent.Pre event) {
        if (namesForTransparency(event.entity)) {
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.75F);
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            resetRender = true;
        }

        if (namesForRainbow(event.entity)) {
            Color color;
            float hue = ((float) event.entity.worldObj.getTotalWorldTime() % 256) / 256;
            color = new Color(Color.HSBtoRGB(hue + (renderTick / 256), 1, 1));

            float r = ((((float) color.getRed() / 256.0F) / 2.0F) + 0.5F);
            float g = ((((float) color.getGreen() / 256.0F) / 2.0F) + 0.5F);
            float b = ((((float) color.getBlue() / 256.0F) / 2.0F) + 0.5F);

            GL11.glColor4f((r > 1.0F ? 1.0F : r), (g > 1.0F ? 1.0F : g), (b > 1.0F ? 1.0F : b), 1.0F);
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            resetRender = true;
        }
    }

    @SubscribeEvent
    public void onPlayerRenderPost(RenderLivingEvent.Post event) {
        if (namesForTransparency(event.entity) || namesForRainbow(event.entity)) {
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            GL11.glDisable(GL11.GL_BLEND);
            resetRender = false;
        }
    }

    @SubscribeEvent
    public void onPlayerRenderSpecialPre(RenderPlayerEvent.Specials.Pre event) {
        Collection<IDevRenderer> renders = renderMap.get(event.entityPlayer.getUniqueID());
        for (IDevRenderer r : renders) {
            r.renderExtras(event.entityPlayer, false, event);
        }
    }

    @SubscribeEvent
    public void onPlayerRenderSpecialPost(RenderPlayerEvent.Specials.Post event) {
        Collection<IDevRenderer> renders = renderMap.get(event.entityPlayer.getUniqueID());
        for (IDevRenderer r : renders) {
            r.renderExtras(event.entityPlayer, true, event);
        }
    }

    private boolean namesForTransparency(Entity entity) {
        if (EnumChatFormatting.getTextWithoutFormattingCodes(entity.getCommandSenderName()).equals("Cricket") || EnumChatFormatting.getTextWithoutFormattingCodes(entity.getCommandSenderName())
            .equals("minecreatr")) {
            return true;
        }
        return false;
    }

    private boolean namesForRainbow(Entity entity) {
        if (EnumChatFormatting.getTextWithoutFormattingCodes(entity.getCommandSenderName()).equals("Drullkus")) {
            return true;
        }
        return false;
    }
}
