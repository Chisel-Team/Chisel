package team.chisel.client.gui;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import com.google.common.base.Optional;
import com.mojang.blaze3d.platform.GlStateManager;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.fluid.Fluids;
import net.minecraft.fluid.IFluidState;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.IBlockReader;
import team.chisel.Chisel;
import team.chisel.api.IChiselItem;
import team.chisel.common.inventory.ContainerChiselHitech;
import team.chisel.common.util.NBTUtil;

@ParametersAreNonnullByDefault
public class GuiHitechChisel extends GuiChisel<ContainerChiselHitech> {

    private class PreviewModeButton extends Button {

        @Getter
        private PreviewType type;
        
        public PreviewModeButton(int buttonId, int x, int y, int widthIn, int heightIn) {
            super(x, y, widthIn, heightIn, "", /* TODO IPressable */ null);
            setType(PreviewType.values()[0]);
        }

        @Override
        public boolean mouseClicked(double mouseX, double mouseY, int i) {
            if (super.mouseClicked(mouseX, mouseY, i)) {
                setType(PreviewType.values()[(type.ordinal() + 1) % PreviewType.values().length]);
                return true;
            }
            return false;
        }
        
        private final void setType(PreviewType type) {
            this.type = type;
            //this.displayString = "< " + I18n.format(type.toString()) + " >";
            GuiHitechChisel.this.fakeworld = new FakeBlockAccess(GuiHitechChisel.this); // Invalidate region cache data
        }
    }
    
    private class RotateButton extends Button {

        @Getter
        @Accessors(fluent = true)
        private boolean rotate = true;
        
        public RotateButton(int buttonId, int x, int y) {
            super(x, y, 16, 16, "", /* TODO IPressable */ null);
        }

        @Override
        public void renderButton(int mouseX, int mouseY, float partialTick) {
            this.isHovered = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;

            GuiHitechChisel.this.getMinecraft().getTextureManager().bindTexture(TEXTURE);
            float a = isMouseOver(mouseX, mouseY) ? 1 : 0.2f;
            int u = rotate ? 0 : 16;
            int v = 238;

            GlStateManager.color4f(1, 1, 1, a);
            GlStateManager.enableBlend();
            GlStateManager.enableDepthTest();
            this.blitOffset = 1000;
            blit(this.x, this.y, u, v, 16, 16);
            this.blitOffset = 0;
            GlStateManager.color4f(1, 1, 1, 1);
        }

        @Override
        public boolean mouseClicked(double mouseX, double mouseY, int i) {
            if (super.mouseClicked(mouseX, mouseY, i)) {
                rotate = !rotate;
                return true;
            }
            return false;
        }
    }
    
    @RequiredArgsConstructor
    private static class FakeBlockAccess implements IBlockReader {
        private final GuiHitechChisel gui;
        
        @Setter
        private BlockState state = Blocks.AIR.getDefaultState();

        @Override
        public @Nullable TileEntity getTileEntity(BlockPos pos) {
            return null;
        }

        //@Override
        //public int getCombinedLight(BlockPos pos, int lightValue) {
        //    return 0xF000F0;
        //}

        @Override
        public BlockState getBlockState(BlockPos pos) {
            return gui.buttonPreview.getType().getPositions().contains(pos) ? state : Blocks.AIR.getDefaultState();
        }

        @Override
        public IFluidState getFluidState(BlockPos pos) {
            return Fluids.EMPTY.getDefaultState();
        }

//        @Override
//        public boolean isAirBlock(BlockPos pos) {
//            return getBlockState(pos).getBlock() == Blocks.AIR;
//        }

//        @Override
//        public Biome getBiome(BlockPos pos) {
//            return Biomes.PLAINS;
//        }

//        // @Override 1.9 only. Not actually ever called, just here for compilation.
//        public boolean extendedLevelsInChunkCache() {
//            return false;
//        }

//        @Override
//        public int getStrongPower(BlockPos pos, Direction direction) {
//            return 0;
//        }

//        @Override
//        public WorldType getWorldType() {
//            return WorldType.DEFAULT;
//        }

//        @Override
//        public boolean isSideSolid(BlockPos pos, Direction side, boolean _default) {
//            return getBlockState(pos).isSideSolid(this, pos, side);
//        }
    }
    
