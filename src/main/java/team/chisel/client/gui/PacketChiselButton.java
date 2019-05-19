package team.chisel.client.gui;

import java.util.Optional;

import javax.annotation.Nonnull;

import io.netty.buffer.ByteBuf;
import lombok.NoArgsConstructor;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import team.chisel.api.IChiselItem;
import team.chisel.api.carving.CarvingUtils;
import team.chisel.api.carving.ICarvingRegistry;
import team.chisel.common.inventory.ContainerChiselHitech;
import team.chisel.common.inventory.SlotChiselSelection;
import team.chisel.common.util.SoundUtil;

@NoArgsConstructor
public class PacketChiselButton implements IMessage {

    private int[] slotIds;

    public PacketChiselButton(int... slots) {
        this.slotIds = slots;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeByte(slotIds.length);
        for (int i : slotIds) {
            buf.writeByte(i);
        }
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        int len = buf.readByte();
        slotIds = new int[len];
        for (int i = 0; i < slotIds.length; i++) {
            slotIds[i] = buf.readByte();
        }
    }

    public static class Handler implements IMessageHandler<PacketChiselButton, IMessage> {

        @Override
        public IMessage onMessage(PacketChiselButton message, MessageContext ctx) {
            EntityPlayerMP player = ctx.getServerHandler().player;
            ctx.getServerHandler().player.getServerWorld().addScheduledTask(
                    () -> chiselAll(player, message.slotIds)
            );
            return null;
        }
    }
    
    public static void chiselAll(EntityPlayer player, int[] slots) {
        if (player.openContainer instanceof ContainerChiselHitech) {
            ContainerChiselHitech container = (ContainerChiselHitech) player.openContainer;
            ItemStack chisel = container.getChisel();
            ItemStack originalChisel = chisel.copy();
            ItemStack target = container.getTargetStack();
            
            if (!(chisel.getItem() instanceof IChiselItem)) {
                return;
            }
            
            @SuppressWarnings("null")
            @Nonnull
            ICarvingRegistry carving = CarvingUtils.getChiselRegistry();

            if (chisel.isEmpty() || target.isEmpty()) {
                return;
            }
            
            boolean playSound = false;

            for (int i : slots) {
                ItemStack s = player.inventory.getStackInSlot(i);
                if (!s.isEmpty()) {
                    if (carving.getGroup(target) != carving.getGroup(s)) {
                        return;
                    }
                    container.getInventoryChisel().setStackInSpecialSlot(s);
                    ItemStack res = SlotChiselSelection.craft(container, player, target.copy(), false);
                    if (!res.isEmpty()) {
                        player.inventory.setInventorySlotContents(i, res);
                        playSound = true;
                    }
                }
                if (chisel.isEmpty()) {
                    return;
                }
            }
            
            container.getInventoryChisel().setStackInSpecialSlot(container.getSelectionStack());
            container.getInventoryChisel().updateItems();
            
            if (playSound) {
                SoundUtil.playSound(player, originalChisel, target);
            }
        }
    }
}
