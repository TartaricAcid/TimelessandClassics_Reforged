package com.tac.guns.client.handler;

import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import static java.lang.Math.PI;

public class ScopeJitterHandler {
    private static ScopeJitterHandler instance;
    private final int component = 85;
    /*private final double duration = 5.5;*/
    private final double duration = 5.5;
    private boolean isStabilizing = false;
    private boolean forceBreathing = false;
    private Long radix = 0L;
    private int respiratoryQuotient = 0;
    //private final int component = 85;
    private double xTarget = Math.random() * 2 - 1;
    private int ticksBeforeBreathing = 500;

    private ScopeJitterHandler() {
    }

    public static ScopeJitterHandler getInstance() {
        return instance == null ? instance = new ScopeJitterHandler() : instance;
    }

    public boolean isStabilizing() {
        return isStabilizing;
    }

    public void setStablizing(boolean value) {
        isStabilizing = value;
    }

    public boolean isForceBreathing() {
        return forceBreathing;
    }

    public void setForceBreathing(boolean value) {
        forceBreathing = value;
    }

    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent event) {
        /*if(!SyncedPlayerData.instance().get(event.player, ModSyncedDataKeys.AIMING))
            ScopeJitterHandler.getInstance().resetBreathingTickBuffer();*/
        tick();
    }

    public void tick() {
        if (ticksBeforeBreathing > 0) {
            ticksBeforeBreathing -= 5;
        } else {
            double suffocationPoint = 120.0;
            if (!isStabilizing() || forceBreathing) {
                radix += respiratoryQuotient / (int) (suffocationPoint * component) * 2 + 1;
                if (radix % (long) (duration * component / 2) <= 2) xTarget = Math.random() * 2 - 1;
                if (respiratoryQuotient > 0) respiratoryQuotient -= 5;
                if (respiratoryQuotient <= 0) forceBreathing = false;
            } else {
                respiratoryQuotient++;
                if (respiratoryQuotient >= suffocationPoint * component) forceBreathing = true;
            }
        }
    }

    public double getYOffsetRatio() {
        double degree = ((radix % (long) (duration * component)) / (duration * component)) * 2 * PI;
        return Math.sin(degree);
    }

    public double getXOffsetRatio() {
        double degree = ((radix % (long) (duration * component / 2)) / (duration * component / 2)) * PI;
        return Math.sin(degree) * xTarget;
    }

    public class BreathAimTracker {
        private double currentBreath;
        private double previousBreath;

        public void resetBreathingTickBuffer() {
            /*this.
            if(this.getInstance().ticksBeforeBreathing < 600)
                this.getInstance().ticksBeforeBreathing += 50;*/
        }

    }

}