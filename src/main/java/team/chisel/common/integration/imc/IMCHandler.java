package team.chisel.common.integration.imc;

import net.minecraft.block.Block;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.event.FMLInterModComms;
import team.chisel.Chisel;
import team.chisel.api.IMC;
import team.chisel.api.carving.ICarvingRegistry;
import team.chisel.common.carving.Carving;

public class IMCHandler {

    public static final IMCHandler INSTANCE = new IMCHandler();

    private int order = 1000;

    private IMCHandler() {
    }

    public void handleMessage(FMLInterModComms.IMCMessage message) {
        for (IMC imc : IMC.values()) {
            if (imc.key.equals(message.key)) {
                handle(message, imc, message.getStringValue());
            }
        }
    }

    private void handle(FMLInterModComms.IMCMessage message, IMC type, String value) {
        Chisel.logger.info("Got IMC");

        ICarvingRegistry reg = Carving.chisel;
        String[] data = value.split("\\|");
        ResourceLocation resource = new ResourceLocation(data[1]);
        try {
            switch (type) {
                case ADD_VARIATION:
                case REMOVE_VARIATION:
                    Block block = Block.REGISTRY.getObject(resource);
                    int meta = Short.parseShort(data[2]);
                    if (type == IMC.ADD_VARIATION) {
                        reg.addVariation(data[0], block.getStateFromMeta(meta), order++);

                    } else {
                        reg.removeVariation(block.getStateFromMeta(meta), data[0]);
                    }
                    break;
                case REGISTER_GROUP_ORE:
                    reg.registerOre(data[0], data[1]);
                    break;
                default:
                    throw new IllegalArgumentException("Invalid IMC constant! How...what...?");
            }
        } catch (Exception e) {
            Chisel.logger.error("Could not handle data {} for IMC type {}. This was sent from mod {}.\n" + "!! This is a bug in that mod !!\nSwallowing error and continuing...", value, type.name(),
                    message.getSender());
            e.printStackTrace();
        }
    }
}
