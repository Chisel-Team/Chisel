package team.chisel.api.block;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import org.apache.commons.lang3.StringUtils;

import com.google.common.base.Strings;
import com.tterrag.registrate.Registrate;
import com.tterrag.registrate.builders.BlockBuilder;
import com.tterrag.registrate.builders.ItemBuilder;
import com.tterrag.registrate.util.RegistryEntry;
import com.tterrag.registrate.util.nullness.NonNullUnaryOperator;

import lombok.Setter;
import lombok.experimental.Accessors;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tags.Tag;
import net.minecraft.util.ResourceLocation;
import team.chisel.api.carving.CarvingUtils;
import team.chisel.client.data.ModelTemplates;
import team.chisel.common.init.ChiselTabs;

/**
 * Building a ChiselBlockData
 */
@Setter
@Accessors(chain = true)
@ParametersAreNonnullByDefault
public class ChiselBlockBuilder<T extends Block & ICarvable> {

    private final ChiselBlockFactory parent;
    private final Registrate registrate;
    private final Material material;
    private final String blockName;

    private @Nullable SoundType sound;

    private int curIndex;

    private List<VariationBuilder<T>> variations;

    private BlockProvider<T> provider;

    private List<String> oreStrings = new ArrayList<>();

    private @Nullable Tag<Block> group;

    @Accessors(fluent = true)
    private boolean opaque = true;

    protected ChiselBlockBuilder(ChiselBlockFactory parent, Registrate registrate, Material material, String blockName, @Nullable Tag<Block> group, BlockProvider<T> provider) {
        this.parent = parent;
        this.registrate = registrate;
        this.material = material;
        this.blockName = blockName;
        this.provider = provider;
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

    public VariationBuilder<T> newVariation(String name, Tag<Block> group) {
        VariationBuilder<T> builder = new VariationBuilder<>(this, name, group, curIndex);
        builder.opaque(opaque);
        curIndex++;
        return builder;
    }

    private static final NonNullUnaryOperator<Block.Properties> NO_ACTION = NonNullUnaryOperator.identity();

    /**
     * Builds the block(s).
     * 
     * @return An array of blocks created. More blocks are automatically created if the unbaked variations will not fit into one block.
     */
    @SuppressWarnings({ "unchecked", "null" })
    public RegistryEntry<T>[] build() {
        return build(NO_ACTION);
    }

    /**
     * Builds the block(s), performing the passed action on each.
     * 
     * @param after
     *            The consumer to call after creating each block. Use this to easily set things like hardness/light/etc. This is called after block registration, but prior to model/variation
     *            registration.
     * @return An array of blocks created. More blocks are automatically created if the unbaked variations will not fit into one block.
     */
    @SuppressWarnings({ "unchecked", "null" })
    public RegistryEntry<T>[] build(NonNullUnaryOperator<Block.Properties> after) {
        if (variations.size() == 0) {
            throw new IllegalArgumentException("Must have at least one variation!");
        }
        VariationData[] data = new VariationData[variations.size()];
        for (int i = 0; i < variations.size(); i++) {
            data[i] = variations.get(i).doBuild();
        }
        RegistryEntry<T>[] ret = (RegistryEntry<T>[]) new RegistryEntry[data.length];
        for (int i = 0; i < ret.length; i++) {
            if (Strings.emptyToNull(data[i].name) != null) {
                final VariationData var = data[i];
                ret[i] = registrate.object(blockName + "/" + var.name)
                        .block(p -> provider.createBlock(p, var))
                        .properties(p -> p.hardnessAndResistance(1))
                        .transform(this::addTag)
                        .properties(after)
                        .blockstate((ctx, prov) -> var.template.accept(prov, ctx.getEntry()))
                        .lang(StringUtils.capitalize(var.name))
                        .item(provider::createBlockItem)
                            // TODO fix this mess in forge, it should check for explicitly "block/" or "item/" not any folder prefix
                            .model((ctx, prov) -> prov.withExistingParent("item/" + prov.name(ctx::getEntry), new ResourceLocation(prov.modid(ctx::getEntry), "block/" + prov.name(ctx::getEntry))))
                            .properties(p -> p.group(ChiselTabs.tab))
                            .transform(this::addTag)
                            .build()
                        .register();
            }
        }
        if (this.group != null) {
            CarvingUtils.getChiselRegistry().addGroup(CarvingUtils.itemGroup(this.group));
        }
        return ret;
    }
    
    private <B extends Block, P> BlockBuilder<B, P> addTag(BlockBuilder<B, P> builder) {
        if (this.group != null) {
            return builder.tag(this.group);
        }
        return builder;
    }
    
    private <I extends Item, P> ItemBuilder<I, P> addTag(ItemBuilder<I, P> builder) {
        if (this.group != null) {
            return builder.tag(parent.getItemTag(this.group.getId()));
        }
        return builder;
    }

    @Accessors(chain = true)
    public static class VariationBuilder<T extends Block & ICarvable> {

        private ChiselBlockBuilder<T> parent;

        private String name;
        private @Nullable Tag<Block> group;

        private int index;

        private @Nullable ItemStack smeltedFrom;
        private int amountSmelted;
        
        @Setter
        private int order;
        
        @Setter
        @Accessors(fluent = true)
        private boolean opaque;
        
        @Setter
        @Accessors(fluent = true)
        private ModelTemplate template = ModelTemplates.simpleBlock();

        private VariationBuilder(ChiselBlockBuilder<T> parent, String name, @Nullable Tag<Block> group, int index) {
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

        public VariationBuilder<T> next(String name, Tag<Block> group) {
            return buildVariation().newVariation(name, group);
        }

        public RegistryEntry<T>[] build() {
            return buildVariation().build();
        }
        
        public RegistryEntry<T>[] build(NonNullUnaryOperator<Block.Properties> after) {
            return buildVariation().build(after);
        }

        private VariationData doBuild() {
            return new VariationData(name, group == null ? null : group.getId(), smeltedFrom, amountSmelted, index, opaque, template);
        }

        public VariationBuilder<T> addOreDict(String oreDict)
        {
            this.parent.addOreDict(oreDict);

            return this;
        }
    }
}
