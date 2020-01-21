package team.chisel.client.data;

import static team.chisel.client.data.VariantTemplates.Metal.*;

import javax.annotation.ParametersAreNonnullByDefault;

import com.google.common.collect.ImmutableList;
import com.tterrag.registrate.util.nullness.FieldsAreNonnullByDefault;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Value;
import lombok.experimental.NonFinal;
import mcp.MethodsReturnNonnullByDefault;
import team.chisel.api.block.ModelTemplate;
import team.chisel.api.block.VariantTemplate;

@MethodsReturnNonnullByDefault
@FieldsAreNonnullByDefault
@ParametersAreNonnullByDefault
public class VariantTemplates {
    
    @Value
    @Getter(onMethod = @__({@Override}))
    @NonFinal
    private static class SimpleTemplate implements VariantTemplate {
        
        String name;
        ModelTemplate modelTemplate;
        String[] tooltip;
    }
    
    @Value
    @EqualsAndHashCode(callSuper = true)
    @Getter(onMethod = @__({@Override}))
    private static class LocalizedTemplate extends SimpleTemplate {
        
        String localizedName;

        public LocalizedTemplate(String name, String localizedName, ModelTemplate modelTemplate, String[] tooltip) {
            super(name, modelTemplate, tooltip);
            this.localizedName = localizedName;
        }
    }
    
    public static VariantTemplate simple(String name, String... tooltip) {
        return simple(name, ModelTemplates.simpleBlock(), tooltip);
    }
    
    public static VariantTemplate simple(String name, ModelTemplate template, String... tooltip) {
        return new SimpleTemplate(name, template, tooltip);
    }
    
    public static VariantTemplate withName(String name, String localizedName, String... tooltip) {
        return withName(name, localizedName, ModelTemplates.simpleBlock(), tooltip);
    }
    
    public static VariantTemplate withName(String name, String localizedName, ModelTemplate template, String... tooltip) {
        return new LocalizedTemplate(name, localizedName, template, tooltip);
    }
    
    @FieldsAreNonnullByDefault
    public static class Metal {
        
        public static final VariantTemplate CAUTION = simple("caution");
        public static final VariantTemplate CRATE = withName("crate", "Shipping Crate");
        public static final VariantTemplate THERMAL = simple("thermal", ModelTemplates.cubeBottomTop());
        public static final VariantTemplate MACHINE = simple("machine", "An Old Relic From The", "Land Of OneTeuFyv");
        public static final VariantTemplate BADGREGGY = withName("badgreggy", "Egregious");
        public static final VariantTemplate BOLTED = simple("bolted");
        public static final VariantTemplate SCAFFOLD = simple("scaffold");

    }
    
    public static final ImmutableList<VariantTemplate> METAL =
            ImmutableList.of(CAUTION, CRATE, THERMAL, MACHINE, BADGREGGY, BOLTED, SCAFFOLD);
}
