package team.chisel.common.block;

import javax.annotation.ParametersAreNonnullByDefault;

import lombok.Getter;
import net.minecraft.block.Block;
import net.minecraft.block.BlockPane;
import net.minecraft.block.material.Material;
import net.minecraft.block.BlockStateContainer;
import net.minecraft.block.BlockState;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.creativetab.ItemGroup;
import net.minecraft.entity.Entity;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.Direction.Axis;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import team.chisel.api.block.ICarvable;
import team.chisel.api.block.VariationData;
import team.chisel.client.util.ClientUtil;
import team.chisel.common.init.ChiselTabs;
import team.chisel.common.util.PropertyAnyInteger;

@ParametersAreNonnullByDefault
public class BlockCarvablePane extends BlockPane implements ICarvable {

    // TODO this class is completely temporary. Need to make a helper object which does all this ICarvable logic
    
    @Getter(onMethod = @__({@Override}))
    public final PropertyAnyInteger metaProp;
    
    @Getter
    private final VariationData[] variations;
    private int index;

    private final int maxVariation;

    private final BlockStateContainer states;

    private boolean dragonProof = false;

    public static BlockCarvablePane create(Material material, int index, int max, VariationData... variations) {
        return new BlockCarvablePane(material, true, index, max, variations);
    }
    
    public static BlockCarvablePane noDrop(Material material, int index, int max, VariationData... variations) {
        return new BlockCarvablePane(material, false, index, max, variations);
    }
    
    public BlockCarvablePane(Material material, boolean canDrop, int index, int max, VariationData... variations) {
        super(material, canDrop);
        setCreativeTab(ChiselTabs.tab);
        this.index = index;
        this.variations = variations;
        this.maxVariation = max;
        this.metaProp = PropertyAnyInteger.create("variation", 0, max > index * 16 ? 15 : max % 16);
        this.states = new BlockStateContainer(this, NORTH, EAST, WEST, SOUTH, metaProp);
        setDefaultState(getBlockState().getBaseState());
    }

    @Override
    public BlockStateContainer getBlockState() {
        return states;
    }

    @Override
    public int damageDropped(BlockState state) {
        return getMetaFromState(state);
    }

    @Override
    public BlockState getStateFromMeta(int meta) {
        this.useNeighborBrightness = true;
        return getBlockState().getBaseState().withProperty(metaProp, meta);
    }

    @Override
    public int getMetaFromState(BlockState state) {
        return state.getValue(metaProp);
    }

    public String getIndexName() {
        if (index == 0) {
            return getUnlocalizedName();
        } else {
            return getUnlocalizedName() + index;
        }
    }

    public static BlockPos pos(int x, int y, int z) {
        return new BlockPos(x, y, z);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(ItemGroup tab, NonNullList<ItemStack> list) {
        int curIndex = 0;
        for (VariationData var : this.variations) {
            if (var == null) {
                continue;
            }
            ItemStack stack = new ItemStack(this, 1, curIndex);
            curIndex++;
            // CTMBlockResources r = SubBlockUtil.getResources(sub);
            // setLore(stack, r.getLore());
            list.add(stack);
        }
    }

    @Override
    public boolean addHitEffects(BlockState state, World worldObj, RayTraceResult target, ParticleManager effectRenderer) {
        ClientUtil.addHitEffects(worldObj, target.getBlockPos(), target.sideHit);
        return true;
    }

    @Override
    public boolean addDestroyEffects(World world, BlockPos pos, ParticleManager effectRenderer) {
        ClientUtil.addDestroyEffects(world, pos, world.getBlockState(pos));
        return true;
    }

    @Override
    public int getVariationIndex(BlockState state) {
        return getMetaFromState(state);
    }

    @Override
    public int getTotalVariations() {
        return this.maxVariation + 1; // off-by-one
    }

    @Override
    public int getIndex() {
        return this.index;
    }

    @Override
    public VariationData getVariationData(int meta) {
        return this.variations[MathHelper.clamp(meta, 0, this.variations.length - 1)];
    }

    public Block setDragonProof() {
        dragonProof = true;
        return this;
    }

    @Override
    public boolean canEntityDestroy(BlockState state, IBlockAccess world, BlockPos pos, Entity entity) {
        if (entity instanceof EntityDragon){
            return !dragonProof;
        }else{
            return super.canEntityDestroy(state, world, pos, entity);
        }
    }
    
    @SideOnly(Side.CLIENT)
    @Override
    public boolean shouldSideBeRendered(BlockState blockState, IBlockAccess blockAccess, BlockPos pos, Direction side) {
        if (!super.shouldSideBeRendered(blockState, blockAccess, pos, side)) {
            if (side.getAxis() != Axis.Y) return false;
            return blockAccess.getBlockState(pos.offset(side)).getActualState(blockAccess, pos.offset(side)) != blockState;
        }
        return true;
    }
}
