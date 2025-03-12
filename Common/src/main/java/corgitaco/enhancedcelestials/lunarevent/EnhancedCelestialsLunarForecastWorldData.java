package corgitaco.enhancedcelestials.lunarevent;

import com.mojang.serialization.Codec;
import corgitaco.enhancedcelestials.EnhancedCelestials;
import corgitaco.enhancedcelestials.api.EnhancedCelestialsRegistry;
import corgitaco.enhancedcelestials.api.lunarevent.LunarDimensionSettings;
import corgitaco.enhancedcelestials.api.lunarevent.LunarEvent;
import corgitaco.enhancedcelestials.api.lunarevent.LunarTextComponents;
import corgitaco.enhancedcelestials.util.CustomTranslationTextComponent;
import dev.corgitaco.dataanchor.data.TickableTrackedData;
import dev.corgitaco.dataanchor.data.registry.TrackedDataKey;
import dev.corgitaco.dataanchor.data.type.level.SyncedLevelTrackedData;
import it.unimi.dsi.fastutil.objects.Object2LongArrayMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Predicate;

public class EnhancedCelestialsLunarForecastWorldData extends SyncedLevelTrackedData implements TickableTrackedData {

    protected final List<LunarEventInstance> forecast = new ArrayList<>();
    protected final List<LunarEventInstance> pastEvents = new ArrayList<>();
    private long lastCheckedDay = -1L;

    private boolean shouldSync = false;

    protected transient final Holder<LunarDimensionSettings> dimensionSettingsHolder;
    protected transient final Map<Holder<LunarEvent>, LunarEvent.SpawnRequirements> lunarEventSpawnRequirements;
    private transient Holder<LunarEvent> lastTickEvent;
    private transient Holder<LunarEvent> lastStoredEvent;
    protected transient float blend = 1F;

    public EnhancedCelestialsLunarForecastWorldData(TrackedDataKey<EnhancedCelestialsLunarForecastWorldData> key, Level level, Holder<LunarDimensionSettings> lunarDimensionSettingsHolder, Map<Holder<LunarEvent>, LunarEvent.SpawnRequirements> lunarEventSpawnRequirementsMap) {
        super(key, level);
        this.dimensionSettingsHolder = lunarDimensionSettingsHolder;
        this.lunarEventSpawnRequirements = lunarEventSpawnRequirementsMap;
        this.lastTickEvent = currentLunarEventHolder();
        this.lastStoredEvent = currentLunarEventHolder();
        String lunarEventNames = Arrays.toString(lunarEventSpawnRequirements.keySet().stream().map(Holder::unwrapKey).map(Optional::orElseThrow).map(ResourceKey::location).map(ResourceLocation::toString).toArray());
        String dimension = level.dimension().location().toString();
        EnhancedCelestials.LOGGER.info("Possible lunar events for dimension \"%s\" are %s.".formatted(dimension, lunarEventNames));
    }

    @Override
    public @Nullable CompoundTag save() {
        CompoundTag compoundTag = new CompoundTag();

        compoundTag.put("forecast", LunarEventInstance.LIST_CODEC.encodeStart(NbtOps.INSTANCE, this.forecast).getOrThrow(false, s -> {}));
        compoundTag.put("pastEvents", LunarEventInstance.LIST_CODEC.encodeStart(NbtOps.INSTANCE, this.pastEvents).getOrThrow(false, s -> {}));
        compoundTag.putLong("lastCheckedDay", this.lastCheckedDay);

        return compoundTag;
    }

    @Override
    public void load(CompoundTag tag) {
        this.forecast.clear();
        this.forecast.addAll(LunarEventInstance.LIST_CODEC.decode(NbtOps.INSTANCE, tag.get("forecast")).getOrThrow(false, s -> {}).getFirst());

        this.pastEvents.clear();
        this.pastEvents.addAll(LunarEventInstance.LIST_CODEC.decode(NbtOps.INSTANCE, tag.get("pastEvents")).getOrThrow(false, s -> {}).getFirst());
        this.lastCheckedDay = tag.getLong("lastCheckedDay");
    }

    @Override
    public void readFromNetwork(CompoundTag tag) {
        super.readFromNetwork(tag);

        if (lastTickEvent != currentLunarEvent()) {
            eventSwitched(lastLunarEventHolder(), currentLunarEventHolder());
        }
    }

