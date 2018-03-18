package team.chisel.client.gui;

import java.text.NumberFormat;
import java.util.List;

import javax.annotation.Nonnull;

import com.google.common.collect.Lists;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import team.chisel.Chisel;
import team.chisel.common.init.ChiselItems;
import team.chisel.common.inventory.ContainerAutoChisel;

public class GuiAutoChisel extends GuiContainer {
    
    @Nonnull
    private static final ResourceLocation TEXTURE = new ResourceLocation(Chisel.MOD_ID, "textures/autochisel.png");
    
    private static final int PROG_BAR_LENGTH = 50;
    private static final int POWER_BAR_LENGTH = 160;

    private final ContainerAutoChisel container;
    
    @Nonnull
    private final ItemStack fakeChisel = new ItemStack(ChiselItems.chisel_iron);
    
    public GuiAutoChisel(ContainerAutoChisel container) {
        super(container);
        this.container = container;
        this.ySize = 200;
    }
    
    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
        this.renderHoveredToolTip(mouseX, mouseY);
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
        
        if (!container.getSlot(container.chiselSlot).getHasStack()) {
            drawGhostItem(fakeChisel, guiLeft + 80, guiTop + 28);
        }
        if (!container.getSlot(container.targetSlot).getHasStack()) {
            drawTexturedModalRect(guiLeft + 80, guiTop + 64, 176, 34, 16, 16);
        }
    }
    
    private void drawGhostItem(@Nonnull ItemStack stack, int x, int y) {
        mc.getRenderItem().renderItemIntoGUI(stack, x, y);
        mc.getTextureManager().bindTexture(TEXTURE);
        float z = zLevel;
        zLevel = 101;
        GlStateManager.color(1, 1, 1, 0.5f);
        drawTexturedModalRect(x, y, x - guiLeft, y - guiTop, 16, 16);
        GlStateManager.color(1, 1, 1, 1);
        zLevel = z;
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        String s = container.te.getDisplayName().getUnformattedText();
        this.fontRenderer.drawString(s, this.xSize / 2 - this.fontRenderer.getStringWidth(s) / 2, 6, 0x404040);
        this.fontRenderer.drawString(container.invPlayer.getDisplayName().getUnformattedText(), 8, this.ySize - 96 + 2, 0x404040);
        
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
