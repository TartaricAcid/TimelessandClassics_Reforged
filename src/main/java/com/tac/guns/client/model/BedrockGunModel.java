package com.tac.guns.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Matrix3f;
import com.mojang.math.Matrix4f;
import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;
import com.tac.guns.client.animation.AnimationController;
import com.tac.guns.client.animation.ObjectAnimationRunner;
import com.tac.guns.client.handler.AimingHandler;
import com.tac.guns.client.handler.AnimationHandler;
import com.tac.guns.client.model.bedrock.BedrockPart;
import com.tac.guns.client.model.bedrock.ModelRendererWrapper;
import com.tac.guns.client.render.item.IOverrideModel;
import com.tac.guns.client.render.item.OverrideModelManager;
import com.tac.guns.client.resource.model.bedrock.BedrockVersion;
import com.tac.guns.client.resource.model.bedrock.pojo.BedrockModelPOJO;
import com.tac.guns.client.util.RenderUtil;
import com.tac.guns.common.Gun;
import com.tac.guns.init.ModItems;
import com.tac.guns.item.GunItem;
import com.tac.guns.item.attachment.IAttachment;
import com.tac.guns.util.math.SecondOrderDynamics;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import static com.tac.guns.client.model.CommonComponents.*;

public class BedrockGunModel extends BedrockAnimatedModel implements IOverrideModel {
    protected Gun currentModifiedGun;
    protected ItemStack currentItem;
    protected ItemStack currentParent;
    protected LivingEntity currentEntity;
    protected final List<BedrockPart> ironSightPath = new ArrayList<>();
    protected final List<BedrockPart> scopePosPath = new ArrayList<>();
    public static final String IRON_VIEW_NODE = "iron_view";
    public static final String SCOPE_POS_NODE = "scope_pos";
    private final SecondOrderDynamics aimingDynamics = new SecondOrderDynamics(0.45f, 0.8f, 1.2f, 0);

