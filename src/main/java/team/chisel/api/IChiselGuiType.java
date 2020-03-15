package team.chisel.api;

import java.util.function.Supplier;

import javax.annotation.Nullable;

import com.tterrag.registrate.Registrate;
import com.tterrag.registrate.util.RegistryEntry;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.text.ITextComponent;
import team.chisel.Chisel;
import team.chisel.client.gui.GuiChisel;
import team.chisel.client.gui.GuiHitechChisel;
import team.chisel.common.inventory.ChiselContainer;
import team.chisel.common.inventory.ContainerChiselHitech;
import team.chisel.common.inventory.InventoryChiselSelection;
import team.chisel.common.util.ContainerBuilder;
import team.chisel.common.util.ContainerBuilder.ContainerFactory;
import team.chisel.common.util.ContainerBuilder.ScreenFactory;

public interface IChiselGuiType<T extends ChiselContainer> {
    
    ContainerType<? extends ChiselContainer> getContainerType();
    
    Container createMenu(int windowId, PlayerInventory inv, PlayerEntity player, Hand hand);
    
    default INamedContainerProvider provide(ItemStack stack, Hand hand) {
        return new INamedContainerProvider() {
            
            @Override
            @Nullable
            public Container createMenu(int windowId, PlayerInventory inv, PlayerEntity player) {
                return IChiselGuiType.this.createMenu(windowId, inv, player, hand);
            }
            
            @Override
            public ITextComponent getDisplayName() {
                return stack.getDisplayName();
            }
        };
    }
    
    public enum ChiselGuiType implements IChiselGuiType<ChiselContainer> {
        NORMAL("chisel_container_normal", ChiselContainer::new, () -> (container, inv, displayName) -> new GuiChisel<>(container, inv, displayName)) {
          
            @Override
            @Nullable
            public Container createMenu(int windowId, PlayerInventory inv, PlayerEntity player, Hand hand) {
                return new ChiselContainer(getContainerType(), windowId, inv, new InventoryChiselSelection(player.getHeldItem(hand), 60), hand);
            }
        },
        HITECH("chisel_container_hitech", ContainerChiselHitech::new, () -> (container, inv, displayName) -> new GuiHitechChisel(container, inv, displayName)) {
            
            @Override
            @Nullable
            public Container createMenu(int windowId, PlayerInventory inv, PlayerEntity player, Hand hand) {
                return new ContainerChiselHitech(getContainerType(), windowId, inv, new InventoryChiselSelection(player.getHeldItem(hand), 63), hand);
            }
        };
        
        private final RegistryEntry<? extends ContainerType<? extends ChiselContainer>> type;
        
        private <C extends ChiselContainer, T extends GuiChisel<C>> ChiselGuiType(String name, ContainerFactory<C> factory, Supplier<ScreenFactory<C, T>> screenFactory) {
            this.type = Chisel.registrate()
                    .entry(name, callback -> new ContainerBuilder<C, T, Registrate>(Chisel.registrate(), Chisel.registrate(), name, callback,
                            factory, screenFactory))
                    .register();
        }

        @Override
        public ContainerType<? extends ChiselContainer> getContainerType() {
            return type.get();
        }
    }

}
