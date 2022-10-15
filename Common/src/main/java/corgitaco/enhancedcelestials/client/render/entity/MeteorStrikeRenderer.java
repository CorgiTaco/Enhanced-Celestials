package corgitaco.enhancedcelestials.client.render.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Matrix3f;
import com.mojang.math.Matrix4f;
import com.mojang.math.Vector3f;
import corgitaco.enhancedcelestials.EnhancedCelestials;
import corgitaco.enhancedcelestials.entity.MeteorStrikeEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import org.lwjgl.glfw.GLFW;

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
        matrices.mulPose(Vector3f.YP.rotationDegrees((float) GLFW.glfwGetTime() * 25));


        renderPlaneTexture(matrices, vertexConsumers, width);
    }

    private static void renderPlaneTexture(PoseStack matrices, MultiBufferSource vertexConsumers, float width) {
        Matrix4f pose = matrices.last().pose();
        Matrix3f normal = matrices.last().normal();
        VertexConsumer buffer = vertexConsumers.getBuffer(RenderType.entityTranslucent(TEXTURE));
        buffer.vertex(pose, -width, 0F, -width).color(255, 255, 255, 255).uv(0,0).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(15728880).normal(normal, 0.0F, 0.0F, 1.0F).endVertex();
        buffer.vertex(pose, -width, 0F, width).color(255, 255, 255, 255).uv(0,1).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(15728880).normal(normal, 0.0F, 0.0F, 1.0F).endVertex();
        buffer.vertex(pose, width, 0F, width).color(255, 255, 255, 255).uv(1,1).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(15728880).normal(normal, 0.0F, 1.0F, 0.0F).endVertex();
        buffer.vertex(pose, width, 0F, -width).color(255, 255, 255, 255).uv(1, 0).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(15728880).normal(normal, 0.0F, 1.0F, 0.0F).endVertex();
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
