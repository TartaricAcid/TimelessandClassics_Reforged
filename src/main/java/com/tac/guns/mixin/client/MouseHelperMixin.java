package com.tac.guns.mixin.client;

import com.tac.guns.client.handler.AimingHandler;
import com.tac.guns.duck.MouseSensitivityModifier;
import net.minecraft.client.Minecraft;
import net.minecraft.client.MouseHandler;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


/**
 * Author: Forked from MrCrayfish, continued by Timeless devs
 */
@Mixin(MouseHandler.class)
public abstract class MouseHelperMixin implements MouseSensitivityModifier {
    private double originSensitivity;
    private double sensitivity;
    @Final
    @Shadow
    private Minecraft minecraft;

    @Inject(method = "turnPlayer", at = @At("HEAD"))
    public void modifySensitivity(CallbackInfo ci) {
        originSensitivity = minecraft.options.sensitivity().get();
        if (AimingHandler.get().isAiming()) {
            minecraft.options.sensitivity().set(this.sensitivity);
        }
    }

    @Inject(method = "turnPlayer", at = @At("RETURN"))
    public void backtraceSensitivity(CallbackInfo ci) {
        minecraft.options.sensitivity().set(originSensitivity);
    }

    @Override
    public void setSensitivity(double sensitivity) {
        this.sensitivity = sensitivity;
    }
}