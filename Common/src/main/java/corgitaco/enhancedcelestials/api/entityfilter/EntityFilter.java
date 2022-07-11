package corgitaco.enhancedcelestials.api.entityfilter;

import com.mojang.serialization.Codec;
import corgitaco.enhancedcelestials.EnhancedCelestials;
import corgitaco.enhancedcelestials.api.EnhancedCelestialsRegistry;
import corgitaco.enhancedcelestials.reg.RegistrationProvider;
import net.minecraft.world.entity.LivingEntity;

public interface EntityFilter {
    Codec<EntityFilter> CODEC = EnhancedCelestialsRegistry.ENTITY_FILTER.byNameCodec().dispatchStable(EntityFilter::codec, codec -> codec.fieldOf("config").codec());
    RegistrationProvider<Codec<? extends EntityFilter>> ENTITY_FILTERS = RegistrationProvider.get(EnhancedCelestialsRegistry.ENTITY_FILTER_KEY, EnhancedCelestials.MOD_ID);

    Codec<? extends EntityFilter> codec();

    boolean filter(LivingEntity entity);

    static void init() {
        ENTITY_FILTERS.register("by_mob_category", () -> MobCategoryFilter.CODEC);
        ENTITY_FILTERS.register("by_type", () -> EntityTypeFilter.CODEC);
        ENTITY_FILTERS.register("by_type_tag", () -> EntityTypeTagFilter.CODEC);
        ENTITY_FILTERS.register("any", () -> AnyEntityFilter.CODEC);
    }
}
