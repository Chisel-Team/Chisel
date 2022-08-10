package team.chisel.client.gui;

import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;

import org.apache.commons.lang3.ArrayUtils;
import org.lwjgl.opengl.GL11;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.math.Matrix4f;
import com.mojang.math.Vector3f;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ColorResolver;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LightChunkGetter;
import net.minecraft.world.level.lighting.LevelLightEngine;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.client.model.data.EmptyModelData;
import team.chisel.Chisel;
import team.chisel.api.IChiselItem;
import team.chisel.client.util.ChiselLangKeys;
import team.chisel.common.inventory.ContainerChiselHitech;
import team.chisel.common.util.NBTUtil;

public class GuiHitechChisel extends GuiChisel<ContainerChiselHitech> {

    private class PreviewModeButton extends Button {

        @Getter
        private PreviewType type;
        
        public PreviewModeButton(int x, int y, int widthIn, int heightIn) {
            super(x, y, widthIn, heightIn, new TextComponent(""), b -> {});
            setType(PreviewType.values()[0]);
        }

        @Override
        public boolean mouseClicked(double mouseX, double mouseY, int i) {
            if (super.mouseClicked(mouseX, mouseY, i)) {
                setType(PreviewType.values()[(type.ordinal() + 1) % PreviewType.values().length]);
                updateChiselData();
                return true;
            }
            return false;
        }
        
        private final void setType(PreviewType type) {
            this.type = type;
            this.setMessage(new TextComponent("< ").append(type.getLocalizedName()).append(" >"));
            GuiHitechChisel.this.fakeworld = new FakeBlockAccess(GuiHitechChisel.this); // Invalidate region cache data
        }
    }
    
    private class RotateButton extends Button {

        @Getter
        @Accessors(fluent = true)
        private boolean rotate = true;
        
        public RotateButton(int x, int y) {
            super(x, y, 16, 16, new TextComponent(""), b -> {});
        }

        @Override
        public void render(PoseStack PoseStack, int mouseX, int mouseY, float partialTicks) {
            this.isHovered = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;

            GuiHitechChisel.this.getMinecraft().getTextureManager().bindForSetup(TEXTURE);
            float a = isMouseOver(mouseX, mouseY) ? 1 : 0.2f;
            int u = rotate ? 0 : 16;
            int v = 238;

            RenderSystem.setShaderColor(1, 1, 1, a);
            RenderSystem.enableBlend();
            RenderSystem.enableDepthTest();
            this.setBlitOffset(1000);
            blit(PoseStack, this.x, this.y, u, v, 16, 16);
            this.setBlitOffset(0);
            RenderSystem.setShaderColor(1, 1, 1, 1);
        }

        @Override
        public boolean mouseClicked(double mouseX, double mouseY, int i) {
            if (super.mouseClicked(mouseX, mouseY, i)) {
                rotate = !rotate;
                updateChiselData();
                return true;
            }
            return false;
        }
    }
    
    @RequiredArgsConstructor
    private static class FakeBlockAccess implements BlockAndTintGetter {
        
        private final GuiHitechChisel gui;
        
        @Setter
        private BlockState state = Blocks.AIR.defaultBlockState();
        
        private final LevelLightEngine light = new LevelLightEngine(new LightChunkGetter() {
            
            @Override
            public BlockGetter getLevel() {
                return FakeBlockAccess.this;
            }
            
            @Override
            @Nullable
            public BlockGetter getChunkForLighting(int p_217202_1_, int p_217202_2_) {
                return FakeBlockAccess.this;
            }
        }, true, true);

        //@Override
        //public int getCombinedLight(BlockPos pos, int lightValue) {
        //    return 0xF000F0;
        //}

        @Override
        public BlockState getBlockState(BlockPos pos) {
            return gui.buttonPreview.getType().getPositions().contains(pos) ? state : Blocks.AIR.defaultBlockState();
        }

        @Override
        public FluidState getFluidState(BlockPos pos) {
            return Fluids.EMPTY.defaultFluidState();
        }

		@Override
		public float getShade(Direction p_230487_1_, boolean p_230487_2_) {
			return Minecraft.getInstance().level.getShade(p_230487_1_, p_230487_2_);
		}

        @Override
        @Nullable
        public BlockEntity getBlockEntity(BlockPos pPos) {
            return null;
        }

        @Override
        public int getHeight() {
            return 32;
        }

        @Override
        public int getMinBuildHeight() {
            return 0;
        }

        @Override
        public LevelLightEngine getLightEngine() {
            return light;
        }

