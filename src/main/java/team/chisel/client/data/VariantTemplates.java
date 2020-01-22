package team.chisel.client.data;

import java.util.Arrays;
import java.util.stream.Collectors;

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
    
    public static class Rock {
        
        public static final VariantTemplate CRACKED = simple("cracked");
        public static final VariantTemplate BRICKS_SOFT = withName("bricks-soft", "Weathered Bricks");
        public static final VariantTemplate BRICKS_CRACKED = withName("bricks-cracked", "Cracked Bricks");
        public static final VariantTemplate BRICKS_TRIPLE = withName("bricks-triple", "Wide Bricks");
        public static final VariantTemplate BRICKS_ENCASED = withName("bricks-encased", "Encased Bricks");
        public static final VariantTemplate BRAID = simple("braid");
        public static final VariantTemplate ARRAY = withName("array", "Arrayed Bricks");
        public static final VariantTemplate TILES_LARGE = withName("tiles-large", "Big Tile");
        public static final VariantTemplate TILES_SMALL = withName("tiles-small", "Small Tiles");
        public static final VariantTemplate CHAOTIC_MEDIUM = withName("chaotic-medium", "Disordered Tiles");
        public static final VariantTemplate CHAOTIC_SMALL = withName("chaotic-small", "Small Disordered Tiles");
        public static final VariantTemplate DENT = simple("dent");
        public static final VariantTemplate FRENCH_1 = withName("french-1", "French 1");
        public static final VariantTemplate FRENCH_2 = withName("french-2", "French 2");
        public static final VariantTemplate JELLYBEAN = simple("jellybean");
        public static final VariantTemplate LAYERS = simple("layers");
        public static final VariantTemplate MOSAIC = simple("mosaic");
        public static final VariantTemplate ORNATE = simple("ornate");
        public static final VariantTemplate PANEL = simple("panel");
        public static final VariantTemplate ROAD = simple("road");
        public static final VariantTemplate SLANTED = simple("slanted");
        public static final VariantTemplate ZAG = simple("zag");
        public static final VariantTemplate CIRCULAR_CTM = withName("circularct", "Circular", ModelTemplates.ctm("circular"), "Has CTM");
        public static final VariantTemplate WEAVER = withName("weaver", "Celtic");
        public static final VariantTemplate BRICKS_SOLID = withName("bricks-solid", "Bricks");
        public static final VariantTemplate BRICKS_SMALL = withName("bricks-small", "Small Bricks");
        public static final VariantTemplate CIRCULAR = simple("circular");
        public static final VariantTemplate TILES_MEDIUM = withName("tiles-medium", "Tiles");
        public static final VariantTemplate PILLAR = simple("pillar", ModelTemplates.cubeColumn());
        public static final VariantTemplate TWISTED = simple("twisted", ModelTemplates.cubeColumn());
        public static final VariantTemplate PRISM = simple("prism");
        public static final VariantTemplate RAW = simple("raw");
        public static final VariantTemplate BRICKS_CHAOTIC = withName("bricks-chaotic", "Trodden Bricks");
        public static final VariantTemplate CUTS = simple("cuts");
    }
    
    public static class Stone {
        
        public static final VariantTemplate EMBOSSED = simple("embossed");
        public static final VariantTemplate INDENTED = simple("indented");
        public static final VariantTemplate MARKER = simple("marker");
        public static final VariantTemplate SUNKEN = simple("sunken");
        public static final VariantTemplate POISON = simple("poison");
    }
    
    @SuppressWarnings("null")
    private static ImmutableList<VariantTemplate> ofClass(Class<?> cls) {
        return ImmutableList.copyOf(Arrays.stream(cls.getDeclaredFields())
                .map(f -> {
                    try {
                        return (VariantTemplate) f.get(null);
                    } catch (IllegalArgumentException | IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                })
                .collect(Collectors.toList()));
    }
    
    public static final ImmutableList<VariantTemplate> METAL = ofClass(Metal.class);
    public static final ImmutableList<VariantTemplate> ROCK = ofClass(Rock.class);
    @SuppressWarnings("null")
    public static final ImmutableList<VariantTemplate> STONE = ImmutableList.<VariantTemplate>builder()
            .addAll(ROCK)
            .addAll(ofClass(Stone.class))
            .build();
}
