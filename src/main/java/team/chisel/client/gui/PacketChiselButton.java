package team.chisel.client.gui;

import java.util.Optional;

import io.netty.buffer.ByteBuf;
import lombok.NoArgsConstructor;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

@NoArgsConstructor
public class PacketChiselButton implements IMessage {

    private ItemStack target;
    private int[] slotIds;

    public PacketChiselButton(ItemStack target, int... slots) {
        this.target = target;
        this.slotIds = slots;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeItemStack(buf, target);
        buf.writeByte(slotIds.length);
        for (int i : slotIds) {
            buf.writeByte(i);
        }
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        target = ByteBufUtils.readItemStack(buf);
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
            for (int i : message.slotIds) {
                ItemStack stack = message.target.copy();
                Optional.ofNullable(player.inventory.getStackInSlot(i)).ifPresent(s -> {
                    stack.stackSize = s.stackSize;
                    player.inventory.setInventorySlotContents(i, stack);
                });
            }
            return null;
        }
    }
}