        @Override
        public int getBlockTint(BlockPos pBlockPos, ColorResolver pColorResolver) {
            return 0;
        }
    }
    
    private static final Rect2i panel = new Rect2i(8, 14, 74, 74);
    
    private static final ResourceLocation TEXTURE = new ResourceLocation("chisel", "textures/chiselguihitech.png");
    
    private final ContainerChiselHitech containerHitech;
    
    private FakeBlockAccess fakeworld = new FakeBlockAccess(this);
    
    private boolean panelClicked;
    private int clickButton;
    private long lastDragTime;
    private int clickX, clickY;
    private float initRotX, initRotY, initZoom;
    private float prevRotX, prevRotY;
    private double momentumX, momentumY;
    private float momentumDampening = 0.98f;
    private float rotX = 165, rotY, zoom = 1;
    
    private int scrollAcc;
    
    private @Nullable PreviewModeButton buttonPreview;
    private @Nullable Button buttonChisel;
    private @Nullable RotateButton buttonRotate;
    
    private @Nullable BlockState erroredState;
    
    public GuiHitechChisel(ContainerChiselHitech container, Inventory playerInventory, Component displayName) {
        super(container, playerInventory, displayName);
        containerHitech = container;
        imageWidth = 256;
        imageHeight = 220;
    }
    
    @Override
    public void init() {
        super.init();
        int x = getGuiLeft() + panel.getX() - 1;
        int y = getGuiTop() + panel.getY() + panel.getHeight() + 3;
        int w = 76, h = 20;

        boolean firstInit = buttonPreview == null;

        addRenderableWidget(buttonPreview = new PreviewModeButton(x, y, w, h));

        addRenderableWidget(buttonChisel = new Button(x, y += h + 2, w, h, ChiselLangKeys.BUTTON_CHISEL.getComponent(), b -> {
            Slot target = containerHitech.getTarget();
            Slot selected = containerHitech.getSelection();
            if (target != null && target.hasItem() && selected != null && selected.hasItem()) {
                if (ItemStack.isSame(target.getItem(), selected.getItem())) {
                    return;
                }
                ItemStack converted = target.getItem().copy();
                converted.setCount(selected.getItem().getCount());
                int[] slots = new int[] { selected.getSlotIndex() };
                if (hasShiftDown()) {
                    slots = ArrayUtils.addAll(slots, containerHitech.getSelectionDuplicates().stream().mapToInt(Slot::getSlotIndex).toArray());
                }

                Chisel.network.sendToServer(new PacketChiselButton(slots));

                PacketChiselButton.chiselAll(player, slots);

                if (!hasShiftDown()) {
                    List<Slot> dupes = containerHitech.getSelectionDuplicates();
                    Slot next = selected;
                    for (Slot s : dupes) {
                        if (s.index > selected.index) {
                            next = s;
                            break;
                        }
                    }
                    if (next == selected && dupes.size() > 0) {
                        next = dupes.get(0);
                    }
                    containerHitech.setSelection(next);
                } else {
                    containerHitech.setSelection(selected); // Force duplicate recalc
                }
            }
        }));
        addRenderableWidget(buttonRotate = new RotateButton(getGuiLeft() + panel.getX() + panel.getWidth() - 16, getGuiTop() + panel.getY() + panel.getHeight() - 16));

        ItemStack chisel = containerHitech.getChisel();

        if (firstInit) {
            buttonPreview.setType(NBTUtil.getHitechType(chisel));
            buttonRotate.rotate = NBTUtil.getHitechRotate(chisel);
        }

        tick();
    }
    
    @Override
    protected Rect2i getModeButtonArea() {
        int down = 133;
        int padding = 7;
        return new Rect2i(getGuiLeft() + padding, getGuiTop() + down + padding, 76, imageHeight - down - (padding * 2));
    }

    @Override
    protected void containerTick() {
        super.containerTick();

        buttonChisel.active = containerHitech.getSelection() != null && containerHitech.getSelection().hasItem() && containerHitech.getTarget() != null && containerHitech.getTarget().hasItem();

        if (!panelClicked) {
            initRotX = rotX;
            initRotY = rotY;
            initZoom = zoom;
        }

        if (hasShiftDown()) {
            buttonChisel.setMessage(ChiselLangKeys.BUTTON_CHISEL_ALL.getComponent().withStyle(ChatFormatting.YELLOW));
        } else {
            buttonChisel.setMessage(ChiselLangKeys.BUTTON_CHISEL.getComponent());
        }
    }

