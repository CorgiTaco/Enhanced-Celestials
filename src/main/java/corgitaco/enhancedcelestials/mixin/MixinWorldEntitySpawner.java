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
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.IdentityHashMap;
import java.util.List;

@Mixin(WorldEntitySpawner.class)
public class MixinWorldEntitySpawner {

    @Inject(method = "func_241463_a_", at = @At("RETURN"), cancellable = true)
    private static void useLunarSpawner(ServerWorld world, StructureManager structureManager, ChunkGenerator generator, EntityClassification classification, BlockPos pos, Biome biome, CallbackInfoReturnable<List<MobSpawnInfo.Spawners>> cir) {
        LunarContext lunarContext = ((EnhancedCelestialsWorldData) world).getLunarContext();
        if (lunarContext != null) {
            LunarMobSpawnInfo lunarSpawner = lunarContext.getCurrentEvent().getLunarSpawner();
            if (lunarSpawner != null) {
                MobSpawnInfo mobSpawnInfo = lunarSpawner.getSpawnInfo();
                if (lunarSpawner.useBiomeSpawnSettings()) {
                    ArrayList<MobSpawnInfo.Spawners> spawners = new ArrayList<>(mobSpawnInfo.getSpawners(classification));
                    spawners.addAll(cir.getReturnValue());
                    cir.setReturnValue(spawners);
                } else {
                    cir.setReturnValue(mobSpawnInfo.getSpawners(classification));
                }
            }
        }
    }

    @Inject(method = "func_234980_b_", at = @At("RETURN"), cancellable = true)
    private static void useLunarSpawner(BlockPos pos, IChunk chunk, CallbackInfoReturnable<Biome> cir) {
        if (chunk instanceof Chunk) {
            World world = ((ChunkAccess) chunk).getWorld();
            LunarContext lunarContext = ((EnhancedCelestialsWorldData) world).getLunarContext();
            if (lunarContext != null) {
                LunarMobSpawnInfo lunarSpawner = lunarContext.getCurrentEvent().getLunarSpawner();
                if (lunarSpawner != null) {
                    MobSpawnInfo lunarMobSpawnInfo = lunarSpawner.getSpawnInfo();
                    Biome.Builder fakeBiome = (new Biome.Builder()).precipitation(Biome.RainType.NONE).category(Biome.Category.NONE).depth(0.1F).scale(0.2F).temperature(0.5F).downfall(0.5F).setEffects((new BiomeAmbience.Builder()).setWaterColor(4159204).setWaterFogColor(329011).setFogColor(12638463).withSkyColor(1).setMoodSound(MoodSoundAmbience.DEFAULT_CAVE).build());
                    if (lunarSpawner.useBiomeSpawnSettings()) {
                        MobSpawnInfo biomeMobSpawnInfo = cir.getReturnValue().getMobSpawnInfo();
                        EnumMap<EntityClassification, List<MobSpawnInfo.Spawners>> mergedSpawnersMap = new EnumMap<>(((MobSpawnInfoAccess) biomeMobSpawnInfo).getSpawners());
                        mergedSpawnersMap.putAll(((MobSpawnInfoAccess) lunarMobSpawnInfo).getSpawners());

                        IdentityHashMap<EntityType<?>, MobSpawnInfo.SpawnCosts> mergedSpawnCosts = new IdentityHashMap<>(((MobSpawnInfoAccess) biomeMobSpawnInfo).getSpawnCosts());
                        mergedSpawnCosts.putAll(((MobSpawnInfoAccess) lunarMobSpawnInfo).getSpawnCosts());

                        MobSpawnInfo mobSpawnInfo = MobSpawnInfoAccess.create(Math.max(lunarMobSpawnInfo.getCreatureSpawnProbability(), biomeMobSpawnInfo.getCreatureSpawnProbability()), mergedSpawnersMap, mergedSpawnCosts, biomeMobSpawnInfo.isValidSpawnBiomeForPlayer());
                        fakeBiome.withMobSpawnSettings(mobSpawnInfo);
                    } else {
                        fakeBiome.withMobSpawnSettings(lunarMobSpawnInfo);
                    }
                    fakeBiome.withGenerationSettings(BiomeGenerationSettings.DEFAULT_SETTINGS);
                    cir.setReturnValue(fakeBiome.build());
                }
            }
        }
    }

    @Inject(method = "getRandomHeight", at = @At("RETURN"), cancellable = true)
    private static void forceSurface(World world, Chunk chunk, CallbackInfoReturnable<BlockPos> cir) {
        LunarContext lunarContext = ((EnhancedCelestialsWorldData) world).getLunarContext();
        if (lunarContext != null) {
            LunarMobSpawnInfo lunarSpawner = lunarContext.getCurrentEvent().getLunarSpawner();
            if (lunarSpawner != null) {
                if (lunarSpawner.isForceSurfaceSpawning()) {
                    BlockPos returnValue = cir.getReturnValue();
                    PlayerEntity closestPlayer = world.getClosestPlayer(returnValue.getX(), returnValue.getY(), returnValue.getZ(), -1.0, false);
                    if (closestPlayer != null) {
                        BlockPos closestPlayerPosition = closestPlayer.getPosition();
                        if (closestPlayerPosition.getY() > world.getHeight(Heightmap.Type.WORLD_SURFACE, closestPlayerPosition.getX(), closestPlayerPosition.getZ())) {
                            cir.setReturnValue(new BlockPos(returnValue.getX(), world.getHeight(Heightmap.Type.WORLD_SURFACE, returnValue.getX(), returnValue.getZ()) + 1, returnValue.getZ()));
                        }
                    }
                }
            }
        }
    }
}