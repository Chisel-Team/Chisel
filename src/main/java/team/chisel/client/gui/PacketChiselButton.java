package team.chisel.client.gui;

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
            if (chisel.isEmpty()) {
                return;
            }
            for (int i : slots) {
                Optional.ofNullable(player.inventory.getStackInSlot(i)).ifPresent(s -> {
                    ItemStack stack = target.copy();
                    int toCraft = s.getCount();
                    if (chisel.isItemStackDamageable()) {
                        int damageLeft = chisel.getMaxDamage() - chisel.getItemDamage() + 1;
                        toCraft = Math.min(toCraft, damageLeft);
                        stack.setCount(toCraft);
                        chisel.damageItem(toCraft, player);
                    }
                    player.inventory.setInventorySlotContents(i, stack);
                    if (chisel.getCount() <= 0) {
                        container.getInventoryChisel().getStackInSpecialSlot().shrink(toCraft);
                        player.inventory.setInventorySlotContents(chiselSlot, ItemStack.EMPTY);
                        if (s.getCount() > toCraft) {
                            ItemStack remainder = s.copy();
                            remainder.shrink(toCraft);
                            if (!player.inventory.addItemStackToInventory(remainder)) {
                                player.dropItem(remainder, false);
                            }
                        }
                    }
                });
                if (chisel.getCount() < 1) {
                    return;
                }
            }
        }
    }
}
