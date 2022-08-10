package team.chisel.api;

import com.tterrag.registrate.builders.MenuBuilder.MenuFactory;
import com.tterrag.registrate.builders.MenuBuilder.ScreenFactory;
import com.tterrag.registrate.util.entry.RegistryEntry;
import com.tterrag.registrate.util.nullness.NonNullSupplier;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import team.chisel.Chisel;
import team.chisel.client.gui.GuiChisel;
import team.chisel.client.gui.GuiHitechChisel;
import team.chisel.common.inventory.ChiselContainer;
import team.chisel.common.inventory.ContainerChiselHitech;
import team.chisel.common.inventory.InventoryChiselSelection;

import javax.annotation.Nullable;

public interface IChiselGuiType<T extends ChiselContainer> {

    MenuType<? extends ChiselContainer> getContainerType();

    AbstractContainerMenu createMenu(int windowId, Inventory inv, Player player, InteractionHand hand);

    default MenuProvider provide(ItemStack stack, InteractionHand hand) {
        return new MenuProvider() {

            @Override
            @Nullable
            public AbstractContainerMenu createMenu(int windowId, Inventory inv, Player player) {
                return IChiselGuiType.this.createMenu(windowId, inv, player, hand);
            }

            @Override
            public Component getDisplayName() {
                return stack.getHoverName();
            }
        };
    }

    enum ChiselGuiType implements IChiselGuiType<ChiselContainer> {
        NORMAL("chisel_container_normal", ChiselContainer::new, () -> (container, inv, displayName) -> new GuiChisel<>(container, inv, displayName)) {
            @Override
            @Nullable
            public AbstractContainerMenu createMenu(int windowId, Inventory inv, Player player, InteractionHand hand) {
                return new ChiselContainer(getContainerType(), windowId, inv, new InventoryChiselSelection(player.getItemInHand(hand), 60), hand);
            }
        },
        HITECH("chisel_container_hitech", ContainerChiselHitech::new, () -> (container, inv, displayName) -> new GuiHitechChisel(container, inv, displayName)) {
            @Override
            @Nullable
            public AbstractContainerMenu createMenu(int windowId, Inventory inv, Player player, InteractionHand hand) {
                return new ContainerChiselHitech(getContainerType(), windowId, inv, new InventoryChiselSelection(player.getItemInHand(hand), 63), hand);
            }
        };

        private final RegistryEntry<? extends MenuType<? extends ChiselContainer>> type;

        <C extends ChiselContainer, T extends GuiChisel<C>> ChiselGuiType(String name, MenuFactory<C> factory, NonNullSupplier<ScreenFactory<C, T>> screenFactory) {
            this.type = Chisel.registrate()
                    .menu(name, factory, screenFactory)
                    .register();
        }

        @Override
        public MenuType<? extends ChiselContainer> getContainerType() {
            return type.get();
        }
    }

}
