package team.chisel.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent.ClientTickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.Phase;

public class ClientTickHandler {

    public static int gameTicks = 0;

    @SubscribeEvent
    public void onClientTick(ClientTickEvent event) {
        if(event.phase == Phase.END) {
            GuiScreen gui = Minecraft.getMinecraft().currentScreen;
            if (gui == null || !gui.doesGuiPauseGame()) {
                gameTicks++;
            }
        }
    }

}
