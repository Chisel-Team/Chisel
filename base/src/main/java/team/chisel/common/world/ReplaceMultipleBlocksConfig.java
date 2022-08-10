package team.chisel.common.world;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("ClassCanBeRecord")
public class ReplaceMultipleBlocksConfig implements FeatureConfiguration {

    public static final Codec<ReplaceMultipleBlocksConfig> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                            OreConfiguration.TargetBlockState.CODEC.listOf().fieldOf("toReplace").forGetter(c -> c.targetStates))
                    .apply(instance, ReplaceMultipleBlocksConfig::new));

    public final List<OreConfiguration.TargetBlockState> targetStates;

    public ReplaceMultipleBlocksConfig(List<OreConfiguration.TargetBlockState> targetStates) {
        this.targetStates = new ArrayList<>(targetStates);
    }
}