package team.chisel.common.init;

import com.tterrag.registrate.Registrate;

import com.tterrag.registrate.util.entry.RegistryEntry;
import net.minecraft.world.level.block.Block;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.common.Tags;
import team.chisel.Chisel;
import team.chisel.client.gui.GuiAutoChisel;
import team.chisel.common.block.BlockAutoChisel;
import team.chisel.common.block.TileAutoChisel;
import team.chisel.common.inventory.ContainerAutoChisel;
import team.chisel.common.util.ContainerBuilder;

public class ChiselTileEntities {
    private static final Registrate REGISTRATE = Chisel.registrate();

    public static final RegistryEntry<? extends Block> AUTO_CHISEL = REGISTRATE
            .object("auto_chisel")
            .block(BlockAutoChisel::new)
            .simpleBlockEntity(TileAutoChisel::new)
            .blockstate((ctx, prov) -> prov.simpleBlock(ctx.getEntry(), prov.models().getExistingFile(ctx.getId())))
            .item(BlockItem::new)
                .model((ctx, prov) -> prov.blockItem(ctx::getEntry))
                .recipe((ctx, prov) -> new ShapedRecipeBuilder(ctx.getEntry(), 1)
                        .define('G', Tags.Items.GLASS)
                        .define('R', Tags.Items.DUSTS_REDSTONE)
                        .define('I', Tags.Items.INGOTS_IRON)
                        .pattern("GGG").pattern("GRG").pattern("III")
                        .unlockedBy("has_iron", prov.hasItem(Tags.Items.INGOTS_IRON))
                        .save(prov))
                .build()
            .register();

    public static final RegistryEntry<BlockEntityType<TileAutoChisel>> AUTO_CHISEL_TE = REGISTRATE.get(BlockEntityType.class);
    
    public static final RegistryEntry<MenuType<ContainerAutoChisel>> AUTO_CHISEL_CONTAINER = REGISTRATE.entry((name, callback) -> 
            new ContainerBuilder<ContainerAutoChisel, GuiAutoChisel, Registrate>(REGISTRATE, REGISTRATE, name, callback, ContainerAutoChisel::new, () -> (container, inv, displayName) -> new GuiAutoChisel(container, inv, displayName)))
                .register();
    
    public static final void init() {}
}
