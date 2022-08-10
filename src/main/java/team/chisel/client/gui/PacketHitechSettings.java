package team.chisel.client.gui;

import io.netty.buffer.ByteBuf;
import lombok.RequiredArgsConstructor;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;
import team.chisel.api.IChiselItem;
import team.chisel.common.util.NBTUtil;

import javax.annotation.Nonnull;
import java.util.function.Supplier;

@RequiredArgsConstructor
public class PacketHitechSettings {

    private final byte type;
    private final boolean rotate;

    private final int chiselSlot;

    public PacketHitechSettings(@Nonnull ItemStack stack, int chiselSlot) {
        this.type = (byte) NBTUtil.getHitechType(stack).ordinal();
        this.rotate = NBTUtil.getHitechRotate(stack);

        this.chiselSlot = chiselSlot;
    }

    public static PacketHitechSettings decode(ByteBuf buf) {
        return new PacketHitechSettings(
                buf.readByte(),
                buf.readBoolean(),
                buf.readByte());
    }

    public void encode(ByteBuf buf) {
        buf.writeByte(type);
        buf.writeBoolean(rotate);
        buf.writeByte(chiselSlot);
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ItemStack stack = ctx.get().getSender().getInventory().getItem(chiselSlot);
        if (stack.getItem() instanceof IChiselItem) { // instanceof check for broken chisel
            NBTUtil.setHitechType(stack, type);
            NBTUtil.setHitechRotate(stack, rotate);
        }
        ctx.get().setPacketHandled(true);
    }
}
