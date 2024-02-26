package com.tac.guns.entity;

import com.tac.guns.common.Gun;
import com.tac.guns.item.GunItem;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

/**
 * Author: Forked from MrCrayfish, continued by Timeless devs
 */
public class GrenadeEntity extends ProjectileEntity {
    public GrenadeEntity(EntityType<? extends ProjectileEntity> entityType, Level world) {
        super(entityType, world);
    }

    public GrenadeEntity(EntityType<? extends ProjectileEntity> entityType, Level world, LivingEntity shooter, ItemStack weapon, GunItem item, Gun modifiedGun) {
        super(entityType, world, shooter, weapon, item, modifiedGun, 0, 0);
    }

    @Override
    protected void onHitEntity(Entity entity, Vec3 hitVec, Vec3 startVec, Vec3 endVec, boolean headshot) {
        createExplosion(this, this.getDamage(), this.getRadius(), null);
    }

    @Override
    protected void onHitBlock(BlockState state, BlockPos pos, Direction face, Vec3 hitVec) {
        createExplosion(this, this.getDamage(), this.getRadius(), null);
    }

    @Override
    public void onExpired() {
        createExplosion(this, this.getDamage(), this.getRadius(), null);
    }
}
