package corgitaco.enhancedcelestials.api.entityfilter;

import com.mojang.serialization.Codec;
import net.minecraft.world.entity.LivingEntity;

public record AnyEntityFilter() implements EntityFilter {

    public static final AnyEntityFilter INSTANCE = new AnyEntityFilter();

    public static final Codec<AnyEntityFilter> CODEC = Codec.unit(() -> INSTANCE);

    @Override
    public Codec<? extends EntityFilter> codec() {
        return CODEC;
    }

    @Override
    public boolean filter(LivingEntity entity) {
        return true;
    }
}
