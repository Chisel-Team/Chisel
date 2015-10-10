package team.chisel.block;

import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import team.chisel.client.render.RenderRoofing;
import team.chisel.utils.RoofingUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Roofing Block
 */
public class BlockRoofing extends BlockCarvable {

    public BlockRoofing(){
        super(Material.clay);
    }

    @Override
    public boolean canCreatureSpawn(EnumCreatureType type, IBlockAccess world, int x, int y, int z) {
        return false;
    }


    @Override
    public int getRenderType(){
        return RenderRoofing.renderID;
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z){
        return super.getCollisionBoundingBoxFromPool(world, x, y, z);
    }

    @Override
    public void addCollisionBoxesToList(World world, int x, int y, int z, AxisAlignedBB mask, List list, Entity entity){
        RoofingUtil.addSlopeBoxesToList(list, RoofingUtil.getSidesSloping(world, x, y, z, this));
    }

    @Override
    public boolean isOpaqueCube()
    {
        return false;
    }

    @Override
    public boolean renderAsNormalBlock()
    {
        return false;
    }
}
