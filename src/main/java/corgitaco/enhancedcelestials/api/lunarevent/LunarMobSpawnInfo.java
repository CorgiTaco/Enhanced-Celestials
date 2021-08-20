package corgitaco.enhancedcelestials.api.lunarevent;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.level.biome.MobSpawnSettings;

public class LunarMobSpawnInfo {
    public static final Codec<LunarMobSpawnInfo> CODEC = RecordCodecBuilder.create((builder) -> {
       return builder.group(Codec.BOOL.fieldOf("useBiomeSpawnSettings").orElse(false).forGetter((lunarmobspawninfo) -> {
           return lunarmobspawninfo.useBiomeSpawnSettings;
       }), Codec.BOOL.fieldOf("forceSurfaceSpawning").orElse(false).forGetter((lunarmobspawninfo) -> {
           return lunarmobspawninfo.forceSurfaceSpawning;
       }), MobSpawnSettings.CODEC.forGetter((lunarMobSpawnInfo) -> {
           return lunarMobSpawnInfo.spawnInfo;
       })).apply(builder, LunarMobSpawnInfo::new);
    });

    private final boolean useBiomeSpawnSettings;
    private final boolean forceSurfaceSpawning;
    private final MobSpawnSettings spawnInfo;

    public LunarMobSpawnInfo(boolean useBiomeSpawnSettings, boolean forceSurfaceSpawning, MobSpawnSettings spawnInfo) {
        this.useBiomeSpawnSettings = useBiomeSpawnSettings;
        this.spawnInfo = spawnInfo;
        this.forceSurfaceSpawning = forceSurfaceSpawning;
    }

    public boolean useBiomeSpawnSettings() {
        return useBiomeSpawnSettings;
    }

    public MobSpawnSettings getSpawnInfo() {
        return spawnInfo;
    }

    public boolean isForceSurfaceSpawning() {
        return forceSurfaceSpawning;
    }
}
