package corgitaco.enhancedcelestials.mixin.client;

import com.mojang.math.Vector3f;
import corgitaco.enhancedcelestials.client.ECWorldRenderer;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.LightTexture;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(LightTexture.class)
public abstract class MixinLightMapTexture {
    @Inject(method = "updateLightTexture", at = @At(value = "INVOKE", target = "Lcom/mojang/math/Vector3f;lerp(Lcom/mojang/math/Vector3f;F)V", ordinal = 0), locals = LocalCapture.CAPTURE_FAILHARD)
    private void doOurLightMap(float partialTicks, CallbackInfo ci, ClientLevel clientLevel, float $$2, float $$4, float $$5, float $$6, float $$7, float $$11, float $$8, Vector3f skyVector) {
        ECWorldRenderer.eventLightMap(skyVector, partialTicks);
    }
}