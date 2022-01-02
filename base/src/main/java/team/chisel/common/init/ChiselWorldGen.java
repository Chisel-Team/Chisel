package team.chisel.common.init;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Codec;

import net.minecraft.core.Registry;
import net.minecraft.data.worldgen.features.OreFeatures;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.GenerationStep.Decoration;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.placement.CountPlacement;
import net.minecraft.world.level.levelgen.placement.HeightRangePlacement;
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

public class ChiselWorldGen {

    public static final DeferredRegister<Feature<?>> FEATURES = DeferredRegister.create(ForgeRegistries.FEATURES, Chisel.MOD_ID);
//    public static final DeferredRegister<PlacementModifier<?>> PLACEMENTS = DeferredRegister.create(DECORATORS, Chisel.MOD_ID);

    public static final RegistryObject<ReplaceBlockDownwardsFeature> REPLACE_BLOCK_DOWNWARDS = FEATURES.register(
            "replace_block_downwards", () -> new ReplaceBlockDownwardsFeature(ReplaceMultipleBlocksConfig.CODEC));
    
    public static final PlacementModifierType<UnderLavaPlacement> PLACE_UNDER_LAVA = PlacementModifierType.register(
            "under_lava", Codec.unit(new UnderLavaPlacement()));

    public static void registerWorldGen(BiomeLoadingEvent event) {
    	if (event.getName() == null) return;
        if (BiomeDictionary.hasType(ResourceKey.create(Registry.BIOME_REGISTRY, event.getName()), BiomeDictionary.Type.OVERWORLD)) {
            // Basalt (under and around lava lakes)
            event.getGeneration().getFeatures(Decoration.UNDERGROUND_ORES).add(0, () -> REPLACE_BLOCK_DOWNWARDS.get()
                    .configured(new ReplaceMultipleBlocksConfig(ImmutableList.of(Blocks.STONE.defaultBlockState(), Blocks.ANDESITE.defaultBlockState(), Blocks.GRANITE.defaultBlockState(), Blocks.DIORITE.defaultBlockState()), Features.DIABASE.get(VariantTemplates.RAW.getName()).get().defaultBlockState()))
                    .placed(new UnderLavaPlacement()));
            // Limestone (33x *6 @ y64 +48)
            event.getGeneration().getFeatures(Decoration.UNDERGROUND_ORES).add(() -> Feature.ORE
                    .configured(new OreConfiguration(OreFeatures.NATURAL_STONE, Features.LIMESTONE.get(VariantTemplates.RAW.getName()).get().defaultBlockState(), 33))
                	.placed(CountPlacement.of(6), HeightRangePlacement.uniform(VerticalAnchor.absolute(64), VerticalAnchor.aboveBottom(48))));
            // Marble (33x *6 @ y24 +48)
            event.getGeneration().getFeatures(Decoration.UNDERGROUND_ORES).add(() -> Feature.ORE
                    .configured(new OreConfiguration(OreFeatures.NATURAL_STONE, Features.MARBLE.get(VariantTemplates.RAW.getName()).get().defaultBlockState(), 33))
                    .placed(CountPlacement.of(6), HeightRangePlacement.uniform(VerticalAnchor.absolute(24), VerticalAnchor.aboveBottom(48))));
        }
    }
}