    //private static final Rectangle panel = new Rectangle(8, 14, 74, 74);
    
    private static final ResourceLocation TEXTURE = new ResourceLocation("chisel", "textures/chiselguihitech.png");
    
    private final ContainerChiselHitech containerHitech;
    
    private FakeBlockAccess fakeworld = new FakeBlockAccess(this);
    
    private boolean panelClicked;
    private int clickButton;
    private long lastDragTime;
    private int clickX, clickY;
    private float initRotX, initRotY, initZoom;
    private float prevRotX, prevRotY;
    private float momentumX, momentumY;
    private float momentumDampening = 0.98f;
    private float rotX = 165, rotY, zoom = 1;
    
    private int scrollAcc;
    
    private @Nullable PreviewModeButton buttonPreview;
    private @Nullable Button buttonChisel;
    private @Nullable RotateButton buttonRotate;
    
    private @Nullable BlockState erroredState;
    
    public GuiHitechChisel(ContainerChiselHitech container, PlayerInventory iinventory, ITextComponent displayName) {
        super(container, iinventory, displayName);
        containerHitech = container;
        xSize = 256;
        ySize = 220;
    }
    
//    @Override
//    public void initGui() {
//        super.initGui();
//        int x = guiLeft + panel.getX() - 1;
//        int y = guiTop + panel.getY() + panel.getHeight() + 3;
//        int w = 76, h = 20;
//        int id = 10;
//
//        boolean firstInit = buttonPreview == null;
//
//        buttonList.add(buttonPreview = new PreviewModeButton(id++, x, y, w, h));
//
//        buttonList.add(buttonChisel = new GuiButton(id++, x, y += h + 2, w, h, I18n.format("container.chisel.hitech.chisel")));
//        buttonList.add(buttonRotate = new RotateButton(id++, guiLeft + panel.getX() + panel.getWidth() - 16, guiTop + panel.getY() + panel.getHeight() - 16));
//
//        ItemStack chisel = containerHitech.getChisel();
//
//        if (firstInit) {
//            buttonPreview.setType(NBTUtil.getHitechType(chisel));
//            buttonRotate.rotate = NBTUtil.getHitechRotate(chisel);
//        }
//
//        try {
//            updateScreen();
//        } catch (Exception e) {
//            e.printStackTrace();
//            Chisel.logger.info("iChisel crash avoided, please consider updating NEI.");
//        }
//    }
    
//    @Override
//    protected Rectangle getModeButtonArea() {
//        int down = 133;
//        int padding = 7;
//        return new Rectangle(guiLeft + padding, guiTop + down + padding, 76, ySize - down - (padding * 2));
//    }

//    @Override
//    public void updateScreen() {
//        super.updateScreen();
//
//        buttonChisel.enabled = containerHitech.getSelection() != null && containerHitech.getSelection().getHasStack() && containerHitech.getTarget() != null && containerHitech.getTarget().getHasStack();
//
//        if (!panelClicked) {
//            initRotX = rotX;
//            initRotY = rotY;
//            initZoom = zoom;
//        }
//
//        if (isShiftDown()) {
//            buttonChisel.displayString = TextFormatting.YELLOW.toString() + I18n.format("container.chisel.hitech.chisel_all");
//        } else {
//            buttonChisel.displayString = I18n.format("container.chisel.hitech.chisel");
//        }
//    }

    @Override
    public void onClose() {
        super.onClose();

        ItemStack stack = containerHitech.getChisel();
        if (!(stack.getItem() instanceof IChiselItem)) {
            return;
        }

        NBTUtil.setHitechType(stack, buttonPreview.getType().ordinal());
        NBTUtil.setHitechSelection(stack, Optional.fromNullable(containerHitech.getSelection()).transform(s -> s.slotNumber).or(-1));
        NBTUtil.setHitechTarget(stack, Optional.fromNullable(containerHitech.getTarget()).transform(s -> s.slotNumber).or(-1));
        NBTUtil.setHitechRotate(stack, buttonRotate.rotate());

        Chisel.network.sendToServer(new PacketHitechSettings(containerHitech.getChisel(), containerHitech.getChiselSlot()));
    }

