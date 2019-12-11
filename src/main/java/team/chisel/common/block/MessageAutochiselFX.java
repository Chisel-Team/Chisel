package team.chisel.common.block;

import javax.annotation.Nonnull;

import org.apache.commons.lang3.Validate;

import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import net.minecraft.item.Item;
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
import team.chisel.api.carving.CarvingUtils;
import team.chisel.common.init.ChiselItems;

@NoArgsConstructor
@AllArgsConstructor
public class MessageAutochiselFX implements IMessage {
    
    private @Nonnull BlockPos pos = BlockPos.ORIGIN;
    private @Nonnull ItemStack chisel = new ItemStack(ChiselItems.chisel_iron);
    private @Nonnull Item chiselitem = ChiselItems.chisel_iron;
    private @Nonnull ItemStack source;
    
    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeLong(pos.toLong());
        ByteBufUtils.writeItemStack(buf, chisel);
        buf.writeInt(Item.getIdFromItem(chiselitem));
        ByteBufUtils.writeItemStack(buf, source);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.pos = BlockPos.fromLong(buf.readLong());
        ItemStack chisel = ByteBufUtils.readItemStack(buf);
        Validate.notNull(chisel);
        this.chisel = chisel;
        this.chiselitem = Item.getItemById(buf.readInt());
        this.source = ByteBufUtils.readItemStack(buf);
    }
    
    public static class Handler implements IMessageHandler<MessageAutochiselFX, IMessage> {
        
        @Override
        public IMessage onMessage(MessageAutochiselFX message, MessageContext ctx) {
            FMLCommonHandler.instance().getWorldThread(ctx.getClientHandler()).addScheduledTask(() -> {
                World world = Chisel.proxy.getClientWorld();
                if (world.isBlockLoaded(message.pos)) {
                    TileEntity te = world.getTileEntity(message.pos);
                    if (te instanceof TileAutoChisel) {
                        ((TileAutoChisel) te).spawnCompletionFX(Chisel.proxy.getClientPlayer(), message.chisel, message.chiselitem, CarvingUtils.getChiselRegistry().getVariation(message.source));
                    }
                }
            });
            return null;
        }
    }
}
