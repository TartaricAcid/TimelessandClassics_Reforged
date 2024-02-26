package com.tac.guns.client.handler;

import com.tac.guns.item.transition.TimelessGunItem;
import com.tac.guns.network.PacketHandler;
import com.tac.guns.network.message.MessageUpdatePlayerMovement;
import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.event.ComputeFovModifierEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class MovementAdaptationsHandler {
    private static MovementAdaptationsHandler instance;
    private boolean readyToUpdate = false;
    private boolean readyToReset = true;
    private float speed = 0.0F;
    private float previousWeight = 0.0F;
    private float movement = 0.0F;

    private MovementAdaptationsHandler() {
    }

    public static MovementAdaptationsHandler get() {
        return instance == null ? instance = new MovementAdaptationsHandler() : instance;
    }

    public boolean isReadyToUpdate() {
        return readyToUpdate;
    }

    public void setReadyToUpdate(boolean readyToUpdate) {
        this.readyToUpdate = readyToUpdate;
    }

    public boolean isReadyToReset() {
        return readyToReset;
    }

    public void setReadyToReset(boolean readyToReset) {
        this.readyToReset = readyToReset;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public float getPreviousWeight() {
        return previousWeight;
    }

    //private Byte previousGun;

    public void setPreviousWeight(float previousWeight) {
        this.previousWeight = previousWeight;
    }

    public float getMovement() {
        return movement;
    }

    @SubscribeEvent(priority = EventPriority.LOW)
    public void onFovUpdate(ComputeFovModifierEvent event) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player != null && !mc.player.getMainHandItem().isEmpty() && mc.options.getCameraType() == CameraType.FIRST_PERSON && mc.options.fovEffectScale().get() > 0) {
            ItemStack heldItem = mc.player.getMainHandItem();
            if (heldItem.getItem() instanceof TimelessGunItem) {
                if (event.getPlayer().isSprinting())
                    event.setNewFovModifier(1.135f);
                else
                    event.setNewFovModifier(1f);
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onJump(LivingEvent.LivingJumpEvent event) {
        if (!(event.getEntity().getMainHandItem().getItem() instanceof TimelessGunItem))
            return;
        Vec3 deltaMovement = event.getEntity().getDeltaMovement();
        if (speed < 0.0875f)
            event.getEntity().setDeltaMovement(deltaMovement.x() / 2.25, deltaMovement.y() / 1.125, deltaMovement.z() / 2.25);
        else if (speed < 0.9f)
            event.getEntity().setDeltaMovement(deltaMovement.x() / 1.75, deltaMovement.y(), deltaMovement.z() / 1.75);
        else if (speed < 0.95f)
            event.getEntity().setDeltaMovement(deltaMovement.x() / 1.25, deltaMovement.y(), deltaMovement.z() / 1.25);
    }

    @SubscribeEvent//(priority = EventPriority.HIGH)
    public void movementUpdate(TickEvent.ClientTickEvent event) {
        if (Minecraft.getInstance().player == null) {
            return;
        }
        PacketHandler.getPlayChannel().sendToServer(new MessageUpdatePlayerMovement());
    }

}