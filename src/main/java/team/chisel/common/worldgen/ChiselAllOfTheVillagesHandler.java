package team.chisel.common.worldgen;

import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Biomes;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.event.terraingen.BiomeEvent;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import team.chisel.api.carving.CarvingUtils;
import team.chisel.api.carving.ICarvingGroup;
import team.chisel.api.carving.ICarvingVariation;

import java.util.List;
import java.util.Random;

public class ChiselAllOfTheVillagesHandler {

    private static final int chanceToChisel = 20;
    private static Random rng = new Random();

    @SubscribeEvent(priority = EventPriority.LOW)
    public void getVillageBlockID(BiomeEvent.GetVillageBlockID event) {
        if (event.getOriginal() == null) {
            return;
        }

        IBlockState originalState = event.getOriginal();

        if (event.getReplacement() != originalState) {
            originalState = event.getReplacement();
        }

        final Biome eventBiome = event.getBiome();
        IBlockState newState = originalState;

        if (!(originalState == null) && (!(originalState.getMaterial() == Material.AIR) || !originalState.getBlock().equals(Blocks.AIR))) {
            if (eventBiome == Biomes.DESERT) {
                if (originalState.getBlock() == Blocks.LOG || originalState.getBlock() == Blocks.LOG2) {
                    newState = Blocks.SANDSTONE.getDefaultState();
                }

                if (originalState.getBlock() == Blocks.COBBLESTONE) {
                    newState = Blocks.SANDSTONE.getStateFromMeta(BlockSandStone.EnumType.DEFAULT.getMetadata());
                }

                if (originalState.getBlock() == Blocks.PLANKS) {
                    newState = Blocks.SANDSTONE.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata());
                }

                if (originalState.getBlock() == Blocks.OAK_STAIRS) {
                    newState = Blocks.SANDSTONE_STAIRS.getDefaultState().withProperty(BlockStairs.FACING, originalState.getValue(BlockStairs.FACING));
                }

                if (originalState.getBlock() == Blocks.STONE_STAIRS) {
                    newState = Blocks.SANDSTONE_STAIRS.getDefaultState().withProperty(BlockStairs.FACING, originalState.getValue(BlockStairs.FACING));
                }

                if (originalState.getBlock() == Blocks.GRAVEL) {
                    newState = Blocks.SANDSTONE.getDefaultState();
                }
            } else if (eventBiome == Biomes.TAIGA) {
                if (originalState.getBlock() == Blocks.LOG || originalState.getBlock() == Blocks.LOG2) {
                    newState = Blocks.LOG.getDefaultState().withProperty(BlockOldLog.VARIANT, BlockPlanks.EnumType.SPRUCE).withProperty(BlockLog.LOG_AXIS, originalState.getValue(BlockLog.LOG_AXIS));
                }

                if (originalState.getBlock() == Blocks.PLANKS) {
                    newState = Blocks.PLANKS.getDefaultState().withProperty(BlockPlanks.VARIANT, BlockPlanks.EnumType.SPRUCE);
                }

                if (originalState.getBlock() == Blocks.OAK_STAIRS) {
                    newState = Blocks.SPRUCE_STAIRS.getDefaultState().withProperty(BlockStairs.FACING, originalState.getValue(BlockStairs.FACING));
                }

                if (originalState.getBlock() == Blocks.OAK_FENCE) {
                    newState = Blocks.SPRUCE_FENCE.getDefaultState();
                }
            } else if (eventBiome == Biomes.SAVANNA) {
                if (originalState.getBlock() == Blocks.LOG || originalState.getBlock() == Blocks.LOG2) {
                    newState = Blocks.LOG2.getDefaultState().withProperty(BlockNewLog.VARIANT, BlockPlanks.EnumType.ACACIA).withProperty(BlockLog.LOG_AXIS, originalState.getValue(BlockLog.LOG_AXIS));
                }

                if (originalState.getBlock() == Blocks.PLANKS) {
                    newState = Blocks.PLANKS.getDefaultState().withProperty(BlockPlanks.VARIANT, BlockPlanks.EnumType.ACACIA);
                }

                if (originalState.getBlock() == Blocks.OAK_STAIRS) {
                    newState = Blocks.ACACIA_STAIRS.getDefaultState().withProperty(BlockStairs.FACING, originalState.getValue(BlockStairs.FACING));
                }

                if (originalState.getBlock() == Blocks.COBBLESTONE) {
                    newState = Blocks.LOG2.getDefaultState().withProperty(BlockNewLog.VARIANT, BlockPlanks.EnumType.ACACIA).withProperty(BlockLog.LOG_AXIS, BlockLog.EnumAxis.Y);
                }

                if (originalState.getBlock() == Blocks.OAK_FENCE) {
                    newState = Blocks.ACACIA_FENCE.getDefaultState();
                }
            }

            if ((rng.nextInt(chanceToChisel) == 1) && (Item.getItemFromBlock(originalState.getBlock()) != null)) {
                ICarvingGroup group = CarvingUtils.getChiselRegistry().getGroup(newState);

                if (group != null) {
                    List<ICarvingVariation> variations = group.getVariations();

                    if (variations != null) {
                        newState = variations.get(rng.nextInt(variations.size())).getBlockState();
                    }
                }
            }

            if (newState != originalState) {
                event.setReplacement(newState);

                event.setResult(Event.Result.DENY);
            }
        }
    }
}
