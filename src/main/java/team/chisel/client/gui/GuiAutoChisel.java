package team.chisel.client.gui;

import java.text.NumberFormat;
import java.util.List;

import com.google.common.collect.Lists;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import team.chisel.Chisel;
import team.chisel.common.inventory.ContainerAutoChisel;

public class GuiAutoChisel extends GuiContainer {
    
    private static final ResourceLocation TEXTURE = new ResourceLocation(Chisel.MOD_ID, "textures/autoChisel.png");
    
    private static final int PROG_BAR_LENGTH = 50;
    private static final int POWER_BAR_LENGTH = 160;

    private final ContainerAutoChisel container;
    
    public GuiAutoChisel(ContainerAutoChisel container) {
        super(container);
        this.container = container;
        this.ySize = 200;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        mc.getTextureManager().bindTexture(TEXTURE);
        drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);

        if (container.te.getProgress() > 0) {
            int scaledProg = (int) (((float) container.te.getProgress() / container.te.getMaxProgress()) * PROG_BAR_LENGTH);
            drawTexturedModalRect(guiLeft + 63, guiTop + 19 + 9, 176, 18, scaledProg + 1, 17);
        }

        IEnergyStorage energy = container.te.getCapability(CapabilityEnergy.ENERGY, null);
        if (energy != null) {
            drawTexturedModalRect(guiLeft + 7, guiTop + 93, 7, 200, 162, 6);
            if (energy.getEnergyStored() > 0) {
                drawTexturedModalRect(guiLeft + 8, guiTop + 94, 8, 206, (int) (((float) energy.getEnergyStored() / energy.getMaxEnergyStored()) * POWER_BAR_LENGTH) + 1, 4);
            }
        }
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        String s = container.te.getDisplayName().getUnformattedText();
        this.fontRendererObj.drawString(s, this.xSize / 2 - this.fontRendererObj.getStringWidth(s) / 2, 6, 0x404040);
        this.fontRendererObj.drawString(container.invPlayer.getDisplayName().getUnformattedText(), 8, this.ySize - 96 + 2, 0x404040);
        
        mouseX -= guiLeft;
        mouseY -= guiTop;
        
        IEnergyStorage energy = container.te.getCapability(CapabilityEnergy.ENERGY, null);
        if (energy != null && mouseX >= 7 && mouseY >= 93 && mouseX <= 169 && mouseY <= 98) {
            NumberFormat fmt = NumberFormat.getNumberInstance();
            String stored = fmt.format(energy.getEnergyStored());
            String max = fmt.format(energy.getMaxEnergyStored());
            List<String> tt = Lists.newArrayList(
                    I18n.format("chisel.tooltip.power.stored", stored, max), 
                    TextFormatting.GRAY + I18n.format("chisel.tooltip.power.pertick", fmt.format(container.te.getUsagePerTick())));
            drawHoveringText(tt, mouseX, mouseY);
        }
    }
}
