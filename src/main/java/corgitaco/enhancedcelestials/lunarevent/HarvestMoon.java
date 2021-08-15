package corgitaco.enhancedcelestials.lunarevent;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import corgitaco.enhancedcelestials.api.lunarevent.LunarEvent;
import corgitaco.enhancedcelestials.api.lunarevent.client.LunarEventClientSettings;

import java.util.ArrayList;
import java.util.Collection;

public class HarvestMoon extends LunarEvent {

    public static final Codec<HarvestMoon> CODEC = RecordCodecBuilder.create((builder) -> {
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
        }), Codec.STRING.fieldOf("startNotificationLangKey").forGetter((clientSettings) -> {
            return clientSettings.getStartNotificationLangKey();
        }), Codec.STRING.fieldOf("endNotificationLangKey").orElse("").forGetter((clientSettings) -> {
            return clientSettings.getEndNotificationLangKey();
        })).apply(builder, HarvestMoon::new);
    });

    public HarvestMoon(LunarEventClientSettings clientSettings, boolean superMoon, int minNumberOfNightsBetween, double chance, Collection<Integer> validMoonPhases, String startNotificationLangKey, String endNotificationLangKey) {
        super(clientSettings, superMoon, minNumberOfNightsBetween, chance, validMoonPhases, startNotificationLangKey, endNotificationLangKey);
    }

    @Override
    public Codec<? extends LunarEvent> codec() {
        return CODEC;
    }
}
