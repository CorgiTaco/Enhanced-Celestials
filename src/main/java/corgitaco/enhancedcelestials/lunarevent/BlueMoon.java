package corgitaco.enhancedcelestials.lunarevent;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import corgitaco.enhancedcelestials.api.lunarevent.LunarEvent;
import corgitaco.enhancedcelestials.api.lunarevent.LunarTextComponents;
import corgitaco.enhancedcelestials.api.lunarevent.client.LunarEventClientSettings;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.Collection;

public class BlueMoon extends LunarEvent {

    public static final Codec<BlueMoon> CODEC = RecordCodecBuilder.create((builder) -> {
        return builder.group(LunarEventClientSettings.CODEC.fieldOf("clientSettings").forGetter((clientSettings) -> {
            return clientSettings.getClientSettings();
        }), Codec.INT.fieldOf("minNumberOfNightsBetween").forGetter((clientSettings) -> {
            return clientSettings.getMinNumberOfNightsBetween();
        }), Codec.DOUBLE.fieldOf("chance").forGetter((clientSettings) -> {
            return clientSettings.getChance();
        }), Codec.list(Codec.INT).fieldOf("validMoonPhases").forGetter((clientSettings) -> {
            return new ArrayList<>(clientSettings.getValidMoonPhases());
        }), LunarTextComponents.CODEC.fieldOf("textComponents").forGetter((blueMoon) -> {
            return blueMoon.getTextComponents();
        }), Codec.BOOL.fieldOf("blockSleeping").forGetter((clientSettings) -> {
            return clientSettings.blockSleeping();
        }), Codec.INT.fieldOf("luckStrength").forGetter((blueMoon -> {
            return blueMoon.luckStrength + 1;
        }))).apply(builder, BlueMoon::new);
    });
    private final int luckStrength;

    public BlueMoon(LunarEventClientSettings clientSettings, int minNumberOfNightsBetween, double chance, Collection<Integer> validMoonPhases, LunarTextComponents lunarTextComponents, boolean blockSleeping, int luckStrength) {
        super(clientSettings, minNumberOfNightsBetween, chance, validMoonPhases, lunarTextComponents, blockSleeping);
        this.luckStrength = Mth.clamp(luckStrength - 1, 0, 255);
    }

    @Override
    public void livingEntityTick(LivingEntity entity, Level world) {
        if (!world.isClientSide) {
            if (!(entity instanceof Monster)) {
                entity.addEffect(new MobEffectInstance(MobEffects.LUCK, 1210, this.luckStrength, true, false, false));
            }
        }
    }

    @Override
    public Codec<? extends LunarEvent> codec() {
        return CODEC;
    }
}
