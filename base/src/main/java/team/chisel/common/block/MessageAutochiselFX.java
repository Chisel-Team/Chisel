package team.chisel.common.block;

import java.util.function.Supplier;

import javax.annotation.Nonnull;

import lombok.RequiredArgsConstructor;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkEvent;
import team.chisel.client.ClientProxy;

@RequiredArgsConstructor
public class MessageAutochiselFX {
    
    private final @Nonnull BlockPos pos;
    private final @Nonnull ItemStack chisel;
    private final @Nonnull BlockState state;
    
    public void encode(PacketBuffer buf) {
        buf.writeLong(pos.toLong());
        buf.writeItemStack(chisel);
        buf.writeInt(Block.getStateId(state));
    }

    public static MessageAutochiselFX decode(PacketBuffer buf) {
        return new MessageAutochiselFX(BlockPos.fromLong(buf.readLong()), buf.readItemStack(), Block.getStateById(buf.readInt()));
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            World world = ClientProxy.getClientWorld();
            if (world.isBlockLoaded(pos)) {
                TileEntity te = world.getTileEntity(pos);
                if (te instanceof TileAutoChisel) {
                    ((TileAutoChisel) te).spawnCompletionFX(ClientProxy.getClientPlayer(), chisel, state);
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
