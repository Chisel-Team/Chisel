package team.chisel.client.gui;

import java.io.IOException;
import java.util.List;

import javax.annotation.ParametersAreNonnullByDefault;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.Rectangle;

import com.google.common.collect.Lists;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.client.config.GuiUtils;
import team.chisel.Chisel;
import team.chisel.api.IChiselItem;
import team.chisel.api.carving.CarvingUtils;
import team.chisel.api.carving.IChiselMode;
import team.chisel.common.inventory.ContainerChisel;
import team.chisel.common.inventory.InventoryChiselSelection;
import team.chisel.common.inventory.SlotChiselInput;
import team.chisel.common.item.ChiselMode;
import team.chisel.common.item.PacketChiselMode;
import team.chisel.common.util.NBTUtil;

@ParametersAreNonnullByDefault
public class GuiChisel extends GuiContainer {

    public EntityPlayer player;
    public ContainerChisel container;

    public GuiChisel(InventoryPlayer iinventory, InventoryChiselSelection menu, EnumHand hand) {
        super(new ContainerChisel(iinventory, menu, hand));
        player = iinventory.player;
        xSize = 252;
        ySize = 202;

        container = (ContainerChisel) inventorySlots;
    }

    @Override
    public void onGuiClosed() {
        super.onGuiClosed();
        inventorySlots.onContainerClosed(player);
    }
    
    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        
        // if this is a selection slot, no double clicking
        Slot slot = getSlotAtPosition(mouseX, mouseY); 
        if (slot != null && slot.slotNumber < container.getInventoryChisel().size - 1) {
            this.doubleClick = false;
        }
    }

    @Override
    public void initGui() {
        super.initGui();

        int id = 0;
        Rectangle area = getModeButtonArea();
        int buttonsPerRow = area.getWidth() / 20;
        int padding = (area.getWidth() - (buttonsPerRow * 20)) / buttonsPerRow;
        IChiselMode currentMode = NBTUtil.getChiselMode(container.getChisel());
        for (IChiselMode mode : CarvingUtils.getModeRegistry().getAllModes()) {
            if (((IChiselItem) container.getChisel().getItem()).supportsMode(player, mode)) {
                int x = area.getX() + (padding / 2) + ((id % buttonsPerRow) * (20 + padding));
                int y = area.getY() + ((id / buttonsPerRow) * (20 + padding));
                ButtonChiselMode button = new ButtonChiselMode(id++, x, y, mode);
                if (mode == currentMode) {
                    button.enabled = false;
                }
                setButtonText(addButton(button));
            }
        }
    }
    
    protected Rectangle getModeButtonArea() {
        int down = 67;
        int padding = 7;
        return new Rectangle(guiLeft + padding, guiTop + down + padding, 50, ySize - down - (padding * 2));
    }

    private void setButtonText(ButtonChiselMode button) {
//        button.displayString = I18n.format(container.getInventoryChisel().getName() + ".mode." + button.getMode().name().toLowerCase());
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
        this.renderHoveredToolTip(mouseX, mouseY);
    }
    
    @SuppressWarnings("null")
    @Override
    protected void drawGuiContainerForegroundLayer(int j, int i) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

        String line = I18n.format(this.container.getInventoryChisel().getName() + ".title");
        List<String> lines = fontRenderer.listFormattedStringToWidth(line, 40);
        int y = 60;
        for (String s : lines) {
            fontRenderer.drawString(s, 32 - fontRenderer.getStringWidth(s) / 2, y, 0x404040);
            y += 10;
        }

        drawButtonTooltips(j, i);
//        if (showMode()) {
//            line = I18n.format(this.container.inventory.getInventoryName() + ".mode");
//            fontRendererObj.drawString(line, fontRendererObj.getStringWidth(line) / 2 + 6, 85, 0x404040);
//        }
    }
    
    protected void drawButtonTooltips(int mx, int my) {
        for (GuiButton button : buttonList) {
            if (button.isMouseOver() && button instanceof ButtonChiselMode) {
                String unloc = ((ButtonChiselMode)button).getMode().getUnlocName();
                List<String> ttLines = Lists.newArrayList(
                        I18n.format(unloc + ".name"),
                        TextFormatting.GRAY + I18n.format(unloc + ".desc")
                );
                GuiUtils.drawHoveringText(ttLines, mx - guiLeft, my - guiTop, width - guiLeft, height - guiTop, -1, fontRenderer);
            }
        }
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float f, int mx, int my) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

        int i = width - xSize >> 1;
        int j = height - ySize >> 1;

        String texture = "chisel:textures/chisel2Gui.png";

        Minecraft.getMinecraft().getTextureManager().bindTexture(new ResourceLocation(texture));
        drawTexturedModalRect(i, j, 0, 0, xSize, ySize);

        int x = (width - xSize) / 2;
        int y = (height - ySize) / 2;

        Slot main = (Slot) container.inventorySlots.get(container.getInventoryChisel().size);
        if (main.getStack() == null) {
            drawSlotOverlay(this, x + 14, y + 14, main, 0, ySize, 60);
        }
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        if (button instanceof ButtonChiselMode) {
            button.enabled = false;
            IChiselMode mode = ((ButtonChiselMode) button).getMode();
            NBTUtil.setChiselMode(container.getChisel(), mode);
            Chisel.network.sendToServer(new PacketChiselMode(container.getChiselSlot(), mode));
            for (GuiButton other : buttonList) {
                if (other != button && other instanceof ButtonChiselMode) {
                    other.enabled = true;
                }
            }
        }

        super.actionPerformed(button);
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

    public static void drawSlotOverlay(GuiContainer gui, int x, int y, Slot slot, int u, int v, int padding) {
        padding /= 2;
        gui.drawTexturedModalRect(x + (slot.xPos - padding), y + (slot.yPos - padding), u, v, 18 + padding, 18 + padding);
    }
}