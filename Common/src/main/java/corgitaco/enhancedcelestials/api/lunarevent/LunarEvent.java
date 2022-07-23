package corgitaco.enhancedcelestials.api.lunarevent;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import corgitaco.enhancedcelestials.api.EnhancedCelestialsRegistry;
import corgitaco.enhancedcelestials.api.entityfilter.EntityFilter;
import corgitaco.enhancedcelestials.api.lunarevent.client.LunarEventClientSettings;
import it.unimi.dsi.fastutil.ints.IntArraySet;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.resources.RegistryFileCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

public class LunarEvent {

    public static final Codec<LunarEvent> DIRECT_CODEC = RecordCodecBuilder.create(builder ->
            builder.group(
                    Codec.unboundedMap(ResourceKey.codec(Registry.DIMENSION_REGISTRY), ChanceEntry.CODEC).fieldOf("dimension_chances").forGetter(LunarEvent::getEventChancesByDimension),
                    LunarEventClientSettings.CODEC.fieldOf("client_settings").forGetter(LunarEvent::getClientSettings),
                    Codec.list(Codec.intRange(0, 8)).fieldOf("valid_moon_phase").forGetter((clientSettings) -> new ArrayList<>(clientSettings.getValidMoonPhases())),
                    LunarTextComponents.CODEC.fieldOf("text_components").forGetter(LunarEvent::getTextComponents),
                    LunarMobSettings.CODEC.fieldOf("mob_settings").forGetter(LunarEvent::getLunarMobSettings),
                    DropSettings.CODEC.fieldOf("drops").forGetter(LunarEvent::getDropSettings)
            ).apply(builder, LunarEvent::new)
    );

    public static final Codec<Holder<LunarEvent>> CODEC = RegistryFileCodec.create(EnhancedCelestialsRegistry.LUNAR_EVENT_KEY, DIRECT_CODEC);


    private final Map<ResourceKey<Level>, ChanceEntry> eventChancesByDimension;
    private final LunarEventClientSettings clientSettings;
    private final Set<Integer> validMoonPhases;
    private final LunarTextComponents textComponents;
    private final LunarMobSettings lunarMobSettings;
    private final DropSettings dropSettings;

    public LunarEvent(Map<ResourceKey<Level>, ChanceEntry> eventChancesByDimension, LunarEventClientSettings clientSettings, Collection<Integer> validMoonPhases, LunarTextComponents textComponents, LunarMobSettings lunarMobSettings, DropSettings dropSettings) {
        this.eventChancesByDimension = eventChancesByDimension;
        this.clientSettings = clientSettings;
        this.validMoonPhases = new IntArraySet(validMoonPhases);
        this.textComponents = textComponents;
        this.lunarMobSettings = lunarMobSettings;
        this.dropSettings = dropSettings;
    }

    public void onBlockItemDrop(ServerLevel world, ItemStack itemStack) {
        this.dropSettings.dropEnhancer().forEach((itemTagKey, multiplier) -> {
            if (itemStack.is(itemTagKey)) {
                itemStack.setCount((int) Math.round(itemStack.getCount() * multiplier));
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

    public void livingEntityTick(LivingEntity entity) {
        this.lunarMobSettings.effectsForEntityTag().forEach((entityFilterMapPair) -> {
            EntityFilter entityFilter = entityFilterMapPair.getFirst();
            if (entityFilter.filter(entity)) {
                MobEffectInstanceBuilder builder = entityFilterMapPair.getSecond();
                entity.addEffect(builder.makeInstance());
            }
        });
    }

    public LunarMobSpawnInfo getLunarSpawner() {
        return this.lunarMobSettings.lunarMobSpawnInfo();
    }

    public Set<Integer> getValidMoonPhases() {
        return validMoonPhases;
    }

    public boolean blockSleeping(LivingEntity entity) {
        return this.lunarMobSettings.blockSleeping().filter(entity);
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

    public Map<ResourceKey<Level>, ChanceEntry> getEventChancesByDimension() {
        return eventChancesByDimension;
    }

    public record ChanceEntry(double chance, int minNumberOfNights) {
        public static final Codec<ChanceEntry> CODEC = RecordCodecBuilder.create(builder ->
                builder.group(
                        Codec.DOUBLE.fieldOf("chance").forGetter(ChanceEntry::chance),
                        Codec.INT.fieldOf("min_number_of_nights_between").forGetter(ChanceEntry::minNumberOfNights)
                ).apply(builder, ChanceEntry::new)
        );
    }
}
