package info.jbcs.minecraft.chisel.client.gui;

import info.jbcs.minecraft.chisel.block.tileentity.TileEntityAutoChisel;
import info.jbcs.minecraft.chisel.inventory.ContainerAutoChisel;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class GuiAutoChisel extends GuiContainer {

    private static final ResourceLocation gui = new ResourceLocation("chisel:textures/autochisel-gui.png");

    public GuiAutoChisel(InventoryPlayer inventoryPlayer, TileEntityAutoChisel tileEntityAutoChisel){
        super(new ContainerAutoChisel(inventoryPlayer, tileEntityAutoChisel));
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3){
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.renderEngine.bindTexture(gui);
        int x = (width - xSize) / 2;
        int y = (height - ySize) / 2;
        this.drawTexturedModalRect(x ,y, 0, 0, xSize, ySize);
    }
}