    @Override
    public void tick() {
        if (!level.isClientSide) {
            serverTick();
        } else {
            baseTick();
        }
    }

    private void serverTick() {
        removeFromForecastIf(lunarEventInstance -> {
            if (lunarEventInstance.passed(getCurrentDay())) {
                this.pastEvents.add(0, lunarEventInstance);
                return true;
            }
            return lunarEventInstance.scheduledDay() > getCurrentDay() + this.dimensionSettingsHolder.value().yearLengthInDays();
        });

        removeFromPastEventsIf(lunarEventInstance -> lunarEventInstance.scheduledDay() > getCurrentDay() || lunarEventInstance.scheduledDay() < getCurrentDay() - this.dimensionSettingsHolder.value().yearLengthInDays());

        baseTick();
        createOrUpdateForecast(lastCheckedDay);
        checkEmptyForecastOrThrow();

        if (shouldSync) {
            super.sync();
            shouldSync = false;
        }
    }

    private void checkEmptyForecastOrThrow() {
        if (this.forecast.isEmpty() && this.dimensionSettingsHolder.value().yearLengthInDays() > this.dimensionSettingsHolder.value().maxDaysBetweenEvents()) {
            throw new IllegalStateException("Forecast cannot be empty.... this should be impossible.... crashing game..... Report this to the Enhanced Celestials Github immediately, please provide your current world instance + other mods.");
        }
    }

    private void baseTick() {
        if (blend < 1F) {
            blend += 0.01F;
        }

        if (currentLunarEventHolder() != lastTickEvent) {
            eventSwitched(lastTickEvent, currentLunarEventHolder());
        }

        if (level.isNight()) {
            if (level.isRaining() && this.dimensionSettingsHolder.value().requiresClearSkies()) {
                lastTickEvent = defaultLunarEvent();
            } else {
                lastTickEvent = getLunarEventForDay(getCurrentDay());
            }
        } else {
            lastTickEvent = defaultLunarEvent();
        }
    }

    public void recomputeForecast() {
        checkServer();
        clearForecast();
        createOrUpdateForecast(getCurrentDay());
    }

    private void clearForecast() {
        this.forecast.clear();
        markChanged();
    }

    public boolean switchingEvents() {
        return blend < 1F;
    }

    public void eventSwitched(Holder<LunarEvent> lastEvent, Holder<LunarEvent> nextEvent) {
        blend = 0;
        lastStoredEvent = lastEvent;
        if (!level.isClientSide) {
            serverEventSwitched(lastEvent, nextEvent);
        }
    }

    public void setLunarEvent(ResourceKey<LunarEvent> lunarEvent) {
        checkServer();

        if (!this.level.isNight()) {
            ((ServerLevel) this.level).setDayTime((getCurrentDay() * this.dimensionSettingsHolder.value().dayLength()) + 13000L);
        }

        LunarEventInstance first = this.forecast.get(0);
        if (first.active(getCurrentDay())) {
            removeEventInForecast(0);
        }
        if (lunarEvent != this.dimensionSettingsHolder.value().defaultEvent()) {
            addEventToForecast(0, new LunarEventInstance(lunarEvent, getCurrentDay(), true));
        }
    }



    private void serverEventSwitched(Holder<LunarEvent> lastEvent, Holder<LunarEvent> nextEvent) {
        checkServer();
        for (Player player : level.players()) {
            lastEvent.value().getTextComponents().setNotification().ifPresent(notification -> {
                if (notification.notificationType() != LunarTextComponents.NotificationType.NONE) {
                    player.displayClientMessage(notification.customTranslationTextComponent().getComponent(), notification.notificationType() == LunarTextComponents.NotificationType.HOT_BAR);
                }
            });

            nextEvent.value().getTextComponents().riseNotification().ifPresent(notification -> {
                if (notification.notificationType() != LunarTextComponents.NotificationType.NONE) {
                    player.displayClientMessage(notification.customTranslationTextComponent().getComponent(), notification.notificationType() == LunarTextComponents.NotificationType.HOT_BAR);
                }
            });
        }
    }



    public LunarEvent lastLunarEvent() {
        return lastLunarEventHolder().value();
    }

    public Holder<LunarEvent> lastLunarEventHolder() {
        return this.lastStoredEvent;
    }

    public LunarEvent currentLunarEvent() {
        return currentLunarEventHolder().value();
    }

