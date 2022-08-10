package team.chisel.api.carving;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collection;

public interface IModeRegistry {

    void registerMode(@Nonnull IChiselMode mode);

    @Nonnull
    Collection<IChiselMode> getAllModes();

    @Nullable
    IChiselMode getModeByName(String name);

}
