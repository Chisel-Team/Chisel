package team.chisel.client.gui;

import java.util.List;

import org.lwjgl.opengl.GL11;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Widget;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import team.chisel.Chisel;
import team.chisel.api.IChiselItem;
import team.chisel.api.carving.CarvingUtils;
import team.chisel.api.carving.IChiselMode;
import team.chisel.common.inventory.ChiselContainer;
import team.chisel.common.inventory.SlotChiselInput;
import team.chisel.common.item.PacketChiselMode;
import team.chisel.common.util.NBTUtil;

public class GuiChisel<T extends ChiselContainer> extends AbstractContainerScreen<T> {

    public Player player;

    public GuiChisel(T container, Inventory iinventory, Component displayName) {
        super(container, iinventory, displayName);
        player = iinventory.player;
        imageWidth = 252;
        imageHeight = 202;
    }

    @Override
    public void removed() {
        super.removed();
        this.getMenu().removed(player);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int mouseButton) {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        
        // if this is a selection slot, no double clicking
        //Slot slot = getSlotAtPosition(mouseX, mouseY);
        //if (slot != null && slot.slotNumber < this.menu.getInventoryChisel().size - 1) {
        //    this.doubleClick = false;
        //}

        return false;
    }


    @Override
    public void init() {
        super.init();

        int id = 0;
        Rect2i area = getModeButtonArea();
        int buttonsPerRow = area.getWidth() / 20;
        int padding = (area.getWidth() - (buttonsPerRow * 20)) / buttonsPerRow;
        IChiselMode currentMode = NBTUtil.getChiselMode(this.getMenu().getChisel());
        for (IChiselMode mode : CarvingUtils.getModeRegistry().getAllModes()) {
            if (((IChiselItem) this.menu.getChisel().getItem()).supportsMode(player, this.menu.getChisel(), mode)) {
                int x = area.getX() + (padding / 2) + ((id % buttonsPerRow) * (20 + padding));
                int y = area.getY() + ((id / buttonsPerRow) * (20 + padding));
                ButtonChiselMode button = new ButtonChiselMode(x, y, mode, b -> {
                    b.active = false;
                    IChiselMode m = ((ButtonChiselMode) b).getMode();
                    NBTUtil.setChiselMode(this.menu.getChisel(), m);
                    Chisel.network.sendToServer(new PacketChiselMode(this.menu.getChiselSlot(), m));
                    for (Widget other : renderables) {
                        if (other != b && other instanceof ButtonChiselMode b2) {
                            // TODO see if Button.enabled == Button.active
                            b2.active = true;
                        }
                    }
                });
                if (mode == currentMode) {
                    // TODO see if Button.enabled == Button.active
                    button.active = false;
                }
                addRenderableWidget(button);
                id++;
            }
        }
    }

    protected Rect2i getModeButtonArea() {
        int down = 73;
        int padding = 7;
        return new Rect2i(getGuiLeft() + padding, getGuiTop() + down + padding, 50, getYSize() - down - (padding * 2));
    }

    @Override
    public void render(PoseStack PoseStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(PoseStack);
        super.render(PoseStack, mouseX, mouseY, partialTicks);
        this.renderTooltip(PoseStack, mouseX, mouseY);
    }
    
    @Override
    protected void renderLabels(PoseStack PoseStack, int j, int i) {
        setSlotScale(PoseStack, false); // Clear slot scaling in case input slot is the last one rendered, this is the very next method called after rendering slots
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

        // TODO fix String
        List<FormattedCharSequence> lines = font.split(title, 40);
        int y = 60;
        MultiBufferSource.BufferSource irendertypebuffer$impl = MultiBufferSource.immediate(Tesselator.getInstance().getBuilder());
        for (FormattedCharSequence s : lines) {
            font.drawInBatch(s, 32 - font.width(s) / 2, y, 0x404040, false, PoseStack.last().pose(), irendertypebuffer$impl, false, 0, 0xF000F0);
            y += 10;
        }

        irendertypebuffer$impl.endBatch();

        drawButtonTooltips(PoseStack, j, i);
    }