    public Holder<LunarEvent> currentLunarEventHolder() {
        if (level.isDay()) {
            return defaultLunarEvent();
        }

        if (this.dimensionSettingsHolder.value().requiresClearSkies()) {
            if (level.isRaining()) {
                return defaultLunarEvent();
            }
        }

        Holder.Reference<LunarEvent> defaultEvent = defaultLunarEvent();
        if (this.forecast.isEmpty()) {
            return defaultEvent;
        }

        LunarEventInstance first = this.forecast.get(0);
        if (first.active(getCurrentDay())) {
            return lunarEventHolder(first.getLunarEventKey());
        }

        return defaultEvent;
    }

    public Holder<LunarEvent> nextScheduledLunarEvent() {
        if (this.forecast.isEmpty()) {
            return defaultLunarEvent();
        }

        LunarEventInstance first = this.forecast.get(0);
        if (first.active(getCurrentDay())) {
            LunarEventInstance second = this.forecast.get(1);
            return lunarEventHolder(second.getLunarEventKey());
        } else {
            return lunarEventHolder(first.getLunarEventKey());
        }
    }

    public Holder<LunarEvent> lastScheduledLunarEvent() {
        Holder.Reference<LunarEvent> defaultEvent = defaultLunarEvent();
        if (this.pastEvents.isEmpty()) {
            return defaultEvent;
        }

        LunarEventInstance first = this.pastEvents.get(0);
        if (first.active(getCurrentDay())) {
            return lunarEventHolder(first.getLunarEventKey());
        }

        return defaultEvent;
    }

    public Holder<LunarEvent> getLunarEventForDay(long day) {
        for (LunarEventInstance lunarEventInstance : this.forecast) {
            if (lunarEventInstance.active(day)) {
                return lunarEventHolder(lunarEventInstance.getLunarEventKey());
            }
        }
        for (LunarEventInstance lunarEventInstance : this.pastEvents) {
            if (lunarEventInstance.active(day)) {
                return lunarEventHolder(lunarEventInstance.getLunarEventKey());
            }
        }

        return defaultLunarEvent();
    }

    /**
     * @return A forecast text component to display in the chat showing up to the next 100 events.
     */
    public Component getForecastComponent() {
        MutableComponent textComponent = null;

        for (int i = Math.min(100, this.forecast.size() - 1); i >= 0; i--) {
            LunarEventInstance lunarEventInstance = this.forecast.get(i);
            Holder<LunarEvent> event = lunarEventHolder(lunarEventInstance.getLunarEventKey());
            CustomTranslationTextComponent name = event.value().getTextComponents().name();
            TextColor color = name.getStyle().getColor();
            if (textComponent == null) {
                textComponent = Component.translatable(name.getKey()).withStyle(Style.EMPTY.withColor(color));
            } else {
                textComponent.append(Component.literal(", ").withStyle(Style.EMPTY.withColor(ChatFormatting.WHITE))).append(Component.translatable(name.getKey()).withStyle(Style.EMPTY.withColor(color)));
            }
            textComponent.append(Component.translatable("enhancedcelestials.lunarforecast.days_left", lunarEventInstance.getDaysUntil(getCurrentDay())).withStyle(Style.EMPTY.withColor(color)));
        }

        if (textComponent != null) {
            return Component.translatable("enhancedcelestials.lunarforecast.header", textComponent.append(Component.literal(".").withStyle(Style.EMPTY.withColor(ChatFormatting.WHITE))));
        } else {
            return Component.translatable("enhancedcelestials.lunarforecast.empty", textComponent).withStyle(ChatFormatting.YELLOW);
        }

    }

    private Holder.Reference<LunarEvent> defaultLunarEvent() {
        return lunarEventHolder(this.dimensionSettingsHolder.value().defaultEvent());
    }

    private Holder.Reference<LunarEvent> lunarEventHolder(ResourceKey<LunarEvent> lunarEventKey) {
        return level.registryAccess().registry(EnhancedCelestialsRegistry.LUNAR_EVENT_KEY).orElseThrow().getHolderOrThrow(lunarEventKey);
    }


