package dev.corgitaco.enhancedcelestials.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import dev.corgitaco.enhancedcelestials.EnhancedCelestials;
import dev.corgitaco.enhancedcelestials.lunarevent.EnhancedCelestialsLunarForecastWorldData;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BeaconBlockEntity;
import net.minecraft.world.phys.AABB;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Optional;

@Mixin(BeaconBlockEntity.class)
public class BeaconBlockEntityMixin {


    @ModifyExpressionValue(method = "applyEffects", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/phys/AABB;expandTowards(DDD)Lnet/minecraft/world/phys/AABB;"))
    private static AABB scaleBeaconArea(AABB original, Level level) {
        Optional<EnhancedCelestialsLunarForecastWorldData> enhancedCelestialsLunarForecastWorldData = EnhancedCelestials.lunarForecastWorldData(level);
        if (enhancedCelestialsLunarForecastWorldData.isEmpty()) {
            return original;
        }
        EnhancedCelestialsLunarForecastWorldData data = enhancedCelestialsLunarForecastWorldData.orElseThrow();

        double beaconRadiusAmplifier = data.currentLunarEvent().beaconRadiusAmplifier();
        return AABB.ofSize(original.getCenter(), original.getXsize() * beaconRadiusAmplifier, original.getYsize() * beaconRadiusAmplifier, original.getZsize() * beaconRadiusAmplifier);

    }
}
