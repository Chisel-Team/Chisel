package team.chisel.common.item;

import java.util.function.Supplier;

import javax.annotation.Nonnull;

import lombok.RequiredArgsConstructor;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;
import team.chisel.api.IChiselItem;
import team.chisel.api.carving.CarvingUtils;
import team.chisel.api.carving.IChiselMode;
import team.chisel.common.util.NBTUtil;

@RequiredArgsConstructor
public class PacketChiselMode {
    
    private final int slot;
    private final @Nonnull IChiselMode mode;

    public void encode(PacketBuffer buf) {
        buf.writeInt(this.slot);
        buf.writeString(this.mode.name(), 256);
    }

    @SuppressWarnings({ "null", "unused" })
    public static PacketChiselMode decode(PacketBuffer buf) {
        int slot = buf.readInt();
        IChiselMode mode = CarvingUtils.getModeRegistry().getModeByName(buf.readString(256));
        if (mode == null) {
            mode = ChiselMode.SINGLE;
        }
        return new PacketChiselMode(slot, mode);
    }
    
    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ItemStack stack = ctx.get().getSender().inventory.getStackInSlot(slot);
            if (stack.getItem() instanceof IChiselItem && ((IChiselItem) stack.getItem()).supportsMode(ctx.get().getSender(), stack, mode)) {
                NBTUtil.setChiselMode(stack, mode);
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
