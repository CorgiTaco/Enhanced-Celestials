package dev.corgitaco.enhancedcelestials.mixin;

import dev.corgitaco.enhancedcelestials.EnhancedCelestialsWorldData;
import dev.corgitaco.enhancedcelestials.core.EnhancedCelestialsContext;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.server.level.ServerLevel;
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
        ServerLevel level = this.localMobCapCalculator.chunkMap.level;
        EnhancedCelestialsContext enhancedCelestialsContext = ((EnhancedCelestialsWorldData) level).getLunarContext();
        if (enhancedCelestialsContext != null) {
            int i = (int) (entityClassification.getMaxInstancesPerChunk() * (this.spawnableChunkCount * enhancedCelestialsContext.getLunarForecast().getCurrentEvent(level.getRainLevel(1) < 1).value().getSpawnMultiplierForMonsterCategory(entityClassification)) / NaturalSpawner.MAGIC_NUMBER);
            // Global Calculation
            if (this.mobCategoryCounts.getInt(entityClassification) >= i) {
                cir.setReturnValue(false);
            } else {
                cir.setReturnValue(this.localMobCapCalculator.canSpawn(entityClassification, chunkPos));
            }
        }
    }
}
