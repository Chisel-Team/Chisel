package team.chisel.common.inventory;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;
import team.chisel.api.IChiselItem;
import team.chisel.api.carving.CarvingUtils;
import team.chisel.common.init.ChiselTileEntities;

public class ContainerAutoChisel extends AbstractContainerMenu {

    private static class ChiselableSlot extends SlotItemHandler {

        public ChiselableSlot(IItemHandler inventoryIn, int index, int xPosition, int yPosition) {
            super(inventoryIn, index, xPosition, yPosition);
        }

        @Override
        public boolean mayPlace(@Nullable ItemStack stack) {
            return stack != null && CarvingUtils.getChiselRegistry().getGroup(stack.getItem()).isPresent();
        }
    }

    public final Inventory invPlayer;
    
    private final int beginInputSlots, endInputSlots;
    public  final int chiselSlot, targetSlot;
    private final int beginOutputSlots, endOutputSlots;
    private final int beginPlayerSlots, endPlayerSlots;
    
    private final ContainerData progressAndPower;
    private final ContainerLevelAccess pos;
    
    public ContainerAutoChisel(MenuType<ContainerAutoChisel> type, int windowId, Inventory invPlayer) {
        this(type, windowId, invPlayer, new ItemStackHandler(12), new ItemStackHandler(12), new ItemStackHandler(2), new SimpleContainerData(6), ContainerLevelAccess.NULL);
    }

    public ContainerAutoChisel(MenuType<ContainerAutoChisel> type, int windowId, Inventory invPlayer, IItemHandler input, IItemHandler output, IItemHandler other, ContainerData progressAndPower, ContainerLevelAccess pos) {
        super(type, windowId);
        this.invPlayer = invPlayer;

        int yStart = 19;
        chiselSlot = 0;
        addSlot(new SlotItemHandler(other, 0, 80, yStart + 9) {

            @Override
            public boolean mayPlace(@Nullable ItemStack stack) {
                return stack != null && stack.getItem() instanceof IChiselItem;
            }
        });
        targetSlot = 1;
        addSlot(new ChiselableSlot(other, 1, 80, 54 + yStart - 9));

        beginInputSlots = slots.size();
        for (int i = 0; i < input.getSlots(); i++) {
            addSlot(new ChiselableSlot(input, i, 8 + 18 * (i % 3), yStart + 18 * (i / 3)));
        }
        endInputSlots = beginOutputSlots = slots.size();
        for (int i = 0; i < output.getSlots(); i++) {
            addSlot(new SlotItemHandler(output, i, 8 + 108 + 18 * (i % 3), yStart + 18 * (i / 3)) {

                @Override
                public boolean mayPlace(@Nullable ItemStack stack) {
                    return false;
                }
            });
        }
        
        beginPlayerSlots = endOutputSlots = slots.size();

        for (int r = 0; r < 3; ++r) {
            for (int c = 0; c < 9; ++c) {
                addSlot(new Slot(invPlayer, c + r * 9 + 9, 8 + c * 18, 118 + r * 18));
            }
        }

        for (int i = 0; i < 9; ++i) {
            addSlot(new Slot(invPlayer, i, 8 + i * 18, 176));
        }
        
        endPlayerSlots = slots.size();
        
        checkContainerDataCount(progressAndPower, 2);
        addDataSlots(progressAndPower);
        
        this.progressAndPower = progressAndPower;
        this.pos = pos;
    }

    @Override
    public boolean stillValid(Player playerIn) {
        return stillValid(pos, playerIn, ChiselTileEntities.AUTO_CHISEL.get()); // TODO temporary
    }

    @Override
    @Nonnull
    public ItemStack quickMoveStack(Player playerIn, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = (Slot) this.slots.get(index);

        if (slot != null && slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();

            if (index >= beginOutputSlots && index < endOutputSlots) {
                if (!this.moveItemStackTo(itemstack1, beginPlayerSlots, endPlayerSlots, true)) {
                    return ItemStack.EMPTY;
                }

                slot.onQuickCraft(itemstack1, itemstack);
            } else if (index >= beginPlayerSlots) {
                if (CarvingUtils.getChiselRegistry().getGroup(itemstack1.getItem()).isPresent()) {
                    if (!this.moveItemStackTo(itemstack1, targetSlot, targetSlot + 1, false)) {
                        if (!this.moveItemStackTo(itemstack1, beginInputSlots, endInputSlots, false)) {
                            return ItemStack.EMPTY;
                        }
                    }
                } else if (itemstack1.getItem() instanceof IChiselItem) {
                    if (!this.moveItemStackTo(itemstack1, chiselSlot, chiselSlot + 1, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (index >= beginPlayerSlots && index < endPlayerSlots - 9) {
                    if (!this.moveItemStackTo(itemstack1, endPlayerSlots - 9, endPlayerSlots, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (index >= endPlayerSlots - 9 && index < endPlayerSlots && !this.moveItemStackTo(itemstack1, 3, 30, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(itemstack1, beginPlayerSlots, endPlayerSlots, false)) {
                return ItemStack.EMPTY;
            }

            if (itemstack1.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }

            if (itemstack1.getCount() == itemstack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(playerIn, itemstack1);
        }

        return itemstack;
    }
    
    public static final int 
            ACTIVE = 0,
            PROGRESS = 1,
            MAX_PROGRESS = 2,
            ENERGY = 3,
            MAX_ENERGY = 4,
            ENERGY_USE = 5;

    public boolean isActive() {
        return progressAndPower.get(ACTIVE) > 0;
    }
    
    public int getProgressScaled(int progBarLength) {
        return (int) (((float) progressAndPower.get(PROGRESS) / progressAndPower.get(MAX_PROGRESS)) * progBarLength);
    }

    public boolean hasEnergy() {
        return getEnergy() > 0;
    }
    
    public int getEnergy() {
        return progressAndPower.get(ENERGY);
    }
    
    public int getMaxEnergy() {
        return progressAndPower.get(MAX_ENERGY);
    }
    
    public int getEnergyScaled(int progBarLength) {
        return (int) (((float) progressAndPower.get(ENERGY) / progressAndPower.get(MAX_ENERGY)) * progBarLength);
    }

    public int getUsagePerTick() {
        return progressAndPower.get(ENERGY_USE);
    }
}
