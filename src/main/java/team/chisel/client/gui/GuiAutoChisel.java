package team.chisel.client.gui;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.util.ResourceLocation;
import team.chisel.Chisel;
import team.chisel.common.inventory.ContainerAutoChisel;

public class GuiAutoChisel extends GuiContainer {
    
    private static final ResourceLocation TEXTURE = new ResourceLocation(Chisel.MOD_ID, "textures/autoChisel.png");
    
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
    }
}
