package dev.corgitaco.enhancedcelestials.client.render.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import dev.corgitaco.enhancedcelestials.EnhancedCelestials;
import dev.corgitaco.enhancedcelestials.entity.MeteorStrikeEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import org.joml.Matrix4f;
import org.lwjgl.glfw.GLFW;

import java.util.UUID;

public class MeteorStrikeRenderer extends EntityRenderer<MeteorStrikeEntity> {


    public static final ResourceLocation TEXTURE = EnhancedCelestials.createLocation("textures/entity/meteor_strike.png");

    public MeteorStrikeRenderer(EntityRendererProvider.Context $$0) {
        super($$0);
    }

    @Override
    public void render(MeteorStrikeEntity meteorStrikeEntity, float yaw, float tickDelta, PoseStack matrices, MultiBufferSource vertexConsumers, int light) {
        super.render(meteorStrikeEntity, yaw, tickDelta, matrices, vertexConsumers, light);
        float width = meteorStrikeEntity.getBbWidth() / 2F;

        matrices.translate(0, 0.02, 0);
        UUID uuid = meteorStrikeEntity.getUUID();
        float rotation = (float) (((GLFW.glfwGetTime() + ((int) uuid.getMostSignificantBits()))) % 360F);

        matrices.mulPose(Axis.YP.rotationDegrees(rotation * 25F));


        renderPlaneTexture(matrices, vertexConsumers, width);
    }

    private static void renderPlaneTexture(PoseStack matrices, MultiBufferSource vertexConsumers, float width) {
        Matrix4f pose = matrices.last().pose();
        VertexConsumer buffer = vertexConsumers.getBuffer(RenderType.entityTranslucent(TEXTURE));
        buffer.addVertex(pose, -width, 0F, -width).setColor(255, 255, 255, 255).setUv(0,0).setOverlay(OverlayTexture.NO_OVERLAY).setLight(15728880).setNormal(matrices.last(), 0.0F, 0.0F, 1.0F);
        buffer.addVertex(pose, -width, 0F, width).setColor(255, 255, 255, 255).setUv(0,1).setOverlay(OverlayTexture.NO_OVERLAY).setLight(15728880).setNormal(matrices.last(), 0.0F, 0.0F, 1.0F);
        buffer.addVertex(pose, width, 0F, width).setColor(255, 255, 255, 255).setUv(1,1).setOverlay(OverlayTexture.NO_OVERLAY).setLight(15728880).setNormal(matrices.last(), 0.0F, 1.0F, 0.0F);
        buffer.addVertex(pose, width, 0F, -width).setColor(255, 255, 255, 255).setUv(1, 0).setOverlay(OverlayTexture.NO_OVERLAY).setLight(15728880).setNormal(matrices.last(), 0.0F, 1.0F, 0.0F);
    }

    @Override
    public ResourceLocation getTextureLocation(MeteorStrikeEntity meteorEntity) {
        return null;
    }

    @Override
    protected boolean shouldShowName(MeteorStrikeEntity $$0) {
        return false;
    }
}
