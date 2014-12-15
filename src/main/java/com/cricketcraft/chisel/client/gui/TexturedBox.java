package com.cricketcraft.chisel.client.gui;

public class TexturedBox
{
    String texture;
    int u, v, texw, texh;
    int borderTop, borderRight, borderBottom, borderLeft;

    public TexturedBox(String texture, int u, int v, int texw, int texh, int borderTop, int borderRight, int borderBottom, int borderLeft)
    {
        this.texture = texture;
        this.u = u;
        this.v = v;
        this.texw = texw;
        this.texh = texh;
        this.borderTop = borderTop;
        this.borderRight = borderRight;
        this.borderBottom = borderBottom;
        this.borderLeft = borderLeft;
    }

    public void render(GuiScreenPlus gui, int x, int y, int w, int h)
    {
        int x1 = x;
        int x2 = x + borderLeft;
        int x3 = x + w - borderRight;
        int y1 = y;
        int y2 = y + borderTop;
        int y3 = y + h - borderBottom;
        int w1 = borderLeft;
        int w2 = w - borderLeft - borderRight;
        int w3 = borderRight;
        int h1 = borderTop;
        int h2 = h - borderTop - borderBottom;
        int h3 = borderBottom;
        int u1 = u;
        int v1 = v;
        int tw = texw;

        if(tw > w)
        {
            tw = w;
        }

        int th = texh;

        if(th > h)
        {
            th = h;
        }

        int u2 = u1 + borderLeft;
        int u3 = u1 + texw - borderRight;
        int v2 = v1 + borderTop;
        int v3 = v1 + texh - borderBottom;
        int texw1 = borderLeft;
        int texw2 = tw - borderLeft - borderRight;
        int texw3 = borderRight;
        int texh1 = borderTop;
        int texh2 = th - borderTop - borderBottom;
        int texh3 = borderBottom;
        gui.bindTexture(texture);
        gui.drawTexturedModalRect(x1, y1, u1, v1, w1, h1);
        gui.drawTiledRect(x2, y1, w2, h1, u2, v1, texw2, texh1);
        gui.drawTexturedModalRect(x3, y1, u3, v1, w3, h1);
        gui.drawTiledRect(x1, y2, w1, h2, u1, v2, w1, texh2);
        gui.drawTiledRect(x2, y2, w2, h2, u2, v2, texw2, texh2);
        gui.drawTiledRect(x3, y2, w3, h2, u3, v2, w3, texh2);
        gui.drawTexturedModalRect(x1, y3, u1, v3, w1, h3);
        gui.drawTiledRect(x2, y3, w2, h3, u2, v3, texw2, texh3);
        gui.drawTexturedModalRect(x3, y3, u3, v3, w3, h3);
    }
}
