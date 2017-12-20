package team.chisel.common.block;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemStackHandler;

@ParametersAreNonnullByDefault
public class TileAutoChisel extends TileEntity {
    
    private class DirtyingStackHandler extends ItemStackHandler {
        
        DirtyingStackHandler() { super(); }
        DirtyingStackHandler(int size) { super(size); }
        
        @Override
        protected void onContentsChanged(int slot) {
            super.onContentsChanged(slot);
            markDirty();
        }
    }
    
    private class View implements IItemHandlerModifiable {
        
        private final IItemHandlerModifiable input, output;
        
        View(EnumFacing side) {
            if (side == EnumFacing.UP) {
                this.input = this.output = chiselInv;
            } else if (side == EnumFacing.DOWN) {
                this.input = this.output = inputInv;
            } else {
                this.input = inputInv;
                this.output = outputInv;
            }
        }

        @Override
        public int getSlots() {
            return input == output ? input.getSlots() : input.getSlots() + output.getSlots();
        }

        @Override
        public @Nullable ItemStack getStackInSlot(int slot) {
            if (slot >= 0 && slot < getSlots()) {
                if (slot < input.getSlots()) {
                    return input.getStackInSlot(slot); 
                } else {
                    return output.getStackInSlot(slot - input.getSlots());
                }
            }
            return null;
        }

        @Override
        public @Nullable ItemStack insertItem(int slot, @Nullable ItemStack stack, boolean simulate) {
            if (slot >= 0 && slot < input.getSlots()) {
                return input.insertItem(slot, stack, simulate);
            }
            return null;
        }

        @Override
        public @Nullable ItemStack extractItem(int slot, int amount, boolean simulate) {
            if (slot >= input.getSlots() && slot < getSlots()) {
                slot -= input.getSlots();
                return output.extractItem(slot, amount, simulate);
            }
            return null;
        }

        @Override
        public void setStackInSlot(int slot, @Nullable ItemStack stack) {
            if (slot >= 0 && slot < getSlots()) {
                if (slot < input.getSlots()) {
                    input.setStackInSlot(slot, stack);
                } else {
                    output.setStackInSlot(slot - input.getSlots(), stack);
                }
            }
        }
    }
     
    private static final int INPUT_COUNT = 12;
    private static final int OUTPUT_COUNT = INPUT_COUNT;
    
    private final ItemStackHandler chiselInv = new DirtyingStackHandler();
    private final ItemStackHandler targetInv = new DirtyingStackHandler();
    private final ItemStackHandler inputInv = new DirtyingStackHandler(INPUT_COUNT);
    private final ItemStackHandler outputInv = new DirtyingStackHandler(OUTPUT_COUNT);
    
    public IItemHandler getChiselInv() {
        return chiselInv;
    }
    
    public IItemHandler getTargetInv() {
        return targetInv;
    }
    
    public IItemHandler getInputInv() {
        return inputInv;
    }
    
    public IItemHandler getOutputInv() {
        return outputInv;
    }
    
    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
        return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY || super.hasCapability(capability, facing);
    }

    @Override
    public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(this.new View(facing));
        }
        return super.getCapability(capability, facing);
    }
    
    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        compound.setTag("chisel", chiselInv.serializeNBT());
        compound.setTag("target", targetInv.serializeNBT());
        compound.setTag("input", inputInv.serializeNBT());
        compound.setTag("output", outputInv.serializeNBT());
        return super.writeToNBT(compound);
    }
    
    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        this.chiselInv.deserializeNBT(compound.getCompoundTag("chisel"));
        this.targetInv.deserializeNBT(compound.getCompoundTag("target"));
        this.inputInv.deserializeNBT(compound.getCompoundTag("input"));
        this.outputInv.deserializeNBT(compound.getCompoundTag("output"));
    }
}
