package corgitaco.enhancedcelestials.client.render.entity;

import corgitaco.enhancedcelestials.EnhancedCelestials;
import corgitaco.enhancedcelestials.entity.SpaceMossBugEntity;
import net.minecraft.client.model.CreeperModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public final class SpaceMossBugEntityRenderer extends MobRenderer<SpaceMossBugEntity, CreeperModel<SpaceMossBugEntity>> {
    private static final ResourceLocation TEXTURE = EnhancedCelestials.createLocation("textures/entity/space_moss_bug.png");

    public SpaceMossBugEntityRenderer(EntityRendererProvider.Context $$0) {
        super($$0, new CreeperModel<>($$0.bakeLayer(ModelLayers.CREEPER)), 0.5F);
    }

    @Override
    public ResourceLocation getTextureLocation(SpaceMossBugEntity spaceMossBug) {
        return TEXTURE;
    }
}
