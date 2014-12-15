package com.cricketcraft.chisel.client.gui;

import com.cricketcraft.chisel.Chisel;
import com.cricketcraft.chisel.Configurations;

import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;
import cpw.mods.fml.client.config.GuiConfig;

public class GuiChiselConfig extends GuiConfig
{

    public GuiChiselConfig(GuiScreen parent)
    {
        super(parent, new ConfigElement(Configurations.config.getCategory(Configuration.CATEGORY_GENERAL)).getChildElements(), Chisel.MOD_ID, false, false, GuiConfig.getAbridgedConfigPath(Configurations.config.toString()));
    }
}
