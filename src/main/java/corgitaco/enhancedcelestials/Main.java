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
import corgitaco.enhancedcelestials.network.NetworkHandler;
import corgitaco.enhancedcelestials.util.CustomTranslationTextComponent;
import it.unimi.dsi.fastutil.objects.Object2DoubleArrayMap;
import net.minecraft.entity.EntityClassification;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Util;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.biome.MobSpawnInfo;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.nio.file.Path;
import java.util.Set;

@Mod(Main.MOD_ID)
public class Main {
    public static final String MOD_ID = "enhancedcelestials";
    public static final Logger LOGGER = LogManager.getLogger();
    public static final Path CONFIG_PATH = new File(String.valueOf(FMLPaths.CONFIGDIR.get().resolve(MOD_ID))).toPath();

    public Main() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        bus.addListener(this::commonSetup);
    }

    public void commonSetup(FMLCommonSetupEvent commonSetupEvent) {
        NetworkHandler.init();
        registerDefaults();
    }

    public static void registerDefaults() {
        Registry.register(EnhancedCelestialsRegistry.LUNAR_EVENT, new ResourceLocation(MOD_ID, "blood_moon"), BloodMoon.CODEC);
        Registry.register(EnhancedCelestialsRegistry.LUNAR_EVENT, new ResourceLocation(MOD_ID, "blue_moon"), BlueMoon.CODEC);
        Registry.register(EnhancedCelestialsRegistry.LUNAR_EVENT, new ResourceLocation(MOD_ID, "harvest_moon"), HarvestMoon.CODEC);
        Registry.register(EnhancedCelestialsRegistry.LUNAR_CLIENT_EVENT_SETTINGS, new ResourceLocation(MOD_ID, "default"), MoonClientSettings.CODEC);

        Set<Integer> defaultMoonPhases = ImmutableSet.of(0, 1, 2, 3, 5, 6, 7);
        Set<Integer> defaultSuperMoonPhases = ImmutableSet.of(0);
        EnhancedCelestialsRegistry.DEFAULT_EVENTS.put(new ResourceLocation(MOD_ID, "blood_moon"), new BloodMoon(new MoonClientSettings(new ColorSettings("FF0000", 0.6, "FF0000", 0.4), 20.0F, ECSounds.BLOOD_MOON), 4, 0.07, defaultMoonPhases, new LunarTextComponents(new CustomTranslationTextComponent("enhancedcelestials.name.blood_moon"), new CustomTranslationTextComponent("enhancedcelestials.notification.blood_moon.rise", Style.EMPTY.applyFormatting(TextFormatting.RED)), new CustomTranslationTextComponent("enhancedcelestials.notification.blood_moon.set")), true, Util.make(new Object2DoubleArrayMap<>(), (map) -> map.put(EntityClassification.MONSTER, 3.0)), new LunarMobSpawnInfo(true, true, new MobSpawnInfo.Builder().copy())));
        EnhancedCelestialsRegistry.DEFAULT_EVENTS.put(new ResourceLocation(MOD_ID, "super_blood_moon"), new BloodMoon(new MoonClientSettings(new ColorSettings("FF0000", 1, "FF0000", 1), 40.0F, ECSounds.BLOOD_MOON), 20, 0.0175, defaultSuperMoonPhases, new LunarTextComponents(new CustomTranslationTextComponent("enhancedcelestials.name.super_blood_moon"), new CustomTranslationTextComponent("enhancedcelestials.notification.super_blood_moon.rise", Style.EMPTY.applyFormatting(TextFormatting.RED).applyFormatting(TextFormatting.BOLD)), new CustomTranslationTextComponent("enhancedcelestials.notification.blood_moon.set")), true, Util.make(new Object2DoubleArrayMap<>(), (map) -> map.put(EntityClassification.MONSTER, 4.5)), new LunarMobSpawnInfo(true, true, new MobSpawnInfo.Builder().copy())));
        EnhancedCelestialsRegistry.DEFAULT_EVENTS.put(new ResourceLocation(MOD_ID, "harvest_moon"), new HarvestMoon(new MoonClientSettings(new ColorSettings("FFDB63", 0.6, "FFDB63", 0.4), 20.0F, ECSounds.HARVEST_MOON), 4, 0.035, defaultMoonPhases, new LunarTextComponents(new CustomTranslationTextComponent("enhancedcelestials.name.harvest_moon"), new CustomTranslationTextComponent("enhancedcelestials.notification.harvest_moon.rise", Style.EMPTY.applyFormatting(TextFormatting.YELLOW)), new CustomTranslationTextComponent("enhancedcelestials.notification.harvest_moon.set")), false, ImmutableSet.of(new ResourceLocation("forge", "item_tag_crops")), 2.0, false));
        EnhancedCelestialsRegistry.DEFAULT_EVENTS.put(new ResourceLocation(MOD_ID, "super_harvest_moon"), new HarvestMoon(new MoonClientSettings(new ColorSettings("FFDB63", 1, "FFDB63", 1), 40.0F, ECSounds.HARVEST_MOON), 20, 0.0175, defaultSuperMoonPhases, new LunarTextComponents(new CustomTranslationTextComponent("enhancedcelestials.name.super_harvest_moon"), new CustomTranslationTextComponent("enhancedcelestials.notification.super_harvest_moon.rise", Style.EMPTY.applyFormatting(TextFormatting.YELLOW).applyFormatting(TextFormatting.BOLD)), new CustomTranslationTextComponent("enhancedcelestials.notification.super_harvest_moon.set")), false, ImmutableSet.of(new ResourceLocation("forge", "item_tag_crops")), 4.0, false));
        EnhancedCelestialsRegistry.DEFAULT_EVENTS.put(new ResourceLocation(MOD_ID, "blue_moon"), new BlueMoon(new MoonClientSettings(new ColorSettings("00ffff", 0.6, "00ffff", 0.4), 20.0F, ECSounds.BLUE_MOON), 4, 0.02, defaultMoonPhases, new LunarTextComponents(new CustomTranslationTextComponent("enhancedcelestials.name.blue_moon"), new CustomTranslationTextComponent("enhancedcelestials.notification.blue_moon.rise", Style.EMPTY.applyFormatting(TextFormatting.AQUA)), new CustomTranslationTextComponent("enhancedcelestials.notification.blue_moon.set")), false, 1));
        EnhancedCelestialsRegistry.DEFAULT_EVENTS.put(new ResourceLocation(MOD_ID, "super_blue_moon"), new BlueMoon(new MoonClientSettings(new ColorSettings("00ffff", 1, "00ffff", 1), 40.0F, ECSounds.BLUE_MOON), 20, 0.01, defaultSuperMoonPhases, new LunarTextComponents(new CustomTranslationTextComponent("enhancedcelestials.name.super_blue_moon"), new CustomTranslationTextComponent("enhancedcelestials.notification.super_blue_moon.rise", Style.EMPTY.applyFormatting(TextFormatting.AQUA).applyFormatting(TextFormatting.BOLD)), new CustomTranslationTextComponent("enhancedcelestials.notification.super_blue_moon.set")), false, 5));
    }
}
