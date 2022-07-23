package corgitaco.enhancedcelestials.api.entityfilter;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import corgitaco.enhancedcelestials.util.CodecUtil;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;

public record EntityTypeFilter(EntityType<?> entityType) implements EntityFilter {

    public static final Codec<EntityTypeFilter> CODEC = RecordCodecBuilder.create(builder ->
            builder.group(
                    CodecUtil.ENTITY_TYPE.fieldOf("id").forGetter(EntityTypeFilter::entityType)
            ).apply(builder, EntityTypeFilter::new)
    );


    @Override
    public Codec<? extends EntityFilter> codec() {
        return CODEC;
    }

    @Override
    public boolean filter(LivingEntity entity) {
        return entity.getType() == this.entityType;
    }
}
