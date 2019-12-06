package team.chisel.common.inventory;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIntArray;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import team.chisel.api.IChiselItem;
import team.chisel.api.carving.CarvingUtils;
import team.chisel.common.block.TileAutoChisel;

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
    public final TileAutoChisel te;
    
    private final int beginInputSlots, endInputSlots;
    public  final int chiselSlot, targetSlot;
    private final int beginOutputSlots, endOutputSlots;
    private final int beginPlayerSlots, endPlayerSlots;
    
    private final IIntArray progressAndPower;

    public ContainerAutoChisel(PlayerInventory invPlayer, TileAutoChisel te, IIntArray progressAndPower) {
        super(null, 0); // TODO 1.14
        this.invPlayer = invPlayer;
        this.te = te;

        int yStart = 19;
        chiselSlot = 0;
        addSlot(new SlotItemHandler(te.getOtherInv(), 0, 80, yStart + 9) {

            @Override
            public boolean isItemValid(@Nullable ItemStack stack) {
                return stack != null && stack.getItem() instanceof IChiselItem;
            }
        });
        targetSlot = 1;
        addSlot(new ChiselableSlot(te.getOtherInv(), 1, 80, 54 + yStart - 9));

        beginInputSlots = inventorySlots.size();
        IItemHandler inv = te.getInputInv();
        for (int i = 0; i < inv.getSlots(); i++) {
            addSlot(new ChiselableSlot(inv, i, 8 + 18 * (i % 3), yStart + 18 * (i / 3)));
        }
        endInputSlots = beginOutputSlots = inventorySlots.size();
        inv = te.getOutputInv();
        for (int i = 0; i < inv.getSlots(); i++) {
            addSlot(new SlotItemHandler(inv, i, 8 + 108 + 18 * (i % 3), yStart + 18 * (i / 3)) {

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
    }

    @Override
    public boolean canInteractWith(PlayerEntity playerIn) {
        return isWithinUsableDistance(IWorldPosCallable.of(te.getWorld(), te.getPos()), playerIn, Blocks.AIR);//TODO 1.14 ChiselBlocks.auto_chisel);
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
}
