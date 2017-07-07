package team.chisel.client.gui;

import java.awt.Rectangle;
import java.io.IOException;
import java.util.List;

import javax.annotation.ParametersAreNonnullByDefault;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import team.chisel.api.IChiselItem;
import team.chisel.api.carving.IChiselMode;
import team.chisel.common.inventory.ContainerChisel;
import team.chisel.common.inventory.InventoryChiselSelection;
import team.chisel.common.inventory.SlotChiselInput;
import team.chisel.common.item.ChiselMode;
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
    public void initGui() {
        super.initGui();

        int id = 0;
        Rectangle area = getModeButtonArea();
        int buttonsPerRow = area.width / 20;
        int padding = (area.width - (buttonsPerRow * 20)) / buttonsPerRow;
        for (IChiselMode mode : ChiselMode.values()) {
            if (((IChiselItem) container.getChisel().getItem()).supportsMode(player, mode)) {
                int x = area.x + (padding / 2) + ((id % buttonsPerRow) * (20 + padding));
                int y = area.y + ((id / buttonsPerRow) * (20 + padding));
                setButtonText(addButton(new ButtonChiselMode(id++, x, y, mode)));
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

    @SuppressWarnings("null")
    @Override
    protected void drawGuiContainerForegroundLayer(int j, int i) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

        String line = I18n.format(this.container.getInventoryChisel().getName() + ".title");
        List<String> lines = fontRendererObj.listFormattedStringToWidth(line, 40);
        int y = 60;
        for (String s : lines) {
            fontRendererObj.drawString(s, 32 - fontRendererObj.getStringWidth(s) / 2, y, 0x404040);
            y += 10;
        }

//        if (showMode()) {
//            line = I18n.format(this.container.inventory.getInventoryName() + ".mode");
//            fontRendererObj.drawString(line, fontRendererObj.getStringWidth(line) / 2 + 6, 85, 0x404040);
//        }
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float f, int mx, int my) {
        drawDefaultBackground();
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
            NBTUtil.setChiselMode(container.getChisel(), ((ButtonChiselMode) button).getMode());
            for (GuiButton other : buttonList) {
                if (other != button && other instanceof ButtonChiselMode) {
                    other.enabled = true;
                }
            }
        }
//        if (button.id == 0) {
//            if (container.chisel != null && container.chisel.getItem() instanceof IAdvancedChisel) {
//                IAdvancedChisel items = (IAdvancedChisel) container.chisel.getItem();
//                currentMode = items.getNextMode(container.chisel, currentMode);
//                PacketHandler.INSTANCE.sendToServer(new MessageChiselMode(currentMode));
//            } else {
//                currentMode = ChiselMode.next(currentMode);
//                PacketHandler.INSTANCE.sendToServer(new MessageChiselMode(currentMode));
//                setButtonText();
//            }
//        }
        super.actionPerformed(button);
    }
    
    @Override
    protected void drawSlot(Slot slot) {
        if (slot instanceof SlotChiselInput) {
            GL11.glPushMatrix();
            GL11.glScalef(2, 2, 1);
            slot.xDisplayPosition -= 16;
            slot.yDisplayPosition -= 16;
            super.drawSlot(slot);
            slot.xDisplayPosition += 16;
            slot.yDisplayPosition += 16;
            GL11.glPopMatrix();
        } else {
            super.drawSlot(slot);
        }
    }

    public static void drawSlotOverlay(GuiContainer gui, int x, int y, Slot slot, int u, int v, int padding) {
        padding /= 2;
        gui.drawTexturedModalRect(x + (slot.xDisplayPosition - padding), y + (slot.yDisplayPosition - padding), u, v, 18 + padding, 18 + padding);
    }
}