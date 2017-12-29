package team.chisel.client.gui;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.util.ResourceLocation;
import team.chisel.Chisel;
import team.chisel.common.inventory.ContainerAutoChisel;

public class GuiAutoChisel extends GuiContainer {
    
    private static final ResourceLocation TEXTURE = new ResourceLocation(Chisel.MOD_ID, "textures/autoChisel.png");
    
    private static final int PROG_BAR_LENGTH = 50;

    private final ContainerAutoChisel container;
    
    public GuiAutoChisel(ContainerAutoChisel container) {
        super(container);
        this.container = container;
        this.ySize = 191;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        mc.getTextureManager().bindTexture(TEXTURE);
        drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);

        if (container.te.getProgress() > 0) {
            int scaledProg = (int) (((float) container.te.getProgress() / container.te.getMaxProgress()) * PROG_BAR_LENGTH);
            drawTexturedModalRect(guiLeft + 63, guiTop + 19 + 9, 176, 18, scaledProg + 1, 17);
        }
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        String s = container.te.getDisplayName().getUnformattedText();
        this.fontRendererObj.drawString(s, this.xSize / 2 - this.fontRendererObj.getStringWidth(s) / 2, 6, 0x404040);
        this.fontRendererObj.drawString(container.invPlayer.getDisplayName().getUnformattedText(), 8, this.ySize - 96 + 2, 0x404040);
    }
}
