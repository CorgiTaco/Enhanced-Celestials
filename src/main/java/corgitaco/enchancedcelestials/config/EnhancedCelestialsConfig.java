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

    public static ForgeConfigSpec.DoubleValue harvestMoonCropGrowthChanceMultiplier;
    public static ForgeConfigSpec.DoubleValue harvestMoonCropDropsMultiplier;
    public static ForgeConfigSpec.ConfigValue<List<String>> blacklistedCropDropItems;
    public static ForgeConfigSpec.ConfigValue<List<String>> blacklistedCropGrowthBlocks;
    public static ForgeConfigSpec.BooleanValue cropGrowthBlocksBlacklist;
    public static ForgeConfigSpec.BooleanValue cropDropItemsBlacklist;



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
        spawnCapMultiplier = COMMON_BUILDER.comment("Multiply the monster spawn cap(70 monsters) by this value.\nRemember, more mobs = more server lag, so set this number responsibly!\nDefault is 5.0. aka 70 * 5.0 = 350 total mobs").defineInRange("MonsterSpawnCapMultiplier", 5.0, 1.5, 30.0);
        bloodMoonChance = COMMON_BUILDER.comment("The chance of a blood moon occurring each night. Chance is rolled at the daytime 13005.\nDefault 0.05").defineInRange("BloodMoonChance", 0.05, 0.01, 1.0);
        redClouds = COMMON_BUILDER.comment("Are blood moon clouds red?\nDefault true").define("BloodMoonRedClouds", true);
        COMMON_BUILDER.pop();
        COMMON_BUILDER.push("Harvest_Moon_Settings").push("Crop_Growth");
        harvestMoonCropGrowthChanceMultiplier = COMMON_BUILDER.comment("Multiplies the rate at which crops grow during harvest moons.\nDefault 50.0").defineInRange("CropGrowthMultiplier", 50.0, 1.0, 10000.0);
        blacklistedCropGrowthBlocks = COMMON_BUILDER.comment("This blacklist functions to remove any BLOCKS that might appear within the following BLOCK tags: \"minecraft/crops\", \"minecraft/bee_growables\", and \"minecraft/saplings\" BLOCK tags prior to multiplying the drops by the value of \"CropGrowthMultiplier\".\nThis allows us to skip these entries while keeping these blocks within those tags.\n\nWhen this is a whitelist, we do not consider those tags and instead multiply the growth rate of the blocks listed directly by the value of \"CropGrowthMultiplier\".").define("BlacklistedCropBlocks", new ArrayList<>());
        cropGrowthBlocksBlacklist = COMMON_BUILDER.comment("Does \"BlacklistedCropBlocks\" function as a blacklist? See \"BlacklistedCropBlocks\" comment for more details.\nFalse - Uses list as whitelist\nTrue - Uses list as blacklist\nDefault: true").define("CropBlocksBlacklist_FunctionsAsBlacklist", true);
        COMMON_BUILDER.pop();
        COMMON_BUILDER.push("Crop_Drops");
        harvestMoonCropDropsMultiplier = COMMON_BUILDER.comment("Multiplies the rate at which crop items drop during harvest moons.\nDefault 2.0").defineInRange("CropItemDropMultiplier", 2.5, 1.0, 1000.0);
        blacklistedCropDropItems = COMMON_BUILDER.comment("This blacklist functions to remove any ITEMS that might appear within the following ITEM tags: \"forge/crops\", \"forge/fruits\", and \"forge/vegetable\" ITEM tags prior to multiplying the drops by the value of \"CropItemDropMultiplier\".\nThis allows us to skip these entries while keeping these blocks within those tags.\n\nWhen this is a whitelist, we do not consider those tags and instead multiply the drops of the items listed directly by the value of \"CropItemDropMultiplier\".").define("BlacklistedCropItems", defaultBlacklistedCropItems);
        cropDropItemsBlacklist = COMMON_BUILDER.comment("Does \"BlacklistedCropItems\" function as a blacklist? See \"BlacklistedCropItems\" comment for more details.\nFalse - Uses list as whitelist\nTrue - Uses list as blacklist\nDefault: true").define("CropBlocksBlacklist_FunctionsAsBlacklist", true);
        COMMON_BUILDER.pop();
        COMMON_BUILDER.pop();
        COMMON_CONFIG = COMMON_BUILDER.build();
    }

    @SubscribeEvent
    public static void onLoad(final ModConfig.Loading configEvent) {

    }
}
