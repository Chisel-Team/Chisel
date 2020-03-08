package team.chisel.client.gui;

import javax.annotation.Nonnull;

import lombok.Getter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.button.Button;
import team.chisel.api.carving.IChiselMode;
import team.chisel.common.util.Point2i;

public class ButtonChiselMode extends Button {

    @Getter
    @Nonnull
    private final IChiselMode mode;
    
    public ButtonChiselMode(int x, int y, @Nonnull IChiselMode mode, IPressable action) {
        super(x, y, 20, 20, "", action);
        this.mode = mode;
    }
    
    @Override
    protected void renderBg(Minecraft mc, int mouseX, int mouseY) {
        super.renderBg(mc, mouseX, mouseY);
        mc.getTextureManager().bindTexture(mode.getSpriteSheet());
        Point2i uv = mode.getSpritePos();
        blit(x + 4, y + 4, uv.getX(), uv.getY(), 24, 24, 12, 12, 256, 256);
    }
}
