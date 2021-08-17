package corgitaco.enhancedcelestials.lunarevent;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import corgitaco.enhancedcelestials.api.lunarevent.LunarEvent;
import corgitaco.enhancedcelestials.api.lunarevent.LunarMobSpawnInfo;
import corgitaco.enhancedcelestials.api.lunarevent.client.LunarEventClientSettings;
import corgitaco.enhancedcelestials.util.CustomTranslationTextComponent;
import it.unimi.dsi.fastutil.objects.Object2DoubleArrayMap;
import net.minecraft.entity.EntityClassification;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

public class BloodMoon extends LunarEvent {

    public static final Codec<BloodMoon> CODEC = RecordCodecBuilder.create((builder) -> {
        return builder.group(LunarEventClientSettings.CODEC.fieldOf("clientSettings").forGetter((clientSettings) -> {
            return clientSettings.getClientSettings();
        }), Codec.INT.fieldOf("minNumberOfNightsBetween").forGetter((clientSettings) -> {
            return clientSettings.getMinNumberOfNightsBetween();
        }), Codec.DOUBLE.fieldOf("chance").forGetter((clientSettings) -> {
            return clientSettings.getChance();
        }), Codec.list(Codec.INT).fieldOf("validMoonPhases").forGetter((clientSettings) -> {
            return new ArrayList<>(clientSettings.getValidMoonPhases());
        }), CustomTranslationTextComponent.CODEC.optionalFieldOf("startNotification", CustomTranslationTextComponent.DEFAULT).forGetter((clientSettings) -> {
            return clientSettings.startNotification();
        }), CustomTranslationTextComponent.CODEC.optionalFieldOf("endNotificationLangKey", CustomTranslationTextComponent.DEFAULT).forGetter((clientSettings) -> {
            return clientSettings.endNotification();
        }), Codec.unboundedMap(EntityClassification.CODEC, Codec.DOUBLE).fieldOf("spawnCategoryMultiplier").forGetter((clientSettings) -> {
            return clientSettings.spawnCategoryMultiplier;
        }), LunarMobSpawnInfo.CODEC.fieldOf("lunarSpawnSettings").forGetter((clientSettings) -> {
            return clientSettings.lunarMobSpawnInfo;
        })).apply(builder, BloodMoon::new);
    });
    private final Object2DoubleArrayMap<EntityClassification> spawnCategoryMultiplier;
    private final LunarMobSpawnInfo lunarMobSpawnInfo;

    public BloodMoon(LunarEventClientSettings clientSettings, int minNumberOfNightsBetween, double chance, Collection<Integer> validMoonPhases, CustomTranslationTextComponent startNotificationLangKey, CustomTranslationTextComponent endNotificationLangKey, Map<EntityClassification, Double> spawnCategoryMultiplier, LunarMobSpawnInfo lunarMobSpawnInfo) {
        super(clientSettings, minNumberOfNightsBetween, chance, validMoonPhases, startNotificationLangKey, endNotificationLangKey);
        this.spawnCategoryMultiplier = new Object2DoubleArrayMap<>(spawnCategoryMultiplier);
        this.lunarMobSpawnInfo = lunarMobSpawnInfo;
    }

    @Override
    public double getSpawnMultiplierForMonsterCategory(EntityClassification classification) {
        return this.spawnCategoryMultiplier.getDouble(classification);
    }

    @Override
    public Codec<? extends LunarEvent> codec() {
        return CODEC;
    }

    @Override
    public LunarMobSpawnInfo getLunarSpawner() {
        return this.lunarMobSpawnInfo;
    }
}
