package com.cricketcraft.minecraft.chisel.client.gui;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import com.cricketcraft.minecraft.chisel.block.tileentity.TileEntityPresent;
import com.cricketcraft.minecraft.chisel.inventory.ContainerPresent;

public class GuiPresent extends GuiContainer {
    private static final ResourceLocation gui = new ResourceLocation("chisel:textures/present-gui.png");
    private IInventory upper, lower;
    private int rows;

    public GuiPresent(InventoryPlayer inventoryPlayer, TileEntityPresent tileEntityPresent) {
        super(new ContainerPresent(inventoryPlayer, tileEntityPresent));
        rows = tileEntityPresent.getSizeInventory() / 9;
        this.ySize = 114 + rows * 18;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float p_146976_1_, int p_146976_2_, int p_146976_3_) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(gui);
        int x = (this.width - this.xSize) / 2;
        int y = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(x, y, 0, 0, this.xSize, this.rows * 18 + 17);
        this.drawTexturedModalRect(x, y + this.rows * 18 + 17, 0, 126, this.xSize, 96);
    }
}
