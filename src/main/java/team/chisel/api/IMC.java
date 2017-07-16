package team.chisel.api;

import lombok.Getter;
import lombok.SneakyThrows;
import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;

/**
 * Use the enum constants (using {@link #key} or calling {@link #toString()}) in this class as keys for IMC messages sent to chisel
 * <p>
 * It is also acceptable to copy the Strings in this class to avoid referencing this API.
 */
public enum IMC {

    /**
     * Adds a variation to a group.
     *
     * Use this to add a variation to a group. String syntax:
     * <p>
     * groupname|blockname|meta
     * <p>
     * An example would be {@code "mygroup|minecraft:dirt|1"} and this will add the vanilla dirt block with metadata 1 to the "mygroup" group, creating that group if need be.
     */
    @Deprecated
    ADD_VARIATION("variation:add"),

    /**
     * Removes a variation from a group.
     *
     * Use this to remove a variation from a group. String syntax:
     * <p>
     * groupname|blockname|meta
     * <p>
     * An example would be {@code "mygroup|minecraft:dirt|1"} and this will add the vanilla dirt block with metadata 1 to the "mygroup" group, creating that group if need be.
     */
    @Deprecated
    REMOVE_VARIATION("variation:remove"),

    /**
     * Registers an oredict name to a group. This can be used to automatically add all blocks with this oredict name to a group. String syntax:
     * <p>
     * groupname|oredictname
     * <p>
     * An example would be {@code "mygroup|plankWood"} which will add all blocks registered in the oredict as "plankWood" to your group called "mygroup".
     */
    @Deprecated
    REGISTER_GROUP_ORE("group:ore"),

    /**
     * Register a variation. Can provide an ItemStack, blockstate, or both.
     * <p>
     * This IMC message is expected to be an {@link NBTTagCompound}, with the following mappings:
     * <ul>
     * <li>"group" -> A String which represents the group being added to. <strong>REQUIRED</strong></li>
     * <li>"stack" -> An NBTTagCompound which is the serialized ItemStack. <strong>OPTIONAL</strong></li>
     * <li>"block" -> A String (ResourceLocation) which reprsents the block. <strong>OPTIONAL</strong></li>
     * <li>"meta" -> An int representing block metadata. Can be dynamic using {@link Block#getMetaFromState(net.minecraft.block.state.IBlockState)}. <strong>OPTIONAL, defaults to 0</strong></li>
     * </ul>
     * <p>
     * <strong> NOTE: If only stack, or only blockstate is provided, automatic conversion will be done, using the following code:</strong>
     * <p>
     * Converting ItemStack to blockstate:
     * 
     * <pre>
     * if (stack.getItem() instanceof ItemBlock) {
     *     return ((ItemBlock) stack.getItem()).getBlock().getStateFromMeta(stack.getMetadata());
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
    ADD_VARIATION_V2("add_variation"),
    
    /**
     * Remove a variation. Will attempt to match for a variation by ItemStack, then blockstate, if both are provided.
     * <p>
     * This IMC message is expected to be an {@link NBTTagCompound}, with the same data used for adding a variation, except that "group" can be left out to remove a variation from multiple groups.
     * 
     * @see {@link #ADD_VARIATION_V2}
     * 
     * @since 0.0.14
     */
    REMOVE_VARIATION_V2("remove_variation"),
    
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

    @Override
    public String toString() {
        return key;
    }

    /**
     * The modid of Chisel so you can easily send IMC to this mod.
     */
    @Deprecated
    public static final String CHISEL_MODID = "chisel";

    public static final String getModid() {
        return ChiselAPIProps.MOD_ID;
    }
}
