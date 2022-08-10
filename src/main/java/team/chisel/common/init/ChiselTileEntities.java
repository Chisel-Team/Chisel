package team.chisel.common.init;

import com.tterrag.registrate.Registrate;
import com.tterrag.registrate.providers.RegistrateRecipeProvider;
import com.tterrag.registrate.util.entry.BlockEntityEntry;
import com.tterrag.registrate.util.entry.BlockEntry;
import com.tterrag.registrate.util.entry.MenuEntry;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.common.Tags;
import team.chisel.Chisel;
import team.chisel.client.gui.GuiAutoChisel;
import team.chisel.common.block.BlockAutoChisel;
import team.chisel.common.block.TileAutoChisel;
import team.chisel.common.inventory.ContainerAutoChisel;

public class ChiselTileEntities {
    private static final Registrate REGISTRATE = Chisel.registrate();

    public static final BlockEntry<BlockAutoChisel> AUTO_CHISEL = REGISTRATE
            .object("auto_chisel")
            .block(BlockAutoChisel::new)
            .initialProperties(() -> Blocks.IRON_BLOCK)
            .tag(BlockTags.MINEABLE_WITH_PICKAXE)
            .tag(BlockTags.NEEDS_STONE_TOOL)
            .simpleBlockEntity(TileAutoChisel::new)
            .blockstate((ctx, prov) -> prov.simpleBlock(ctx.getEntry(), prov.models().getExistingFile(ctx.getId())))
            .addLayer(() -> RenderType::cutout)
            .item(BlockItem::new)
            .model((ctx, prov) -> prov.blockItem(ctx::getEntry))
            .recipe((ctx, prov) -> new ShapedRecipeBuilder(ctx.getEntry(), 1)
                    .define('G', Tags.Items.GLASS)
                    .define('R', Tags.Items.DUSTS_REDSTONE)
                    .define('I', Tags.Items.INGOTS_IRON)
                    .pattern("GGG").pattern("GRG").pattern("III")
                    .unlockedBy("has_iron", RegistrateRecipeProvider.has(Tags.Items.INGOTS_IRON))
                    .save(prov))
            .build()
            .register();

    public static final BlockEntityEntry<TileAutoChisel> AUTO_CHISEL_TE = BlockEntityEntry.cast(AUTO_CHISEL.getSibling(BlockEntityType.class));

    public static final MenuEntry<ContainerAutoChisel> AUTO_CHISEL_CONTAINER = REGISTRATE
            .menu(ContainerAutoChisel::new, () -> GuiAutoChisel::new)
            .register();

    public static void init() {
    }
}
