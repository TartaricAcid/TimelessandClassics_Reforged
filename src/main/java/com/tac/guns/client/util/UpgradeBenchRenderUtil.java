package com.tac.guns.client.util;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import com.tac.guns.client.handler.GunRenderingHandler;
import com.tac.guns.init.ModItems;
import com.tac.guns.item.transition.TimelessGunItem;
import com.tac.guns.tileentity.UpgradeBenchTileEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class UpgradeBenchRenderUtil implements BlockEntityRenderer<UpgradeBenchTileEntity> {


    /**
     * render the tile entity - called every frame while the tileentity is in view of the player
     *
     * @param tileEntityMBE21 the associated tile entity
     * @param partialTicks    the fraction of a tick that this frame is being rendered at - used to interpolate frames between
     *                        ticks, to make animations smoother.  For example - if the frame rate is steady at 80 frames per second,
     *                        this method will be called four times per tick, with partialTicks spaced 0.25 apart, (eg) 0, 0.25, 0.5, 0.75
     * @param matrixStack     the matrixStack is used to track the current view transformations that have been applied - i.e translation, rotation, scaling
     *                        it is needed for you to render the view properly.
     * @param renderBuffers   the buffer that you should render your model to
     * @param combinedLight   the blocklight + skylight value for the tileEntity.  see http://greyminecraftcoder.blogspot.com/2014/12/lighting-18.html (outdated, but the concepts are still valid)
     * @param combinedOverlay value for the "combined overlay" which changes the render based on an overlay texture (see OverlayTexture class).
     *                        Used by vanilla for (1) red tint when a living entity takes damage, and (2) "flash" effect for creeper when ignited
     *                        CreeperRenderer.getOverlayProgress()
     */
    @Override
    public void render(UpgradeBenchTileEntity tileEntityMBE21, float partialTicks, PoseStack matrixStack, MultiBufferSource renderBuffers,
                       int combinedLight, int combinedOverlay) {
        //ItemTransforms.TransformType.GROUND,
        matrixStack.pushPose();
        matrixStack.translate(0.5, 1.05, 0.5);
        matrixStack.mulPose(Vector3f.ZP.rotationDegrees(90F));

        matrixStack.translate(-0.14, -0.4200001, 0);
        if (!(tileEntityMBE21.getItem(0).getItem() instanceof TimelessGunItem))
            GunRenderingHandler.get().renderWeapon(Minecraft.getInstance().player, ItemStack.of(tileEntityMBE21.getUpdateTag().getCompound("weapon")), ItemTransforms.TransformType.GROUND, matrixStack, renderBuffers, combinedLight, combinedOverlay);
        else
            GunRenderingHandler.get().renderWeapon(Minecraft.getInstance().player, tileEntityMBE21.getItem(0), ItemTransforms.TransformType.GROUND, matrixStack, renderBuffers, combinedLight, combinedOverlay);
        matrixStack.popPose();

        matrixStack.pushPose();

        /*if(Config.COMMON.development.enableTDev.get() && (ObjectRenderEditor.get() != null && ObjectRenderEditor.get().currElement == 1 && ObjectRenderEditor.get().GetFromElements(1) != null)) {
            matrixStack.translate(ObjectRenderEditor.get().GetFromElements(1).getxMod(), ObjectRenderEditor.get().GetFromElements(1).getyMod(), ObjectRenderEditor.get().GetFromElements(1).getzMod());
        }*/
        matrixStack.translate(-0.14, -0.4200001, 0);
        matrixStack.translate(0.205, 1.48, 0.19);
        if (tileEntityMBE21.getItem(1).getItem() == ModItems.MODULE.get()) {
            if (tileEntityMBE21.getItem(1).getCount() > 0) {
                Minecraft.getInstance().getItemRenderer().renderStatic(tileEntityMBE21.getItem(1), ItemTransforms.TransformType.GROUND, combinedLight, combinedOverlay, matrixStack, renderBuffers, 0);
            }
            if (tileEntityMBE21.getItem(1).getCount() > 1) {
                matrixStack.translate(0.12, 0, 0);
                Minecraft.getInstance().getItemRenderer().renderStatic(tileEntityMBE21.getItem(1), ItemTransforms.TransformType.GROUND, combinedLight, combinedOverlay, matrixStack, renderBuffers, 0);
            }
            if (tileEntityMBE21.getItem(1).getCount() > 2) {
                matrixStack.translate(0.12, 0, 0);
                Minecraft.getInstance().getItemRenderer().renderStatic(tileEntityMBE21.getItem(1), ItemTransforms.TransformType.GROUND, combinedLight, combinedOverlay, matrixStack, renderBuffers, 0);
            }
        }

        matrixStack.popPose();
    }

    // this should be true for tileentities which render globally (no render bounding box), such as beacons.
    @Override
    public boolean shouldRenderOffScreen(UpgradeBenchTileEntity tileEntityMBE21) {
        return true;
    }
}
