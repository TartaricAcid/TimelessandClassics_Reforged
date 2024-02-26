package com.tac.guns.common;

import com.tac.guns.Reference;
import com.tac.guns.event.GunFireEvent;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Map;
import java.util.WeakHashMap;

@Mod.EventBusSubscriber(modid = Reference.MOD_ID)
public class ShootTracker {
    private static final Map<Player, ShootTracker> SHOOT_TRACKER_MAP = new WeakHashMap<>();
    private boolean isShooting = false;
    private boolean isTicked = false;

    public static ShootTracker getShootTracker(Player player) {
        return SHOOT_TRACKER_MAP.computeIfAbsent(player, player1 -> new ShootTracker());
    }

    @SubscribeEvent
    public static void onGunFire(GunFireEvent.Post event) {
        if (event.isClient()) return;
        ShootTracker tracker = getShootTracker(event.getEntity());
        tracker.isShooting = true;
        tracker.isTicked = false;
    }

    @SubscribeEvent
    public static void onServerTick(TickEvent.ServerTickEvent event) {
        SHOOT_TRACKER_MAP.values().forEach(tracker -> {
            if (tracker.isTicked) tracker.isShooting = false;
            else tracker.isTicked = true;
        });
    }

    public boolean isShooting() {
        return isShooting;
    }
}
