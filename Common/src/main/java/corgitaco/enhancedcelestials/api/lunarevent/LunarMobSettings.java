package corgitaco.enhancedcelestials.api.lunarevent;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import corgitaco.enhancedcelestials.util.CodecUtil;
import net.minecraft.core.Registry;
import net.minecraft.tags.TagKey;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

import java.util.HashMap;
import java.util.Map;

public record LunarMobSettings(Map<MobCategory, Double> spawnCategoryMultiplier, LunarMobSpawnInfo lunarMobSpawnInfo, Map<TagKey<EntityType<?>>, Map<MobEffect, IntProvider>> effectsForEntityTag) {

    public static final Codec<LunarMobSettings> CODEC = RecordCodecBuilder.create(builder ->
            builder.group(
                    Codec.unboundedMap(MobCategory.CODEC, Codec.DOUBLE).fieldOf("spawnCategoryMultiplier").forGetter((clientSettings) -> clientSettings.spawnCategoryMultiplier),
                    LunarMobSpawnInfo.CODEC.fieldOf("lunarSpawnSettings").forGetter((clientSettings) -> clientSettings.lunarMobSpawnInfo),
                    Codec.unboundedMap(TagKey.codec(Registry.ENTITY_TYPE_REGISTRY), Codec.unboundedMap(CodecUtil.MOB_EFFECT, IntProvider.NON_NEGATIVE_CODEC)).fieldOf("mobEffects").forGetter(lunarMobSettings -> lunarMobSettings.effectsForEntityTag)
            ).apply(builder, LunarMobSettings::new)
    );

    public static final LunarMobSettings DEFAULT = new LunarMobSettings(new HashMap<>(), LunarMobSpawnInfo.DEFAULT, new HashMap<>());
}
