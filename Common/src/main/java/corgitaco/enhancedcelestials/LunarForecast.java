package corgitaco.enhancedcelestials;

import com.mojang.datafixers.util.Either;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import corgitaco.enhancedcelestials.api.lunarevent.LunarDimensionSettings;
import corgitaco.enhancedcelestials.api.lunarevent.LunarEvent;
import corgitaco.enhancedcelestials.api.lunarevent.LunarTextComponents;
import corgitaco.enhancedcelestials.network.LunarForecastChangedPacket;
import corgitaco.enhancedcelestials.platform.Services;
import corgitaco.enhancedcelestials.util.CustomTranslationTextComponent;
import it.unimi.dsi.fastutil.objects.Object2LongArrayMap;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.arguments.ResourceOrTagLocationArgument;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;

import java.util.*;
import java.util.stream.Collectors;

public class LunarForecast {

    private final Holder<LunarDimensionSettings> dimensionSettingsHolder;
    private final List<LunarEventInstance> forecast;
    private final List<LunarEventInstance> pastEvents;
    private final Registry<LunarEvent> lunarEventRegistry;
    private final List<Holder<LunarEvent>> scrambledKeys;

    private Holder<LunarEvent> currentEvent;
    private Holder<LunarEvent> mostRecentEvent;
    private float blend;
    private long lastCheckedGameTime;


    public LunarForecast(Holder<LunarDimensionSettings> dimensionSettingsHolder, Registry<LunarEvent> lunarEventRegistry, long currentDayTime, LunarForecast.SaveData savedData) {
        this(dimensionSettingsHolder, lunarEventRegistry, currentDayTime, savedData.forecast(), savedData.pastEvents(), savedData.lastCheckedGameTime());
    }

    public LunarForecast(Holder<LunarDimensionSettings> dimensionSettingsHolder, Registry<LunarEvent> lunarEventRegistry, long currentDayTime) {
        this(dimensionSettingsHolder, lunarEventRegistry, currentDayTime, new ArrayList<>(), new ArrayList<>(), -1L);
    }

    public LunarForecast(Holder<LunarDimensionSettings> dimensionSettingsHolder, Registry<LunarEvent> lunarEventRegistry, long currentDayTime, List<LunarEventInstance> forecast, List<LunarEventInstance> pastEvents, long lastCheckedGameTime) {
        this.lunarEventRegistry = lunarEventRegistry;
        LunarDimensionSettings lunarDimensionSettings = dimensionSettingsHolder.value();

        Map<Holder<LunarEvent>, LunarDimensionSettings.Entry> eventChances = lunarDimensionSettings.eventChance();
        Collection<Holder<LunarEvent>> possibleEvents = eventChances.keySet();
        this.dimensionSettingsHolder = dimensionSettingsHolder;
        this.forecast = new ArrayList<>(forecast);
        this.pastEvents = new ArrayList<>(pastEvents);
        Set<ResourceKey<LunarEvent>> possibleEventResourceKeys = possibleEvents.stream().map(Holder::unwrapKey).map(Optional::orElseThrow).collect(Collectors.toSet());
        for (int i = 0; i < forecast.size(); i++) {
            LunarEventInstance lunarEventInstance = forecast.get(i);
            ResourceKey<LunarEvent> lunarEventKey = lunarEventInstance.getLunarEventKey();
            if (!possibleEventResourceKeys.contains(lunarEventKey)) {
                EnhancedCelestials.LOGGER.error(String.format("\"%s\" is not a valid lunar event key, removing....", lunarEventKey.location()));
                this.forecast.remove(i);
            }
        }

        this.lastCheckedGameTime = lastCheckedGameTime;
        this.scrambledKeys = new ArrayList<>(possibleEvents);
        this.currentEvent = !forecast.isEmpty() && forecast.get(0).active(currentDayTime / lunarDimensionSettings.dayLength()) ? forecast.get(0).getEvent(lunarEventRegistry) : lunarDimensionSettings.defaultEvent();
        this.mostRecentEvent = pastEvents.isEmpty() ? lunarDimensionSettings.defaultEvent() : pastEvents.get(0).getEvent(lunarEventRegistry);
    }

    public SaveData saveData() {
        return new SaveData(this.forecast, this.pastEvents, this.lastCheckedGameTime);
    }

