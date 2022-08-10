package team.chisel.common.init;

import java.util.List;
import java.util.function.Supplier;

import javax.annotation.Nonnull;

import com.google.common.collect.ImmutableList;
import com.tterrag.registrate.util.nullness.FieldsAreNonnullByDefault;
import com.tterrag.registrate.util.nullness.NonNullSupplier;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.data.worldgen.features.OreFeatures;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.GenerationStep.Decoration;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.placement.CountPlacement;
import net.minecraft.world.level.levelgen.placement.HeightRangePlacement;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;
import net.minecraft.world.level.levelgen.placement.PlacementModifierType;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import team.chisel.Chisel;
import team.chisel.Features;
import team.chisel.client.data.VariantTemplates;
import team.chisel.common.world.ReplaceBlockDownwardsFeature;
import team.chisel.common.world.ReplaceMultipleBlocksConfig;
import team.chisel.common.world.UnderLavaPlacement;

@FieldsAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class ChiselWorldGen {

    public static final DeferredRegister<Feature<?>> FEATURES = DeferredRegister.create(ForgeRegistries.FEATURES, Chisel.MOD_ID);
    public static final DeferredRegister<PlacementModifierType<?>> PLACEMENTS = DeferredRegister.create(Registry.PLACEMENT_MODIFIER_REGISTRY, Chisel.MOD_ID);

    public static final RegistryObject<ReplaceBlockDownwardsFeature> REPLACE_BLOCK_DOWNWARDS = FEATURES.register(
            "replace_block_downwards", () -> new ReplaceBlockDownwardsFeature(ReplaceMultipleBlocksConfig.CODEC));
    
    public static final RegistryObject<PlacementModifierType<UnderLavaPlacement>> PLACE_UNDER_LAVA = PLACEMENTS.register(
            "under_lava", () -> () -> UnderLavaPlacement.CODEC);
    
    // Basalt (under and around lava lakes)
    private static final NonNullSupplier<Holder<PlacedFeature>> DIABASE = createPlacedFeature("diabase",
            REPLACE_BLOCK_DOWNWARDS,
            () -> new ReplaceMultipleBlocksConfig(List.of(OreConfiguration.target(OreFeatures.NATURAL_STONE, Features.DIABASE.get(VariantTemplates.RAW.getName()).get().defaultBlockState()))),
            new UnderLavaPlacement());
    
    // Marble (33x *6 @ y24 +48)
    private static final NonNullSupplier<Holder<PlacedFeature>> LIMESTONE = createPlacedFeature("limestone",
            () -> Feature.ORE,
            () -> new OreConfiguration(OreFeatures.NATURAL_STONE, Features.LIMESTONE.get(VariantTemplates.RAW.getName()).get().defaultBlockState(), 33),
            CountPlacement.of(6), HeightRangePlacement.uniform(VerticalAnchor.absolute(64), VerticalAnchor.aboveBottom(48)));
    
    // Limestone (33x *6 @ y64 +48)
    private static final NonNullSupplier<Holder<PlacedFeature>> MARBLE = createPlacedFeature("marble",
            () -> Feature.ORE,
            () -> new OreConfiguration(OreFeatures.NATURAL_STONE, Features.MARBLE.get(VariantTemplates.RAW.getName()).get().defaultBlockState(), 33),
            CountPlacement.of(6), HeightRangePlacement.uniform(VerticalAnchor.absolute(24), VerticalAnchor.aboveBottom(48)));
    
    public static void registerWorldGen(BiomeLoadingEvent event) {
    	if (event.getName() == null) return;
        if (BiomeDictionary.hasType(ResourceKey.create(Registry.BIOME_REGISTRY, event.getName()), BiomeDictionary.Type.OVERWORLD)) {
            var features = event.getGeneration().getFeatures(Decoration.UNDERGROUND_ORES);
            features.add(0, DIABASE.get());
            features.addAll(List.of(LIMESTONE.get(), MARBLE.get()));
        }
    }
    
    private static <FC extends FeatureConfiguration, F extends Feature<FC>> NonNullSupplier<Holder<PlacedFeature>> createPlacedFeature(String name, Supplier<F> feature, Supplier<FC> config, PlacementModifier... modifiers) {
        return NonNullSupplier.lazy(() -> {
            Holder<ConfiguredFeature<?, ?>> configured = BuiltinRegistries.register(BuiltinRegistries.CONFIGURED_FEATURE, new ResourceLocation(Chisel.MOD_ID, name),
                    new ConfiguredFeature<>(feature.get(), config.get()));
            return BuiltinRegistries.register(BuiltinRegistries.PLACED_FEATURE, new ResourceLocation(Chisel.MOD_ID, name),
                    new PlacedFeature(Holder.hackyErase(configured), List.of(modifiers)));
        });
    }
}
