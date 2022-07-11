package corgitaco.enhancedcelestials.api.lunarevent;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import corgitaco.enhancedcelestials.api.entityfilter.EntityFilter;
import net.minecraft.world.entity.MobCategory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public record LunarMobSettings(Map<MobCategory, Double> spawnCategoryMultiplier, LunarMobSpawnInfo lunarMobSpawnInfo, List<Pair<EntityFilter, MobEffectInstanceBuilder>> effectsForEntityTag) {

    public static final Codec<LunarMobSettings> CODEC = RecordCodecBuilder.create(builder ->
            builder.group(
                    Codec.unboundedMap(MobCategory.CODEC, Codec.DOUBLE).fieldOf("spawnCategoryMultiplier").forGetter((clientSettings) -> clientSettings.spawnCategoryMultiplier),
                    LunarMobSpawnInfo.CODEC.fieldOf("lunarSpawnSettings").forGetter((clientSettings) -> clientSettings.lunarMobSpawnInfo),
                    Codec.pair(EntityFilter.CODEC, MobEffectInstanceBuilder.CODEC).listOf().fieldOf("mob_effects").forGetter(lunarMobSettings -> lunarMobSettings.effectsForEntityTag)
            ).apply(builder, LunarMobSettings::new)
    );

    public static final LunarMobSettings DEFAULT = new LunarMobSettings(new HashMap<>(), LunarMobSpawnInfo.DEFAULT, new ArrayList<>());
}
