package team.chisel.client.gui;

import java.text.NumberFormat;
import java.util.List;

import javax.annotation.Nonnull;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
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
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        this.renderHoveredTooltip(matrixStack, mouseX, mouseY);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(MatrixStack matrixStack, float partialTicks, int mouseX, int mouseY) {
        Minecraft.getInstance().getTextureManager().bindTexture(TEXTURE);
        blit(matrixStack, guiLeft, guiTop, 0, 0, xSize, ySize);

        if (container.isActive()) {
            int scaledProg = container.getProgressScaled(PROG_BAR_LENGTH);
            blit(matrixStack, guiLeft + 63, guiTop + 19 + 9, 176, 18, scaledProg + 1, 16);
        }

        if (Configurations.autoChiselPowered) {
            blit(matrixStack, guiLeft + 7, guiTop + 93, 7, 200, 162, 6);
            if (container.hasEnergy()) {
                blit(matrixStack, guiLeft + 8, guiTop + 94, 8, 206, container.getEnergyScaled(POWER_BAR_LENGTH) + 1, 4);
            }
        }
        
        if (!container.getSlot(container.chiselSlot).getHasStack()) {
            drawGhostItem(matrixStack, fakeChisel, guiLeft + 80, guiTop + 28);
        }
        if (!container.getSlot(container.targetSlot).getHasStack()) {
            RenderSystem.color4f(1, 1, 1, 1);
            blit(matrixStack, guiLeft + 80, guiTop + 64, 176, 34, 16, 16);
        }
    }
    
    private void drawGhostItem(MatrixStack matrixStack, @Nonnull ItemStack stack, int x, int y) {
        Minecraft.getInstance().getItemRenderer().renderItemIntoGUI(stack, x, y);
        Minecraft.getInstance().getTextureManager().bindTexture(TEXTURE);
        RenderSystem.color4f(1, 1, 1, 0.5f);
        RenderSystem.disableDepthTest();
        RenderSystem.enableBlend();
        RenderSystem.disableLighting();
        blit(matrixStack, x, y, x - guiLeft, y - guiTop, 16, 16);
        RenderSystem.color4f(1, 1, 1, 1);
        RenderSystem.disableBlend();
        RenderSystem.enableDepthTest();
    }

    @Override
    protected void drawGuiContainerForegroundLayer(MatrixStack matrixStack, int mouseX, int mouseY) {
        this.font.drawText(matrixStack, title, this.xSize / 2 - this.font.getStringPropertyWidth(title) / 2, 6, 0x404040);
        this.font.drawText(matrixStack, container.invPlayer.getDisplayName(), 8, this.ySize - 96 + 2, 0x404040);
        
        if (Configurations.autoChiselPowered) {
            mouseX -= guiLeft;
            mouseY -= guiTop;

            int finalMouseX = mouseX;
            int finalMouseY = mouseY;
            
            if (finalMouseX >= 7 && finalMouseY >= 93 && finalMouseX <= 169 && finalMouseY <= 98) {
                NumberFormat fmt = NumberFormat.getNumberInstance();
                String stored = fmt.format(container.getEnergy());
                String max = fmt.format(container.getMaxEnergy());
                List<ITextComponent> tt = Lists.newArrayList(
                        new TranslationTextComponent("chisel.tooltip.power.stored", stored, max),
                        new TranslationTextComponent("chisel.tooltip.power.pertick", fmt.format(container.getUsagePerTick())).mergeStyle(TextFormatting.GRAY));
                func_243308_b(matrixStack, tt, finalMouseX, finalMouseY);
            }
        }
    }
}
