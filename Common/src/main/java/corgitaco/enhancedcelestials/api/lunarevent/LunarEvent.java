package corgitaco.enhancedcelestials.api.lunarevent;

import com.mojang.serialization.Codec;
import corgitaco.enhancedcelestials.api.EnhancedCelestialsRegistry;
import corgitaco.enhancedcelestials.api.lunarevent.client.LunarEventClient;
import corgitaco.enhancedcelestials.api.lunarevent.client.LunarEventClientSettings;
import it.unimi.dsi.fastutil.ints.IntArraySet;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Set;
import java.util.function.Function;

public abstract class LunarEvent {

    public static final Codec<LunarEvent> CODEC = EnhancedCelestialsRegistry.LUNAR_EVENT.byNameCodec().dispatchStable(LunarEvent::codec, Function.identity());

    private LunarEventClientSettings clientSettings;
    private final int minNumberOfNightsBetween;
    private final double chance;
    private final Set<Integer> validMoonPhases;
    private final LunarTextComponents textComponents;
    private final boolean blockSleeping;
    private LunarEventClient<?> lunarEventClient;
    private String key;

    public LunarEvent(LunarEventClientSettings clientSettings, int minNumberOfNightsBetween, double chance, Collection<Integer> validMoonPhases, LunarTextComponents textComponents, boolean blockSleeping) {
        this.clientSettings = clientSettings;
        this.minNumberOfNightsBetween = minNumberOfNightsBetween;
        this.chance = chance;
        this.validMoonPhases = new IntArraySet(validMoonPhases);
        this.textComponents = textComponents;
        this.blockSleeping = blockSleeping;
    }

    public abstract Codec<? extends LunarEvent> codec();

    public void onBlockTick() {
    }

    public void onBlockItemDrop(ServerLevel world, ItemStack itemStack) {
    }

    public String getKey() {
        return key;
    }

    public LunarEvent setKey(String key) {
        this.key = key;
        return this;
    }

    @Nullable
    public LunarTextComponents.Notification startNotification() {
        return this.textComponents.getRiseNotification();
    }

    @Nullable
    public LunarTextComponents.Notification endNotification() {
        return this.textComponents.getSetNotification();
    }

    public LunarEventClientSettings getClientSettings() {
        return clientSettings;
    }

    public void setClientSettings(LunarEventClientSettings clientSettings) {
        this.clientSettings = clientSettings;
    }

    public LunarEventClient<?> getClient() {
        return this.lunarEventClient;
    }

    public LunarEventClient<?> setLunarEventClient(LunarEventClient<?> lunarEventClient, String path) {
        if (lunarEventClient == null) {
            throw new NullPointerException("Client settings for file \"" + path + "\" are incomplete/broken. The fastest solution is to copy this file to a separate directory and update the settings in the new file for all relevant fields.");
        }
        this.lunarEventClient = lunarEventClient;
        return this.lunarEventClient;
    }

    public double getSpawnMultiplierForMonsterCategory(MobCategory classification) {
        return 1.0;
    }

    public void livingEntityTick(LivingEntity entity, Level world) {
    }

    @Nullable
    public LunarMobSpawnInfo getLunarSpawner() {
        return null;
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

    public boolean blockSleeping() {
        return blockSleeping;
    }

    public LunarTextComponents getTextComponents() {
        return textComponents;
    }
}