    private boolean isShiftDown() {
        return false; //return Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT);
    }
    
    @Override
    protected void drawGuiContainerBackgroundLayer(float f, int mx, int my) {
        //GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

        Minecraft.getInstance().getTextureManager().bindTexture(TEXTURE);
        //drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);

        if (containerHitech.getSelection() != null) {
            Slot sel = containerHitech.getSelection();
            if (sel.getHasStack()) {
                drawSlotHighlight(sel, 0);
                for (Slot s : containerHitech.getSelectionDuplicates()) {
                    drawSlotHighlight(s, isShiftDown() ? 0 : 18);
                }
            }
        }
        if (containerHitech.getTarget() != null && !containerHitech.getTarget().getStack().isEmpty()) {
            drawSlotHighlight(containerHitech.getTarget(), 36);
        }

        if (buttonRotate.rotate() && !panelClicked && System.currentTimeMillis() - lastDragTime > 2000) {
            rotY = initRotY + (f * 2);
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

//        BlockRendererDispatcher brd = Minecraft.getInstance().getBlockRendererDispatcher();
//        if (containerHitech.getTarget() != null) {
//
//            ItemStack stack = containerHitech.getTarget().getStack();
//
//            if (!stack.isEmpty()) {
//                GlStateManager.pushMatrix();
//
//                GlStateManager.translatef(panel.getX() + (panel.getWidth() / 2), panel.getY() + (panel.getHeight() / 2), 100);
//
//                GlStateManager.matrixMode(GL11.GL_PROJECTION);
//                GlStateManager.pushMatrix();
//                GlStateManager.loadIdentity();
//                int scale = new ScaledResolution(mc).getScaleFactor();
//                Project.gluPerspective(60, (float) panel.getWidth() / panel.getHeight(), 0.01F, 4000);
//                GlStateManager.matrixMode(GL11.GL_MODELVIEW);
//                GlStateManager.translate(-panel.getX() - panel.getWidth() / 2, -panel.getY() - panel.getHeight() / 2, 0);
//                GlStateManager.viewport((guiLeft + panel.getX()) * scale, mc.displayHeight - (guiTop + panel.getY() + panel.getHeight()) * scale, panel.getWidth() * scale, panel.getHeight() * scale);
//                GlStateManager.clear(GL11.GL_DEPTH_BUFFER_BIT);
//
//                // Makes zooming slower as zoom increases, but leaves 1 as the default zoom.
//                double sc = 300 + 8 * buttonPreview.getType().getScale() * (Math.sqrt(zoom + 99) - 9);
//                GlStateManager.scale(-sc, -sc, sc);
//
//                GlStateManager.rotate(rotX, 1, 0, 0);
//                GlStateManager.rotate(rotY, 0, 1, 0);
//                GlStateManager.translate(-1.5, -2.5, -0.5);
//
//                Block block = Block.getBlockFromItem(stack.getItem());
//                BlockState state = block == null ? null : block.getStateFromMeta(stack.getMetadata());
//                if (state instanceof IExtendedBlockState) {
//                    state = ((IExtendedBlockState) state).getClean();
//                }
//
//                if (state != null && state != erroredState) {
//                    erroredState = null;
//
//                    fakeworld.setState(state);
//
//                    mc.getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
//                    Tessellator.getInstance().getBuffer().begin(GL11.GL_QUADS, DefaultVertexFormats.BLOCK);
//                    try {
//                        for (BlockPos pos : buttonPreview.getType().getPositions()) {
//                            brd.renderBlock(state, pos, fakeworld, Tessellator.getInstance().getBuffer());
//                        }
//                    } catch (Exception e) {
//                        erroredState = state;
//                        Chisel.logger.error("Exception rendering block {}", state, e);
//                    } finally {
//                        if (erroredState == null) {
//                            Tessellator.getInstance().draw();
//                        } else {
//                            Tessellator.getInstance().getBuffer().finishDrawing();
//                        }
//                    }
//                }
//
//                GlStateManager.popMatrix();
//                GlStateManager.matrixMode(GL11.GL_PROJECTION);
//                GlStateManager.popMatrix();
//                GlStateManager.matrixMode(GL11.GL_MODELVIEW);
//                GlStateManager.viewport(0, 0, mc.displayWidth, mc.displayHeight);
//            }
//        }
    }
    
    private void drawSlotHighlight(Slot slot, int u) {
        //drawTexturedModalRect(guiLeft + slot.xPos - 1, guiTop + slot.yPos - 1, u, 220, 18, 18);
    }
    
    @SuppressWarnings("deprecation")
    @Override
    protected void drawGuiContainerForegroundLayer(int j, int i) {
        boolean doMomentum = true;
        if (panelClicked) {
            if (clickButton == 0) {
                prevRotX = rotX;
                prevRotY = rotY;
                //rotX = initRotX + Mouse.getY() - clickY;
                //rotY = initRotY + Mouse.getX() - clickX;
                momentumX = rotX - prevRotX;
                momentumY = rotY - prevRotY;
                doMomentum = false;
            }
            //else if (clickButton == 1) {
            //    zoom = Math.max(1, initZoom + (clickY - Mouse.getY()));
            //}
        } 
        
        if (doMomentum) {
            rotX += momentumX;
            rotY += momentumY;
            momentumX *= momentumDampening;
            momentumY *= momentumDampening;
        }

        String s = I18n.format("container.chisel.hitech.preview");
        //font.drawString(s, panel.getX() + (panel.getWidth() / 2) - (font.getStringWidth(s) / 2), panel.getY() - 9, 0x404040);
        GlStateManager.disableAlphaTest();
        
        drawButtonTooltips(j, i);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int mouseButton) {
        return super.mouseClicked(mouseX, mouseY, mouseButton);
//        if (!buttonRotate.isMouseOver() && panel.contains(mouseX - guiLeft, mouseY - guiTop)) {
//            clickButton = mouseButton;
//            panelClicked = true;
//            clickX = Mouse.getX();
//            clickY = Mouse.getY();
//        }
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int state) {
        if (panelClicked) {
            lastDragTime = System.currentTimeMillis();
        }
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
//            Slot target = containerHitech.getTarget();
//            Slot selected = containerHitech.getSelection();
//            if (target != null && target.getHasStack() && selected != null && selected.getHasStack()) {
//                if (ItemStack.areItemsEqual(target.getStack(), selected.getStack())) {
//                    return;
//                }
//                ItemStack converted = target.getStack().copy();
//                converted.setCount(selected.getStack().getCount());
//                int[] slots = new int[] { selected.getSlotIndex() };
//                if (isShiftDown()) {
//                    slots = ArrayUtils.addAll(slots, containerHitech.getSelectionDuplicates().stream().mapToInt(Slot::getSlotIndex).toArray());
//                }
//
//                Chisel.network.sendToServer(new PacketChiselButton(slots));
//
//                PacketChiselButton.chiselAll(player, slots);
//
//                if (!isShiftDown()) {
//                    List<Slot> dupes = containerHitech.getSelectionDuplicates();
//                    Slot next = selected;
//                    for (Slot s : dupes) {
//                        if (s.slotNumber > selected.slotNumber) {
//                            next = s;
//                            break;
//                        }
//                    }
//                    if (next == selected && dupes.size() > 0) {
//                        next = dupes.get(0);
//                    }
//                    containerHitech.setSelection(next);
//                } else {
//                    containerHitech.setSelection(selected); // Force duplicate recalc
//                }
//            }
//        }
//    }

    // @Override
//    protected void renderToolTip(ItemStack stack, int x, int y) {
//        if (slots.contains(x, y)) {
//            List<String> list = stack.getTooltip(this.mc.thePlayer, this.mc.gameSettings.advancedItemTooltips);
//
//            for (int i = 0; i < list.size(); ++i) {
//                if (i == 0) {
//                    list.set(i, stack.getRarity().rarityColor + (String) list.get(i));
//                } else {
//                    list.set(i, TextFormatting.GRAY + (String) list.get(i));
//                }
//            }
//
//            FontRenderer font = stack.getItem().getFontRenderer(stack);
//            list.addAll(Lists.newArrayList("", "", "", "", ""));
//            this.drawHoveringText(list, x, y, (font == null ? fontRendererObj : font));
//        } else {
//            super.renderToolTip(stack, x, y);
//        }
//    }
}
