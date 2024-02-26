package com.tac.guns.client.render.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

/**
 * Author: Forked from MrCrayfish, continued by Timeless devs
 */
@OnlyIn(Dist.CLIENT)
public class BloodParticle extends ExplodeParticle {
    BloodParticle(ClientLevel world, double x, double y, double z, double motionX, double motionY, double motionZ, SpriteSet spriteSet) {
        super(world, x, y, z, motionX, motionY, motionZ, spriteSet);

        this.setColor(0.45F, 0.027F, 0.027F);
        this.quadSize = 0.2f + 0.1f * this.random.nextFloat();
        this.gravity = 0.65F;
    }

    @OnlyIn(Dist.CLIENT)
    public static class Provider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprites;

        public Provider(SpriteSet p_106588_) {
            this.sprites = p_106588_;
        }

        @Override
        public Particle createParticle(@NotNull SimpleParticleType p_106599_, @NotNull ClientLevel p_106600_, double p_106601_, double p_106602_, double p_106603_, double p_106604_, double p_106605_, double p_106606_) {
            return new BloodParticle(p_106600_, p_106601_, p_106602_, p_106603_, p_106604_, p_106605_, p_106606_, this.sprites);
        }
    }
}
