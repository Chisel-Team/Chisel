package team.chisel.client.gui;

import java.util.function.Supplier;

import javax.annotation.Nonnull;

import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;
import team.chisel.api.IChiselItem;
import team.chisel.api.carving.CarvingUtils;
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
    
    public void encode(final PacketBuffer buf) {
        buf.writeVarIntArray(slotIds);
    }
    
    public static PacketChiselButton decode(final PacketBuffer buf) {
        return new PacketChiselButton(buf.readVarIntArray());
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ServerPlayerEntity player = ctx.get().getSender();
        ctx.get().enqueueWork(() -> chiselAll(player, slotIds));
        ctx.get().setPacketHandled(true);
    }
    
    public static void chiselAll(PlayerEntity player, int[] slots) {
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
            IVariationRegistry carving = CarvingUtils.getChiselRegistry();

            if (chisel.isEmpty() || target.isEmpty()) {
                return;
            }
            
            boolean playSound = false;

            for (int i : slots) {
                ItemStack s = player.inventory.getStackInSlot(i);
                if (!s.isEmpty()) {
                    if (carving.getGroup(target.getItem()) != carving.getGroup(s.getItem())) {
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
