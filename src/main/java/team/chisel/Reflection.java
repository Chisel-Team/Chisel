package team.chisel;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.EntityHanging;
import net.minecraft.entity.item.EntityPainting;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Reflection {
    static Reflection INSTANCE;

    public Reflection() {
        INSTANCE = this;
    }

    public static Reflection getReflection() {
        return INSTANCE;
    }

    public void updateFacingWithBoundingBox(EntityPainting painting) {
        try {
            Class<?> entityHangingClass = Class.forName("net.minecraft.entity.EntityHanging");

            Method method = entityHangingClass.getDeclaredMethod("updateFacingWithBoundingBox", EnumFacing.class);

            method.invoke(painting, painting.facingDirection);
        } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    public void falseDoubleClick(GuiContainer container) {
        try {
            Field field = container.getClass().getDeclaredField("doubleClick");

            field.setAccessible(true);
            field.setBoolean(container, true);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public Slot getSlotAtPosition(GuiContainer container, int x, int y) {
        try {
            Method getSlotAtPositionMethod = container.getClass().getDeclaredMethod("getSlotAtPosition", int.class, int.class);

            return (Slot)getSlotAtPositionMethod.invoke(container, x, y);
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    public ItemStack getDraggedStack(GuiContainer container) {
        try {
            return (ItemStack) container.getClass().getDeclaredField("draggedStack").get(container);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void setHoveredSlot(GuiContainer container, Slot slot) {
        try {
            Field field = container.getClass().getDeclaredField("hoveredSlot");

            field.setAccessible(true);
            field.set(container, slot);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    public boolean isRightMouseClick(GuiContainer container) {
        try {
            return container.getClass().getDeclaredField("isRightMouseClick").getBoolean(container);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean isMouseOverSlot(GuiContainer container, Slot slot, int mouseX, int mouseY) {
        try {
            Method isMouseOverSlotMethod = container.getClass().getDeclaredMethod("isMouseOverSlot", Slot.class, int.class, int.class);

            return (boolean) isMouseOverSlotMethod.invoke(container, slot, mouseX, mouseY);
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return false;
    }

    public int dragSplittingRemnant(GuiContainer container) {
        try {
            return container.getClass().getDeclaredField("dragSplittingRemnant").getInt(container);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public void drawItemStack(GuiContainer container, ItemStack itemstack, int x, int y, String str) {
        try {
            Method method = container.getClass().getDeclaredMethod("drawItemStack", ItemStack.class, int.class, int.class, String.class);

            method.invoke(container, itemstack, x, y, str);
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public void superDrawScreen(GuiContainer container, int x, int y, float partialTicks) {
        try {
            Method method = container.getClass().getSuperclass().getDeclaredMethod("drawScreen", int.class, int.class, float.class);

            method.invoke(container, x, y, partialTicks);
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public void drawSlot(GuiContainer container, Slot slot) {
        try {
            Method method = container.getClass().getDeclaredMethod("drawSlot", Slot.class);

            method.invoke(container, slot);
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public <T extends Block> Block setSoundType(T block, SoundType type) {
        try {
            return (Block) block.getClass().getDeclaredMethod("setSoundType", SoundType.class).invoke(block, type);
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }
}
