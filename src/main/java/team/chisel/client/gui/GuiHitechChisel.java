package team.chisel.client.gui;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
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
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.property.IExtendedBlockState;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.Rectangle;

import team.chisel.Chisel;
import team.chisel.client.ClientUtil;
import team.chisel.common.inventory.ContainerChiselHitech;
import team.chisel.common.inventory.InventoryChiselSelection;

import com.google.common.collect.ImmutableSet;


public class GuiHitechChisel extends GuiChisel {
    
    @Getter
    private enum PreviewType {
        PANEL(16, generateBetween(-1, 0, 0, 1, 2, 0)),
        
        HOLLOW(16, ArrayUtils.removeElement(generateBetween(-1, 0, 0, 1, 2, 0), new BlockPos(0, 1, 0))),
        
        PLUS(20,
            new BlockPos(0, 0, 0),
            new BlockPos(0, 1, 0),
            new BlockPos(1, 1, 0),
            new BlockPos(-1, 1, 0),
            new BlockPos(0, 2, 0)
        ),

        SINGLE(39, new BlockPos(0, 1, 0)),
        
        ;
        
        private static BlockPos[] generateBetween(int minX, int minY, int minZ, int maxX, int maxY, int maxZ) {
            BlockPos[] ret = new BlockPos[(maxX - minX + 1) * (maxY - minY + 1) * (maxZ - minZ + 1)];
            int i = 0;
            for (int x = minX; x <= maxX; x++) {
                for (int y = minY; y <= maxY; y++) {
                    for (int z = minZ; z <= maxZ; z++) {
                        ret[i++] = new BlockPos(x, y, z);
                    }
                }
            }
            return ret;
        }
        
        private float scale;
        private Set<BlockPos> positions;
        
        private PreviewType(float scale, BlockPos... positions) {
            this.scale = scale;
            this.positions = ImmutableSet.copyOf(positions);
        }
        
        @Override
        public String toString() {
            return StringUtils.capitalize(name().toLowerCase(Locale.US));
        }
    }

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
        
        private void setType(PreviewType type) {
            this.type = type;
            this.displayString = "< " + type.toString() + " >";
        }
    }
    
    @RequiredArgsConstructor
    private static class FakeBlockAccess implements IBlockAccess {
        
        private final GuiHitechChisel gui;
        
        @Setter
        private IBlockState state = Blocks.AIR.getDefaultState();

        @Override
        public TileEntity getTileEntity(BlockPos pos) {
            return null;
        }

        @Override
        public int getCombinedLight(BlockPos pos, int lightValue) {
            return 0xF0000F0;
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

        @Override
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
    
    private static final Rectangle slots = new Rectangle(0, 0, 1000, 1000);
    private static final Rectangle panel = new Rectangle(8, 14, 74, 74);
    
    private static final ResourceLocation TEXTURE = new ResourceLocation("chisel", "textures/chiselGuiHitech.png");
    
    private final ContainerChiselHitech containerHitech;
    
    private final FakeBlockAccess fakeworld = new FakeBlockAccess(this);
    
    private boolean panelClicked;
    private long lastDragTime;
    private int clickX, clickY;
    private float prevRotX, prevRotY;
    private float rotX = -15, rotY;
    
    private int scrollAcc;
    
    private PreviewModeButton buttonPreview;
    private GuiButton buttonChisel;
    
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
        
        buttonList.add(buttonPreview = new PreviewModeButton(id++, x, y, w, h));

        buttonList.add(buttonChisel = new GuiButton(id++, x, y += h + 2, w, h, "Chisel"));
    }
    
    @Override
    public void updateScreen() {
        super.updateScreen();
        
        if (!panelClicked) {
            prevRotX = rotX;
            prevRotY = rotY;
        }
    
        if (isShiftDown()) {
            buttonChisel.displayString = TextFormatting.YELLOW.toString() + "Chisel All";
        } else {
            buttonChisel.displayString = "Chisel";
        }
    }

    private boolean isShiftDown() {
        return Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT);
    }
    
    @Override
    protected void drawGuiContainerBackgroundLayer(float f, int mx, int my) {
        drawDefaultBackground();
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

        Minecraft.getMinecraft().getTextureManager().bindTexture(TEXTURE);
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

        if (!panelClicked && System.currentTimeMillis() - lastDragTime > 2000) {
            rotY = prevRotY + (f * 2);
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
    }
    
    private void drawSlotHighlight(Slot slot, int u) {
        drawTexturedModalRect(guiLeft + slot.xDisplayPosition - 1, guiTop + slot.yDisplayPosition - 1, u, 220, 18, 18);
    }
    
    @SuppressWarnings("deprecation")
    @Override
    protected void drawGuiContainerForegroundLayer(int j, int i) {
        if (panelClicked) {
            rotX = prevRotX + Mouse.getY() - clickY;
            rotY = prevRotY + Mouse.getX() - clickX;
        }

        String s = "Preview";
        fontRendererObj.drawString("Preview", panel.getX() + (panel.getWidth() / 2) - (fontRendererObj.getStringWidth(s) / 2), panel.getY() - 9, 0x404040);
                
        try {

            BlockRendererDispatcher brd = this.mc.getBlockRendererDispatcher();
            if (containerHitech.getTarget() != null) {
                
                ItemStack stack = containerHitech.getTarget().getStack();

                if (stack != null) {

                    GlStateManager.pushMatrix();

                    GlStateManager.translate(panel.getX() + (panel.getWidth() / 2), panel.getY() + (panel.getHeight() / 2) - 2, 100);

                    GlStateManager.translate(0.5, 1.5, 0.5);
                    float sc = buttonPreview.getType().getScale();
                    GlStateManager.scale(-sc, -sc, -sc);

                    GlStateManager.rotate(rotX, 1, 0, 0);
                    GlStateManager.rotate(rotY, 0, 1, 0);
                    GlStateManager.translate(-0.5, -1.5, -0.5);

                    Block block = Block.getBlockFromItem(stack.getItem());
                    IBlockState state = block.getStateFromMeta(stack.getMetadata());
                    if (state instanceof IExtendedBlockState) {
                        state = ((IExtendedBlockState) state).getClean();
                    }

                    fakeworld.setState(state);

                    Minecraft.getMinecraft().getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
                    Tessellator.getInstance().getBuffer().begin(GL11.GL_QUADS, DefaultVertexFormats.BLOCK);
                    for (BlockPos pos : buttonPreview.getType().getPositions()) {
                        brd.renderBlock(state, pos, fakeworld, Tessellator.getInstance().getBuffer());
                    }
                    Tessellator.getInstance().draw();

                    GlStateManager.popMatrix();
                }
            }
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        
        GlStateManager.disableAlpha();
    }
    
    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        if (mouseButton == 0 && panel.contains(mouseX - guiLeft, mouseY - guiTop)) {
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
        prevRotX = rotX;
        prevRotY = rotY;
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
                
                Chisel.network.sendToServer(new PacketHitechChisel(converted, slots)); 
                
                for (int i : slots) {
                    converted = converted.copy();
                    converted.stackSize = containerHitech.getInventoryPlayer().getStackInSlot(i).stackSize;
                    containerHitech.getInventoryPlayer().setInventorySlotContents(i, converted);
                }
                
                String sound = container.getCarving().getVariationSound(target.getStack());
                ClientUtil.playSound(player.worldObj, MathHelper.floor_double(player.posX), MathHelper.floor_double(player.posY), MathHelper.floor_double(player.posZ), sound, SoundCategory.BLOCKS);
                
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
