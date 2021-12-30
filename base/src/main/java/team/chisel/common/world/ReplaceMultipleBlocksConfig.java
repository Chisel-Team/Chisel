package team.chisel.common.world;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;

public class ReplaceMultipleBlocksConfig implements FeatureConfiguration {

	public static final Codec<ReplaceMultipleBlocksConfig> CODEC = RecordCodecBuilder.create(instance ->
			instance.group(
					BlockState.CODEC.listOf().fieldOf("toReplace").forGetter(c -> new ArrayList<>(c.toReplace)),
					BlockState.CODEC.fieldOf("result").forGetter(c -> c.result))
			.apply(instance, ReplaceMultipleBlocksConfig::new));
					
   public final Set<BlockState> toReplace;
   public final BlockState result;

   public ReplaceMultipleBlocksConfig(List<BlockState> toReplace, BlockState result) {
      this.toReplace = new HashSet<>(toReplace);
      this.result = result;
   }
}