package team.chisel.common.util;

import com.tterrag.registrate.Registrate;
import com.tterrag.registrate.builders.AbstractBuilder;
import com.tterrag.registrate.builders.BuilderCallback;
import com.tterrag.registrate.util.nullness.NonnullType;

import net.minecraft.client.gui.IHasContainer;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;

public class ContainerBuilder<T extends Container, S extends Screen & IHasContainer<T>,  P> extends AbstractBuilder<ContainerType<?>, ContainerType<T>, P, ContainerBuilder<T, S, P>> {
    
    public interface ContainerFactory<T extends Container> {
        
        T create(ContainerType<T> type, int windowId, PlayerInventory inv);
    }
    
    public interface ScreenFactory<C extends Container, T extends Screen & IHasContainer<C>> {
        
        T create(C container, PlayerInventory inv, ITextComponent displayName);
    }
    
    private final ContainerFactory<T> factory;
    private final ScreenFactory<T, S> screenFactory;

    public ContainerBuilder(Registrate owner, P parent, String name, BuilderCallback callback, ContainerFactory<T> factory, ScreenFactory<T, S> screenFactory) {
        super(owner, parent, name, callback, ContainerType.class);
        this.factory = factory;
        this.screenFactory = screenFactory;
    }

    @Override
    protected @NonnullType ContainerType<T> createEntry() {
        ContainerType<T> ret = new ContainerType<>((windowId, inv) -> factory.create(get().get(), windowId, inv));
        DistExecutor.runWhenOn(Dist.CLIENT, () -> () -> ScreenManager.registerFactory(ret, (type, inv, displayName) -> screenFactory.create(type, inv, displayName)));
        return ret;
    }
}
