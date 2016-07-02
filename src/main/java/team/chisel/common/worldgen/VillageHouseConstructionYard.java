package team.chisel.common.worldgen;

import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;
import net.minecraft.world.gen.structure.StructureVillagePieces;
import team.chisel.Chisel;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Random;

public class VillageHouseConstructionYard extends StructureVillagePieces.House1
{
    private int averageGroundLevel = -1;

    @SuppressWarnings("unused")
    public VillageHouseConstructionYard()
    {
        //TODO Choose random chisel blocks
    }

    public VillageHouseConstructionYard(StructureVillagePieces.Start startingPoint, int componentType, Random rand, StructureBoundingBox structureBBox, EnumFacing enumFacing)
    {
        super(startingPoint, componentType, rand, structureBBox, enumFacing);

        //TODO Choose random chisel blocks
    }

    public static VillageHouseConstructionYard buildComponent(StructureVillagePieces.Start startingPoint, List<StructureComponent> pieces, Random rand, int structureMinX, int structureMinY, int structureMinZ, EnumFacing enumFacing, int i3)
    {
        Chisel.logger.info("built house");

        StructureBoundingBox structureBBox = StructureBoundingBox.getComponentToAddBoundingBox(structureMinX, structureMinY, structureMinZ, 0, 0, 0, 10, 10, 10, enumFacing);

        return new VillageHouseConstructionYard(startingPoint, i3, rand, structureBBox, enumFacing);
    }

    @Override
    public boolean addComponentParts(@Nonnull World world, @Nonnull Random random, @Nonnull StructureBoundingBox structureBoundingBox) {
        Chisel.logger.info("added parts at ");

        if (averageGroundLevel < 0) {
            averageGroundLevel = getAverageGroundLevel(world, structureBoundingBox);
            if (averageGroundLevel < 0) {
                return true;
            }

            boundingBox.offset(0, averageGroundLevel - boundingBox.maxY + 9 - 1, 0);
        }

        fillWithBlocks(world, structureBoundingBox, 1, 1, 1, 9, 9, 9, Blocks.COAL_BLOCK.getDefaultState(), Blocks.EMERALD_BLOCK.getDefaultState(), false);

        return true;
    }
}
