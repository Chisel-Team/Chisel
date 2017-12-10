package team.chisel.client.gui;

import javax.annotation.Nonnull;
import javax.vecmath.Point2i;

import lombok.Getter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import team.chisel.api.carving.IChiselMode;

public class ButtonChiselMode extends GuiButton {

    @Getter
    @Nonnull
    private final IChiselMode mode;
    
    public ButtonChiselMode(int buttonId, int x, int y, @Nonnull IChiselMode mode) {
        super(buttonId, x, y, 20, 20, "");
        this.mode = mode;
    }
    
    @Override
    public void drawButton(@Nonnull Minecraft mc, int mouseX, int mouseY, float partialTicks) {
        super.drawButton(mc, mouseX, mouseY, partialTicks);
        mc.getTextureManager().bindTexture(mode.getSpriteSheet());
        Point2i uv = mode.getSpritePos();
        drawScaledCustomSizeModalRect(x + 4, y + 4, uv.x, uv.y, 24, 24, 12, 12, 256, 256);
    }
    
    @Override
    public boolean mousePressed(@Nonnull Minecraft mc, int mouseX, int mouseY) {
        return super.mousePressed(mc, mouseX, mouseY);
    }
}
