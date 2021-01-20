package corgitaco.enchancedcelestials.mixin.client;

import corgitaco.enchancedcelestials.EnhancedCelestials;
import net.minecraft.world.biome.Biome;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.awt.*;

@Mixin(Biome.class)
public class MixinBiomeClient {

    @Inject(method = "getSkyColor", at = @At("RETURN"), cancellable = true)
    private void modifyLunarEventSkyColor(CallbackInfoReturnable<Integer> cir) {
        if (EnhancedCelestials.currentLunarEvent != null) {
            EnhancedCelestials.currentLunarEvent.modifySkyColor(new Color(cir.getReturnValue()));
        }
    }

    @Inject(method = "getFogColor", at = @At("RETURN"), cancellable = true)
    private void modifyLunarEventFogColor(CallbackInfoReturnable<Integer> cir) {
        if (EnhancedCelestials.currentLunarEvent != null) {
            EnhancedCelestials.currentLunarEvent.modifyFogColor(new Color(cir.getReturnValue()));
        }
    }

    @Inject(method = "getWaterColor", at = @At("RETURN"), cancellable = true)
    private void modifyLunarEventWaterColor(CallbackInfoReturnable<Integer> cir) {
        if (EnhancedCelestials.currentLunarEvent != null) {
            EnhancedCelestials.currentLunarEvent.modifyWaterColor(new Color(cir.getReturnValue()));
        }
    }

    @Inject(method = "getWaterFogColor", at = @At("RETURN"), cancellable = true)
    private void modifyLunarEventWaterFogColor(CallbackInfoReturnable<Integer> cir) {
        if (EnhancedCelestials.currentLunarEvent != null) {
            EnhancedCelestials.currentLunarEvent.modifyWaterFogColor(new Color(cir.getReturnValue()));
        }
    }
}
