package team.chisel.client.gui;

import javax.annotation.Nonnull;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;
import team.chisel.api.IChiselGuiType.ChiselGuiType;
import team.chisel.api.IChiselItem;
import team.chisel.common.block.TileAutoChisel;
import team.chisel.common.inventory.ContainerAutoChisel;
import team.chisel.common.inventory.ContainerChisel;
import team.chisel.common.inventory.ContainerChiselHitech;
import team.chisel.common.inventory.InventoryChiselSelection;

public class ChiselGuiHandler implements IGuiHandler {

    @Override
    public Object getServerGuiElement(int id, PlayerEntity player, World world, int x, int y, int z) {
        if (id == 0) {
            @SuppressWarnings("null")
            @Nonnull
            Hand hand = Hand.values()[x];
            ItemStack held = player.getHeldItem(hand);
            if (held.getItem() instanceof IChiselItem) {
                // FIXME unsafe cast
                switch ((ChiselGuiType) ((IChiselItem) held.getItem()).getGuiType(world, player, hand)) {
                case NORMAL:
                    return new ContainerChisel(player.inventory, new InventoryChiselSelection(player.getHeldItem(hand), 60), hand);
                case HITECH:
                    return new ContainerChiselHitech(player.inventory, new InventoryChiselSelection(player.getHeldItem(hand), 63), hand);
                }
            }
        } else if (id == 1) {
            TileEntity te = world.getTileEntity(new BlockPos(x, y, z));
            if (te instanceof TileAutoChisel) {
                return new ContainerAutoChisel(player.inventory, (TileAutoChisel) te, /* TODO what in tarnation is an IIntArray */null);
            }
        }
        return null;
    }

    @Override
    public Object getClientGuiElement(int id, PlayerEntity player, World world, int x, int y, int z) {
        if (id == 0) {
            @SuppressWarnings("null")
            @Nonnull
            Hand hand = Hand.values()[x];
            ItemStack held = player.getHeldItem(hand);
            if (held.getItem() instanceof IChiselItem) {
                // FIXME unsafe cast
                switch ((ChiselGuiType) ((IChiselItem) held.getItem()).getGuiType(world, player, hand)) {
                case NORMAL:
                    return new GuiChisel(player.inventory, new InventoryChiselSelection(player.getHeldItem(hand), 60), hand);
                case HITECH:
                    return new GuiHitechChisel(player.inventory, new InventoryChiselSelection(player.getHeldItem(hand), 63), hand);
                }
            }
        } else if (id == 1) {
            TileEntity te = world.getTileEntity(new BlockPos(x, y, z));
            if (te instanceof TileAutoChisel) {
                return new GuiAutoChisel(new ContainerAutoChisel(player.inventory, (TileAutoChisel) te, /* TODO what in tarnation is an IIntArray */null));
            }
        }
        return null;
    }
}
