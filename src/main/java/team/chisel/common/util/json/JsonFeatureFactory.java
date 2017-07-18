package team.chisel.common.util.json;

import static com.google.common.base.CaseFormat.LOWER_CAMEL;
import static com.google.common.base.CaseFormat.UPPER_UNDERSCORE;

import java.util.function.BooleanSupplier;

import com.google.common.base.Converter;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.util.JsonUtils;
import net.minecraftforge.common.crafting.IConditionFactory;
import net.minecraftforge.common.crafting.JsonContext;
import team.chisel.Features;

public final class JsonFeatureFactory implements IConditionFactory {

    private final Converter<String, String> configToEnum = LOWER_CAMEL.converterTo(UPPER_UNDERSCORE);

    @Override
    public BooleanSupplier parse(JsonContext context, JsonObject json) {
        String value = JsonUtils.getString(json, "feature");
        String name = configToEnum.convert(value);
        Features feature;
        try {
            feature = Features.valueOf(name);
        } catch (IllegalArgumentException e) {
            throw new JsonParseException("Unknown feature: " + value);
        }
        return feature::enabled;
    }

}
