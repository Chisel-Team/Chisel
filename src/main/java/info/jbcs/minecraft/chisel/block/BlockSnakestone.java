package info.jbcs.minecraft.chisel.block;

import info.jbcs.minecraft.chisel.client.render.BlockSnakeStoneRenderer;

import java.util.List;

import info.jbcs.minecraft.chisel.init.ModTabs;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class BlockSnakestone extends Block
{
    static final int SEC_HEAD = 0;
    static final int SEC_DOWN = 4;
    static final int SEC_UP = 8;
    static final int SEC_HOR = 12;

    static final int SIDE_B = 0;
    static final int SIDE_T = 1;
    static final int SIDE_N = 2;
    static final int SIDE_S = 3;
    static final int SIDE_W = 4;
    static final int SIDE_E = 5;

    String iconPrefix;
    IIcon iconFaceLeft;
    IIcon iconFaceRight;
    IIcon iconCross;
    IIcon iconFace;
    IIcon iconTopTip;
    IIcon iconTopSide;
    IIcon iconBottom;
    IIcon iconBottomSide;
    IIcon iconBottomTip;
    IIcon iconLeftDown;
    IIcon iconRightDown;
    IIcon iconLeftUp;
    IIcon iconRightUp;
    IIcon iconLeftTip;
    IIcon iconSide;
    IIcon iconRightTip;
    IIcon iconTop;

    public boolean flipTopTextures;

    public BlockSnakestone(String iconPrefix)
    {
        super(Material.rock);
        setHardness(1.5F);
        setResistance(10.0F);
        setStepSound(Block.soundTypeStone);
        setCreativeTab(ModTabs.tabChiselBlocks);
        flipTopTextures = false;

        this.iconPrefix = iconPrefix;
    }

    @Override
    public int getRenderType()
    {
        return BlockSnakeStoneRenderer.id;
    }

    static final int[] rotRemap = {0, 3, 1, 2};

    @Override
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entity, ItemStack stack)
    {
        int meta = world.getBlockMetadata(x, y, z);
        if(meta > 3) return;

        if(rotateHead(world, x, y, z) != -1)
            return;

        int rot = rotRemap[(MathHelper.floor_double(entity.rotationYaw * 4.0 / 360.0 + 0.5)) & 3];

        world.setBlockMetadataWithNotify(x, y, z, rot, 3);
    }

    @Override
    public int onBlockPlaced(World world, int x, int y, int z, int side, float hx, float hy, float hz, int meta)
    {
        if(meta <= 3) return meta;

        if(rotateBodyPart(world, x, y, z))
        {
            return world.getBlockMetadata(x, y, z);
        }

        if(side == 0 || side == 1) meta = 14;
        if(side == 2 || side == 3) meta = 13;
        if(side == 4 || side == 5) meta = 12;

        world.setBlockMetadataWithNotify(x, y, z, meta, 2);

        return meta;
    }

    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, Block block)
    {
        int kind = world.getBlockMetadata(x, y, z) & 0xC;

        if(kind == 0)
        {
            rotateHead(world, x, y, z);
        } else
        {
            rotateBodyPart(world, x, y, z);
        }
    }

    public int[] getConnections(World world, int ox, int oy, int oz)
    {
        int index = 0;
        int[] res = {-1, -1, -1, -1, -1, -1, -1};

        for(int direction = 0; direction < 6; direction++)
        {
            int x = ox, y = oy, z = oz;
            switch(direction)
            {
                case 0:
                    z--;
                    break;
                case 1:
                    z++;
                    break;
                case 2:
                    x--;
                    break;
                case 3:
                    x++;
                    break;
                case 4:
                    y--;
                    break;
                case 5:
                    y++;
                    break;
            }

            if(!world.getBlock(x, y, z).equals(this)) continue;
            boolean match = false;

            int px1 = x, px2 = x;
            int py1 = y, py2 = y;
            int pz1 = z, pz2 = z;

            int meta = world.getBlockMetadata(x, y, z);
            if((meta & 0xc) == 4 || (meta & 0xc) == 8)
            {
                if((meta & 4) == 4) py1--;
                else py1++;

                switch(meta & 3)
                {
                    case 0:
                        pz2--;
                        break;
                    case 1:
                        pz2++;
                        break;
                    case 2:
                        px2--;
                        break;
                    case 3:
                        px2++;
                        break;
                }
            } else if(meta == 12)
            {
                px1--;
                px2++;
            } else if(meta == 13)
            {
                pz1--;
                pz2++;
            } else if(meta == 14)
            {
                py1--;
                py2++;
            } else if(meta == 15)
            {
                match = true;
            } else if(meta < 4)
            {
                match = true;
            }

            if(!match)
            {
                Block b1 = world.getBlock(px1, py1, pz1);
                Block b2 = world.getBlock(px2, py2, pz2);


                if(b1 != this || b2 != this)
                    match = true;

                if(ox == px1 && oy == py1 && oz == pz1)
                    match = true;

                if(ox == px2 && oy == py2 && oz == pz2)
                    match = true;
            }

            if(match)
                res[index++] = direction;
        }

        if(index != 5)
            res[index] = -1;

        return res;
    }


    public boolean rotateBodyPart(World world, int x, int y, int z)
    {
        int[] con = getConnections(world, x, y, z);
        int blockMeta = world.getBlockMetadata(x, y, z);
        int kind = blockMeta & 0xc;

        if(con[1] == -1) return false;
        if(con[2] != -1) return false;

        int meta = 15;

        if(con[0] == 2 && con[1] == 3)
        {
            meta = 12;
        } else if(con[0] == 0 && con[1] == 1)
        {
            meta = 13;
        } else if(con[0] == 4 && con[1] == 5)
        {
            meta = 14;
        } else if(con[1] == 4)
        {
            meta = SEC_DOWN | con[0];
        } else if(con[1] == 5)
        {
            meta = SEC_UP | con[0];
        }

        if(blockMeta != meta)
            world.setBlockMetadataWithNotify(x, y, z, meta, 3);

        return true;
    }

    public int rotateHead(World world, int x, int y, int z)
    {
        int[] con = getConnections(world, x, y, z);

        if(con[1] >= 0 && con[1] < 4) return -1;

        int meta = -1;
        switch(con[0])
        {
            case 0:
                meta = 1;
                break;
            case 1:
                meta = 0;
                break;
            case 2:
                meta = 3;
                break;
            case 3:
                meta = 2;
                break;
        }

        if(meta == -1) return -1;

        world.setBlock(x, y, z, this, meta, 3);
        return meta;
    }

    public boolean connectedTo(World par1World, int x, int y, int z, int direction)
    {
        switch(direction)
        {
            case 0:
                return par1World.getBlock(x, y, z - 1).equals(this);
            case 1:
                return par1World.getBlock(x, y, z + 1).equals(this);
            case 2:
                return par1World.getBlock(x - 1, y, z).equals(this);
            case 3:
                return par1World.getBlock(x + 1, y, z).equals(this);
            case 4:
                return par1World.getBlock(x, y - 1, z).equals(this);
            case 5:
                return par1World.getBlock(x, y + 1, z).equals(this);
        }

        return false;
    }

    @Override
    public IIcon getIcon(int side, int meta)
    {
        int type = meta & 0xc;
        int orient = meta & 0x3;

        switch(meta | (side << 4))
        {

            case (SEC_HOR | 0 | (SIDE_B << 4)):
                return iconBottomTip;
            case (SEC_HOR | 0 | (SIDE_T << 4)):
                return iconTopSide;
            case (SEC_HOR | 0 | (SIDE_N << 4)):
                return iconSide;
            case (SEC_HOR | 0 | (SIDE_S << 4)):
                return iconSide;
            case (SEC_HOR | 0 | (SIDE_W << 4)):
                return iconCross;
            case (SEC_HOR | 0 | (SIDE_E << 4)):
                return iconCross;

            case (SEC_HOR | 1 | (SIDE_B << 4)):
                return iconBottomTip;
            case (SEC_HOR | 1 | (SIDE_T << 4)):
                return iconTopSide;
            case (SEC_HOR | 1 | (SIDE_N << 4)):
                return iconCross;
            case (SEC_HOR | 1 | (SIDE_S << 4)):
                return iconCross;
            case (SEC_HOR | 1 | (SIDE_W << 4)):
                return iconSide;
            case (SEC_HOR | 1 | (SIDE_E << 4)):
                return iconSide;

            case (SEC_HOR | 2 | (SIDE_B << 4)):
                return iconCross;
            case (SEC_HOR | 2 | (SIDE_T << 4)):
                return iconCross;
            case (SEC_HOR | 2 | (SIDE_N << 4)):
                return iconSide;
            case (SEC_HOR | 2 | (SIDE_S << 4)):
                return iconSide;
            case (SEC_HOR | 2 | (SIDE_W << 4)):
                return iconSide;
            case (SEC_HOR | 2 | (SIDE_E << 4)):
                return iconSide;

            case (SEC_HOR | 3 | (SIDE_B << 4)):
                return iconBottom;
            case (SEC_HOR | 3 | (SIDE_T << 4)):
                return iconTop;
            case (SEC_HOR | 3 | (SIDE_N << 4)):
                return iconSide;
            case (SEC_HOR | 3 | (SIDE_S << 4)):
                return iconSide;
            case (SEC_HOR | 3 | (SIDE_W << 4)):
                return iconSide;
            case (SEC_HOR | 3 | (SIDE_E << 4)):
                return iconSide;

            case (SEC_DOWN | 0 | (SIDE_B << 4)):
                return iconCross;
            case (SEC_DOWN | 0 | (SIDE_T << 4)):
                return iconTopTip;
            case (SEC_DOWN | 0 | (SIDE_N << 4)):
                return iconCross;
            case (SEC_DOWN | 0 | (SIDE_S << 4)):
                return iconRightTip;
            case (SEC_DOWN | 0 | (SIDE_W << 4)):
                return iconLeftDown;
            case (SEC_DOWN | 0 | (SIDE_E << 4)):
                return iconRightDown;

            case (SEC_DOWN | 1 | (SIDE_B << 4)):
                return iconCross;
            case (SEC_DOWN | 1 | (SIDE_T << 4)):
                return iconTopTip;
            case (SEC_DOWN | 1 | (SIDE_N << 4)):
                return iconLeftTip;
            case (SEC_DOWN | 1 | (SIDE_S << 4)):
                return iconCross;
            case (SEC_DOWN | 1 | (SIDE_W << 4)):
                return iconRightDown;
            case (SEC_DOWN | 1 | (SIDE_E << 4)):
                return iconLeftDown;

            case (SEC_DOWN | 2 | (SIDE_B << 4)):
                return iconCross;
            case (SEC_DOWN | 2 | (SIDE_T << 4)):
                return iconTopTip;
            case (SEC_DOWN | 2 | (SIDE_N << 4)):
                return iconRightDown;
            case (SEC_DOWN | 2 | (SIDE_S << 4)):
                return iconLeftDown;
            case (SEC_DOWN | 2 | (SIDE_W << 4)):
                return iconCross;
            case (SEC_DOWN | 2 | (SIDE_E << 4)):
                return iconLeftTip;

            case (SEC_DOWN | 3 | (SIDE_B << 4)):
                return iconCross;
            case (SEC_DOWN | 3 | (SIDE_T << 4)):
                return iconTopTip;
            case (SEC_DOWN | 3 | (SIDE_N << 4)):
                return iconLeftDown;
            case (SEC_DOWN | 3 | (SIDE_S << 4)):
                return iconRightDown;
            case (SEC_DOWN | 3 | (SIDE_W << 4)):
                return iconRightTip;
            case (SEC_DOWN | 3 | (SIDE_E << 4)):
                return iconCross;

            case (SEC_UP | 0 | (SIDE_B << 4)):
                return iconBottomSide;
            case (SEC_UP | 0 | (SIDE_T << 4)):
                return iconCross;
            case (SEC_UP | 0 | (SIDE_N << 4)):
                return iconCross;
            case (SEC_UP | 0 | (SIDE_S << 4)):
                return iconLeftTip;
            case (SEC_UP | 0 | (SIDE_W << 4)):
                return iconLeftUp;
            case (SEC_UP | 0 | (SIDE_E << 4)):
                return iconRightUp;

            case (SEC_UP | 1 | (SIDE_B << 4)):
                return iconBottomSide;
            case (SEC_UP | 1 | (SIDE_T << 4)):
                return iconCross;
            case (SEC_UP | 1 | (SIDE_N << 4)):
                return iconRightTip;
            case (SEC_UP | 1 | (SIDE_S << 4)):
                return iconCross;
            case (SEC_UP | 1 | (SIDE_W << 4)):
                return iconRightUp;
            case (SEC_UP | 1 | (SIDE_E << 4)):
                return iconLeftUp;

            case (SEC_UP | 2 | (SIDE_B << 4)):
                return iconBottomSide;
            case (SEC_UP | 2 | (SIDE_T << 4)):
                return iconCross;
            case (SEC_UP | 2 | (SIDE_N << 4)):
                return iconRightUp;
            case (SEC_UP | 2 | (SIDE_S << 4)):
                return iconLeftUp;
            case (SEC_UP | 2 | (SIDE_W << 4)):
                return iconCross;
            case (SEC_UP | 2 | (SIDE_E << 4)):
                return iconRightTip;

            case (SEC_UP | 3 | (SIDE_B << 4)):
                return iconBottomSide;
            case (SEC_UP | 3 | (SIDE_T << 4)):
                return iconCross;
            case (SEC_UP | 3 | (SIDE_N << 4)):
                return iconLeftUp;
            case (SEC_UP | 3 | (SIDE_S << 4)):
                return iconRightUp;
            case (SEC_UP | 3 | (SIDE_W << 4)):
                return iconLeftTip;
            case (SEC_UP | 3 | (SIDE_E << 4)):
                return iconCross;

            case (SEC_HEAD | 0 | (SIDE_B << 4)):
                return iconBottomSide;
            case (SEC_HEAD | 0 | (SIDE_T << 4)):
                return iconTopTip;
            case (SEC_HEAD | 0 | (SIDE_N << 4)):
                return iconFace;
            case (SEC_HEAD | 0 | (SIDE_S << 4)):
                return iconCross;
            case (SEC_HEAD | 0 | (SIDE_W << 4)):
                return iconFaceLeft;
            case (SEC_HEAD | 0 | (SIDE_E << 4)):
                return iconFaceRight;

            case (SEC_HEAD | 1 | (SIDE_B << 4)):
                return iconBottomSide;
            case (SEC_HEAD | 1 | (SIDE_T << 4)):
                return iconTopTip;
            case (SEC_HEAD | 1 | (SIDE_N << 4)):
                return iconCross;
            case (SEC_HEAD | 1 | (SIDE_S << 4)):
                return iconFace;
            case (SEC_HEAD | 1 | (SIDE_W << 4)):
                return iconFaceRight;
            case (SEC_HEAD | 1 | (SIDE_E << 4)):
                return iconFaceLeft;

            case (SEC_HEAD | 2 | (SIDE_B << 4)):
                return iconBottomSide;
            case (SEC_HEAD | 2 | (SIDE_T << 4)):
                return iconTopTip;
            case (SEC_HEAD | 2 | (SIDE_N << 4)):
                return iconFaceRight;
            case (SEC_HEAD | 2 | (SIDE_S << 4)):
                return iconFaceLeft;
            case (SEC_HEAD | 2 | (SIDE_W << 4)):
                return iconFace;
            case (SEC_HEAD | 2 | (SIDE_E << 4)):
                return iconCross;

            case (SEC_HEAD | 3 | (SIDE_B << 4)):
                return iconBottomSide;
            case (SEC_HEAD | 3 | (SIDE_T << 4)):
                return iconTopTip;
            case (SEC_HEAD | 3 | (SIDE_N << 4)):
                return iconFaceLeft;
            case (SEC_HEAD | 3 | (SIDE_S << 4)):
                return iconFaceRight;
            case (SEC_HEAD | 3 | (SIDE_W << 4)):
                return iconCross;
            case (SEC_HEAD | 3 | (SIDE_E << 4)):
                return iconFace;

        }

        return iconCross;
    }

    @Override
    public void getSubBlocks(Item item, CreativeTabs tabs, List list)
    {
        list.add(new ItemStack(item, 1, 1));
        list.add(new ItemStack(item, 1, 13));
    }

    @Override
    public int damageDropped(int meta)
    {
        if(meta < 4)
        {
            return 1;
        }

        return 13;
    }

    @Override
    public void registerBlockIcons(IIconRegister register)
    {
        iconFaceLeft = register.registerIcon(iconPrefix + "face-left");
        iconFaceRight = register.registerIcon(iconPrefix + "face-right");
        iconFace = register.registerIcon(iconPrefix + "face");

        iconLeftUp = register.registerIcon(iconPrefix + "left-up");
        iconLeftDown = register.registerIcon(iconPrefix + "left-down");
        iconLeftTip = register.registerIcon(iconPrefix + "left-tip");

        iconRightUp = register.registerIcon(iconPrefix + "right-up");
        iconRightDown = register.registerIcon(iconPrefix + "right-down");
        iconRightTip = register.registerIcon(iconPrefix + "right-tip");

        iconTop = register.registerIcon(iconPrefix + "top");
        iconTopTip = register.registerIcon(iconPrefix + "top-tip");
        iconTopSide = register.registerIcon(iconPrefix + "top-side");

        iconBottom = register.registerIcon(iconPrefix + "bot");
        iconBottomTip = register.registerIcon(iconPrefix + "bot-tip");
        iconBottomSide = register.registerIcon(iconPrefix + "bot-side");

        iconCross = register.registerIcon(iconPrefix + "crosssection");
        iconSide = register.registerIcon(iconPrefix + "side");
    }
}