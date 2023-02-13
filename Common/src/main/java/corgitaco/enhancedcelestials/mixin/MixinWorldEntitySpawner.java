package corgitaco.enhancedcelestials.mixin;

import corgitaco.enhancedcelestials.EnhancedCelestialsWorldData;
import corgitaco.enhancedcelestials.api.lunarevent.LunarMobSpawnInfo;
import corgitaco.enhancedcelestials.core.EnhancedCelestialsContext;
import corgitaco.enhancedcelestials.mixin.access.ChunkAccessAccess;
import corgitaco.enhancedcelestials.mixin.access.MobSpawnInfoAccess;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.random.WeightedRandomList;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.NaturalSpawner;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.biome.*;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.levelgen.Heightmap;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.IdentityHashMap;
import java.util.List;

@Mixin(NaturalSpawner.class)
public class MixinWorldEntitySpawner {

    @Inject(method = "mobsAt", at = @At("RETURN"), cancellable = true)
    private static void useLunarSpawner(ServerLevel world, StructureManager $$1, ChunkGenerator $$2, MobCategory classification, BlockPos $$4, Holder<Biome> $$5, CallbackInfoReturnable<WeightedRandomList<MobSpawnSettings.SpawnerData>> cir) {
        EnhancedCelestialsContext enhancedCelestialsContext = ((EnhancedCelestialsWorldData) world).getLunarContext();
        if (enhancedCelestialsContext != null) {
            LunarMobSpawnInfo lunarSpawner = enhancedCelestialsContext.getLunarForecast().getCurrentEvent(world.getRainLevel(1) < 1).value().getLunarSpawner();
            if (lunarSpawner != null) {
                MobSpawnSettings mobSpawnInfo = lunarSpawner.spawnInfo();
                if (lunarSpawner.useBiomeSpawnSettings()) {
                    List<MobSpawnSettings.SpawnerData> unwrap = new ArrayList<>(mobSpawnInfo.getMobs(classification).unwrap());
                    unwrap.addAll(cir.getReturnValue().unwrap());
                    cir.setReturnValue(WeightedRandomList.create(unwrap));
                } else {
                    cir.setReturnValue(mobSpawnInfo.getMobs(classification));
                }
            }
        }
    }

    @Inject(method = "getRoughBiome", at = @At("RETURN"), cancellable = true)
    private static void useLunarSpawner(BlockPos pos, net.minecraft.world.level.chunk.ChunkAccess chunk, CallbackInfoReturnable<Biome> cir) {
        if (chunk instanceof LevelChunk) {
            Level world = ((ChunkAccessAccess) chunk).getLevel();
            EnhancedCelestialsContext enhancedCelestialsContext = ((EnhancedCelestialsWorldData) world).getLunarContext();
            if (enhancedCelestialsContext != null) {
                LunarMobSpawnInfo lunarSpawner = enhancedCelestialsContext.getLunarForecast().getCurrentEvent(world.getRainLevel(1) < 1).value().getLunarSpawner();
                if (lunarSpawner != null) {
                    MobSpawnSettings lunarMobSpawnInfo = lunarSpawner.spawnInfo();
                    Biome.BiomeBuilder fakeBiome = (new Biome.BiomeBuilder()).precipitation(Biome.Precipitation.NONE).temperature(0.5F).downfall(0.5F).specialEffects((new BiomeSpecialEffects.Builder()).waterColor(4159204).waterFogColor(329011).fogColor(12638463).skyColor(1).ambientMoodSound(AmbientMoodSettings.LEGACY_CAVE_SETTINGS).build());
                    if (lunarSpawner.useBiomeSpawnSettings()) {
                        MobSpawnSettings biomeMobSpawnInfo = cir.getReturnValue().getMobSettings();
                        EnumMap<MobCategory, List<MobSpawnSettings.SpawnerData>> mergedSpawnersMap = new EnumMap<>(((MobSpawnInfoAccess) biomeMobSpawnInfo).getSpawners());
                        mergedSpawnersMap.putAll(((MobSpawnInfoAccess) lunarMobSpawnInfo).getSpawners());

                        IdentityHashMap<EntityType<?>, MobSpawnSettings.MobSpawnCost> mergedSpawnCosts = new IdentityHashMap<>(((MobSpawnInfoAccess) biomeMobSpawnInfo).getMobSpawnCosts());
                        mergedSpawnCosts.putAll(((MobSpawnInfoAccess) lunarMobSpawnInfo).getMobSpawnCosts());

                        MobSpawnSettings mobSpawnInfo = MobSpawnInfoAccess.create(Math.max(lunarMobSpawnInfo.getCreatureProbability(), biomeMobSpawnInfo.getCreatureProbability()), mergedSpawnersMap, mergedSpawnCosts);
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
    private static void forceSurface(Level world, LevelChunk chunk, CallbackInfoReturnable<BlockPos> cir) {
        EnhancedCelestialsContext enhancedCelestialsContext = ((EnhancedCelestialsWorldData) world).getLunarContext();
        if (enhancedCelestialsContext != null) {
            LunarMobSpawnInfo lunarSpawner = enhancedCelestialsContext.getLunarForecast().getCurrentEvent(world.getRainLevel(1) < 1).value().getLunarSpawner();
            if (lunarSpawner != null) {
                if (lunarSpawner.forceSurfaceSpawning()) {
                    BlockPos returnValue = cir.getReturnValue();
                    Player closestPlayer = world.getNearestPlayer(returnValue.getX(), returnValue.getY(), returnValue.getZ(), -1.0, false);
                    if (closestPlayer != null) {
                        BlockPos closestPlayerPosition = closestPlayer.blockPosition();
                        if (closestPlayerPosition.getY() > world.getHeight(Heightmap.Types.WORLD_SURFACE, closestPlayerPosition.getX(), closestPlayerPosition.getZ())) {
                            cir.setReturnValue(new BlockPos(returnValue.getX(), world.getHeight(Heightmap.Types.WORLD_SURFACE, returnValue.getX(), returnValue.getZ()) + 1, returnValue.getZ()));
                        }
                    }
                }
            }
        }
    }
}