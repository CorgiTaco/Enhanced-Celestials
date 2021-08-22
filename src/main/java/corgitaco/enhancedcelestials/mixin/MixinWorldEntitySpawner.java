package corgitaco.enhancedcelestials.mixin;

import corgitaco.enhancedcelestials.EnhancedCelestialsWorldData;
import corgitaco.enhancedcelestials.LunarContext;
import corgitaco.enhancedcelestials.api.lunarevent.LunarMobSpawnInfo;
import corgitaco.enhancedcelestials.mixin.access.ChunkAccess;
import corgitaco.enhancedcelestials.mixin.access.MobSpawnInfoAccess;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.*;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunk;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.feature.structure.StructureManager;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.spawner.WorldEntitySpawner;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.IdentityHashMap;
import java.util.List;

@Mixin(WorldEntitySpawner.class)
public class MixinWorldEntitySpawner {

    @Inject(method = "mobsAt", at = @At("RETURN"), cancellable = true)
    private static void useLunarSpawner(ServerWorld world, StructureManager structureManager, ChunkGenerator generator, EntityClassification classification, BlockPos pos, Biome biome, CallbackInfoReturnable<List<MobSpawnInfo.Spawners>> cir) {
        LunarContext lunarContext = ((EnhancedCelestialsWorldData) world).getLunarContext();
        if (lunarContext != null) {
            LunarMobSpawnInfo lunarSpawner = lunarContext.getCurrentEvent().getLunarSpawner();
            if (lunarSpawner != null) {
                MobSpawnInfo mobSpawnInfo = lunarSpawner.getSpawnInfo();
                if (lunarSpawner.useBiomeSpawnSettings()) {
                    ArrayList<MobSpawnInfo.Spawners> spawners = new ArrayList<>(mobSpawnInfo.getMobs(classification));
                    spawners.addAll(cir.getReturnValue());
                    cir.setReturnValue(spawners);
                } else {
                    cir.setReturnValue(mobSpawnInfo.getMobs(classification));
                }
            }
        }
    }

    @Inject(method = "getRoughBiome", at = @At("RETURN"), cancellable = true)
    private static void useLunarSpawner(BlockPos pos, IChunk chunk, CallbackInfoReturnable<Biome> cir) {
        if (chunk instanceof Chunk) {
            World world = ((ChunkAccess) chunk).getLevel();
            LunarContext lunarContext = ((EnhancedCelestialsWorldData) world).getLunarContext();
            if (lunarContext != null) {
                LunarMobSpawnInfo lunarSpawner = lunarContext.getCurrentEvent().getLunarSpawner();
                if (lunarSpawner != null) {
                    MobSpawnInfo lunarMobSpawnInfo = lunarSpawner.getSpawnInfo();
                    Biome.Builder fakeBiome = (new Biome.Builder()).precipitation(Biome.RainType.NONE).biomeCategory(Biome.Category.NONE).depth(0.1F).scale(0.2F).temperature(0.5F).downfall(0.5F).specialEffects((new BiomeAmbience.Builder()).waterColor(4159204).waterFogColor(329011).fogColor(12638463).skyColor(1).ambientMoodSound(MoodSoundAmbience.LEGACY_CAVE_SETTINGS).build());
                    if (lunarSpawner.useBiomeSpawnSettings()) {
                        MobSpawnInfo biomeMobSpawnInfo = cir.getReturnValue().getMobSettings();
                        EnumMap<EntityClassification, List<MobSpawnInfo.Spawners>> mergedSpawnersMap = new EnumMap<>(((MobSpawnInfoAccess) biomeMobSpawnInfo).getSpawners());
                        mergedSpawnersMap.putAll(((MobSpawnInfoAccess) lunarMobSpawnInfo).getSpawners());

                        IdentityHashMap<EntityType<?>, MobSpawnInfo.SpawnCosts> mergedSpawnCosts = new IdentityHashMap<>(((MobSpawnInfoAccess) biomeMobSpawnInfo).getMobSpawnCosts());
                        mergedSpawnCosts.putAll(((MobSpawnInfoAccess) lunarMobSpawnInfo).getMobSpawnCosts());

                        MobSpawnInfo mobSpawnInfo = MobSpawnInfoAccess.create(Math.max(lunarMobSpawnInfo.getCreatureProbability(), biomeMobSpawnInfo.getCreatureProbability()), mergedSpawnersMap, mergedSpawnCosts, biomeMobSpawnInfo.playerSpawnFriendly());
                        fakeBiome.mobSpawnSettings(mobSpawnInfo);
                    } else {
                        fakeBiome.mobSpawnSettings(lunarMobSpawnInfo);
                    }
                    fakeBiome.generationSettings(BiomeGenerationSettings.EMPTY);
                    cir.setReturnValue(fakeBiome.build());
                }
            }
        }
    }

    @Inject(method = "getRandomPosWithin", at = @At("RETURN"), cancellable = true)
    private static void forceSurface(World world, Chunk chunk, CallbackInfoReturnable<BlockPos> cir) {
        LunarContext lunarContext = ((EnhancedCelestialsWorldData) world).getLunarContext();
        if (lunarContext != null) {
            LunarMobSpawnInfo lunarSpawner = lunarContext.getCurrentEvent().getLunarSpawner();
            if (lunarSpawner != null) {
                if (lunarSpawner.isForceSurfaceSpawning()) {
                    BlockPos returnValue = cir.getReturnValue();
                    PlayerEntity closestPlayer = world.getNearestPlayer(returnValue.getX(), returnValue.getY(), returnValue.getZ(), -1.0, false);
                    if (closestPlayer != null) {
                        BlockPos closestPlayerPosition = closestPlayer.blockPosition();
                        if (closestPlayerPosition.getY() > world.getHeight(Heightmap.Type.WORLD_SURFACE, closestPlayerPosition.getX(), closestPlayerPosition.getZ())) {
                            cir.setReturnValue(new BlockPos(returnValue.getX(), world.getHeight(Heightmap.Type.WORLD_SURFACE, returnValue.getX(), returnValue.getZ()) + 1, returnValue.getZ()));
                        }
                    }
                }
            }
        }
    }
}