    private void updateChiselData() {
        ItemStack stack = containerHitech.getChisel();
        if (!(stack.getItem() instanceof IChiselItem)) {
            return;
        }

        NBTUtil.setHitechType(stack, buttonPreview.getType().ordinal());
        NBTUtil.setHitechRotate(stack, buttonRotate.rotate());

        Chisel.network.sendToServer(new PacketHitechSettings(containerHitech.getChisel(), containerHitech.getChiselSlot()));
    }
    
    @Override
    protected void renderBg(PoseStack pStack, float f, int mx, int my) {
        RenderSystem.setShaderColor(1, 1, 1, 1);

        RenderSystem.setShaderTexture(0, TEXTURE);
        blit(pStack, getGuiLeft(), getGuiTop(), 0, 0, getXSize(), getYSize());

        if (containerHitech.getSelection() != null) {
            Slot sel = containerHitech.getSelection();
            if (sel.hasItem()) {
                drawSlotHighlight(pStack, sel, 0);
                for (Slot s : containerHitech.getSelectionDuplicates()) {
                    drawSlotHighlight(pStack, s, hasShiftDown() ? 0 : 18);
                }
            }
        }
        if (containerHitech.getTarget() != null && !containerHitech.getTarget().getItem().isEmpty()) {
            drawSlotHighlight(pStack, containerHitech.getTarget(), 36);
        }

        if (buttonRotate.rotate() && momentumX == 0 && momentumY == 0 && !panelClicked && System.currentTimeMillis() - lastDragTime > 2000) {
            rotY = initRotY + (f * 2);
        }
        
        if (panelClicked && clickButton == 0) {
            momentumX = rotX - prevRotX;
            momentumY = rotY - prevRotY;
            prevRotX = rotX;
            prevRotY = rotY;
        }
        
//        scrollAcc += Mouse.getDWheel();
//        if (Math.abs(scrollAcc) >= 120) {
//            int idx = -1;
//            if (containerHitech.getTarget() != null) {
//                idx = containerHitech.getTarget().getSlotIndex();
//            }
//            while (Math.abs(scrollAcc) >= 120) {
//                if (scrollAcc > 0) {
//                    idx--;
//                    scrollAcc -= 120;
//                } else {
//                    idx++;
//                    scrollAcc += 120;
//                }
//            }
//            if (idx < 0) {
//                for (int i = containerHitech.getInventoryChisel().size - 1; i >= 0; i--) {
//                    if (containerHitech.getSlot(i).getHasStack()) {
//                        idx = i;
//                        break;
//                    }
//                    if (i == 0) {
//                        idx = 0;
//                    }
//                }
//            } else if (idx >= containerHitech.getInventoryChisel().size || !containerHitech.getSlot(idx).getHasStack()) {
//                idx = 0;
//            }
//
//            containerHitech.setTarget(containerHitech.getSlot(idx));
//        }

        BlockRenderDispatcher brd = Minecraft.getInstance().getBlockRenderer();
        if (containerHitech.getTarget() != null) {

            ItemStack stack = containerHitech.getTarget().getItem();

            if (!stack.isEmpty()) {
                pStack.pushPose();

                pStack.translate(panel.getX() + (panel.getWidth() / 2), panel.getY() + (panel.getHeight() / 2), 100);

                ShaderInstance shader = RenderSystem.getShader();
                RenderSystem.setShader(GameRenderer::getBlockShader);
                pStack.pushPose();
                pStack.setIdentity();
                int scale = (int) getMinecraft().getWindow().getGuiScale();
                pStack.mulPoseMatrix(Matrix4f.perspective(60, (float) panel.getWidth() / panel.getHeight(), 0.01F, 4000));
//                RenderSystem.matrixMode(GL11.GL_MODELVIEW);
                pStack.translate(-panel.getX() - panel.getWidth() / 2, -panel.getY() - panel.getHeight() / 2, 0);
                RenderSystem.viewport((getGuiLeft() + panel.getX()) * scale, getMinecraft().getWindow().getHeight() - (getGuiTop() + panel.getY() + panel.getHeight()) * scale, panel.getWidth() * scale, panel.getHeight() * scale);
                RenderSystem.clear(GL11.GL_DEPTH_BUFFER_BIT, true);

                // Makes zooming slower as zoom increases, but leaves 1 as the default zoom.
                float sc = (float) (300 + 8 * buttonPreview.getType().getScale() * (Math.sqrt(zoom + 99) - 9));
                pStack.scale(-sc, -sc, sc);

                pStack.mulPose(Vector3f.XP.rotation(-rotX));
                pStack.mulPose(Vector3f.YP.rotation(rotY));
                pStack.translate(-1.5, -2.5, -0.5);
                
                RenderSystem.enableDepthTest();

                Block block = Block.byItem(stack.getItem());
                BlockState state = block.defaultBlockState();

                if (state != null && state != erroredState) {
                    erroredState = null;

                    fakeworld.setState(state);

                    RenderSystem.setShaderTexture(0, InventoryMenu.BLOCK_ATLAS);
                    BufferBuilder builder = Tesselator.getInstance().getBuilder();
                    try {
                        PoseStack ms = new PoseStack();
                        for (BlockPos pos : buttonPreview.getType().getPositions()) {
                            ms.pushPose();
                            ms.translate(pos.getX(), pos.getY(), pos.getZ());
                            brd.renderBatched(state, pos, fakeworld, ms, builder, true, new Random(), EmptyModelData.INSTANCE);
                            ms.popPose();
                        }
                    } catch (Exception e) {
                        erroredState = state;
                        Chisel.logger.error("Exception rendering block {}", state, e);
                    } finally {
                        if (erroredState == null) {
                            builder.end();
                        } else {
                            builder.discard();
                        }
                    }
                }

                pStack.popPose();
//                RenderSystem.matrixMode(GL11.GL_PROJECTION);
//                RenderSystem.popMatrix();
//                RenderSystem.matrixMode(GL11.GL_MODELVIEW);
                RenderSystem.viewport(0, 0, getMinecraft().getWindow().getWidth(), getMinecraft().getWindow().getHeight());
            }
        }
    }
    
