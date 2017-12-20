package team.chisel.common.block;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import team.chisel.Chisel;

public class BlockAutoChisel extends Block {

    public BlockAutoChisel() {
        super(Material.ROCK);
    }
    
    @Override
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }

    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        return new TileAutoChisel();
    }
    
    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, @Nullable ItemStack heldItem, EnumFacing side, float hitX, float hitY,
            float hitZ) {
        if (!worldIn.isRemote) {
            playerIn.openGui(Chisel.instance, 1, worldIn, pos.getX(), pos.getY(), pos.getZ());
        }
        return true;
    }
}
