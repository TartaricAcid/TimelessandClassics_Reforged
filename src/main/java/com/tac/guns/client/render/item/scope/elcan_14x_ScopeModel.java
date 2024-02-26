package com.tac.guns.client.render.item.scope;

import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Matrix3f;
import com.mojang.math.Matrix4f;
import com.mojang.math.Vector3f;
import com.tac.guns.Config;
import com.tac.guns.Reference;
import com.tac.guns.client.GunRenderType;
import com.tac.guns.client.handler.AimingHandler;
import com.tac.guns.client.handler.GunRenderingHandler;
import com.tac.guns.client.handler.command.ScopeEditor;
import com.tac.guns.client.handler.command.data.ScopeData;
import com.tac.guns.client.render.item.IOverrideModel;
import com.tac.guns.client.util.RenderUtil;
import com.tac.guns.item.ScopeItem;
import com.tac.guns.item.attachment.IAttachment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

/**
 * Author: Forked from MrCrayfish, continued by Timeless devs
 */
public class elcan_14x_ScopeModel implements IOverrideModel {
    private static final ResourceLocation RED_DOT_RETICLE = new ResourceLocation(Reference.MOD_ID, "textures/items/timeless_scopes/elcan_1.png");
    private static final ResourceLocation HIT_MARKER = new ResourceLocation(Reference.MOD_ID, "textures/items/timeless_scopes/hit_marker/razor_lpvo_reticle.png");

