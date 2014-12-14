package com.cricketcraft.minecraft.chisel.client.gui;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.inventory.Container;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import com.cricketcraft.minecraft.chisel.utils.DummyContainer;
import com.cricketcraft.minecraft.chisel.utils.GeneralClient;

public class GuiScreenPlus extends GuiContainer
{
    public int screenW;
    public int screenH;
    public int screenX;
    public int screenY;

    public GuiElement root;

    String backgroundTexture;

    public GuiScreenPlus(Container container, int w, int h, String backgroundTexture)
    {
        super(container);
        root = new GuiElement(0, 0, w, h);
        root.gui = this;
        this.screenW = w;
        this.screenH = h;
        this.backgroundTexture = backgroundTexture;
    }

    public GuiScreenPlus(int w, int h, String backgroundTexture)
    {
        this(new DummyContainer(), w, h, backgroundTexture);
    }

    @Override
    public void initGui()
    {
        xSize = screenW;
        ySize = screenH;
        super.initGui();
        screenX = guiLeft;
        screenY = guiTop;
        root.onAdded();

        Keyboard.enableRepeatEvents(true);
    }


    @Override
    public void handleInput()
    {
        while(Mouse.next())
        {
            this.handleMouseInput();
        }

        while(Keyboard.next())
        {
            this.handleKeyboardInput();
        }
    }

    InputMouseEvent mouseEvent = new InputMouseEvent();
    int oldX = -1;
    int oldY = -1;

    boolean[] downButtons = new boolean[12];

    @Override
    public void handleMouseInput()
    {
        mouseEvent.handled = false;
        mouseEvent.x = Mouse.getEventX() * width / mc.displayWidth - this.screenX;
        mouseEvent.y = height - Mouse.getEventY() * height / mc.displayHeight - 1 - this.screenY;

        if(oldX == -1)
        {
            oldX = mouseEvent.x;
            oldY = mouseEvent.y;
        }

        mouseEvent.dx = mouseEvent.x - oldX;
        mouseEvent.dy = mouseEvent.y - oldY;
        oldX = mouseEvent.x;
        oldY = mouseEvent.y;
        mouseEvent.down = Mouse.getEventButtonState();
        mouseEvent.button = Mouse.getEventButton();
        mouseEvent.wheel = Mouse.getEventDWheel();

        if(mouseEvent.wheel != 0)
        {
            if(mouseEvent.wheel < 0)
            {
                mouseEvent.wheel = -1;
            } else
            {
                mouseEvent.wheel = 1;
            }

            root.mouseWheel(mouseEvent);
        } else if(mouseEvent.button >= 0 && mouseEvent.button < downButtons.length)
        {
            if(downButtons[mouseEvent.button] != mouseEvent.down)
            {
                downButtons[mouseEvent.button] = mouseEvent.down;

                if(mouseEvent.down)
                {
                    root.mouseDown(mouseEvent);
                } else
                {
                    root.mouseUp(mouseEvent);
                }
            } else if(mouseEvent.dx != 0 || mouseEvent.dy != 0)
            {
                root.mouseMove(mouseEvent);
            }
        } else if(mouseEvent.dx != 0 || mouseEvent.dy != 0)
        {
            root.mouseMove(mouseEvent);
        }

        if(!mouseEvent.handled)
        {
            super.handleMouseInput();
        }
    }

    InputKeyboardEvent keyboardEvent = new InputKeyboardEvent();

    @Override
    public void handleKeyboardInput()
    {
        keyboardEvent.handled = false;

        if(Keyboard.getEventKeyState())
        {
            keyboardEvent.key = Keyboard.getEventKey();
            keyboardEvent.character = Keyboard.getEventCharacter();

            switch(keyboardEvent.key)
            {
                case 1:
                    break;

                default:
                    root.keyPressed(keyboardEvent);
            }
        }

        if(!keyboardEvent.handled)
        {
            super.handleKeyboardInput();
        }
    }

    public void close()
    {
        mc.displayGuiScreen((GuiScreen) null);
        mc.setIngameFocus();
    }

    protected void addChild(GuiElement e)
    {
        root.addChild(e);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float f, int bx, int by)
    {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GeneralClient.bind(backgroundTexture);
        drawTexturedModalRect(screenX, screenY, 0, 0, screenW, screenH);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int fx, int fy)
    {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        root.render();
    }

    public void drawString(String text, int sx, int sy, int color)
    {
        fontRendererObj.drawString(text, sx, sy, color);
    }

    public void drawCenteredString(String text, int sx, int sy, int color)
    {
        fontRendererObj.drawString(text, sx - fontRendererObj.getStringWidth(text) / 2, sy - fontRendererObj.FONT_HEIGHT / 2, color);
    }

    public void drawStringWithShadow(String text, int sx, int sy, int color)
    {
        fontRendererObj.drawStringWithShadow(text, sx, sy, color);
    }

    public void drawCenteredStringWithShadow(String text, int sx, int sy, int color)
    {
        fontRendererObj.drawStringWithShadow(text, sx - fontRendererObj.getStringWidth(text) / 2, sy - fontRendererObj.FONT_HEIGHT / 2, color);
    }

    public FontRenderer fontRendererObj()
    {
        return this.fontRendererObj;
    }

    protected void drawRect(int gx, int gy, int gw, int gh, int c1, int c2)
    {
        drawGradientRect(gx, gy, gx + gw, gy + gh, c1, c2);
    }

    public void drawTiledRect(int rx, int ry, int rw, int rh, int u, int v, int tw, int th)
    {
        if(rw == 0 || rh == 0 || tw == 0 || th == 0) return;

        float pixel = 0.00390625f;
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();

        for(int y = 0; y < rh; y += th)
        {
            for(int x = 0; x < rw; x += tw)
            {
                int qw = tw;

                if(x + qw > rw)
                {
                    qw = rw - x;
                }

                int qh = th;

                if(y + qh > rh)
                {
                    qh = rh - y;
                }

                double x1 = rx + x;
                double x2 = rx + x + qw;
                double y1 = ry + y;
                double y2 = ry + y + qh;
                double u1 = pixel * (u);
                double u2 = pixel * (u + tw);
                double v1 = pixel * (v);
                double v2 = pixel * (v + th);
                tessellator.addVertexWithUV(x1, y2, this.zLevel, u1, v2);
                tessellator.addVertexWithUV(x2, y2, this.zLevel, u2, v2);
                tessellator.addVertexWithUV(x2, y1, this.zLevel, u2, v1);
                tessellator.addVertexWithUV(x1, y1, this.zLevel, u1, v1);
            }
        }

        tessellator.draw();
    }

    public void bindTexture(String tex)
    {
        GeneralClient.bind(tex);
    }

    public void playSound(String sound, float volume, float pitch)
    {
        // TODO
        //mc.getSoundHandler().playSoundFX(null, 1.0F, 1.0F);
    }
}
