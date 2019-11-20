package team.chisel.common.inventory;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
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
            return stack != null && CarvingUtils.getChiselRegistry().getGroup(stack) != null;
        }
    }

    public final InventoryPlayer invPlayer;
    public final TileAutoChisel te;
    
    private final int beginInputSlots, endInputSlots;
    public  final int chiselSlot, targetSlot;
    private final int beginOutputSlots, endOutputSlots;
    private final int beginPlayerSlots, endPlayerSlots;

    public ContainerAutoChisel(InventoryPlayer invPlayer, TileAutoChisel te) {
        this.invPlayer = invPlayer;
        this.te = te;

        int yStart = 19;
        chiselSlot = 0;
        this.addSlotToContainer(new SlotItemHandler(te.getOtherInv(), 0, 80, yStart + 9) {

            @Override
            public boolean isItemValid(@Nullable ItemStack stack) {
                return stack != null && stack.getItem() instanceof IChiselItem;
            }
        });
        targetSlot = 1;
        this.addSlotToContainer(new ChiselableSlot(te.getOtherInv(), 1, 80, 54 + yStart - 9));

        beginInputSlots = inventorySlots.size();
        IItemHandler inv = te.getInputInv();
        for (int i = 0; i < inv.getSlots(); i++) {
            this.addSlotToContainer(new ChiselableSlot(inv, i, 8 + 18 * (i % 3), yStart + 18 * (i / 3)));
        }
        endInputSlots = beginOutputSlots = inventorySlots.size();
        inv = te.getOutputInv();
        for (int i = 0; i < inv.getSlots(); i++) {
            this.addSlotToContainer(new SlotItemHandler(inv, i, 8 + 108 + 18 * (i % 3), yStart + 18 * (i / 3)) {

                @Override
                public boolean isItemValid(@Nullable ItemStack stack) {
                    return false;
                }
            });
        }
        
        beginPlayerSlots = endOutputSlots = inventorySlots.size();

        for (int r = 0; r < 3; ++r) {
            for (int c = 0; c < 9; ++c) {
                this.addSlotToContainer(new Slot(invPlayer, c + r * 9 + 9, 8 + c * 18, 118 + r * 18));
            }
        }

        for (int i = 0; i < 9; ++i) {
            this.addSlotToContainer(new Slot(invPlayer, i, 8 + i * 18, 176));
        }
        
        endPlayerSlots = inventorySlots.size();
    }

    private int progress, power;

    @Override
    public void detectAndSendChanges() {
        super.detectAndSendChanges();

        int prog = te.getProgress();
        IEnergyStorage energy = te.getCapability(CapabilityEnergy.ENERGY, null); 
        int pow = energy == null ? 0 : energy.getEnergyStored();

        for (IContainerListener listener : listeners) {
            if (prog != progress) {
                listener.sendWindowProperty(this, 0, prog);
            }
            if (pow != power) {
                listener.sendWindowProperty(this, 1, pow);
            }
        }

        this.progress = prog;
        this.power = pow;
    }

    @Override
    public void updateProgressBar(int id, int data) {
        super.updateProgressBar(id, data);
        if (id == 0) {
            this.te.setProgress(data);
        } else if (id == 1) {
            te.setEnergy(data);
        }
    }

    @Override
    public boolean canInteractWith(PlayerEntity playerIn) {
        return te.getWorld().getTileEntity(te.getPos()) != te ? false : invPlayer.player.getDistanceSqToCenter(te.getPos()) <= 64.0D;
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
                if (CarvingUtils.getChiselRegistry().getGroup(itemstack1) != null) {
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
