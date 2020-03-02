package team.chisel.api.block;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import com.google.common.base.Strings;
import com.tterrag.registrate.Registrate;
import com.tterrag.registrate.builders.BlockBuilder;
import com.tterrag.registrate.builders.ItemBuilder;
import com.tterrag.registrate.providers.ProviderType;
import com.tterrag.registrate.providers.RegistrateLangProvider;
import com.tterrag.registrate.util.RegistryEntry;
import com.tterrag.registrate.util.nullness.NonNullBiConsumer;
import com.tterrag.registrate.util.nullness.NonNullSupplier;
import com.tterrag.registrate.util.nullness.NonNullUnaryOperator;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.Value;
import lombok.experimental.Accessors;
import lombok.experimental.NonFinal;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraft.tags.Tag;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.registries.ForgeRegistries;
import team.chisel.api.carving.CarvingUtils;
import team.chisel.api.carving.ICarvingGroup;
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

    private int curIndex;

    private List<VariationBuilder<T>> variations;

    private BlockProvider<T> provider;

    private @Nullable Tag<Block> group;
    
    @Setter(AccessLevel.NONE)
    private Set<ResourceLocation> otherBlocks = new HashSet<>();
    
    private String groupName;

    @Accessors(fluent = true)
    private boolean opaque = true;

    protected ChiselBlockBuilder(ChiselBlockFactory parent, Registrate registrate, Material material, String blockName, @Nullable Tag<Block> group, BlockProvider<T> provider) {
        this.parent = parent;
        this.registrate = registrate;
        this.material = material;
        this.blockName = blockName;
        this.provider = provider;
        this.group = group;
        this.groupName = RegistrateLangProvider.toEnglishName(group.getId().getPath());
        this.variations = new ArrayList<VariationBuilder<T>>();
    }

    @SuppressWarnings("null")
    public VariationBuilder<T> variation(String name) {
        return variation(name, group);
    }

    public VariationBuilder<T> variation(String name, Tag<Block> group) {
        VariationBuilder<T> builder = new VariationBuilder<>(this, name);
        builder.opaque(opaque);
        curIndex++;
        return builder;
    }
    
    public ChiselBlockBuilder<T> variation(VariantTemplate template) {
        return variation(template.getName())
                .localizedName(template.getLocalizedName())
                .model(template.getModelTemplate())
                .recipe(template.getRecipeTemplate().orElse(RecipeTemplate.none()))
                .tooltip(template.getTooltip())
                .buildVariation();
    }
    
    public ChiselBlockBuilder<T> variations(Collection<? extends VariantTemplate> templates) {
        ChiselBlockBuilder<T> ret = this;
        for (VariantTemplate template : templates) {
            ret = variation(template);
        }
        return ret;
    }
    
    public ChiselBlockBuilder<T> addBlock(Block block) {
        return addBlock(block.getRegistryName());
    }

    public ChiselBlockBuilder<T> addBlock(ResourceLocation block) {
        this.otherBlocks.add(block);
        return this;
    }

    private static final NonNullUnaryOperator<Block.Properties> NO_ACTION = NonNullUnaryOperator.identity();

    /**
     * Builds the block(s).
     * 
     * @return An array of blocks created. More blocks are automatically created if the unbaked variations will not fit into one block.
     */
    @SuppressWarnings({ "unchecked", "null" })
    public Map<String, RegistryEntry<T>> build() {
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
    public Map<String, RegistryEntry<T>> build(NonNullUnaryOperator<Block.Properties> after) {
        if (variations.size() == 0) {
            throw new IllegalArgumentException("Must have at least one variation!");
        }
        Variation[] data = new Variation[variations.size()];
        for (int i = 0; i < variations.size(); i++) {
            data[i] = variations.get(i).doBuild();
        }
        Map<String, RegistryEntry<T>> ret = new HashMap<>(data.length);
        ICarvingGroup group = CarvingUtils.itemGroup(this.group, this.groupName);
        for (int i = 0; i < data.length; i++) {
            if (Strings.emptyToNull(data[i].getName()) != null) {
                final int index = i;
                final Variation var = data[index];
                final VariationBuilder<T> builder = variations.get(index);
                ret.put(var.getName(), registrate.object(blockName + "/" + var.getName())
                        .block(material, p -> provider.createBlock(p, new VariationDataImpl(ret.get(var.getName()), var.getName(), var.getDisplayName(), group)))
                        .properties(p -> p.hardnessAndResistance(1))
                        .transform(this::addTag)
                        .properties(after)
                        .blockstate((ctx, prov) -> builder.model.accept(prov, ctx.getEntry()))
                        .setData(ProviderType.LANG, NonNullBiConsumer.noop())
                        .recipe((ctx, prov) -> builder.recipe.accept(prov, ctx.getEntry()))
                        .item(provider::createBlockItem)
                            // TODO fix this mess in forge, it should check for explicitly "block/" or "item/" not any folder prefix
                            .model((ctx, prov) -> prov.withExistingParent("item/" + prov.name(ctx::getEntry), new ResourceLocation(prov.modid(ctx::getEntry), "block/" + prov.name(ctx::getEntry))))
                            .properties(p -> p.group(ChiselTabs.tab))
                            .transform(this::addTag)
                            .build()
                        .register());
            }
        }
        if (this.group != null) {
            if (!otherBlocks.isEmpty()) {
                CarvingUtils.getChiselRegistry().addGroup(group);
                registrate.addDataGenerator(ProviderType.BLOCK_TAGS, prov -> prov.getBuilder(this.group)
                        .add(otherBlocks.stream()
                                .map(ForgeRegistries.BLOCKS::getValue)
                                .toArray(Block[]::new)));
                registrate.addDataGenerator(ProviderType.ITEM_TAGS, prov -> prov.getBuilder(parent.getItemTag(this.group.getId()))
                        .add(otherBlocks.stream()
                                .map(ForgeRegistries.ITEMS::getValue)
                                .toArray(Item[]::new)));
            }
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
        
        private static final Map<String, String> TRANSLATIONS = new HashMap<>();
        private static final Map<String, TranslationTextComponent> COMPONENTS = new HashMap<>();

        private ChiselBlockBuilder<T> parent;

        private String name;
        @Setter
        @Accessors(fluent = true)
        private String localizedName;
        
        @Setter
        @Accessors(fluent = true)
        private boolean opaque;
        
        @Setter
        @Accessors(fluent = true)
        private ModelTemplate model = ModelTemplates.simpleBlock();
        
        @Setter
        @Accessors(fluent = true)
        private RecipeTemplate recipe = RecipeTemplate.none();
        
        private String[] tooltip = new String[0];

        private VariationBuilder(ChiselBlockBuilder<T> parent, String name) {
            this.parent = parent;
            this.name = name;
            this.localizedName = RegistrateLangProvider.toEnglishName(name);
        }
        
        public VariationBuilder<T> tooltip(String... lines) {
            this.tooltip = Arrays.copyOf(lines, lines.length);
            return this;
        }

        public ChiselBlockBuilder<T> buildVariation() {
            this.parent.variations.add(this);
            return this.parent;
        }

        public VariationBuilder<T> next(String name) {
            return buildVariation().variation(name);
        }

        public VariationBuilder<T> next(String name, Tag<Block> group) {
            return buildVariation().variation(name, group);
        }

        public Map<String, RegistryEntry<T>> build() {
            return buildVariation().build();
        }
        
        public Map<String, RegistryEntry<T>> build(NonNullUnaryOperator<Block.Properties> after) {
            return buildVariation().build(after);
        }

        private Variation doBuild() {
            String existingTranslation = TRANSLATIONS.get(name);
            if (existingTranslation != null && !Objects.equals(localizedName, existingTranslation)) {
                throw new IllegalStateException("Cannot redefine existing variation's localized name: " + name + " -> " + localizedName + " (should be " + existingTranslation + ")");
            }
            if (existingTranslation == null) {
                TRANSLATIONS.put(name, localizedName);
                TranslationTextComponent component = parent.registrate.addLang("variant", new ResourceLocation(parent.registrate.getModid(), name), localizedName);
                COMPONENTS.put(name, component);
                for (int j = 0; j < tooltip.length; j++) {
                    parent.registrate.addRawLang(component.getKey() + ".desc." + (j + 1), tooltip[j]);
                }
            }
            return new Variation(name, COMPONENTS.get(name));
        }
    }

    @Value
    @NonFinal
    @MethodsReturnNonnullByDefault
    private static class Variation {

        /**
         * The Name of this variation
         */
        String name;

        TranslationTextComponent displayName;
    }

    @Value
    @Getter(onMethod = @__({@Override}))
    @MethodsReturnNonnullByDefault
    @ParametersAreNonnullByDefault
    private static class VariationDataImpl implements VariationData {

        String name;
        TranslationTextComponent displayName;
        NonNullSupplier<? extends Block> block;
        ICarvingGroup group;

        VariationDataImpl(NonNullSupplier<? extends Block> block, String name, TranslationTextComponent displayName, ICarvingGroup group) {
            this.block = block;
            this.name = name;
            this.displayName = displayName;
            this.group = group;
        }

        @Override
        public Block getBlock() {
            return block.get();
        }

        @Override
        public Item getItem() {
            return getBlock().asItem();
        }
    }
}
