package corgitaco.enhancedcelestials.lunarevent;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import corgitaco.enhancedcelestials.api.lunarevent.LunarEvent;
import corgitaco.enhancedcelestials.api.lunarevent.client.LunarEventClientSettings;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.Collection;

public class BlueMoon extends LunarEvent {

    public static final Codec<BlueMoon> CODEC = RecordCodecBuilder.create((builder) -> {
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
        })).apply(builder, BlueMoon::new);
    });

    public BlueMoon(LunarEventClientSettings clientSettings, boolean superMoon, int minNumberOfNightsBetween, double chance, Collection<Integer> validMoonPhases, String startNotificationLangKey, String endNotificationLangKey) {
        super(clientSettings, superMoon, minNumberOfNightsBetween, chance, validMoonPhases, startNotificationLangKey, endNotificationLangKey);
    }

    @Override
    public void livingEntityTick(LivingEntity entity, World world) {
        if (!world.isRemote) {
            if (!(entity instanceof MonsterEntity)) {
                entity.addPotionEffect(new EffectInstance(Effects.LUCK, 1200, this.isSuperMoon() ? 4 : 0, true, false, false));
            }
        }
    }

    @Override
    public Codec<? extends LunarEvent> codec() {
        return CODEC;
    }
}
