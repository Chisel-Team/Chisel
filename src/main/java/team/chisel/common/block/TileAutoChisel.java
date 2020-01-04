package team.chisel.common.block;

import java.util.EnumMap;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import org.apache.commons.lang3.Validate;

import lombok.Getter;
import lombok.Setter;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.DiggingParticle;
import net.minecraft.client.particle.Particle;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.particles.BlockParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.IIntArray;
import net.minecraft.util.INameable;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.FakePlayerFactory;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemStackHandler;
import team.chisel.api.IChiselItem;
import team.chisel.api.carving.CarvingUtils;
import team.chisel.api.carving.ICarvingGroup;
import team.chisel.api.carving.ICarvingVariation;
import team.chisel.common.config.Configurations;
import team.chisel.common.init.ChiselTileEntities;
import team.chisel.common.util.SoundUtil;

@ParametersAreNonnullByDefault
public class TileAutoChisel extends TileEntity implements ITickableTileEntity, INameable {
    
    private class DirtyingStackHandler extends ItemStackHandler {
        
        DirtyingStackHandler() { super(); }
        DirtyingStackHandler(int size) { super(size); }
        
        @Override
        protected void onContentsChanged(int slot) {
            super.onContentsChanged(slot);
            markDirty();
        }
    }
    
    private class ItemView implements IItemHandlerModifiable {
        
        private final IItemHandlerModifiable input, output;
        
        ItemView(@Nullable Direction side) {
            if (side == null || side.getAxis().isVertical()) {
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
        public ItemStack getStackInSlot(int slot) {
            if (slot >= 0 && slot < getSlots()) {
                if (slot < input.getSlots()) {
                    return input.getStackInSlot(slot); 
                } else {
                    return output.getStackInSlot(slot - input.getSlots());
                }
            }
            return ItemStack.EMPTY;
        }

        @Override
        public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
            if (slot >= 0 && slot < input.getSlots()) {
                return input.insertItem(slot, stack, simulate);
            }
            return stack;
        }

        @Override
        public ItemStack extractItem(int slot, int amount, boolean simulate) {
            if (slot >= input.getSlots() && slot < getSlots()) {
                slot -= input.getSlots();
                return output.extractItem(slot, amount, simulate);
            }
            return ItemStack.EMPTY;
        }

        @Override
        public void setStackInSlot(int slot, ItemStack stack) {
            if (slot >= 0 && slot < getSlots()) {
                if (slot < input.getSlots()) {
                    input.setStackInSlot(slot, stack);
                } else {
                    output.setStackInSlot(slot - input.getSlots(), stack);
                }
            }
        }
        
        @Override
        public boolean isItemValid(int slot, ItemStack stack) {
            if (slot >= 0 && slot < getSlots()) {
                return slot < input.getSlots() && input.isItemValid(slot, stack);
            }
            return false;
        }

        @Override
        public int getSlotLimit(int slot) {
            return 64;
        }
    }
     
    private static final int INPUT_COUNT = 12;
    private static final int OUTPUT_COUNT = INPUT_COUNT;
    
    private static final int MAX_PROGRESS = 1024;
    private static final int BASE_PROGRESS = 16;
    private static final int SPEEDUP_PROGRESS = 64;
    private static final int POWER_PER_TICK = 20;
    
