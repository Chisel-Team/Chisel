package team.chisel.api;

import lombok.Getter;
import lombok.SneakyThrows;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.Block;

/**
 * Use the enum constants (using {@link #key} or calling {@link #toString()}) in this class as keys for IMC messages sent to chisel
 * <p>
 * It is also acceptable to copy the Strings in this class to avoid referencing this API.
 */
public enum IMC {

    /**
     * Register a variation. Can provide an ItemStack, blockstate, or both.
     * <p>
     * This IMC message is expected to be an {@link CompoundTag}, with the following mappings:
     * <ul>
     * <li>"group" -> A String which represents the group being added to. <strong>REQUIRED</strong></li>
     * <li>"stack" -> An CompoundTag which is the serialized ItemStack. <strong>OPTIONAL</strong></li>
     * <li>"block" -> A String (ResourceLocation) which reprsents the block. <strong>OPTIONAL</strong></li>
     * <li>"meta" -> An int representing block metadata. Can be dynamic using {@link Block#getMetaFromState(net.minecraft.block.BlockState)}. <strong>OPTIONAL, defaults to 0</strong></li>
     * </ul>
     * <p>
     * <strong> NOTE: If only stack, or only blockstate is provided, automatic conversion will be done, using the following code:</strong>
     * <p>
     * Converting ItemStack to blockstate:
     *
     * <pre>
     * if (stack.getItem() instanceof BlockItem) {
     *     return ((BlockItem) stack.getItem()).getBlock().getStateFromMeta(stack.getMetadata());
     * }
     * return null;
     * </pre>
     * <p>
     * Converting blockstate to ItemStack:
     *
     * <pre>
     * return new ItemStack(state.getBlock(), 1, state.getBlock().damageDropped(state))
     * </pre>
     *
     * @since 0.0.14
     */
    ADD_VARIATION("add_variation"),

    /**
     * Remove a variation. Will attempt to match for a variation by ItemStack, then blockstate, if both are provided.
     * <p>
     * This IMC message is expected to be an {@link CompoundTag}, with the same data used for adding a variation, except that "group" can be left out to remove a variation from multiple groups.
     *
     * @see {@link #ADD_VARIATION_V2}
     * @since 0.0.14
     */
    REMOVE_VARIATION("remove_variation"),

    ;

    /**
     * The IMC message key for this message type.
     */
    public final String key;

    @Getter
    private final boolean deprecated;

    @SneakyThrows
    IMC(String key) {
        this.key = key;
        this.deprecated = IMC.class.getDeclaredField(name()).isAnnotationPresent(Deprecated.class);
    }

    public static final String getModid() {
        return ChiselAPIProps.MOD_ID;
    }

    @Override
    public String toString() {
        return key;
    }
}
