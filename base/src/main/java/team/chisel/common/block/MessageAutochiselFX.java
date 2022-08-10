package team.chisel.common.block;

import lombok.RequiredArgsConstructor;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.network.NetworkEvent;
import team.chisel.client.ClientProxy;

import javax.annotation.Nonnull;
import java.util.function.Supplier;

@RequiredArgsConstructor
public class MessageAutochiselFX {

    private final @Nonnull BlockPos pos;
    private final @Nonnull ItemStack chisel;
    private final @Nonnull BlockState state;

    public static MessageAutochiselFX decode(FriendlyByteBuf buf) {
        return new MessageAutochiselFX(BlockPos.of(buf.readLong()), buf.readItem(), Block.stateById(buf.readInt()));
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeLong(pos.asLong());
        buf.writeItem(chisel);
        buf.writeInt(Block.getId(state));
    }

    @SuppressWarnings("deprecation")
    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            Level world = ClientProxy.getClientWorld();
            if (world.hasChunkAt(pos)) {
                BlockEntity te = world.getBlockEntity(pos);
                if (te instanceof TileAutoChisel) {
                    ((TileAutoChisel) te).spawnCompletionFX(ClientProxy.getClientPlayer(), chisel, state);
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
