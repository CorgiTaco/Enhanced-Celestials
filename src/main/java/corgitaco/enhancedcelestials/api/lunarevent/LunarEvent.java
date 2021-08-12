package corgitaco.enhancedcelestials.api.lunarevent;

import com.mojang.serialization.Codec;
import corgitaco.enhancedcelestials.api.EnhancedCelestialsRegistry;
import corgitaco.enhancedcelestials.api.lunarevent.client.LunarEventClient;
import corgitaco.enhancedcelestials.api.lunarevent.client.LunarEventClientSettings;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.Set;
import java.util.function.Function;

public abstract class LunarEvent {

    public static final Codec<LunarEvent> CODEC = EnhancedCelestialsRegistry.LUNAR_EVENT.dispatchStable(LunarEvent::codec, Function.identity());

    private final LunarEventClientSettings clientSettings;
    private final boolean superMoon;
    private final int minNumberOfNightsBetween;
    private final double chance;
    private final Set<Integer> validMoonPhases;
    private LunarEventClient<?> lunarEventClient;

    public LunarEvent(LunarEventClientSettings clientSettings, boolean superMoon, int minNumberOfNightsBetween, double chance, Set<Integer> validMoonPhases) {
        this.clientSettings = clientSettings;
        this.superMoon = superMoon;
        this.minNumberOfNightsBetween = minNumberOfNightsBetween;
        this.chance = chance;
        this.validMoonPhases = validMoonPhases;
    }

    public abstract Codec<? extends LunarEvent> codec();

    public void onBlockTick() {
    }

    @Nullable
    public TranslationTextComponent lunarEventStartNotification() {
        return null;
    }

    public LunarEventClientSettings getClientSettings() {
        return clientSettings;
    }

    @OnlyIn(Dist.CLIENT)
    public LunarEventClient<?> getClient() {
        return this.lunarEventClient;
    }

    @OnlyIn(Dist.CLIENT)
    public LunarEventClient<?> setLunarEventClient(LunarEventClient<?> lunarEventClient) {
        this.lunarEventClient = lunarEventClient;
        return this.lunarEventClient;
    }

    public boolean isSuperMoon() {
        return superMoon;
    }

    public int getMinNumberOfNightsBetween() {
        return minNumberOfNightsBetween;
    }

    public double getChance() {
        return chance;
    }

    public Set<Integer> getValidMoonPhases() {
        return validMoonPhases;
    }
}
