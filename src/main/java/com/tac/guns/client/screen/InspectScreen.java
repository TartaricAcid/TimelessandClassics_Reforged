package com.tac.guns.client.screen;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import com.tac.guns.client.handler.GunRenderingHandler;
import com.tac.guns.client.util.RenderUtil;
import com.tac.guns.common.container.InspectionContainer;
import com.tac.guns.item.GunItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;

/**
 * Author: Forked from MrCrayfish, continued by Timeless devs
 */
public class InspectScreen extends AbstractContainerScreen<InspectionContainer> {
    //private static final ResourceLocation GUI_TEXTURES = new ResourceLocation("tac:textures/gui/attachments.png");

    private final Inventory playerInventory;
    //private final IInventory weaponInventory;

    private boolean showHelp = true;
    private int windowZoom = 20;
    private int windowX, windowY;
    private float windowRotationX, windowRotationY;
    private boolean mouseGrabbed;
    private int mouseGrabbedButton;
    private int mouseClickedX, mouseClickedY;

    public InspectScreen(InspectionContainer screenContainer, Inventory playerInventory, Component titleIn) {
        super(screenContainer, playerInventory, titleIn);
        this.playerInventory = playerInventory;
        //this.weaponInventory = screenContainer.getWeaponInventory();
        this.imageHeight = -270; //186
    }

    @Override
    protected void containerTick() {
        super.containerTick();
        if (this.minecraft != null && this.minecraft.player != null) {
            if (!(this.minecraft.player.getMainHandItem().getItem() instanceof GunItem)) {
                Minecraft.getInstance().setScreen(null);
            }
        }
    }

    @Override
    public void render(PoseStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        this.renderTooltip(matrixStack, mouseX, mouseY); //Render tool tips
    }

    @Override
    protected void renderLabels(PoseStack matrixStack, int mouseX, int mouseY) {
        Minecraft minecraft = Minecraft.getInstance();
        this.font.draw(matrixStack, this.title, (float) this.titleLabelX, (float) this.titleLabelY, 4210752);
        this.font.draw(matrixStack, this.playerInventory.getDisplayName(), (float) this.inventoryLabelX, (float) this.inventoryLabelY + 19, 4210752);

        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        int left = (this.width - this.imageWidth) / 2;
        int top = (this.height - this.imageHeight) / 2;
        RenderUtil.scissor(left - 166, top - 277, 650, 1600);

        //RenderUtil.scissor(left + 26, top + 17, 142, 70);

        PoseStack stack = RenderSystem.getModelViewStack();
        stack.pushPose();
        {
            stack.translate(06, -150, 550);
            stack.translate(this.windowX + (this.mouseGrabbed && this.mouseGrabbedButton == 0 ? mouseX - this.mouseClickedX : 0), 0, 0);
            stack.translate(0, this.windowY + (this.mouseGrabbed && this.mouseGrabbedButton == 0 ? mouseY - this.mouseClickedY : 0), 0);
            stack.mulPose(Vector3f.XP.rotationDegrees(-30F));
            stack.mulPose(Vector3f.XP.rotationDegrees(this.windowRotationY - (this.mouseGrabbed && this.mouseGrabbedButton == 1 ? mouseY - this.mouseClickedY : 0)));
            stack.mulPose(Vector3f.YP.rotationDegrees(this.windowRotationX + (this.mouseGrabbed && this.mouseGrabbedButton == 1 ? mouseX - this.mouseClickedX : 0)));
            stack.mulPose(Vector3f.YP.rotationDegrees(150F));
            stack.scale(this.windowZoom / 10F, this.windowZoom / 10F, this.windowZoom / 10F);
            stack.scale(90F, -90F, 90F);
            stack.mulPose(Vector3f.XP.rotationDegrees(5F));
            stack.mulPose(Vector3f.YP.rotationDegrees(90F));

            RenderSystem.applyModelViewMatrix();

            RenderSystem.enableBlend();
            RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);

            MultiBufferSource.BufferSource buffer = this.minecraft.renderBuffers().bufferSource();
            GunRenderingHandler.get().renderWeapon(this.minecraft.player, this.minecraft.player.getMainHandItem(), ItemTransforms.TransformType.GROUND, matrixStack, buffer, 15728880, 0F); // GROUND, matrixStack, buffer, 15728880, 0F);
            buffer.endBatch();
        }
        stack.popPose();
        RenderSystem.applyModelViewMatrix();

        GL11.glDisable(GL11.GL_SCISSOR_TEST);
    }

    @Override
    protected void renderBg(PoseStack matrixStack, float partialTicks, int mouseX, int mouseY) {
        /*RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        Minecraft minecraft = Minecraft.getInstance();
        minecraft.getTextureManager().bind(GUI_TEXTURES);
        int left = (this.width - this.imageWidth) / 2;
        int top = (this.height - this.imageHeight) / 2;
        this.blit(matrixStack, left, top, 0, 0, this.imageWidth, this.imageHeight);

        *//* Draws the icons for each attachment slot. If not applicable
         * for the weapon, it will draw a cross instead. *//*
        for(int i = 0; i < IAttachment.Type.values().length; i++)
        {
            if(!this.menu.getSlot(i).isActive())
            {
                this.blit(matrixStack, left + 8, top + 17 + i * 18, 176, 0, 16, 16);
            }
            else if(this.weaponInventory.getItem(i).isEmpty())
            {
                this.blit(matrixStack, left + 8, top + 17 + i * 18, 176, 16 + i * 16, 16, 16);
            }
        }*/
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double scroll) {
        int startX = (this.width - this.imageWidth) / 2;
        int startY = (this.height - this.imageHeight) / 2;
        if (RenderUtil.isMouseWithin((int) mouseX, (int) mouseY, startX - 196, startY - 277, 650, 1600)) {
            if (scroll < 0 && this.windowZoom > 0) {
                this.showHelp = false;
                this.windowZoom--;
            } else if (scroll > 0) {
                this.showHelp = false;
                this.windowZoom++;
            }
        }
        return false;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        int startX = (this.width - this.imageWidth) / 2;
        int startY = (this.height - this.imageHeight) / 2;

        if (RenderUtil.isMouseWithin((int) mouseX, (int) mouseY, startX - 196, startY - 277, 650, 1600)) {
            if (!this.mouseGrabbed && (button == GLFW.GLFW_MOUSE_BUTTON_LEFT || button == GLFW.GLFW_MOUSE_BUTTON_RIGHT)) {
                this.mouseGrabbed = true;
                this.mouseGrabbedButton = button == GLFW.GLFW_MOUSE_BUTTON_RIGHT ? 1 : 0;
                this.mouseClickedX = (int) mouseX;
                this.mouseClickedY = (int) mouseY;
                this.showHelp = false;
                return true;
            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (this.mouseGrabbed) {
            if (this.mouseGrabbedButton == 0 && button == GLFW.GLFW_MOUSE_BUTTON_LEFT) {
                this.mouseGrabbed = false;
                this.windowX += (mouseX - this.mouseClickedX - 1);
                this.windowY += (mouseY - this.mouseClickedY);
            } else if (mouseGrabbedButton == 1 && button == GLFW.GLFW_MOUSE_BUTTON_RIGHT) {
                this.mouseGrabbed = false;
                this.windowRotationX += (mouseX - this.mouseClickedX);
                this.windowRotationY -= (mouseY - this.mouseClickedY);
            }
        }
        return super.mouseReleased(mouseX, mouseY, button);
    }
}
