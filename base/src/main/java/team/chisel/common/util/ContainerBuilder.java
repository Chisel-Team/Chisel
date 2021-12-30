package team.chisel.common.util;

import java.util.function.Supplier;

import com.tterrag.registrate.Registrate;
import com.tterrag.registrate.builders.AbstractBuilder;
import com.tterrag.registrate.builders.BuilderCallback;
import com.tterrag.registrate.util.nullness.NonnullType;

import net.minecraft.client.gui.screens.inventory.MenuAccess;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;

public class ContainerBuilder<T extends AbstractContainerMenu, S extends Screen & MenuAccess<T>,  P> extends AbstractBuilder<MenuType<?>, MenuType<T>, P, ContainerBuilder<T, S, P>> {
    
    public interface ContainerFactory<T extends AbstractContainerMenu> {
        
        T create(MenuType<T> type, int windowId, Inventory inv);
    }
    
    public interface ScreenFactory<C extends AbstractContainerMenu, T extends Screen & MenuAccess<C>> {
        
        T create(C container, Inventory inv, Component displayName);
    }
    
    private final ContainerFactory<T> factory;
    private final Supplier<ScreenFactory<T, S>> screenFactory;

    public ContainerBuilder(Registrate owner, P parent, String name, BuilderCallback callback, ContainerFactory<T> factory, Supplier<ScreenFactory<T, S>> screenFactory) {
        super(owner, parent, name, callback, MenuType.class);
        this.factory = factory;
        this.screenFactory = screenFactory;
    }

    @Override
    protected @NonnullType MenuType<T> createEntry() {
        MenuType<T> ret = new MenuType<>((windowId, inv) -> factory.create(getEntry(), windowId, inv));
        DistExecutor.runWhenOn(Dist.CLIENT, () -> () -> MenuScreens.register(ret, screenFactory.get()::create));
        return ret;
    }
}
