package team.chisel.client.gui;

import java.io.IOException;
import java.util.List;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import org.apache.commons.lang3.ArrayUtils;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.Rectangle;
import org.lwjgl.util.glu.Project;

import com.google.common.base.Optional;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Biomes;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.property.IExtendedBlockState;
import team.chisel.Chisel;
import team.chisel.api.carving.CarvingUtils;
import team.chisel.client.ClientUtil;
import team.chisel.common.inventory.ContainerChiselHitech;
import team.chisel.common.inventory.InventoryChiselSelection;
import team.chisel.common.util.NBTUtil;

@ParametersAreNonnullByDefault
public class GuiHitechChisel extends GuiChisel {

    @SuppressWarnings("null")
    private static class PreviewModeButton extends GuiButton {

        @Getter
        private PreviewType type;
        
        public PreviewModeButton(int buttonId, int x, int y, int widthIn, int heightIn) {
            super(buttonId, x, y, widthIn, heightIn, "");
            setType(PreviewType.values()[0]);
        }
        
        @Override
        public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
            if (super.mousePressed(mc, mouseX, mouseY)) {
                setType(PreviewType.values()[(type.ordinal() + 1) % PreviewType.values().length]);
                return true;
            }
            return false;
        }
        
        private final void setType(PreviewType type) {
            this.type = type;
            this.displayString = "< " + type.toString() + " >";
        }
    }
    
    private class RotateButton extends GuiButton {

        @Getter
        @Accessors(fluent = true)
        private boolean rotate = true;
        
        public RotateButton(int buttonId, int x, int y) {
            super(buttonId, x, y, 16, 16, "");
        }
        
        @Override
        public void drawButton(Minecraft mc, int mouseX, int mouseY) {
            this.hovered = mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height;

            mc.getTextureManager().bindTexture(TEXTURE);
            float a = isMouseOver() ? 1 : 0.2f;
            int u = rotate ? 0 : 16;
            int v = 238;
            
            GlStateManager.color(1, 1, 1, a);
            GlStateManager.enableBlend();
            GlStateManager.enableDepth();
            zLevel = 1000;
            drawTexturedModalRect(this.xPosition, this.yPosition, u, v, 16, 16);
            zLevel = 0;
            GlStateManager.color(1, 1, 1, 1);
        }
        
        @Override
        public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
            if (super.mousePressed(mc, mouseX, mouseY)) {
                rotate = !rotate;
                return true;
            }
            return false;
        }
        
    }
    
    @RequiredArgsConstructor
    private static class FakeBlockAccess implements IBlockAccess {
        
        private final GuiHitechChisel gui;
        
        @Setter
        private IBlockState state = Blocks.AIR.getDefaultState();

        @Override
        public @Nullable TileEntity getTileEntity(BlockPos pos) {
            return null;
        }

        @Override
        public int getCombinedLight(BlockPos pos, int lightValue) {
            return 0xF000F0;
        }

        @Override
        public IBlockState getBlockState(BlockPos pos) {
            return gui.buttonPreview.getType().getPositions().contains(pos) ? state : Blocks.AIR.getDefaultState();
        }

        @Override
        public boolean isAirBlock(BlockPos pos) {
            return getBlockState(pos).getBlock() == Blocks.AIR;
        }

        @Override
        public Biome getBiome(BlockPos pos) {
            return Biomes.PLAINS;
        }

        // @Override 1.9 only. Not actually ever called, just here for compilation.
        public boolean extendedLevelsInChunkCache() {
            return false;
        }

        @Override
        public int getStrongPower(BlockPos pos, EnumFacing direction) {
            return 0;
        }

        @Override
        public WorldType getWorldType() {
            return WorldType.DEFAULT;
        }

        @Override
        public boolean isSideSolid(BlockPos pos, EnumFacing side, boolean _default) {
            return getBlockState(pos).isSideSolid(this, pos, side);
        }
    }
    
    private static final Rectangle panel = new Rectangle(8, 14, 74, 74);
    
    private static final ResourceLocation TEXTURE = new ResourceLocation("chisel", "textures/chiselGuiHitech.png");
    
    private final ContainerChiselHitech containerHitech;
    
    private final FakeBlockAccess fakeworld = new FakeBlockAccess(this);
    
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
    private @Nullable GuiButton buttonChisel;
    private @Nullable RotateButton buttonRotate;
    
    private @Nullable IBlockState erroredState;
    
    public GuiHitechChisel(InventoryPlayer iinventory, InventoryChiselSelection menu, EnumHand hand) {
        super(iinventory, menu, hand);
        inventorySlots = containerHitech = new ContainerChiselHitech(iinventory, menu, hand);
        xSize = 256;
        ySize = 220;
    }
    
    @Override
    public void initGui() {
        super.initGui();
        int x = guiLeft + panel.getX() - 1;
        int y = guiTop + panel.getY() + panel.getHeight() + 3;
        int w = 76, h = 20;
        int id = 0;
        
        boolean firstInit = buttonPreview == null;
        
        buttonList.add(buttonPreview = new PreviewModeButton(id++, x, y, w, h));

        buttonList.add(buttonChisel = new GuiButton(id++, x, y += h + 2, w, h, "Chisel"));
        buttonList.add(buttonRotate = new RotateButton(id++, guiLeft + panel.getX() + panel.getWidth() - 16, guiTop + panel.getY() + panel.getHeight() - 16));

        ItemStack chisel = containerHitech.getChisel();
        
        if (firstInit) {
            buttonPreview.setType(NBTUtil.getHitechType(chisel));
            buttonRotate.rotate = NBTUtil.getHitechRotate(chisel);
        }

        try {
            updateScreen();
        } catch (Exception e) {
            e.printStackTrace();
            Chisel.logger.info("iChisel crash avoided, please consider updating NEI.");
        }
    }
    
    @Override
    public void updateScreen() {
        super.updateScreen();
        
        buttonChisel.enabled = containerHitech.getSelection() != null && containerHitech.getSelection().getHasStack() && containerHitech.getTarget() != null && containerHitech.getTarget().getHasStack();
        
        if (!panelClicked) {
            initRotX = rotX;
            initRotY = rotY;
            initZoom = zoom;
        }
    
        if (isShiftDown()) {
            buttonChisel.displayString = TextFormatting.YELLOW.toString() + "Chisel All";
        } else {
            buttonChisel.displayString = "Chisel";
        }
    }
    
    @Override
    public void onGuiClosed() {
        super.onGuiClosed();

        ItemStack stack = containerHitech.getChisel();
        if (stack == null) {
            return;
        }

        NBTUtil.setHitechType(stack, buttonPreview.getType().ordinal());
        NBTUtil.setHitechSelection(stack, Optional.fromNullable(containerHitech.getSelection()).transform(s -> s.slotNumber).or(-1));
        NBTUtil.setHitechTarget(stack, Optional.fromNullable(containerHitech.getTarget()).transform(s -> s.slotNumber).or(-1));
        NBTUtil.setHitechRotate(stack, buttonRotate.rotate());

        Chisel.network.sendToServer(new PacketHitechSettings(containerHitech.getChisel(), containerHitech.getChiselSlot()));
    }

    private boolean isShiftDown() {
        return Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT);
    }
    
    @Override
    protected void drawGuiContainerBackgroundLayer(float f, int mx, int my) {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

        mc.getTextureManager().bindTexture(TEXTURE);
        drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);

        if (containerHitech.getSelection() != null) {
            Slot sel = containerHitech.getSelection();
            if (sel.getHasStack()) {
                drawSlotHighlight(sel, 0);
                for (Slot s : containerHitech.getSelectionDuplicates()) {
                    drawSlotHighlight(s, isShiftDown() ? 0 : 18);
                }
            }
        }
        if (containerHitech.getTarget() != null && containerHitech.getTarget().getStack() != null) {
            drawSlotHighlight(containerHitech.getTarget(), 36);
        }

        if (buttonRotate.rotate() && !panelClicked && System.currentTimeMillis() - lastDragTime > 2000) {
            rotY = initRotY + (f * 2);
        }
        
        scrollAcc += Mouse.getDWheel();
        if (Math.abs(scrollAcc) >= 120) {
            int idx = -1;
            if (containerHitech.getTarget() != null) {
                idx = containerHitech.getTarget().getSlotIndex();
            }
            while (Math.abs(scrollAcc) >= 120) {
                if (scrollAcc > 0) {
                    idx--;
                    scrollAcc -= 120;
                } else {
                    idx++;
                    scrollAcc += 120;
                }
            }
            if (idx < 0) {
                for (int i = containerHitech.getInventoryChisel().size - 1; i >= 0; i--) {
                    if (containerHitech.getSlot(i).getHasStack()) {
                        idx = i;
                        break;
                    }
                    if (i == 0) {
                        idx = 0;
                    }
                }
            } else if (idx >= containerHitech.getInventoryChisel().size || !containerHitech.getSlot(idx).getHasStack()) {
                idx = 0;
            }

            containerHitech.setTarget(containerHitech.getSlot(idx));
        }
    
        BlockRendererDispatcher brd = this.mc.getBlockRendererDispatcher();
        if (containerHitech.getTarget() != null) {

            ItemStack stack = containerHitech.getTarget().getStack();

            if (stack != null) {

                GlStateManager.pushMatrix();

                GlStateManager.translate(panel.getX() + (panel.getWidth() / 2), panel.getY() + (panel.getHeight() / 2), 100);

                GlStateManager.matrixMode(GL11.GL_PROJECTION);
                GlStateManager.pushMatrix();
                GlStateManager.loadIdentity();
                int scale = new ScaledResolution(mc).getScaleFactor();
                Project.gluPerspective(60, (float) panel.getWidth() / panel.getHeight(), 0.01F, 4000);
                GlStateManager.matrixMode(GL11.GL_MODELVIEW);
                GlStateManager.translate(-panel.getX() - panel.getWidth() / 2, -panel.getY() - panel.getHeight() / 2, 0);
                GlStateManager.viewport((guiLeft + panel.getX()) * scale, mc.displayHeight - (guiTop + panel.getY() + panel.getHeight()) * scale, panel.getWidth() * scale, panel.getHeight() * scale);
                GlStateManager.clear(GL11.GL_DEPTH_BUFFER_BIT);

                // Makes zooming slower as zoom increases, but leaves 1 as the default zoom.
                double sc = 300 + 8 * buttonPreview.getType().getScale() * (Math.sqrt(zoom + 99) - 9);
                GlStateManager.scale(-sc, -sc, sc);

                GlStateManager.rotate(rotX, 1, 0, 0);
                GlStateManager.rotate(rotY, 0, 1, 0);
                GlStateManager.translate(-1.5, -2.5, -0.5);

                Block block = Block.getBlockFromItem(stack.getItem());
                IBlockState state = block == null ? null : block.getStateFromMeta(stack.getMetadata());
                if (state instanceof IExtendedBlockState) {
                    state = ((IExtendedBlockState) state).getClean();
                }

                if (state != null && state != erroredState) {
                    erroredState = null;

                    fakeworld.setState(state);

                    mc.getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
                    Tessellator.getInstance().getBuffer().begin(GL11.GL_QUADS, DefaultVertexFormats.BLOCK);
                    try {
                        for (BlockPos pos : buttonPreview.getType().getPositions()) {
                            brd.renderBlock(state, pos, fakeworld, Tessellator.getInstance().getBuffer());
                        }
                    } catch (Exception e) {
                        erroredState = state;
                        Chisel.logger.error("Exception rendering block {}", state, e);
                    } finally {
                        if (erroredState == null) {
                            Tessellator.getInstance().draw();
                        } else {
                            Tessellator.getInstance().getBuffer().finishDrawing();
                        }
                    }
                }

                GlStateManager.popMatrix();
                GlStateManager.matrixMode(GL11.GL_PROJECTION);
                GlStateManager.popMatrix();
                GlStateManager.matrixMode(GL11.GL_MODELVIEW);
                GlStateManager.viewport(0, 0, mc.displayWidth, mc.displayHeight);
            }
        }
    }
    
    private void drawSlotHighlight(Slot slot, int u) {
        drawTexturedModalRect(guiLeft + slot.xDisplayPosition - 1, guiTop + slot.yDisplayPosition - 1, u, 220, 18, 18);
    }
    
    @SuppressWarnings("deprecation")
    @Override
    protected void drawGuiContainerForegroundLayer(int j, int i) {
        boolean doMomentum = true;
        if (panelClicked) {
            if (clickButton == 0) {
                prevRotX = rotX;
                prevRotY = rotY;
                rotX = initRotX + Mouse.getY() - clickY;
                rotY = initRotY + Mouse.getX() - clickX;
                momentumX = rotX - prevRotX;
                momentumY = rotY - prevRotY;
                doMomentum = false;
            } else if (clickButton == 1) {
                zoom = Math.max(1, initZoom + (clickY - Mouse.getY()));
            }
        } 
        
        if (doMomentum) {
            rotX += momentumX;
            rotY += momentumY;
            momentumX *= momentumDampening;
            momentumY *= momentumDampening;
        }

        String s = "Preview";
        fontRendererObj.drawString("Preview", panel.getX() + (panel.getWidth() / 2) - (fontRendererObj.getStringWidth(s) / 2), panel.getY() - 9, 0x404040);
        GlStateManager.disableAlpha();
    }
    
    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        if (!buttonRotate.isMouseOver() && panel.contains(mouseX - guiLeft, mouseY - guiTop)) {
            clickButton = mouseButton;
            panelClicked = true;
            clickX = Mouse.getX();
            clickY = Mouse.getY();
        }
    }
    
    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        super.mouseReleased(mouseX, mouseY, state);
        if (panelClicked) {
            lastDragTime = System.currentTimeMillis();
        }
        panelClicked = false;
        initRotX = rotX;
        initRotY = rotY;
        initZoom = zoom;
    }
    
    @Override
    protected void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
        super.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
    }
    
    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        super.actionPerformed(button);
        
        if (button == buttonChisel) {
            Slot target = containerHitech.getTarget();
            Slot selected = containerHitech.getSelection();
            if (target != null && target.getHasStack() && selected != null && selected.getHasStack()) {
                if (ItemStack.areItemsEqual(target.getStack(), selected.getStack())) {
                    return;
                }
                ItemStack converted = target.getStack().copy();
                converted.stackSize = selected.getStack().stackSize;
                int[] slots = new int[] { selected.getSlotIndex() };
                if (isShiftDown()) {
                    slots = ArrayUtils.addAll(slots, containerHitech.getSelectionDuplicates().stream().mapToInt(Slot::getSlotIndex).toArray());
                }
                
                Chisel.network.sendToServer(new PacketChiselButton(slots));
                
                PacketChiselButton.chiselAll(player, slots);
                
                ClientUtil.playSound(player.world, player, containerHitech.getChisel(), CarvingUtils.getChiselRegistry().getVariation(target.getStack()).getBlockState());
                
                if (!isShiftDown()) {
                    List<Slot> dupes = containerHitech.getSelectionDuplicates();
                    Slot next = selected;
                    for (Slot s : dupes) {
                        if (s.slotNumber > selected.slotNumber) {
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
        }
    }

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
