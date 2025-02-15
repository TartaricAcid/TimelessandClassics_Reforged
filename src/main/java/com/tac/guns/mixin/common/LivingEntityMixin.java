package com.tac.guns.mixin.common;

import com.tac.guns.Config;
import com.tac.guns.entity.DamageSourceProjectile;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.damagesource.DamageSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Author: Forked from MrCrayfish, continued by Timeless devs
 */
@Mixin(LivingEntity.class)
public class LivingEntityMixin {
    private DamageSource source;

    @Inject(method = "hurt", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;knockback(DDD)V"))
    private void capture(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        this.source = source;
    }

    @ModifyArg(method = "hurt", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;knockback(DDD)V"), index = 0)
    private double modifyApplyKnockbackArgs(double original)
    {
        if(this.source instanceof DamageSourceProjectile)
        {
            if(!Config.COMMON.gameplay.enableKnockback.get())
            {
                return 0;
            }

            double strength = Config.COMMON.gameplay.knockbackStrength.get();
            if(strength > 0)
            {
                return strength;
            }
        }
        return original;
    }
}
