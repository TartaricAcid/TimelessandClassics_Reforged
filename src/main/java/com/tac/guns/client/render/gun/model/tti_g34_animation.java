package com.tac.guns.client.render.gun.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.tac.guns.client.SpecialModels;
import com.tac.guns.client.handler.GunRenderingHandler;
import com.tac.guns.client.handler.ShootingHandler;
import com.tac.guns.client.render.animation.TtiG34AnimationController;
import com.tac.guns.client.render.animation.module.AnimationMeta;
import com.tac.guns.client.render.animation.module.GunAnimationController;
import com.tac.guns.client.render.animation.module.PlayerHandAnimation;
import com.tac.guns.client.render.gun.IOverrideModel;
import com.tac.guns.client.util.RenderUtil;
import com.tac.guns.common.Gun;
import com.tac.guns.init.ModItems;
import com.tac.guns.item.GunItem;
import com.tac.guns.item.attachment.IAttachment;
import com.tac.guns.util.GunModifierHelper;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

/**
 * Author: Timeless Development, and associates.
 */
public class tti_g34_animation implements IOverrideModel {

    @Override
    public void render(float partialTicks, ItemTransforms.TransformType transformType, ItemStack stack, ItemStack parent, LivingEntity entity, PoseStack matrices, MultiBufferSource renderBuffer, int light, int overlay) {

        //matrices.translate(0.01, 0.1, -0.1);
        //matrices.rotate(Vector3f.YP.rotationDegrees(-0.5F));

        /*
            // So this area will be tested for the item specific name, allowing the use of custom attachments
        if(Gun.getAttachment(IAttachment.Type.BARREL,stack).getItem().getName() != "")
        {
            RenderUtil.renderModel(SpecialModels.GLOCK_17_SUPPRESSOR_OVERIDE.getModel(), stack, matrices, renderBuffer, light, overlay);
        }


        PROTIP VS CODE, CTRL K CTRL F, REFORMATS THE GLTF FILE INNERS
        */

        

        TtiG34AnimationController controller = TtiG34AnimationController.getInstance();
        GunItem gunItem = ((GunItem) stack.getItem());

        matrices.pushPose();
        {
            controller.applySpecialModelTransform(SpecialModels.TTI_G34.getModel(),TtiG34AnimationController.INDEX_BODY,transformType,matrices);
            if (Gun.getAttachment(IAttachment.Type.PISTOL_BARREL, stack).getItem() == ModItems.PISTOL_SILENCER.get()) {
                RenderUtil.renderModel(SpecialModels.TTI_G34_SUPPRESSOR.getModel(), stack, matrices, renderBuffer, light, overlay);
            }
            RenderUtil.renderModel(SpecialModels.TTI_G34.getModel(), stack, matrices, renderBuffer, light, overlay);
        }
        matrices.popPose();

        matrices.pushPose();
        {
            controller.applySpecialModelTransform(SpecialModels.TTI_G34.getModel(),TtiG34AnimationController.INDEX_MAG,transformType,matrices);
            if (GunModifierHelper.getAmmoCapacity(stack) > -1) {
                RenderUtil.renderModel(SpecialModels.TTI_G34_EXTENDED_MAG.getModel(), stack, matrices, renderBuffer, light, overlay);
            } else {
                RenderUtil.renderModel(SpecialModels.TTI_G34_STANDARD_MAG.getModel(), stack, matrices, renderBuffer, light, overlay);
            }
        }
        matrices.popPose();

        // reload norm mag
        if(transformType.firstPerson() && controller.isAnimationRunning()) {
            matrices.pushPose();
            {

                controller.applySpecialModelTransform(SpecialModels.TTI_G34.getModel(), TtiG34AnimationController.INDEX_EXTRA_MAG, transformType, matrices);
                if (GunModifierHelper.getAmmoCapacity(stack) > -1) {
                    RenderUtil.renderModel(SpecialModels.TTI_G34_EXTENDED_MAG.getModel(), stack, matrices, renderBuffer, light, overlay);
                } else {
                    RenderUtil.renderModel(SpecialModels.TTI_G34_STANDARD_MAG.getModel(), stack, matrices, renderBuffer, light, overlay);
                }

            }
            matrices.popPose();
        }

        //Always push
        matrices.pushPose();
        if(transformType.firstPerson()) {
            controller.applySpecialModelTransform(SpecialModels.TTI_G34.getModel(), TtiG34AnimationController.INDEX_SLIDE, transformType, matrices);
            Gun gun = ((GunItem) stack.getItem()).getGun();
            float cooldownOg = ShootingHandler.get().getshootMsGap() / ShootingHandler.calcShootTickGap(gun.getGeneral().getRate()) < 0 ? 1 : ShootingHandler.get().getshootMsGap() / ShootingHandler.calcShootTickGap(gun.getGeneral().getRate());

            AnimationMeta reloadEmpty = controller.getAnimationFromLabel(GunAnimationController.AnimationLabel.RELOAD_EMPTY);
            boolean shouldOffset = reloadEmpty != null && reloadEmpty.equals(controller.getPreviousAnimation()) && controller.isAnimationRunning();
            if (Gun.hasAmmo(stack) || shouldOffset) {
                matrices.translate(0, 0, 0.185f * (-4.5 * Math.pow(cooldownOg - 0.5, 2) + 1.0));
                GunRenderingHandler.get().opticMovement = 0.185f * (-4.5 * Math.pow(cooldownOg - 0.5, 2) + 1.0);
            } else if (!Gun.hasAmmo(stack)) {
                matrices.translate(0, 0, 0.185f * (-4.5 * Math.pow(0.5 - 0.5, 2) + 1.0));
                GunRenderingHandler.get().opticMovement = 0.185f * (-4.5 * Math.pow(0.5 - 0.5, 2) + 1.0);
            }
        } else {
            matrices.translate(0, 0, 0.025F);
        }
        RenderUtil.renderModel(SpecialModels.TTI_G34_SLIDE.getModel(), stack, matrices, renderBuffer, light, overlay);

        //Always pop
        matrices.popPose();
        PlayerHandAnimation.render(controller,transformType,matrices,renderBuffer,light);
    }
     

    //TODO comments
}
