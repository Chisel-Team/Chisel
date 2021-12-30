package team.chisel.client.gui;

import java.util.List;

import javax.annotation.ParametersAreNonnullByDefault;

import org.lwjgl.opengl.GL11;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.util.IReorderingProcessor;
import net.minecraft.util.ResourceLocation;
import net.minecraft.network.chat.Component;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslatableComponent;
import net.minecraftforge.fml.client.gui.GuiUtils;
import team.chisel.Chisel;
import team.chisel.api.IChiselItem;
import team.chisel.api.carving.CarvingUtils;
import team.chisel.api.carving.IChiselMode;
import team.chisel.common.inventory.ChiselContainer;
import team.chisel.common.inventory.SlotChiselInput;
import team.chisel.common.item.PacketChiselMode;
import team.chisel.common.util.NBTUtil;

@ParametersAreNonnullByDefault
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
        //if (slot != null && slot.slotNumber < this.getContainer().getInventoryChisel().size - 1) {
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
            if (((IChiselItem) this.getContainer().getChisel().getItem()).supportsMode(player, this.getContainer().getChisel(), mode)) {
                int x = area.getX() + (padding / 2) + ((id % buttonsPerRow) * (20 + padding));
                int y = area.getY() + ((id / buttonsPerRow) * (20 + padding));
                ButtonChiselMode button = new ButtonChiselMode(x, y, mode, b -> {
                    b.active = false;
                    IChiselMode m = ((ButtonChiselMode) b).getMode();
                    NBTUtil.setChiselMode(this.getContainer().getChisel(), m);
                    Chisel.network.sendToServer(new PacketChiselMode(this.getContainer().getChiselSlot(), m));
                    for (Widget other : buttons) {
                        if (other != b && other instanceof ButtonChiselMode) {
                            // TODO see if Button.enabled == Button.active
                            other.active = true;
                        }
                    }
                });
                if (mode == currentMode) {
                    // TODO see if Button.enabled == Button.active
                    button.active = false;
                }
                addButton(button);
                id++;
            }
        }
    }

    protected Rectangle2d getModeButtonArea() {
        int down = 73;
        int padding = 7;
        return new Rectangle2d(guiLeft + padding, guiTop + down + padding, 50, ySize - down - (padding * 2));
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        this.renderHoveredTooltip(matrixStack, mouseX, mouseY);
    }
    
    @Override
    protected void drawGuiContainerForegroundLayer(MatrixStack matrixStack, int j, int i) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

        // TODO fix String
        List<IReorderingProcessor> lines = font.trimStringToWidth(title, 40);
        int y = 60;
        IRenderTypeBuffer.Impl irendertypebuffer$impl = IRenderTypeBuffer.getImpl(Tessellator.getInstance().getBuffer());
        for (IReorderingProcessor s : lines) {
            font.drawEntityText(s, 32 - font.width(s) / 2, y, 0x404040, false, matrixStack.getLast().getMatrix(), irendertypebuffer$impl, false, 0, 0xF000F0);
            y += 10;
        }
        irendertypebuffer$impl.finish();

        drawButtonTooltips(matrixStack, j, i);
//        if (showMode()) {
//            line = I18n.format(this.container.inventory.getInventoryName() + ".mode");
//            fontRendererObj.drawString(line, fontRendererObj.getStringWidth(line) / 2 + 6, 85, 0x404040);
//        }
    }
    
    protected void drawButtonTooltips(MatrixStack matrixStack, int mx, int my) {
        for (Widget button : buttons) {
            if (button.isMouseOver(mx, my) && button instanceof ButtonChiselMode) {
                String unloc = ((ButtonChiselMode)button).getMode().getUnlocName();
                List<ITextComponent> ttLines = Lists.newArrayList(
                        new TranslatableComponent(unloc),
                        new TranslatableComponent(unloc + ".desc").mergeStyle(TextFormatting.GRAY)
                );
                GuiUtils.drawHoveringText(matrixStack, ttLines, mx - guiLeft, my - guiTop, width - guiLeft, height - guiTop, -1, font);
            }
        }
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(MatrixStack matrixStack, float f, int mx, int my) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

        int i = width - xSize >> 1;
        int j = height - ySize >> 1;

        String texture = "chisel:textures/chisel2gui.png";

        Minecraft.getInstance().getTextureManager().bindTexture(new ResourceLocation(texture));
        blit(matrixStack, i, j, 0, 0, xSize, ySize);

        int x = (width - xSize) / 2;
        int y = (height - ySize) / 2;

        Slot main = (Slot) this.getContainer().inventorySlots.get(this.getContainer().getInventoryChisel().size);
        if (main.getStack().isEmpty()) {
            drawSlotOverlay(matrixStack, this, x + 14, y + 14, main, 0, ySize, 60);
        }
    }

    @Override
    protected boolean isSlotSelected(Slot slotIn, double mouseX, double mouseY) {
    	if (slotIn == container.getInputSlot()) {
    		return isPointInRegion(slotIn.xPos - 8, slotIn.yPos - 8, 32, 32, mouseX, mouseY);
    	}
    	return super.isSlotSelected(slotIn, mouseX, mouseY);
    }

    @Override
    protected void fillGradient(MatrixStack matrixStack, int x1, int y1, int x2, int y2, int colorFrom, int colorTo) {
    	if (x1 == container.getInputSlot().xPos && y1 == container.getInputSlot().yPos) {
    		super.fillGradient(matrixStack, x1 - 8, y1 - 8, x2 + 8, y2 + 8, colorFrom, colorTo);
    	} else {
    		super.fillGradient(matrixStack, x1, y1, x2, y2, colorFrom, colorTo);
    	}
    }

    @Override
	public void moveItems(MatrixStack matrixStack, Slot slot) {
        if (slot instanceof SlotChiselInput) {
        	matrixStack.push();
        	matrixStack.scale(2, 2, 1);
        	// Item rendering doesn't use the matrix stack yet
        	RenderSystem.pushMatrix();
        	RenderSystem.scalef(2, 2, 1);
        	slot.xPos -= 16;
            slot.yPos -= 16;
            super.moveItems(matrixStack, slot);
            slot.xPos += 16;
            slot.yPos += 16;
            matrixStack.pop();
            RenderSystem.popMatrix();
        } else {
            super.moveItems(matrixStack, slot);
        }
    }

    public static void drawSlotOverlay(MatrixStack matrixStack, ContainerScreen<?> gui, int x, int y, Slot slot, int u, int v, int padding) {
        padding /= 2;
        gui.blit(matrixStack, x + (slot.xPos - padding), y + (slot.yPos - padding), u, v, 18 + padding, 18 + padding);
    }
}