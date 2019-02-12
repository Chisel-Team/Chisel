package team.chisel.common.block;

import java.util.List;

import javax.annotation.ParametersAreNonnullByDefault;

import lombok.Getter;
import net.minecraft.block.Block;
import net.minecraft.block.BlockPane;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
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
    
    public BlockCarvablePane(Material material, int index, int max, VariationData... variations) {
        super(material, true);
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
    public int damageDropped(IBlockState state) {
        return getMetaFromState(state);
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        this.useNeighborBrightness = true;
        return getBlockState().getBaseState().withProperty(metaProp, meta);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
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
    public void getSubBlocks(CreativeTabs tab, NonNullList<ItemStack> list) {
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
    public boolean addHitEffects(IBlockState state, World worldObj, RayTraceResult target, ParticleManager effectRenderer) {
        ClientUtil.addHitEffects(worldObj, target.getBlockPos(), target.sideHit);
        return true;
    }

    @Override
    public boolean addDestroyEffects(World world, BlockPos pos, ParticleManager effectRenderer) {
        ClientUtil.addDestroyEffects(world, pos, world.getBlockState(pos));
        return true;
    }

    @Override
    public int getVariationIndex(IBlockState state) {
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

    @SuppressWarnings("null") // No type annotations
    @Override
    public VariationData getVariationData(int meta) {
        return this.variations[MathHelper.clamp(meta, 0, this.variations.length - 1)];
    }

    public Block setDragonProof() {
        dragonProof = true;
        return this;
    }

    @Override
    public boolean canEntityDestroy(IBlockState state, IBlockAccess world, BlockPos pos, Entity entity) {
        if (entity instanceof EntityDragon){
            return !dragonProof;
        }else{
            return super.canEntityDestroy(state, world, pos, entity);
        }
    }
}
