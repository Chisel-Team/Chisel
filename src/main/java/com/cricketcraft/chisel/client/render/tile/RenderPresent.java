package com.cricketcraft.chisel.client.render.tile;

import com.cricketcraft.chisel.Chisel;
import com.cricketcraft.chisel.block.BlockPresent;
import com.cricketcraft.chisel.block.tileentity.TileEntityPresent;
import cpw.mods.fml.common.FMLLog;
import net.minecraft.block.Block;
import net.minecraft.client.model.ModelChest;
import net.minecraft.client.renderer.tileentity.TileEntityChestRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

public class RenderPresent extends TileEntityChestRenderer {
    private ModelChest chest = new ModelChest();

    public RenderPresent() {
    }

    public void renderTileEntityAt(TileEntityPresent present, double x, double y, double z, float partialTicks) {
        int i;

        if (!present.hasWorldObj()) {
            i = 0;
        } else {
            Block block = present.getBlockType();
            i = present.getBlockMetadata();

            if (block instanceof BlockPresent && i == 0) {
                try {
                    ((BlockPresent) block).changeMetadataBasedOffSpecs(present.getWorldObj(), present.xCoord, present.yCoord, present.zCoord);
                } catch (ClassCastException e) {
                    FMLLog.severe("[Chisel 2] Attempted to render a present at %d,  %d, %d that was not a present", present.xCoord, present.yCoord, present.zCoord);
                }
                i = present.getBlockMetadata();
            }

            present.checkForAdjacentPresents();
        }

        if (present.adjacentChestZNeg == null && present.adjacentChestXNeg == null) {
            BlockPresent blockPresent = (BlockPresent) present.getWorldObj().getBlock(present.xCoord, present.yCoord, present.zCoord);
            this.bindTexture(new ResourceLocation(Chisel.MOD_ID, blockPresent.getKindOfChest(present.type) + ".png"));

            GL11.glPushMatrix();
            GL11.glEnable(GL12.GL_RESCALE_NORMAL);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            GL11.glTranslatef((float) x, (float) y + 1.0F, (float) z + 1.0F);
            GL11.glScalef(1.0F, -1.0F, -1.0F);
            GL11.glTranslatef(0.5F, 0.5F, 0.5F);
            short short1 = 0;

            if (i == 2) {
                short1 = 180;
            }

            if (i == 3) {
                short1 = 0;
            }

            if (i == 4) {
                short1 = 90;
            }

            if (i == 5) {
                short1 = -90;
            }

            if (i == 2 && present.adjacentChestXPos != null) {
                GL11.glTranslatef(1.0F, 0.0F, 0.0F);
            }

            if (i == 5 && present.adjacentChestZPos != null) {
                GL11.glTranslatef(0.0F, 0.0F, -1.0F);
            }

            GL11.glRotatef((float) short1, 0.0F, 1.0F, 0.0F);
            GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
            float f1 = present.prevLidAngle + (present.lidAngle - present.prevLidAngle) * partialTicks;
            float f2;

            if (present.adjacentChestZNeg != null) {
                f2 = present.adjacentChestZNeg.prevLidAngle + (present.adjacentChestZNeg.lidAngle - present.adjacentChestZNeg.prevLidAngle) * partialTicks;

                if (f2 > f1) {
                    f1 = f2;
                }
            }

            if (present.adjacentChestXNeg != null) {
                f2 = present.adjacentChestXNeg.prevLidAngle + (present.adjacentChestXNeg.lidAngle - present.adjacentChestXNeg.prevLidAngle) * partialTicks;

                if (f2 > f1) {
                    f1 = f2;
                }
            }

            f1 = 1.0F - f1;
            f1 = 1.0F - f1 * f1 * f1;
            this.chest.chestLid.rotateAngleX = -(f1 * (float) Math.PI / 2.0F);
            this.chest.renderAll();
            GL11.glDisable(GL12.GL_RESCALE_NORMAL);
            GL11.glPopMatrix();
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        }
    }

    @Override
    public void renderTileEntityAt(TileEntity tileEntity, double x, double y, double z, float partialTicks) {
        this.renderTileEntityAt((TileEntityPresent) tileEntity, x, y, z, partialTicks);
    }
}
