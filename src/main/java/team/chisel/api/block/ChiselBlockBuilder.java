package team.chisel.api.block;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import com.google.common.base.Strings;
import com.tterrag.registrate.Registrate;

import lombok.Setter;
import lombok.experimental.Accessors;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.RegistryObject;

/**
 * Building a ChiselBlockData
 */
@Setter
@Accessors(chain = true)
@ParametersAreNonnullByDefault
public class ChiselBlockBuilder<T extends Block & ICarvable> {

    private final Registrate registrate;
    private final Material material;
    private final String blockName;

    private @Nullable SoundType sound;

    private int curIndex;

    private List<VariationBuilder<T>> variations;

    private BlockProvider<T> provider;

    private String parentFolder;

    private List<String> oreStrings = new ArrayList<>();

    private @Nullable String group;
    
    @Accessors(fluent = true)
    private boolean opaque = true;

    protected ChiselBlockBuilder(Registrate registrate, Material material, String blockName, @Nullable String group, BlockProvider<T> provider) {
        this.registrate = registrate;
        this.material = material;
        this.blockName = blockName;
        this.provider = provider;
        this.parentFolder = blockName;
        this.group = group;
        this.variations = new ArrayList<VariationBuilder<T>>();
    }

    public void addOreDict(String oreDict)
    {
        this.oreStrings.add(oreDict);
    }

    @SuppressWarnings("null")
    public VariationBuilder<T> newVariation(String name) {
        return newVariation(name, group);
    }

    public VariationBuilder<T> newVariation(String name, String group) {
        VariationBuilder<T> builder = new VariationBuilder<>(this, name, group, curIndex);
        builder.opaque(opaque);
        curIndex++;
        return builder;
    }

    private static final Consumer<?> NO_ACTION = o -> {};

    /**
     * Builds the block(s).
     * 
     * @return An array of blocks created. More blocks are automatically created if the unbaked variations will not fit into one block.
     */
    @SuppressWarnings({ "unchecked", "null" })
    public RegistryObject<T>[] build() {
        return build((Consumer<T>) NO_ACTION);
    }

    /**
     * Builds the block(s), performing the passed action on each.
     * 
     * @param after
     *            The consumer to call after creating each block. Use this to easily set things like hardness/light/etc. This is called after block registration, but prior to model/variation
     *            registration.
     * @return An array of blocks created. More blocks are automatically created if the unbaked variations will not fit into one block.
     */
    @SuppressWarnings({ "unchecked", "deprecation", "null" })
    public RegistryObject<T>[] build(Consumer<? super T> after) {
        if (variations.size() == 0) {
            throw new IllegalArgumentException("Must have at least one variation!");
        }
        VariationData[] data = new VariationData[variations.size()];
        for (int i = 0; i < variations.size(); i++) {
            data[i] = variations.get(i).doBuild();
        }
        RegistryObject<T>[] ret = (RegistryObject<T>[]) new RegistryObject[data.length];
        for (int i = 0; i < ret.length; i++) {
            if (Strings.emptyToNull(data[i].name) != null) {
                final VariationData var = data[i];
                ret[i] = registrate.object(blockName + "/" + var.name)
                        .block(p -> provider.createBlock(p.sound(sound).hardnessAndResistance(1), var))
                        .item(provider::createBlockItem)
                            .build()
                        .register();
            // TODO after.accept(ret[i]);
                if (var.group != null) {
                    VariationBuilder<T> v = variations.get(var.index);
//                    TODO CarvingUtils.getChiselRegistry().addVariation(data[i].group, ret[i], v.order);
                }
            }
        }
        return ret;
    }

    @Accessors(chain = true)
    public static class VariationBuilder<T extends Block & ICarvable> {

        private ChiselBlockBuilder<T> parent;

        private String name;
        private @Nullable String group;

        private int index;

        private @Nullable ItemStack smeltedFrom;
        private int amountSmelted;
        
        @Setter
        private int order;
        
        @Setter
        @Accessors(fluent = true)
        private boolean opaque;

        private VariationBuilder(ChiselBlockBuilder<T> parent, String name, @Nullable String group, int index) {
            this.parent = parent;
            this.name = name;
            this.group = group;
            this.index = index;
        }

        public VariationBuilder<T> setSmeltRecipe(ItemStack smeltedFrom, int amountSmelted) {
            this.smeltedFrom = smeltedFrom;
            this.amountSmelted = amountSmelted;
            return this;
        }

        public ChiselBlockBuilder<T> buildVariation() {
            this.parent.variations.add(this);
            return this.parent;
        }

        public VariationBuilder<T> next(String name) {
            return buildVariation().newVariation(name);
        }

        public VariationBuilder<T> next(String name, String group) {
            return buildVariation().newVariation(name, group);
        }

        public RegistryObject<T>[] build() {
            return buildVariation().build();
        }
        
        public RegistryObject<T>[] build(Consumer<? super T> after) {
            return buildVariation().build(after);
        }

        private VariationData doBuild() {
            return new VariationData(name, parent.parentFolder + "/" + name, group, smeltedFrom, amountSmelted, index, opaque);
        }

        public VariationBuilder<T> addOreDict(String oreDict)
        {
            this.parent.addOreDict(oreDict);

            return this;
        }
    }
}
