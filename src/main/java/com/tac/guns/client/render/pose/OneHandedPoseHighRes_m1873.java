package com.tac.guns.client.render.pose;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import com.tac.guns.client.util.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.item.ItemStack;

/**
 * Author: ClumsyAlien, codebase and design based off Mr.Crayfish's class concept
 */

/*
    Here is a more complicated piece of the mod, the custom Pose

    Much of this entire file is from the Original One Handed Pose from the CGM mod, and is only slightly modified in some very specific values, the one comment is the only part I have changed
*/
public class OneHandedPoseHighRes_m1873 extends OneHandedPose {
    @Override
    public void renderFirstPersonArms(LocalPlayer player, HumanoidArm hand, ItemStack stack, PoseStack matrixStack, MultiBufferSource buffer, int light, float partialTicks) {
        /*
            For changing the single hand Pose animation ( Where will the hand be rendered, used to display the hand / hands on screen in a certain location, matching the current held gun)
            We would need to change the location of our hand by adjusting the values of [x,y,z](1), changing the final rotation of the rendered hand(2), and changing the scale of the rendered hand (3).
        */

        matrixStack.translate(-0.151, -0.195, 0.8525);
        matrixStack.mulPose(Vector3f.YP.rotationDegrees(180F));

        double centerOffset = 2.5;
        if (Minecraft.getInstance().player.getModelName().equals("slim")) {
            centerOffset += hand == HumanoidArm.RIGHT ? 0.2 : 0.8;
        }

        centerOffset = hand == HumanoidArm.RIGHT ? -centerOffset : centerOffset;
        if (Minecraft.getInstance().player.getModelName().equals("slim")) {
            matrixStack.translate(centerOffset * 0.0755, -0.45, 1.0); // (1)^
        } else {
            matrixStack.translate(centerOffset * 0.0625, -0.45, 1.0); // (1)^
        }
        matrixStack.mulPose(Vector3f.XP.rotationDegrees(95F)); // (2)^
        matrixStack.scale(1.4F, 1.4F, 1.7F); // (3)^

        RenderUtil.renderFirstPersonArm(player, hand, matrixStack, buffer, light); // Finally render our hand with the params we've set
    }
}
