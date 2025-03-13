package dev.corgitaco.enhancedcelestials.client;

import dev.corgitaco.enhancedcelestials.EnhancedCelestials;
import dev.corgitaco.enhancedcelestials.client.render.entity.MeteorEntityRenderer;
import dev.corgitaco.enhancedcelestials.client.render.entity.MeteorStrikeRenderer;
import dev.corgitaco.enhancedcelestials.client.render.entity.SpaceMossBugEntityRenderer;
import dev.corgitaco.enhancedcelestials.core.ECEntities;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;

public class ECEntityRenderers {

    public static <T extends Entity> void register(RegisterStrategy registerStrategy) {
        if (EnhancedCelestials.NEW_CONTENT) {
            registerStrategy.register(ECEntities.METEOR.get(), MeteorEntityRenderer::new);
            registerStrategy.register(ECEntities.SPACE_MOSS_BUG.get(), SpaceMossBugEntityRenderer::new);
            registerStrategy.register(ECEntities.METEOR_STRIKE.get(), MeteorStrikeRenderer::new);
        }
    }

    @FunctionalInterface
    public interface RegisterStrategy {
        <T extends Entity> void register(EntityType<? extends T> entityType, EntityRendererProvider<T> entityRendererProvider);
    }
}
