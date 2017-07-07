package team.chisel.client.gui;

import lombok.Getter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import team.chisel.api.carving.IChiselMode;

public class ButtonChiselMode extends GuiButton {

    @Getter
    private final IChiselMode mode;
    
    public ButtonChiselMode(int buttonId, int x, int y, IChiselMode mode) {
        super(buttonId, x, y, 20, 20, mode.name().substring(0, 1));
        this.mode = mode;
    }
    
    @Override
    public void drawButton(Minecraft mc, int mouseX, int mouseY) {
        super.drawButton(mc, mouseX, mouseY);
    }
    
    @Override
    public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
        return super.mousePressed(mc, mouseX, mouseY);
    }
}
