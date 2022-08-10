package team.chisel.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import lombok.Getter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.TextComponent;
import team.chisel.api.carving.IChiselMode;
import team.chisel.common.util.Point2i;

import javax.annotation.Nonnull;

public class ButtonChiselMode extends Button {

    @Getter
    @Nonnull
    private final IChiselMode mode;

    public ButtonChiselMode(int x, int y, @Nonnull IChiselMode mode, OnPress action) {
        super(x, y, 20, 20, new TextComponent(""), action);
        this.mode = mode;
    }

    @Override
    protected void renderBg(PoseStack PoseStack, Minecraft mc, int mouseX, int mouseY) {
        super.renderBg(PoseStack, mc, mouseX, mouseY);
        RenderSystem.setShaderTexture(0, mode.getSpriteSheet());
        Point2i uv = mode.getSpritePos();
        blit(PoseStack, x + 4, y + 4, 12, 12, uv.getX(), uv.getY(), 24, 24, 256, 256);
    }
}
