package team.chisel.client.gui;

import team.chisel.common.inventory.ContainerChisel;
import team.chisel.common.inventory.InventoryChiselSelection;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

/**
 * The Gui Handler
 *
 * @author minecreatr
 */
public class ChiselGuiHandler implements IGuiHandler {

    @Override
    public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
        if (id == 0){
            return new ContainerChisel(player.inventory, new InventoryChiselSelection(player.getHeldItem()));
        }
        else {
            return null;
        }
    }

    @Override
    public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
        if (id == 0) {
            return new GuiChisel(player.inventory, new InventoryChiselSelection(player.getHeldItem()));
        }
        return null;
    }
}
