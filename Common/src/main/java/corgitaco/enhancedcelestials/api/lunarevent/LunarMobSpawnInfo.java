package corgitaco.enhancedcelestials.api.lunarevent;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.level.biome.MobSpawnSettings;

public record LunarMobSpawnInfo(boolean useBiomeSpawnSettings, boolean forceSurfaceSpawning, boolean slimesSpawnEverywhere,
                                MobSpawnSettings spawnInfo) {
    public static final LunarMobSpawnInfo DEFAULT = new LunarMobSpawnInfo(true, false, false, MobSpawnSettings.EMPTY);


    public static final Codec<LunarMobSpawnInfo> CODEC = RecordCodecBuilder.create(builder ->
            builder.group(
                    Codec.BOOL.fieldOf("use_biome_spawn_settings").orElse(false).forGetter(LunarMobSpawnInfo::useBiomeSpawnSettings),
                    Codec.BOOL.fieldOf("force_surface_spawning").orElse(false).forGetter(LunarMobSpawnInfo::forceSurfaceSpawning),
                    Codec.BOOL.fieldOf("slimes_spawn_everywhere").orElse(false).forGetter(LunarMobSpawnInfo::slimesSpawnEverywhere),
                    MobSpawnSettings.CODEC.forGetter(LunarMobSpawnInfo::spawnInfo)
            ).apply(builder, LunarMobSpawnInfo::new)
    );
}
