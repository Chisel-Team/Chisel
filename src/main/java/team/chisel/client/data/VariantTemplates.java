package team.chisel.client.data;

import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import com.google.common.collect.ImmutableList;
import com.tterrag.registrate.util.nullness.FieldsAreNonnullByDefault;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.data.ShapelessRecipeBuilder;
import net.minecraft.item.DyeColor;
import net.minecraft.item.Item;
import team.chisel.Chisel;
import team.chisel.api.block.ModelTemplate;
import team.chisel.api.block.RecipeTemplate;
import team.chisel.api.block.VariantTemplate;
import team.chisel.common.block.BlockCarvable;

@MethodsReturnNonnullByDefault
@FieldsAreNonnullByDefault
@ParametersAreNonnullByDefault
public class VariantTemplates {
    
    @MethodsReturnNonnullByDefault
    @RequiredArgsConstructor
    @Builder
    public static class SimpleTemplate implements VariantTemplate {
        
        @Getter(onMethod = @__({@Override}))
        private final String name;
        private final String localizedName;
        private final ModelTemplate modelTemplate;
        private final RecipeTemplate recipeTemplate;
        @Getter(onMethod = @__({@Override}))
        private final String[] tooltip;
        
        @Override
        public String getLocalizedName() {
            return localizedName == null ? VariantTemplate.super.getLocalizedName() : localizedName;
        }
        
        @Override
        public Optional<ModelTemplate> getModelTemplate() {
            return Optional.ofNullable(modelTemplate);
        }
        
        @Override
        public Optional<RecipeTemplate> getRecipeTemplate() {
            return Optional.ofNullable(recipeTemplate);
        }
        
        public static SimpleTemplateBuilder builderFrom(VariantTemplate from) {
            return builder()
                    .name(from.getName())
                    .localizedName(from instanceof SimpleTemplate ? ((SimpleTemplate)from).localizedName : from.getLocalizedName())
                    .modelTemplate(from.getModelTemplate().orElse(null))
                    .recipeTemplate(from.getRecipeTemplate().orElse(null))
                    .tooltip(from.getTooltip());
        }
    }
    
    public static VariantTemplate empty(String name, String... tooltip) {
        return simple(name, null, tooltip);
    }
    
    public static VariantTemplate simple(String name, String... tooltip) {
        return simple(name, ModelTemplates.simpleBlock(), tooltip);
    }
    
    public static VariantTemplate simple(String name, @Nullable ModelTemplate template, String... tooltip) {
        return SimpleTemplate.builder().name(name).modelTemplate(template).tooltip(tooltip).build();
    }
    
    public static VariantTemplate withName(String name, String localizedName, String... tooltip) {
        return withName(name, localizedName, ModelTemplates.simpleBlock(), tooltip);
    }
    