    public Object2LongArrayMap<ResourceKey<LunarEvent>> eventsByDay() {
        Object2LongArrayMap<ResourceKey<LunarEvent>> eventByLastTime = new Object2LongArrayMap<>();

        for (LunarEventInstance lunarEventInstance : this.pastEvents) {
            eventByLastTime.put(lunarEventInstance.getLunarEventKey(), lunarEventInstance.scheduledDay());
        }

        for (LunarEventInstance lunarEventInstance : this.forecast) {
            eventByLastTime.put(lunarEventInstance.getLunarEventKey(), lunarEventInstance.scheduledDay());
        }

        return eventByLastTime;
    }

    public long lastScheduledEventDay() {
        long lastScheduledEventDay = -1L;

        for (LunarEventInstance lunarEventInstance : this.forecast) {
            lastScheduledEventDay = Math.max(lunarEventInstance.scheduledDay(), lastScheduledEventDay);
        }

        for (LunarEventInstance lunarEventInstance : this.pastEvents) {
            lastScheduledEventDay = Math.max(lunarEventInstance.scheduledDay(), lastScheduledEventDay);
        }

        return lastScheduledEventDay;
    }


    public long getCurrentDay() {
        return getDayFromDayTime(this.level.getDayTime());
    }

    public long getDayFromDayTime(long dayTime) {
        return dayTime / this.dimensionSettingsHolder.value().dayLength();
    }

    public long getDayTimeFromDay(long day) {
        return day * this.dimensionSettingsHolder.value().dayLength();
    }

    public float getBlend() {
        return blend;
    }

    private void createOrUpdateForecast(long lastCheckedDay) {
        long yearLengthInDays = this.dimensionSettingsHolder.value().yearLengthInDays();

        if (getCurrentDay() < lastCheckedDay - yearLengthInDays) {
            lastCheckedDay = getCurrentDay();
            setLastCheckedDay(lastCheckedDay);
        }

        long dayDifference = clamp(lastCheckedDay - getCurrentDay(), 0L, yearLengthInDays);

        if (dayDifference < yearLengthInDays) {
            Object2LongArrayMap<ResourceKey<LunarEvent>> eventsByDay = eventsByDay();
            long lastScheduledEventDay = lastScheduledEventDay();
            long yearDayDifference = yearLengthInDays - dayDifference;
            for (int dayOffset = 0; dayOffset <= yearDayDifference; dayOffset++) {
                long day = getCurrentDay() + dayDifference + dayOffset;

                long seed = day + ((ServerLevel) level).getSeed() + level.dimension().hashCode();
                Random random = new Random(seed);

                List<Holder<LunarEvent>> scrambledLunarEvents = new ArrayList<>(this.lunarEventSpawnRequirements.keySet());
                Collections.shuffle(scrambledLunarEvents, random);

                for (Holder<LunarEvent> scrambledLunarEvent : scrambledLunarEvents) {
                    LunarEvent.SpawnRequirements spawnRequirements = this.lunarEventSpawnRequirements.get(scrambledLunarEvent);
                    boolean pastMinNumberOfNightsBetweenThisTypeOfEvent = (day - eventsByDay.getOrDefault(scrambledLunarEvent.unwrapKey().orElseThrow(), getCurrentDay())) > spawnRequirements.minNumberOfNights();
                    boolean pastMinNumberOfNightsBetweenAllEvents = (day - lastScheduledEventDay) > this.dimensionSettingsHolder.value().minDaysBetweenEvents();
                    boolean isValidMoonPhase = spawnRequirements.validMoonPhases().contains(this.level.dimensionType().moonPhase(getDayTimeFromDay(day)));
                    boolean chance = spawnRequirements.chance() >= random.nextDouble();
                    boolean checksPass = pastMinNumberOfNightsBetweenThisTypeOfEvent && pastMinNumberOfNightsBetweenAllEvents && isValidMoonPhase && chance;

                    boolean override = !checksPass && lastScheduledEventDay != -1 && day - lastScheduledEventDay >= this.dimensionSettingsHolder.value().maxDaysBetweenEvents();
                    if (checksPass || override) {
                        lastScheduledEventDay = day;
                        LunarEventInstance newLunarEventInstance = new LunarEventInstance(scrambledLunarEvent.unwrapKey().orElseThrow(), day);
                        eventsByDay.put(newLunarEventInstance.getLunarEventKey(), day);
                        forecast.add(newLunarEventInstance);
                    }
                }
            }
            setLastCheckedDay(getCurrentDay() + yearLengthInDays);
        }
    }

    public void setLastCheckedDay(long lastCheckedDay) {
        this.lastCheckedDay = lastCheckedDay;
        markChanged();
    }

