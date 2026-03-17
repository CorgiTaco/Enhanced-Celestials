package dev.corgitaco.enhancedcelestials.mixin.client;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.blaze3d.systems.RenderSystem;
import dev.corgitaco.enhancedcelestials.EnhancedCelestials;
import dev.corgitaco.enhancedcelestials.client.ECWorldRenderer;
import dev.corgitaco.enhancedcelestials.lunarevent.EnhancedCelestialsLunarForecastWorldData;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;

@Mixin(LevelRenderer.class)
public abstract class MixinWorldRenderer {

    @Inject(method = "renderSky", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/ClientLevel;getMoonPhase()I"))
    private void changeMoonColor(Matrix4f frustumMatrix, Matrix4f projectionMatrix, float partialTick, Camera camera, boolean isFoggy, Runnable skyFogSetup, CallbackInfo ci) {
        ECWorldRenderer.changeMoonColor(partialTick);
    }


    @WrapOperation(method = "renderSky", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderSystem;setShaderTexture(ILnet/minecraft/resources/ResourceLocation;)V", ordinal = 1))
    private void bindCustomMoonTexture(int moonTextureId, ResourceLocation moonLocation, Operation<Void> original) {
        ClientLevel level = Minecraft.getInstance().level;
        Optional<EnhancedCelestialsLunarForecastWorldData> lunarForecastWorldData = EnhancedCelestials.lunarForecastWorldData(level);
        if (lunarForecastWorldData.isEmpty()) {
            original.call(moonTextureId, moonLocation);
        } else {
            EnhancedCelestialsLunarForecastWorldData data = lunarForecastWorldData.orElseThrow();
            RenderSystem.setShaderTexture(moonTextureId, data.currentLunarEvent().getClientSettings().moonTextureLocation());
        }
    }


    @ModifyExpressionValue(method = "renderSky", at = @At(value = "CONSTANT", args = "floatValue=20.0"))
    private float getSuperMoonSize(float original) {

        ClientLevel level = Minecraft.getInstance().level;
        Optional<EnhancedCelestialsLunarForecastWorldData> lunarForecastWorldData = EnhancedCelestials.lunarForecastWorldData(level);

        if (lunarForecastWorldData.isEmpty()) {
            return original;
        }

        EnhancedCelestialsLunarForecastWorldData data = lunarForecastWorldData.orElseThrow();
        return Mth.clampedLerp(data.lastLunarEvent().getClientSettings().moonSize(), data.currentLunarEvent().getClientSettings().moonSize(), data.getBlend());
    }
}