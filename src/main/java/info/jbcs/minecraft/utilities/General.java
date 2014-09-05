package info.jbcs.minecraft.utilities;

import java.util.HashMap;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class General
{
    public static Random rand = new Random();

    public static void propelTowards(Entity what, Entity whereTo, double force)
    {
        double dx = whereTo.posX - what.posX;
        double dy = whereTo.posY - what.posY;
        double dz = whereTo.posZ - what.posZ;
        double total = Math.sqrt(dx * dx + dy * dy + dz * dz);

        if(total == 0)
        {
            what.motionX = 0;
            what.motionY = 0;
            what.motionZ = 0;
        } else
        {
            what.motionX = dx / total * force;
            what.motionY = dy / total * force;
            what.motionZ = dz / total * force;
        }
    }

    public static boolean isInRange(double distance, double x1, double y1, double z1, double x2, double y2, double z2)
    {
        double x = x1 - x2;
        double y = y1 - y2;
        double z = z1 - z2;

        return x * x + y * y + z * z < distance * distance;
    }

    public static Item getItem(ItemStack stack)
    {
        if(stack == null)
            return null;
        if(stack.getItem() == null)
            return null;

        return stack.getItem();
    }

    public static String getName(Item item)
    {
        String res = item.getUnlocalizedName();
        return cleanTags(res);
    }

    public static String getName(Block block)
    {
        String res = block.getUnlocalizedName();
        return cleanTags(res);
    }

    public static String cleanTags(String tag)
    {
        return tag.replaceAll("[Cc]hisel\\p{Punct}", "").replaceFirst("^tile\\.", "").replaceFirst("^item\\.", "");
    }


    public static MovingObjectPosition getMovingObjectPositionFromPlayer(World par1World, EntityPlayer par2EntityPlayer, boolean par3)
    {
        float var4 = 1.0F;
        float var5 = par2EntityPlayer.prevRotationPitch + (par2EntityPlayer.rotationPitch - par2EntityPlayer.prevRotationPitch) * var4;
        float var6 = par2EntityPlayer.prevRotationYaw + (par2EntityPlayer.rotationYaw - par2EntityPlayer.prevRotationYaw) * var4;
        double var7 = par2EntityPlayer.prevPosX + (par2EntityPlayer.posX - par2EntityPlayer.prevPosX) * var4;
        double var9 = par2EntityPlayer.prevPosY + (par2EntityPlayer.posY - par2EntityPlayer.prevPosY) * var4 + 1.62D - par2EntityPlayer.yOffset;
        double var11 = par2EntityPlayer.prevPosZ + (par2EntityPlayer.posZ - par2EntityPlayer.prevPosZ) * var4;
        //TODO- 1.7.10 fix?
        Vec3 var13 = Vec3.createVectorHelper(var7, var9, var11);
        float var14 = MathHelper.cos(-var6 * 0.017453292F - (float) Math.PI);
        float var15 = MathHelper.sin(-var6 * 0.017453292F - (float) Math.PI);
        float var16 = -MathHelper.cos(-var5 * 0.017453292F);
        float var17 = MathHelper.sin(-var5 * 0.017453292F);
        float var18 = var15 * var16;
        float var20 = var14 * var16;
        double var21 = 5.0D;

        if(par2EntityPlayer instanceof EntityPlayerMP)
        {
            var21 = ((EntityPlayerMP) par2EntityPlayer).theItemInWorldManager.getBlockReachDistance();
        }

        Vec3 var23 = var13.addVector(var18 * var21, var17 * var21, var20 * var21);
        return par1World.rayTraceBlocks(var13, var23, par3);
    }

}
