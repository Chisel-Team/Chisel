package team.chisel.common.util.json;

import com.google.common.base.Supplier;

public abstract class JsonObjectBase<T> implements Supplier<T> {

    private T cache;

    @Override
    public final T get() {
        return cache != null ? cache : (cache = create());
    }

    public abstract T create();
}
