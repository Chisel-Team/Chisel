package team.chisel.client;

import net.minecraft.client.Minecraft;
import net.minecraft.world.World;

import java.util.Random;

/**
 * Client Utility stuff
 */
public class ClientUtil {

    public static final Random rand = new Random();

    public static void playSound(World world, int x, int y, int z, String sound) {
        Minecraft.getMinecraft().theWorld.playSound(x + 0.5, y + 0.5, z + 0.5, sound, 0.3f + 0.7f * rand.nextFloat(), 0.6f + 0.4f * rand.nextFloat(), true);
    }
}
