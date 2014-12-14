package com.cricketcraft.minecraft.chisel.entity.fx;

import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.world.World;

import com.cricketcraft.minecraft.chisel.block.BlockVoidstone;
import com.cricketcraft.minecraft.chisel.utils.General;

public class EntityVoidstoneFX extends EntityFX {

    static final float fadetime = 20F;
    float initialScale;
    float angleOffset;

    public EntityVoidstoneFX(World world, BlockVoidstone blockVoidstone, double x, double y, double z) {
        super(world, x, y, z, 0, 0, 0);
        initialScale = 1.0F + 1.0F * General.rand.nextFloat();
        angleOffset = rand.nextFloat() * 360;

        particleMaxAge = (int) (Math.random() * 10.0D) + 80;

        setPosition(x, y, z);
        prevPosX = posX;
        prevPosY = posY;
        prevPosZ = posZ;

        noClip = true;

        setParticleIcon(blockVoidstone.iconParticle);
    }

    @Override
    public int getFXLayer() {
        return 1;
    }

    @Override
    public void renderParticle(Tessellator tesselator, float partialTick, float rotX, float rotXZ, float rotZ, float rotYZ, float rotXY) {
        particleScale = 0.25F + initialScale * (float) Math.sin(particleAge + angleOffset) / 180.0F;

        if (particleAge < fadetime) {
            particleAlpha = particleAge / fadetime;
        } else if (particleAge + fadetime >= particleMaxAge) {
            particleAlpha = (particleMaxAge - particleAge) / fadetime;
        } else {
            particleAlpha = 1.0F;
        }

        super.renderParticle(tesselator, partialTick, rotX, rotXZ, rotZ, rotYZ, rotXY);
    }

    @Override
    public void onUpdate() {
        if (particleAge++ >= particleMaxAge) {
            setDead();
        }
    }
}
