package team.chisel.common.init;

import com.tterrag.registrate.Registrate;
import com.tterrag.registrate.util.RegistryEntry;

import net.minecraft.block.Block;
import net.minecraft.data.ShapedRecipeBuilder;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.BlockItem;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
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
            .tileEntity(TileAutoChisel::new)
            .blockstate((ctx, prov) -> prov.simpleBlock(ctx.getEntry(), prov.getExistingFile(ctx.getId())))
            .item(BlockItem::new)
                .model((ctx, prov) -> prov.blockItem(ctx::getEntry))
                .recipe((ctx, prov) -> new ShapedRecipeBuilder(ctx.getEntry(), 1)
                        .key('G', Tags.Items.GLASS)
                        .key('R', Tags.Items.DUSTS_REDSTONE)
                        .key('I', Tags.Items.INGOTS_IRON)
                        .patternLine("GGG").patternLine("GRG").patternLine("III")
                        .addCriterion("has_iron", prov.hasItem(Tags.Items.INGOTS_IRON))
                        .build(prov))
                .build()
            .register();

    public static final RegistryEntry<TileEntityType<? extends TileEntity>> AUTO_CHISEL_TE = REGISTRATE.get(TileEntityType.class);
    
    public static final RegistryEntry<ContainerType<ContainerAutoChisel>> AUTO_CHISEL_CONTAINER = REGISTRATE.entry((name, callback) -> 
            new ContainerBuilder<>(REGISTRATE, REGISTRATE, name, callback, ContainerAutoChisel::new, GuiAutoChisel::new))
                .register();
    
    public static final void init() {}
}