    public BedrockGunModel(BedrockModelPOJO pojo, BedrockVersion version, RenderType renderType) {
        super(pojo, version, renderType);
        this.setFunctionalRenderer("LeftHand", bedrockPart -> (poseStack, transformType, consumer, light, overlay) -> {
            if (transformType.firstPerson()) {
                poseStack.mulPose(Vector3f.ZP.rotationDegrees(180f));
                Matrix3f normal = poseStack.last().normal().copy();
                Matrix4f pose = poseStack.last().pose().copy();
                //和枪械模型共用缓冲区的都需要代理到渲染结束后渲染
                this.delegateRender((poseStack1, transformType1, consumer1, light1, overlay1) -> {
                    PoseStack poseStack2 = new PoseStack();
                    poseStack2.last().normal().mul(normal);
                    poseStack2.last().pose().multiply(pose);
                    RenderUtil.renderFirstPersonArm(Minecraft.getInstance().player, HumanoidArm.LEFT, poseStack2, Minecraft.getInstance().renderBuffers().bufferSource(), light1);
                });
            }
        });
        this.setFunctionalRenderer("RightHand", bedrockPart -> (poseStack, transformType, consumer, light, overlay) -> {
            if (transformType.firstPerson()) {
                poseStack.mulPose(Vector3f.ZP.rotationDegrees(180f));
                Matrix3f normal = poseStack.last().normal().copy();
                Matrix4f pose = poseStack.last().pose().copy();
                //和枪械模型共用缓冲区的都需要代理到渲染结束后渲染
                this.delegateRender((poseStack1, transformType1, consumer1, light1, overlay1) -> {
                    PoseStack poseStack2 = new PoseStack();
                    poseStack2.last().normal().mul(normal);
                    poseStack2.last().pose().multiply(pose);
                    RenderUtil.renderFirstPersonArm(Minecraft.getInstance().player, HumanoidArm.RIGHT, poseStack2, Minecraft.getInstance().renderBuffers().bufferSource(), light1);
                });
            }
        });
        this.setFunctionalRenderer(BULLET_IN_BARREL, bedrockPart -> {
            CompoundTag tag = currentItem.getOrCreateTag();
            int ammoCount = tag.getInt("AmmoCount");
            bedrockPart.visible = ammoCount != 0;
            return null;
        });
        this.setFunctionalRenderer(BULLET_IN_MAG, bedrockPart -> {
            CompoundTag tag = currentItem.getOrCreateTag();
            int ammoCount = tag.getInt("AmmoCount");
            bedrockPart.visible = ammoCount > 1;
            return null;
        });
        this.setFunctionalRenderer(BULLET_CHAIN, bedrockPart -> {
            CompoundTag tag = currentItem.getOrCreateTag();
            int ammoCount = tag.getInt("AmmoCount");
            bedrockPart.visible = ammoCount != 0;
            return null;
        });
        this.setFunctionalRenderer(MUZZLE_BRAKE, bedrockPart -> {
            if (currentModifiedGun.canAttachType(IAttachment.Type.BARREL)) {
                ItemStack muzzle = Gun.getAttachment(IAttachment.Type.BARREL, currentItem);
                bedrockPart.visible = muzzle.getItem() == ModItems.MUZZLE_BRAKE.get();
            } else
                bedrockPart.visible = false;
            return null;
        });
        this.setFunctionalRenderer(MUZZLE_COMPENSATOR, bedrockPart -> {
            if (currentModifiedGun.canAttachType(IAttachment.Type.BARREL)) {
                ItemStack muzzle = Gun.getAttachment(IAttachment.Type.BARREL, currentItem);
                bedrockPart.visible = muzzle.getItem() == ModItems.MUZZLE_COMPENSATOR.get();
            } else
                bedrockPart.visible = false;
            return null;
        });
        this.setFunctionalRenderer(MUZZLE_SILENCER, bedrockPart -> {
            if (currentModifiedGun.canAttachType(IAttachment.Type.BARREL)) {
                ItemStack muzzle = Gun.getAttachment(IAttachment.Type.BARREL, currentItem);
                bedrockPart.visible = muzzle.getItem() == ModItems.SILENCER.get();
            } else
                bedrockPart.visible = false;
            return null;
        });
        this.setFunctionalRenderer(MUZZLE_DEFAULT, bedrockPart -> {
            if (currentModifiedGun.canAttachType(IAttachment.Type.BARREL)) {
                ItemStack muzzle = Gun.getAttachment(IAttachment.Type.BARREL, currentItem);
                bedrockPart.visible = muzzle.getItem() == ItemStack.EMPTY.getItem();
            } else
                bedrockPart.visible = true;
            return null;
        });
        this.setFunctionalRenderer(MOUNT, bedrockPart -> {
            bedrockPart.visible = currentModifiedGun.canAttachType(IAttachment.Type.SCOPE) && Gun.getScope(currentItem) != null;
            return null;
        });
        this.setFunctionalRenderer(CARRY, bedrockPart -> {
            bedrockPart.visible = currentModifiedGun.canAttachType(IAttachment.Type.SCOPE) && Gun.getScope(currentItem) == null;
            return null;
        });
        this.setFunctionalRenderer(SIGHT_FOLDED, bedrockPart -> {
            bedrockPart.visible = currentModifiedGun.canAttachType(IAttachment.Type.SCOPE) && Gun.getScope(currentItem) != null;
            return null;
        });
        this.setFunctionalRenderer(SIGHT, bedrockPart -> {
            bedrockPart.visible = currentModifiedGun.canAttachType(IAttachment.Type.SCOPE) && Gun.getScope(currentItem) == null;
            return null;
        });
        this.setFunctionalRenderer(STOCK_LIGHT, bedrockPart -> {
            if (currentModifiedGun.canAttachType(IAttachment.Type.STOCK)) {
                ItemStack stock = Gun.getAttachment(IAttachment.Type.STOCK, currentItem);
                bedrockPart.visible = stock.getItem() == ModItems.LIGHT_STOCK.get();
            } else
                bedrockPart.visible = false;
            return null;
        });
        this.setFunctionalRenderer(STOCK_TACTICAL, bedrockPart -> {
            if (currentModifiedGun.canAttachType(IAttachment.Type.STOCK)) {
                ItemStack stock = Gun.getAttachment(IAttachment.Type.STOCK, currentItem);
                bedrockPart.visible = stock.getItem() == ModItems.TACTICAL_STOCK.get();
            } else
                bedrockPart.visible = false;
            return null;
        });
        this.setFunctionalRenderer(STOCK_HEAVY, bedrockPart -> {
            if (currentModifiedGun.canAttachType(IAttachment.Type.STOCK)) {
                ItemStack stock = Gun.getAttachment(IAttachment.Type.STOCK, currentItem);
                bedrockPart.visible = stock.getItem() == ModItems.WEIGHTED_STOCK.get();
            } else
                bedrockPart.visible = false;
            return null;
        });
        this.setFunctionalRenderer(STOCK_DEFAULT, bedrockPart -> {
            if (currentModifiedGun.canAttachType(IAttachment.Type.STOCK)) {
                ItemStack stock = Gun.getAttachment(IAttachment.Type.STOCK, currentItem);
                bedrockPart.visible = stock.getItem() == ItemStack.EMPTY.getItem();
            } else
                bedrockPart.visible = true;
            return null;
        });
        this.setFunctionalRenderer(MAG_EXTENDED_1, bedrockPart -> {
            if (currentModifiedGun.canAttachType(IAttachment.Type.EXTENDED_MAG)) {
                ItemStack mag = Gun.getAttachment(IAttachment.Type.EXTENDED_MAG, currentItem);
                bedrockPart.visible = mag.getItem() == ModItems.SMALL_EXTENDED_MAG.get();
            } else
                bedrockPart.visible = false;
            return null;
        });
        this.setFunctionalRenderer(MAG_EXTENDED_2, bedrockPart -> {
            if (currentModifiedGun.canAttachType(IAttachment.Type.EXTENDED_MAG)) {
                ItemStack mag = Gun.getAttachment(IAttachment.Type.EXTENDED_MAG, currentItem);
                bedrockPart.visible = mag.getItem() == ModItems.MEDIUM_EXTENDED_MAG.get();
            } else
                bedrockPart.visible = false;
            return null;
        });
        this.setFunctionalRenderer(MAG_EXTENDED_3, bedrockPart -> {
            if (currentModifiedGun.canAttachType(IAttachment.Type.EXTENDED_MAG)) {
                ItemStack mag = Gun.getAttachment(IAttachment.Type.EXTENDED_MAG, currentItem);
                bedrockPart.visible = mag.getItem() == ModItems.LARGE_EXTENDED_MAG.get();
            } else
                bedrockPart.visible = false;
            return null;
        });
        this.setFunctionalRenderer(MAG_STANDARD, bedrockPart -> {
            if (currentModifiedGun.canAttachType(IAttachment.Type.EXTENDED_MAG)) {
                ItemStack mag = Gun.getAttachment(IAttachment.Type.EXTENDED_MAG, currentItem);
                bedrockPart.visible = mag.getItem() == ItemStack.EMPTY.getItem();
            } else
                bedrockPart.visible = true;
            return null;
        });
        this.setFunctionalRenderer(MAG_EXTENDED_1_A, bedrockPart -> {
            AnimationController controller = AnimationHandler.controllers.get(currentItem.getItem().getRegistryName());
            if (controller != null) {
                ObjectAnimationRunner runner = controller.getAnimation(AnimationHandler.MAIN_TRACK);
                if (runner != null) {
                    if (runner.getAnimation().name.contains("reload")) {
                        if (currentModifiedGun.canAttachType(IAttachment.Type.EXTENDED_MAG)) {
                            ItemStack mag = Gun.getAttachment(IAttachment.Type.EXTENDED_MAG, currentItem);
                            bedrockPart.visible = mag.getItem() == ModItems.SMALL_EXTENDED_MAG.get();
                            return null;
                        }
                    }
                }
            }
            bedrockPart.visible = false;
            return null;
        });
        this.setFunctionalRenderer(MAG_EXTENDED_2_A, bedrockPart -> {
            AnimationController controller = AnimationHandler.controllers.get(currentItem.getItem().getRegistryName());
            if (controller != null) {
                ObjectAnimationRunner runner = controller.getAnimation(AnimationHandler.MAIN_TRACK);
                if (runner != null) {
                    if (runner.getAnimation().name.contains("reload")) {
                        if (currentModifiedGun.canAttachType(IAttachment.Type.EXTENDED_MAG)) {
                            ItemStack mag = Gun.getAttachment(IAttachment.Type.EXTENDED_MAG, currentItem);
                            bedrockPart.visible = mag.getItem() == ModItems.MEDIUM_EXTENDED_MAG.get();
                            return null;
                        }
                    }
                }
            }
            bedrockPart.visible = false;
            return null;
        });
        this.setFunctionalRenderer(MAG_EXTENDED_3_A, bedrockPart -> {
            AnimationController controller = AnimationHandler.controllers.get(currentItem.getItem().getRegistryName());
            if (controller != null) {
                ObjectAnimationRunner runner = controller.getAnimation(AnimationHandler.MAIN_TRACK);
                if (runner != null) {
                    if (runner.getAnimation().name.contains("reload")) {
                        if (currentModifiedGun.canAttachType(IAttachment.Type.EXTENDED_MAG)) {
                            ItemStack mag = Gun.getAttachment(IAttachment.Type.EXTENDED_MAG, currentItem);
                            bedrockPart.visible = mag.getItem() == ModItems.LARGE_EXTENDED_MAG.get();
                            return null;
                        }
                    }
                }
            }
            bedrockPart.visible = false;
            return null;
        });
        this.setFunctionalRenderer(MAG_STANDARD_A, bedrockPart -> {
            AnimationController controller = AnimationHandler.controllers.get(currentItem.getItem().getRegistryName());
            if (controller != null) {
                ObjectAnimationRunner runner = controller.getAnimation(AnimationHandler.MAIN_TRACK);
                if (runner != null) {
                    if (runner.getAnimation().name.contains("reload")) {
                        if (currentModifiedGun.canAttachType(IAttachment.Type.EXTENDED_MAG)) {
                            ItemStack mag = Gun.getAttachment(IAttachment.Type.EXTENDED_MAG, currentItem);
                            bedrockPart.visible = mag.getItem() == ItemStack.EMPTY.getItem();
                            return null;
                        }
                    }
                }
            }
            bedrockPart.visible = false;
            return null;
        });
        {
            ModelRendererWrapper rendererWrapper = modelMap.get(IRON_VIEW_NODE);
            if(rendererWrapper != null) {
                BedrockPart it = rendererWrapper.getModelRenderer();
                Stack<BedrockPart> stack = new Stack<>();
                do {
                    stack.push(it);
                    it = it.getParent();
                } while (it != null);
                while (!stack.isEmpty()) {
                    it = stack.pop();
                    ironSightPath.add(it);
                }
            }
        }
        this.setFunctionalRenderer(IRON_VIEW_NODE, bedrockPart -> {
            bedrockPart.visible = false;
            return null;
        });
        {
            ModelRendererWrapper rendererWrapper = modelMap.get(SCOPE_POS_NODE);
            if(rendererWrapper != null) {
                BedrockPart it = rendererWrapper.getModelRenderer();
                Stack<BedrockPart> stack = new Stack<>();
                do {
                    stack.push(it);
                    it = it.getParent();
                } while (it != null);
                while (!stack.isEmpty()) {
                    it = stack.pop();
                    scopePosPath.add(it);
                }
            }
        }
        this.setFunctionalRenderer(SCOPE_POS_NODE, bedrockPart -> {
            bedrockPart.visible = false;
            return (poseStack, transformType, consumer, light, overlay) -> {
                ItemStack scopeItemStack = Gun.getAttachment(IAttachment.Type.SCOPE, currentItem);
                if (!scopeItemStack.isEmpty()) {
                    IOverrideModel scopeModel = OverrideModelManager.getModel(scopeItemStack);
                    if (scopeModel instanceof BedrockAttachmentModel bedrockScopeModel) {
                        Matrix3f normal = poseStack.last().normal().copy();
                        Matrix4f pose = poseStack.last().pose().copy();
                        //和枪械模型共用缓冲区的都需要代理到渲染结束后渲染
                        this.delegateRender((poseStack1, transformType1, consumer1, light1, overlay1) -> {
                            PoseStack poseStack2 = new PoseStack();
                            poseStack2.last().normal().mul(normal);
                            poseStack2.last().pose().multiply(pose);
                            //从bedrock model的渲染原点(0, 24, 0)移动到模型原点(0, 0, 0)
                            poseStack2.translate(0, -1.5f, 0);
                            bedrockScopeModel.render(transformType1, poseStack2, Minecraft.getInstance().renderBuffers().bufferSource(), light1, overlay1);
                        });
                    }
                }
            };
        });
    }