    public void recompute(ServerLevel level) {
        this.forecast.clear();
        this.lastCheckedGameTime = Long.MIN_VALUE;
        if (updateForecast(level, this.dimensionSettingsHolder.value(), this)) {
            List<ServerPlayer> players = level.players();
            Services.PLATFORM.sendToAllClients(players, new LunarForecastChangedPacket(this, level.isNight()));
        }
    }

    public Pair<Component, Boolean> setOrReplaceEventWithResponse(ResourceOrTagLocationArgument.Result<LunarEvent> result, long currentDay, RandomSource randomSource) {
        if (result.test(this.currentEvent)) {
            return Pair.of(Component.translatable("Event is already active"), false);
        }

        Either<ResourceKey<LunarEvent>, TagKey<LunarEvent>> unwrappedResult = result.unwrap();

        Optional<ResourceKey<LunarEvent>> left = unwrappedResult.left();
        if (left.isPresent()) {
            ResourceKey<LunarEvent> resourceKey = left.orElseThrow();
            removeIfActive(currentDay);
            this.forecast.add(0, new LunarEventInstance(resourceKey, currentDay, true));
            return Pair.of(Component.translatable("Set lunar event to \"%s\"", resourceKey.location()), true);
        }
        Optional<TagKey<LunarEvent>> right = unwrappedResult.right();


        TagKey<LunarEvent> lunarEventTagKey = right.orElseThrow();
        Optional<HolderSet.Named<LunarEvent>> tag = this.lunarEventRegistry.getTag(lunarEventTagKey);
        if (tag.isPresent()) {
            Holder<LunarEvent> randomElement = tag.get().getRandomElement(randomSource).orElseThrow();
            removeIfActive(currentDay);
            this.forecast.add(0, new LunarEventInstance(randomElement.unwrapKey().orElseThrow(), currentDay, true));
            return Pair.of(Component.translatable("Set lunar event to \"%s\" from tag \"%s", lunarEventTagKey.location()), true);
        }
        return Pair.of(Component.translatable("Could not set lunar event for result:\n\"%s\"", result.toString()), false);
    }

    public Component getForecastComponent(long currentDayTime) {
        long currentDay = currentDayTime / this.dimensionSettingsHolder.value().dayLength();
        MutableComponent textComponent = null;

        for (int i = Math.min(100, this.getForecast().size() - 1); i >= 0; i--) {
            LunarEventInstance lunarEventInstance = this.getForecast().get(i);
            Holder<LunarEvent> event = lunarEventInstance.getEvent(this.lunarEventRegistry);
            CustomTranslationTextComponent name = event.value().getTextComponents().name();
            TextColor color = name.getStyle().getColor();
            if (textComponent == null) {
                textComponent = Component.translatable(name.getKey()).withStyle(Style.EMPTY.withColor(color));
            } else {
                textComponent.append(Component.literal(", ").withStyle(Style.EMPTY.withColor(ChatFormatting.WHITE))).append(Component.translatable(name.getKey()).withStyle(Style.EMPTY.withColor(color)));
            }
            textComponent.append(Component.translatable("enhancedcelestials.lunarforecast.days_left", lunarEventInstance.getDaysUntil(currentDay)).withStyle(Style.EMPTY.withColor(color)));
        }

        if (textComponent != null) {
            return Component.translatable("enhancedcelestials.lunarforecast.header", textComponent.append(Component.literal(".").withStyle(Style.EMPTY.withColor(ChatFormatting.WHITE))));
        } else {
            return Component.translatable("enhancedcelestials.lunarforecast.empty", textComponent).withStyle(ChatFormatting.YELLOW);
        }

    }

    private void removeIfActive(long currentDay) {
        if (!forecast.isEmpty() && this.forecast.get(0).active(currentDay)) {
            this.forecast.remove(0);
        }
    }

    public void tick(Level level) {
        LunarDimensionSettings lunarDimensionSettings = this.dimensionSettingsHolder.value();

        Holder<LunarEvent> lastCurrentEvent = this.currentEvent;

        long dayTime = level.getDayTime();
        long dayLength = lunarDimensionSettings.dayLength();
        long currentDay = dayTime / dayLength;

        if (!level.isClientSide) {
            serverTick((ServerLevel) level, lastCurrentEvent, lunarDimensionSettings, currentDay);
        }
        this.blend = Mth.clamp(this.blend + 0.01F, 0, 1.0F);
    }

