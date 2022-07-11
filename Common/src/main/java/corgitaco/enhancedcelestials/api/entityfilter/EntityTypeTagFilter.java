package corgitaco.enhancedcelestials.api.entityfilter;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Registry;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;

public record EntityTypeTagFilter(TagKey<EntityType<?>> entityTypeTag, boolean inverse) implements EntityFilter {

    public static final Codec<EntityTypeTagFilter> CODEC = RecordCodecBuilder.create(builder ->
            builder.group(
                    TagKey.hashedCodec(Registry.ENTITY_TYPE_REGISTRY).fieldOf("tag").forGetter(EntityTypeTagFilter::entityTypeTag),
                    Codec.BOOL.fieldOf("inverse").forGetter(EntityTypeTagFilter::inverse)
            ).apply(builder, EntityTypeTagFilter::new)
    );


    @Override
    public Codec<? extends EntityFilter> codec() {
        return CODEC;
    }

    @Override
    public boolean filter(LivingEntity entity) {
        return this.inverse != (entity.getType().is(this.entityTypeTag));
    }
}