    @Override
    public void render(float partialTicks, ItemTransforms.TransformType transformType, ItemStack stack, ItemStack parent, LivingEntity entity, PoseStack matrixStack, MultiBufferSource buffer, int light, int overlay) {
        currentItem = stack;
        currentParent = parent;
        currentEntity = entity;

        matrixStack.pushPose();

        if (currentItem != null) {
            if (!(currentItem.getItem() instanceof GunItem))
                throw new ClassCastException("The Item type of the item stack in the formal parameter must be GunItem when render BedrockGunModel");
            currentModifiedGun = ((GunItem) currentItem.getItem()).getModifiedGun(currentItem);
        }
        if(transformType.firstPerson()) {
            //计算瞄准的进度
            float v = aimingDynamics.update(0.05f, (float) AimingHandler.get().getLerpAdsProgress(partialTicks));
            //从渲染原点(0, 24, 0)移动到模型原点(0, 0, 0)
            matrixStack.translate(0, 1.5f, 0);
            //游戏中模型是上下颠倒的，需要翻转过来。
            matrixStack.mulPose(Vector3f.ZP.rotationDegrees(180f));
            ItemStack scopeItemStack = Gun.getAttachment(IAttachment.Type.SCOPE, stack);
            if (scopeItemStack.isEmpty()) {
                //应用定位组的反向位移、旋转，使定位组的位置就是屏幕中心
                matrixStack.translate(0, 1.5f, 0);
                for (int f = ironSightPath.size() - 1; f >= 0; f--) {
                    BedrockPart t = ironSightPath.get(f);
                    float[] q = toQuaternion(-t.xRot * v, -t.yRot * v, -t.zRot * v);
                    matrixStack.mulPose(new Quaternion(q[0], q[1], q[2], q[3]));
                    if (t.getParent() != null)
                        matrixStack.translate(-t.x / 16.0F * v, -t.y / 16.0F * v, -t.z / 16.0F * v);
                    else {
                        matrixStack.translate(-t.x / 16.0F * v, (1.5F - t.y / 16.0F) * v, -t.z / 16.0F * v);
                    }
                }
                matrixStack.translate(0, -1.5f, 0);
            } else {
                IOverrideModel scopeModel = OverrideModelManager.getModel(scopeItemStack);
                if (scopeModel instanceof BedrockAttachmentModel bedrockScopeModel) {
                    //应用定位组的反向位移、旋转，使定位组的位置就是屏幕中心
                    matrixStack.translate(0, 1.5f, 0);
                    for (int f = bedrockScopeModel.scopeViewPath.size() - 1; f >= 0; f--) {
                        BedrockPart t = bedrockScopeModel.scopeViewPath.get(f);
                        float[] q = toQuaternion(-t.xRot * v, -t.yRot * v, -t.zRot * v);
                        matrixStack.mulPose(new Quaternion(q[0], q[1], q[2], q[3]));
                        if (t.getParent() != null)
                            matrixStack.translate(-t.x / 16.0F * v, -t.y / 16.0F * v, -t.z / 16.0F * v);
                        else {
                            matrixStack.translate(-t.x / 16.0F * v, (1.5F - t.y / 16.0F) * v, -t.z / 16.0F * v);
                        }
                    }
                    for (int f = scopePosPath.size() - 1; f >= 0; f--) {
                        BedrockPart t = scopePosPath.get(f);
                        float[] q = toQuaternion(-t.xRot * v, -t.yRot * v, -t.zRot * v);
                        matrixStack.mulPose(new Quaternion(q[0], q[1], q[2], q[3]));
                        if (t.getParent() != null)
                            matrixStack.translate(-t.x / 16.0F * v, -t.y / 16.0F * v, -t.z / 16.0F * v);
                        else {
                            matrixStack.translate(-t.x / 16.0F * v, (1.5F - t.y / 16.0F) * v, -t.z / 16.0F * v);
                        }
                    }
                    matrixStack.translate(0, -1.5f, 0);
                }
            }
            //主摄像机的默认位置是(0, 8, 12)
            matrixStack.translate(0, 0.5 * (1 - v), -0.75 * (1 - v));
        }
        //调用上层渲染方法
        render(transformType, matrixStack, buffer, light, overlay);
        matrixStack.popPose();
    }

    private static float[] toQuaternion(float roll, float pitch, float yaw) {
        double cy = Math.cos(yaw * 0.5);
        double sy = Math.sin(yaw * 0.5);
        double cp = Math.cos(pitch * 0.5);
        double sp = Math.sin(pitch * 0.5);
        double cr = Math.cos(roll * 0.5);
        double sr = Math.sin(roll * 0.5);
        return new float[]{
                (float) (cy * cp * sr - sy * sp * cr),
                (float) (sy * cp * sr + cy * sp * cr),
                (float) (sy * cp * cr - cy * sp * sr),
                (float) (cy * cp * cr + sy * sp * sr)
        };
    }
}
