package team.chisel.common.integration.imc;

import java.util.Set;

import org.apache.commons.lang3.tuple.Pair;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.SetMultimap;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.InterModComms.IMCMessage;
import net.minecraftforge.registries.ForgeRegistries;
import team.chisel.Chisel;
import team.chisel.api.IMC;
import team.chisel.api.carving.CarvingUtils;
import team.chisel.api.carving.ICarvingVariation;
import team.chisel.api.carving.IVariationRegistry;

public class IMCHandler {

    public static final IMCHandler INSTANCE = new IMCHandler();
    
    public final Object2IntMap<String> imcCounts = new Object2IntOpenHashMap<>();

    private int order = 1000;

    private IMCHandler() {
    }

    public void handleMessage(IMCMessage message) {
        for (IMC imc : IMC.values()) {
            if (imc.key.equals(message.getMethod())) {
                handle(message, imc);
            }
        }
    }
    
    private Pair<ItemStack, BlockState> parseNBT(CompoundNBT tag) {
        CompoundNBT stacktag = tag.getCompound("stack");
        String blockname = tag.getString("block");
        Preconditions.checkArgument(!(stacktag.isEmpty() && blockname.isEmpty()), "Must provide stack or blockstate.");
        ItemStack stack = null;
        if (!stacktag.isEmpty()) {
            stack = ItemStack.read(stacktag);
        }
        BlockState state = null;
        if (!blockname.isEmpty()) {
            Block block = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(blockname));
            Preconditions.checkNotNull(block, "Could not find block %s in registry!", blockname);
        }
        return Pair.of(stack, state);
    }
    
    private SetMultimap<String, String> deprecatedUses = HashMultimap.create();

    @SuppressWarnings("deprecation")
    private void handle(IMCMessage message, IMC type) {
        imcCounts.mergeInt(message.getSenderModId(), 1, (i1, i2) -> i1 + i2);
        if (type.isDeprecated()) {
            Set<String> usedForMod = deprecatedUses.get(message.getSenderModId());
            if (usedForMod.add(type.toString())) {
                Chisel.logger.warn("Mod {} is using deprecated IMC {}! This IMC type may be removed soon, please notify the author of this mod.", message.getSenderModId(), type);
            }
        }

        IVariationRegistry reg = CarvingUtils.getChiselRegistry();
        String[] data = null;
        ResourceLocation resource = null;
        try {
            switch (type) {
            case ADD_VARIATION: {
                CompoundNBT tag = (CompoundNBT) message.getMessageSupplier().get();
                ResourceLocation group = new ResourceLocation(tag.getString("group"));
                Preconditions.checkNotNull(Strings.emptyToNull(group.getPath()), "No group specified");
                
                Pair<ItemStack, BlockState> variationdata = parseNBT(tag);
                ICarvingVariation v;
                // TODO
                //if (variationdata.getLeft() == null) {
                //    v = CarvingUtils.variationFor(group, variationdata.getRight(), order++);
                //} else if (variationdata.getRight() == null) {
                //    v = CarvingUtils.variationFor(group, variationdata.getLeft(), order++);
                //} else {
                //    v = CarvingUtils.variationFor(group, variationdata.getLeft(), variationdata.getRight(), order++);
                //}
                //reg.addVariation(group, v);
                break;
            }
            case REMOVE_VARIATION:{
                CompoundNBT tag = (CompoundNBT) message.getMessageSupplier().get();
                String group = tag.getString("group");
                Pair<ItemStack, BlockState> variationdata = parseNBT(tag);
                // TODO
                //if (Strings.isNullOrEmpty(group)) {
                //    if (variationdata.getLeft() != null) {
                //        reg.removeVariation(variationdata.getLeft());
                //    }
                //    if (variationdata.getRight() != null) {
                //        reg.removeVariation(variationdata.getRight());
                //    }
                //} else {
                //    if (variationdata.getLeft() != null) {
                //        reg.removeVariation(variationdata.getLeft(), group);
                //    }
                //    if (variationdata.getRight() != null) {
                //        reg.removeVariation(variationdata.getRight(), group);
                //    }
                //}
                break;
            }
            default: {
                throw new IllegalArgumentException("Invalid IMC constant! How...what...?");
            }
            }
        } catch (Exception e) {
            Object value = message.getMessageSupplier().get();
            Chisel.logger.error("Could not handle data {} for IMC type {}. This was sent from mod {}.\n" + "!! This is a bug in that mod !!\nSwallowing error and continuing...", value, type.name(),
                    message.getSenderModId());
            e.printStackTrace();
        }
    }
}
