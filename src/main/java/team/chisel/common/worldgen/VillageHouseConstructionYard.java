package team.chisel.common.worldgen;

import net.minecraft.block.BlockLever;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.BlockStoneSlab;
import net.minecraft.block.BlockTorch;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;
import net.minecraft.world.gen.structure.StructureVillagePieces;
import team.chisel.common.init.ChiselBlocks;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Random;

public class VillageHouseConstructionYard extends StructureVillagePieces.House1
{

    private final int[] pillarIterations = {1, 7, 13, 19};
    private final int[] torchIterations = {4, 10, 16};

    private final int camperXSize = 10;
    private final int camperZSize = 4;
    private final int wallOffset = 3;

    private final IBlockState airState = Blocks.AIR.getDefaultState();

    private int surplusPlacementX;
    private int surplusPlacementZ;
    private int camperPlacementX;
    private int averageGroundLevel = -1;

    @SuppressWarnings("unused")
    public VillageHouseConstructionYard()
    {
        //TODO Choose random chisel blocks
    }

    public VillageHouseConstructionYard(StructureVillagePieces.Start startingPoint, int componentType, Random rand, StructureBoundingBox structureBBox, EnumFacing enumFacing)
    {
        super(startingPoint, componentType, rand, structureBBox, enumFacing);

        surplusPlacementX = rand.nextInt(2)+2+9;
        surplusPlacementZ = rand.nextInt(11)+2;

        camperPlacementX = rand.nextInt(structureBBox.getXSize()-camperXSize-(wallOffset*2)) + wallOffset;

        //TODO Choose random chisel blocks
    }

    public static VillageHouseConstructionYard buildComponent(StructureVillagePieces.Start startingPoint, List<StructureComponent> pieces, Random rand, int structureMinX, int structureMinY, int structureMinZ, EnumFacing enumFacing, int i3)
    {
        //Chisel.logger.info("built house");

        StructureBoundingBox structureBBox = StructureBoundingBox.getComponentToAddBoundingBox(structureMinX, structureMinY, structureMinZ, 0, 0, 0, 20, 8, 20, enumFacing);

        return new VillageHouseConstructionYard(startingPoint, i3, rand, structureBBox, enumFacing);
    }

    @Override
    public boolean addComponentParts(@Nonnull World world, @Nonnull Random random, @Nonnull StructureBoundingBox structureBoundingBox) {
        //Chisel.logger.info("added parts");

        if (averageGroundLevel < 0) {
            averageGroundLevel = getAverageGroundLevel(world, structureBoundingBox);
            if (averageGroundLevel < 0) {
                return true;
            }

            boundingBox.offset(0, averageGroundLevel - boundingBox.maxY + 9 - 2, 0);
        }

        fillUnder(world, structureBoundingBox);

        fillWithBlocks(world, structureBoundingBox, 1, -1, 1, 19, 8, 19, airState, airState, false);

        makeYard(world, structureBoundingBox);

        //makeSurplus(world, structureBoundingBox, random);

        makeCamper(world, structureBoundingBox, random);

        IBlockState slabBottom = Blocks.STONE_SLAB.getDefaultState();

        for(int i = 1; i < 19; i++)
        {
            setBlockState(world, slabBottom, i, 2, 1, structureBoundingBox);
            setBlockState(world, slabBottom, i, 2, 19, structureBoundingBox);

            setBlockState(world, slabBottom, 1, 2, i, structureBoundingBox);
            setBlockState(world, slabBottom, 19, 2, i, structureBoundingBox);
        }

        IBlockState pillarMaterial = Blocks.LOG.getDefaultState();
        IBlockState torchUpright = Blocks.TORCH.getDefaultState().withProperty(BlockTorch.FACING, EnumFacing.UP);

        for (int pillarIteration1 : pillarIterations) {
            for (int pillarIteration2 : pillarIterations) {
                for (int height = 0; height < 3; height++) {
                    if(((pillarIteration1 == 1)||(pillarIteration1 == 19))||((pillarIteration2 == 1)||(pillarIteration2 == 19)))
                        setBlockState(world, pillarMaterial, pillarIteration1, height, pillarIteration2, structureBoundingBox);
                }
                if(((pillarIteration1 == 1)||(pillarIteration1 == 19))||((pillarIteration2 == 1)||(pillarIteration2 == 19)))
                    setBlockState(world, torchUpright, pillarIteration1, 3, pillarIteration2, structureBoundingBox);
            }
        }

        for (int torchIteration1 : torchIterations) {
            for (int torchIteration2 : torchIterations) {
                setBlockState(world, torchUpright, torchIteration1, 0, torchIteration2, structureBoundingBox);
            }
        }

        fillWithBlocks(world, structureBoundingBox, 8, 0, 1, 12, 5, 1, airState, airState, false);

        return true;
    }

