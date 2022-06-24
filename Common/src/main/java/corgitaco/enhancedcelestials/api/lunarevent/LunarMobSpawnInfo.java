package corgitaco.enhancedcelestials.api.lunarevent;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.level.biome.MobSpawnSettings;

public record LunarMobSpawnInfo(boolean useBiomeSpawnSettings, boolean forceSurfaceSpawning,
                                MobSpawnSettings spawnInfo) {
    public static final LunarMobSpawnInfo DEFAULT = new LunarMobSpawnInfo(true, false, MobSpawnSettings.EMPTY);


    public static final Codec<LunarMobSpawnInfo> CODEC = RecordCodecBuilder.create((builder) -> {
        return builder.group(Codec.BOOL.fieldOf("useBiomeSpawnSettings").orElse(false).forGetter((lunarmobspawninfo) -> {
            return lunarmobspawninfo.useBiomeSpawnSettings;
        }), Codec.BOOL.fieldOf("forceSurfaceSpawning").orElse(false).forGetter((lunarmobspawninfo) -> {
            return lunarmobspawninfo.forceSurfaceSpawning;
        }), MobSpawnSettings.CODEC.forGetter((lunarMobSpawnInfo) -> {
            return lunarMobSpawnInfo.spawnInfo;
        })).apply(builder, LunarMobSpawnInfo::new);
    });

}
