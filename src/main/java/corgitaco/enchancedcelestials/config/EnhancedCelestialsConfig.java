package corgitaco.enchancedcelestials.config;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
import corgitaco.enchancedcelestials.EnhancedCelestials;
import net.minecraft.block.Blocks;
import net.minecraft.util.Util;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;

import java.nio.file.Path;
import java.util.*;

@Mod.EventBusSubscriber(modid = EnhancedCelestials.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class EnhancedCelestialsConfig {
    private static final ForgeConfigSpec.Builder COMMON_BUILDER = new ForgeConfigSpec.Builder();
    public static ForgeConfigSpec COMMON_CONFIG;
    public static ForgeConfigSpec.DoubleValue spawnCapMultiplier;
    public static ForgeConfigSpec.DoubleValue bloodMoonChance;
    public static ForgeConfigSpec.BooleanValue redClouds;
    public static ForgeConfigSpec.BooleanValue bloodMoonNotification;
    public static ForgeConfigSpec.DoubleValue harvestMoonCropGrowthChanceMultiplier;
    public static ForgeConfigSpec.DoubleValue harvestMoonCropDropsMultiplier;
    public static ForgeConfigSpec.DoubleValue harvestMoonChance;
    public static ForgeConfigSpec.BooleanValue harvestMoonNotification;

    public static List<String> defaultBlacklistedCropItems = Util.make(new ArrayList<>(), (list) -> {
        list.add(Registry.BLOCK.getKey(Blocks.MELON).toString());
        list.add(Registry.BLOCK.getKey(Blocks.PUMPKIN).toString());
    });

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
        spawnCapMultiplier = COMMON_BUILDER.comment("Multiply the monster spawn cap (70 monsters) by this value.\nRemember, more mobs = more server lag, so set this number responsibly!\nDefault is 5.0. aka 70 * 5.0 = 350 total mobs").defineInRange("MonsterSpawnCapMultiplier", 5.0, 1.5, 30.0);
        bloodMoonChance = COMMON_BUILDER.comment("The chance of Blood Moon occurring each night. Chance is rolled at the daytime 12000.\nDefault 0.05").defineInRange("BloodMoonChance", 0.05, 0.01, 1.0);
        bloodMoonNotification = COMMON_BUILDER.comment("Show notifications of Blood Moon?\nDefault true").define("BloodMoonNotification", true);
        redClouds = COMMON_BUILDER.comment("Are Blood Moon clouds red?\nDefault true").define("BloodMoonRedClouds", true);
        COMMON_BUILDER.pop();
        COMMON_BUILDER.push("Harvest_Moon_Settings");
        harvestMoonChance = COMMON_BUILDER.comment("The chance of a Harvest Moon occurring each night. Chance is rolled at the daytime 12000.\nDefault 0.025").defineInRange("HarvestMoonChance", 0.025, 0.0, 1.0);
        harvestMoonNotification = COMMON_BUILDER.comment("Show notifications of Harvest Moon?\nDefault true").define("HarvestMoonNotification", true);
        harvestMoonCropGrowthChanceMultiplier = COMMON_BUILDER.comment("Multiplies the rate at which crops grow during Harvest Moon.\nDefault 15.0").defineInRange("CropGrowthMultiplier", 15.0, 1.0, 10000.0);
        harvestMoonCropDropsMultiplier = COMMON_BUILDER.comment("Multiplies the rate at which crop items drop during Harvest Moon.\nDefault 2.0").defineInRange("CropItemDropMultiplier", 2.5, 1.0, 1000.0);
        COMMON_BUILDER.pop();
        COMMON_CONFIG = COMMON_BUILDER.build();
    }

    @SubscribeEvent
    public static void onLoad(final ModConfig.Loading configEvent) {
    }
}