package corgitaco.enhancedcelestials.api.lunarevent;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import corgitaco.enhancedcelestials.api.entityfilter.AnyEntityFilter;
import corgitaco.enhancedcelestials.api.entityfilter.EntityFilter;
import corgitaco.enhancedcelestials.api.entityfilter.InverseEntityFilter;
import net.minecraft.world.entity.MobCategory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public record LunarMobSettings(Map<MobCategory, Double> spawnCategoryMultiplier, LunarMobSpawnInfo lunarMobSpawnInfo, List<Pair<EntityFilter, MobEffectInstanceBuilder>> effectsForEntityTag, EntityFilter blockSleeping) {

    public static final Codec<LunarMobSettings> CODEC = RecordCodecBuilder.create(builder ->
            builder.group(
                    Codec.unboundedMap(MobCategory.CODEC, Codec.DOUBLE).fieldOf("spawn_category_multiplier").forGetter((clientSettings) -> clientSettings.spawnCategoryMultiplier),
                    LunarMobSpawnInfo.CODEC.fieldOf("lunar_spawn_settings").forGetter((clientSettings) -> clientSettings.lunarMobSpawnInfo),
                    Codec.pair(EntityFilter.CODEC, MobEffectInstanceBuilder.CODEC).listOf().fieldOf("mob_effects").forGetter(lunarMobSettings -> lunarMobSettings.effectsForEntityTag),
                    EntityFilter.CODEC.fieldOf("blocks_sleeping").forGetter(lunarMobSettings -> lunarMobSettings.blockSleeping)
            ).apply(builder, LunarMobSettings::new)
    );

    public static final LunarMobSettings DEFAULT = new LunarMobSettings(new HashMap<>(), LunarMobSpawnInfo.DEFAULT, new ArrayList<>(), new InverseEntityFilter(AnyEntityFilter.INSTANCE));
}
