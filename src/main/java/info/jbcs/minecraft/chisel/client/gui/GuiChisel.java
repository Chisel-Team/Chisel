package info.jbcs.minecraft.chisel.client.gui;

import info.jbcs.minecraft.chisel.inventory.ContainerChisel;
import info.jbcs.minecraft.chisel.inventory.InventoryChiselSelection;
import info.jbcs.minecraft.utilities.GeneralClient;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;

import org.lwjgl.opengl.GL11;

public class GuiChisel extends GuiContainer
{
    public EntityPlayer player;
    public ContainerChisel container;

    public GuiChisel(InventoryPlayer iinventory, InventoryChiselSelection menu)
    {
        super(new ContainerChisel(iinventory, menu));
        player = iinventory.player;
        height = 177;

        container = (ContainerChisel) inventorySlots;
    }

    @Override
    public void onGuiClosed()
    {
        super.onGuiClosed();
        inventorySlots.onContainerClosed(player);
    }

    boolean isExtended()
    {
        return container.inventory.activeVariations > 16;
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int j, int i)
    {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

        String line = isExtended() ? 
                I18n.format(this.container.inventory.getInventoryName() + ".titleShort") : 
                I18n.format(this.container.inventory.getInventoryName() + ".title");
        fontRendererObj.drawString(line, 88 - fontRendererObj.getStringWidth(line) / 2, 13, 0x404040);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float f, int mx, int my)
    {
        drawDefaultBackground();
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

        int i = width - xSize >> 1;
        int j = height - ySize >> 1;

        String texture = isExtended() ?
                "chisel:textures/chisel-gui-24.png" :
                "chisel:textures/chisel-gui.png";

        GeneralClient.bind(texture);
        drawTexturedModalRect(i, j, 0, 0, xSize, ySize);
    }
}
