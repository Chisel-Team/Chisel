package team.chisel.common.carving;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.annotation.Nonnull;

import team.chisel.api.carving.IChiselMode;
import team.chisel.api.carving.IModeRegistry;

public enum ChiselModeRegistry implements IModeRegistry {
    
    INSTANCE;
    
    private final Map<String, IChiselMode> modes = new LinkedHashMap<>();

    @Override
    public void registerMode(@Nonnull IChiselMode mode) {
        this.modes.put(mode.name(), mode);
    }

    @SuppressWarnings("null")
    @Override
    public @Nonnull Collection<IChiselMode> getAllModes() {
        return Collections.unmodifiableCollection(modes.values());
    }

    @Override
    public IChiselMode getModeByName(String name) {
        return modes.get(name);
    }

}
