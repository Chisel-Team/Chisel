package team.chisel.api.block;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import com.google.common.base.Predicate;
import com.google.common.collect.FluentIterable;

import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.Tolerate;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;
import team.chisel.api.carving.CarvingUtils;
import team.chisel.client.render.ChiselModelRegistry;
import team.chisel.common.init.BlockRegistry;

/**
 * Building a ChiselBlockData
 */
@Setter
@Accessors(chain = true)
@ParametersAreNonnullByDefault
public class ChiselBlockBuilder<T extends Block & ICarvable> {

    private final Material material;
    private final String domain;
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

    protected ChiselBlockBuilder(Material material, String domain, String blockName, @Nullable String group, BlockProvider<T> provider) {
        this.material = material;
        this.domain = domain;
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
    @SuppressWarnings("unchecked")
    public T[] build() {
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
    public T[] build(Consumer<? super T> after) {
        if (variations.size() == 0) {
            throw new IllegalArgumentException("Must have at least one variation!");
        }
        VariationData[] vars = new VariationData[variations.size()];
        for (int i = 0; i < variations.size(); i++) {
            vars[i] = variations.get(i).doBuild();
        }
        VariationData[][] data = BlockRegistry.splitVariationArray(vars);
        T[] ret = (T[]) Array.newInstance(provider.getBlockClass(), data.length);
        for (int i = 0; i < ret.length; i++) {
            ret[i] = provider.createBlock(material, i, vars.length, data[i]);
            ret[i].setRegistryName(blockName + (i == 0 ? "" : i));
            ret[i].setUnlocalizedName(domain + '.' + blockName);
            ret[i].setHardness(1);
            if (sound != null) {
                ret[i].setSoundType(sound);
            }
            
            GameRegistry.register(ret[i]);
            GameRegistry.register(provider.createItemBlock(ret[i]));

            after.accept(ret[i]);

            if (FMLCommonHandler.instance().getEffectiveSide().isClient()) {
                ChiselModelRegistry.INSTANCE.register(ret[i]);
            }
            for (int j = 0; j < data[i].length; j++) {
                if (data[i][j].group != null) {
                    VariationBuilder<T> v = variations.get(data[i][j].index);
                    CarvingUtils.getChiselRegistry().addVariation(data[i][j].group, ret[i].getStateFromMeta(j), v.order);
                }

                if (!oreStrings.isEmpty()) {
                    for (String oreEntry : oreStrings) {
                        OreDictionary.registerOre(oreEntry, new ItemStack(ret[i], 1, j));
                    }
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

        @Setter
        private @Nullable ChiselRecipe recipe;

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

        public T[] build() {
            return buildVariation().build();
        }
        
        public T[] build(Consumer<? super T> after) {
            return buildVariation().build(after);
        }

        private VariationData doBuild() {
            return new VariationData(name, parent.parentFolder + "/" + name, group, recipe, smeltedFrom, amountSmelted, index, opaque);
        }

        public VariationBuilder<T> addOreDict(String oreDict)
        {
            this.parent.addOreDict(oreDict);

            return this;
        }
    }
}
