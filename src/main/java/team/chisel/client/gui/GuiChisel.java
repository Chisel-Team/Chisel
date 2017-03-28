package team.chisel.client.gui;

import java.util.List;

import javax.annotation.ParametersAreNonnullByDefault;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import team.chisel.common.inventory.ContainerChisel;
import team.chisel.common.inventory.InventoryChiselSelection;
import team.chisel.common.inventory.SlotChiselInput;

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

//        if (showMode()) {
//            int x = this.width / 2 - 120;
//            int y = this.height / 2 - 6;
//            buttonList.add(new GuiButton(0, x, y, 53, 20, ""));
//            setButtonText();
//        }
    }

//    private void setButtonText() {
//        ((GuiButton) buttonList.get(0)).displayString = I18n.format(container.inventory.getInventoryName() + ".mode." + currentMode.name().toLowerCase());
//    }

//    private boolean showMode() {
//        if (container.chisel != null && container.chisel.getItem() instanceof IChiselItem) {
//            return ((IChiselItem) container.chisel.getItem()).hasModes(container.chisel);
//        }
//        return false;
//    }

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

//    @Override
//    protected void actionPerformed(GuiButton button) {
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
//        super.actionPerformed(button);
//    }
    
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