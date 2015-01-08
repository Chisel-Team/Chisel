package com.cricketcraft.chisel.client.render.tile;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.client.model.obj.Face;
import net.minecraftforge.client.model.obj.GroupObject;
import net.minecraftforge.client.model.obj.TextureCoordinate;
import net.minecraftforge.client.model.obj.Vertex;
import net.minecraftforge.client.model.obj.WavefrontObject;

import org.lwjgl.opengl.GL11;

import com.cricketcraft.chisel.Chisel;
import com.cricketcraft.chisel.block.tileentity.TileEntityAutoChisel;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class RenderAutoChisel extends TileEntitySpecialRenderer implements ISimpleBlockRenderingHandler, IItemRenderer {

    private static final WavefrontObject model = new WavefrontObject(new ResourceLocation(Chisel.MOD_ID, "models/autoChisel/autoChisel.obj"));
    private static final ResourceLocation texture = new ResourceLocation(Chisel.MOD_ID, "textures/blocks/autoChisel/autoChisel.png");
   
    @Override
    public boolean handleRenderType(ItemStack item, ItemRenderType type)
    {
        return true;
    }

    @Override
    public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper)
    {
        return helper == ItemRendererHelper.INVENTORY_BLOCK || helper == ItemRendererHelper.ENTITY_BOBBING || helper == ItemRendererHelper.ENTITY_ROTATION;
    }

    @Override
    public void renderItem(ItemRenderType type, ItemStack item, Object... data)
    {
        switch(type)
        {
        case ENTITY:
            GL11.glTranslatef(-0.5f, 0, 0.5f);
            break;
        case EQUIPPED:
            GL11.glRotatef(20, 0, 1, 0);
            GL11.glRotatef(-15, 1, 0, -1);
            GL11.glScalef(0.65f, 0.65f, 0.65f);
            GL11.glTranslatef(0.75f, -0.5f, 1.25f);
            break;
        case EQUIPPED_FIRST_PERSON:
            GL11.glRotatef(20, 0, 0, 1);
            GL11.glRotatef(30, 0, 1, 0);
            GL11.glTranslatef(0.2f, 0, -0.1f);
            GL11.glScalef(0.5f, 0.5f, 0.5f);
            break;
        case FIRST_PERSON_MAP:
            break;
        case INVENTORY:
            GL11.glTranslatef(-0.5f, -0.5f, 0.5f);
            break;
        }
        
        Minecraft.getMinecraft().renderEngine.bindTexture(texture);
        model.renderAll();
    }

    @Override
    public void renderInventoryBlock(Block block, int metadata, int modelId, RenderBlocks renderer)
    {
        ;
    }

    @Override
    public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer)
    {
        Tessellator tes = Tessellator.instance;
        IIcon icon = block.getIcon(0, 0);
        tes.setBrightness(block.getMixedBrightnessForBlock(world, x, y, z));
        tes.setColorOpaque_F(1, 1, 1);
        tes.addTranslation(x, y, z + 1);
        for (GroupObject go : model.groupObjects)
        {
            for (Face f : go.faces)
            {
                Vertex n = f.faceNormal;
                tes.setNormal(n.x, n.y, n.z);
                for (int i = 0; i < f.vertices.length; i++)
                {
                    Vertex v = f.vertices[i];
                    TextureCoordinate t = f.textureCoordinates[i];
                    tes.addVertexWithUV(v.x, v.y, v.z, icon.getInterpolatedU(t.u * 16), icon.getInterpolatedV(t.v * 16));
                }
            }
        }
        tes.addTranslation(-x, -y, -z - 1);
        return true;
    }

    @Override
    public boolean shouldRender3DInInventory(int modelId)
    {
        return true;
    }

    @Override
    public int getRenderId()
    {
        return Chisel.renderAutoChiselId;
    }
    
	private static EntityItem ghostItem;

	private final RenderItem renderTargetAndChisel, renderBase;

	public RenderAutoChisel() {
		Chisel.renderAutoChiselId = RenderingRegistry.getNextAvailableRenderId();

		renderTargetAndChisel = new RenderItem() {

			@Override
			public boolean shouldBob() {
				return false;
			}
		};
		renderTargetAndChisel.setRenderManager(RenderManager.instance);

		renderBase = new RenderItem() {

			@Override
			public boolean shouldBob() {
				return true;
			}
		};

		renderBase.setRenderManager(RenderManager.instance);
	}

	@Override
	public void renderTileEntityAt(TileEntity tile, double x, double y, double z, float scale) {

		// Render Blocks
		TileEntityAutoChisel autoChisel = (TileEntityAutoChisel) tile;
		EntityItem item = autoChisel.getItemForRendering(1);

		// TODO item rendering
//		if (item != null) {
//			GL11.glPushMatrix();
//			GL11.glTranslated(x + 0.5, y + 0.5, z + 0.5);
//			GL11.glScaled(3, 3, 3);
//			renderTargetAndChisel.doRender(item, 0, 0, 0, 0, 0);
//			GL11.glPopMatrix();
//		}
//
//		EntityItem item2 = autoChisel.getItemForRendering(0);
//
//		if (item2 != null) {
//			GL11.glPushMatrix();
//			GL11.glTranslated(x + 0.5, y + 1.5, z + 0.5);
//			GL11.glScaled(1, 1, 1);
//			renderBase.doRender(item2, 0, 0, 0, 0, 0);
//			GL11.glPopMatrix();
//		}
	}

	@SideOnly(Side.CLIENT)
	public EntityItem getGhostItem(World world, ItemStack itemStack) {
		if (ghostItem == null) {
			ghostItem = new EntityItem(world);
			ghostItem.hoverStart = 0.0F;
		}

		if (itemStack == null) {
			return null;
		} else {
			ghostItem.setEntityItemStack(itemStack);
			return ghostItem;
		}
	}

}
