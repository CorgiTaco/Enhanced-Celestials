package corgitaco.enhancedcelestials.api.lunarevent;

import com.mojang.serialization.Codec;
import corgitaco.enhancedcelestials.api.EnhancedCelestialsRegistry;
import corgitaco.enhancedcelestials.api.lunarevent.client.LunarEventClient;
import corgitaco.enhancedcelestials.api.lunarevent.client.LunarEventClientSettings;
import it.unimi.dsi.fastutil.ints.IntArraySet;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Set;
import java.util.function.Function;

public abstract class LunarEvent {

    public static final Codec<LunarEvent> CODEC = EnhancedCelestialsRegistry.LUNAR_EVENT.dispatchStable(LunarEvent::codec, Function.identity());

    private LunarEventClientSettings clientSettings;
    private final boolean superMoon;
    private final int minNumberOfNightsBetween;
    private final double chance;
    private final Set<Integer> validMoonPhases;
    private LunarEventClient<?> lunarEventClient;
    private String name;

    public LunarEvent(LunarEventClientSettings clientSettings, boolean superMoon, int minNumberOfNightsBetween, double chance, Collection<Integer> validMoonPhases) {
        this.clientSettings = clientSettings;
        this.superMoon = superMoon;
        this.minNumberOfNightsBetween = minNumberOfNightsBetween;
        this.chance = chance;
        this.validMoonPhases = new IntArraySet(validMoonPhases);
    }

    public abstract Codec<? extends LunarEvent> codec();

    public void onBlockTick() {
    }

    public void onBlockItemDrop(ServerWorld world, ItemStack itemStack) {
    }

    public String getName() {
        return name;
    }

    public LunarEvent setName(String name) {
        this.name = name;
        return this;
    }

    @Nullable
    public TranslationTextComponent lunarEventStartNotification() {
        return null;
    }

    public LunarEventClientSettings getClientSettings() {
        return clientSettings;
    }

    public void setClientSettings(LunarEventClientSettings clientSettings) {
        this.clientSettings = clientSettings;
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

    public double getSpawnMultiplierForMonsterCategory(EntityClassification classification) {
        return 1.0;
    }

    public void livingEntityTick(LivingEntity entity, World world) {
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
