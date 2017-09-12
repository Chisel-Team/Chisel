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
import team.chisel.api.carving.CarvingUtils;
import team.chisel.api.carving.ICarvingRegistry;
import team.chisel.common.inventory.ContainerChiselHitech;

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
            ItemStack target = container.getTargetStack();

            @SuppressWarnings("null")
            @Nonnull
            ICarvingRegistry carving = CarvingUtils.getChiselRegistry();

            if (chisel.isEmpty() || target.isEmpty()) {
                return;
            }

            for (int i : slots) {
                Optional.ofNullable(player.inventory.getStackInSlot(i)).ifPresent((@Nonnull ItemStack s) -> {
                    if (carving.getGroup(target) != carving.getGroup(s)) {
                        return;
                    }
                    ItemStack stack = target.copy();
                    int toCraft = Math.min(s.getCount(), stack.getMaxStackSize());
                    if (chisel.isItemStackDamageable()) {
                        int damageLeft = chisel.getMaxDamage() - chisel.getItemDamage() + 1;
                        toCraft = Math.min(toCraft, damageLeft);
                        stack.setCount(toCraft);
                        chisel.damageItem(toCraft, player);
                    }
                    if (chisel.isEmpty()) {
                        player.inventory.setInventorySlotContents(i, stack);
                        container.getInventoryChisel().getStackInSpecialSlot().shrink(toCraft);
                        player.inventory.setInventorySlotContents(container.getChiselSlot(), null);
                        if (s.getCount() > toCraft) {
                            ItemStack remainder = s.copy();
                            remainder.shrink(toCraft);
                            if (!player.inventory.addItemStackToInventory(remainder)) {
                                player.dropItem(remainder, false);
                            }
                        }
                    } else if (toCraft < s.getCount()) {
                        s.shrink(toCraft);
                        if (!player.inventory.addItemStackToInventory(stack)) {
                            player.dropItem(stack, false);
                        }
                    } else {
                        player.inventory.setInventorySlotContents(i, stack);
                    }
                });
                if (chisel.getCount() < 1) {
                    return;
                }
            }
        }
    }
}
