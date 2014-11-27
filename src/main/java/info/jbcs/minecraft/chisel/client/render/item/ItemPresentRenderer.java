package info.jbcs.minecraft.chisel.client.render.item;

import cpw.mods.fml.client.FMLClientHandler;
import info.jbcs.minecraft.chisel.Chisel;
import net.minecraft.client.model.ModelChest;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.IItemRenderer;
import org.lwjgl.opengl.GL11;

public class ItemPresentRenderer implements IItemRenderer {
    private final ModelChest model = new ModelChest();

    @Override
    public boolean handleRenderType(ItemStack item, ItemRenderType type) {
        return type != ItemRenderType.FIRST_PERSON_MAP;
    }

    @Override
    public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper) {
        return true;
    }

    @Override
    public void renderItem(ItemRenderType type, ItemStack item, Object... data) {
        FMLClientHandler.instance().getClient().renderEngine.bindTexture(new ResourceLocation(Chisel.MOD_ID, "textures/blocks/present/1.png"));
        switch (type) {
            case ENTITY:
                renderBlock(0.0F, 1.0F, 0.0F);
                break;
            case EQUIPPED:
                renderBlock(0.5F, 1.5F, 0.5F);
                break;
            case EQUIPPED_FIRST_PERSON:
                renderBlock(0.5F, 1.0F, 0.5F);
                break;
            case INVENTORY:
                renderInventory(0.0F, 0.625F, 0.0F);
                break;
            default:
                break;
        }
    }

    private void renderBlock(float x, float y, float z) {
        GL11.glPushMatrix();
        GL11.glTranslatef(x, y, z);
        GL11.glScaled(-1, -1, 1);
        model.renderAll();
        GL11.glPopMatrix();
    }

    private void renderInventory(float x, float y, float z) {
        GL11.glPushMatrix();
        GL11.glTranslatef(x, y, z);
        GL11.glRotatef(180F, 1F, 0, 0);
        GL11.glRotatef(-90F, 0, 1F, 0);
        GL11.glScalef(0.8F, 0.8F, 0.8F);
        model.renderAll();
        GL11.glPopMatrix();
    }
}