    private void drawSlotHighlight(PoseStack PoseStack, Slot slot, int u) {
        blit(PoseStack, getGuiLeft() + slot.x - 1, getGuiTop() + slot.y - 1, u, 220, 18, 18);
    }
    
    private boolean doMomentum = false;
    
    @Override
    protected void renderTooltip(PoseStack PoseStack, int j, int i) {
        if (doMomentum) {
            rotX += momentumX;
            rotX = Mth.clamp(rotX, 90, 270);
            rotY += momentumY;
            momentumX *= momentumDampening;
            momentumY *= momentumDampening;
            if (Math.abs(momentumX) < 0.2) {
                if (Math.abs(momentumX) < 0.05) {
                    momentumX = 0;
                } else {
                    momentumX *= momentumDampening * momentumDampening;
                }
            }
            if (Math.abs(momentumY) < 0.2) {
                if (Math.abs(momentumY) < 0.05) {
                    momentumY = 0;
                } else {
                    momentumY *= momentumDampening * momentumDampening;
                }
            }
        }

        TranslatableComponent s = ChiselLangKeys.PREVIEW.getComponent();
        font.draw(PoseStack, s, panel.getX() + (panel.getWidth() / 2) - (font.width(s) / 2), panel.getY() - 9, 0x404040);
        
        drawButtonTooltips(PoseStack, j, i);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int mouseButton) {
        boolean ret = super.mouseClicked(mouseX, mouseY, mouseButton);
        if (!ret && !buttonRotate.isMouseOver(mouseX, mouseY) && panel.contains((int) (mouseX - getGuiLeft()), (int) (mouseY - getGuiTop()))) {
            if (mouseButton == 0) {
                doMomentum = false;
            }
            clickButton = mouseButton;
            panelClicked = true;
            clickX = (int) mouseX;
            clickY = (int) mouseY;
            return true;
        }
        return ret;
    }
    
    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int p_mouseDragged_5_, double p_mouseDragged_6_, double p_mouseDragged_8_) {
        if (panelClicked) {
            if (clickButton == 0) {
                rotX = Mth.clamp(initRotX + (float) mouseY - clickY, 90, 270);
                rotY = initRotY + (float) mouseX - clickX;
            }
            else if (clickButton == 1) {
                zoom = Math.max(1, initZoom + (clickY - (float) mouseY));
            }
        }
        
        return super.mouseDragged(mouseX, mouseY, p_mouseDragged_5_, p_mouseDragged_6_, p_mouseDragged_8_);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int state) {
        if (panelClicked) {
            lastDragTime = System.currentTimeMillis();
        }
        doMomentum = true;
        panelClicked = false;
        initRotX = rotX;
        initRotY = rotY;
        initZoom = zoom;

        return super.mouseReleased(mouseX, mouseY, state);
    }

//    @Override
//    protected void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
//        super.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
//    }
    
//    @Override
//    protected void actionPerformed(Button button) {
//        super.actionPerformed(button);
//
//        if (button == buttonChisel) {

//        }
//    }
}
