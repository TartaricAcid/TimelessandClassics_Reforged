package com.tac.guns.client.handler;

import com.mrcrayfish.framework.entity.sync.SyncedEntityData;
import com.tac.guns.client.Keys;
import com.tac.guns.client.render.crosshair.Crosshair;
import com.tac.guns.common.Rig;
import com.tac.guns.init.ModSyncedDataKeys;
import com.tac.guns.item.transition.wearables.ArmorRigItem;
import com.tac.guns.network.PacketHandler;
import com.tac.guns.network.message.MessageArmorRepair;
import com.tac.guns.network.message.MessageArmorUpdate;
import com.tac.guns.util.WearableHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * Author: Forked from MrCrayfish, continued by Timeless devs
 */
public class ArmorInteractionHandler {
    private static final double MAX_AIM_PROGRESS = 4;
    private static ArmorInteractionHandler instance;
    // TODO: Only commented, since we may need to track players per client for future third person animation ... private final Map<PlayerEntity, AimTracker> aimingMap = new WeakHashMap<>();
    private double normalisedRepairProgress;
    private boolean repairing = false;
    private int repairTime = -1;
    private int prevRepairTime = 0;

    private ArmorInteractionHandler() {
        Keys.ARMOR_REPAIRING.addPressCallback(() -> {
            if (!Keys.noConflict(Keys.ARMOR_REPAIRING))
                return;

            final Minecraft mc = Minecraft.getInstance();
            if (mc.player != null && !WearableHelper.PlayerWornRig(mc.player).isEmpty() && !WearableHelper.isFullDurability(WearableHelper.PlayerWornRig(mc.player))) {
                this.repairing = true;
                this.repairTime = ((ArmorRigItem) WearableHelper.PlayerWornRig(mc.player).getItem()).getRig().getRepair().getTicksToRepair();// Replace with enchantment checker
            }
        });
    }

    public static ArmorInteractionHandler get() {
        if (instance == null) {
            instance = new ArmorInteractionHandler();
        }
        return instance;
    }

    public float getRepairProgress(float partialTicks, Player player) {
        return this.repairTime != 0 ? ((this.prevRepairTime + ((this.repairTime - this.prevRepairTime) * partialTicks)) / (float) ((ArmorRigItem) WearableHelper.PlayerWornRig(player).getItem()).getRig().getRepair().getTicksToRepair()) : 1F;
    }

    /*public float getRepairProgress(float partialTicks, ItemStack stack) {
        return this.repairTime != 0 ? (this.repairTime - this.prevRepairTime) * partialTicks : 1F;
    }*/

    /*@SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent event)
    {
        if(event.phase != TickEvent.Phase.START)
            return;
        *//*if(!this.aiming)
            ScopeJitterHandler.getInstance().resetBreathingTickBuffer();*//*
        PlayerEntity player = event.player;
        AimTracker tracker = getAimTracker(player);
        if(tracker != null) {
            tracker.handleAiming(player, player.getHeldItem(Hand.MAIN_HAND));
            if (!tracker.isAiming()) {
                this.aimingMap.remove(player);
            }
        }
        if (this.repairing)
            player.setSprinting(false);
    }*/

    /*@Nullable
    private AimTracker getAimTracker(PlayerEntity player)
    {
        if(SyncedPlayerData.instance().get(player, ModSyncedDataKeys.QREPAIRING))
        {
            this.aimingMap.put(player, new AimTracker());
        }
    }*/

    /*public float getAimProgress(PlayerEntity player, float partialTicks)
    {
        if(player.isUser())
        {
            return (float) this.localTracker.getNormalProgress(partialTicks);
        }

        AimTracker tracker = this.getAimTracker(player);
        if(tracker != null)
        {
            return (float) tracker.getNormalProgress(partialTicks);
        }
        return 0F;
    }*/

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.START)
            return;

        Player player = Minecraft.getInstance().player;
        if (player == null)
            return;

        PacketHandler.getPlayChannel().sendToServer(new MessageArmorUpdate());
        this.prevRepairTime = this.repairTime;
        if (Keys.ARMOR_REPAIRING.isDown() && this.repairTime > 0)
            this.repairTime--;
        else if (this.repairTime == 0) {
            PacketHandler.getPlayChannel().sendToServer(new MessageArmorRepair(true, true));
            this.repairTime = -1;
            return;
        }

        if (Keys.AIM_HOLD.isDown()) {
            if (!this.repairing) {
                SyncedEntityData.instance().set(player, ModSyncedDataKeys.QREPAIRING, true);
                PacketHandler.getPlayChannel().sendToServer(new MessageArmorRepair(true, false));
                this.repairing = true;
            }
        } else if (this.repairing && !Keys.AIM_HOLD.isDown()) {
            SyncedEntityData.instance().set(player, ModSyncedDataKeys.QREPAIRING, false);
            PacketHandler.getPlayChannel().sendToServer(new MessageArmorRepair(false, false));
            this.repairing = false;
        }
    }

    /**
     * I think was supposed to be used to replace current crosshair with a repair crosshair, disable for now
     */
    //@SubscribeEvent(receiveCanceled = true)
    public void onRenderOverlay(RenderGuiOverlayEvent.Pre event) {
        //this.normalisedRepairProgress = this.localTracker.getNormalProgress(event.getPartialTicks());
        Crosshair crosshair = CrosshairHandler.get().getCurrentCrosshair();
        if (this.repairing && event.getOverlay().id().equals(VanillaGuiOverlay.CROSSHAIR.id()) && (crosshair == null || crosshair.isDefault())) {
            event.setCanceled(true);
        }
    }

    public boolean isRepairing() {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null)
            return false;

        if (mc.player.isSpectator())
            return false;

        if (mc.screen != null)
            return false;

        if (WearableHelper.PlayerWornRig(mc.player).isEmpty())
            return false;
        Rig rig = ((ArmorRigItem) WearableHelper.PlayerWornRig(mc.player).getItem()).getRig();
        return this.repairTime != 0 && rig.getRepair().getItem().equals(ForgeRegistries.ITEMS.getKey(mc.player.getMainHandItem().getItem())) && !WearableHelper.isFullDurability(WearableHelper.PlayerWornRig(mc.player));
    }

    public double getNormalisedRepairProgress() {
        return this.normalisedRepairProgress;
    }


}