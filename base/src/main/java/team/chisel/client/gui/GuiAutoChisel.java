package team.chisel.client.gui;

import java.text.NumberFormat;
import java.util.List;

import javax.annotation.Nonnull;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.energy.CapabilityEnergy;
import team.chisel.Chisel;
import team.chisel.common.config.Configurations;
import team.chisel.common.init.ChiselItems;
import team.chisel.common.inventory.ContainerAutoChisel;
import team.chisel.common.item.ItemChisel.ChiselType;

public class GuiAutoChisel extends ContainerScreen<ContainerAutoChisel> {
    @Nonnull
    private static final ResourceLocation TEXTURE = new ResourceLocation(Chisel.MOD_ID, "textures/autochisel.png");
    
    private static final int PROG_BAR_LENGTH = 50;
    private static final int POWER_BAR_LENGTH = 160;

    private final ContainerAutoChisel container;
    
    @Nonnull
    private final ItemStack fakeChisel = new ItemStack(ChiselItems.IRON_CHISEL.get());
    
    public GuiAutoChisel(ContainerAutoChisel container, PlayerInventory inv, ITextComponent displayName) {
        super(container, inv, displayName);
        this.container = container;
        this.ySize = 200;
    }
    
    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {
        this.renderBackground();
        super.render(mouseX, mouseY, partialTicks);
        this.renderHoveredToolTip(mouseX, mouseY);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        Minecraft.getInstance().getTextureManager().bindTexture(TEXTURE);
        blit(guiLeft, guiTop, 0, 0, xSize, ySize);

        if (container.isActive()) {
            int scaledProg = container.getProgressScaled(PROG_BAR_LENGTH);
            blit(guiLeft + 63, guiTop + 19 + 9, 176, 18, scaledProg + 1, 16);
        }

        if (Configurations.autoChiselPowered) {
            blit(guiLeft + 7, guiTop + 93, 7, 200, 162, 6);
            if (container.hasEnergy()) {
                blit(guiLeft + 8, guiTop + 94, 8, 206, container.getEnergyScaled(POWER_BAR_LENGTH) + 1, 4);
            }
        }
        
        if (!container.getSlot(container.chiselSlot).getHasStack()) {
            drawGhostItem(fakeChisel, guiLeft + 80, guiTop + 28);
        }
        if (!container.getSlot(container.targetSlot).getHasStack()) {
            RenderSystem.color4f(1, 1, 1, 1);
            blit(guiLeft + 80, guiTop + 64, 176, 34, 16, 16);
        }
    }
    
    private void drawGhostItem(@Nonnull ItemStack stack, int x, int y) {
        Minecraft.getInstance().getItemRenderer().renderItemIntoGUI(stack, x, y);
        Minecraft.getInstance().getTextureManager().bindTexture(TEXTURE);
        RenderSystem.color4f(1, 1, 1, 0.5f);
        RenderSystem.disableDepthTest();
        RenderSystem.enableBlend();
        RenderSystem.disableLighting();
        blit(x, y, x - guiLeft, y - guiTop, 16, 16);
        RenderSystem.color4f(1, 1, 1, 1);
        RenderSystem.disableBlend();
        RenderSystem.enableDepthTest();
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        String s = title.getFormattedText();
        this.font.drawString(s, this.xSize / 2 - this.font.getStringWidth(s) / 2, 6, 0x404040);
        this.font.drawString(container.invPlayer.getDisplayName().getFormattedText(), 8, this.ySize - 96 + 2, 0x404040);
        
        if (Configurations.autoChiselPowered) {
            mouseX -= guiLeft;
            mouseY -= guiTop;

            int finalMouseX = mouseX;
            int finalMouseY = mouseY;
            
            if (finalMouseX >= 7 && finalMouseY >= 93 && finalMouseX <= 169 && finalMouseY <= 98) {
                NumberFormat fmt = NumberFormat.getNumberInstance();
                String stored = fmt.format(container.getEnergy());
                String max = fmt.format(container.getMaxEnergy());
                List<String> tt = Lists.newArrayList(
                        I18n.format("chisel.tooltip.power.stored", stored, max),
                        TextFormatting.GRAY + I18n.format("chisel.tooltip.power.pertick", fmt.format(container.getUsagePerTick())));
                renderTooltip(tt, finalMouseX, finalMouseY);
            }
        }
    }
}