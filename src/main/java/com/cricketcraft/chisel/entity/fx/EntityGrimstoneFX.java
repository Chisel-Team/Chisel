package com.cricketcraft.chisel.entity.fx;

import com.cricketcraft.chisel.block.BlockGrimstone;
import com.cricketcraft.chisel.utils.General;

import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.world.World;

public class EntityGrimstoneFX extends EntityFX
{
    float initialScale;
    float angleOffset;
    static final float fadetime = 20f;

    public EntityGrimstoneFX(World world, BlockGrimstone block, double x, double y, double z)
    {
        super(world, x, y, z, 0, 0, 0);

        initialScale = 1.0f + 1.0f * General.rand.nextFloat();
        angleOffset = rand.nextFloat() * 360;

        particleMaxAge = (int) (Math.random() * 10.0D) + 80;

        setPosition(x, y, z);
        prevPosX = posX;
        prevPosY = posY;
        prevPosZ = posZ;

        noClip = true;

        setParticleIcon(block.iconStar);
    }

    @Override
    public int getFXLayer()
    {
        return 1;
    }

    @Override
    public void renderParticle(Tessellator tessellator, float partialTick, float rotX, float rotXZ, float rotZ, float rotYZ, float rotXY)
    {
        particleScale = 0.25f + initialScale * (float) Math.sin((particleAge + angleOffset) / 180.f);

        if(particleAge < fadetime)
            particleAlpha = particleAge / fadetime;
        else if(particleAge + fadetime >= particleMaxAge)
            particleAlpha = (particleMaxAge - particleAge) / fadetime;
        else
            particleAlpha = 1.0f;

        super.renderParticle(tessellator, partialTick, rotX, rotXZ, rotZ, rotYZ, rotXY);

    }

    @Override
    public void onUpdate()
    {
        if(particleAge++ >= particleMaxAge)
        {
            setDead();
        }

    }
}
