package corgitaco.enhancedcelestials.api.lunarevent;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import corgitaco.corgilib.entity.condition.AnyCondition;
import corgitaco.corgilib.entity.condition.Condition;
import corgitaco.corgilib.entity.condition.FlipCondition;
import net.minecraft.world.entity.MobCategory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public record LunarMobSettings(Map<MobCategory, Double> spawnCategoryMultiplier, LunarMobSpawnInfo lunarMobSpawnInfo,
                               List<Pair<Condition, MobEffectInstanceBuilder>> effectsForEntityTag,
                               Condition blockSleeping) {

    public static final Codec<LunarMobSettings> CODEC = RecordCodecBuilder.create(builder ->
            builder.group(
                    Codec.unboundedMap(MobCategory.CODEC, Codec.DOUBLE).fieldOf("spawn_category_multiplier").forGetter((clientSettings) -> clientSettings.spawnCategoryMultiplier),
                    LunarMobSpawnInfo.CODEC.fieldOf("lunar_spawn_settings").forGetter((clientSettings) -> clientSettings.lunarMobSpawnInfo),
                    Codec.pair(Condition.CODEC.fieldOf("filter").codec(), MobEffectInstanceBuilder.CODEC.fieldOf("mob_effect").codec()).listOf().fieldOf("mob_effects").forGetter(lunarMobSettings -> lunarMobSettings.effectsForEntityTag),
                    Condition.CODEC.fieldOf("blocks_sleeping").forGetter(lunarMobSettings -> lunarMobSettings.blockSleeping)
            ).apply(builder, LunarMobSettings::new)
    );

    public static final LunarMobSettings DEFAULT = new LunarMobSettings(new HashMap<>(), LunarMobSpawnInfo.DEFAULT, new ArrayList<>(), new FlipCondition(AnyCondition.INSTANCE));
}
