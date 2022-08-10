package team.chisel.common.block;

import lombok.RequiredArgsConstructor;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.network.NetworkEvent;
import team.chisel.client.ClientProxy;

import javax.annotation.Nonnull;
import java.util.function.Supplier;

@RequiredArgsConstructor
public class MessageUpdateAutochiselSource {

    private final @Nonnull BlockPos pos;
    private final @Nonnull ItemStack stack;

    public static MessageUpdateAutochiselSource decode(FriendlyByteBuf buf) {
        return new MessageUpdateAutochiselSource(BlockPos.of(buf.readLong()), buf.readItem());
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeLong(pos.asLong());
        buf.writeItem(stack);
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            Level world = ClientProxy.getClientWorld();
            if (world.hasChunkAt(pos)) {
                BlockEntity te = world.getBlockEntity(pos);
                if (te instanceof TileAutoChisel) {
                    ((TileAutoChisel) te).setSource(stack);
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
