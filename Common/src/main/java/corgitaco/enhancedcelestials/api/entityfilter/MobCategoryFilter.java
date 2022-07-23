package corgitaco.enhancedcelestials.api.entityfilter;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobCategory;

public record MobCategoryFilter(MobCategory category) implements EntityFilter {

    public static final Codec<MobCategoryFilter> CODEC = RecordCodecBuilder.create(builder ->
            builder.group(
                    MobCategory.CODEC.fieldOf("mob_category").forGetter(MobCategoryFilter::category)
            ).apply(builder, MobCategoryFilter::new)
    );

    @Override
    public Codec<? extends EntityFilter> codec() {
        return CODEC;
    }

    @Override
    public boolean filter(LivingEntity entity) {
        return entity.getType().getCategory() == this.category;
    }
}
