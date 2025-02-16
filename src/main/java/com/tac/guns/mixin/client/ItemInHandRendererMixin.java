package com.tac.guns.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.tac.guns.client.animation.module.AnimationMeta;
import com.tac.guns.client.animation.module.GunAnimationController;
import com.tac.guns.client.event.BeforeRenderHandEvent;
import com.tac.guns.network.CommonStateBox;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemInHandRenderer.class)
public class ItemInHandRendererMixin {
    @Shadow
    private ItemStack mainHandItem;
    private ItemStack prevItemStack = ItemStack.EMPTY;
    private int prevSlot = 0;
    @Shadow
    private float mainHandHeight;
    @Shadow
    private float oMainHandHeight;

    @Inject(method = "tick",at = @At("HEAD"))
    public void applyDrawAndHolster(CallbackInfo ci){
        if(Minecraft.getInstance().player == null) return;
        ItemStack mainHandItemStack = Minecraft.getInstance().player.getMainHandItem();
        GunAnimationController controller = GunAnimationController.fromItem(mainHandItemStack.getItem());
        GunAnimationController controller1 = GunAnimationController.fromItem(this.prevItemStack.getItem());
        if(prevItemStack.sameItem(mainHandItemStack)
                && (prevSlot == Minecraft.getInstance().player.getInventory().selected && !CommonStateBox.isSwapped ) )
            return;
        prevItemStack = mainHandItemStack;
        prevSlot = Minecraft.getInstance().player.getInventory().selected;
        CommonStateBox.isSwapped = false;
        //if(isSameWeapon(Minecraft.getInstance().player)) return;
        if(controller1 != null) {
            controller1.stopAnimation();
        }
        if(controller != null && controller == controller1){
            //Stop the previous item's animation
            AnimationMeta meta = controller.getAnimationFromLabel(GunAnimationController.AnimationLabel.DRAW);
            if(!controller.getPreviousAnimation().equals(meta)) controller.stopAnimation();
            controller.runAnimation(GunAnimationController.AnimationLabel.DRAW);
        }else if(controller != null && controller.getAnimationFromLabel(GunAnimationController.AnimationLabel.DRAW) != null) {
            this.mainHandItem = mainHandItemStack;
            controller.runAnimation(GunAnimationController.AnimationLabel.DRAW);
        }
    }

    @Inject(method = "tick",at = @At("RETURN"))
    public void cancelEquippedProgress(CallbackInfo ci){
        if(Minecraft.getInstance().player == null) return;
        ItemStack mainHandItemStack = Minecraft.getInstance().player.getMainHandItem();
        GunAnimationController controller = GunAnimationController.fromItem(mainHandItemStack.getItem());
        if(controller == null ) return;
        mainHandHeight = 1.0f;
        oMainHandHeight = 1.0f;
    }

    @Inject(method = "renderHandsWithItems", at = @At("HEAD"))
    public void beforeHandRender(float p_109315_, PoseStack p_109316_, MultiBufferSource.BufferSource p_109317_, LocalPlayer p_109318_, int p_109319_, CallbackInfo ci){
        MinecraftForge.EVENT_BUS.post(new BeforeRenderHandEvent(p_109316_));
    }
}
