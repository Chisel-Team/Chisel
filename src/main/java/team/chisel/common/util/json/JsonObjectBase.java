package team.chisel.common.util.json;

import com.google.common.base.Supplier;

public abstract class JsonObjectBase<T> implements Supplier<T>{

    private T cache;

    public final T get() {
        return cache != null ? cache : (cache = create());
    }

    protected abstract T create();
}
