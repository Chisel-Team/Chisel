package com.cricketcraft.chisel.client.gui;

import java.util.ArrayList;

import org.lwjgl.opengl.GL11;

public class GuiElement
{
    public int x;
    public int y;
    public int w;
    public int h;

    public GuiScreenPlus gui;
    public GuiElement parent, addedParent;
    private ArrayList<GuiElement> children = null;

    public GuiElement(int x, int y, int w, int h)
    {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
    }

    public GuiElement addChild(GuiElement e)
    {
        if(children == null)
        {
            children = new ArrayList<GuiElement>();
        }

        if(e.parent != null)
        {
            e.parent.removeChild(e);
        }

        children.add(e);
        e.x += x;
        e.y += y;
        e.parent = this;
        e.gui = gui;
        return e;
    }

    public GuiElement removeChild(GuiElement e)
    {
        if(children == null)
        {
            return e;
        }

        children.remove(e);
        e.x -= x;
        e.y -= y;
        e.parent = null;
        e.gui = null;
        return e;
    }

    public void onAdded()
    {
        if(children == null)
        {
            return;
        }

        for(GuiElement e : children)
        {
            if(e.parent != e.addedParent)
            {
                e.onAdded();
            }

            e.addedParent = e.parent;
        }
    }

    boolean isMouseOver(InputMouseEvent ev)
    {
        return ev.x >= x && ev.x < x + w && ev.y >= y && ev.y < y + h;
    }

    public void mouseDown(InputMouseEvent ev)
    {
        if(children == null)
        {
            return;
        }

        for(GuiElement e : children)
        {
            e.mouseDown(ev);

            if(ev.handled)
            {
                return;
            }
        }
    }

    public void mouseUp(InputMouseEvent ev)
    {
        if(children == null)
        {
            return;
        }

        for(GuiElement e : children)
        {
            e.mouseUp(ev);

            if(ev.handled)
            {
                return;
            }
        }
    }

    public void mouseMove(InputMouseEvent ev)
    {
        if(children == null)
        {
            return;
        }

        for(GuiElement e : children)
        {
            e.mouseMove(ev);

            if(ev.handled)
            {
                return;
            }
        }
    }

    public void mouseWheel(InputMouseEvent ev)
    {
        if(children == null)
        {
            return;
        }

        for(GuiElement e : children)
        {
            e.mouseWheel(ev);

            if(ev.handled)
            {
                return;
            }
        }
    }

    public void render()
    {
        if(children == null)
        {
            return;
        }

        for(GuiElement e : children)
        {
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            e.render();
        }
    }

    public void keyPressed(InputKeyboardEvent ev)
    {
        if(children == null)
        {
            return;
        }

        for(GuiElement e : children)
        {
            e.keyPressed(ev);

            if(ev.handled)
            {
                return;
            }
        }
    }
}
