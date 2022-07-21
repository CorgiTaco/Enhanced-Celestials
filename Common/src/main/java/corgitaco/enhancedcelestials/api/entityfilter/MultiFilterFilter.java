package corgitaco.enhancedcelestials.api.entityfilter;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.entity.LivingEntity;

import java.util.List;

public record MultiFilterFilter(List<EntityFilter> filters, boolean inverse) implements EntityFilter {

    public static final Codec<MultiFilterFilter> CODEC = RecordCodecBuilder.create(builder ->
            builder.group(
                    EntityFilter.CODEC.listOf().fieldOf("mob_category").forGetter(MultiFilterFilter::filters),
                    Codec.BOOL.fieldOf("inverse").forGetter(MultiFilterFilter::inverse)
            ).apply(builder, MultiFilterFilter::new)
    );


    @Override
    public Codec<? extends EntityFilter> codec() {
        return CODEC;
    }

    @Override
    public boolean filter(LivingEntity entity) {
        for (EntityFilter filter : filters) {
            if (!filter.filter(entity)) {
                return false;
            }
        }
        return true;
    }
}
