package corgitaco.enhancedcelestials.api.lunarevent;

import com.google.common.collect.ImmutableMap;
import com.mojang.datafixers.util.Pair;
import corgitaco.corgilib.entity.condition.AnyCondition;
import corgitaco.corgilib.entity.condition.FlipCondition;
import corgitaco.enhancedcelestials.EnhancedCelestials;
import corgitaco.enhancedcelestials.api.ECItemTags;
import corgitaco.enhancedcelestials.api.EnhancedCelestialsRegistry;
import corgitaco.enhancedcelestials.api.client.ColorSettings;
import corgitaco.enhancedcelestials.api.lunarevent.client.LunarEventClientSettings;
import corgitaco.enhancedcelestials.core.ECSounds;
import corgitaco.enhancedcelestials.util.CustomTranslationTextComponent;
import it.unimi.dsi.fastutil.ints.IntArraySet;
import it.unimi.dsi.fastutil.objects.Reference2ObjectOpenHashMap;
import net.minecraft.ChatFormatting;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.MobSpawnSettings;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class DefaultLunarEvents {

    public static final Map<ResourceKey<LunarEvent>, LunarEventFactory> LUNAR_EVENT_FACTORIES = new Reference2ObjectOpenHashMap<>();


    public static final Collection<Integer> DEFAULT_PHASES = IntArraySet.of(0, 1, 2, 3, 4, 5, 6, 7);
    public static final Collection<Integer> DEFAULT_SUPER_MOON_PHASES = IntArraySet.of(0);


    public static final ResourceKey<LunarEvent> DEFAULT = createEvent("default", () ->
            new LunarEvent(
                    ImmutableMap.of(),
                    new LunarEventClientSettings(
                            new ColorSettings("3333ff", "ffffff"),
                            20,
                            null,
                            null
                    ),
                    new LunarTextComponents(
                            new CustomTranslationTextComponent("enhancedcelestials.name.moon"),
                            CustomTranslationTextComponent.DEFAULT,
                            CustomTranslationTextComponent.DEFAULT
                    ),
                    LunarMobSettings.DEFAULT,
                    DropSettings.EMPTY)
    );

    public static final ResourceKey<LunarEvent> SUPER_MOON = createEvent("super_moon", () ->
            new LunarEvent(
                    ImmutableMap.of(Level.OVERWORLD, new LunarEvent.SpawnRequirements(0.05, 20, DEFAULT_SUPER_MOON_PHASES)),
                    new LunarEventClientSettings(
                            new ColorSettings("6766ff", "ffffff"),
                            40,
                            null,
                            null
                    ),
                    new LunarTextComponents(
                            new CustomTranslationTextComponent(
                                    "enhancedcelestials.name.super_moon"
                            ),
                            new CustomTranslationTextComponent(
                                    "enhancedcelestials.notification.super_moon.rise"
                            ),
                            new CustomTranslationTextComponent("enhancedcelestials.notification.super_moon.set")),
                    new LunarMobSettings(ImmutableMap.of(),
                            new LunarMobSpawnInfo(true, false, true, MobSpawnSettings.EMPTY),
                            List.of(),
                            new FlipCondition(AnyCondition.INSTANCE)),
                    DropSettings.EMPTY)
    );

    public static final ResourceKey<LunarEvent> BLOOD_MOON = createEvent("blood_moon", () ->
            new LunarEvent(
                    ImmutableMap.of(Level.OVERWORLD, new LunarEvent.SpawnRequirements(0.1, 4, DEFAULT_PHASES)),
                    new LunarEventClientSettings(
                            new ColorSettings("990000", "990000"),
                            20,
                            null,
                            ECSounds.BLOOD_MOON.get()
                    ),
                    new LunarTextComponents(
                            new CustomTranslationTextComponent(
                                    "enhancedcelestials.name.blood_moon",
                                    Style.EMPTY.applyFormat(ChatFormatting.RED)
                            ),
                            new CustomTranslationTextComponent(
                                    "enhancedcelestials.notification.blood_moon.rise",
                                    Style.EMPTY.applyFormat(ChatFormatting.RED)
                            ),
                            new CustomTranslationTextComponent("enhancedcelestials.notification.blood_moon.set")
                    ),
                    new LunarMobSettings(
                            ImmutableMap.of(MobCategory.MONSTER, 4.5D),
                            new LunarMobSpawnInfo(true, true, false, MobSpawnSettings.EMPTY),
                            List.of(),
                            AnyCondition.INSTANCE
                    ),
                    DropSettings.EMPTY)
    );

    public static final ResourceKey<LunarEvent> SUPER_BLOOD_MOON = createEvent("super_blood_moon", () ->
            new LunarEvent(
                    ImmutableMap.of(Level.OVERWORLD, new LunarEvent.SpawnRequirements(0.05, 20, DEFAULT_SUPER_MOON_PHASES)),
                    new LunarEventClientSettings(
                            new ColorSettings("FF0000", "FF0000"),
                            40,
                            null,
                            ECSounds.BLOOD_MOON.get()
                    ),
                    new LunarTextComponents(
                            new CustomTranslationTextComponent(
                                    "enhancedcelestials.name.super_blood_moon",
                                    Style.EMPTY.applyFormat(ChatFormatting.RED)
                            ),
                            new CustomTranslationTextComponent(
                                    "enhancedcelestials.notification.super_blood_moon.rise",
                                    Style.EMPTY.applyFormat(ChatFormatting.RED).applyFormat(ChatFormatting.BOLD)
                            ),
                            new CustomTranslationTextComponent("enhancedcelestials.notification.blood_moon.set")
                    ),
                    new LunarMobSettings(
                            ImmutableMap.of(MobCategory.MONSTER, 4.5D),
                            new LunarMobSpawnInfo(true, true, false, MobSpawnSettings.EMPTY),
                            List.of(),
                            AnyCondition.INSTANCE
                    ),
                    DropSettings.EMPTY)
    );

    public static final ResourceKey<LunarEvent> HARVEST_MOON = createEvent("harvest_moon", () ->
            new LunarEvent(
                    ImmutableMap.of(Level.OVERWORLD, new LunarEvent.SpawnRequirements(0.1, 4, DEFAULT_PHASES)),
                    new LunarEventClientSettings(
                            new ColorSettings("99833b", "665828"),
                            20,
                            null,
                            ECSounds.HARVEST_MOON.get()
                    ),
                    new LunarTextComponents(
                            new CustomTranslationTextComponent(
                                    "enhancedcelestials.name.harvest_moon",
                                    Style.EMPTY.applyFormat(ChatFormatting.YELLOW)
                            ),
                            new CustomTranslationTextComponent(
                                    "enhancedcelestials.notification.harvest_moon.rise",
                                    Style.EMPTY.applyFormat(ChatFormatting.YELLOW)
                            ),
                            new CustomTranslationTextComponent("enhancedcelestials.notification.harvest_moon.set")),
                    LunarMobSettings.DEFAULT,
                    new DropSettings(ImmutableMap.of(ECItemTags.HARVEST_MOON_CROPS, 2.0)))
    );

    public static final ResourceKey<LunarEvent> SUPER_HARVEST_MOON = createEvent("super_harvest_moon", () ->
            new LunarEvent(
                    ImmutableMap.of(Level.OVERWORLD, new LunarEvent.SpawnRequirements(0.05, 20, DEFAULT_SUPER_MOON_PHASES)),
                    new LunarEventClientSettings(
                            new ColorSettings("FFDB63", "FFDB63"),
                            40,
                            null,
                            ECSounds.HARVEST_MOON.get()
                    ),
                    new LunarTextComponents(
                            new CustomTranslationTextComponent(
                                    "enhancedcelestials.name.super_harvest_moon",
                                    Style.EMPTY.applyFormat(ChatFormatting.YELLOW)
                            ),
                            new CustomTranslationTextComponent(
                                    "enhancedcelestials.notification.super_harvest_moon.rise",
                                    Style.EMPTY.applyFormat(ChatFormatting.YELLOW).applyFormat(ChatFormatting.BOLD)
                            ),
                            new CustomTranslationTextComponent("enhancedcelestials.notification.super_harvest_moon.set")),
                    LunarMobSettings.DEFAULT,
                    new DropSettings(ImmutableMap.of(ECItemTags.HARVEST_MOON_CROPS, 4.0)))
    );

    public static final ResourceKey<LunarEvent> BLUE_MOON = createEvent("blue_moon", () ->
            new LunarEvent(
                    ImmutableMap.of(Level.OVERWORLD, new LunarEvent.SpawnRequirements(0.1, 4, DEFAULT_PHASES)),
                    new LunarEventClientSettings(
                            new ColorSettings("009999", "009999"),
                            20,
                            null,
                            ECSounds.BLUE_MOON.get()
                    ),
                    new LunarTextComponents(
                            new CustomTranslationTextComponent(
                                    "enhancedcelestials.name.blue_moon",
                                    Style.EMPTY.applyFormat(ChatFormatting.AQUA)
                            ),
                            new CustomTranslationTextComponent(
                                    "enhancedcelestials.notification.blue_moon.rise",
                                    Style.EMPTY.applyFormat(ChatFormatting.AQUA)
                            ),
                            new CustomTranslationTextComponent("enhancedcelestials.notification.blue_moon.set")),
                    new LunarMobSettings(ImmutableMap.of(), LunarMobSpawnInfo.DEFAULT, List.of(
                            Pair.of(AnyCondition.INSTANCE, new MobEffectInstanceBuilder(MobEffects.LUCK, 1210, 0, true, false, false))
                    ), new FlipCondition(AnyCondition.INSTANCE)),
                    DropSettings.EMPTY)
    );

    public static final ResourceKey<LunarEvent> SUPER_BLUE_MOON = createEvent("super_blue_moon", () ->
            new LunarEvent(
                    ImmutableMap.of(Level.OVERWORLD, new LunarEvent.SpawnRequirements(0.05, 20, DEFAULT_SUPER_MOON_PHASES)),
                    new LunarEventClientSettings(
                            new ColorSettings("00ffff", "00ffff"),
                            40,
                            null,
                            ECSounds.BLUE_MOON.get()
                    ),
                    new LunarTextComponents(
                            new CustomTranslationTextComponent(
                                    "enhancedcelestials.name.super_blue_moon",
                                    Style.EMPTY.applyFormat(ChatFormatting.AQUA)
                            ),
                            new CustomTranslationTextComponent(
                                    "enhancedcelestials.notification.super_blue_moon.rise",
                                    Style.EMPTY.applyFormat(ChatFormatting.AQUA)
                            ),
                            new CustomTranslationTextComponent("enhancedcelestials.notification.super_blue_moon.set")),
                    new LunarMobSettings(ImmutableMap.of(), LunarMobSpawnInfo.DEFAULT, List.of(
                            Pair.of(AnyCondition.INSTANCE, new MobEffectInstanceBuilder(MobEffects.LUCK, 1210, 4, true, false, false))
                    ), new FlipCondition(AnyCondition.INSTANCE)),
                    DropSettings.EMPTY)
    );


    public static ResourceKey<LunarEvent> createEvent(String id, Supplier<LunarEvent> event) {
        ResourceKey<LunarEvent> lunarEventResourceKey = ResourceKey.create(EnhancedCelestialsRegistry.LUNAR_EVENT_KEY, EnhancedCelestials.createLocation(id));
        LUNAR_EVENT_FACTORIES.put(lunarEventResourceKey, placedFeatureHolderGetter -> event.get());
        return lunarEventResourceKey;
    }

    public static void loadClass() {
    }

    @FunctionalInterface
    public interface LunarEventFactory {

        LunarEvent generate(BootstapContext<LunarEvent> placedFeatureHolderGetter);
    }
}