    private void fillUnder(World world, StructureBoundingBox structureBoundingBox)
    {
        for(int i1 = 1; i1 < 20; i1++)
        {
            for(int i2 = 1; i2 < 20; i2++)
            {
                for(int height = -1; height >= -10 ; height--)
                {
                    if(this.getBlockStateFromPos(world, i1, height, i2, structureBoundingBox).getMaterial() == Material.AIR)
                    {
                        setBlockState(world, Blocks.COBBLESTONE.getDefaultState(), i1, height, i2, structureBoundingBox);
                    }
                    else
                    {
                        break;
                    }
                }
            }
        }

        for(int i1 = 8; i1 < 13; i1++)
        {
            for(int height = -1; height >= -10 ; height--)
            {
                if(this.getBlockStateFromPos(world, i1, height, 0, structureBoundingBox).getMaterial() == Material.AIR)
                {
                    setBlockState(world, Blocks.GRAVEL.getDefaultState(), i1, height, 0, structureBoundingBox);
                }
                else
                {
                    break;
                }
            }
        }
    }

    private void makeYard(World world, StructureBoundingBox structureBoundingBox) {
        fillWithBlocks(world, structureBoundingBox, 1, -1, 1, 19, 2, 19, Blocks.OAK_FENCE.getDefaultState(), airState, false);
        fillWithBlocks(world, structureBoundingBox, 2, -1, 2, 18, 2, 18, airState, airState, false);

        fillWithBlocks(world, structureBoundingBox, 1, -1, 1, 19, -1, 19, Blocks.COBBLESTONE.getDefaultState(), Blocks.COBBLESTONE.getDefaultState(), false);

        fillWithBlocks(world, structureBoundingBox, 8, -1, 0, 12, -1, 0, Blocks.GRAVEL.getDefaultState(), Blocks.COBBLESTONE.getDefaultState(), false);
    }

    private void makeSurplus(World world, StructureBoundingBox structureBoundingBox, Random random) {

        fillWithBlocks(world, structureBoundingBox, surplusPlacementX, 0, surplusPlacementZ, surplusPlacementX+2, 2, surplusPlacementZ+2, Blocks.END_STONE.getDefaultState(), Blocks.END_STONE.getDefaultState(), false);
    }

