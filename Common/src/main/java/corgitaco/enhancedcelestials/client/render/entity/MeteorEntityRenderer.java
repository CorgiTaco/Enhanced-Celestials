package corgitaco.enhancedcelestials.client.render.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import corgitaco.enhancedcelestials.core.ECBlocks;
import corgitaco.enhancedcelestials.entity.MeteorEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;

public class MeteorEntityRenderer extends EntityRenderer<MeteorEntity> {
    private final BlockRenderDispatcher blockRenderDispatcher;

    public MeteorEntityRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.blockRenderDispatcher = context.getBlockRenderDispatcher();
    }


    @Override
    public void render(MeteorEntity entity, float yaw, float tickDelta, PoseStack matrices, MultiBufferSource vertexConsumers, int light) {
        matrices.pushPose();
        matrices.scale(3, 3, 3);
        this.blockRenderDispatcher.renderSingleBlock(ECBlocks.METEOR.get().defaultBlockState(), matrices, vertexConsumers, light, OverlayTexture.NO_OVERLAY);
        matrices.popPose();
        super.render(entity, yaw, tickDelta, matrices, vertexConsumers, light);
    }

    @Override
    public ResourceLocation getTextureLocation(MeteorEntity meteorEntity) {
        return null;
    }
}