    private void serverTick(ServerLevel level, Holder<LunarEvent> lastCurrentEvent, LunarDimensionSettings lunarDimensionSettings, long currentDay) {
        List<ServerPlayer> players = level.players();
        boolean isNight = level.isNight();
        if (updateForecast(level, lunarDimensionSettings, this, 0L)) {
            Services.PLATFORM.sendToAllClients(players, new LunarForecastChangedPacket(this, isNight));
        }

        updatePastEventsAndRecentAndCurrentEvents(this, currentDay);
        LunarEventInstance lunarEventInstance = this.forecast.get(0);
        if (lunarEventInstance.active(currentDay) && isNight) {
            this.currentEvent = lunarEventInstance.getEvent(this.lunarEventRegistry);
        } else {
            this.currentEvent = lunarDimensionSettings.defaultEvent();
        }

        if (lastCurrentEvent != this.currentEvent) {
            onLunarEventChange(lastCurrentEvent, players, isNight);
        }

        // Sync every 5 minutes just in case.
        if (level.getGameTime() % 6000L == 0) {
            Services.PLATFORM.sendToAllClients(players, new LunarForecastChangedPacket(this, isNight));
        }
    }

    private void onLunarEventChange(Holder<LunarEvent> lastCurrentEvent, List<ServerPlayer> players, boolean isNight) {
        this.blend = 0;
        Services.PLATFORM.sendToAllClients(players, new LunarForecastChangedPacket(this, isNight));
        notifyPlayers(lastCurrentEvent, players);
    }

    private void notifyPlayers(Holder<LunarEvent> lastCurrentEvent, List<ServerPlayer> players) {
        LunarTextComponents.Notification endNotification = lastCurrentEvent.value().endNotification();
        if (endNotification != null) {
            for (ServerPlayer player : players) {
                player.displayClientMessage(endNotification.customTranslationTextComponent().getComponent(), endNotification.notificationType() == LunarTextComponents.NotificationType.HOT_BAR);
            }
        }
        LunarTextComponents.Notification startNotification = this.currentEvent.value().startNotification();
        if (startNotification != null) {
            for (ServerPlayer player : players) {
                player.displayClientMessage(startNotification.customTranslationTextComponent().getComponent(), startNotification.notificationType() == LunarTextComponents.NotificationType.HOT_BAR);
            }
        }
    }

    public List<LunarEventInstance> getForecast() {
        return forecast;
    }

    public long getLastCheckedGameTime() {
        return lastCheckedGameTime;
    }

    public List<LunarEventInstance> getPastEvents() {
        return pastEvents;
    }

    public Registry<LunarEvent> getLunarEventRegistry() {
        return lunarEventRegistry;
    }

    public Holder<LunarEvent> getCurrentEvent() {
        return currentEvent;
    }

    public Holder<LunarEvent> getMostRecentEvent() {
        return mostRecentEvent;
    }

    public float getBlend() {
        return blend;
    }

    public List<Holder<LunarEvent>> getScrambledKeys() {
        return scrambledKeys;
    }

    public void setLastCheckedGameTime(long lastCheckedGameTime) {
        this.lastCheckedGameTime = lastCheckedGameTime;
    }

    public void setCurrentEvent(ResourceKey<LunarEvent> key) {
        setCurrentEvent(this.lunarEventRegistry.getHolderOrThrow(key));
    }

    public Holder<LunarDimensionSettings> getDimensionSettingsHolder() {
        return dimensionSettingsHolder;
    }

    public void setCurrentEvent(Holder<LunarEvent> currentEvent) {
        if (currentEvent != this.currentEvent) {
            this.mostRecentEvent = this.currentEvent;
            this.currentEvent = currentEvent;
            blend = 0;
        }
    }

    public static boolean updateForecast(ServerLevel world, LunarDimensionSettings dimensionSettings, LunarForecast lunarForecast) {
        return updateForecast(world, dimensionSettings, lunarForecast, 0L);
    }