    public static VariantTemplate withName(String name, String localizedName, @Nullable ModelTemplate template, String... tooltip) {
        return SimpleTemplate.builder().name(name).localizedName(localizedName).modelTemplate(template).tooltip(tooltip).build();
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
    
    public static final VariantTemplate RAW = simple("raw");
    
    public static class Rock {

        public static final VariantTemplate CRACKED = simple("cracked");
        public static final VariantTemplate SOLID_BRICKS = withName("solid_bricks", "Bricks");
        public static final VariantTemplate SMALL_BRICKS = simple("small_bricks");
        public static final VariantTemplate SOFT_BRICKS = withName("soft_bricks", "Weathered Bricks");
        public static final VariantTemplate CRACKED_BRICKS = simple("cracked_bricks");
        public static final VariantTemplate TRIPLE_BRICKS = withName("triple_bricks", "Wide Bricks");
        public static final VariantTemplate ENCASED_BRICKS = simple("encased_bricks");
        public static final VariantTemplate CHAOTIC_BRICKS = withName("chaotic_bricks", "Trodden Bricks");
        public static final VariantTemplate ARRAY = withName("array", "Arrayed Bricks");
        public static final VariantTemplate TILES_MEDIUM = withName("tiles_medium", "Tiles");
        public static final VariantTemplate TILES_LARGE = withName("tiles_large", "Big Tile");
        public static final VariantTemplate TILES_SMALL = withName("tiles_small", "Small Tiles");
        public static final VariantTemplate CHAOTIC_MEDIUM = withName("chaotic_medium", "Disordered Tiles");
        public static final VariantTemplate CHAOTIC_SMALL = withName("chaotic_small", "Small Disordered Tiles");
        public static final VariantTemplate BRAID = simple("braid");
        public static final VariantTemplate DENT = simple("dent");
        public static final VariantTemplate FRENCH_1 = simple("french_1");
        public static final VariantTemplate FRENCH_2 = simple("french_2");
        public static final VariantTemplate JELLYBEAN = simple("jellybean");
        public static final VariantTemplate LAYERS = simple("layers");
        public static final VariantTemplate MOSAIC = simple("mosaic");
        public static final VariantTemplate ORNATE = simple("ornate");
        public static final VariantTemplate PANEL = simple("panel");
        public static final VariantTemplate ROAD = simple("road");
        public static final VariantTemplate SLANTED = simple("slanted");
        public static final VariantTemplate ZAG = simple("zag");
        public static final VariantTemplate CIRCULAR = simple("circular");
        public static final VariantTemplate CIRCULAR_CTM = withName("circularct", "Circular", ModelTemplates.ctm("circular"), "Has CTM");
        public static final VariantTemplate WEAVER = withName("weaver", "Celtic");
        public static final VariantTemplate PILLAR = simple("pillar", ModelTemplates.cubeColumn());
        public static final VariantTemplate TWISTED = simple("twisted", ModelTemplates.cubeColumn());
        public static final VariantTemplate PRISM = simple("prism");
        public static final VariantTemplate CUTS = simple("cuts");
    }

    public static class Wood {
        public static final VariantTemplate BLINDS = simple("blinds");
        public static final VariantTemplate CHAOTIC = simple("chaotic");
        public static final VariantTemplate CHAOTIC_HORIZONTAL = simple("chaotic-hor");
        public static final VariantTemplate CLEAN = simple("clean");
        public static final VariantTemplate CRATE = withName("crate", "Shipping Crate");
        public static final VariantTemplate CRATE_FANCY = simple("crate-fancy");
        public static final VariantTemplate CRATEEX = simple("crateex");
        public static final VariantTemplate DOUBLE = simple("double", ModelTemplates.cubeColumn());
        public static final VariantTemplate FANCY = simple("fancy");
        public static final VariantTemplate LARGE = simple("large");
        public static final VariantTemplate PANEL = simple("panel-nails");
        public static final VariantTemplate PARQUET = simple("parquet");
        public static final VariantTemplate SHORT = simple("short", ModelTemplates.axisFaces());
        public static final VariantTemplate VERTICAL = simple("vertical");
        public static final VariantTemplate VERTICAL_UNEVEN = simple("vertical-uneven");
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
    
    @SuppressWarnings("null")
    public static final ImmutableList<VariantTemplate> COLORS = ImmutableList.copyOf(Arrays.stream(DyeColor.values())
            .map(color -> simple(color.getName()))
            .collect(Collectors.toList()));
    
    @SuppressWarnings("null")
    public static final ImmutableList<VariantTemplate> colors(ModelTemplate template) {
        return colors(template, null);
    }

    public static ImmutableList<VariantTemplate> colors(ModelTemplate model, RecipeTemplate whiteRecipe) {
        return ImmutableList.copyOf(COLORS.stream()
                .map(SimpleTemplate::builderFrom)
                .map(b -> b.modelTemplate(model))
                .map(b -> b.recipeTemplate(b.name.equals(DyeColor.WHITE.getName()) ? whiteRecipe : null))
                .map(SimpleTemplate.SimpleTemplateBuilder::build)
                .collect(Collectors.toList()));
    }
    
    public static final ImmutableList<VariantTemplate> METAL = ofClass(Metal.class);

    public static final ImmutableList<VariantTemplate> ROCK = ofClass(Rock.class);

    public static final ImmutableList<VariantTemplate> WOOD = ofClass(Wood.class);

    public static final SimpleTemplate withRecipe(VariantTemplate template, RecipeTemplate recipe) {
        return SimpleTemplate.builderFrom(template)
                .recipeTemplate(recipe)
                .build();
    }
    
    @SuppressWarnings("null")
    public static final ImmutableList<VariantTemplate> withUncraft(Collection<VariantTemplate> templates, Item item) {
        return ImmutableList.copyOf(templates.stream()
                .map(SimpleTemplate::builderFrom)
                .map(b -> b.recipeTemplate((prov, block) -> new ShapelessRecipeBuilder(item, 9)
                        .addIngredient(block, 9)
                        .addCriterion("has_" + item.getRegistryName().getPath(), prov.hasItem(item))
                        .build(prov, Chisel.MOD_ID + ":" + item.getRegistryName().getPath() + "_from_" + ((BlockCarvable)block).getVariation().getName())))
                .map(SimpleTemplate.SimpleTemplateBuilder::build)
                .collect(Collectors.toList()));
    }
}
