package com.cricketcraft.chisel.client.render;

import com.cricketcraft.chisel.entity.EntityChiselSnowman;
import net.minecraft.block.Block;
import net.minecraft.client.model.ModelSnowMan;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.client.MinecraftForgeClient;
import static net.minecraftforge.client.IItemRenderer.ItemRenderType.*;
import static net.minecraftforge.client.IItemRenderer.ItemRendererHelper.*;
import org.lwjgl.opengl.GL11;

public class RenderChiselSnowman extends RenderLiving{

    private static final ResourceLocation snowManTextures = new ResourceLocation("textures/entity/snowman.png");
    private ModelSnowMan snowMan;

    public RenderChiselSnowman() {
        super(new ModelSnowMan(), 0.5F);
        snowMan = (ModelSnowMan)super.mainModel;
        setRenderPassModel(this.snowMan);
    }

    @Override
    protected void renderEquippedItems(EntityLivingBase entityLivingBase, float size){
        this.renderEquippedItems((EntityChiselSnowman) entityLivingBase, size);
    }

    @Override
    protected ResourceLocation getEntityTexture(Entity entity) {
        return this.getEntityTexture((EntityChiselSnowman) entity);
    }

    protected ResourceLocation getEntityTexture(EntityChiselSnowman snowman){
        return snowManTextures;
    }

    protected void renderEquippedItems(EntityChiselSnowman snowman, float size){
        super.renderEquippedItems(snowman, size);
        ItemStack itemStack = snowman.getEquipmentInSlot(2);

        if(itemStack.getItem() instanceof ItemBlock){
            GL11.glPushMatrix();
            this.snowMan.head.postRender(0.0625F);

            IItemRenderer customRenderer = MinecraftForgeClient.getItemRenderer(itemStack, EQUIPPED);
            boolean is3D = (customRenderer != null && customRenderer.shouldUseRenderHelper(EQUIPPED, itemStack, BLOCK_3D));

            if(is3D || RenderBlocks.renderItemIn3d(Block.getBlockFromItem(itemStack.getItem()).getRenderType())){
                float f1 = 0.625F;
                GL11.glTranslatef(0.0F, -0.34375F, 0.0F);
                GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
                GL11.glScalef(f1, -f1, f1);
            }
            this.renderManager.itemRenderer.renderItem(snowman, itemStack, 0);
            GL11.glPopMatrix();
        }
    }
}
