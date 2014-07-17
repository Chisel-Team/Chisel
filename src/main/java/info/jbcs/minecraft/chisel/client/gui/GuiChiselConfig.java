package info.jbcs.minecraft.chisel.client.gui;

import cpw.mods.fml.client.config.GuiConfig;
import info.jbcs.minecraft.chisel.Configurations;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;

public class GuiChiselConfig extends GuiConfig
{

    public GuiChiselConfig(GuiScreen parent)
    {
        super(parent, new ConfigElement(Configurations.config.getCategory(Configuration.CATEGORY_GENERAL)).getChildElements(), "chisel", false, false, GuiConfig.getAbridgedConfigPath(Configurations.config.toString()));
    }
}
