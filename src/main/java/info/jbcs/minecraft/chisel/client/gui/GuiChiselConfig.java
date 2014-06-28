package info.jbcs.minecraft.chisel.client.gui;

import cpw.mods.fml.client.config.GuiConfig;
import info.jbcs.minecraft.chisel.Chisel;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;

public class GuiChiselConfig extends GuiConfig{

    public GuiChiselConfig(GuiScreen parent) {
        super(parent, new ConfigElement(Chisel.config.getCategory(Configuration.CATEGORY_GENERAL)).getChildElements(),
                "chisel", false, false, GuiConfig.getAbridgedConfigPath(Chisel.config.toString()));
    }
}