    private void makeCamper(World world, StructureBoundingBox structureBoundingBox, Random random) {
        int zOffset = 10;

        // Place Base
        fillWithBlocks(world, structureBoundingBox, camperPlacementX+wallOffset, 1, wallOffset+zOffset, camperPlacementX+camperXSize+wallOffset-1, 1, wallOffset+camperZSize+zOffset, ChiselBlocks.sandstoneyellow.getStateFromMeta(7), ChiselBlocks.sandstoneyellow.getStateFromMeta(7), false);

        // Slabs under Base
        fillWithBlocks(world, structureBoundingBox, camperPlacementX+wallOffset, 0, wallOffset+zOffset, camperPlacementX+camperXSize+wallOffset-2, 0, wallOffset+camperZSize+zOffset, Blocks.STONE_SLAB.getDefaultState(), Blocks.STONE_SLAB.getDefaultState().withProperty(BlockStoneSlab.VARIANT, BlockStoneSlab.EnumType.SAND).withProperty(BlockSlab.HALF, BlockSlab.EnumBlockHalf.TOP), false);

        // Wheels
        setBlockState(world, ChiselBlocks.basalt.getStateFromMeta(11), camperPlacementX+wallOffset+1, 0, wallOffset+zOffset, structureBoundingBox);
        setBlockState(world, ChiselBlocks.basalt.getStateFromMeta(11), camperPlacementX+wallOffset+1, 0, wallOffset+zOffset+4, structureBoundingBox);
        setBlockState(world, ChiselBlocks.basalt.getStateFromMeta(11), camperPlacementX+wallOffset+6, 0, wallOffset+zOffset, structureBoundingBox);
        setBlockState(world, ChiselBlocks.basalt.getStateFromMeta(11), camperPlacementX+wallOffset+6, 0, wallOffset+zOffset+4, structureBoundingBox);

        // Body, with red stripes
        fillWithBlocks(world, structureBoundingBox, camperPlacementX+wallOffset, 2, wallOffset+zOffset, camperPlacementX+camperXSize+wallOffset, 2, wallOffset+camperZSize+zOffset, ChiselBlocks.sandstonered.getStateFromMeta(15), ChiselBlocks.sandstonered.getStateFromMeta(15), false);
        fillWithBlocks(world, structureBoundingBox, camperPlacementX+wallOffset+1, 3, wallOffset+zOffset, camperPlacementX+camperXSize+wallOffset, 3, wallOffset+camperZSize+zOffset, ChiselBlocks.sandstoneyellow.getStateFromMeta(7), ChiselBlocks.sandstoneyellow.getStateFromMeta(7), false);
        fillWithBlocks(world, structureBoundingBox, camperPlacementX+wallOffset+1, 4, wallOffset+zOffset, camperPlacementX+camperXSize+wallOffset, 4, wallOffset+camperZSize+zOffset, ChiselBlocks.sandstonered.getStateFromMeta(15), ChiselBlocks.sandstonered.getStateFromMeta(15), false);
        fillWithBlocks(world, structureBoundingBox, camperPlacementX+wallOffset+2, 5, wallOffset+zOffset, camperPlacementX+camperXSize+wallOffset, 5, wallOffset+camperZSize+zOffset, Blocks.STONE_SLAB.getDefaultState().withProperty(BlockStoneSlab.VARIANT, BlockStoneSlab.EnumType.SAND), Blocks.STONE_SLAB.getDefaultState().withProperty(BlockStoneSlab.VARIANT, BlockStoneSlab.EnumType.SAND), false);

        // Shade
        fillWithBlocks(world, structureBoundingBox, camperPlacementX+wallOffset+1, 4, wallOffset+zOffset-1, camperPlacementX+camperXSize+wallOffset, 4, wallOffset+zOffset-1, Blocks.PLANKS.getDefaultState(), Blocks.PLANKS.getDefaultState(), false);
        fillWithBlocks(world, structureBoundingBox, camperPlacementX+wallOffset+1, 4, wallOffset+zOffset-2, camperPlacementX+camperXSize+wallOffset, 4, wallOffset+zOffset-2, Blocks.WOODEN_SLAB.getDefaultState(), Blocks.WOODEN_SLAB.getDefaultState(), false);

        // Supports
        setBlockState(world, Blocks.LEVER.getDefaultState().withProperty(BlockLever.FACING, BlockLever.EnumOrientation.SOUTH), camperPlacementX+wallOffset+1, 3, wallOffset+zOffset-1, structureBoundingBox);

        // Interior ---------------

        // Clear inside
        fillWithBlocks(world, structureBoundingBox, camperPlacementX+wallOffset+1, 2, wallOffset+zOffset+1, camperPlacementX+camperXSize+wallOffset-1, 4, wallOffset+camperZSize+zOffset-1, airState, airState, false);

        // Door
        this.placeDoorCurrentPosition(world, structureBoundingBox, random, camperPlacementX+wallOffset+3, 2, wallOffset+zOffset, EnumFacing.NORTH);

        // Upper windshield
        fillWithBlocks(world, structureBoundingBox, camperPlacementX+wallOffset+1, 4, wallOffset+zOffset+1, camperPlacementX+wallOffset+1, 4, wallOffset+zOffset+3, ChiselBlocks.sandstonered.getStateFromMeta(15), ChiselBlocks.sandstonered.getStateFromMeta(15), false);



        // Debug - size -----------

        setBlockState(world, Blocks.REDSTONE_BLOCK.getDefaultState(), camperPlacementX+wallOffset, 6, wallOffset+zOffset, structureBoundingBox);
        setBlockState(world, Blocks.LAPIS_BLOCK.getDefaultState(), camperPlacementX+camperXSize+wallOffset, 6, wallOffset+camperZSize+zOffset, structureBoundingBox);
    }
}
