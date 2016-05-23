package team.chisel.common.block;

import static team.chisel.common.block.BlockCarvable.CTX_LIST;

import java.util.List;

import lombok.Getter;
import net.minecraft.block.BlockPane;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.property.ExtendedBlockState;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import team.chisel.Chisel;
import team.chisel.api.block.ICarvable;
import team.chisel.api.block.VariationData;
import team.chisel.api.render.IBlockRenderType;
import team.chisel.api.render.RenderContextList;
import team.chisel.client.BlockFaceData;
import team.chisel.client.ClientUtil;
import team.chisel.common.init.ChiselTabs;
import team.chisel.common.init.TextureTypeRegistry;
import team.chisel.common.util.PropertyAnyInteger;

import com.google.common.collect.Lists;

public class BlockCarvablePane extends BlockPane implements ICarvable {

    // TODO this class is completely temporary. Need to make a helper object which does all this ICarvable logic
    
    private final PropertyAnyInteger metaProp;

    @SideOnly(Side.CLIENT)
    private BlockFaceData blockFaceData;
    
    @Getter
    private final VariationData[] variations;
    private int index;

    private final int maxVariation;
    
    private final BlockStateContainer realBlockState;

    
    public BlockCarvablePane(Material material, boolean canDrop, int index, int max, VariationData... variations) {
        super(material, canDrop);
        setCreativeTab(ChiselTabs.tab);
        this.index = index;
        this.variations = variations;
        this.maxVariation = max;
        this.metaProp = PropertyAnyInteger.create("variation", 0, max > index * 16 ? 15 : max % 16);
        this.realBlockState = createRealBlockState();
        setupStates();
        Chisel.proxy.initiateFaceData(this);
    }
    
    @Override
    public int damageDropped(IBlockState state) {
        return getMetaFromState(state);
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
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

    @SideOnly(Side.CLIENT)
    @Override
    public IBlockState getExtendedState(IBlockState stateIn, IBlockAccess w, BlockPos pos) {
        if (stateIn.getBlock() == null || stateIn.getBlock().getMaterial(stateIn) == Material.AIR) {
            return stateIn;
        }
        IExtendedBlockState state = (IExtendedBlockState) stateIn;
        List<IBlockRenderType> types = Lists.newArrayList(this.blockFaceData.getForMeta(getMetaFromState(state)).getTypesUsed());
        types.add(TextureTypeRegistry.getType("CTM"));

        RenderContextList ctxList = new RenderContextList(types, w, pos);

        return state.withProperty(CTX_LIST, ctxList);
    }

    public static BlockPos pos(int x, int y, int z) {
        return new BlockPos(x, y, z);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(Item item, CreativeTabs tab, List list) {
        int curIndex = 0;
        for (VariationData var : this.variations) {
            if (var == null) {
                continue;
            }
            ItemStack stack = new ItemStack(item, 1, curIndex);
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
    
    
    private BlockStateContainer createRealBlockState() {
        return new ExtendedBlockState(this, new IProperty[] { metaProp, NORTH, SOUTH, EAST, WEST }, new IUnlistedProperty[] { CTX_LIST });
    }

    private void setupStates() {
        IBlockState state = getExtendedBlockState().withProperty(CTX_LIST, new RenderContextList()).withProperty(metaProp, 0);
        state = state.withProperty(metaProp, 0);
        this.setDefaultState(state);
    }
    
    public ExtendedBlockState getBaseExtendedState() {
        return (ExtendedBlockState) this.getBlockState();
    }

    public IExtendedBlockState getExtendedBlockState() {
        return (IExtendedBlockState) this.getBaseExtendedState().getBaseState();
    }

    @SideOnly(Side.CLIENT)
    @Override
    public BlockFaceData getBlockFaceData() {
        return this.blockFaceData;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void setBlockFaceData(BlockFaceData data) {
        this.blockFaceData = data;
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
    public BlockStateContainer getBlockState() {
        return this.realBlockState;
    }

    @Override
    public int getIndex() {
        return this.index;
    }

    @Override
    public VariationData getVariationData(int meta) {
        return this.variations[MathHelper.clamp_int(meta, 0, this.variations.length - 1)];
    }
}
