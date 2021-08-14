package corgitaco.enhancedcelestials;

import corgitaco.enhancedcelestials.api.EnhancedCelestialsRegistry;
import corgitaco.enhancedcelestials.api.client.ColorSettings;
import corgitaco.enhancedcelestials.lunarevent.BloodMoon;
import corgitaco.enhancedcelestials.lunarevent.BlueMoon;
import corgitaco.enhancedcelestials.lunarevent.HarvestMoon;
import corgitaco.enhancedcelestials.lunarevent.client.MoonClientSettings;
import corgitaco.enhancedcelestials.network.NetworkHandler;
import it.unimi.dsi.fastutil.objects.Object2DoubleArrayMap;
import net.minecraft.entity.EntityClassification;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Util;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.nio.file.Path;
import java.util.Collections;

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

        EnhancedCelestialsRegistry.DEFAULT_EVENTS.put(new ResourceLocation(MOD_ID, "blood_moon"),new BloodMoon(new MoonClientSettings(new ColorSettings("FF0000", 0.6, "FF0000", 0.4)), false, 4, 1.0, Collections.singleton(0), Util.make(new Object2DoubleArrayMap<>(), (map) -> map.put(EntityClassification.MONSTER, 3.0))));
        EnhancedCelestialsRegistry.DEFAULT_EVENTS.put(new ResourceLocation(MOD_ID, "harvest_moon"),new HarvestMoon(new MoonClientSettings(new ColorSettings("FFDB63", 0.6, "FFDB63", 0.4)), false, 4, 1.0, Collections.singleton(0)));
    }
}
