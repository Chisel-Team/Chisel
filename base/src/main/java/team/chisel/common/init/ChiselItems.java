package team.chisel.common.init;

import java.util.Locale;

import javax.annotation.ParametersAreNonnullByDefault;

import com.google.common.collect.ImmutableList;
import com.tterrag.registrate.Registrate;
import com.tterrag.registrate.builders.ItemBuilder;
import com.tterrag.registrate.util.entry.ItemEntry;
import com.tterrag.registrate.util.nullness.FieldsAreNonnullByDefault;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraftforge.common.Tags;
import team.chisel.Chisel;
import team.chisel.api.IChiselGuiType.ChiselGuiType;
import team.chisel.common.item.ChiselMode;
import team.chisel.common.item.ItemChisel;
import team.chisel.common.item.ItemChisel.ChiselType;
import team.chisel.common.item.ItemOffsetTool;

@ParametersAreNonnullByDefault
@FieldsAreNonnullByDefault
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ChiselItems {
    
    private static final Registrate REGISTRATE = Chisel.registrate();
    
    public static final ItemEntry<ItemChisel> IRON_CHISEL = chisel(ChiselType.IRON)
            .recipe((ctx, prov) -> new ShapedRecipeBuilder(ctx.getEntry(), 1)
                    .pattern(" I").pattern("S ")
                    .define('I', Tags.Items.INGOTS_IRON)
                    .define('S', Tags.Items.RODS_WOODEN)
                    .unlockedBy("has_iron", prov.hasItem(Tags.Items.INGOTS_IRON))
                    .save(prov))
            .register();
    
    public static final ItemEntry<ItemChisel> DIAMOND_CHISEL = chisel(ChiselType.DIAMOND)
            .recipe((ctx, prov) -> new ShapedRecipeBuilder(ctx.getEntry(), 1)
                    .pattern(" D").pattern("S ")
                    .define('D', Tags.Items.GEMS_DIAMOND)
                    .define('S', Tags.Items.RODS_WOODEN)
                    .unlockedBy("has_diamond", prov.hasItem(Tags.Items.INGOTS_IRON))
                    .save(prov))
            .register();
    
    public static final ItemEntry<ItemChisel> HITECH_CHISEL = chisel(ChiselType.HITECH)
    		.lang("iChisel")
            .recipe((ctx, prov) -> new ShapelessRecipeBuilder(ctx.getEntry(), 1)
                    .requires(DIAMOND_CHISEL::get)
                    .requires(Tags.Items.INGOTS_GOLD)
                    .requires(Tags.Items.DUSTS_REDSTONE)
                    .unlockedBy("has_diamond_chisel", prov.hasItem(DIAMOND_CHISEL::get))
                    .save(prov))
            .register();
    
    public static final ImmutableList<ItemEntry<ItemChisel>> CHISELS = ImmutableList.of(IRON_CHISEL, DIAMOND_CHISEL, HITECH_CHISEL);
    
    static {
        ChiselGuiType.values(); // Init container types
        ChiselMode.values(); // Init mode translations
    }
    
    public static final ItemEntry<ItemOffsetTool> offsettool = REGISTRATE.item("offset_tool", ItemOffsetTool::new)
            .register();
    
    private static ItemBuilder<ItemChisel, Registrate> chisel(ChiselType type) {
        return REGISTRATE.item(type.name().toLowerCase(Locale.ROOT) + "_chisel", p -> new ItemChisel(type, p))
        		.properties(p -> p.stacksTo(1));
    }
    
    public static void init() {}
}