    private final ItemStackHandler otherInv = new DirtyingStackHandler(2) {
        @Override
        public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
            if (stack.isEmpty()) {
                return stack;
            }
            if (slot == 0 && stack.getItem() instanceof IChiselItem) {
                return super.insertItem(slot, stack, simulate);
            }
            if (slot == 1 && CarvingUtils.getChiselRegistry().getVariation(stack.getItem()) != null) {
                return super.insertItem(slot, stack, simulate);
            }
            return stack;
        }
    };
    private final ItemStackHandler inputInv = new DirtyingStackHandler(INPUT_COUNT) {

        @Override
        public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
            if (!stack.isEmpty() && CarvingUtils.getChiselRegistry().getVariation(stack.getItem()) != null) {
                return super.insertItem(slot, stack, simulate);
            }
            return stack;
        }
    };
    private final ItemStackHandler outputInv = new DirtyingStackHandler(OUTPUT_COUNT);
    
    private static class EnergyStorageMutable extends EnergyStorage {
        
        public EnergyStorageMutable(int capacity, int maxReceive, int maxExtract) {
            super(capacity, maxReceive, maxExtract);
        }

        public void setEnergyStored(int energy) {
            this.energy = MathHelper.clamp(energy, 0, getMaxEnergyStored());
        }
    };
    private final EnergyStorageMutable energyStorage = new EnergyStorageMutable(10000, POWER_PER_TICK * 2, POWER_PER_TICK);
    
    private class EnergyView extends EnergyStorageMutable {
        
        public EnergyView() {
            super(energyStorage.getMaxEnergyStored(), POWER_PER_TICK * 2, 0);
            this.setEnergyStored(energyStorage.getEnergyStored());
        }
        
        @Override
        public int receiveEnergy(int maxReceive, boolean simulate) {
            return energyStorage.receiveEnergy(maxReceive, simulate);
        }
    }
    
    public final IIntArray energyData = new IIntArray() {
        
        @Override
        public int size() {
            return 1;
        }
        
        @Override
        public void set(int index, int value) {
            energyStorage.setEnergyStored(value);
        }
        
        @Override
        public int get(int index) {
            return energyStorage.getEnergyStored();
        }
    };
    
    private int sourceSlot = -1;
    private int prevSource = -1;
    
    @Getter
    @Setter
    private int progress = 0;
    
    @Setter
    private @Nullable ITextComponent customName;
    
    public TileAutoChisel() {
        super(ChiselTileEntities.AUTO_CHISEL_TE.get());
    }
    
    public IItemHandler getOtherInv() {
        return otherInv;
    }
    
    public ItemStack getChisel() {
        return getOtherInv().getStackInSlot(0);
    }
    
    public ItemStack getTarget() {
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
    
    public float getSpeedFactor() {
        return Configurations.autoChiselPowered ? (float) energyStorage.getEnergyStored() / energyStorage.getMaxEnergyStored() : 1;
    }
    
    public int getPowerProgressPerTick() {
        int maxPerTick = Configurations.autoChiselNeedsPower ? (BASE_PROGRESS + SPEEDUP_PROGRESS) : SPEEDUP_PROGRESS;
        return (int) Math.ceil(getSpeedFactor() * maxPerTick);
    }
    
    public int getUsagePerTick() {
        return (int) Math.ceil(getSpeedFactor() * POWER_PER_TICK);
    }
    
    public void setEnergy(int energy) {
        this.energyStorage.setEnergyStored(energy);
    }

    protected boolean canOutput(ItemStack stack) {
        ItemStack res = stack;
        for (int i = 0; i < getOutputInv().getSlots(); i++) {
            res = getOutputInv().insertItem(i, res, true);
            if (res.isEmpty()) {
                return true;
            }
        }
        return false;
    }
    
    protected void setSourceSlot(int slot) {
        this.sourceSlot = slot;
    }
    
    protected void updateClientSlot() {
        if (sourceSlot != prevSource) {
//            TODO 1.14 packets Chisel.network.sendToDimension(new MessageUpdateAutochiselSource(getPos(), sourceSlot < 0 ? ItemStack.EMPTY : inputInv.getStackInSlot(sourceSlot)), getWorld().provider.getDimension());
        }
        prevSource = sourceSlot;
    }
    
    @SuppressWarnings("null")
    protected void mergeOutput(ItemStack stack) {
        for (int i = 0; stack != null && i < getOutputInv().getSlots(); i++) {
            stack = getOutputInv().insertItem(i, stack, false);
        }
    }
    
    @Override
    public void tick() {
        if (getWorld() == null || getWorld().isRemote) {
            return;
        }
                
        if (energyStorage.getEnergyStored() == 0 && Configurations.autoChiselNeedsPower) {
            return;
        }
        
        @Nonnull ItemStack target = getTarget();
        @Nonnull ItemStack chisel = getChisel();
        @Nonnull ItemStack source = sourceSlot < 0 ? ItemStack.EMPTY : getInputInv().getStackInSlot(sourceSlot);
        chisel = chisel.isEmpty() ? ItemStack.EMPTY : chisel.copy();
        
        ICarvingVariation v = target.isEmpty() || chisel.isEmpty() ? null : CarvingUtils.getChiselRegistry().getVariation(target.getItem()).orElse(null);
        ICarvingGroup g = target.isEmpty() || chisel.isEmpty() ? null : CarvingUtils.getChiselRegistry().getGroup(target.getItem()).orElse(null);

        if (chisel.isEmpty() || v == null) {
            setSourceSlot(-1);
            progress = 0;
            updateClientSlot();
            return;
        }
        
        // Force a source slot recalc if the stack has changed to something that cannot be converted to the target
        if (!source.isEmpty() && !CarvingUtils.getChiselRegistry().getGroup(source.getItem()).equals(g)) {
            source = ItemStack.EMPTY;
        }
        
        IChiselItem chiselitem = (IChiselItem) chisel.getItem();
        
        // Make sure to run this block if the source stack is removed, so a new one can be found
        if ((sourceSlot < 0 && getWorld().getGameTime() % 20 == 0) || sourceSlot >= 0) {
            // Reset source slot if it's been removed
            if (source.isEmpty()) {
                setSourceSlot(-1);
            }
            // Make sure we can output this stack
            ItemStack res = new ItemStack(v.getItem());
            if (!source.isEmpty()) {
                res.setCount(source.getCount());
            }
            if (source.isEmpty() || canOutput(res)) {
                for (int i = 0; sourceSlot < 0 && i < getInputInv().getSlots(); i++) {
                    ItemStack stack = getInputInv().getStackInSlot(i);
                    if (!stack.isEmpty() && g.equals(CarvingUtils.getChiselRegistry().getGroup(stack.getItem()))) {
                        res.setCount(stack.getCount());
                        if (canOutput(res) && chiselitem.canChisel(getWorld(), FakePlayerFactory.getMinecraft((ServerWorld) getWorld()), chisel, v)) {
                            setSourceSlot(i);
                            source = res.copy();
                        }
                    }
                }
            } else {
                setSourceSlot(-1);
            }
        }
        
        if (sourceSlot >= 0) {
            source = getInputInv().getStackInSlot(sourceSlot);
            Validate.notNull(source);
            ICarvingVariation sourceVar = CarvingUtils.getChiselRegistry().getVariation(source.getItem()).orElse(null);
            if (sourceVar != v) {
                if (progress < MAX_PROGRESS) {
                    if (!Configurations.autoChiselNeedsPower) {
                        // Add constant progress
                        progress = Math.min(MAX_PROGRESS, progress + BASE_PROGRESS);
                    }
                    // Compute progress added by FE
                    int toUse = Math.min(MAX_PROGRESS - progress, getPowerProgressPerTick());
                    // Compute FE usage
                    int powerToUse = getUsagePerTick();
                    // Avoid NaN
                    if (toUse > 0 && powerToUse > 0) {
                        if (Configurations.autoChiselPowered) {
                            int used = energyStorage.extractEnergy(powerToUse, false);
                            progress += toUse * ((float) used / powerToUse);
                        } else {
                            progress += toUse;
                        }
                    }
                } else {
                    ItemStack res = new ItemStack(v.getItem());
                    source = source.copy();
                    chisel = chisel.copy();

                    ServerPlayerEntity player = FakePlayerFactory.getMinecraft((ServerWorld) getWorld());
                    player.inventory.mainInventory.set(player.inventory.currentItem, chisel);
                    res = chiselitem.craftItem(chisel, source, res, player, $ -> {}); // FIXME
                    player.inventory.mainInventory.set(player.inventory.currentItem, ItemStack.EMPTY);

                    chiselitem.onChisel(getWorld(), player, chisel, v);

                    inputInv.setStackInSlot(sourceSlot, source);
                    
                    // TODO 1.14 packets Chisel.network.send(PacketDistributor.DIMENSION.with(() -> getWorld().getDimension().getType()), new MessageAutochiselFX(getPos(), chisel, sourceVar.getBlockState()));

                    otherInv.setStackInSlot(0, chisel);

                    mergeOutput(res);
                    // Try the next slot, if this is invalid it will be fixed next update
                    setSourceSlot((sourceSlot + 1) % getInputInv().getSlots());
                    progress = 0;
                }
            } else {
                // This is the same variation, so just move it to the output
                inputInv.setStackInSlot(sourceSlot, ItemStack.EMPTY);
                mergeOutput(source);
            }
        } else {
            progress = 0;
        }
        
        updateClientSlot();
    }
    
    private final EnumMap<Direction, LazyOptional<IItemHandler>> viewCache = new EnumMap<>(Direction.class);
    private final LazyOptional<IEnergyStorage> energyCap = LazyOptional.of(EnergyView::new);

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> capability, @Nullable Direction facing) {
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.orEmpty(capability, 
                    viewCache.computeIfAbsent(facing, f -> LazyOptional.of(() -> new ItemView(f))));
        } else if (Configurations.autoChiselPowered && capability == CapabilityEnergy.ENERGY) {
            return CapabilityEnergy.ENERGY.orEmpty(capability, energyCap);
        }
        return super.getCapability(capability, facing);
    }
    
    @Override
    @Nullable
    public SUpdateTileEntityPacket getUpdatePacket() {
        return new SUpdateTileEntityPacket(getPos(), 0, getUpdateTag());
    }
    
    @Override
    public CompoundNBT getUpdateTag() {
        CompoundNBT ret = super.getUpdateTag();
        if (hasCustomName()) {
            ret.putString("customName", ITextComponent.Serializer.toJson(getName()));
        }
        return ret;
    }
    
    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
        handleUpdateTag(pkt.getNbtCompound());
        super.onDataPacket(net, pkt);
    }
    
    @Override
    public void handleUpdateTag(CompoundNBT tag) {
        if (tag.contains("customName")) {
            setCustomName(ITextComponent.Serializer.fromJson(tag.getString("customName")));
        }
        super.handleUpdateTag(tag);
    }
    
    @Override
    public CompoundNBT write(CompoundNBT compound) {
        compound.put("other", otherInv.serializeNBT());
        compound.put("input", inputInv.serializeNBT());
        compound.put("output", outputInv.serializeNBT());
        compound.putInt("energy", energyStorage.getEnergyStored());
        compound.putInt("progress", getProgress());
        compound.putInt("source", sourceSlot);
        if (hasCustomName()) {
            compound.putString("customName", ITextComponent.Serializer.toJson(getName()));
        }
        return super.write(compound);
    }
    
    @Override
    public void read(CompoundNBT compound) {
        super.read(compound);
        this.otherInv.deserializeNBT(compound.getCompound("other"));
        this.inputInv.deserializeNBT(compound.getCompound("input"));
        this.outputInv.deserializeNBT(compound.getCompound("output"));
        this.energyStorage.setEnergyStored(compound.getInt("energy"));
        this.progress = compound.getInt("progress");
        this.sourceSlot = compound.getInt("source");
        if (compound.contains("customName")) {
            this.customName = ITextComponent.Serializer.fromJson(compound.getString("customName"));
        }
    }
    
    /* == IWorldNameable == */
    
    @Override
    public ITextComponent getDisplayName() {
        return hasCustomName() ? getCustomName() : getName();
    }

    @Override
    public ITextComponent getName() {
        ITextComponent name = getCustomName();
        if (name == null) {
            name = new TranslationTextComponent("container.autochisel.title");
        }
        return name;
    }

    @Override
    public boolean hasCustomName() {
        return customName != null;
    }
    
    /* == Rendering Data == */
    
    @Setter
    @Getter
    private @Nullable ItemStack source;

    @SuppressWarnings("null")
    public void spawnCompletionFX(PlayerEntity player, ItemStack chisel, BlockState source) {
        SoundUtil.playSound(player, getPos(), SoundUtil.getSound(player, chisel, source.getBlock()));
        if (chisel.isEmpty()) {
            getWorld().playSound(player, pos, SoundEvents.ENTITY_ITEM_BREAK, SoundCategory.BLOCKS, 0.8F, 0.8F + this.world.rand.nextFloat() * 0.4F);
        }
        int i = 3;
        float mid = i / 2f;
        for (int j = 0; j < i; ++j) {
            for (int k = 0; k < i; ++k) {
                for (int l = 0; l < i; ++l) {
                    double vx = (mid - j) * 0.05;
                    double vz = (mid - l) * 0.05;
                    Particle fx = Minecraft.getInstance().particles.addParticle(
                            new BlockParticleData(ParticleTypes.BLOCK, source), 
                            pos.getX() + 0.5, pos.getY() + 10/16D, pos.getZ() + 0.5, 
                            vx, 0, vz);

                    if (fx != null) {
                        ((DiggingParticle)fx).setBlockPos(pos);
                    }
                }
            }
        }
    }
}
