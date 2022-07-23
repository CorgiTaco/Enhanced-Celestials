package corgitaco.enhancedcelestials.api.entityfilter;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.entity.LivingEntity;

public record InverseEntityFilter(EntityFilter filter) implements EntityFilter {

    public static final Codec<InverseEntityFilter> CODEC = RecordCodecBuilder.create(builder ->
            builder.group(
                    EntityFilter.CODEC.fieldOf("filter").forGetter(InverseEntityFilter::filter)
            ).apply(builder, InverseEntityFilter::new)
    );

    @Override
    public Codec<? extends EntityFilter> codec() {
        return CODEC;
    }

    @Override
    public boolean filter(LivingEntity entity) {
        return !this.filter.filter(entity);
    }
}
