package com.tac.guns.client.handler.command;

import com.google.gson.GsonBuilder;
import com.tac.guns.Config;
import com.tac.guns.client.Keys;
import com.tac.guns.common.Gun;
import com.tac.guns.common.tooling.CommandsHandler;
import com.tac.guns.item.attachment.IAttachment;
import com.tac.guns.item.transition.TimelessGunItem;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.apache.logging.log4j.Level;
import org.lwjgl.glfw.GLFW;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

import static com.tac.guns.GunMod.LOGGER;

public class GunEditor {
    private static GunEditor instance;
    double reloadMagTimerMod = 0;
    double additionalReloadEmptyMagTimerMod = 0;
    double reloadAmountMod = 0;
    double preReloadPauseTicksMod = 0;
    double interReloadPauseTicksMod = 0;
    private String previousWeaponTag = "";
    private TaCWeaponDevModes prevMode;
    private TaCWeaponDevModes mode;
    private HashMap<String, Gun> map = new HashMap<>();
    private boolean resetMode;
    private double rateMod = 0;
    private double burstRateMod = 0;
    private float recoilAngleMod = 0;
    private float recoilKickMod = 0;
    private float horizontalRecoilAngleMod = 0;
    private float cameraRecoilModifierMod = 0;
    private float weaponRecoilDurationMod = 0;
    private float cameraRecoilDurationMod = 0;
    private float recoilDurationMod = 0;
    private float recoilAdsReductionMod = 0;
    private double projectileAmountMod = 0;
    private float spreadMod = 0;
    private float weightKiloMod = 0;
    private float damageMod = 0;
    private float armorIgnoreMod = 0;
    private float criticalMod = 0;
    private float criticalDamageMod = 0;
    private float headDamageMod = 0;
    private float closeDamageMod = 0;
    private float decayStartMod = 0;
    private float minDecayMultiplierMod = 0;
    private float decayEndMod = 0;
    private float sizePrjMod = 0;
    private double speedMod = 0;
    private double lifeMod = 0;
    private double xMod = 0;
    private double yMod = 0;
    private double zMod = 0;
    private boolean controlToggle = false;
    private boolean altToggle = false;
    private double sizeMod = 0;

    private GunEditor() {
    }

    public static GunEditor get() {
        if (instance == null) {
            instance = new GunEditor();
        }
        return instance;
    }

    public static String formattedModeContext() {
        String result = "\n";
        for (TaCWeaponDevModes mode : TaCWeaponDevModes.values()) {
            result += mode.tagName + "\n";
        }
        return result;
    }

    public TaCWeaponDevModes getMode() {
        return this.mode;
    }

    public void setMode(TaCWeaponDevModes mode) {
        this.mode = mode;
    }

