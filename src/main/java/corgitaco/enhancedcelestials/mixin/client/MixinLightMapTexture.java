package corgitaco.enhancedcelestials.mixin.client;

import corgitaco.enhancedcelestials.EnhancedCelestialsWorldData;
import corgitaco.enhancedcelestials.LunarContext;
import corgitaco.enhancedcelestials.api.client.ColorSettings;
import corgitaco.enhancedcelestials.api.lunarevent.LunarEvent;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(LightTexture.class)
public abstract class MixinLightMapTexture {
    @Inject(method = "updateLightmap", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/vector/Vector3f;lerp(Lnet/minecraft/util/math/vector/Vector3f;F)V", ordinal = 0), cancellable = true, locals = LocalCapture.CAPTURE_FAILHARD)
    private void doOurLightMap(float partialTicks, CallbackInfo ci, ClientWorld clientworld, float f, float f1, float f3, float f2, Vector3f skyVector) {
        LunarContext lunarContext = ((EnhancedCelestialsWorldData) clientworld).getLunarContext();
        if (lunarContext != null) {
            LunarEvent lastEvent = lunarContext.getLastEvent();
            LunarEvent currentEvent = lunarContext.getCurrentEvent();

            ColorSettings colorSettings = currentEvent.getClient().getColorSettings();
            ColorSettings lastColorSettings = lastEvent.getClient().getColorSettings();

            Vector3f targetColor = lastColorSettings.getGLSkyLightColor().copy();;
            targetColor.lerp(colorSettings.getGLSkyLightColor(), lunarContext.getStrength());
            skyVector.lerp(targetColor, (float) MathHelper.lerp(lunarContext.getStrength(), lastColorSettings.getSkyLightBlendStrength(), colorSettings.getSkyLightBlendStrength()));
        }
    }
}