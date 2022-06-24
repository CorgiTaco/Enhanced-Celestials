package corgitaco.enhancedcelestials.api.lunarevent;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import corgitaco.enhancedcelestials.api.EnhancedCelestialsRegistry;
import corgitaco.enhancedcelestials.api.lunarevent.client.LunarEventClientSettings;
import it.unimi.dsi.fastutil.ints.IntArraySet;
import net.minecraft.core.Holder;
import net.minecraft.resources.RegistryFileCodec;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

public class LunarEvent {

    public static final Codec<LunarEvent> DIRECT_CODEC = RecordCodecBuilder.create(builder ->
            builder.group(LunarEventClientSettings.CODEC.fieldOf("clientSettings").forGetter(LunarEvent::getClientSettings),
                    Codec.list(Codec.INT).fieldOf("validMoonPhases").forGetter((clientSettings) -> new ArrayList<>(clientSettings.getValidMoonPhases())),
                    LunarTextComponents.CODEC.fieldOf("textComponents").forGetter(LunarEvent::getTextComponents),
                    Codec.BOOL.fieldOf("blockSleeping").forGetter(LunarEvent::blockSleeping),
                    LunarMobSettings.CODEC.fieldOf("mob_settings").forGetter(LunarEvent::getLunarMobSettings),
                    DropSettings.CODEC.fieldOf("drops").forGetter(LunarEvent::getDropSettings)
            ).apply(builder, LunarEvent::new)
    );

    public static final Codec<Holder<LunarEvent>> CODEC = RegistryFileCodec.create(EnhancedCelestialsRegistry.LUNAR_EVENT_KEY, DIRECT_CODEC);


    private final LunarEventClientSettings clientSettings;
    private final Set<Integer> validMoonPhases;
    private final LunarTextComponents textComponents;
    private final boolean blockSleeping;
    private final LunarMobSettings lunarMobSettings;
    private final DropSettings dropSettings;

    public LunarEvent(LunarEventClientSettings clientSettings, Collection<Integer> validMoonPhases, LunarTextComponents textComponents, boolean blockSleeping, LunarMobSettings lunarMobSettings, DropSettings dropSettings) {
        this.clientSettings = clientSettings;
        this.validMoonPhases = new IntArraySet(validMoonPhases);
        this.textComponents = textComponents;
        this.blockSleeping = blockSleeping;
        this.lunarMobSettings = lunarMobSettings;
        this.dropSettings = dropSettings;
    }

    public void onBlockItemDrop(ServerLevel world, ItemStack itemStack) {
        this.dropSettings.dropEnhancer().forEach((itemTagKey, multiplier) -> {
            if (itemStack.is(itemTagKey)) {
                itemStack.setCount((int) (itemStack.getCount() * multiplier));
            }
        });
    }

    @Nullable
    public LunarTextComponents.Notification startNotification() {
        return this.textComponents.riseNotification().orElse(null);
    }

    @Nullable
    public LunarTextComponents.Notification endNotification() {
        return this.textComponents.setNotification().orElse(null);
    }

    public LunarEventClientSettings getClientSettings() {
        return clientSettings;
    }

    public double getSpawnMultiplierForMonsterCategory(MobCategory classification) {
        return this.lunarMobSettings.spawnCategoryMultiplier().getOrDefault(classification, 1.0D);
    }

    public void livingEntityTick(LivingEntity entity, Level world) {
        this.lunarMobSettings.effectsForEntityTag().forEach((entityTypeTagKey, mobEffectDoubleMap) -> {
            if (entity.getType().is(entityTypeTagKey)) {
                mobEffectDoubleMap.forEach((mobEffect, intProvider) -> {
                    entity.addEffect(new MobEffectInstance(MobEffects.LUCK, 1210, intProvider.sample(world.random), true, false, false));
                });
            }
        });
    }

    @Nullable
    public LunarMobSpawnInfo getLunarSpawner() {
        return null;
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

    public LunarMobSettings getLunarMobSettings() {
        return lunarMobSettings;
    }

    public DropSettings getDropSettings() {
        return dropSettings;
    }
}