    public void setResetMode(boolean reset) {
        this.resetMode = reset;
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (!Config.COMMON.development.enableTDev.get())
            return;
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null)
            return;
        CommandsHandler ch = CommandsHandler.get();
        if (ch == null || ch.getCatCurrentIndex() != 1)
            return;
        if ((mc.player.getMainHandItem() == null || mc.player.getMainHandItem() == ItemStack.EMPTY || !(mc.player.getMainHandItem().getItem() instanceof TimelessGunItem)))
            return;
        TimelessGunItem gunItem = (TimelessGunItem) mc.player.getMainHandItem().getItem();
        if (this.prevMode == null)
            this.prevMode = this.mode;
        else if (this.prevMode != this.mode && this.resetMode) {
            this.resetMode = false;
            resetData();
            ensureData(getMapItem(gunItem.getDescriptionId(), gunItem.getGun()), gunItem.getGun().copy());
            this.prevMode = this.mode;
        }
        if (this.previousWeaponTag == "")
            this.previousWeaponTag = gunItem.getDescriptionId();
        else if (this.previousWeaponTag != gunItem.getDescriptionId()) {
            this.previousWeaponTag = gunItem.getDescriptionId();
            resetData();
            /*if(this.map.containsKey(gunItem.getTranslationKey()))
                ensureData(this.map.get(gunItem.getTranslationKey()), gunItem.getGun().copy());*/
            ensureData(getMapItem(gunItem.getDescriptionId(), gunItem.getGun()), gunItem.getGun().copy());
        }
    }

    @SubscribeEvent
    public void onKeyPressed(InputEvent.Key event) {
        if (!Config.COMMON.development.enableTDev.get())
            return;
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null)
            return;
        if ((mc.player.getMainHandItem() == null || mc.player.getMainHandItem() == ItemStack.EMPTY || !(mc.player.getMainHandItem().getItem() instanceof TimelessGunItem)))
            return;
        CommandsHandler ch = CommandsHandler.get();
        if (ch == null || ch.getCatCurrentIndex() != 1)
            return;
        TimelessGunItem gunItem = (TimelessGunItem) mc.player.getMainHandItem().getItem();
        if (ch.catInGlobal(1) && this.mode != null) {
            //TODO: HANDLE FOR PER MODULE, BEFORE APPLICATION, SAVE DATA ON INSTANCE TO SERIALIZE LATER.
            switch (this.mode) {
                case general:
                    this.handleGeneralMod(event, gunItem);
                    break;
                case reloads:
                    this.handleReloadsMod(event, gunItem);
                    break;
                case projectile:
                    this.handleProjectileMod(event, gunItem);
                    break;
                case display:
                    break;
                case flash:
                    this.handleFlashMod(event, gunItem);
                    break;
                case zoom:
                    this.handleZoomMod(event, gunItem);
                    break;
                case scope:
                    this.handleScopeMod(event, gunItem);
                    break;
                case barrel:
                    this.handleBarrelMod(event, gunItem);
                    break;
                case oldScope:
                    this.handleOldScopeMod(event, gunItem);
                    break;
                case pistolScope:
                    this.handlePistolScopeMod(event, gunItem);
                    break;
                case pistolBarrel:
                    this.handlePistolBarrelMod(event, gunItem);
                    break;
                default:
                    break;
            }
            /*NetworkGunManager manager = NetworkGunManager.get();
            if (manager != null)
                PacketHandler.getPlayChannel().send(PacketDistributor.ALL.noArg(), new MessageUpdateGuns());*/
        }

    }

    public double getRateMod() {
        return rateMod;
    }

    public double getBurstRateMod() {
        return burstRateMod;
    }

    public float getRecoilAngleMod() {
        return recoilAngleMod;
    }

    public float getRecoilKickMod() {
        return recoilKickMod;
    }

    public float getHorizontalRecoilAngleMod() {
        return horizontalRecoilAngleMod;
    }

    public float getCameraRecoilModifierMod() {
        return cameraRecoilModifierMod;
    }

    public float getWeaponRecoilDurationMod() {
        return weaponRecoilDurationMod;
    }

    public float getcameraRecoilDurationMod() {
        return cameraRecoilDurationMod;
    }

    public float getRecoilDurationMod() {
        return recoilDurationMod;
    }

    public float getRecoilAdsReductionMod() {
        return recoilAdsReductionMod;
    }

    public double getProjectileAmountMod() {
        return projectileAmountMod;
    }

    public float getSpreadMod() {
        return spreadMod;
    }

    public float getWeightKiloMod() {
        return weightKiloMod;
    }

    private void handleGeneralMod(InputEvent.Key event, TimelessGunItem gunItem) {
        double stepModifier = 1;
        boolean isLeft = event.getKey() == GLFW.GLFW_KEY_LEFT;
        boolean isRight = event.getKey() == GLFW.GLFW_KEY_RIGHT;
        boolean isUp = event.getKey() == GLFW.GLFW_KEY_UP;
        boolean isDown = event.getKey() == GLFW.GLFW_KEY_DOWN;
        boolean isControlDown = Keys.CONTROLLY.isDown() || Keys.CONTROLLYR.isDown(); // Increase Module Size
        boolean isShiftDown = Keys.SHIFTY.isDown() || Keys.SHIFTYR.isDown(); // Increase Step Size
        boolean isAltDown = Keys.ALTY.isDown() || Keys.ALTYR.isDown(); // Swap X -> Z modify
        Player player = Minecraft.getInstance().player;
        Gun gunTmp = gunItem.getGun();
        if (Keys.P.isDown()) {
            if (isShiftDown)
                stepModifier *= 10;
            if (isControlDown)
                stepModifier /= 10;
            player.displayClientMessage(Component.translatable("VerticalRecoilAngle: " + gunTmp.getGeneral().getRecoilAngle() + " | HorizontalRecoilAngle: " + gunTmp.getGeneral().getHorizontalRecoilAngle() + " | RecoilKick: " + gunTmp.getGeneral().getRecoilKick()), true);
            if (isLeft) {
                this.horizontalRecoilAngleMod += 0.025 * stepModifier;
                player.displayClientMessage(Component.translatable("HorizontalRecoilAngle: " + gunTmp.getGeneral().getHorizontalRecoilAngle()).withStyle(ChatFormatting.GREEN), true);
            } else if (isRight) {
                this.horizontalRecoilAngleMod -= 0.025 * stepModifier;
                player.displayClientMessage(Component.translatable("HorizontalRecoilAngle: " + gunTmp.getGeneral().getHorizontalRecoilAngle()).withStyle(ChatFormatting.DARK_RED), true);
            } else if (isUp && isAltDown) {
                this.recoilKickMod += 0.025 * stepModifier;
                player.displayClientMessage(Component.translatable("RecoilKick: " + gunTmp.getGeneral().getRecoilKick()).withStyle(ChatFormatting.GREEN), true);
            } else if (isDown && isAltDown) {
                this.recoilKickMod -= 0.025 * stepModifier;
                player.displayClientMessage(Component.translatable("RecoilKick: " + gunTmp.getGeneral().getRecoilKick()).withStyle(ChatFormatting.DARK_RED), true);
            } else if (isUp) {
                this.recoilAngleMod += 0.025 * stepModifier;
                player.displayClientMessage(Component.translatable("VerticalRecoilAngle: " + gunTmp.getGeneral().getRecoilAngle()).withStyle(ChatFormatting.GREEN), true);
            } else if (isDown) {
                this.recoilAngleMod -= 0.025 * stepModifier;
                player.displayClientMessage(Component.translatable("VerticalRecoilAngle: " + gunTmp.getGeneral().getRecoilAngle()).withStyle(ChatFormatting.DARK_RED), true);
            }
        } else if (Keys.L.isDown()) {
            if (isShiftDown)
                stepModifier *= 5;
            player.displayClientMessage(Component.translatable("weaponRecoilOffset: " + gunTmp.getGeneral().getWeaponRecoilOffset() + " | RecoilDuration: " + gunTmp.getGeneral().getRecoilDuration()), true);
            if (isLeft) {
                this.weaponRecoilDurationMod += 0.0025 * stepModifier;
                player.displayClientMessage(Component.translatable("weaponRecoilOffset: " + gunTmp.getGeneral().getWeaponRecoilOffset()).withStyle(ChatFormatting.GREEN), true);
            } else if (isRight) {
                this.weaponRecoilDurationMod -= 0.0025 * stepModifier;
                player.displayClientMessage(Component.translatable("weaponRecoilOffset: " + gunTmp.getGeneral().getWeaponRecoilOffset()).withStyle(ChatFormatting.DARK_RED), true);
            } else if (isUp) {
                this.recoilDurationMod += 0.0025 * stepModifier;
                player.displayClientMessage(Component.translatable("RecoilDuration: " + gunTmp.getGeneral().getRecoilDuration()).withStyle(ChatFormatting.GREEN), true);
            } else if (isDown) {
                this.recoilDurationMod -= 0.0025 * stepModifier;
                player.displayClientMessage(Component.translatable("RecoilDuration: " + gunTmp.getGeneral().getRecoilDuration()).withStyle(ChatFormatting.DARK_RED), true);
            }
        } else if (Keys.O.isDown()) {
            if (isShiftDown)
                stepModifier *= 5;
            player.displayClientMessage(Component.translatable("CameraRecoilModifier: " + gunTmp.getGeneral().getCameraRecoilModifier()), true);
            if (isUp) {
                this.cameraRecoilModifierMod += 0.0025 * stepModifier;
                player.displayClientMessage(Component.translatable("CameraRecoilModifier: " + gunTmp.getGeneral().getCameraRecoilModifier()).withStyle(ChatFormatting.GREEN), true);
            } else if (isDown) {
                this.cameraRecoilModifierMod -= 0.0025 * stepModifier;
                player.displayClientMessage(Component.translatable("CameraRecoilModifier: " + gunTmp.getGeneral().getCameraRecoilModifier()).withStyle(ChatFormatting.DARK_RED), true);
            }
        } else if (Keys.K.isDown()) {
            if (isShiftDown)
                stepModifier *= 5;
            player.displayClientMessage(Component.translatable("RecoilAdsReduction: " + gunTmp.getGeneral().getRecoilAdsReduction()), true);
            if (isUp) {
                this.recoilAdsReductionMod += 0.001 * stepModifier;
                player.displayClientMessage(Component.translatable("RecoilAdsReduction: " + gunTmp.getGeneral().getRecoilAdsReduction()).withStyle(ChatFormatting.GREEN), true);
            } else if (isDown) {
                this.recoilAdsReductionMod -= 0.001 * stepModifier;
                player.displayClientMessage(Component.translatable("RecoilAdsReduction: " + gunTmp.getGeneral().getRecoilAdsReduction()).withStyle(ChatFormatting.DARK_RED), true);
            }
        } else if (Keys.M.isDown()) {
            if (isShiftDown)
                stepModifier *= 10;
            if (isControlDown)
                stepModifier /= 10;
            player.displayClientMessage(Component.translatable("Inaccuracy in Degrees: " + gunTmp.getGeneral().getSpread()), true);
            if (isUp) {
                this.spreadMod += 0.025 * stepModifier;
                player.displayClientMessage(Component.translatable("Inaccuracy in Degrees: " + gunTmp.getGeneral().getSpread()).withStyle(ChatFormatting.GREEN), true);
            } else if (isDown) {
                this.spreadMod -= 0.025 * stepModifier;
                player.displayClientMessage(Component.translatable("Inaccuracy in Degrees: " + gunTmp.getGeneral().getSpread()).withStyle(ChatFormatting.DARK_RED), true);
            }
        } else if (Keys.I.isDown()) {
            if (isShiftDown)
                stepModifier *= 10;
            if (isControlDown)
                stepModifier /= 10;
            player.displayClientMessage(Component.translatable("Weight in Kilograms: " + gunTmp.getGeneral().getWeightKilo()), true);
            if (isUp) {
                this.weightKiloMod += 0.025 * stepModifier;
                player.displayClientMessage(Component.translatable("Weight in Kilograms: " + gunTmp.getGeneral().getWeightKilo()).withStyle(ChatFormatting.GREEN), true);
            } else if (isDown) {
                this.weightKiloMod -= 0.025 * stepModifier;
                player.displayClientMessage(Component.translatable("Weight in Kilograms: " + gunTmp.getGeneral().getWeightKilo()).withStyle(ChatFormatting.DARK_RED), true);
            }
        } else if (Keys.J.isDown()) {
            player.displayClientMessage(Component.translatable("Rate in Ticks: " + gunTmp.getGeneral().getRate() + " | Burst Rate in Ticks:: " + gunTmp.getGeneral().getBurstRate()), true);
            if (isLeft) {
                this.burstRateMod += 0.5 * stepModifier;
                player.displayClientMessage(Component.translatable("Burst Rate in Ticks: " + gunTmp.getGeneral().getBurstRate()).withStyle(ChatFormatting.GREEN), true);
            } else if (isRight) {
                this.burstRateMod -= 0.5 * stepModifier;
                player.displayClientMessage(Component.translatable("Burst Rate in Ticks: " + gunTmp.getGeneral().getBurstRate()).withStyle(ChatFormatting.DARK_RED), true);
            } else if (isUp) {
                this.rateMod += 0.5;
                player.displayClientMessage(Component.translatable("Rate in Ticks: " + gunTmp.getGeneral().getRate()).withStyle(ChatFormatting.GREEN), true);
            } else if (isDown) {
                this.rateMod -= 0.5;
                player.displayClientMessage(Component.translatable("Rate in Ticks: " + gunTmp.getGeneral().getRate()).withStyle(ChatFormatting.DARK_RED), true);
            }
        } else if (Keys.N.isDown()) {
            player.displayClientMessage(Component.translatable("Projectile Amount: " + gunTmp.getGeneral().getProjectileAmount()), true);
            if (isUp) {
                this.projectileAmountMod += 0.5;
                player.displayClientMessage(Component.translatable("Projectile Amount: " + gunTmp.getGeneral().getProjectileAmount()).withStyle(ChatFormatting.GREEN), true);
            } else if (isDown) {
                this.projectileAmountMod -= 0.5;
                player.displayClientMessage(Component.translatable("Projectile Amount: " + gunTmp.getGeneral().getProjectileAmount()).withStyle(ChatFormatting.DARK_RED), true);
            }
        }

        CompoundTag gun = getMapItem(gunItem.getDescriptionId(), gunItem.getGun()).serializeNBT(); // Copy to ensure we are grabbing a copy of this data. new CompoundNBT();//
        gun.getCompound("General").remove("Rate");
        gun.getCompound("General").remove("RecoilAngle");
        gun.getCompound("General").remove("RecoilKick");
        gun.getCompound("General").remove("HorizontalRecoilAngle");
        gun.getCompound("General").remove("CameraRecoilModifier");
        gun.getCompound("General").remove("RecoilDurationOffset");
        gun.getCompound("General").remove("weaponRecoilOffset");
        gun.getCompound("General").remove("RecoilAdsReduction");
        gun.getCompound("General").remove("ProjectileAmount");
        gun.getCompound("General").remove("Spread");
        gun.getCompound("General").remove("WeightKilo");
        gun.getCompound("General").putDouble("Rate", gunItem.getGun().getGeneral().getRate());
        gun.getCompound("General").putDouble("RecoilAngle", gunItem.getGun().getGeneral().getRecoilAngle());
        gun.getCompound("General").putDouble("RecoilKick", gunItem.getGun().getGeneral().getRecoilKick());
        gun.getCompound("General").putDouble("HorizontalRecoilAngle", gunItem.getGun().getGeneral().getHorizontalRecoilAngle());
        gun.getCompound("General").putDouble("CameraRecoilModifier", gunItem.getGun().getGeneral().getCameraRecoilModifier());
        gun.getCompound("General").putDouble("RecoilDurationOffset", gunItem.getGun().getGeneral().getRecoilDuration());
        gun.getCompound("General").putDouble("weaponRecoilOffset", gunItem.getGun().getGeneral().getWeaponRecoilOffset());
        gun.getCompound("General").putDouble("RecoilAdsReduction", gunItem.getGun().getGeneral().getRecoilAdsReduction());
        gun.getCompound("General").putDouble("ProjectileAmount", gunItem.getGun().getGeneral().getProjectileAmount());
        gun.getCompound("General").putDouble("Spread", gunItem.getGun().getGeneral().getSpread());
        gun.getCompound("General").putDouble("WeightKilo", gunItem.getGun().getGeneral().getWeightKilo());
        /*gunItem.getGun().getGeneral().deserializeNBT(gun);
        this.map.put(gunItem.getTranslationKey(), gunItem.getGun());*/
        this.getMapItem(gunItem.getDescriptionId(), gunItem.getGun()).deserializeNBT(gun);
    }

    public float getDamageMod() {
        return this.damageMod;
    }

    public float getArmorIgnoreMod() {
        return this.armorIgnoreMod;
    }

    public float getCriticalMod() {
        return this.criticalMod;
    }

    public float getCriticalDamageMod() {
        return this.criticalDamageMod;
    }

    public float getHeadDamageMod() {
        return this.headDamageMod;
    }

    public float getCloseDamageMod() {
        return this.closeDamageMod;
    }

    public float getDecayStartMod() {
        return this.decayStartMod;
    }

    public float getMinDecayMultiplierMod() {
        return this.minDecayMultiplierMod;
    }

    public float getDecayEndMod() {
        return this.decayEndMod;
    }

    public float getSizePrjMod() {
        return this.sizePrjMod;
    }

    public double getSpeedMod() {
        return this.speedMod;
    }

    public double getLifeMod() {
        return this.lifeMod;
    }

    private void handleProjectileMod(InputEvent.Key event, TimelessGunItem gunItem) {
        double stepModifier = 1;
        boolean isUp = event.getKey() == GLFW.GLFW_KEY_UP;
        boolean isDown = event.getKey() == GLFW.GLFW_KEY_DOWN;
        boolean isControlDown = Keys.CONTROLLY.isDown() || Keys.CONTROLLYR.isDown(); // Increase Module Size
        boolean isShiftDown = Keys.SHIFTY.isDown() || Keys.SHIFTYR.isDown(); // Increase Step Size
        Player player = Minecraft.getInstance().player;
        Gun gunTmp = gunItem.getGun();
        if (Keys.P.isDown()) {
            if (isShiftDown)
                stepModifier *= 10;
            if (isControlDown)
                stepModifier /= 10;
            player.displayClientMessage(Component.translatable("Damage: " + gunTmp.getProjectile().getDamage()), true);
            if (isUp) {
                this.damageMod += 0.025f * stepModifier;
                player.displayClientMessage(Component.translatable("Damage: " + gunTmp.getProjectile().getDamage()).withStyle(ChatFormatting.GREEN), true);
                this.criticalMod += 0.025f * stepModifier;
                player.displayClientMessage(Component.translatable("Damage: " + gunTmp.getProjectile().getGunCritical()).withStyle(ChatFormatting.GREEN), true);
                this.armorIgnoreMod += 0.025f * stepModifier;
                player.displayClientMessage(Component.translatable("Damage: " + gunTmp.getProjectile().getGunArmorIgnore()).withStyle(ChatFormatting.GREEN), true);
                this.criticalDamageMod += 0.025f * stepModifier;
                player.displayClientMessage(Component.translatable("Damage: " + gunTmp.getProjectile().getGunCriticalDamage()).withStyle(ChatFormatting.GREEN), true);
                this.headDamageMod += 0.025f * stepModifier;
                player.displayClientMessage(Component.translatable("Damage: " + gunTmp.getProjectile().getGunHeadDamage()).withStyle(ChatFormatting.GREEN), true);
                this.closeDamageMod += 0.025f * stepModifier;
                player.displayClientMessage(Component.translatable("Damage: " + gunTmp.getProjectile().getGunCloseDamage()).withStyle(ChatFormatting.GREEN), true);
                this.decayStartMod += 0.025f * stepModifier;
                player.displayClientMessage(Component.translatable("Damage: " + gunTmp.getProjectile().getGunDecayStart()).withStyle(ChatFormatting.GREEN), true);
                this.minDecayMultiplierMod += 0.025f * stepModifier;
                player.displayClientMessage(Component.translatable("Damage: " + gunTmp.getProjectile().getGunMinDecayMultiplier()).withStyle(ChatFormatting.GREEN), true);
                this.decayEndMod += 0.025f * stepModifier;
                player.displayClientMessage(Component.translatable("Damage: " + gunTmp.getProjectile().getGunDecayEnd()).withStyle(ChatFormatting.GREEN), true);
            } else if (isDown) {
                this.damageMod -= 0.025f * stepModifier;
                player.displayClientMessage(Component.translatable("Damage: " + gunTmp.getProjectile().getDamage()).withStyle(ChatFormatting.DARK_RED), true);
                this.criticalMod -= 0.025f * stepModifier;
                player.displayClientMessage(Component.translatable("Damage: " + gunTmp.getProjectile().getGunCritical()).withStyle(ChatFormatting.DARK_RED), true);
                this.armorIgnoreMod -= 0.025f * stepModifier;
                player.displayClientMessage(Component.translatable("Damage: " + gunTmp.getProjectile().getGunArmorIgnore()).withStyle(ChatFormatting.DARK_RED), true);
                this.criticalDamageMod -= 0.025f * stepModifier;
                player.displayClientMessage(Component.translatable("Damage: " + gunTmp.getProjectile().getGunCriticalDamage()).withStyle(ChatFormatting.DARK_RED), true);
                this.headDamageMod -= 0.025f * stepModifier;
                player.displayClientMessage(Component.translatable("Damage: " + gunTmp.getProjectile().getGunHeadDamage()).withStyle(ChatFormatting.DARK_RED), true);
                this.closeDamageMod -= 0.025f * stepModifier;
                player.displayClientMessage(Component.translatable("Damage: " + gunTmp.getProjectile().getGunCloseDamage()).withStyle(ChatFormatting.DARK_RED), true);
                this.decayStartMod -= 0.025f * stepModifier;
                player.displayClientMessage(Component.translatable("Damage: " + gunTmp.getProjectile().getGunDecayStart()).withStyle(ChatFormatting.DARK_RED), true);
                this.minDecayMultiplierMod -= 0.025f * stepModifier;
                player.displayClientMessage(Component.translatable("Damage: " + gunTmp.getProjectile().getGunMinDecayMultiplier()).withStyle(ChatFormatting.DARK_RED), true);
                this.decayEndMod -= 0.025f * stepModifier;
                player.displayClientMessage(Component.translatable("Damage: " + gunTmp.getProjectile().getGunDecayEnd()).withStyle(ChatFormatting.DARK_RED), true);
            }
        } else if (Keys.L.isDown()) {
            if (isShiftDown)
                stepModifier *= 10;
            if (isControlDown)
                stepModifier /= 10;
            player.displayClientMessage(Component.translatable("Projectile size: " + gunTmp.getProjectile().getSize()), true);
            if (isUp) {
                this.sizePrjMod += 0.025f * stepModifier;
                player.displayClientMessage(Component.translatable("Projectile size: " + gunTmp.getProjectile().getSize()).withStyle(ChatFormatting.GREEN), true);
            } else if (isDown) {
                this.sizePrjMod -= 0.025f * stepModifier;
                player.displayClientMessage(Component.translatable("Projectile size: " + gunTmp.getProjectile().getSize()).withStyle(ChatFormatting.DARK_RED), true);
            }
        } else if (Keys.O.isDown()) {
            if (isShiftDown)
                stepModifier *= 10;
            player.displayClientMessage(Component.translatable("Speed: " + gunTmp.getProjectile().getSpeed()), true);
            if (isUp) {
                this.speedMod += 0.025 * stepModifier;
                player.displayClientMessage(Component.translatable("Speed: " + gunTmp.getProjectile().getSpeed()).withStyle(ChatFormatting.GREEN), true);
            } else if (isDown) {
                this.speedMod -= 0.025 * stepModifier;
                player.displayClientMessage(Component.translatable("Speed: " + gunTmp.getProjectile().getSpeed()).withStyle(ChatFormatting.DARK_RED), true);
            }
        } else if (Keys.K.isDown()) {
            player.displayClientMessage(Component.translatable("Ticks bullet Exists for: " + gunTmp.getProjectile().getLife()), true);
            if (isUp) {
                this.lifeMod += 0.5;
                player.displayClientMessage(Component.translatable("Ticks bullet Exists for: " + gunTmp.getProjectile().getLife()).withStyle(ChatFormatting.GREEN), true);
            } else if (isDown) {
                this.lifeMod -= 0.5;
                player.displayClientMessage(Component.translatable("Ticks bullet Exists for: " + gunTmp.getProjectile().getLife()).withStyle(ChatFormatting.DARK_RED), true);
            }
        }

        CompoundTag gun = getMapItem(gunItem.getDescriptionId(), gunItem.getGun()).serializeNBT(); // Copy to ensure we are grabbing a copy of this data. new CompoundNBT();//
        gun.getCompound("Projectile").remove("Damage");
        gun.getCompound("Projectile").remove("ArmorIgnore");
        gun.getCompound("Projectile").remove("Critical");
        gun.getCompound("Projectile").remove("CriticalDamage");
        gun.getCompound("Projectile").remove("HeadDamage");
        gun.getCompound("Projectile").remove("Size");
        gun.getCompound("Projectile").remove("Speed");
        gun.getCompound("Projectile").remove("Life");
        gun.getCompound("Projectile").putDouble("Damage", gunItem.getGun().getProjectile().getDamage());
        gun.getCompound("Projectile").putDouble("ArmorIgnore", gunItem.getGun().getProjectile().getGunArmorIgnore());
        gun.getCompound("Projectile").putDouble("Critical", gunItem.getGun().getProjectile().getGunCritical());
        gun.getCompound("Projectile").putDouble("CriticalDamage", gunItem.getGun().getProjectile().getGunCriticalDamage());
        gun.getCompound("Projectile").putDouble("HeadDamage", gunItem.getGun().getProjectile().getGunHeadDamage());
        gun.getCompound("Projectile").putDouble("Size", gunItem.getGun().getProjectile().getSize());
        gun.getCompound("Projectile").putDouble("Speed", gunItem.getGun().getProjectile().getSpeed());
        gun.getCompound("Projectile").putDouble("Life", gunItem.getGun().getProjectile().getLife());
        /*gunItem.getGun().getGeneral().deserializeNBT(gun);
        this.map.put(gunItem.getTranslationKey(), gunItem.getGun());*/
        this.getMapItem(gunItem.getDescriptionId(), gunItem.getGun()).deserializeNBT(gun);
    }

    public double getReloadMagTimerMod() {
        return reloadMagTimerMod;
    }

    public double getAdditionalReloadEmptyMagTimerMod() {
        return additionalReloadEmptyMagTimerMod;
    }

    public double getReloadAmountMod() {
        return reloadAmountMod;
    }

    public double getPreReloadPauseTicksMod() {
        return preReloadPauseTicksMod;
    }

    public double getInterReloadPauseTicksMod() {
        return interReloadPauseTicksMod;
    }

    private void handleReloadsMod(InputEvent.Key event, TimelessGunItem gunItem) {
        double stepModifier = 1;
        boolean isUp = event.getKey() == GLFW.GLFW_KEY_UP;
        boolean isDown = event.getKey() == GLFW.GLFW_KEY_DOWN;
        Player player = Minecraft.getInstance().player;
        Gun gunTmp = gunItem.getGun();
        if (Keys.P.isDown()) {
            player.displayClientMessage(Component.translatable("ReloadMagTimer: " + gunTmp.getReloads().getReloadMagTimer()), true);
            if (isUp) {
                this.reloadMagTimerMod += 0.5;
                player.displayClientMessage(Component.translatable("ReloadMagTimer: " + gunTmp.getReloads().getReloadMagTimer()).withStyle(ChatFormatting.GREEN), true);
            } else if (isDown) {
                this.reloadMagTimerMod -= 0.5;
                player.displayClientMessage(Component.translatable("ReloadMagTimer: " + gunTmp.getReloads().getReloadMagTimer()).withStyle(ChatFormatting.DARK_RED), true);
            }
        } else if (Keys.L.isDown()) {
            player.displayClientMessage(Component.translatable("AdditionalReloadEmptyMagTimer: " + gunTmp.getReloads().getAdditionalReloadEmptyMagTimer()), true);
            if (isUp) {
                this.additionalReloadEmptyMagTimerMod += 0.5;
                player.displayClientMessage(Component.translatable("AdditionalReloadEmptyMagTimer: " + gunTmp.getReloads().getAdditionalReloadEmptyMagTimer()).withStyle(ChatFormatting.GREEN), true);
            } else if (isDown) {
                this.additionalReloadEmptyMagTimerMod -= 0.5;
                player.displayClientMessage(Component.translatable("AdditionalReloadEmptyMagTimer: " + gunTmp.getReloads().getAdditionalReloadEmptyMagTimer()).withStyle(ChatFormatting.DARK_RED), true);
            }
        } else if (Keys.O.isDown()) {
            player.displayClientMessage(Component.translatable("ReloadAmount: " + gunTmp.getReloads().getReloadAmount()), true);
            if (isUp) {
                this.reloadAmountMod += 0.5;
                player.displayClientMessage(Component.translatable("ReloadAmount: " + gunTmp.getReloads().getReloadAmount()).withStyle(ChatFormatting.GREEN), true);
            } else if (isDown) {
                this.reloadAmountMod -= 0.5;
                player.displayClientMessage(Component.translatable("ReloadAmount: " + gunTmp.getReloads().getReloadAmount()).withStyle(ChatFormatting.DARK_RED), true);
            }
        } else if (Keys.K.isDown()) {
            player.displayClientMessage(Component.translatable("PreReloadPauseTicks: " + gunTmp.getReloads().getPreReloadPauseTicks()), true);
            if (isUp) {
                this.preReloadPauseTicksMod += 0.5;
                player.displayClientMessage(Component.translatable("PreReloadPauseTicks: " + gunTmp.getReloads().getPreReloadPauseTicks()).withStyle(ChatFormatting.GREEN), true);
            } else if (isDown) {
                this.preReloadPauseTicksMod -= 0.5;
                player.displayClientMessage(Component.translatable("PreReloadPauseTicks: " + gunTmp.getReloads().getPreReloadPauseTicks()).withStyle(ChatFormatting.DARK_RED), true);
            }
        } else if (Keys.M.isDown()) {
            player.displayClientMessage(Component.translatable("InterReloadPauseTicks: " + gunTmp.getReloads().getinterReloadPauseTicks()), true);
            if (isUp) {
                this.interReloadPauseTicksMod += 0.5;
                player.displayClientMessage(Component.translatable("InterReloadPauseTicks: " + gunTmp.getReloads().getinterReloadPauseTicks()).withStyle(ChatFormatting.GREEN), true);
            } else if (isDown) {
                this.interReloadPauseTicksMod -= 0.5;
                player.displayClientMessage(Component.translatable("InterReloadPauseTicks: " + gunTmp.getReloads().getinterReloadPauseTicks()).withStyle(ChatFormatting.DARK_RED), true);
            }
        }
        CompoundTag gun = getMapItem(gunItem.getDescriptionId(), gunItem.getGun()).serializeNBT(); // Copy to ensure we are grabbing a copy of this data. new CompoundNBT();//
        gun.getCompound("Reloads").remove("ReloadSpeed");
        gun.getCompound("Reloads").remove("ReloadMagTimer");
        gun.getCompound("Reloads").remove("AdditionalReloadEmptyMagTimer");
        gun.getCompound("Reloads").remove("ReloadPauseTicks");
        gun.getCompound("Reloads").remove("InterReloadPauseTicks");
        gun.getCompound("Reloads").putDouble("ReloadSpeed", gunItem.getGun().getReloads().getReloadAmount());
        gun.getCompound("Reloads").putDouble("ReloadMagTimer", gunItem.getGun().getReloads().getReloadMagTimer());
        gun.getCompound("Reloads").putDouble("AdditionalReloadEmptyMagTimer", gunItem.getGun().getReloads().getAdditionalReloadEmptyMagTimer());
        gun.getCompound("Reloads").putDouble("ReloadPauseTicks", gunItem.getGun().getReloads().getPreReloadPauseTicks());
        gun.getCompound("Reloads").putDouble("InterReloadPauseTicks", gunItem.getGun().getReloads().getinterReloadPauseTicks());
        /*gunItem.getGun().getGeneral().deserializeNBT(gun);
        this.map.put(gunItem.getTranslationKey(), gunItem.getGun());*/
        this.getMapItem(gunItem.getDescriptionId(), gunItem.getGun()).deserializeNBT(gun);
    }

    private void handleZoomMod(InputEvent.Key event, TimelessGunItem gunItem) {
        this.handlePositionedMod(event, gunItem);
    }

    private void handleFlashMod(InputEvent.Key event, TimelessGunItem gunItem) {
        this.handleScaledPositionedMod(event, gunItem);
    }

    private void handleScopeMod(InputEvent.Key event, TimelessGunItem gunItem) {
        this.handleScaledPositionedMod(event, gunItem);
    }

    private void handleBarrelMod(InputEvent.Key event, TimelessGunItem gunItem) {
        this.handleScaledPositionedMod(event, gunItem);
    }

    private void handleOldScopeMod(InputEvent.Key event, TimelessGunItem gunItem) {
        this.handleScaledPositionedMod(event, gunItem);
    }

    private void handlePistolScopeMod(InputEvent.Key event, TimelessGunItem gunItem) {
        this.handleScaledPositionedMod(event, gunItem);
    }

    private void handlePistolBarrelMod(InputEvent.Key event, TimelessGunItem gunItem) { // "zoom" extends positioned
        this.handleScaledPositionedMod(event, gunItem);
    }

    public double getxMod() {
        return this.xMod;
    }

    public double getyMod() {
        return this.yMod;
    }

    public double getzMod() {
        return this.zMod;
    }

    private void handlePositionedMod(InputEvent.Key event, TimelessGunItem gunItem) {
        double stepModifier = 1;
        boolean isLeft = Keys.LEFT.isDown();
        boolean isRight = Keys.RIGHT.isDown();
        boolean isUp = Keys.UP.isDown();
        boolean isDown = Keys.DOWN.isDown();
        //boolean isControlDown = Keys.CONTROLLY.isDown() || Keys.CONTROLLYR.isDown(); // Increase Module Size
        //boolean isShiftDown = Keys.SHIFTY.isDown() || Keys.SHIFTYR.isDown(); // Increase Step Size
        //boolean isAltDown = Keys.ALTY.isDown() || Keys.ALTYR.isDown(); // Swap X -> Z modify


        controlToggle = Keys.CONTROLLY.isDown() || Keys.CONTROLLYR.isDown();
        altToggle = Keys.ALTY.isDown() || Keys.ALTYR.isDown();

        if (controlToggle)
            stepModifier /= 10;

        if (isLeft)
            this.xMod -= 0.1 * stepModifier;
        else if (isRight)
            this.xMod += 0.1 * stepModifier;
        else if (isUp && altToggle)
            this.zMod -= 0.1 * stepModifier; // Forward
        else if (isDown && altToggle)
            this.zMod += 0.1 * stepModifier; // Backward
        else if (isUp)
            this.yMod += 0.1 * stepModifier;
        else if (isDown)
            this.yMod -= 0.1 * stepModifier;

        CompoundTag gun = this.getMapItem(gunItem.getDescriptionId(), gunItem.getGun()).serializeNBT(); // Copy to ensure we are grabbing a copy of this data. new CompoundNBT();//
        if (this.mode == TaCWeaponDevModes.flash) // Flash was apparently brought out of Display, I don't wish to break every gun at this point in time.
        {
            gun.getCompound("Display").getCompound(this.mode.tagName).remove("XOffset");
            gun.getCompound("Display").getCompound(this.mode.tagName).remove("YOffset");
            gun.getCompound("Display").getCompound(this.mode.tagName).remove("ZOffset");
            gun.getCompound("Display").getCompound(this.mode.tagName).putDouble("XOffset", this.casedGetX());
            gun.getCompound("Display").getCompound(this.mode.tagName).putDouble("YOffset", this.casedGetY());
            gun.getCompound("Display").getCompound(this.mode.tagName).putDouble("ZOffset", this.casedGetZ());
        }
        if (this.mode == TaCWeaponDevModes.zoom) // Flash was apparently brought out of Display, I don't wish to break every gun at this point in time.
        {
            gun.getCompound("Modules").getCompound("Zoom").remove("XOffset");
            gun.getCompound("Modules").getCompound("Zoom").remove("YOffset");
            gun.getCompound("Modules").getCompound("Zoom").remove("ZOffset");
            gun.getCompound("Modules").getCompound("Zoom").putDouble("XOffset", this.casedGetX());
            gun.getCompound("Modules").getCompound("Zoom").putDouble("YOffset", this.casedGetY());
            gun.getCompound("Modules").getCompound("Zoom").putDouble("ZOffset", this.casedGetZ());
            //this.getMapItem(gunItem.getTranslationKey(), gunItem.getGun()).getModules().getAttachments().getScope().deserializeNBT(gun.getCompound("Modules").getCompound("Zoom"));
        }
        if (this.mode == TaCWeaponDevModes.scope) {
            gun.getCompound("Modules").getCompound("Attachments").getCompound("Scope").remove("XOffset");
            gun.getCompound("Modules").getCompound("Attachments").getCompound("Scope").remove("YOffset");
            gun.getCompound("Modules").getCompound("Attachments").getCompound("Scope").remove("ZOffset");
            gun.getCompound("Modules").getCompound("Attachments").getCompound("Scope").putDouble("XOffset", this.casedGetX());
            gun.getCompound("Modules").getCompound("Attachments").getCompound("Scope").putDouble("YOffset", this.casedGetY());
            gun.getCompound("Modules").getCompound("Attachments").getCompound("Scope").putDouble("ZOffset", this.casedGetZ());
            //LOGGER.log(Level.FATAL, gun.getCompound("Modules").getCompound("Attachments").getCompound("Scope").toString());
            //LOGGER.log(Level.FATAL, gun.getCompound("Modules").getCompound("Attachments").toString());
        } else {
            gun.getCompound("Modules").getCompound("Attachments").getCompound(this.mode.tagName).remove("XOffset");
            gun.getCompound("Modules").getCompound("Attachments").getCompound(this.mode.tagName).remove("YOffset");
            gun.getCompound("Modules").getCompound("Attachments").getCompound(this.mode.tagName).remove("ZOffset");
            gun.getCompound("Modules").getCompound("Attachments").getCompound(this.mode.tagName).putDouble("XOffset", this.casedGetX());
            gun.getCompound("Modules").getCompound("Attachments").getCompound(this.mode.tagName).putDouble("YOffset", this.casedGetY());
            gun.getCompound("Modules").getCompound("Attachments").getCompound(this.mode.tagName).putDouble("ZOffset", this.casedGetZ());

        }
        //gunItem.getGun().deserializeNBT(gun);
        this.getMapItem(gunItem.getDescriptionId(), gunItem.getGun()).deserializeNBT(gun);
        //LOGGER.log(Level.FATAL, this.getMapItem(gunItem.getTranslationKey(), gunItem.getGun()).serializeNBT().getCompound("Modules").getCompound("Attachments").getCompound("Scope").toString());
        //this.map.put(gunItem.getTranslationKey(), getMapItem(gunItem.getTranslationKey(), gunItem.getGun()).deserializeNBT(gun));
    }

    public double getSizeMod() {
        return this.sizeMod;
    }

    private void handleScaledPositionedMod(InputEvent.Key event, TimelessGunItem gunItem) {
        this.handlePositionedMod(event, gunItem);
        boolean isPeriodDown = Keys.SIZE_OPT.isDown(); // Increase Step Size

        if (isPeriodDown) {
            double stepModifier = 1;
            boolean isUp = Keys.UP.isDown();
            boolean isDown = Keys.DOWN.isDown();
            boolean isShiftDown = Keys.SHIFTY.isDown() || Keys.SHIFTYR.isDown(); // Increase Step Size

            if (isShiftDown)
                stepModifier *= 10;

            else if (isUp)
                this.sizeMod += 0.0075 * stepModifier;
            else if (isDown)
                this.sizeMod -= 0.0075 * stepModifier;
        }
        CompoundTag gun = getMapItem(gunItem.getDescriptionId(), gunItem.getGun()).serializeNBT(); //new CompoundNBT();//
        if (this.mode == TaCWeaponDevModes.flash) // Flash was apparently brought out of Display, I don't wish to break every gun at this point in time.
        {
            gun.getCompound("Display").getCompound(this.mode.tagName).remove("Scale");
            gun.getCompound("Display").getCompound(this.mode.tagName).putDouble("Scale", this.casedGetScale());
        } else {
            gun.getCompound("Modules").getCompound(this.mode.tagName).remove("Scale");
            gun.getCompound("Modules").getCompound(this.mode.tagName).putDouble("Scale", this.casedGetScale());
        }
        /*gunItem.getGun().deserializeNBT(gun);
        this.map.put(gunItem.getTranslationKey(), gunItem.getGun());*/
        this.getMapItem(gunItem.getDescriptionId(), gunItem.getGun()).deserializeNBT(gun);
    }

    private Gun getMapItem(String gunTagName, Gun gun) {
        if (this.map.containsKey(gunTagName))
            return this.map.get(gunTagName);
        else {
            this.map.put(gunTagName, gun.copy());
            return this.map.get(gunTagName);
        }
    }

    private double casedGetScale() {
        TimelessGunItem gunItem = (TimelessGunItem) Minecraft.getInstance().player.getMainHandItem().getItem();
        switch (this.mode) {
            case flash:
                return gunItem.getGun().getDisplay().getFlash().getSize();
            case scope:
                return gunItem.getGun().canAttachType(IAttachment.Type.SCOPE) ? gunItem.getGun().getModules().getAttachments().getScope().getScale() : 0;
            case barrel:
                return gunItem.getGun().canAttachType(IAttachment.Type.BARREL) ? gunItem.getGun().getModules().getAttachments().getBarrel().getScale() : 0;
            case oldScope:
                return gunItem.getGun().canAttachType(IAttachment.Type.OLD_SCOPE) ? gunItem.getGun().getModules().getAttachments().getOldScope().getScale() : 0;
            case pistolScope:
                return gunItem.getGun().canAttachType(IAttachment.Type.PISTOL_SCOPE) ? gunItem.getGun().getModules().getAttachments().getPistolScope().getScale() : 0;
            case pistolBarrel:
                return gunItem.getGun().canAttachType(IAttachment.Type.PISTOL_BARREL) ? gunItem.getGun().getModules().getAttachments().getPistolBarrel().getScale() : 0;
        }
        return 0;
    }

    private double casedGetX() {
        TimelessGunItem gunItem = (TimelessGunItem) Minecraft.getInstance().player.getMainHandItem().getItem();
        switch (this.mode) {
            case flash:
                return gunItem.getGun().getDisplay().getFlash().getXOffset();
            case zoom:
                return gunItem.getGun().getModules().getZoom().getXOffset();
            case scope:
                return gunItem.getGun().getModules().getAttachments().getScope() != null ? gunItem.getGun().getModules().getAttachments().getScope().getXOffset() : 0;
            case barrel:
                return gunItem.getGun().getModules().getAttachments().getBarrel() != null ? gunItem.getGun().getModules().getAttachments().getBarrel().getXOffset() : 0;
            case oldScope:
                return gunItem.getGun().getModules().getAttachments().getOldScope() != null ? gunItem.getGun().getModules().getAttachments().getOldScope().getXOffset() : 0;
            case pistolScope:
                return gunItem.getGun().getModules().getAttachments().getPistolScope() != null ? gunItem.getGun().getModules().getAttachments().getPistolScope().getXOffset() : 0;
            case pistolBarrel:
                return gunItem.getGun().getModules().getAttachments().getPistolBarrel() != null ? gunItem.getGun().getModules().getAttachments().getPistolBarrel().getXOffset() : 0;
        }
        return 0;
    }

    private double casedGetY() {
        TimelessGunItem gunItem = (TimelessGunItem) Minecraft.getInstance().player.getMainHandItem().getItem();
        switch (this.mode) {
            case flash:
                return gunItem.getGun().getDisplay().getFlash().getYOffset();
            case zoom:
                return gunItem.getGun().getModules().getZoom().getYOffset();
            case scope:
                return gunItem.getGun().getModules().getAttachments().getScope() != null ? gunItem.getGun().getModules().getAttachments().getScope().getYOffset() : 0;
            case barrel:
                return gunItem.getGun().getModules().getAttachments().getBarrel() != null ? gunItem.getGun().getModules().getAttachments().getBarrel().getYOffset() : 0;
            case oldScope:
                return gunItem.getGun().getModules().getAttachments().getOldScope() != null ? gunItem.getGun().getModules().getAttachments().getOldScope().getYOffset() : 0;
            case pistolScope:
                return gunItem.getGun().getModules().getAttachments().getPistolScope() != null ? gunItem.getGun().getModules().getAttachments().getPistolScope().getYOffset() : 0;
            case pistolBarrel:
                return gunItem.getGun().getModules().getAttachments().getPistolBarrel() != null ? gunItem.getGun().getModules().getAttachments().getPistolBarrel().getYOffset() : 0;
        }
        return 0;
    }

    private double casedGetZ() {
        TimelessGunItem gunItem = (TimelessGunItem) Minecraft.getInstance().player.getMainHandItem().getItem();
        switch (this.mode) {
            case flash:
                return gunItem.getGun().getDisplay().getFlash().getZOffset();
            case zoom:
                return gunItem.getGun().getModules().getZoom().getZOffset();
            case scope:
                return gunItem.getGun().getModules().getAttachments().getScope() != null ? gunItem.getGun().getModules().getAttachments().getScope().getZOffset() : 0;
            case barrel:
                return gunItem.getGun().getModules().getAttachments().getBarrel() != null ? gunItem.getGun().getModules().getAttachments().getBarrel().getZOffset() : 0;
            case oldScope:
                return gunItem.getGun().getModules().getAttachments().getOldScope() != null ? gunItem.getGun().getModules().getAttachments().getOldScope().getZOffset() : 0;
            case pistolScope:
                return gunItem.getGun().getModules().getAttachments().getPistolScope() != null ? gunItem.getGun().getModules().getAttachments().getPistolScope().getZOffset() : 0;
            case pistolBarrel:
                return gunItem.getGun().getModules().getAttachments().getPistolBarrel() != null ? gunItem.getGun().getModules().getAttachments().getPistolBarrel().getZOffset() : 0;
        }
        return 0;
    }

    public void resetData() {
        switch (this.mode) {
            case general:
                this.rateMod = 0;
                this.recoilAngleMod = 0;
                this.recoilKickMod = 0;
                this.horizontalRecoilAngleMod = 0;
                this.cameraRecoilModifierMod = 0;
                this.weaponRecoilDurationMod = 0;
                this.recoilAdsReductionMod = 0;
                this.projectileAmountMod = 0;
                this.spreadMod = 0;
                this.weightKiloMod = 0;
                break;
            case reloads:
                this.reloadMagTimerMod = 0;
                this.additionalReloadEmptyMagTimerMod = 0;
                this.reloadAmountMod = 0;
                this.preReloadPauseTicksMod = 0;
                this.interReloadPauseTicksMod = 0;
                break;

            case projectile:
                this.damageMod = 0;
                this.armorIgnoreMod = 0;
                this.criticalMod = 0;
                this.criticalDamageMod = 0;
                this.headDamageMod = 0;
                this.closeDamageMod = 0;
                this.decayStartMod = 0;
                this.minDecayMultiplierMod = 0;
                this.decayEndMod = 0;
                this.sizePrjMod = 0;
                this.speedMod = 0;
                this.lifeMod = 0;
                break;

            case display:
                break;

            case flash:
                this.xMod = 0;
                this.yMod = 0;
                this.zMod = 0;
                this.sizeMod = 0;
                break;
            case zoom:
                this.xMod = 0;
                this.yMod = 0;
                this.zMod = 0;
                break;
            case scope:
                this.xMod = 0;
                this.yMod = 0;
                this.zMod = 0;
                this.sizeMod = 0;
                break;
            case barrel:
                this.xMod = 0;
                this.yMod = 0;
                this.zMod = 0;
                break;
            case oldScope:
                this.xMod = 0;
                this.yMod = 0;
                this.zMod = 0;
                this.sizeMod = 0;
                break;
            case pistolScope:
                this.xMod = 0;
                this.yMod = 0;
                this.zMod = 0;
                break;
            case pistolBarrel:
                this.xMod = 0;
                this.yMod = 0;
                this.zMod = 0;
                break;
            default:
                break;
        }
    }

    //TODO: ENSURE WE ARE EDITING "TO EXPORT DATA", I DON'T WANT TO MANAGE MULTIPLE DATA STORES AND ATTRIBUTES, HERE WE JUST ADJUST BASED ON THE GUNS MODIFIED AND EXISTING!
    private void ensureData(Gun gun, Gun toUpdate) {
        switch (this.mode) {
            case general:
                if (gun.getGeneral() != null) {
                    this.rateMod = gun.getGeneral().getRate() - toUpdate.getGeneral().getRate();
                    this.recoilAngleMod = gun.getGeneral().getRecoilAngle() - toUpdate.getGeneral().getRecoilAngle();
                    this.recoilKickMod = gun.getGeneral().getRecoilKick() - toUpdate.getGeneral().getRecoilKick();
                    this.horizontalRecoilAngleMod = gun.getGeneral().getHorizontalRecoilAngle() - toUpdate.getGeneral().getHorizontalRecoilAngle();
                    this.cameraRecoilModifierMod = gun.getGeneral().getCameraRecoilModifier() - toUpdate.getGeneral().getCameraRecoilModifier();
                    this.weaponRecoilDurationMod = gun.getGeneral().getWeaponRecoilOffset() - toUpdate.getGeneral().getWeaponRecoilOffset();
                    this.recoilDurationMod = gun.getGeneral().getRecoilDuration() - toUpdate.getGeneral().getRecoilDuration();
                    this.recoilAdsReductionMod = gun.getGeneral().getRecoilAdsReduction() - toUpdate.getGeneral().getRecoilAdsReduction();
                    this.projectileAmountMod = gun.getGeneral().getProjectileAmount() - toUpdate.getGeneral().getProjectileAmount();
                    this.spreadMod = gun.getGeneral().getSpread() - toUpdate.getGeneral().getSpread();
                    this.weightKiloMod = gun.getGeneral().getWeightKilo() - toUpdate.getGeneral().getWeightKilo();
                }
                break;
            case reloads:
                break;
            case projectile:
                break;
            case display:
                break;
            case flash:
                if (toUpdate.getDisplay().getFlash() != null && gun.getDisplay().getFlash() != null) {
                    this.xMod = gun.getDisplay().getFlash().getXOffset() - toUpdate.getDisplay().getFlash().getXOffset();
                    this.yMod = gun.getDisplay().getFlash().getYOffset() - toUpdate.getDisplay().getFlash().getYOffset();
                    this.zMod = gun.getDisplay().getFlash().getZOffset() - toUpdate.getDisplay().getFlash().getZOffset();
                    this.sizeMod = gun.getDisplay().getFlash().getSize() - toUpdate.getDisplay().getFlash().getSize();
                }

                break;
            case zoom:
                if (toUpdate.getModules().getZoom() != null && gun.getModules().getZoom() != null) {
                    this.xMod = gun.getModules().getZoom().getXOffset() - toUpdate.getModules().getZoom().getXOffset();
                    this.yMod = gun.getModules().getZoom().getYOffset() - toUpdate.getModules().getZoom().getYOffset();
                    this.zMod = gun.getModules().getZoom().getZOffset() - toUpdate.getModules().getZoom().getZOffset();
                }
                break;
            case scope:
                if (toUpdate.getModules().getAttachments().getScope() != null && gun.getModules().getAttachments().getScope() != null) {
                    this.xMod = gun.getModules().getAttachments().getScope().getXOffset() - toUpdate.getModules().getAttachments().getScope().getXOffset();
                    this.yMod = gun.getModules().getAttachments().getScope().getYOffset() - toUpdate.getModules().getAttachments().getScope().getYOffset();
                    this.zMod = gun.getModules().getAttachments().getScope().getZOffset() - toUpdate.getModules().getAttachments().getScope().getZOffset();
                    this.sizeMod = gun.getModules().getAttachments().getScope().getScale() - toUpdate.getModules().getAttachments().getScope().getScale();
                }
                break;
            case barrel:
                if (toUpdate.getModules().getAttachments().getBarrel() != null && gun.getModules().getAttachments().getBarrel() != null) {
                    this.xMod = gun.getModules().getAttachments().getBarrel().getXOffset() - toUpdate.getModules().getAttachments().getBarrel().getXOffset();
                    this.yMod = gun.getModules().getAttachments().getBarrel().getYOffset() - toUpdate.getModules().getAttachments().getBarrel().getYOffset();
                    this.zMod = gun.getModules().getAttachments().getBarrel().getZOffset() - toUpdate.getModules().getAttachments().getBarrel().getZOffset();
                }
                break;
            case oldScope:
                if (toUpdate.getModules().getAttachments().getOldScope() != null && gun.getModules().getAttachments().getOldScope() != null) {
                    this.xMod = gun.getModules().getAttachments().getOldScope().getXOffset() - toUpdate.getModules().getAttachments().getOldScope().getXOffset();
                    this.yMod = gun.getModules().getAttachments().getOldScope().getYOffset() - toUpdate.getModules().getAttachments().getOldScope().getYOffset();
                    this.zMod = gun.getModules().getAttachments().getOldScope().getZOffset() - toUpdate.getModules().getAttachments().getOldScope().getZOffset();
                    this.sizeMod = gun.getModules().getAttachments().getOldScope().getScale() - toUpdate.getModules().getAttachments().getOldScope().getScale();
                }
                break;
            case pistolScope:
                if (toUpdate.getModules().getAttachments().getPistolScope() != null && gun.getModules().getAttachments().getPistolScope() != null) {
                    this.xMod = gun.getModules().getAttachments().getPistolScope().getXOffset() - toUpdate.getModules().getAttachments().getPistolScope().getXOffset();
                    this.yMod = gun.getModules().getAttachments().getPistolScope().getYOffset() - toUpdate.getModules().getAttachments().getPistolScope().getYOffset();
                    this.zMod = gun.getModules().getAttachments().getPistolScope().getZOffset() - toUpdate.getModules().getAttachments().getPistolScope().getZOffset();
                    this.sizeMod = gun.getModules().getAttachments().getPistolScope().getScale() - toUpdate.getModules().getAttachments().getPistolScope().getScale();
                }
                break;
            case pistolBarrel:
                if (toUpdate.getModules().getAttachments().getPistolBarrel() != null && gun.getModules().getAttachments().getPistolBarrel() != null) {
                    this.xMod = gun.getModules().getAttachments().getPistolBarrel().getXOffset() - toUpdate.getModules().getAttachments().getPistolBarrel().getXOffset();
                    this.yMod = gun.getModules().getAttachments().getPistolBarrel().getYOffset() - toUpdate.getModules().getAttachments().getPistolBarrel().getYOffset();
                    this.zMod = gun.getModules().getAttachments().getPistolBarrel().getZOffset() - toUpdate.getModules().getAttachments().getPistolBarrel().getZOffset();
                }
                break;
            default:
                break;
        }
    }

    public void exportData() {
        this.map.forEach((name, gun) ->
        {
            if (this.map.get(name) == null) {
                LOGGER.log(Level.ERROR, "WEAPON EDITOR FAILED TO EXPORT THIS BROKEN DATA. CONTACT CLUMSYALIEN.");
                return;
            }
            LOGGER.log(Level.FATAL, gun.serializeNBT().getCompound("Modules").getCompound("Attachments").toString());
            GsonBuilder gsonB = new GsonBuilder().setLenient().addSerializationExclusionStrategy(Gun.strategy).setPrettyPrinting();//.setNumberToNumberStrategy(ToNumberPolicy.DOUBLE).setObjectToNumberStrategy(ToNumberPolicy.DOUBLE).serializeSpecialFloatingPointValues();;

            String jsonString = gsonB.create().toJson(gun);//gson.toJson(ch.getCatGlobal(1).get(this.previousWeaponTag));
            jsonString += "\nTRAIL_ADJUST___" + gsonB.create().toJson(this.sizeMod);
            jsonString += "\nSCOPE" + gsonB.create().toJson(gun.serializeNBT().getCompound("Modules").getCompound("Attachments").getCompound("Scope").toString());
            jsonString += "\nBARREL" + gsonB.create().toJson(gun.serializeNBT().getCompound("Modules").getCompound("Attachments").getCompound("Barrel").toString());
            jsonString += "\nOLD_SCOPE" + gsonB.create().toJson(gun.serializeNBT().getCompound("Modules").getCompound("Attachments").getCompound("OldScope").toString());
            jsonString += "\nPISTOL_SCOPE" + gsonB.create().toJson(gun.serializeNBT().getCompound("Modules").getCompound("Attachments").getCompound("PistolScope").toString());
            jsonString += "\nPISTOL_BARREL" + gsonB.create().toJson(gun.serializeNBT().getCompound("Modules").getCompound("Attachments").getCompound("PistolBarrel").toString());
            this.writeExport(jsonString, name);
        });
    }

    private void writeExport(String jsonString, String name) {
        try {
            File dir = new File(Config.COMMON.development.TDevPath.get() + "\\tac_export\\");
            dir.mkdir();
            FileWriter dataWriter = new FileWriter(dir.getAbsolutePath() + "\\" + name + "_export.json");
            dataWriter.write(jsonString);
            dataWriter.close();
            LOGGER.log(Level.INFO, "WEAPON EDITOR EXPORTED FILE ( " + name + "_export.txt ). BE PROUD!");
        } catch (IOException e) {
            LOGGER.log(Level.ERROR, "WEAPON EDITOR FAILED TO EXPORT, NO FILE CREATED!!! NO ACCESS IN PATH?. CONTACT CLUMSYALIEN.");
        }
    }

    // This is built manually, ensuring that I have both tested and built the new system around any new category.
    public enum TaCWeaponDevModes {
        general("General"),
        reloads("Reloads"),
        projectile("Projectile"),
        /*SOUNDS(""),*/
        display("Display"),
        flash("Flash"),
        zoom("Zoom"),
        scope("Scope"),
        barrel("Barrel"),
        oldScope("OldScope"),
        pistolScope("PistolScope"),
        pistolBarrel("PistolBarrel");

        private String tagName;

        TaCWeaponDevModes(String name) {
            this.tagName = name;
        }

        public String getTagName() {
            return tagName;
        }
    }
}
