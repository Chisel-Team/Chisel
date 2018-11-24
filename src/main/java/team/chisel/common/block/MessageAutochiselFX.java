package team.chisel.common.block;

import javax.annotation.Nonnull;

import org.apache.commons.lang3.Validate;

import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
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
public class MessageAutochiselFX implements IMessage {
    
    private @Nonnull BlockPos pos = BlockPos.ORIGIN;
    private @Nonnull ItemStack chisel = new ItemStack(Chisel.itemChiselIron);
    private @Nonnull IBlockState state = Blocks.AIR.getDefaultState();
    
    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeLong(pos.toLong());
        ByteBufUtils.writeItemStack(buf, chisel);
        buf.writeInt(Block.getStateId(state));
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.pos = BlockPos.fromLong(buf.readLong());
        ItemStack chisel = ByteBufUtils.readItemStack(buf);
        Validate.notNull(chisel);
        this.chisel = chisel;
        this.state = Block.getStateById(buf.readInt());
    }
    
    public static class Handler implements IMessageHandler<MessageAutochiselFX, IMessage> {
        
        @Override
        public IMessage onMessage(MessageAutochiselFX message, MessageContext ctx) {
            FMLCommonHandler.instance().getWorldThread(ctx.getClientHandler()).addScheduledTask(() -> {
                World world = Chisel.proxy.getClientWorld();
                if (world.isBlockLoaded(message.pos)) {
                    TileEntity te = world.getTileEntity(message.pos);
                    if (te instanceof TileAutoChisel) {
                        ((TileAutoChisel) te).spawnCompletionFX(Chisel.proxy.getClientPlayer(), message.chisel, message.state);
                    }
                }
            });
            return null;
        }
    }
}
