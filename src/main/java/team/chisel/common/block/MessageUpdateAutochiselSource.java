package team.chisel.common.block;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import team.chisel.Chisel;

@NoArgsConstructor
@AllArgsConstructor
public class MessageUpdateAutochiselSource implements IMessage {
    
    private @Nonnull BlockPos pos = BlockPos.ORIGIN;
    private @Nullable ItemStack stack;
    
    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeLong(pos.toLong());
        ByteBufUtils.writeItemStack(buf, stack);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.pos = BlockPos.fromLong(buf.readLong());
        this.stack = ByteBufUtils.readItemStack(buf);
    }
    
    public static class Handler implements IMessageHandler<MessageUpdateAutochiselSource, IMessage> {
        
        @Override
        public IMessage onMessage(MessageUpdateAutochiselSource message, MessageContext ctx) {
            FMLCommonHandler.instance().getWorldThread(ctx.getClientHandler()).addScheduledTask(() -> {
                World world = Chisel.proxy.getClientWorld();
                if (world.isBlockLoaded(message.pos)) {
                    TileEntity te = world.getTileEntity(message.pos);
                    if (te instanceof TileAutoChisel) {
                        ((TileAutoChisel) te).setSource(message.stack);
                    }
                }
            });
            return null;
        }
    }
}
