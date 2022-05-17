package corgitaco.enhancedcelestials;

import com.google.common.collect.ImmutableSet;
import corgitaco.enhancedcelestials.api.EnhancedCelestialsRegistry;
import corgitaco.enhancedcelestials.api.client.ColorSettings;
import corgitaco.enhancedcelestials.api.lunarevent.LunarMobSpawnInfo;
import corgitaco.enhancedcelestials.api.lunarevent.LunarTextComponents;
import corgitaco.enhancedcelestials.core.ECSounds;
import corgitaco.enhancedcelestials.lunarevent.BloodMoon;
import corgitaco.enhancedcelestials.lunarevent.BlueMoon;
import corgitaco.enhancedcelestials.lunarevent.HarvestMoon;
import corgitaco.enhancedcelestials.lunarevent.client.MoonClientSettings;
import corgitaco.enhancedcelestials.platform.Services;
import corgitaco.enhancedcelestials.util.CustomTranslationTextComponent;
import it.unimi.dsi.fastutil.objects.Object2DoubleArrayMap;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.MobSpawnSettings;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.file.Path;
import java.util.Set;

public class EnhancedCelestials {
    public static final String MOD_ID = "enhancedcelestials";
    public static final Logger LOGGER = LogManager.getLogger();
    public static final Path CONFIG_PATH = Services.PLATFORM.configDir().resolve(MOD_ID);

    public EnhancedCelestials() {
    }

    public static void commonSetup() {
        registerDefaults();
    }

    public static void registerDefaults() {
        Registry.register(EnhancedCelestialsRegistry.LUNAR_EVENT, createLocation("blood_moon"), BloodMoon.CODEC);
        Registry.register(EnhancedCelestialsRegistry.LUNAR_EVENT, createLocation("blue_moon"), BlueMoon.CODEC);
        Registry.register(EnhancedCelestialsRegistry.LUNAR_EVENT, createLocation("harvest_moon"), HarvestMoon.CODEC);
        Registry.register(EnhancedCelestialsRegistry.LUNAR_CLIENT_EVENT_SETTINGS, createLocation("default"), MoonClientSettings.CODEC);

        Set<Integer> defaultMoonPhases = ImmutableSet.of(0, 1, 2, 3, 5, 6, 7);
        Set<Integer> defaultSuperMoonPhases = ImmutableSet.of(0);
        EnhancedCelestialsRegistry.DEFAULT_EVENTS.put(createLocation("blood_moon"), new BloodMoon(new MoonClientSettings(new ColorSettings("FF0000", 0.6, "FF0000", 0.4), 20.0F, ECSounds.BLOOD_MOON.get()), 4, 0.07, defaultMoonPhases, new LunarTextComponents(new CustomTranslationTextComponent("enhancedcelestials.name.blood_moon"), new CustomTranslationTextComponent("enhancedcelestials.notification.blood_moon.rise", Style.EMPTY.applyFormat(ChatFormatting.RED)), new CustomTranslationTextComponent("enhancedcelestials.notification.blood_moon.set")), true, Util.make(new Object2DoubleArrayMap<>(), (map) -> map.put(MobCategory.MONSTER, 3.0)), new LunarMobSpawnInfo(true, true, new MobSpawnSettings.Builder().build())));
        EnhancedCelestialsRegistry.DEFAULT_EVENTS.put(createLocation("super_blood_moon"), new BloodMoon(new MoonClientSettings(new ColorSettings("FF0000", 1, "FF0000", 1), 40.0F, ECSounds.BLOOD_MOON.get()), 20, 0.0175, defaultSuperMoonPhases, new LunarTextComponents(new CustomTranslationTextComponent("enhancedcelestials.name.super_blood_moon"), new CustomTranslationTextComponent("enhancedcelestials.notification.super_blood_moon.rise", Style.EMPTY.applyFormat(ChatFormatting.RED).applyFormat(ChatFormatting.BOLD)), new CustomTranslationTextComponent("enhancedcelestials.notification.blood_moon.set")), true, Util.make(new Object2DoubleArrayMap<>(), (map) -> map.put(MobCategory.MONSTER, 4.5)), new LunarMobSpawnInfo(true, true, new MobSpawnSettings.Builder().build())));
        EnhancedCelestialsRegistry.DEFAULT_EVENTS.put(createLocation("harvest_moon"), new HarvestMoon(new MoonClientSettings(new ColorSettings("FFDB63", 0.6, "FFDB63", 0.4), 20.0F, ECSounds.HARVEST_MOON.get()), 4, 0.035, defaultMoonPhases, new LunarTextComponents(new CustomTranslationTextComponent("enhancedcelestials.name.harvest_moon"), new CustomTranslationTextComponent("enhancedcelestials.notification.harvest_moon.rise", Style.EMPTY.applyFormat(ChatFormatting.YELLOW)), new CustomTranslationTextComponent("enhancedcelestials.notification.harvest_moon.set")), false, ImmutableSet.of(createLocation("item_tag_harvest_moon_crops")), 2.0, false));
        EnhancedCelestialsRegistry.DEFAULT_EVENTS.put(createLocation("super_harvest_moon"), new HarvestMoon(new MoonClientSettings(new ColorSettings("FFDB63", 1, "FFDB63", 1), 40.0F, ECSounds.HARVEST_MOON.get()), 20, 0.0175, defaultSuperMoonPhases, new LunarTextComponents(new CustomTranslationTextComponent("enhancedcelestials.name.super_harvest_moon"), new CustomTranslationTextComponent("enhancedcelestials.notification.super_harvest_moon.rise", Style.EMPTY.applyFormat(ChatFormatting.YELLOW).applyFormat(ChatFormatting.BOLD)), new CustomTranslationTextComponent("enhancedcelestials.notification.super_harvest_moon.set")), false, ImmutableSet.of(createLocation("item_tag_harvest_moon_crops")), 4.0, false));
        EnhancedCelestialsRegistry.DEFAULT_EVENTS.put(createLocation("blue_moon"), new BlueMoon(new MoonClientSettings(new ColorSettings("00ffff", 0.6, "00ffff", 0.4), 20.0F, ECSounds.BLUE_MOON.get()), 4, 0.02, defaultMoonPhases, new LunarTextComponents(new CustomTranslationTextComponent("enhancedcelestials.name.blue_moon"), new CustomTranslationTextComponent("enhancedcelestials.notification.blue_moon.rise", Style.EMPTY.applyFormat(ChatFormatting.AQUA)), new CustomTranslationTextComponent("enhancedcelestials.notification.blue_moon.set")), false, 1));
        EnhancedCelestialsRegistry.DEFAULT_EVENTS.put(createLocation("super_blue_moon"), new BlueMoon(new MoonClientSettings(new ColorSettings("00ffff", 1, "00ffff", 1), 40.0F, ECSounds.BLUE_MOON.get()), 20, 0.01, defaultSuperMoonPhases, new LunarTextComponents(new CustomTranslationTextComponent("enhancedcelestials.name.super_blue_moon"), new CustomTranslationTextComponent("enhancedcelestials.notification.super_blue_moon.rise", Style.EMPTY.applyFormat(ChatFormatting.AQUA).applyFormat(ChatFormatting.BOLD)), new CustomTranslationTextComponent("enhancedcelestials.notification.super_blue_moon.set")), false, 5));
    }

    public static ResourceLocation createLocation(String path) {
        return new ResourceLocation(MOD_ID, path);
    }
}
