package team.chisel.common.inventory;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIntArray;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.IntArray;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;
import team.chisel.Chisel;
import team.chisel.api.IChiselItem;
import team.chisel.api.carving.CarvingUtils;
import team.chisel.common.init.ChiselTileEntities;

@ParametersAreNonnullByDefault
public class ContainerAutoChisel extends Container {

    private static class ChiselableSlot extends SlotItemHandler {

        public ChiselableSlot(IItemHandler inventoryIn, int index, int xPosition, int yPosition) {
            super(inventoryIn, index, xPosition, yPosition);
        }

        @Override
        public boolean isItemValid(@Nullable ItemStack stack) {
            return stack != null && CarvingUtils.getChiselRegistry().getGroup(stack.getItem()).isPresent();
        }
    }

    public final PlayerInventory invPlayer;
    
    private final int beginInputSlots, endInputSlots;
    public  final int chiselSlot, targetSlot;
    private final int beginOutputSlots, endOutputSlots;
    private final int beginPlayerSlots, endPlayerSlots;
    
    private final IIntArray progressAndPower;
    private final IWorldPosCallable pos;
    
    public ContainerAutoChisel(ContainerType<ContainerAutoChisel> type, int windowId, PlayerInventory invPlayer) {
        this(type, windowId, invPlayer, new ItemStackHandler(12), new ItemStackHandler(12), new ItemStackHandler(2), new IntArray(6), IWorldPosCallable.DUMMY);
    }

    public ContainerAutoChisel(ContainerType<ContainerAutoChisel> type, int windowId, PlayerInventory invPlayer, IItemHandler input, IItemHandler output, IItemHandler other, IIntArray progressAndPower, IWorldPosCallable pos) {
        super(type, windowId);
        this.invPlayer = invPlayer;

        int yStart = 19;
        chiselSlot = 0;
        addSlot(new SlotItemHandler(other, 0, 80, yStart + 9) {

            @Override
            public boolean isItemValid(@Nullable ItemStack stack) {
                return stack != null && stack.getItem() instanceof IChiselItem;
            }
        });
        targetSlot = 1;
        addSlot(new ChiselableSlot(other, 1, 80, 54 + yStart - 9));

        beginInputSlots = inventorySlots.size();
        for (int i = 0; i < input.getSlots(); i++) {
            addSlot(new ChiselableSlot(input, i, 8 + 18 * (i % 3), yStart + 18 * (i / 3)));
        }
        endInputSlots = beginOutputSlots = inventorySlots.size();
        for (int i = 0; i < output.getSlots(); i++) {
            addSlot(new SlotItemHandler(output, i, 8 + 108 + 18 * (i % 3), yStart + 18 * (i / 3)) {

                @Override
                public boolean isItemValid(@Nullable ItemStack stack) {
                    return false;
                }
            });
        }
        
        beginPlayerSlots = endOutputSlots = inventorySlots.size();

        for (int r = 0; r < 3; ++r) {
            for (int c = 0; c < 9; ++c) {
                addSlot(new Slot(invPlayer, c + r * 9 + 9, 8 + c * 18, 118 + r * 18));
            }
        }

        for (int i = 0; i < 9; ++i) {
            addSlot(new Slot(invPlayer, i, 8 + i * 18, 176));
        }
        
        endPlayerSlots = inventorySlots.size();
        
        assertIntArraySize(progressAndPower, 2);
        trackIntArray(progressAndPower);
        
        this.progressAndPower = progressAndPower;
        this.pos = pos;
    }

    @Override
    public boolean canInteractWith(PlayerEntity playerIn) {
        return isWithinUsableDistance(pos, playerIn, ChiselTileEntities.AUTO_CHISEL.get()); // TODO temporary
    }

    @Override
    @Nonnull
    public ItemStack transferStackInSlot(PlayerEntity playerIn, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = (Slot) this.inventorySlots.get(index);

        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            if (index >= beginOutputSlots && index < endOutputSlots) {
                if (!this.mergeItemStack(itemstack1, beginPlayerSlots, endPlayerSlots, true)) {
                    return ItemStack.EMPTY;
                }

                slot.onSlotChange(itemstack1, itemstack);
            } else if (index >= beginPlayerSlots) {
                if (CarvingUtils.getChiselRegistry().getGroup(itemstack1.getItem()).isPresent()) {
                    if (!this.mergeItemStack(itemstack1, targetSlot, targetSlot + 1, false)) {
                        if (!this.mergeItemStack(itemstack1, beginInputSlots, endInputSlots, false)) {
                            return ItemStack.EMPTY;
                        }
                    }
                } else if (itemstack1.getItem() instanceof IChiselItem) {
                    if (!this.mergeItemStack(itemstack1, chiselSlot, chiselSlot + 1, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (index >= beginPlayerSlots && index < endPlayerSlots - 9) {
                    if (!this.mergeItemStack(itemstack1, endPlayerSlots - 9, endPlayerSlots, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (index >= endPlayerSlots - 9 && index < endPlayerSlots && !this.mergeItemStack(itemstack1, 3, 30, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.mergeItemStack(itemstack1, beginPlayerSlots, endPlayerSlots, false)) {
                return ItemStack.EMPTY;
            }

            if (itemstack1.isEmpty()) {
                slot.putStack(ItemStack.EMPTY);
            } else {
                slot.onSlotChanged();
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
