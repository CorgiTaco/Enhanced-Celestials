package corgitaco.enhancedcelestials.client.render.entity;

import com.mojang.blaze3d.vertex.PoseStack;
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
    private static final int BIT_MASK = 0x1F;

    public MeteorEntityRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void render(MeteorEntity entity, float yaw, float tickDelta, PoseStack matrices, MultiBufferSource vertexConsumers, int light) {
        var size = entity.getBbWidth();

        matrices.pushPose();

        float xzOffset = size * -0.5F;
        matrices.translate(xzOffset, 0, xzOffset);
        matrices.scale(size, size, size);

        var dispatcher = Minecraft.getInstance().getBlockRenderer();

        var state = ECBlocks.METEOR.get().defaultBlockState();

        var consumer = vertexConsumers.getBuffer(RenderType.solid());

        var source = RandomSource.create(state.getSeed(BlockPos.ZERO));

        var uuid = entity.getUUID();
        var most = uuid.getMostSignificantBits();
        var least = uuid.getLeastSignificantBits();

        dispatcher.renderBatched(state, BlockPos.ZERO, entity.level, matrices, consumer, false, source);


        for (int i = 0; i < Math.max(Math.floorMod(least, 5), 1); i++) {
            var random = most * i; // First should always be 0, 0, 0
            var x = (random & BIT_MASK) * 0.0325;
            var y = (random >> 5 & BIT_MASK) * 0.0325;
            var z = (random >> 10 & BIT_MASK) * 0.0325;

            matrices.pushPose();
            matrices.translate(
                    ((random & 1) == 1 ? x : z),
                    ((random >> 5 & 1) == 1 ? y : x),
                    ((random >> 10 & 1) == 1 ? z : y)
            );
            dispatcher.renderBatched(state, BlockPos.ZERO, entity.level, matrices, consumer, false, source);
            matrices.popPose();
        }

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
