package corgitaco.enhancedcelestials;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.dimension.DimensionType;

public class DimensionTypeTags {

    public static final TagKey<DimensionType> HAS_LUNAR_EVENTS = create("has_lunar_events");

    private static TagKey<DimensionType> create(String id) {
        return TagKey.create(Registry.DIMENSION_TYPE_REGISTRY, new ResourceLocation(EnhancedCelestials.MOD_ID, id));
    }

    public static void init() {
    }
}