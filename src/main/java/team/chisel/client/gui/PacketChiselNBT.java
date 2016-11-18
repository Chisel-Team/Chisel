package team.chisel.client.gui;

import javax.annotation.Nonnull;

import io.netty.buffer.ByteBuf;
import lombok.NoArgsConstructor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import team.chisel.common.util.NBTUtil;

@NoArgsConstructor
public class PacketChiselNBT implements IMessage {

    private @Nonnull NBTTagCompound tag;
    private int chiselSlot;

    public PacketChiselNBT(@Nonnull NBTTagCompound tag, int chiselSlot) {
        this.tag = tag;
        this.chiselSlot = chiselSlot;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeTag(buf, tag);
        buf.writeByte(chiselSlot);
    }

    @SuppressWarnings("null")
    @Override
    public void fromBytes(ByteBuf buf) {
        this.tag = ByteBufUtils.readTag(buf);
        this.chiselSlot = buf.readByte();
    }

    public static class Handler implements IMessageHandler<PacketChiselNBT, IMessage> {

        @Override
        public IMessage onMessage(PacketChiselNBT message, MessageContext ctx) {
            ItemStack stack = ctx.getServerHandler().playerEntity.inventory.getStackInSlot(message.chiselSlot);
            if (!stack.isEmpty()) {
                NBTUtil.setChiselTag(stack, message.tag);
            }
            return null;
        }
    }
}
