package corgitaco.enhancedcelestials.mixin.client;

import com.mojang.math.Vector3f;
import corgitaco.enhancedcelestials.EnhancedCelestialsWorldData;
import corgitaco.enhancedcelestials.LunarContext;
import corgitaco.enhancedcelestials.LunarForecast;
import corgitaco.enhancedcelestials.api.client.ColorSettings;
import corgitaco.enhancedcelestials.api.lunarevent.LunarEvent;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.util.Mth;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(LightTexture.class)
public abstract class MixinLightMapTexture {
    @Inject(method = "updateLightTexture", at = @At(value = "INVOKE", target = "Lcom/mojang/math/Vector3f;lerp(Lcom/mojang/math/Vector3f;F)V", ordinal = 0), locals = LocalCapture.CAPTURE_FAILHARD)
    private void doOurLightMap(float $$0, CallbackInfo ci, ClientLevel clientLevel, float $$2, float $$4, float $$5, float $$6, float $$7, float $$11, float $$8, Vector3f skyVector) {
        LunarContext lunarContext = ((EnhancedCelestialsWorldData) clientLevel).getLunarContext();
        if (lunarContext != null) {
            LunarForecast lunarForecast = lunarContext.getLunarForecast();
            LunarEvent lastEvent = lunarForecast.getMostRecentEvent().value();
            LunarEvent currentEvent = lunarForecast.getCurrentEvent().value();

            ColorSettings colorSettings = currentEvent.getClientSettings().colorSettings();
            ColorSettings lastColorSettings = lastEvent.getClientSettings().colorSettings();;

            Vector3f targetColor = lastColorSettings.getGLSkyLightColor().copy();;
            float blend = lunarForecast.getBlend();
            targetColor.lerp(colorSettings.getGLSkyLightColor(), blend);
            skyVector.lerp(targetColor, (float) Mth.lerp(blend, lastColorSettings.getSkyLightBlendStrength(), colorSettings.getSkyLightBlendStrength()));
        }
    }
}