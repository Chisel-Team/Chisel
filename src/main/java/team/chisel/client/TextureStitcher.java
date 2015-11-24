package team.chisel.client;

import net.minecraft.client.gui.GuiMainMenu;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import team.chisel.Chisel;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * To stitch textures
 *
 * @author minecreatr
 */
public class TextureStitcher {

    public static List<String> toBeRegistered = new ArrayList<String>();

    @SubscribeEvent
    public void onTextureStitch(TextureStitchEvent.Pre event) {
        Chisel.proxy.preTextureStitch();
        for (String s : toBeRegistered) {
            event.map.registerSprite(new ResourceLocation(s));
            Chisel.debug("Stitching texture " + s);
        }
        //CTMBlockResources.refreshAll(event.map);
    }

    public static void register(String s) {
        toBeRegistered.add(s);
    }

}
