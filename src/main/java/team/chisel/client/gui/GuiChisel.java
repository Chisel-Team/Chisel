package team.chisel.client.gui;

import java.util.List;

import javax.annotation.ParametersAreNonnullByDefault;

import org.lwjgl.opengl.GL11;

import com.google.common.collect.Lists;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.client.renderer.Rectangle2d;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.client.config.GuiUtils;
import team.chisel.Chisel;
import team.chisel.api.IChiselItem;
import team.chisel.api.carving.CarvingUtils;
import team.chisel.api.carving.IChiselMode;
import team.chisel.common.inventory.ChiselContainer;
import team.chisel.common.inventory.SlotChiselInput;
import team.chisel.common.item.PacketChiselMode;
import team.chisel.common.util.NBTUtil;

@ParametersAreNonnullByDefault
public class GuiChisel<T extends ChiselContainer> extends ContainerScreen<T> {

    public PlayerEntity player;

    public GuiChisel(T container, PlayerInventory iinventory, ITextComponent displayName) {
        super(container, iinventory, displayName);
        player = iinventory.player;
        xSize = 252;
        ySize = 202;
    }

    @Override
    public void onClose() {
        super.onClose();
        this.getContainer().onContainerClosed(player);
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
        Rectangle2d area = getModeButtonArea();
        int buttonsPerRow = area.getWidth() / 20;
        int padding = (area.getWidth() - (buttonsPerRow * 20)) / buttonsPerRow;
        IChiselMode currentMode = NBTUtil.getChiselMode(this.getContainer().getChisel());
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
    public void render(int mouseX, int mouseY, float partialTicks) {
        this.renderBackground();
        super.render(mouseX, mouseY, partialTicks);
        this.renderHoveredToolTip(mouseX, mouseY);
    }
    
    @Override
    protected void drawGuiContainerForegroundLayer(int j, int i) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

        // TODO fix String
        String line = title.getFormattedText();
        List<String> lines = font.listFormattedStringToWidth(line, 40);
        int y = 60;
        for (String s : lines) {
            font.drawString(s, 32 - font.getStringWidth(s) / 2, y, 0x404040);
            y += 10;
        }

        drawButtonTooltips(j, i);
//        if (showMode()) {
//            line = I18n.format(this.container.inventory.getInventoryName() + ".mode");
//            fontRendererObj.drawString(line, fontRendererObj.getStringWidth(line) / 2 + 6, 85, 0x404040);
//        }
    }
    
    protected void drawButtonTooltips(int mx, int my) {
        for (Widget button : buttons) {
            if (button.isMouseOver(mx, my) && button instanceof ButtonChiselMode) {
                String unloc = ((ButtonChiselMode)button).getMode().getUnlocName();
                List<String> ttLines = Lists.newArrayList(
                        I18n.format(unloc),
                        TextFormatting.GRAY + I18n.format(unloc + ".desc")
                );
                GuiUtils.drawHoveringText(ttLines, mx - guiLeft, my - guiTop, width - guiLeft, height - guiTop, -1, font);
            }
        }
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float f, int mx, int my) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

        int i = width - xSize >> 1;
        int j = height - ySize >> 1;

        String texture = "chisel:textures/chisel2gui.png";

        Minecraft.getInstance().getTextureManager().bindTexture(new ResourceLocation(texture));
        blit(i, j, 0, 0, xSize, ySize);

        int x = (width - xSize) / 2;
        int y = (height - ySize) / 2;

        Slot main = (Slot) this.getContainer().inventorySlots.get(this.getContainer().getInventoryChisel().size);
        if (main.getStack().isEmpty()) {
            drawSlotOverlay(this, x + 14, y + 14, main, 0, ySize, 60);
        }
    }

    @Override
    protected void drawSlot(Slot slot) {
        if (slot instanceof SlotChiselInput) {
            GL11.glPushMatrix();
            GL11.glScalef(2, 2, 1);
            slot.xPos -= 16;
            slot.yPos -= 16;
            super.drawSlot(slot);
            slot.xPos += 16;
            slot.yPos += 16;
            GL11.glPopMatrix();
        } else {
            super.drawSlot(slot);
        }
    }

    public static void drawSlotOverlay(ContainerScreen<?> gui, int x, int y, Slot slot, int u, int v, int padding) {
        padding /= 2;
        gui.blit(x + (slot.xPos - padding), y + (slot.yPos - padding), u, v, 18 + padding, 18 + padding);
    }
}