package info.jbcs.minecraft.gui;

public class GuiLabel extends GuiElement {
	String caption;

	public GuiLabel(int x, int y, int w, int h, String caption) {
		super(x, y, w, h);
		this.caption = caption;
	}

	public GuiLabel(int x, int y, String string) {
		this(x, y, 0, 0, string);
	}

	@Override
	public void render() {
		gui.drawString(caption, x, y, 0x404040);
	}
}
