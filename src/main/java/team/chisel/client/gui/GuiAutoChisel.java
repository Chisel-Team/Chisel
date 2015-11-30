package team.chisel.client.gui;

import java.util.List;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import team.chisel.block.tileentity.TileEntityAutoChisel;
import team.chisel.inventory.ContainerAutoChisel;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;

public class GuiAutoChisel extends GuiContainer {

	private static final ResourceLocation gui = new ResourceLocation("chisel:textures/autochisel-gui.png");

	private TileEntityAutoChisel autochisel;

	public GuiAutoChisel(InventoryPlayer inventoryPlayer, TileEntityAutoChisel tileEntityAutoChisel) {
		super(new ContainerAutoChisel(inventoryPlayer, tileEntityAutoChisel));
		autochisel = tileEntityAutoChisel;
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		super.drawGuiContainerForegroundLayer(mouseX, mouseY);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTick) {
		super.drawScreen(mouseX, mouseY, partialTick);

		int x = (this.width - this.xSize) / 2;
		int y = (this.height - this.ySize) / 2;

		for (Slot slot : (List<Slot>) inventorySlots.inventorySlots) {
			if (!slot.getHasStack() && mouseInside(slot, mouseX - x, mouseY - y)) {
				if (slot.slotNumber < autochisel.getSizeInventory()) {
					String tt = autochisel.getSlotTooltipUnloc(slot.slotNumber);
					if (!Strings.isNullOrEmpty(tt)) {
						this.func_146283_a(Lists.newArrayList(tt), mouseX, mouseY);
					}
				}
			}
		}
		RenderHelper.enableGUIStandardItemLighting();
	}

	private boolean mouseInside(Slot slot, int x, int y) {
		return x >= slot.xDisplayPosition && x <= slot.xDisplayPosition + 16 && y >= slot.yDisplayPosition && y <= slot.yDisplayPosition + 16;
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3) {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.renderEngine.bindTexture(gui);
		int x = (width - xSize) / 2;
		int y = (height - ySize) / 2;
		this.drawTexturedModalRect(x, y, 0, 0, xSize, ySize);

		for (int i = TileEntityAutoChisel.BASE; i < TileEntityAutoChisel.CHISEL; i++) {
			if (autochisel.getStackInSlot(i) == null) {
				drawSlotOverlay(this, x, y, inventorySlots.getSlot(i), xSize, 0, 8);
			}
		}

		if (autochisel.getStackInSlot(TileEntityAutoChisel.CHISEL) == null) {
			drawSlotOverlay(this, x, y, inventorySlots.getSlot(TileEntityAutoChisel.CHISEL), xSize, 24, 0);
		}
	}

	public static void drawSlotOverlay(GuiContainer gui, int x, int y, Slot slot, int u, int v, int padding) {
		padding /= 2;
		gui.drawTexturedModalRect(x + (slot.xDisplayPosition - padding), y + (slot.yDisplayPosition - padding), u, v, 18 + padding, 18 + padding);
	}
}