    protected void drawButtonTooltips(PoseStack PoseStack, int mx, int my) {
        for (Widget button : renderables) {
            if (button instanceof ButtonChiselMode b && b.isMouseOver(mx, my)) {
                String unloc = ((ButtonChiselMode)button).getMode().getUnlocName();
                List<Component> ttLines = Lists.newArrayList(
                        new TranslatableComponent(unloc),
                        new TranslatableComponent(unloc + ".desc").withStyle(ChatFormatting.GRAY)
                );
                renderComponentTooltip(PoseStack, ttLines, mx - getGuiLeft(), my - getGuiTop());
            }
        }
    }

    @Override
    protected void renderBg(PoseStack PoseStack, float f, int mx, int my) {
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

        int i = width - getXSize() >> 1;
        int j = height - getYSize() >> 1;

        String texture = "chisel:textures/chisel2gui.png";

        RenderSystem.setShaderTexture(0, new ResourceLocation(texture));
        blit(PoseStack, i, j, 0, 0, getXSize(), getYSize());

        int x = (width - getXSize()) / 2;
        int y = (height - getYSize()) / 2;

        Slot main = this.menu.getInputSlot();
        if (main.getItem().isEmpty()) {
            drawSlotOverlay(PoseStack, this, i, j, main, 0, getYSize(), 0);
        }
    }

    @Override
    protected boolean isHovering(Slot slotIn, double mouseX, double mouseY) {
    	if (slotIn == menu.getInputSlot()) {
    	    // If scaling is not active, this is a check for a click action, which must be shifted the opposite way as the slot position is not shifted
    	    if (scaleActive) {
    	        return isHovering(slotIn.x + 8, slotIn.y + 8, 32, 32, mouseX, mouseY);
    	    } else {
    	        return isHovering(slotIn.x - 8, slotIn.y - 8, 32, 32, mouseX, mouseY);
    	    }
    	}
    	return super.isHovering(slotIn, mouseX, mouseY);
    }

    @Override
    protected void fillGradient(PoseStack PoseStack, int x1, int y1, int x2, int y2, int colorFrom, int colorTo) {
		super.fillGradient(PoseStack, x1, y1, x2, y2, colorFrom, colorTo);
    }

    private boolean scaleActive = false;
    private void setSlotScale(PoseStack stack, boolean active) {
        Slot slot = this.menu.getInputSlot();
        if (!scaleActive && active) {
            /*
             * Item rendering does not use the PoseStack, but drawing the highlight does.
             * 
             * This means that if the slot has an item, and we apply both methods, it will result in the highlight being double scaled.
             * However, the model view stack will never be used if an item is not drawn, which breaks the highlight when there is no item in the slot.
             * 
             * So to avoid this we apply either method based on if the slot has an item to render or not.
             */

            if (slot.hasItem()) {
                RenderSystem.getModelViewStack().pushPose();
                RenderSystem.getModelViewStack().scale(2, 2, 1);
            } else {
                stack.pushPose();
                stack.scale(2, 2, 2);
            }
            // Item rendering doesn't use the matrix stack yet
            slot.x -= 16;
            slot.y -= 16;
        }
        if (scaleActive && !active) {
            slot.x += 16;
            slot.y += 16;
            if (slot.hasItem()) {
                RenderSystem.getModelViewStack().popPose();
                RenderSystem.applyModelViewMatrix();
            } else {
                stack.popPose();
            }
        }
        scaleActive = active;
    }

    @Override
	public void renderSlot(PoseStack stack, Slot slot) {
        if (slot == menu.getInputSlot()) {
            setSlotScale(stack, true);
        } else {
            setSlotScale(stack, false);
        }
        super.renderSlot(stack, slot);
    }

    private void drawSlotOverlay(PoseStack PoseStack, AbstractContainerScreen<?> gui, int x, int y, Slot slot, int u, int v, int padding) {
        if (scaleActive) {
            gui.blit(PoseStack, x + (slot.x - 16 - padding), y + (slot.y - 16 - padding), u, v, 48 + padding, 48 + padding);
//        } else {
//            gui.blit(PoseStack, x + (slot.x - padding), y, u, v, 16, 16);
        }
    }
}