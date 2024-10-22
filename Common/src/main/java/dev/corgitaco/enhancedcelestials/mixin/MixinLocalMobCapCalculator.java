package dev.corgitaco.enhancedcelestials.mixin;

import dev.corgitaco.enhancedcelestials.EnhancedCelestialsWorldData;
import dev.corgitaco.enhancedcelestials.core.EnhancedCelestialsContext;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.minecraft.server.level.ChunkMap;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.LocalMobCapCalculator;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import org.jetbrains.annotations.Nullable;

@Mixin(LocalMobCapCalculator.class)
public class MixinLocalMobCapCalculator {

    @Shadow @Final private ChunkMap chunkMap;

    // TODO: Do we want to make more mobs spawn on players with better gear?!
    @Redirect(method = "canSpawn", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/LocalMobCapCalculator$MobCounts;canSpawn(Lnet/minecraft/world/entity/MobCategory;)Z"))
    private boolean useLunarEventMobCap(LocalMobCapCalculator.MobCounts instance, MobCategory mobCategory) {
        ServerLevel level = this.chunkMap.level;
        @Nullable
        EnhancedCelestialsContext enhancedCelestialsContext = ((EnhancedCelestialsWorldData) level).getLunarContext();
        if (enhancedCelestialsContext != null) {
            Object2IntMap<MobCategory> counts = instance.counts;
            final int currentCount = counts.getOrDefault(mobCategory, 0);
            return currentCount < mobCategory.getMaxInstancesPerChunk() * enhancedCelestialsContext.getLunarForecast().getCurrentEvent(level.getRainLevel(1) < 1).value().getSpawnMultiplierForMonsterCategory(mobCategory);
        } else {
            return instance.canSpawn(mobCategory);
        }
    }
}