    /**
     * @return true if forecast changes occurred.
     */
    public static boolean updateForecast(ServerLevel world, LunarDimensionSettings dimensionSettings, LunarForecast lunarForecast, long seedModifier) {
        long dayTime = world.getDayTime();
        long lastCheckedTime = lunarForecast.getLastCheckedGameTime();
        long dayLength = dimensionSettings.dayLength();
        long yearLengthInDays = dimensionSettings.yearLengthInDays();
        long currentDay = dayTime / dayLength;
        long lastCheckedDay = lastCheckedTime / dayLength;

        if (lastCheckedDay < currentDay) {
            lunarForecast.getForecast().clear();
            lunarForecast.setLastCheckedGameTime(currentDay * dayLength);
            lastCheckedTime = lunarForecast.getLastCheckedGameTime();
            lastCheckedDay = lastCheckedTime / dayLength;
        }

        if (currentDay + yearLengthInDays <= lastCheckedDay) {
            return false;
        }

        List<LunarEventInstance> newLunarEvents = new ArrayList<>();

        Object2LongArrayMap<LunarEvent> eventByLastTime = new Object2LongArrayMap<>();
        List<LunarEventInstance> forecast = lunarForecast.getForecast();
        long lastDay = !forecast.isEmpty() ? forecast.get(forecast.size() - 1).scheduledDay() : currentDay;

        long day = lastCheckedDay;

        for (LunarEventInstance lunarEventInstance : forecast) {
            eventByLastTime.put(lunarEventInstance.getEvent(lunarForecast.lunarEventRegistry).value(), lunarEventInstance.scheduledDay());
        }

        for (; day <= currentDay + yearLengthInDays; day++) {
            dayTime += dayLength;
            Random random = new Random(world.getSeed() + world.dimension().location().hashCode() + day + seedModifier);
            Collections.shuffle(lunarForecast.scrambledKeys, random);
            for (Holder<LunarEvent> lunarEventHolder : lunarForecast.scrambledKeys) {
                LunarDimensionSettings.Entry entry = dimensionSettings.eventChance().get(lunarEventHolder);
                LunarEvent value = lunarEventHolder.value();
                if ((day - eventByLastTime.getOrDefault(value, currentDay)) > entry.minNumberOfNights() && (day - lastDay) > dimensionSettings.minDaysBetweenEvents() && entry.chance() > random.nextDouble() && value.getValidMoonPhases().contains(world.dimensionType().moonPhase(dayTime - 1))) {
                    lastDay = day;
                    newLunarEvents.add(new LunarEventInstance(lunarEventHolder.unwrapKey().orElseThrow(), day));
                    eventByLastTime.put(value, day);
                }
            }
        }
        forecast.addAll(newLunarEvents);
        lunarForecast.setLastCheckedGameTime(day * dayLength);
        return true;
    }

    private static void updatePastEventsAndRecentAndCurrentEvents(LunarForecast lunarForecast, long currentDay) {
        if (!lunarForecast.getForecast().isEmpty()) {
            LunarEventInstance mostRecentInstance = lunarForecast.getForecast().get(0);
            if (mostRecentInstance.passed(currentDay)) {
                lunarForecast.forecast.remove(0);
                List<LunarEventInstance> pastEvents = lunarForecast.pastEvents;
                pastEvents.add(0, mostRecentInstance);
                while (pastEvents.size() > 100) {
                    pastEvents.remove(pastEvents.size() - 1);
                }
                lunarForecast.mostRecentEvent = mostRecentInstance.getEvent(lunarForecast.lunarEventRegistry);
            }
        }
    }

    public record SaveData(List<LunarEventInstance> forecast,
                           List<LunarEventInstance> pastEvents,
                           long lastCheckedGameTime) {
        public static final Codec<SaveData> CODEC = RecordCodecBuilder.create(builder ->
                builder.group(
                        LunarEventInstance.CODEC.listOf().fieldOf("forecast").forGetter(SaveData::forecast),
                        LunarEventInstance.CODEC.listOf().fieldOf("past_events").forGetter(SaveData::pastEvents),
                        Codec.LONG.fieldOf("last_checked_game_time").forGetter(SaveData::lastCheckedGameTime)
                ).apply(builder, SaveData::new)
        );
    }
}
