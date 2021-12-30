package team.chisel.client.gui;

import java.text.NumberFormat;
import java.util.List;

import javax.annotation.Nonnull;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.chat.Component;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraftforge.energy.CapabilityEnergy;
import team.chisel.Chisel;
import team.chisel.common.config.Configurations;
import team.chisel.common.init.ChiselItems;
import team.chisel.common.inventory.ContainerAutoChisel;
import team.chisel.common.item.ItemChisel.ChiselType;

public class GuiAutoChisel extends AbstractContainerScreen<ContainerAutoChisel> {
    @Nonnull
    private static final ResourceLocation TEXTURE = new ResourceLocation(Chisel.MOD_ID, "textures/autochisel.png");
    
    private static final int PROG_BAR_LENGTH = 50;
    private static final int POWER_BAR_LENGTH = 160;

    private final ContainerAutoChisel container;
    
    @Nonnull
    private final ItemStack fakeChisel = new ItemStack(ChiselItems.IRON_CHISEL.get());
    
    public GuiAutoChisel(ContainerAutoChisel container, Inventory inv, Component displayName) {
        super(container, inv, displayName);
        this.container = container;
        this.imageHeight = 200;
    }
    
    @Override
    public void render(PoseStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        this.renderTooltip(matrixStack, mouseX, mouseY);
    }

    @Override
    protected void renderBg(PoseStack matrixStack, float partialTicks, int mouseX, int mouseY) {
        Minecraft.getInstance().getTextureManager().bind(TEXTURE);
        blit(matrixStack, leftPos, topPos, 0, 0, imageWidth, imageHeight);

        if (container.isActive()) {
            int scaledProg = container.getProgressScaled(PROG_BAR_LENGTH);
            blit(matrixStack, leftPos + 63, topPos + 19 + 9, 176, 18, scaledProg + 1, 16);
        }

        if (Configurations.autoChiselPowered) {
            blit(matrixStack, leftPos + 7, topPos + 93, 7, 200, 162, 6);
            if (container.hasEnergy()) {
                blit(matrixStack, leftPos + 8, topPos + 94, 8, 206, container.getEnergyScaled(POWER_BAR_LENGTH) + 1, 4);
            }
        }
        
        if (!container.getSlot(container.chiselSlot).hasItem()) {
            drawGhostItem(matrixStack, fakeChisel, leftPos + 80, topPos + 28);
        }
        if (!container.getSlot(container.targetSlot).hasItem()) {
            RenderSystem.color4f(1, 1, 1, 1);
            blit(matrixStack, leftPos + 80, topPos + 64, 176, 34, 16, 16);
        }
    }
    
    private void drawGhostItem(PoseStack matrixStack, @Nonnull ItemStack stack, int x, int y) {
        Minecraft.getInstance().getItemRenderer().renderGuiItem(stack, x, y);
        Minecraft.getInstance().getTextureManager().bind(TEXTURE);
        RenderSystem.color4f(1, 1, 1, 0.5f);
        RenderSystem.disableDepthTest();
        RenderSystem.enableBlend();
        RenderSystem.disableLighting();
        blit(matrixStack, x, y, x - leftPos, y - topPos, 16, 16);
        RenderSystem.color4f(1, 1, 1, 1);
        RenderSystem.disableBlend();
        RenderSystem.enableDepthTest();
    }

    @Override
    protected void renderLabels(PoseStack matrixStack, int mouseX, int mouseY) {
        this.font.draw(matrixStack, title, this.imageWidth / 2 - this.font.width(title) / 2, 6, 0x404040);
        this.font.draw(matrixStack, container.invPlayer.getDisplayName(), 8, this.imageHeight - 96 + 2, 0x404040);
        
        if (Configurations.autoChiselPowered) {
            mouseX -= leftPos;
            mouseY -= topPos;

            int finalMouseX = mouseX;
            int finalMouseY = mouseY;
            
            if (finalMouseX >= 7 && finalMouseY >= 93 && finalMouseX <= 169 && finalMouseY <= 98) {
                NumberFormat fmt = NumberFormat.getNumberInstance();
                String stored = fmt.format(container.getEnergy());
                String max = fmt.format(container.getMaxEnergy());
                List<Component> tt = Lists.newArrayList(
                        new TranslatableComponent("chisel.tooltip.power.stored", stored, max),
                        new TranslatableComponent("chisel.tooltip.power.pertick", fmt.format(container.getUsagePerTick())).withStyle(ChatFormatting.GRAY));
                renderComponentTooltip(matrixStack, tt, finalMouseX, finalMouseY);
            }
        }
    }
}
