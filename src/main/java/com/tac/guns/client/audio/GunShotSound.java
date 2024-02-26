package com.tac.guns.client.audio;

import com.tac.guns.Config;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.resources.sounds.AbstractSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;

/**
 * Author: Forked from MrCrayfish, continued by Timeless devs
 */
public class GunShotSound extends AbstractSoundInstance {
    public GunShotSound(ResourceLocation soundIn, SoundSource categoryIn, float x, float y, float z, float volume, float pitch, boolean reload) {
        super(soundIn, categoryIn, SoundInstance.createUnseededRandom());
        this.x = x;
        this.y = y;
        this.z = z;
        this.pitch = pitch;
        this.attenuation = Attenuation.NONE;

        LocalPlayer player = Minecraft.getInstance().player;
        if (player != null) {
            float distance = reload ? 16.0F : Config.SERVER.gunShotMaxDistance.get().floatValue();
            this.volume = volume * (1.0F - Math.min(1.0F, (float) Math.sqrt(player.distanceToSqr(x, y, z)) / distance));
            this.volume *= this.volume; //Ease the volume instead of linear
        }
    }
}
