package team.chisel.api.block;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import org.apache.commons.lang3.StringUtils;

import com.google.common.base.Strings;
import com.tterrag.registrate.Registrate;
import com.tterrag.registrate.builders.BlockBuilder;
import com.tterrag.registrate.builders.ItemBuilder;
import com.tterrag.registrate.providers.ProviderType;
import com.tterrag.registrate.providers.RegistrateLangProvider;
import com.tterrag.registrate.util.RegistryEntry;
import com.tterrag.registrate.util.nullness.NonNullBiConsumer;
import com.tterrag.registrate.util.nullness.NonNullUnaryOperator;

import lombok.Setter;
import lombok.experimental.Accessors;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraft.tags.Tag;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;
import team.chisel.Chisel;
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

    private @Nullable Tag<Block> group;
    
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
        VariationBuilder<T> builder = new VariationBuilder<>(this, name, group, curIndex);
        builder.opaque(opaque);
        curIndex++;
        return builder;
    }
    
    public ChiselBlockBuilder<T> variation(VariantTemplate template) {
        return variation(template.getName())
                .localizedName(template.getLocalizedName())
                .template(template.getModelTemplate())
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
            if (Strings.emptyToNull(data[i].getName()) != null) {
                final VariationData var = data[i];
                ret[i] = registrate.object(blockName + "/" + var.getName())
                        .block(material, p -> provider.createBlock(p, var))
                        .properties(p -> p.hardnessAndResistance(1))
                        .transform(this::addTag)
                        .properties(after)
                        .blockstate((ctx, prov) -> var.getTemplate().accept(prov, ctx.getEntry()))
                        .setData(ProviderType.LANG, NonNullBiConsumer.noop())
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
            CarvingUtils.getChiselRegistry().addGroup(CarvingUtils.itemGroup(this.group, this.groupName));
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
        private @Nullable Tag<Block> group;

        private int index;

        @Setter
        private int order;
        
        @Setter
        @Accessors(fluent = true)
        private boolean opaque;
        
        @Setter
        @Accessors(fluent = true)
        private ModelTemplate template = ModelTemplates.simpleBlock();
        
        private String[] tooltip = new String[0];

        private VariationBuilder(ChiselBlockBuilder<T> parent, String name, @Nullable Tag<Block> group, int index) {
            this.parent = parent;
            this.name = name;
            this.localizedName = RegistrateLangProvider.toEnglishName(name);
            this.group = group;
            this.index = index;
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

        public RegistryEntry<T>[] build() {
            return buildVariation().build();
        }
        
        public RegistryEntry<T>[] build(NonNullUnaryOperator<Block.Properties> after) {
            return buildVariation().build(after);
        }

        private VariationData doBuild() {
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
            return new VariationData(name, COMPONENTS.get(name), group == null ? null : group.getId(), index, opaque, template);
        }
    }
}