    public void addEventToForecast(LunarEventInstance event) {
        this.forecast.add(event);
        markChanged();
    }

    public void addEventToForecast(int idx, LunarEventInstance event) {
        this.forecast.add(idx, event);
        markChanged();
    }

    public void removeEventInForecast(LunarEventInstance event) {
        this.forecast.remove(event);
        markChanged();
    }

    public void removeEventInForecast(int index) {
        this.forecast.remove(index);
        markChanged();
    }

    public void removeFromForecastIf(Predicate<LunarEventInstance> filter) {
        forecast.removeIf(lunarEventInstance -> {
            if (filter.test(lunarEventInstance)) {
                markChanged();
                return true;
            }


            return false;
        });
    }

    public void removeFromPastEventsIf(Predicate<LunarEventInstance> filter) {
        pastEvents.removeIf(lunarEventInstance -> {
            if (filter.test(lunarEventInstance)) {
                markChanged();
                return true;
            }
            return false;
        });
    }

    private void markChanged() {
        sync();
        markDirty();
    }

    @Override
    public void sync() {
        if (!level.isClientSide) {
            shouldSync = true;
        }
    }

    private void checkServer() {
        if (level.isClientSide) {
            throw new IllegalStateException("MUST BE CALLED FROM SERVER SIDE ONLY!");
        }
    }

    public LunarDimensionSettings getDimensionSettings() {
        return dimensionSettingsHolder.value();
    }

    public Holder<LunarDimensionSettings> getDimensionSettingsHolder() {
        return dimensionSettingsHolder;
    }

    public static long clamp(long value, long min, long max) {
        return value < min ? min : Math.min(value, max);
    }

    @Nullable
    public static EnhancedCelestialsLunarForecastWorldData factory(TrackedDataKey<EnhancedCelestialsLunarForecastWorldData> key, Level level) {
        Registry<LunarDimensionSettings> lunarDimensionSettingsRegistry = level.registryAccess().registryOrThrow(EnhancedCelestialsRegistry.LUNAR_DIMENSION_SETTINGS_KEY);
        ResourceLocation location = level.dimension().location();
        Optional<Holder.Reference<LunarDimensionSettings>> possibleLunarDimensionSettings = lunarDimensionSettingsRegistry.getHolder(ResourceKey.create(EnhancedCelestialsRegistry.LUNAR_DIMENSION_SETTINGS_KEY, location));

        if (possibleLunarDimensionSettings.isEmpty()) {
            return null;
        }
        Holder.Reference<LunarDimensionSettings> dimensionSettingsHolder = possibleLunarDimensionSettings.orElseThrow();

        Registry<LunarEvent> lunarEvents = level.registryAccess().registry(EnhancedCelestialsRegistry.LUNAR_EVENT_KEY).orElseThrow();
        final Object2ObjectOpenHashMap<Holder<LunarEvent>, LunarEvent.SpawnRequirements> lunarEventSpawnRequirements = new Object2ObjectOpenHashMap<>();

        for (Map.Entry<ResourceKey<LunarEvent>, LunarEvent> resourceKeyLunarEventEntry : lunarEvents.entrySet()) {
            Holder<LunarEvent> lunarEventHolder = lunarEvents.getHolderOrThrow(resourceKeyLunarEventEntry.getKey());
            ResourceKey<Level> levelResourceKey = ResourceKey.create(Registries.DIMENSION, dimensionSettingsHolder.unwrapKey().orElseThrow().location());
            Map<ResourceKey<Level>, LunarEvent.SpawnRequirements> eventChancesByDimension = lunarEventHolder.value().getEventChancesByDimension();
            if (eventChancesByDimension.containsKey(levelResourceKey)) {
                LunarEvent.SpawnRequirements spawnRequirements = eventChancesByDimension.get(levelResourceKey);

                if (spawnRequirements.chance() > 0 && !spawnRequirements.validMoonPhases().isEmpty() && spawnRequirements.minNumberOfNights() >= 0) {
                    lunarEventSpawnRequirements.put(lunarEventHolder, spawnRequirements);
                }
            }
        }
        return lunarEventSpawnRequirements.isEmpty() ? null : new EnhancedCelestialsLunarForecastWorldData(key, level, dimensionSettingsHolder, lunarEventSpawnRequirements);
    }
}
