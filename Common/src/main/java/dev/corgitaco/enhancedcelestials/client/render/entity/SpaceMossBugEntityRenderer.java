package dev.corgitaco.enhancedcelestials.client.render.entity;

import dev.corgitaco.enhancedcelestials.EnhancedCelestials;
import dev.corgitaco.enhancedcelestials.client.EnhancedCelestialsModelLayers;
import dev.corgitaco.enhancedcelestials.client.model.SpaceMossBugModel;
import dev.corgitaco.enhancedcelestials.entity.SpaceMossBugEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public final class SpaceMossBugEntityRenderer extends MobRenderer<SpaceMossBugEntity, SpaceMossBugModel> {
    private static final ResourceLocation TEXTURE = EnhancedCelestials.createLocation("textures/entity/space_moss_bug.png");

    public SpaceMossBugEntityRenderer(EntityRendererProvider.Context $$0) {
        super($$0, new SpaceMossBugModel($$0.bakeLayer(EnhancedCelestialsModelLayers.SPACE_MOSS_BUG)), 0.5F);
    }

    @Override
    public ResourceLocation getTextureLocation(SpaceMossBugEntity spaceMossBug) {
        return TEXTURE;
    }
}
