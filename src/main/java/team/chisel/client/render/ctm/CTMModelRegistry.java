package team.chisel.client.render.ctm;

import team.chisel.Chisel;
import team.chisel.common.Reference;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.HashMap;
import java.util.Map;

/**
 * Registers all the ctm models/blocks
 *
 * @author minecreatr
 */
public class CTMModelRegistry implements Reference {

    private static final Map<ModelResourceLocation, ModelCTM> models = new HashMap<ModelResourceLocation, ModelCTM>();


    private static void register(ModelResourceLocation location, ModelCTM model) {
        models.put(location, model);
    }

    public static class BakedEventListener {

        @SubscribeEvent
        public void onModelBake(ModelBakeEvent event) {
            for (Map.Entry<ModelResourceLocation, ModelCTM> entry : models.entrySet()) {
                Chisel.debug("Registering CTM model for " + entry.getKey().toString());
                event.modelRegistry.putObject(entry.getKey(), entry.getValue());
            }
        }
    }

    public static void register(String block, String variation, int amount) {
        for (int i = 0; i < amount; i++) {
            ModelResourceLocation location;
            if (i == 0) {
                location = new ModelResourceLocation(MOD_ID.toLowerCase() + ":" + block, "variation=" + variation);
            } else {
                location = new ModelResourceLocation(MOD_ID.toLowerCase() + ":" + block + i, "variation=" + variation);
            }
            register(location, new ModelCTM());
        }
    }
}
