package team.chisel.client.gui;

import java.util.function.Supplier;

import javax.annotation.Nonnull;

import io.netty.buffer.ByteBuf;
import lombok.RequiredArgsConstructor;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.network.NetworkEvent;
import team.chisel.api.IChiselItem;
import team.chisel.common.util.NBTUtil;

@RequiredArgsConstructor
public class PacketHitechSettings {
    
    private final byte type;
    private final int selection, target;
    private final boolean rotate;
    
    private final int chiselSlot;

    public PacketHitechSettings(@Nonnull ItemStack stack, int chiselSlot) {
        this.type = (byte) NBTUtil.getHitechType(stack).ordinal();
        this.selection = NBTUtil.getHitechSelection(stack);
        this.target = NBTUtil.getHitechTarget(stack);
        this.rotate = NBTUtil.getHitechRotate(stack);

        this.chiselSlot = chiselSlot;
    }

    public void encode(ByteBuf buf) {
        buf.writeByte(type);
        buf.writeInt(selection);
        buf.writeInt(target);
        buf.writeBoolean(rotate);
        buf.writeByte(chiselSlot);
    }

    public static PacketHitechSettings decode(ByteBuf buf) {
        return new PacketHitechSettings(
                buf.readByte(),
                buf.readInt(),
                buf.readInt(),
                buf.readBoolean(),
                buf.readByte());
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ItemStack stack = ctx.get().getSender().inventory.getStackInSlot(chiselSlot);
        if (stack.getItem() instanceof IChiselItem) { // instanceof check for broken chisel
            NBTUtil.setHitechType(stack, type);
            NBTUtil.setHitechSelection(stack, selection);
            NBTUtil.setHitechTarget(stack, target);
            NBTUtil.setHitechRotate(stack, rotate);
        }
        ctx.get().setPacketHandled(true);
    }
}
