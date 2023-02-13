package corgitaco.enhancedcelestials.mixin;

import corgitaco.enhancedcelestials.EnhancedCelestialsWorldData;
import corgitaco.enhancedcelestials.core.EnhancedCelestialsContext;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.levelgen.Heightmap;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static net.minecraft.world.entity.Mob.checkMobSpawnRules;

@Mixin(Slime.class)
public class MixinSlimeEntity {


    @Inject(method = "checkSlimeSpawnRules", at = @At("HEAD"), cancellable = true)
    private static void allowSlimeSpawnsAnywhere(EntityType<Slime> slimeEntityType, LevelAccessor accessor, MobSpawnType spawnType, BlockPos pos, RandomSource randomSource, CallbackInfoReturnable<Boolean> cir) {
        if (accessor instanceof ServerLevel serverLevel) {
            EnhancedCelestialsContext enhancedCelestialsContext = ((EnhancedCelestialsWorldData) serverLevel).getLunarContext();
            if (enhancedCelestialsContext != null) {
                boolean slimesSpawnEverywhere = enhancedCelestialsContext.getLunarForecast().getCurrentEvent(serverLevel.getRainLevel(1) < 1).value().getLunarMobSettings().lunarMobSpawnInfo().slimesSpawnEverywhere();

                boolean aboveY = pos.getY() > 50 && pos.getY() >= accessor.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, pos.getX(), pos.getZ()) - 1;

                if (slimesSpawnEverywhere && aboveY && randomSource.nextFloat() < 0.5F && randomSource.nextFloat() < accessor.getMoonBrightness() && accessor.getMaxLocalRawBrightness(pos) <= randomSource.nextInt(8)) {
                    cir.setReturnValue(checkMobSpawnRules(slimeEntityType, accessor, spawnType, pos, randomSource));
                }
            }
        }
    }
}
