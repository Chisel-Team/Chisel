package team.chisel.common.world;

import java.util.Set;
import java.util.stream.Collectors;

import com.google.common.collect.ImmutableMap;
import com.mojang.datafixers.Dynamic;
import com.mojang.datafixers.types.DynamicOps;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.world.gen.feature.IFeatureConfig;

public class ReplaceMultipleBlocksConfig implements IFeatureConfig {
   public final Set<BlockState> toReplace;
   public final BlockState result;

   public ReplaceMultipleBlocksConfig(Set<BlockState> toReplace, BlockState result) {
      this.toReplace = toReplace;
      this.result = result;
   }

   public <T> Dynamic<T> serialize(DynamicOps<T> ops) {
      return new Dynamic<>(ops, ops.createMap(ImmutableMap.of(
                      ops.createString("toReplace"),
                      ops.createList(toReplace.stream()
                              .map(s -> BlockState.serialize(ops, s).getValue())),
                      ops.createString("result"),
                      BlockState.serialize(ops, this.result).getValue())));
   }

   public static <T> ReplaceMultipleBlocksConfig deserialize(Dynamic<T> p_214657_0_) {
      Set<BlockState> toReplace = p_214657_0_.get("toReplace").asStream().map(BlockState::deserialize).collect(Collectors.toSet());
      BlockState target = p_214657_0_.get("result").map(BlockState::deserialize).orElse(Blocks.AIR.getDefaultState());
      return new ReplaceMultipleBlocksConfig(toReplace, target);
   }
}