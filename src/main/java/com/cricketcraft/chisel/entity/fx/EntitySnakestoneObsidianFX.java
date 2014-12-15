package com.cricketcraft.chisel.entity.fx;

import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.world.World;

import org.lwjgl.opengl.GL11;

import com.cricketcraft.chisel.block.BlockSnakestoneObsidian;
import com.cricketcraft.chisel.utils.General;

public class EntitySnakestoneObsidianFX extends EntityFX
{
    BlockSnakestoneObsidian block;
    double tx, ty, tz;
    double speed;

    public EntitySnakestoneObsidianFX(World world, BlockSnakestoneObsidian b, int x, int y, int z)
    {
        super(world, x + 0.5, y + 0.5, z + 0.5, 0, 0, 0);

        block = b;

        particleScale = 0.5f + 0.5f * General.rand.nextFloat();
        speed = 0.04 + 0.04 * General.rand.nextDouble();

        particleMaxAge = (int) (Math.random() * 10.0D) + 40;

        tx = x + General.rand.nextDouble();
        ty = y + General.rand.nextDouble();
        tz = z + General.rand.nextDouble();
        switch(General.rand.nextInt(6))
        {
            case 0:
                tx = x;
                break;
            case 1:
                tx = x + 1;
                break;
            case 2:
                ty = y;
                break;
            case 3:
                ty = y + 1;
                break;
            case 4:
                tz = z;
                break;
            case 5:
                tz = z + 1;
                break;
        }

        double dx = (tx - posX) * 3;
        double dy = (ty - posY) * 3;
        double dz = (tz - posZ) * 3;

        setPosition(posX + dx, posY + dy, posZ + dz);
        prevPosX = posX;
        prevPosY = posY;
        prevPosZ = posZ;

        noClip = true;

        setParticleIcon(block.particles[General.rand.nextInt(block.particles.length)]);
    }

    @Override
    public int getFXLayer()
    {
        return 1;
    }

    @Override
    public void renderParticle(Tessellator tessellator, float partialTick, float rotX, float rotXZ, float rotZ, float rotYZ, float rotXY)
    {
        GL11.glDepthMask(false);
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        super.renderParticle(tessellator, partialTick, rotX, rotXZ, rotZ, rotYZ, rotXY);
    }

    @Override
    public void onUpdate()
    {
        if(particleAge++ >= particleMaxAge)
        {
            setDead();
            return;
        }

        double dx = tx - posX;
        double dy = ty - posY;
        double dz = tz - posZ;

        double distance = Math.sqrt(dx * dx + dy * dy + dz * dz);
        if(distance == 0)
        {
            setDead();
            return;
        }
        if(distance < 0.4)
        {
            particleAlpha = (float) (distance / 0.4);
        } else if(particleAge < 20)
        {
            particleAlpha = 1.0f * particleAge / 20;
        } else
        {
            particleAlpha = 1.0f;
        }

        if(distance < speed)
        {
            speed = distance;
        }

        prevPosX = posX;
        prevPosY = posY;
        prevPosZ = posZ;

        double px = posX + dx / distance * speed;
        double py = posY + dy / distance * speed;
        double pz = posZ + dz / distance * speed;

        setPosition(px, py, pz);
    }
}
