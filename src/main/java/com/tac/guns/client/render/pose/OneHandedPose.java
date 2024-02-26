package com.tac.guns.client.render.pose;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import com.tac.guns.client.render.IHeldAnimation;
import com.tac.guns.client.util.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * Author: Forked from MrCrayfish, continued by Timeless devs
 */
public class OneHandedPose implements IHeldAnimation {
    @Override
    @OnlyIn(Dist.CLIENT)
    public void applyPlayerModelRotation(Player player, PlayerModel model, InteractionHand hand, float aimProgress) {
        boolean right = Minecraft.getInstance().options.mainHand == HumanoidArm.RIGHT ? hand == InteractionHand.MAIN_HAND : hand == InteractionHand.OFF_HAND;
        ModelPart arm = right ? model.rightArm : model.leftArm;
        IHeldAnimation.copyModelAngles(model.head, arm);
        arm.xRot += Math.toRadians(-90F);
    }

    @Override
    public void renderFirstPersonArms(LocalPlayer player, HumanoidArm hand, ItemStack stack, PoseStack matrixStack, MultiBufferSource buffer, int light, float partialTicks) {
        matrixStack.translate(0, 0, -1);
        matrixStack.mulPose(Vector3f.YP.rotationDegrees(180F));

        double centerOffset = 2.5;
        if (Minecraft.getInstance().player.getModelName().equals("slim")) {
            centerOffset += hand == HumanoidArm.RIGHT ? 0.2 : 0.8;
        }
        centerOffset = hand == HumanoidArm.RIGHT ? -centerOffset : centerOffset;
        matrixStack.translate(centerOffset * 0.0625, -0.45, -1.0);

        matrixStack.mulPose(Vector3f.XP.rotationDegrees(75F));
        matrixStack.scale(0.5F, 0.5F, 0.5F);

        RenderUtil.renderFirstPersonArm(player, hand, matrixStack, buffer, light);
    }

    @Override
    public boolean applyOffhandTransforms(Player player, PlayerModel model, ItemStack stack, PoseStack matrixStack, float partialTicks) {
        matrixStack.mulPose(Vector3f.YP.rotationDegrees(180F));
        matrixStack.mulPose(Vector3f.ZP.rotationDegrees(180F));

        if (player.isCrouching()) {
            matrixStack.translate(-4.5 * 0.0625, -15 * 0.0625, -4 * 0.0625);
        } else if (!player.getItemBySlot(EquipmentSlot.LEGS).isEmpty()) {
            matrixStack.translate(-4.0 * 0.0625, -13 * 0.0625, 1 * 0.0625);
        } else {
            matrixStack.translate(-3.5 * 0.0625, -13 * 0.0625, 1 * 0.0625);
        }

        matrixStack.mulPose(Vector3f.YP.rotationDegrees(90F));
        matrixStack.mulPose(Vector3f.ZP.rotationDegrees(75F));
        matrixStack.mulPose(Vector3f.ZP.rotationDegrees((float) (Math.toDegrees(model.rightLeg.xRot) / 10F)));
        matrixStack.scale(0.5F, 0.5F, 0.5F);

        return true;
    }

    @Override
    public boolean canApplySprintingAnimation() {
        return true;
    }

    @Override
    public boolean canRenderOffhandItem() {
        return true;
    }
}
