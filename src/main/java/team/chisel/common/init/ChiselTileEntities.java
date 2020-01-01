package team.chisel.common.init;

import com.tterrag.registrate.Registrate;
import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateItemModelProvider;
import com.tterrag.registrate.util.nullness.NonNullConsumer;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.RegistryObject;
import team.chisel.Chisel;
import team.chisel.common.block.BlockAutoChisel;
import team.chisel.common.block.TileAutoChisel;

public class ChiselTileEntities {
    private static final Registrate REGISTRATE = Chisel.registrate();

    public static final RegistryObject<? extends Block> AUTO_CHISEL = REGISTRATE
            .object("autochisel")
            .block(BlockAutoChisel::new)
            .tileEntity(TileAutoChisel::new)

            .blockstate(cons -> cons.getProvider().simpleBlock(cons.getEntry(), cons.getProvider().cubeAll(cons.getName(), new ResourceLocation("block/smithing_table_top"))))

            .item(BlockItem::new)
            .model(cons -> cons.getProvider().blockItem(cons::getEntry))
            .build()

            .register();



    public static final RegistryObject<TileEntityType<? extends TileEntity>> AUTO_CHISEL_TE = REGISTRATE.get("autochisel", TileEntityType.class);
}
