package team.chisel.common.block;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import org.apache.commons.lang3.Validate;

import lombok.Getter;
import lombok.Setter;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.IWorldNameable;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.FakePlayerFactory;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemStackHandler;
import team.chisel.api.IChiselItem;
import team.chisel.api.carving.CarvingUtils;
import team.chisel.api.carving.ICarvingGroup;
import team.chisel.api.carving.ICarvingVariation;
import team.chisel.common.util.SoundUtil;

@ParametersAreNonnullByDefault
public class TileAutoChisel extends TileEntity implements ITickable, IWorldNameable {
    
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
            if (side.getAxis().isVertical()) {
                this.input = inputInv;
                this.output = outputInv;
            } else {
                this.input = this.output = otherInv;
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
            return stack;
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
    
    private static final int MAX_PROGRESS = 20 * 5;
    
    private final ItemStackHandler otherInv = new DirtyingStackHandler(2) {
        @Override
        public @Nullable ItemStack insertItem(int slot, @Nullable ItemStack stack, boolean simulate) {
            if (stack == null) {
                return null;
            }
            if (slot == 0 && stack.getItem() instanceof IChiselItem) {
                return super.insertItem(slot, stack, simulate);
            }
            if (slot == 1 && CarvingUtils.getChiselRegistry().getVariation(stack) != null) {
                return super.insertItem(slot, stack, simulate);
            }
            return stack;
        }
    };
    private final ItemStackHandler inputInv = new DirtyingStackHandler(INPUT_COUNT) {

        @Override
        public @Nullable ItemStack insertItem(int slot, @Nullable ItemStack stack, boolean simulate) {
            if (stack != null && CarvingUtils.getChiselRegistry().getVariation(stack) != null) {
                return super.insertItem(slot, stack, simulate);
            }
            return stack;
        }
    };
    private final ItemStackHandler outputInv = new DirtyingStackHandler(OUTPUT_COUNT);
    
    private int sourceSlot = -1;
    
    @Getter
    @Setter
    private int progress = 0;
    
    @Setter
    private @Nullable String customName;
    
    public IItemHandler getOtherInv() {
        return otherInv;
    }
    
    public @Nullable ItemStack getChisel() {
        return getOtherInv().getStackInSlot(0);
    }
    
    public @Nullable ItemStack getTarget() {
        return getOtherInv().getStackInSlot(1);
    }
    
    public IItemHandler getInputInv() {
        return inputInv;
    }
    
    public IItemHandler getOutputInv() {
        return outputInv;
    }
    
    public int getMaxProgress() {
        return MAX_PROGRESS;
    }
    
    protected boolean canOutput(ItemStack stack) {
        ItemStack res = stack;
        for (int i = 0; i < getOutputInv().getSlots(); i++) {
            res = getOutputInv().insertItem(i, res, true);
            if (res == null) {
                return true;
            }
        }
        return false;
    }
    
    @SuppressWarnings("null")
    protected void mergeOutput(ItemStack stack) {
        for (int i = 0; stack != null && i < getOutputInv().getSlots(); i++) {
            stack = getOutputInv().insertItem(i, stack, false);
        }
    }
    
    @Override
    public void update() {
        if (getWorld() == null || getWorld().isRemote) {
            return;
        }
        
        ItemStack target = getTarget();
        ItemStack chisel = getChisel();
        ItemStack source = sourceSlot < 0 ? null : getInputInv().getStackInSlot(sourceSlot);
        chisel = chisel == null ? chisel : chisel.copy();
        
        ICarvingVariation v = target == null ? null : CarvingUtils.getChiselRegistry().getVariation(target);
        ICarvingGroup g = target == null ? null : CarvingUtils.getChiselRegistry().getGroup(target);

        if (chisel == null || chisel.stackSize < 1 || v == null) {
            sourceSlot = -1;
            progress = 0;
            return;
        }
        
        // Force a source slot recalc if the stack has changed to something that cannot be converted to the target
        if (source != null && CarvingUtils.getChiselRegistry().getGroup(source) != g) {
            source = null;
        }
        
        IChiselItem chiselitem = (IChiselItem) chisel.getItem();
        
        // Make sure to run this block if the source stack is removed, so a new one can be found
        if ((sourceSlot < 0 && getWorld().getTotalWorldTime() % 20 == 0) || (sourceSlot >= 0 && source == null)) {
            // Reset source slot if it's been removed
            if (source == null) {
                sourceSlot = -1;
            }
            for (int i = 0; sourceSlot < 0 && i < getInputInv().getSlots(); i++) {
                ItemStack stack = getInputInv().getStackInSlot(i);
                if (stack != null && g == CarvingUtils.getChiselRegistry().getGroup(stack)) {
                    ItemStack res = v.getStack();
                    res.stackSize = stack.stackSize;
                    if (canOutput(res) && chiselitem.canChisel(getWorld(), FakePlayerFactory.getMinecraft((WorldServer) getWorld()), chisel, v)) {
                        sourceSlot = i;
                    }
                }
            }
        }

        if (sourceSlot >= 0) {
            source = getInputInv().getStackInSlot(sourceSlot);
            Validate.notNull(source);
            if (CarvingUtils.getChiselRegistry().getVariation(source) != v) {
                if (progress < MAX_PROGRESS) {
                    progress++;
                } else {
                    ItemStack res = v.getStack();
                    source = source.copy();
                    chisel = chisel.copy();

                    EntityPlayerMP player = FakePlayerFactory.getMinecraft((WorldServer) getWorld());
                    player.inventory.mainInventory[player.inventory.currentItem] = chisel;
                    res = chiselitem.craftItem(chisel, source, res, player);
                    player.inventory.mainInventory[player.inventory.currentItem] = null;

                    chiselitem.onChisel(getWorld(), player, chisel, v);

                    inputInv.setStackInSlot(sourceSlot, source.stackSize == 0 ? null : source);

                    SoundUtil.playSound(player, chisel, v.getBlockState());
                    if (chisel.stackSize == 0) {
                        getWorld().playSound(player, pos, SoundEvents.ENTITY_ITEM_BREAK, SoundCategory.BLOCKS, 0.8F, 0.8F + this.world.rand.nextFloat() * 0.4F);
                    }
                    otherInv.setStackInSlot(0, chisel.stackSize == 0 ? null : chisel);

                    mergeOutput(res);
                    // Try the next slot, if this is invalid it will be fixed next update
                    sourceSlot = (sourceSlot + 1) % getInputInv().getSlots();
                    progress = 0;
                }
            } else {
                // This is the same variation, so just move it to the output
                inputInv.setStackInSlot(sourceSlot, null);
                mergeOutput(source);
            }
        } else {
            progress = 0;
        }
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
    @Nullable
    public SPacketUpdateTileEntity getUpdatePacket() {
        return new SPacketUpdateTileEntity(getPos(), getBlockMetadata(), getUpdateTag());
    }
    
    @Override
    public NBTTagCompound getUpdateTag() {
        NBTTagCompound ret = super.getUpdateTag();
        if (hasCustomName()) {
            ret.setString("customName", getName());
        }
        return ret;
    }
    
    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
        handleUpdateTag(pkt.getNbtCompound());
        super.onDataPacket(net, pkt);
    }
    
    @Override
    public void handleUpdateTag(NBTTagCompound tag) {
        if (tag.hasKey("customName")) {
            this.customName = tag.getString("customName");
        }
        super.handleUpdateTag(tag);
    }
    
    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        compound.setTag("other", otherInv.serializeNBT());
        compound.setTag("input", inputInv.serializeNBT());
        compound.setTag("output", outputInv.serializeNBT());
        compound.setInteger("progress", getProgress());
        compound.setInteger("source", sourceSlot);
        if (hasCustomName()) {
            compound.setString("customName", getName());
        }
        return super.writeToNBT(compound);
    }
    
    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        this.otherInv.deserializeNBT(compound.getCompoundTag("other"));
        this.inputInv.deserializeNBT(compound.getCompoundTag("input"));
        this.outputInv.deserializeNBT(compound.getCompoundTag("output"));
        this.progress = compound.getInteger("progress");
        this.sourceSlot = compound.getInteger("source");
        if (compound.hasKey("customName")) {
            this.customName = compound.getString("customName");
        }
    }
    
    /* == IWorldNameable == */
    
    @Override
    public ITextComponent getDisplayName() {
        return hasCustomName() ? new TextComponentString(getName()) : new TextComponentTranslation(getName());
    }

    @Override
    public String getName() {
        String name = customName;
        if (name == null) {
            name = "container.autochisel.title";
        }
        return name;
    }

    @Override
    public boolean hasCustomName() {
        return customName != null;
    }
}