package team.chisel.common.block;

import java.util.function.Supplier;

import javax.annotation.Nonnull;

import lombok.RequiredArgsConstructor;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkEvent;
import team.chisel.client.ClientProxy;

@RequiredArgsConstructor
public class MessageUpdateAutochiselSource {
    
    private final @Nonnull BlockPos pos;
    private final @Nonnull ItemStack stack;
    
    public void encode(PacketBuffer buf) {
        buf.writeLong(pos.toLong());
        buf.writeItemStack(stack);
    }

    public static MessageUpdateAutochiselSource decode(PacketBuffer buf) {
        return new MessageUpdateAutochiselSource(BlockPos.fromLong(buf.readLong()), buf.readItemStack());
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            World world = ClientProxy.getClientWorld();
            if (world.isBlockLoaded(pos)) {
                TileEntity te = world.getTileEntity(pos);
                if (te instanceof TileAutoChisel) {
                    ((TileAutoChisel) te).setSource(stack);
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
