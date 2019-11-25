package team.chisel.api.carving;

import java.util.List;

@FunctionalInterface
public interface ICarvingVariationProvider {
    
    List<ICarvingVariation> provide();
}
