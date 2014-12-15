package com.cricketcraft.chisel.utils;

import java.util.ArrayList;
import java.util.Collections;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import cpw.mods.fml.common.network.IGuiHandler;
import cpw.mods.fml.common.network.NetworkRegistry;

public abstract class GuiHandler implements Comparable<Object>
{
    static ArrayList<GuiHandler> items = new ArrayList<GuiHandler>();

    int index;
    Object mod;
    String name;

    public GuiHandler(String n)
    {
        items.add(this);
        name = n;
    }

    public void open(EntityPlayer player, World world, int x, int y, int z)
    {
        player.openGui(mod, index, world, x, y, z);
    }

    @Override
    public int compareTo(Object a)
    {
        return name.compareTo(((GuiHandler) a).name);
    }

    public static void register(Object mod)
    {
        Collections.sort(items);
        int index = 0;

        for(GuiHandler h : items)
        {
            h.mod = mod;
            h.index = index++;
        }

        NetworkRegistry.INSTANCE.registerGuiHandler(mod, new IGuiHandler()
        {
            @Override
            public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z)
            {
                if(id < 0 || id >= items.size())
                {
                    return null;
                }

                return items.get(id).getServerGuiElement(id, player, world, x, y, z);
            }

            @Override
            public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z)
            {
                if(id < 0 || id >= items.size())
                {
                    return null;
                }

                return items.get(id).getClientGuiElement(id, player, world, x, y, z);
            }
        });
    }

    public abstract Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z);

    public abstract Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z);
}
