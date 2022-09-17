package corgitaco.enhancedcelestials.client.render.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import corgitaco.enhancedcelestials.core.ECBlocks;
import corgitaco.enhancedcelestials.entity.MeteorEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;

public class MeteorEntityRenderer extends EntityRenderer<MeteorEntity> {

    public MeteorEntityRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void render(MeteorEntity entity, float yaw, float tickDelta, PoseStack matrices, MultiBufferSource vertexConsumers, int light) {
        var size = entity.getSize();

        matrices.pushPose();
        matrices.scale(0.5F * size, 0.5F * size, 0.5F * size);

        var dispatcher = Minecraft.getInstance().getBlockRenderer();

        var state = ECBlocks.METEOR.get().defaultBlockState();

        var consumer = vertexConsumers.getBuffer(RenderType.solid());

        var source = RandomSource.create(state.getSeed(BlockPos.ZERO));

        matrices.pushPose();
        matrices.mulPose(Vector3f.XP.rotationDegrees(15F));
        dispatcher.renderBatched(state, BlockPos.ZERO, entity.level, matrices, consumer, false, source);
        matrices.popPose();

        matrices.pushPose();
        matrices.translate(0.4, 0.4, 0.4);
        matrices.mulPose(Vector3f.XP.rotationDegrees(45F));
        dispatcher.renderBatched(state, BlockPos.ZERO, entity.level, matrices, consumer, false, source);
        matrices.popPose();

        matrices.pushPose();
        matrices.translate(-0.1, 0.2, 0.7);
        matrices.mulPose(Vector3f.ZN.rotationDegrees(45F));
        dispatcher.renderBatched(state, BlockPos.ZERO, entity.level, matrices, consumer, false, source);
        matrices.popPose();

        matrices.popPose();

        super.render(entity, yaw, tickDelta, matrices, vertexConsumers, light);
    }

    @Override
    public boolean shouldRender(MeteorEntity $$0, Frustum $$1, double $$2, double $$3, double $$4) {
        return true;
    }

    @Override
    public ResourceLocation getTextureLocation(MeteorEntity meteorEntity) {
        return null;
    }
}
