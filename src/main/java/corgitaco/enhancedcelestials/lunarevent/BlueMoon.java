package corgitaco.enhancedcelestials.lunarevent;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import corgitaco.enhancedcelestials.api.lunarevent.LunarEvent;
import corgitaco.enhancedcelestials.api.lunarevent.LunarTextComponents;
import corgitaco.enhancedcelestials.api.lunarevent.client.LunarEventClientSettings;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

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
        this.luckStrength = MathHelper.clamp(luckStrength - 1, 0, 255);
    }

    @Override
    public void livingEntityTick(LivingEntity entity, World world) {
        if (!world.isRemote) {
            if (!(entity instanceof MonsterEntity)) {
                entity.addPotionEffect(new EffectInstance(Effects.LUCK, 1210, this.luckStrength, true, false, false));
            }
        }
    }

    @Override
    public Codec<? extends LunarEvent> codec() {
        return CODEC;
    }
}
