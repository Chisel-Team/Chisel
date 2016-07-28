package team.chisel.client.gui;

import java.awt.Container;
import java.util.Optional;

import io.netty.buffer.ByteBuf;
import lombok.NoArgsConstructor;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import team.chisel.common.inventory.ContainerChisel;

@NoArgsConstructor
public class PacketChiselButton implements IMessage {

    private ItemStack target;
    private int chiselSlot;
    private int[] slotIds;

    public PacketChiselButton(ItemStack target, int chiselSlot, int... slots) {
        this.target = target;
        this.chiselSlot = chiselSlot;
        this.slotIds = slots;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeItemStack(buf, target);
        buf.writeByte(chiselSlot);
        buf.writeByte(slotIds.length);
        for (int i : slotIds) {
            buf.writeByte(i);
        }
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        target = ByteBufUtils.readItemStack(buf);
        chiselSlot = buf.readByte();
        int len = buf.readByte();
        slotIds = new int[len];
        for (int i = 0; i < slotIds.length; i++) {
            slotIds[i] = buf.readByte();
        }
    }

    public static class Handler implements IMessageHandler<PacketChiselButton, IMessage> {

        @Override
        public IMessage onMessage(PacketChiselButton message, MessageContext ctx) {
            EntityPlayerMP player = ctx.getServerHandler().playerEntity;
            chiselAll(player, message.chiselSlot, message.target, message.slotIds);
            return null;
        }
    }
    
    public static void chiselAll(EntityPlayer player, int chiselSlot, ItemStack target, int[] slots) {
        if (player.openContainer instanceof ContainerChisel) {
            ContainerChisel container = (ContainerChisel) player.openContainer;
            ItemStack chisel = player.inventory.getStackInSlot(chiselSlot);
            if (chisel == null) {
                return;
            }
            for (int i : slots) {
                ItemStack stack = target.copy();
                int damageLeft = chisel.getMaxDamage() - chisel.getItemDamage() + 1;
                Optional.ofNullable(player.inventory.getStackInSlot(i)).ifPresent(s -> {
                    int toCraft = Math.min(s.stackSize, damageLeft);
                    stack.stackSize = toCraft;
                    player.inventory.setInventorySlotContents(i, stack);
                    chisel.damageItem(toCraft, player);
                    if (chisel.stackSize <= 0) {
                        container.getInventoryChisel().getStackInSpecialSlot().stackSize = s.stackSize - toCraft;
                        player.inventory.setInventorySlotContents(chiselSlot, null);
                        if (s.stackSize > toCraft) {
                            ItemStack remainder = s.copy();
                            remainder.stackSize = s.stackSize - toCraft;
                            if (!player.inventory.addItemStackToInventory(remainder)) {
                                player.dropItem(remainder, false);
                            }
                        }
                    }
                });
                if (chisel.stackSize < 1) {
                    return;
                }
            }
        }
    }
}
