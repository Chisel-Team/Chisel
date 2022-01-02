package team.chisel.common.inventory;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import net.minecraft.world.Container;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import team.chisel.api.IChiselItem;
import team.chisel.api.carving.CarvingUtils;
import team.chisel.api.carving.ICarvingVariation;
import team.chisel.common.util.SoundUtil;

public class SlotChiselSelection extends Slot {

    private final @Nonnull ChiselContainer container;

    public SlotChiselSelection(ChiselContainer container, InventoryChiselSelection inv, Container iinventory, int i, int j, int k) {
        super(iinventory, i, j, k);
        this.container = container;
    }

    @Override
    public boolean mayPlace(ItemStack itemstack) {
        return false;
    }

    @Override
    public boolean mayPickup(Player par1Player) {
        return par1Player.inventoryMenu.getCarried().isEmpty();
    }
    
    public static ItemStack craft(ChiselContainer container, Player player, ItemStack itemstack, boolean simulate) {
        ItemStack crafted = container.getInventoryChisel().getStackInSpecialSlot();
        ItemStack chisel = container.getChisel();
        if (simulate) {
            itemstack = itemstack.copy();
            crafted = crafted.isEmpty() ? ItemStack.EMPTY : crafted.copy();
            chisel = chisel.copy();
        }
        ItemStack res = ItemStack.EMPTY;
        if (!chisel.isEmpty() && !crafted.isEmpty()) {                
            IChiselItem item = (IChiselItem) container.getChisel().getItem();
            ICarvingVariation variation = CarvingUtils.getChiselRegistry().getVariation(itemstack.getItem()).orElseThrow(IllegalArgumentException::new);
            if (!item.canChisel(player.level, player, chisel, variation)) {
                return res;
            }
            res = item.craftItem(chisel, crafted, itemstack, player, p -> p.broadcastBreakEvent(container.getHand() == InteractionHand.MAIN_HAND ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND));
            if (!simulate) {
                container.getInventoryChisel().setStackInSpecialSlot(crafted.getCount() == 0 ? ItemStack.EMPTY : crafted);
                container.onChiselSlotChanged();
                item.onChisel(player.level, player, chisel, variation);
                if (chisel.getCount() == 0) {
                    container.getInventoryPlayer().setItem(container.getChiselSlot(), ItemStack.EMPTY);
                }
                if (!crafted.isEmpty() && !item.canChisel(player.level, player, chisel, variation)) {
                    container.onChiselBroken();
                }

                container.getInventoryChisel().updateItems();
                container.broadcastChanges();
            }
        }
        
        return res;
    }

    @Override
    public void onTake(Player player, ItemStack itemstack) {
        ItemStack chisel = container.getChisel().copy();
        ItemStack res = craft(container, player, itemstack, false);
        if (container.currentClickType != ClickType.PICKUP) {
            res.shrink(1);
        }
        if (!res.isEmpty()) {
            SoundUtil.playSound(player, chisel, itemstack);
            player.getInventory().setPickedItem(res);
        }
        return;
    }
}