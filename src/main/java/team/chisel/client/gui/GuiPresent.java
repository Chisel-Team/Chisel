package team.chisel.client.gui;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import team.chisel.block.tileentity.TileEntityPresent;
import team.chisel.inventory.ContainerPresent;

public class GuiPresent extends GuiContainer {

	private static final ResourceLocation gui = new ResourceLocation("chisel:textures/present-gui.png");
	private int rows;
	private final EntityPlayer player;
	private final TileEntityPresent present;

	public GuiPresent(InventoryPlayer inventoryPlayer, TileEntityPresent tileEntityPresent) {
		super(new ContainerPresent(inventoryPlayer, tileEntityPresent));
		rows = tileEntityPresent.getSizeInventory() / 9;
		this.ySize = 114 + rows * 18;
		this.player = inventoryPlayer.player;
		this.present = tileEntityPresent;
		this.present.getParent().addPlayerUsing(player);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float p_146976_1_, int p_146976_2_, int p_146976_3_) {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.getTextureManager().bindTexture(gui);
		int x = (this.width - this.xSize) / 2;
		int y = (this.height - this.ySize) / 2;
		this.drawTexturedModalRect(x, y, 0, 0, this.xSize, this.rows * 18 + 17);
		this.drawTexturedModalRect(x, y + this.rows * 18 + 17, 0, 126, this.xSize, 96);
	}

	@Override
	public void onGuiClosed() {
		super.onGuiClosed();
		this.present.getParent().removePlayerUsing(player);
	}
}
