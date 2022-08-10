package team.chisel.client.gui;

import java.util.function.Supplier;

import javax.annotation.Nonnull;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;
import team.chisel.api.IChiselItem;
import team.chisel.api.carving.CarvingUtils;
import team.chisel.api.carving.ICarvingGroup;
import team.chisel.api.carving.IVariationRegistry;
import team.chisel.common.inventory.ContainerChiselHitech;
import team.chisel.common.inventory.SlotChiselSelection;
import team.chisel.common.util.SoundUtil;

public class PacketChiselButton {

    @Nonnull
    private final int[] slotIds;

    public PacketChiselButton(int... slots) {
        this.slotIds = slots;
    }
    
    public void encode(final FriendlyByteBuf buf) {
        buf.writeVarIntArray(slotIds);
    }
    
    public static PacketChiselButton decode(final FriendlyByteBuf buf) {
        return new PacketChiselButton(buf.readVarIntArray());
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ServerPlayer player = ctx.get().getSender();
        ctx.get().enqueueWork(() -> chiselAll(player, slotIds));
        ctx.get().setPacketHandled(true);
    }
    
    public static void chiselAll(Player player, int[] slots) {
        if (player.containerMenu instanceof ContainerChiselHitech) {
            ContainerChiselHitech container = (ContainerChiselHitech) player.containerMenu;
            ItemStack chisel = container.getChisel();
            ItemStack originalChisel = chisel.copy();
            ItemStack target = container.getTargetItem();
            
            if (!(chisel.getItem() instanceof IChiselItem)) {
                return;
            }
            
            @SuppressWarnings("null")
            @Nonnull
            IVariationRegistry carving = CarvingUtils.getChiselRegistry();

            if (chisel.isEmpty() || target.isEmpty()) {
                return;
            }
            
            boolean playSound = false;

            for (int i : slots) {
                ItemStack s = player.getInventory().getItem(i);
                if (!s.isEmpty()) {
                    if (!carving.getGroup(target.getItem())
                            .map(ICarvingGroup::getId)
                            .flatMap(id -> carving.getGroup(s.getItem()).map(g -> g.getId().equals(id)))
                            .orElse(false)) {
                        return;
                    }
                    container.getInventoryChisel().setStackInSpecialSlot(s);
                    ItemStack res = SlotChiselSelection.craft(container, player, target.copy(), false);
                    if (!res.isEmpty()) {
                        player.getInventory().setItem(i, s);
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
