package team.chisel.client.gui;

import javax.annotation.Nonnull;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;
import team.chisel.common.inventory.ContainerChisel;
import team.chisel.common.inventory.ContainerChiselHitech;
import team.chisel.common.inventory.InventoryChiselSelection;
import team.chisel.common.item.ItemChisel;

/**
 * The Gui Handler
 *
 * @author minecreatr
 */
public class ChiselGuiHandler implements IGuiHandler {

    @Override
    public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
        if (id == 0) {
            @SuppressWarnings("null")
            @Nonnull
            EnumHand hand = EnumHand.values()[x];
            ItemStack held = player.getHeldItem(hand);
            if (held != null && held.getItem() instanceof ItemChisel) {
                switch (((ItemChisel) held.getItem()).getType()) {
                case IRON:
                case DIAMOND:
                    return new ContainerChisel(player.inventory, new InventoryChiselSelection(player.getHeldItem(hand), 60), hand);
                case HITECH:
                    return new ContainerChiselHitech(player.inventory, new InventoryChiselSelection(player.getHeldItem(hand), 63), hand);
                }
            }
        }
        return null;
    }

    @Override
    public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
        if (id == 0) {
            @SuppressWarnings("null")
            @Nonnull
            EnumHand hand = EnumHand.values()[x];
            ItemStack held = player.getHeldItem(hand);
            if (held != null && held.getItem() instanceof ItemChisel) {
                switch (((ItemChisel) held.getItem()).getType()) {
                case IRON:
                case DIAMOND:
                    return new GuiChisel(player.inventory, new InventoryChiselSelection(player.getHeldItem(hand), 60), hand);
                case HITECH:
                    return new GuiHitechChisel(player.inventory, new InventoryChiselSelection(player.getHeldItem(hand), 63), hand);
                }
            }
        }
        return null;
    }
}
