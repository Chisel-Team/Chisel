package team.chisel.common.item;

import javax.annotation.Nonnull;

import io.netty.buffer.ByteBuf;
import lombok.NoArgsConstructor;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import team.chisel.api.IChiselItem;
import team.chisel.api.carving.CarvingUtils;
import team.chisel.api.carving.IChiselMode;
import team.chisel.common.util.NBTUtil;

@NoArgsConstructor
public class PacketChiselMode implements IMessage {
    
    private int slot;
    private @Nonnull IChiselMode mode;
    
    public PacketChiselMode(int slot, @Nonnull IChiselMode mode) {
        this.slot = slot;
        this.mode = mode;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(this.slot);
        ByteBufUtils.writeUTF8String(buf, this.mode.name());
    }

    @SuppressWarnings({ "null", "unused" })
    @Override
    public void fromBytes(ByteBuf buf) {
        this.slot = buf.readInt();
        this.mode = CarvingUtils.getModeRegistry().getModeByName(ByteBufUtils.readUTF8String(buf));
        if (this.mode == null) {
            this.mode = ChiselMode.SINGLE;
        }
    }

    public static class Handler implements IMessageHandler<PacketChiselMode, IMessage> {
        
        @Override
        public IMessage onMessage(PacketChiselMode message, MessageContext ctx) {
            ctx.getServerHandler().player.getServer().addScheduledTask(() -> {
                ItemStack stack = ctx.getServerHandler().player.inventory.getStackInSlot(message.slot);
                if (stack.getItem() instanceof IChiselItem && ((IChiselItem) stack.getItem()).supportsMode(ctx.getServerHandler().player, stack, message.mode)) {
                    NBTUtil.setChiselMode(stack, message.mode);
                }
            });
            return null;
        }
    }
}
