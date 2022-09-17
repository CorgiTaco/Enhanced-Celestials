package corgitaco.enhancedcelestials.client;

import corgitaco.enhancedcelestials.client.render.entity.MeteorEntityRenderer;
import corgitaco.enhancedcelestials.core.ECEntities;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;

public class ECEntityRenderers {

    public static <T extends Entity> void register(RegisterStrategy registerStrategy) {
        registerStrategy.register(ECEntities.METEOR.get(), MeteorEntityRenderer::new);
    }

    @FunctionalInterface
    public interface RegisterStrategy {
        <T extends Entity> void register(EntityType<? extends T> entityType, EntityRendererProvider<T> entityRendererProvider);
    }
}
