package corgitaco.enhancedcelestials.lunarevent;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import corgitaco.enhancedcelestials.api.lunarevent.LunarEvent;
import corgitaco.enhancedcelestials.api.lunarevent.client.LunarEventClientSettings;
import it.unimi.dsi.fastutil.objects.Object2DoubleArrayMap;
import net.minecraft.entity.EntityClassification;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

public class BloodMoon extends LunarEvent {

    public static final Codec<BloodMoon> CODEC = RecordCodecBuilder.create((builder) -> {
        return builder.group(LunarEventClientSettings.CODEC.fieldOf("clientSettings").forGetter((clientSettings) -> {
            return clientSettings.getClientSettings();
        }), Codec.BOOL.fieldOf("superMoon").forGetter((clientSettings) -> {
            return clientSettings.isSuperMoon();
        }), Codec.INT.fieldOf("minNumberOfNightsBetween").forGetter((clientSettings) -> {
            return clientSettings.getMinNumberOfNightsBetween();
        }), Codec.DOUBLE.fieldOf("chance").forGetter((clientSettings) -> {
            return clientSettings.getChance();
        }), Codec.list(Codec.INT).fieldOf("validMoonPhases").forGetter((clientSettings) -> {
            return new ArrayList<>(clientSettings.getValidMoonPhases());
        }), Codec.unboundedMap(EntityClassification.CODEC, Codec.DOUBLE).fieldOf("spawnCategoryMultiplier").forGetter((clientSettings) -> {
            return clientSettings.spawnCategoryMultiplier;
        })).apply(builder, BloodMoon::new);
    });
    private final Object2DoubleArrayMap<EntityClassification> spawnCategoryMultiplier;

    public BloodMoon(LunarEventClientSettings clientSettings, boolean superMoon, int minNumberOfNightsBetween, double chance, Collection<Integer> validMoonPhases, Map<EntityClassification, Double> spawnCategoryMultiplier) {
        super(clientSettings, superMoon, minNumberOfNightsBetween, chance, validMoonPhases);
        this.spawnCategoryMultiplier = new Object2DoubleArrayMap<>(spawnCategoryMultiplier);
    }

    @Override
    public double getSpawnMultiplierForMonsterCategory(EntityClassification classification) {
        return this.spawnCategoryMultiplier.getDouble(classification);
    }

    @Override
    public Codec<? extends LunarEvent> codec() {
        return CODEC;
    }
}
