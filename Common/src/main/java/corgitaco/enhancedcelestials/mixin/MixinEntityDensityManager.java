package corgitaco.enhancedcelestials.mixin;

import corgitaco.enhancedcelestials.EnhancedCelestialsWorldData;
import corgitaco.enhancedcelestials.LunarContext;
import corgitaco.enhancedcelestials.mixin.access.ChunkMapAccess;
import corgitaco.enhancedcelestials.mixin.access.LocalMobCapCalculatorAccess;
import corgitaco.enhancedcelestials.mixin.access.WorldEntitySpawnerAccess;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LocalMobCapCalculator;
import net.minecraft.world.level.NaturalSpawner;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(NaturalSpawner.SpawnState.class)
public class MixinEntityDensityManager {

    @Shadow
    @Final
    private Object2IntOpenHashMap<MobCategory> mobCategoryCounts;
    @Shadow
    @Final
    private int spawnableChunkCount;
    @Shadow
    @Final
    private LocalMobCapCalculator localMobCapCalculator;

    @Inject(method = "canSpawnForCategory", at = @At("HEAD"), cancellable = true)
    private void modifySpawnCapByCategory(MobCategory entityClassification, ChunkPos chunkPos, CallbackInfoReturnable<Boolean> cir) {
        LunarContext lunarContext = ((EnhancedCelestialsWorldData) ((ChunkMapAccess) ((LocalMobCapCalculatorAccess) this.localMobCapCalculator).getChunkMap()).getLevel()).getLunarContext();
        if (lunarContext != null) {
            int i = (int) (entityClassification.getMaxInstancesPerChunk() * (this.spawnableChunkCount * lunarContext.getCurrentEvent().getSpawnMultiplierForMonsterCategory(entityClassification)) / WorldEntitySpawnerAccess.getMagicNumber());
            // Global Calculation
            if (this.mobCategoryCounts.getInt(entityClassification) >= i) {
                cir.setReturnValue(false);
            } else {
                cir.setReturnValue(this.localMobCapCalculator.canSpawn(entityClassification, chunkPos));
            }
        }
    }
}
