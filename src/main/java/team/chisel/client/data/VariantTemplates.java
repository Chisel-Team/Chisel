package team.chisel.client.data;

import com.google.common.collect.ImmutableList;
import com.tterrag.registrate.providers.RegistrateRecipeProvider;
import com.tterrag.registrate.util.nullness.FieldsAreNonnullByDefault;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import team.chisel.Chisel;
import team.chisel.api.block.ModelTemplate;
import team.chisel.api.block.RecipeTemplate;
import team.chisel.api.block.VariantTemplate;
import team.chisel.common.block.BlockCarvable;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@SuppressWarnings("unused")
@FieldsAreNonnullByDefault
public class VariantTemplates {

    public static final VariantTemplate RAW = simple("raw");
    @SuppressWarnings("null")
    public static final ImmutableList<VariantTemplate> COLORS = ImmutableList.copyOf(Arrays.stream(DyeColor.values())
            .map(color -> simple(color.getSerializedName()))
            .collect(Collectors.toList()));
    public static final ImmutableList<VariantTemplate> METAL = ofClass(Metal.class);
    public static final ImmutableList<VariantTemplate> STONE = ofClass(Stone.class);
    public static final ImmutableList<VariantTemplate> ROCK = ofClass(Rock.class);
    @SuppressWarnings("null")
    public static final ImmutableList<VariantTemplate> COBBLESTONE = ImmutableList.<VariantTemplate>builder()
            .addAll(ROCK)
            .add(withName("extra/emboss", "Embossed"))
            .add(withName("extra/indent", "Indent"))
            .add(withName("extra/marker", "Marker"))
            .build();
    @SuppressWarnings("null")
    public static final ImmutableList<VariantTemplate> COBBLESTONE_MOSSY = ImmutableList.copyOf(COBBLESTONE.stream()
            .map(v -> SimpleTemplate.builderFrom(v)
                    .modelTemplate(mossyModel(v))
                    .build())
            .collect(Collectors.toList()));
    public static final ImmutableList<VariantTemplate> PLANKS = ofClass(Planks.class);

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

    @SuppressWarnings({"null", "ConstantConditions"})
    public static ImmutableList<VariantTemplate> colors(ModelTemplate template) {
        return colors(template, null);
    }

    public static ImmutableList<VariantTemplate> colors(ModelTemplate model, RecipeTemplate whiteRecipe) {
        return ImmutableList.copyOf(COLORS.stream()
                .map(SimpleTemplate::builderFrom)
                .map(b -> b.modelTemplate(model))
                .map(b -> b.recipeTemplate(b.name.equals(DyeColor.WHITE.getSerializedName()) ? whiteRecipe : null))
                .map(SimpleTemplate.SimpleTemplateBuilder::build)
                .collect(Collectors.toList()));
    }

    private static ModelTemplate mossyModel(VariantTemplate template) {
        if (template.getName().equals("circularct")) {
            return ModelTemplates.mossyCtm("cobblestone", "circular");
        } else if (template.getName().equals("pillar") || template.getName().equals("twisted")) {
            return ModelTemplates.mossyColumn("cobblestone");
        } else {
            return ModelTemplates.mossy("cobblestone");
        }
    }

    public static SimpleTemplate withRecipe(VariantTemplate template, RecipeTemplate recipe) {
        return SimpleTemplate.builderFrom(template)
                .recipeTemplate(recipe)
                .build();
    }

    @SuppressWarnings("null")
    public static ImmutableList<VariantTemplate> withUncraft(Collection<VariantTemplate> templates, Item item) {
        return ImmutableList.copyOf(templates.stream()
                .map(SimpleTemplate::builderFrom)
                .map(b -> b.recipeTemplate((prov, block) -> new ShapelessRecipeBuilder(item, 9)
                        .requires(block, 9)
                        .unlockedBy("has_" + Objects.requireNonNull(item.getRegistryName()).getPath(), RegistrateRecipeProvider.has(item))
                        .save(prov, Chisel.MOD_ID + ":" + item.getRegistryName().getPath() + "_from_" + ((BlockCarvable) block).getVariation().getName())))
                .map(SimpleTemplate.SimpleTemplateBuilder::build)
                .collect(Collectors.toList()));
    }

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

        public static SimpleTemplateBuilder builderFrom(VariantTemplate from) {
            return builder()
                    .name(from.getName())
                    .localizedName(from instanceof SimpleTemplate ? ((SimpleTemplate) from).localizedName : from.getLocalizedName())
                    .modelTemplate(from.getModelTemplate().orElse(null))
                    .recipeTemplate(from.getRecipeTemplate().orElse(null))
                    .tooltip(from.getTooltip());
        }

        @Override
        public String getLocalizedName() {
            return localizedName;
        }

        @Override
        public Optional<ModelTemplate> getModelTemplate() {
            return Optional.of(modelTemplate);
        }

        @Override
        public Optional<RecipeTemplate> getRecipeTemplate() {
            return Optional.of(recipeTemplate);
        }
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

    public static class Stone {
        public static final VariantTemplate CRACKED = simple("cracked");
        public static final VariantTemplate SOLID_BRICKS = withName("solid_bricks", "Bricks");
        public static final VariantTemplate SMALL_BRICKS = simple("small_bricks");
        public static final VariantTemplate SOFT_BRICKS = withName("soft_bricks", "Weathered Bricks");
        public static final VariantTemplate CRACKED_BRICKS = simple("cracked_bricks");
        public static final VariantTemplate TRIPLE_BRICKS = withName("triple_bricks", "Wide Bricks");
        public static final VariantTemplate ENCASED_BRICKS = simple("encased_bricks");
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
        public static final VariantTemplate CIRCULAR = simple("circular");
        public static final VariantTemplate PILLAR = simple("pillar", ModelTemplates.cubeColumn());
        public static final VariantTemplate TWISTED = simple("twisted", ModelTemplates.cubeColumn());
        public static final VariantTemplate PRISM = simple("prism");
    }

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

    public static class Planks {
        public static final VariantTemplate LARGE_PLANKS = simple("large_planks");
        public static final VariantTemplate CRUDE_HORIZONTAL_PLANKS = simple("crude_horizontal_planks");
        public static final VariantTemplate VERTICAL_PLANKS = simple("vertical_planks");
        public static final VariantTemplate CRUDE_VERTICAL_PLANKS = simple("crude_vertical_planks");
        public static final VariantTemplate ENCASED_PLANKS = simple("encased_planks");
        public static final VariantTemplate ENCASED_LARGE_PLANKS = simple("encased_large_planks");
        public static final VariantTemplate BRACED_PLANKS = simple("braced_planks", ModelTemplates.cubeColumn(name -> ModelTemplates.replaceVariant(name, "log_bordered")));
        public static final VariantTemplate CRATE = simple("shipping_crate");
        public static final VariantTemplate PANELING = simple("paneling");
        public static final VariantTemplate CRUDE_PANELING = simple("crude_paneling");
        public static final VariantTemplate STACKED = simple("stacked");
        public static final VariantTemplate SMOOTH = simple("smooth");
        public static final VariantTemplate ENCASED_SMOOTH = simple("encased_smooth");
        public static final VariantTemplate BRAID = simple("braid");
        public static final VariantTemplate LOG_CABIN = simple("log_cabin", (prov, block) -> {
            String name = "block/" + ModelTemplates.name(block);
            prov.simpleBlock(block, prov.models().withExistingParent(name, prov.modLoc("cube_axis"))
                    .texture("x", name + "-ew")
                    .texture("y", ModelTemplates.replaceVariant(name, "log_bordered"))
                    .texture("z", name + "-ns"));
        });
    }
}
