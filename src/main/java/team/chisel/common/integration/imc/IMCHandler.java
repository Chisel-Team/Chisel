package team.chisel.common.integration.imc;

import org.apache.commons.lang3.tuple.Pair;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.event.FMLInterModComms;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import team.chisel.Chisel;
import team.chisel.api.IMC;
import team.chisel.api.carving.CarvingUtils;
import team.chisel.api.carving.ICarvingRegistry;
import team.chisel.api.carving.ICarvingVariation;
import team.chisel.common.carving.Carving;

public class IMCHandler {

    public static final IMCHandler INSTANCE = new IMCHandler();

    private int order = 1000;

    private IMCHandler() {
    }

    public void handleMessage(FMLInterModComms.IMCMessage message) {
        for (IMC imc : IMC.values()) {
            if (imc.key.equals(message.key)) {
                handle(message, imc);
            }
        }
    }
    
    private Pair<ItemStack, IBlockState> parseNBT(NBTTagCompound tag) {
        NBTTagCompound stacktag = tag.getCompoundTag("stack");
        String blockname = tag.getString("block");
        int blockmeta = tag.getInteger("meta");
        Preconditions.checkArgument(!(stacktag.hasNoTags() && blockname.isEmpty()), "Must provide stack or blockstate.");
        ItemStack stack = null;
        if (!stacktag.hasNoTags()) {
            stack = ItemStack.loadItemStackFromNBT(stacktag);
        }
        IBlockState state = null;
        if (!blockname.isEmpty()) {
            Block block = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(blockname));
            Preconditions.checkNotNull(block, "Could not find block %s in registry!", blockname);
            state = block.getStateFromMeta(blockmeta);
        }
        return Pair.of(stack, state);
    }

    @SuppressWarnings("deprecation")
    private void handle(FMLInterModComms.IMCMessage message, IMC type) {
        Chisel.logger.info("Received IMC from {}, of type {}.", message.getSender(), message.key);
        if (type.isDeprecated()) {
            Chisel.logger.warn("MOD {} IS USING DEPRECATED IMC {}. This IMC type may be removed soon, please notify the author of this mod.", message.getSender(), type);
        }

        ICarvingRegistry reg = Carving.chisel;
        String[] data = null;
        ResourceLocation resource = null;
        if (message.isStringMessage()) {
            data = message.getStringValue().split("\\|");
            resource = new ResourceLocation(data[1]);
        }
        try {
            switch (type) {
            case ADD_VARIATION:
            case REMOVE_VARIATION: {
                Block block = Block.REGISTRY.getObject(resource);
                int meta = Short.parseShort(data[2]);
                if (type == IMC.ADD_VARIATION) {
                    reg.addVariation(data[0], block.getStateFromMeta(meta), order++);

                } else {
                    reg.removeVariation(block.getStateFromMeta(meta), data[0]);
                }
                break;
            }
            case REGISTER_GROUP_ORE: {
                reg.registerOre(data[0], data[1]);
                break;
            }
            case ADD_VARIATION_V2: {
                NBTTagCompound tag = message.getNBTValue();
                String group = tag.getString("group");
                Preconditions.checkNotNull(Strings.emptyToNull(group), "No variation specified");
                
                Pair<ItemStack, IBlockState> variationdata = parseNBT(tag);
                ICarvingVariation v;
                if (variationdata.getLeft() == null) {
                    v = CarvingUtils.variationFor(variationdata.getRight(), order++);
                } else if (variationdata.getRight() == null) {
                    v = CarvingUtils.variationFor(variationdata.getLeft(), order++);
                } else {
                    v = CarvingUtils.variationFor(variationdata.getLeft(), variationdata.getRight(), order++);
                }
                reg.addVariation(group, v);
                break;
            }
            case REMOVE_VARIATION_V2:{
                NBTTagCompound tag = message.getNBTValue();
                String group = tag.getString("group");
                Pair<ItemStack, IBlockState> variationdata = parseNBT(tag);
                if (Strings.isNullOrEmpty(group)) {
                    if (variationdata.getLeft() != null) {
                        reg.removeVariation(variationdata.getLeft());
                    }
                    if (variationdata.getRight() != null) {
                        reg.removeVariation(variationdata.getRight());
                    }
                } else {
                    if (variationdata.getLeft() != null) {
                        reg.removeVariation(variationdata.getLeft(), group);
                    }
                    if (variationdata.getRight() != null) {
                        reg.removeVariation(variationdata.getRight(), group);
                    }
                }
                break;
            }
            default: {
                throw new IllegalArgumentException("Invalid IMC constant! How...what...?");
            }
            }
        } catch (Exception e) {
            Object value = message.isNBTMessage() ? message.getNBTValue() : message.isStringMessage() ? message.getStringValue() : "UNKNOWN";
            Chisel.logger.error("Could not handle data {} for IMC type {}. This was sent from mod {}.\n" + "!! This is a bug in that mod !!\nSwallowing error and continuing...", value, type.name(),
                    message.getSender());
            e.printStackTrace();
        }
    }
}
