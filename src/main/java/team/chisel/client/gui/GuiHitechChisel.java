package team.chisel.client.gui;

import java.io.IOException;
import java.util.Locale;
import java.util.Set;

import javax.annotation.Generated;

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
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.property.IExtendedBlockState;

import org.apache.commons.lang3.StringUtils;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.Rectangle;
import org.lwjgl.util.glu.GLU;

import team.chisel.common.inventory.ContainerChiselHitech;
import team.chisel.common.inventory.InventoryChiselSelection;

import com.google.common.collect.ImmutableSet;


public class GuiHitechChisel extends GuiChisel {
    
    @Getter
    private enum PreviewType {
        PANEL(16, generateBetween(-1, 0, 0, 1, 2, 0)),
        
        PLUS(20,
            new BlockPos(0, 0, 0),
            new BlockPos(0, 1, 0),
            new BlockPos(1, 1, 0),
            new BlockPos(-1, 1, 0),
            new BlockPos(0, 2, 0)
        ),
        
        SINGLE(38, new BlockPos(0, 1, 0)),
        
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
            return gui.button.getType().getPositions().contains(pos) ? state : Blocks.AIR.getDefaultState();
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
    private static final Rectangle panel = new Rectangle(8, 33, 74, 74);
    
    private static final ResourceLocation TEXTURE = new ResourceLocation("chisel", "textures/chiselGuiHitech.png");
    
    private final ContainerChiselHitech containerHitech;
    
    private final FakeBlockAccess fakeworld = new FakeBlockAccess(this);
    
    private boolean panelClicked;
    private int clickX, clickY;
    private int prevRotX, prevRotY;
    private int rotX, rotY;
    
    private PreviewModeButton button;
    
    public GuiHitechChisel(InventoryPlayer iinventory, InventoryChiselSelection menu, EnumHand hand) {
        super(iinventory, menu, hand);
        inventorySlots = containerHitech = new ContainerChiselHitech(iinventory, menu, hand);
        xSize = 256;
        ySize = 220;
    }
    
    @Override
    public void initGui() {
        super.initGui();
        buttonList.add(button = new PreviewModeButton(0, guiLeft + panel.getX() - 1, guiTop + panel.getY() + panel.getHeight() + 2, 76, 20));
    }
    
    @Override
    protected void drawGuiContainerBackgroundLayer(float f, int mx, int my) {
        drawDefaultBackground();
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

        int i = width - xSize >> 1;
        int j = height - ySize >> 1;

        Minecraft.getMinecraft().getTextureManager().bindTexture(TEXTURE);
        drawTexturedModalRect(i, j, 0, 0, xSize, ySize);
        
        if (containerHitech.getSelection() != null) {
            drawTexturedModalRect(i + containerHitech.getSelection().xDisplayPosition - 1, j + containerHitech.getSelection().yDisplayPosition - 1, 0, 220, 18, 18);
        }
        drawTexturedModalRect(i + containerHitech.getTarget().xDisplayPosition - 1, j + containerHitech.getTarget().yDisplayPosition - 1, 18, 220, 18, 18);
    }
    
    @Override
    protected void drawGuiContainerForegroundLayer(int j, int i) {
        if (panelClicked) {
            rotX = prevRotX + Mouse.getY() - clickY;
            rotY = prevRotY + Mouse.getX() - clickX;
        }
        
        GlStateManager.enableAlpha();
        Minecraft.getMinecraft().getTextureManager().bindTexture(TEXTURE);
        
        zLevel = 1000;
        drawTexturedModalRect(panel.getX() + 1, panel.getY() + 1, 36, 220, 16, 16);
        zLevel = 0;

        String s = "Preview";
        fontRendererObj.drawString("Preview", panel.getX() + (panel.getWidth() / 2) - (fontRendererObj.getStringWidth(s) / 2), panel.getY() - 10, 0x404040);
                
        try {

            BlockRendererDispatcher brd = this.mc.getBlockRendererDispatcher();
            ItemStack stack = containerHitech.getTarget().getStack();
            
            if (stack != null) {
                
                GlStateManager.pushMatrix();
                
                GlStateManager.translate(panel.getX() + (panel.getWidth() / 2), panel.getY() + (panel.getHeight() / 2), 100);

                GlStateManager.translate(0.5, 1.5, 0.5);
                float sc = button.getType().getScale();
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
                for (BlockPos pos : button.getType().getPositions()) {
                    brd.renderBlock(state, pos, fakeworld, Tessellator.getInstance().getBuffer());
                }
                Tessellator.getInstance().draw();

                GlStateManager.popMatrix();
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
        panelClicked = false;
        prevRotX = rotX;
        prevRotY = rotY;
    }
    
    @Override
    protected void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
        super.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
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
