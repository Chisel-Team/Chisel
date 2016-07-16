package team.chisel.common.worldgen;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.gen.structure.MapGenStructureIO;
import net.minecraft.world.gen.structure.StructureComponent;
import net.minecraft.world.gen.structure.StructureVillagePieces;
import net.minecraftforge.fml.common.registry.VillagerRegistry;

import java.util.List;
import java.util.Random;

public class VillageCreationConstructionYard implements VillagerRegistry.IVillageCreationHandler {

    public static void registerVillageComponents() {
        MapGenStructureIO.registerStructureComponent(VillageHouseConstructionYard.class, "Chisel:Yard");
    }

    @Override
    public StructureVillagePieces.PieceWeight getVillagePieceWeight(Random random, int size) {
        return new StructureVillagePieces.PieceWeight(VillageHouseConstructionYard.class, 25, MathHelper.getRandomIntegerInRange(random, size, 1 + size));
    }

    @Override
    public Class<?> getComponentClass() {
        return VillageHouseConstructionYard.class;
    }

    @Override
    public StructureVillagePieces.Village buildComponent(StructureVillagePieces.PieceWeight pieceWeight, StructureVillagePieces.Start start, List<StructureComponent> list, Random random, int i, int i1, int i2, EnumFacing enumFacing, int i3) {
        return VillageHouseConstructionYard.buildComponent(start, list, random, i, i1, i2, enumFacing, i3);
    }
}
