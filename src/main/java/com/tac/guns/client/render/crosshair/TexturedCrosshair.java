package com.tac.guns.client.render.crosshair;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Matrix4f;
import com.tac.guns.client.handler.AimingHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;

/**
 * Author: Forked from MrCrayfish, continued by Timeless devs
 */
public class TexturedCrosshair extends Crosshair {
    protected ResourceLocation texture;
    protected boolean blend;

    private boolean isHitMarker;

    public TexturedCrosshair(ResourceLocation id, boolean blend, boolean isHitMarker) {
        super(id);
        this.texture = new ResourceLocation(id.getNamespace(), "textures/crosshair_hit/" + id.getPath() + ".png");
        this.isHitMarker = isHitMarker;
        this.blend = blend;
    }

    public TexturedCrosshair(ResourceLocation id) {
        this(id, true);
    }

    public TexturedCrosshair(ResourceLocation id, boolean blend) {
        super(id);
        this.texture = new ResourceLocation(id.getNamespace(), "textures/crosshair/" + id.getPath() + ".png");
        this.blend = blend;
    }

    public void renderHitMarker(Minecraft mc, PoseStack stack, int windowWidth, int windowHeight, float alpha) {
        //float alpha = 1.0F * Math.abs((stepping-1)*0.5f) ;
        float size = 8.0F;
        stack.translate((windowWidth - size) / 2F, (windowHeight - size) / 2F, 0);
        RenderSystem.setShaderTexture(0, this.texture);
        RenderSystem.enableBlend();

        if (this.blend) {
            RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.ONE_MINUS_DST_COLOR, GlStateManager.DestFactor.ONE_MINUS_SRC_COLOR, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        }

        Matrix4f matrix = stack.last().pose();
        BufferBuilder buffer = Tesselator.getInstance().getBuilder();
        buffer.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR);
        buffer.vertex(matrix, 0, size, 0).uv(0, 1).color(1.0F, 1.0F, 1.0F, alpha).endVertex();
        buffer.vertex(matrix, size, size, 0).uv(1, 1).color(1.0F, 1.0F, 1.0F, alpha).endVertex();
        buffer.vertex(matrix, size, 0, 0).uv(1, 0).color(1.0F, 1.0F, 1.0F, alpha).endVertex();
        buffer.vertex(matrix, 0, 0, 0).uv(0, 0).color(1.0F, 1.0F, 1.0F, alpha).endVertex();
        BufferUploader.drawWithShader(buffer.end());

        if (this.blend) {
            RenderSystem.defaultBlendFunc();
        }
    }

    @Override
    public void render(Minecraft mc, PoseStack stack, int windowWidth, int windowHeight, float partialTicks) {
        float alpha = 1.0F - (float) AimingHandler.get().getNormalisedAdsProgress();
        float size = 8.0F;
        stack.translate((windowWidth - size) / 2F, (windowHeight - size) / 2F, 0);

        RenderSystem.setShaderTexture(0, this.texture);
        RenderSystem.enableBlend();

        if (this.blend) {
            RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.ONE_MINUS_DST_COLOR, GlStateManager.DestFactor.ONE_MINUS_SRC_COLOR, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        }

        Matrix4f matrix = stack.last().pose();
        BufferBuilder buffer = Tesselator.getInstance().getBuilder();
        buffer.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR);
        buffer.vertex(matrix, 0, size, 0).uv(0, 1).color(1.0F, 1.0F, 1.0F, alpha).endVertex();
        buffer.vertex(matrix, size, size, 0).uv(1, 1).color(1.0F, 1.0F, 1.0F, alpha).endVertex();
        buffer.vertex(matrix, size, 0, 0).uv(1, 0).color(1.0F, 1.0F, 1.0F, alpha).endVertex();
        buffer.vertex(matrix, 0, 0, 0).uv(0, 0).color(1.0F, 1.0F, 1.0F, alpha).endVertex();
        RenderSystem.setShaderTexture(0, this.texture);
        BufferUploader.drawWithShader(buffer.end());

        if (this.blend) {
            RenderSystem.defaultBlendFunc();
        }
    }
}
