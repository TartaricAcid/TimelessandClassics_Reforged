package com.tac.guns.entity;

import com.tac.guns.Config;
import com.tac.guns.init.ModEntities;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

/**
 * Author: Forked from MrCrayfish, continued by Timeless devs
 */
public class ThrowableGrenadeEntity extends ThrowableItemEntity {
    public float rotation;
    public float prevRotation;
    public float power;
    public float radius;

    public ThrowableGrenadeEntity(EntityType<? extends ThrowableItemEntity> entityType, Level worldIn) {
        super(entityType, worldIn);
    }

    public ThrowableGrenadeEntity(EntityType<? extends ThrowableItemEntity> entityType, Level world, LivingEntity entity) {
        super(entityType, world, entity);
        //this.setShouldBounce(true);
        //this.setGravityVelocity(0.05F);
        //this.setItem(new ItemStack(ModItems.LIGHT_GRENADE.get()));
        //this.setMaxLife(20 * 3);
    }

    public ThrowableGrenadeEntity(Level world, LivingEntity entity, int timeLeft, float power, float radius) {
        super(ModEntities.THROWABLE_GRENADE.get(), world, entity);
        this.power = power;
        this.radius = radius;
        //this.setShouldBounce(true);
        //this.setGravityVelocity(0.045F);
        //this.setItem(new ItemStack(ModItems.LIGHT_GRENADE.get()));
        this.setMaxLife(timeLeft);
    }

    @Override
    protected void defineSynchedData() {
    }

    @Override
    public void tick() {
        super.tick();
        this.prevRotation = this.rotation;
        double speed = this.getDeltaMovement().length();
        if (speed > 0.1) {
            this.rotation += speed * 50;
        }
        if (this.level.isClientSide) {
            this.level.addParticle(ParticleTypes.SMOKE, true, this.getX(), this.getY() + 0.25, this.getZ(), 0, 0.1, 0);
        }
    }

    @Override
    public void onDeath() {
        GrenadeEntity.createExplosion(this, this.power, this.radius * Config.COMMON.grenades.explosionRadius.get().floatValue(), null);
    }
}
