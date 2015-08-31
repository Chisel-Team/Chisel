package team.chisel.client.render.item;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraftforge.client.IItemRenderer;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import team.chisel.api.rendering.IShaderRenderItem;

public class ItemStarFieldRenderer implements IItemRenderer {

    @Override
    public boolean handleRenderType(ItemStack item, ItemRenderType type) {
        //return type != ItemRenderType.INVENTORY;
        return true;
    }

    @Override
    public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper) {
        return helper == ItemRendererHelper.ENTITY_ROTATION || helper == ItemRendererHelper.ENTITY_BOBBING;
    }

    @Override
    public void renderItem(ItemRenderType type, ItemStack item, Object... data) {
        Minecraft mc = Minecraft.getMinecraft();
        this.processLightLevel(type, item, data);
        //ShaderHelper.useShader(ShaderHelper.testShader, this.shaderCallback);
        switch(type) {
            case ENTITY : {
                GL11.glPushMatrix();
                GL11.glTranslatef(-0.5F, 0F, 0F);
                if(item.isOnItemFrame())
                    GL11.glTranslatef(0F, -0.3F, 0.01F);
                render(item, null);
                GL11.glPopMatrix();

                break;
            }
            case EQUIPPED : {
                render(item, data[1] instanceof EntityPlayer ? (EntityPlayer) data[1] : null);
                break;
            }
            case EQUIPPED_FIRST_PERSON : {
                render(item, data[1] instanceof EntityPlayer ? (EntityPlayer) data[1] : null);
                break;
            }
            case INVENTORY: {
                GL11.glPushMatrix();
                GL11.glEnable(GL11.GL_BLEND);
                GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
                RenderHelper.enableGUIStandardItemLighting();

                GL11.glDisable(GL11.GL_ALPHA_TEST);
                GL11.glDisable(GL11.GL_DEPTH_TEST);

                RenderItem r = RenderItem.getInstance();
                r.renderItemIntoGUI(mc.fontRenderer, mc.getTextureManager(), item, 0, 0, true);

                if (item.getItem() instanceof IShaderRenderItem) {
                    GL11.glEnable(GL11.GL_BLEND);
                    GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
                    RenderHelper.enableGUIStandardItemLighting();

                    GL11.glDisable(GL11.GL_ALPHA_TEST);
                    GL11.glDisable(GL11.GL_DEPTH_TEST);

                    IShaderRenderItem icri = (IShaderRenderItem)(item.getItem());

                    StarFieldRendererHelper.cosmicOpacity = icri.getMaskMultiplier(item, null);
                    StarFieldRendererHelper.inventoryRender = true;
                    StarFieldRendererHelper.useShader();

                    IIcon cosmicicon = icri.getMaskTexture(item, null);

                    GL11.glColor4d(1, 1, 1, 1);

                    float minu = cosmicicon.getMinU();
                    float maxu = cosmicicon.getMaxU();
                    float minv = cosmicicon.getMinV();
                    float maxv = cosmicicon.getMaxV();

                    Tessellator t = Tessellator.instance;

                    t.startDrawingQuads();
                    t.addVertexWithUV(0, 0, 0, minu, minv);
                    t.addVertexWithUV(0, 16, 0, minu, maxv);
                    t.addVertexWithUV(16, 16, 0, maxu, maxv);
                    t.addVertexWithUV(16, 0, 0, maxu, minv);
                    t.draw();

                    StarFieldRendererHelper.releaseShader();
                    StarFieldRendererHelper.inventoryRender = false;
                }

                GL11.glEnable(GL11.GL_ALPHA_TEST);
                GL11.glEnable(GL12.GL_RESCALE_NORMAL);
                GL11.glEnable(GL11.GL_DEPTH_TEST);

                r.renderWithColor = true;

                GL11.glDisable(GL11.GL_BLEND);
                GL11.glPopMatrix();
                break;
            }
            default : break;
        }
        //ShaderHelper.releaseShader();

        //Lumberjack.log(Level.INFO, light+"");
    }

    public void render(ItemStack item, EntityPlayer player) {
        int passes = 1;
        if (item.getItem().requiresMultipleRenderPasses())
        {
            passes = item.getItem().getRenderPasses(item.getItemDamage());
        }

        GL11.glPushMatrix();
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glColor4f(1F, 1F, 1F, 1F);
        //ItemRenderer.renderItemIn2D(Tessellator.instance, f1, f2, f, f3, icon.getIconWidth(), icon.getIconHeight(), scale);

        float r,g,b;
        IIcon icon;
        float f,f1,f2,f3;
        float scale = 1F / 16F;

        //Lumberjack.log(Level.INFO, "passes: "+passes);

        for (int i = 0; i < passes; i++)
        {
            icon = this.getStackIcon(item, i, player);

            //Lumberjack.log(Level.INFO, "icon "+i+": "+icon);

            f = icon.getMinU();
            f1 = icon.getMaxU();
            f2 = icon.getMinV();
            f3 = icon.getMaxV();

            int colour = item.getItem().getColorFromItemStack(item, i);
            r = (float)(colour >> 16 & 255) / 255.0F;
            g = (float)(colour >> 8 & 255) / 255.0F;
            b = (float)(colour & 255) / 255.0F;
            GL11.glColor4f(r, g, b, 1.0F);
            ItemRenderer.renderItemIn2D(Tessellator.instance, f1, f2, f, f3, icon.getIconWidth(), icon.getIconHeight(), scale);
        }

        if (item.getItem() instanceof IShaderRenderItem) {
            GL11.glDisable(GL11.GL_ALPHA_TEST);
            GL11.glDepthFunc(GL11.GL_EQUAL);
            IShaderRenderItem icri = (IShaderRenderItem)(item.getItem());
            StarFieldRendererHelper.cosmicOpacity = icri.getMaskMultiplier(item, player);
            StarFieldRendererHelper.useShader();

            IIcon cosmicicon = icri.getMaskTexture(item, player);

            float minu = cosmicicon.getMinU();
            float maxu = cosmicicon.getMaxU();
            float minv = cosmicicon.getMinV();
            float maxv = cosmicicon.getMaxV();
            ItemRenderer.renderItemIn2D(Tessellator.instance, maxu, minv, minu, maxv, cosmicicon.getIconWidth(), cosmicicon.getIconHeight(), scale);
            StarFieldRendererHelper.releaseShader();
            GL11.glDepthFunc(GL11.GL_LEQUAL);
            GL11.glEnable(GL11.GL_ALPHA_TEST);
        }

        GL11.glDisable(GL11.GL_BLEND);
        GL11.glPopMatrix();

        GL11.glColor4f(1F, 1F, 1F, 1F);
    }

    public void processLightLevel(ItemRenderType type, ItemStack item, Object... data) {
        switch(type) {
            case ENTITY : {
                EntityItem ent = (EntityItem)(data[1]);
                if (ent != null) {
                    StarFieldRendererHelper.setLightFromLocation(ent.worldObj, MathHelper.floor_double(ent.posX), MathHelper.floor_double(ent.posY), MathHelper.floor_double(ent.posZ));
                }
                break;
            }
            case EQUIPPED : {
                EntityLivingBase ent = (EntityLivingBase)(data[1]);
                if (ent != null) {
                    StarFieldRendererHelper.setLightFromLocation(ent.worldObj, MathHelper.floor_double(ent.posX), MathHelper.floor_double(ent.posY), MathHelper.floor_double(ent.posZ));
                }
                break;
            }
            case EQUIPPED_FIRST_PERSON : {
                EntityLivingBase ent = (EntityLivingBase)(data[1]);
                if (ent != null) {
                    StarFieldRendererHelper.setLightFromLocation(ent.worldObj, MathHelper.floor_double(ent.posX), MathHelper.floor_double(ent.posY), MathHelper.floor_double(ent.posZ));
                }
                break;
            }
            case INVENTORY: {
                StarFieldRendererHelper.setLightLevel(1.2f);
                return;
            }
            default : {
                StarFieldRendererHelper.setLightLevel(1.0f);
                return;
            }
        }
    }

    public IIcon getStackIcon(ItemStack stack, int pass, EntityPlayer player) {
        return stack.getItem().getIcon(stack, pass);
    }

}
