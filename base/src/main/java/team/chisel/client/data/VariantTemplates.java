package team.chisel.client.data;

import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.annotation.Nullable;

import com.google.common.collect.ImmutableList;
import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateItemModelProvider;
import com.tterrag.registrate.util.nullness.FieldsAreNonnullByDefault;

import com.tterrag.registrate.util.nullness.NonNullBiConsumer;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraftforge.fml.common.Mod;
import team.chisel.Chisel;
import team.chisel.api.block.ModelTemplate;
import team.chisel.api.block.RecipeTemplate;
import team.chisel.api.block.VariantTemplate;
import team.chisel.common.block.BlockCarvable;

@FieldsAreNonnullByDefault
public class VariantTemplates {

    @RequiredArgsConstructor
    @Builder
    public static class SimpleTemplate implements VariantTemplate {
        
        @Getter(onMethod = @__({@Override}))
        private final String name;
        private final String localizedName;
        private final ModelTemplate modelTemplate;
        private final NonNullBiConsumer<DataGenContext<Item, BlockItem>, RegistrateItemModelProvider> itemModelTemplate;
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
        public Optional<NonNullBiConsumer<DataGenContext<Item, BlockItem>, RegistrateItemModelProvider>> getItemModelTemplate() {
            return Optional.ofNullable(itemModelTemplate);
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

    public static class MetalTerrain {
        public static final VariantTemplate LARGE_INGOT = simple("large_ingot", ModelTemplates.cubeBottomTop());
        public static final VariantTemplate SMALL_INGOT = simple("small_ingot", ModelTemplates.cubeBottomTop());
        public static final VariantTemplate BRICK = simple("brick", ModelTemplates.cubeBottomTop());
        public static final VariantTemplate COIN_HEADS = withName("coin_heads", "Coin (Heads)", ModelTemplates.cubeBottomTop());
        public static final VariantTemplate COIN_TAILS = withName("coin_tails", "Coin (Heads)", ModelTemplates.cubeBottomTop());
        public static final VariantTemplate CRATE_DARK = withName("crate_dark", "Dark Crate", ModelTemplates.cubeBottomTop());
        public static final VariantTemplate CRATE_LIGHT = withName("crate_light", "Light Crate", ModelTemplates.cubeBottomTop());
        public static final VariantTemplate PLATES = simple("plates", ModelTemplates.cubeBottomTop());
        public static final VariantTemplate RIVETS = withName("rivets", "Riveted Plates", ModelTemplates.cubeBottomTop());
        public static final VariantTemplate SPACE = withName("space", "Purple Space");
        public static final VariantTemplate SPACE_BLACK = withName("space_black", "Black Space");
        public static final VariantTemplate SIMPLE = simple("simple", ModelTemplates.cubeBottomTop());

    }

    //TODO fix pillar CTM models
    public static class Pillar {
        public static final VariantTemplate PLAINPLAIN = withName("plainplain", "Plain-Capped Plain Pillar", ModelTemplates.cubeAll("-top"));
        public static final VariantTemplate PLAINGREEK = withName("plaingreek", "Greek-Capped Plain Pillar", ModelTemplates.cubeAll("-top"));
        public static final VariantTemplate GREEKPLAIN = withName("greekplain", "Plain-Capped Greek Pillar", ModelTemplates.cubeAll("-top"));
        public static final VariantTemplate GREEKGREEK = withName("greekgreek", "Greek-Capped Greek Pillar", ModelTemplates.cubeAll("-top"));
        public static final VariantTemplate CONVEXPLAIN = withName("convexplain", "Convexed Pillar", ModelTemplates.cubeAll("-top"));
        public static final VariantTemplate CARVED = withName("carved", "Scribed Pillar", ModelTemplates.cubeColumn());
        public static final VariantTemplate ORNAMENTAL = withName("ornamental", "Ornamental Pillar", ModelTemplates.cubeColumn());

    }

    public static class Scribbles {
        public static final VariantTemplate ZERO = withName("scribbles_0", "Hieroglyphs 1", ModelTemplates.cubeColumn( s -> ModelTemplates.replaceVariant(s, "scribbles_0-side"),  s -> ModelTemplates.replaceVariant(s, "scribbles_0-top")));
        public static final VariantTemplate ONE = withName("scribbles_1", "Hieroglyphs 2", ModelTemplates.cubeColumn( s -> ModelTemplates.replaceVariant(s, "scribbles_1-side"),  s -> ModelTemplates.replaceVariant(s, "scribbles_0-top")));
        public static final VariantTemplate TWO = withName("scribbles_2", "Hieroglyphs 3", ModelTemplates.cubeColumn( s -> ModelTemplates.replaceVariant(s, "scribbles_2-side"),  s -> ModelTemplates.replaceVariant(s, "scribbles_0-top")));
        public static final VariantTemplate THREE = withName("scribbles_3", "Skull 1", ModelTemplates.cubeColumn( s -> ModelTemplates.replaceVariant(s, "scribbles_3-side"),  s -> ModelTemplates.replaceVariant(s, "scribbles_0-top")));
        public static final VariantTemplate FOUR = withName("scribbles_4", "Eye of Horus", ModelTemplates.cubeColumn( s -> ModelTemplates.replaceVariant(s, "scribbles_4-side"),  s -> ModelTemplates.replaceVariant(s, "scribbles_0-top")));
        public static final VariantTemplate FIVE = withName("scribbles_5", "Bird", ModelTemplates.cubeColumn( s -> ModelTemplates.replaceVariant(s, "scribbles_5-side"),  s -> ModelTemplates.replaceVariant(s, "scribbles_0-top")));
        public static final VariantTemplate SIX = withName("scribbles_6", "Halo", ModelTemplates.cubeColumn( s -> ModelTemplates.replaceVariant(s, "scribbles_6-side"),  s -> ModelTemplates.replaceVariant(s, "scribbles_0-top")));
        public static final VariantTemplate SEVEN = withName("scribbles_7", "Hieroglyphs 4", ModelTemplates.cubeColumn( s -> ModelTemplates.replaceVariant(s, "scribbles_7-side"),  s -> ModelTemplates.replaceVariant(s, "scribbles_0-top")));
        public static final VariantTemplate EIGHT = withName("scribbles_8", "Man with Staff", ModelTemplates.cubeColumn( s -> ModelTemplates.replaceVariant(s, "scribbles_8-side"),  s -> ModelTemplates.replaceVariant(s, "scribbles_0-top")));
        public static final VariantTemplate NINE = withName("scribbles_9", "Waves", ModelTemplates.cubeColumn( s -> ModelTemplates.replaceVariant(s, "scribbles_9-side"),  s -> ModelTemplates.replaceVariant(s, "scribbles_0-top")));
        public static final VariantTemplate TEN = withName("scribbles_10", "Landscape 1", ModelTemplates.cubeColumn( s -> ModelTemplates.replaceVariant(s, "scribbles_10-side"),  s -> ModelTemplates.replaceVariant(s, "scribbles_0-top")));
        public static final VariantTemplate ELEVEN = withName("scribbles_11", "Skull Landscape", ModelTemplates.cubeColumn( s -> ModelTemplates.replaceVariant(s, "scribbles_11-side"),  s -> ModelTemplates.replaceVariant(s, "scribbles_0-top")));
        public static final VariantTemplate TWELVE = withName("scribbles_12", "Pattern 1", ModelTemplates.cubeColumn( s -> ModelTemplates.replaceVariant(s, "scribbles_12-side"),  s -> ModelTemplates.replaceVariant(s, "scribbles_0-top")));
        public static final VariantTemplate THIRTEEN = withName("scribbles_13", "Pattern 2", ModelTemplates.cubeColumn( s -> ModelTemplates.replaceVariant(s, "scribbles_13-side"),  s -> ModelTemplates.replaceVariant(s, "scribbles_0-top")));
        public static final VariantTemplate FOURTEEN = withName("scribbles_14", "Hieroglyphs 5", ModelTemplates.cubeColumn( s -> ModelTemplates.replaceVariant(s, "scribbles_14-side"),  s -> ModelTemplates.replaceVariant(s, "scribbles_0-top")));
        public static final VariantTemplate FIFTEEN = withName("scribbles_15", "Hieroglyphs 6", ModelTemplates.cubeColumn( s -> ModelTemplates.replaceVariant(s, "scribbles_15-side"),  s -> ModelTemplates.replaceVariant(s, "scribbles_0-top")));

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
            .map(color -> simple(color.getSerializedName()))
            .collect(Collectors.toList()));
    
    @SuppressWarnings("null")
    public static final ImmutableList<VariantTemplate> colors(ModelTemplate template) {
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
    
    public static final ImmutableList<VariantTemplate> METAL = ofClass(Metal.class);
    public static final ImmutableList<VariantTemplate> METAL_TERRAIN = ofClass(MetalTerrain.class);

    public static final ImmutableList<VariantTemplate> STONE = ofClass(Stone.class);
    public static final ImmutableList<VariantTemplate> PILLAR = ofClass(Pillar.class);
    public static final ImmutableList<VariantTemplate> SCRIBBLES = ofClass(Scribbles.class);

    public static final ImmutableList<VariantTemplate> ROCK = ofClass(Rock.class);
    @SuppressWarnings("null")
    public static final ImmutableList<VariantTemplate> COBBLESTONE = ImmutableList.<VariantTemplate>builder()
            .addAll(ROCK)
            .add(withName("extra/emboss", "Embossed"))
            .add(withName("extra/indent", "Indent"))
            .add(withName("extra/marker", "Marker"))
            .build();
    
    private static ModelTemplate mossyModel(String base, VariantTemplate template) {
        if (template.getName().equals("circularct")) {
            return ModelTemplates.mossyCtm(base, "circular");
        } else if (template.getName().equals("pillar") || template.getName().equals("twisted")) {
            return ModelTemplates.mossyColumn(base);
        } else {
            return ModelTemplates.mossy(base);
        }
    }
    
    @SuppressWarnings("null")
    public static final ImmutableList<VariantTemplate> COBBLESTONE_MOSSY = ImmutableList.copyOf(COBBLESTONE.stream()
            .map(v -> SimpleTemplate.builderFrom(v)
                    .modelTemplate(mossyModel("cobblestone", v))
                    .build())
            .collect(Collectors.toList()));
    
    public static final ImmutableList<VariantTemplate> PLANKS = ofClass(Planks.class);

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
                        .requires(block, 9)
                        .unlockedBy("has_" + item.getRegistryName().getPath(), prov.has(item))
                        .save(prov, Chisel.MOD_ID + ":" + item.getRegistryName().getPath() + "_from_" + ((BlockCarvable)block).getVariation().getName())))
                .map(SimpleTemplate.SimpleTemplateBuilder::build)
                .collect(Collectors.toList()));
    }
}
