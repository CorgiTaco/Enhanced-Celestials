package corgitaco.enhancedcelestials.client;

import corgitaco.enhancedcelestials.EnhancedCelestials;
import corgitaco.enhancedcelestials.client.model.SpaceMossBugModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.builders.LayerDefinition;

import java.util.function.BiConsumer;
import java.util.function.Supplier;

public final class EnhancedCelestialsModelLayers {
    public static final ModelLayerLocation SPACE_MOSS_BUG = new ModelLayerLocation(EnhancedCelestials.createLocation("space_moss_bug"), "main");

    private EnhancedCelestialsModelLayers() {
    }

    public static void register(BiConsumer<ModelLayerLocation, Supplier<LayerDefinition>> consumer) {
        consumer.accept(SPACE_MOSS_BUG, SpaceMossBugModel::createBodyLayer);
    }
}
