package team.chisel.common.inventory;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
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
    
    private final InventoryPlayer invPlayer;
    private final TileAutoChisel te;
    
    public ContainerAutoChisel(InventoryPlayer invPlayer, TileAutoChisel te) {
        this.invPlayer = invPlayer;
        this.te = te;
        
        int yStart = 19;
        this.addSlotToContainer(new SlotItemHandler(te.getChiselInv(), 0, 80, yStart) {
            @Override
            public boolean isItemValid(@Nullable ItemStack stack) {
                return stack != null && stack.getItem() instanceof IChiselItem;
            }
        });
        this.addSlotToContainer(new ChiselableSlot(te.getTargetInv(), 0, 80, 54 + yStart));
        
        IItemHandler inv = te.getInputInv();
        for (int i = 0; i < inv.getSlots(); i++) {
            this.addSlotToContainer(new ChiselableSlot(inv, i, 8 + 18 * (i / 4), yStart + 18 * (i % 4)));
        }
        inv = te.getOutputInv();
        for (int i = 0; i < inv.getSlots(); i++) {
            this.addSlotToContainer(new SlotItemHandler(inv, i, 8 + 108 + 18 * (i / 4), yStart + 18 * (i % 4)) {
                @Override
                public boolean isItemValid(@Nullable ItemStack stack) { 
                    return false; 
                }
            });
        }

        for (int r = 0; r < 3; ++r) {
            for (int c = 0; c < 9; ++c) {
                this.addSlotToContainer(new Slot(invPlayer, c + r * 9 + 9, 8 + c * 18, 109 + r * 18));
            }
        }

        for (int i = 0; i < 9; ++i) {
            this.addSlotToContainer(new Slot(invPlayer, i, 8 + i * 18, 167));
        }
    }

    @Override
    public boolean canInteractWith(EntityPlayer playerIn) {
        return te.getWorld().getTileEntity(te.getPos()) != te ? false : invPlayer.player.getDistanceSqToCenter(te.getPos()) <= 64.0D;
    }
    
    @Override
    @Nullable
    public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
        return null;
    }
}
