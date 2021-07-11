package team.chisel.common.init;

import com.google.common.collect.ImmutableList;

import net.minecraft.block.Blocks;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.GenerationStage.Decoration;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.OreFeatureConfig;
import net.minecraft.world.gen.placement.NoPlacementConfig;
import net.minecraft.world.gen.placement.Placement;
import net.minecraft.world.gen.placement.TopSolidRangeConfig;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import team.chisel.Chisel;
import team.chisel.Features;
import team.chisel.client.data.VariantTemplates;
import team.chisel.common.world.ReplaceBlockDownwardsFeature;
import team.chisel.common.world.ReplaceMultipleBlocksConfig;
import team.chisel.common.world.UnderLavaPlacement;

public class ChiselWorldGen {

    public static final DeferredRegister<Feature<?>> FEATURES = DeferredRegister.create(ForgeRegistries.FEATURES, Chisel.MOD_ID);
    public static final DeferredRegister<Placement<?>> PLACEMENTS = DeferredRegister.create(ForgeRegistries.DECORATORS, Chisel.MOD_ID);

    public static final RegistryObject<ReplaceBlockDownwardsFeature> REPLACE_BLOCK_DOWNWARDS = FEATURES.register(
            "replace_block_downwards", () -> new ReplaceBlockDownwardsFeature(ReplaceMultipleBlocksConfig.CODEC));
    
    public static final RegistryObject<UnderLavaPlacement> PLACE_UNDER_LAVA = PLACEMENTS.register(
            "under_lava", () -> new UnderLavaPlacement());

    public static void registerWorldGen(BiomeLoadingEvent event) {
    	if (event.getName() == null) return;
        if (BiomeDictionary.hasType(RegistryKey.getOrCreateKey(Registry.BIOME_KEY, event.getName()), BiomeDictionary.Type.OVERWORLD)) {
            // Basalt (under and around lava lakes)
            event.getGeneration().getFeatures(Decoration.UNDERGROUND_ORES).add(() -> REPLACE_BLOCK_DOWNWARDS.get()
                    .withConfiguration(new ReplaceMultipleBlocksConfig(ImmutableList.of(Blocks.STONE.getDefaultState(), Blocks.ANDESITE.getDefaultState(), Blocks.GRANITE.getDefaultState(), Blocks.DIORITE.getDefaultState()), Features.DIABASE.get(VariantTemplates.RAW.getName()).get().getDefaultState()))
                    .withPlacement(PLACE_UNDER_LAVA.get().configure(NoPlacementConfig.NO_PLACEMENT_CONFIG)));
            // Limestone (33x *6 @ y64 +48)
            event.getGeneration().getFeatures(Decoration.UNDERGROUND_ORES).add(() -> Feature.ORE
                    .withConfiguration(new OreFeatureConfig(OreFeatureConfig.FillerBlockType.BASE_STONE_OVERWORLD, Features.LIMESTONE.get(VariantTemplates.RAW.getName()).get().getDefaultState(), 33))
                	.withPlacement(Placement.RANGE.configure(new TopSolidRangeConfig(64, 0, 48)))
                    .count(6));
            // Marble (33x *6 @ y24 +48)
            event.getGeneration().getFeatures(Decoration.UNDERGROUND_ORES).add(() -> Feature.ORE
                    .withConfiguration(new OreFeatureConfig(OreFeatureConfig.FillerBlockType.BASE_STONE_OVERWORLD, Features.MARBLE.get(VariantTemplates.RAW.getName()).get().getDefaultState(), 33))
                    .withPlacement(Placement.RANGE.configure(new TopSolidRangeConfig(24, 0, 48)))
                    .count(6));
        }
    }
}
