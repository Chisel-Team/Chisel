package team.chisel.common.init;

import com.tterrag.registrate.Registrate;

import net.minecraft.block.Block;
import net.minecraft.data.ShapedRecipeBuilder;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.BlockItem;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.ModelFile.ExistingModelFile;
import net.minecraftforge.common.Tags;
import net.minecraftforge.fml.RegistryObject;
import team.chisel.Chisel;
import team.chisel.client.gui.GuiAutoChisel;
import team.chisel.common.block.BlockAutoChisel;
import team.chisel.common.block.TileAutoChisel;
import team.chisel.common.inventory.ContainerAutoChisel;
import team.chisel.common.util.ContainerBuilder;

public class ChiselTileEntities {
    private static final Registrate REGISTRATE = Chisel.registrate();

    public static final RegistryObject<? extends Block> AUTO_CHISEL = REGISTRATE
            .object("auto_chisel")
            .block(BlockAutoChisel::new)
            .tileEntity(TileAutoChisel::new)
            .blockstate(cons -> cons.getProvider().simpleBlock(cons.getEntry(), cons.getProvider().getExistingFile(cons.getId())))
            .item(BlockItem::new)
                .model(cons -> cons.getProvider().blockItem(cons::getEntry))
                .recipe(ctx -> new ShapedRecipeBuilder(ctx.getEntry(), 1)
                        .key('G', Tags.Items.GLASS)
                        .key('R', Tags.Items.DUSTS_REDSTONE)
                        .key('I', Tags.Items.INGOTS_IRON)
                        .patternLine("GGG").patternLine("GRG").patternLine("III")
                        .addCriterion("has_iron", ctx.getProvider().hasItem(Tags.Items.INGOTS_IRON))
                        .build(ctx.getProvider()))
                .build()
            .register();

    public static final RegistryObject<TileEntityType<? extends TileEntity>> AUTO_CHISEL_TE = REGISTRATE.get(TileEntityType.class);
    
    public static final RegistryObject<ContainerType<ContainerAutoChisel>> AUTO_CHISEL_CONTAINER = REGISTRATE.entry((name, callback) -> 
            new ContainerBuilder<>(REGISTRATE, REGISTRATE, name, callback, ContainerAutoChisel::new, GuiAutoChisel::new))
                .register();
    
    public static final void init() {}
}
