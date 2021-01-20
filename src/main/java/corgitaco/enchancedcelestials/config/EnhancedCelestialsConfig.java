package corgitaco.enchancedcelestials.config;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
import corgitaco.enchancedcelestials.EnhancedCelestials;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;

import java.nio.file.Path;

@Mod.EventBusSubscriber(modid = EnhancedCelestials.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class EnhancedCelestialsConfig {

    private static final ForgeConfigSpec.Builder COMMON_BUILDER = new ForgeConfigSpec.Builder();
    public static ForgeConfigSpec COMMON_CONFIG;
    public static ForgeConfigSpec.DoubleValue spawnCapMultiplier;
    public static ForgeConfigSpec.DoubleValue bloodMoonChance;
    public static ForgeConfigSpec.BooleanValue redClouds;

    public static void loadConfig(Path path) {
        EnhancedCelestials.LOGGER.info("Loading config: " + path);
        refreshConfig();
        CommentedFileConfig file = CommentedFileConfig.builder(path).sync().autosave().writingMode(WritingMode.REPLACE).build();
        file.load();
        COMMON_CONFIG.setConfig(file);
    }

    private static void refreshConfig() {
        COMMON_BUILDER.comment("Blood Moon Settings");
        COMMON_BUILDER.push("Blood_Moon_Settings");
        spawnCapMultiplier = COMMON_BUILDER.comment("Multiply the monster spawn cap(70 monsters) by this value.\nDefault is 5.0. aka 70 * 5.0 = 350 total mobs").defineInRange("MonsterSpawnCapMultiplier", 5.0, 1.5, 30.0);
        bloodMoonChance = COMMON_BUILDER.comment("The chance of a blood moon occurring each night. Chance is rolled at the daytime 13005.\nDefault 0.05").defineInRange("BloodMoonChance", 0.05, 0.01, 1.0);
        redClouds = COMMON_BUILDER.comment("Are blood moon clouds red?\nDefault true").define("BloodMoonRedClouds", true);
        COMMON_BUILDER.pop();
        COMMON_CONFIG = COMMON_BUILDER.build();
    }

    @SubscribeEvent
    public static void onLoad(final ModConfig.Loading configEvent) {

    }
}
