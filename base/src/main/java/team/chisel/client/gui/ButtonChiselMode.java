package team.chisel.client.gui;

import javax.annotation.Nonnull;

import com.mojang.blaze3d.matrix.MatrixStack;

import lombok.Getter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.text.StringTextComponent;
import team.chisel.api.carving.IChiselMode;
import team.chisel.common.util.Point2i;

public class ButtonChiselMode extends Button {
    
    @Getter
    @Nonnull
    private final IChiselMode mode;
    
    public ButtonChiselMode(int x, int y, @Nonnull IChiselMode mode, IPressable action) {
        super(x, y, 20, 20, new StringTextComponent(""), action);
        this.mode = mode;
    }
    
    @Override
    protected void renderBg(MatrixStack matrixStack, Minecraft mc, int mouseX, int mouseY) {
        super.renderBg(matrixStack, mc, mouseX, mouseY);
        mc.getTextureManager().bindTexture(mode.getSpriteSheet());
        Point2i uv = mode.getSpritePos();
        blit(matrixStack, x + 4, y + 4, 12, 12, uv.getX(), uv.getY(), 24, 24, 256, 256);
    }
}
