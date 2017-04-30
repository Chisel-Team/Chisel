package team.chisel.client.gui;

import javax.annotation.Nonnull;

import io.netty.buffer.ByteBuf;
import lombok.NoArgsConstructor;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import team.chisel.common.util.NBTUtil;

@NoArgsConstructor
public class PacketHitechSettings implements IMessage {
    
    private byte type;
    private int selection, target;
    private boolean rotate;
    
    private int chiselSlot;

    public PacketHitechSettings(@Nonnull ItemStack stack, int chiselSlot) {
        this.type = (byte) NBTUtil.getHitechType(stack).ordinal();
        this.selection = NBTUtil.getHitechSelection(stack);
        this.target = NBTUtil.getHitechTarget(stack);
        this.rotate = NBTUtil.getHitechRotate(stack);

        this.chiselSlot = chiselSlot;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeByte(type);
        buf.writeInt(selection);
        buf.writeInt(target);
        buf.writeBoolean(rotate);
        buf.writeByte(chiselSlot);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.type = buf.readByte();
        this.selection = buf.readInt();
        this.target = buf.readInt();
        this.rotate = buf.readBoolean();
        this.chiselSlot = buf.readByte();
    }

    public static class Handler implements IMessageHandler<PacketHitechSettings, IMessage> {

        @Override
        public IMessage onMessage(PacketHitechSettings message, MessageContext ctx) {
            ItemStack stack = ctx.getServerHandler().playerEntity.inventory.getStackInSlot(message.chiselSlot);
            if (stack != null) {
                NBTUtil.setHitechType(stack, message.type);
                NBTUtil.setHitechSelection(stack, message.selection);
                NBTUtil.setHitechTarget(stack, message.target);
                NBTUtil.setHitechRotate(stack, message.rotate);
            }
            return null;
        }
    }
}
