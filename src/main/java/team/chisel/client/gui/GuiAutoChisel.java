package team.chisel.client.gui;

import java.text.NumberFormat;
import java.util.List;

import javax.annotation.Nonnull;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.platform.GlStateManager;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.util.NonNullConsumer;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import team.chisel.Chisel;
import team.chisel.common.config.Configurations;
import team.chisel.common.init.ChiselItems;
import team.chisel.common.inventory.ContainerAutoChisel;
import team.chisel.common.inventory.ContainerChisel;

public class GuiAutoChisel extends ContainerScreen<ContainerAutoChisel> {
    @Nonnull
    private static final ResourceLocation TEXTURE = new ResourceLocation(Chisel.MOD_ID, "textures/autochisel.png");
    
    private static final int PROG_BAR_LENGTH = 50;
    private static final int POWER_BAR_LENGTH = 160;

    private final ContainerAutoChisel container;
    
    @Nonnull
    private final ItemStack fakeChisel = new ItemStack(ChiselItems.CHISEL_IRON.get());
    
    public GuiAutoChisel(ContainerAutoChisel container) {
        // TODO
        super(container, null, new StringTextComponent(""));
        this.container = container;
        this.ySize = 200;
    }
    
//    @Override
//    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
//        this.drawDefaultBackground();
//        super.drawScreen(mouseX, mouseY, partialTicks);
//        this.renderHoveredToolTip(mouseX, mouseY);
//    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        Minecraft.getInstance().getTextureManager().bindTexture(TEXTURE);
        //drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);

        if (container.te.getProgress() > 0) {
            int scaledProg = (int) (((float) container.te.getProgress() / container.te.getMaxProgress()) * PROG_BAR_LENGTH);
            //drawTexturedModalRect(guiLeft + 63, guiTop + 19 + 9, 176, 18, scaledProg + 1, 16);
        }

        if (Configurations.autoChiselPowered) {
            container.te.getCapability(CapabilityEnergy.ENERGY, null).ifPresent(energy1 -> {
                //drawTexturedModalRect(guiLeft + 7, guiTop + 93, 7, 200, 162, 6);
                if (energy1.getEnergyStored() > 0) {
                    //drawTexturedModalRect(guiLeft + 8, guiTop + 94, 8, 206, (int) (((float) energy.getEnergyStored() / energy.getMaxEnergyStored()) * POWER_BAR_LENGTH) + 1, 4);
                }
            });
        }
        
        if (!container.getSlot(container.chiselSlot).getHasStack()) {
            drawGhostItem(fakeChisel, guiLeft + 80, guiTop + 28);
        }
        if (!container.getSlot(container.targetSlot).getHasStack()) {
            GlStateManager.color3f(1, 1, 1);
            //drawTexturedModalRect(guiLeft + 80, guiTop + 64, 176, 34, 16, 16);
        }
    }
    
    private void drawGhostItem(@Nonnull ItemStack stack, int x, int y) {
        Minecraft.getInstance().getItemRenderer().renderItemIntoGUI(stack, x, y);
        Minecraft.getInstance().getTextureManager().bindTexture(TEXTURE);
        //float z = zLevel;
        //zLevel = 101;
        //GlStateManager.color(1, 1, 1, 0.5f);
        //GlStateManager.disableDepth();
        //GlStateManager.enableBlend();
        //GlStateManager.disableLighting();
        //drawTexturedModalRect(x, y, x - guiLeft, y - guiTop, 16, 16);
        //GlStateManager.color(1, 1, 1, 1);
        //GlStateManager.disableBlend();
        //GlStateManager.enableDepth();
        //zLevel = z;
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        String s = container.te.getDisplayName().getUnformattedComponentText();
        this.font.drawString(s, this.xSize / 2 - this.font.getStringWidth(s) / 2, 6, 0x404040);
        this.font.drawString(container.invPlayer.getDisplayName().getUnformattedComponentText(), 8, this.ySize - 96 + 2, 0x404040);
        
        if (Configurations.autoChiselPowered) {
            mouseX -= guiLeft;
            mouseY -= guiTop;

            int finalMouseX = mouseX;
            int finalMouseY = mouseY;
            container.te.getCapability(CapabilityEnergy.ENERGY, null).ifPresent(energy -> {
                if (finalMouseX >= 7 && finalMouseY >= 93 && finalMouseX <= 169 && finalMouseY <= 98) {
                    NumberFormat fmt = NumberFormat.getNumberInstance();
                    String stored = fmt.format(energy.getEnergyStored());
                    String max = fmt.format(energy.getMaxEnergyStored());
                    List<String> tt = Lists.newArrayList(
                            I18n.format("chisel.tooltip.power.stored", stored, max),
                            TextFormatting.GRAY + I18n.format("chisel.tooltip.power.pertick", fmt.format(container.te.getUsagePerTick())));
                    //TODO drawHoveringText(tt, finalMouseX, finalMouseY);
                }
            });
        }
    }
}