    @Override
    public void render(float partialTicks, ItemTransforms.TransformType transformType, ItemStack stack, ItemStack parent, LivingEntity entity, PoseStack matrixStack, MultiBufferSource renderTypeBuffer, int light, int overlay) {
        matrixStack.pushPose();

        /*if (Config.CLIENT.display.redDotSquishUpdate.get() && transformType.firstPerson() && entity.equals(Minecraft.getInstance().player)) {
            double prog = 0;
            if(AimingHandler.get().getNormalisedRepairProgress() > 0.725) {
                prog = (AimingHandler.get().getNormalisedRepairProgress() - 0.725) * 3.63;
            }
            double transition = 1.0D - Math.pow(1.0D - prog, 2.0D);
            double zScale = 0.05D + 0.95D * (1.0D - transition);
            matrixStack.translate(0,0,transition*0.18);
            matrixStack.scale(1.0F, 1.0F, (float) zScale);

        }*/

        /*if (OptifineHelper.isShadersEnabled() || !Config.CLIENT.display.scopeDoubleRender.get()) {
            double transition = 1.0D - Math.pow(1.0D - AimingHandler.get().getNormalisedRepairProgress(), 2.0D);
            double zScale = 0.05D + 0.75D * (1.0D - transition);
            matrixStack.scale(1.0F, 1.0F, (float)zScale);
        }*/

        matrixStack.translate(0, 0.074, 0);

        RenderUtil.renderModel(stack, parent, matrixStack, renderTypeBuffer, light, overlay);

        matrixStack.translate(0, -0.057, 0);
        matrixStack.popPose();
        matrixStack.translate(0, 0.017, 0);
        if (transformType.firstPerson() && entity.equals(Minecraft.getInstance().player)) {
            ScopeData scopeData = ScopeEditor.get().getScopeData() == null || ScopeEditor.get().getScopeData().getTagName() != "elcan14x" ? new ScopeData("") : ScopeEditor.get().getScopeData();
            if (entity.getMainArm() == HumanoidArm.LEFT) {
                matrixStack.scale(-1, 1, 1);
            }

            ScopeItem scopeItem = (ScopeItem) stack.getItem();
            float scopePrevSize = (0.965F + 0.84F) + scopeData.getReticleSizeMod();
            float scopeSize = 1.65705F + scopeData.getDrZoomSizeMod();
            float size = scopeSize / 16.0F;
            float reticleSize = scopePrevSize / 16.0F;
            float crop = Config.CLIENT.quality.worldRerenderPiPAlpha.get() ? 0.1f : scopeItem.getProperties().getAdditionalZoom().getDrCropZoom() + scopeData.getDrZoomCropMod();//scopeItem.getProperties().getAdditionalZoom().getDrCropZoom() +
            // scopeData.getDrZoomCropMod();//0.43F
            Minecraft mc = Minecraft.getInstance();
            Window window = mc.getWindow();

            float texU = ((window.getScreenWidth() - window.getScreenHeight() + window.getScreenHeight() * crop * 2.0F) / 2.0F) / window.getScreenWidth();

            //matrixStack.rotate(Vector3f.ZP.rotationDegrees(-GunRenderingHandler.get().immersiveWeaponRoll));
            matrixStack.pushPose();
            {
                Matrix4f matrix = matrixStack.last().pose();
                Matrix3f normal = matrixStack.last().normal();

                matrixStack.translate((-size / 2) + scopeData.getDrXZoomMod(), 0.0846175 + scopeData.getDrYZoomMod(), Config.CLIENT.display.scopeDoubleRender.get() ? (3.115 + scopeData.getDrZZoomMod()) * 0.0625 : (3.115 + scopeData.getDrZZoomMod()) * 0.0625); //3.275

                float color = (float) AimingHandler.get().getNormalisedAdsProgress() * 0.8F + 0.2F;

                VertexConsumer builder;

                if (Config.CLIENT.display.scopeDoubleRender.get()) {
                    RenderSystem.enableBlend();
                    RenderSystem.defaultBlendFunc();
                    RenderSystem.setShader(GameRenderer::getPositionTexColorShader);
                    builder = renderTypeBuffer.getBuffer(GunRenderType.getScreen());
                    builder.vertex(matrix, 0, size, 0).color(color, color, color, 1.0F).uv(texU, 1.0F - crop).overlayCoords(overlay).uv2(15728880).normal(normal, 0.0F, 1.0F, 0.0F).endVertex();
                    builder.vertex(matrix, 0, 0, 0).color(color, color, color, 1.0F).uv(texU, crop).overlayCoords(overlay).uv2(15728880).normal(normal, 0.0F, 1.0F, 0.0F).endVertex();
                    builder.vertex(matrix, size, 0, 0).color(color, color, color, 1.0F).uv(1.0F - texU, crop).overlayCoords(overlay).uv2(15728880).normal(normal, 0.0F, 1.0F, 0.0F).endVertex();
                    builder.vertex(matrix, size, size, 0).color(color, color, color, 1.0F).uv(1.0F - texU, 1.0F - crop).overlayCoords(overlay).uv2(15728880).normal(normal, 0.0F, 1.0F, 0.0F).endVertex();
                }

                matrixStack.translate(0, 0, 0.0001);

                double invertProgress = (1.0 - AimingHandler.get().getNormalisedAdsProgress());
                matrixStack.translate(-0.04 * invertProgress, 0.01 * invertProgress, 0);

                double scale = 8.0;
                matrixStack.translate(size / 2, size / 2, 0);
                matrixStack.translate(-(size / scale) / 2, -(size / scale) / 2, 0);
                matrixStack.translate(0, 0, 0.0001);

                int reticleGlowColor = RenderUtil.getItemStackColor(stack, parent, IAttachment.Type.SCOPE_RETICLE_COLOR, 1);

                float red = ((reticleGlowColor >> 16) & 0xFF) / 255F;
                float green = ((reticleGlowColor >> 8) & 0xFF) / 255F;
                float blue = ((reticleGlowColor >> 0) & 0xFF) / 255F;
                float alpha = (float) AimingHandler.get().getNormalisedAdsProgress();

                matrixStack.scale(7.5f, 7.5f, 7.5f);
                matrixStack.translate(0, 0, 0.001);
                matrixStack.translate((-0.00335715 - 0.0033) + scopeData.getReticleXMod(), (-0.0035055 - 0.0046) + scopeData.getReticleYMod(), 0.0003 + scopeData.getReticleZMod());


                builder = renderTypeBuffer.getBuffer(RenderType.entityTranslucent(RED_DOT_RETICLE));
                // Walking bobbing
                boolean aimed = false;
                /* The new controlled bobbing */
                if (AimingHandler.get().isAiming())
                    aimed = true;
                alpha = (float) (1F * AimingHandler.get().getNormalisedAdsProgress());

                GunRenderingHandler.get().applyDelayedSwayTransforms(matrixStack, Minecraft.getInstance().player, partialTicks, -0.0525f);
                GunRenderingHandler.get().applyBobbingTransforms(matrixStack, true, 0.085f);
                GunRenderingHandler.get().applyNoiseMovementTransform(matrixStack, -0.11f);
                GunRenderingHandler.get().applyJumpingTransforms(matrixStack, partialTicks, -0.0625f);

                float recoilReversedMod = 0.075f;
                matrixStack.translate(0, 0, -0.35);
                matrixStack.mulPose(Vector3f.YP.rotationDegrees(GunRenderingHandler.get().newSwayYaw * recoilReversedMod));
                matrixStack.mulPose(Vector3f.ZN.rotationDegrees(GunRenderingHandler.get().newSwayPitch * recoilReversedMod));
                matrixStack.mulPose(Vector3f.XP.rotationDegrees((GunRenderingHandler.get().recoilLift * 0.02f * GunRenderingHandler.get().recoilReduction) * 0.65F));
                matrixStack.translate(0, 0, 0.35);

                int lightmapValue = 15728880;
                //alpha *= 0.6;
                builder.vertex(matrix, 0, (float) (reticleSize / scale), 0).color(red, green, blue, alpha).uv(0.0F, 0.9375F).overlayCoords(overlay).uv2(lightmapValue).normal(normal, 0.0F, 1.0F, 0.0F).endVertex();
                builder.vertex(matrix, 0, 0, 0).color(red, green, blue, alpha).uv(0.0F, 0.0F).overlayCoords(overlay).uv2(lightmapValue).normal(normal, 0.0F, 1.0F, 0.0F).endVertex();
                builder.vertex(matrix, (float) (reticleSize / scale), 0, 0).color(red, green, blue, alpha).uv(0.9375F, 0.0F).overlayCoords(overlay).uv2(lightmapValue).normal(normal, 0.0F, 1.0F, 0.0F).endVertex();
                builder.vertex(matrix, (float) (reticleSize / scale), (float) (reticleSize / scale), 0).color(red, green, blue, alpha).uv(0.9375F, 0.9375F).overlayCoords(overlay).uv2(lightmapValue).normal(normal, 0.0F, 1.0F, 0.0F).endVertex();
            }
            matrixStack.popPose();
        }
    }
}
