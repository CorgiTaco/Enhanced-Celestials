package corgitaco.enhancedcelestials.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Matrix4f;
import corgitaco.enhancedcelestials.client.ECWorldRenderer;
import net.minecraft.client.Camera;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LevelRenderer.class)
public abstract class MixinWorldRenderer {

    @Shadow
    @Final
    private static ResourceLocation MOON_LOCATION;

    @Inject(method = "renderSky", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/ClientLevel;getMoonPhase()I"))
    private void changeMoonColor(PoseStack $$0, Matrix4f $$1, float partialTicks, Camera $$3, boolean $$4, Runnable $$5, CallbackInfo ci) {
        ECWorldRenderer.changeMoonColor(partialTicks);
    }


    @Redirect(method = "renderSky", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderSystem;setShaderTexture(ILnet/minecraft/resources/ResourceLocation;)V", ordinal = 1))
    private void bindCustomMoonTexture(int moonTextureId, ResourceLocation resourceLocation) {
        ECWorldRenderer.bindMoonTexture(moonTextureId, MOON_LOCATION);
    }


    @ModifyConstant(method = "renderSky", constant = @Constant(floatValue = 20.0F))
    private float getSuperMoonSize(float ogSize) {
        return ECWorldRenderer.getMoonSize(ogSize);
    }
}