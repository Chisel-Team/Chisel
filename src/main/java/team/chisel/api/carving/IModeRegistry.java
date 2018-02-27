package team.chisel.api.carving;

import java.util.Collection;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public interface IModeRegistry {
    
    void registerMode(@Nonnull IChiselMode mode);
    
    @Nonnull
    Collection<IChiselMode> getAllModes();
    
    @Nullable
    IChiselMode getModeByName(String name);

}
