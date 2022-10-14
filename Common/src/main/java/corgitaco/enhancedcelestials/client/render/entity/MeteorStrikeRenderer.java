package corgitaco.enhancedcelestials.client.render.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import corgitaco.enhancedcelestials.entity.MeteorStrikeEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class MeteorStrikeRenderer extends EntityRenderer<MeteorStrikeEntity>  {



    public MeteorStrikeRenderer(EntityRendererProvider.Context $$0) {
        super($$0);
    }

    @Override
    public void render(MeteorStrikeEntity meteorStrikeEntity, float yaw, float tickDelta, PoseStack matrices, MultiBufferSource vertexConsumers, int light) {
        super.render(meteorStrikeEntity, yaw, tickDelta, matrices, vertexConsumers, light);



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
